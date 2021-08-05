package com.miscos.staffhandler.shiftmanagement.fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.miscos.staffhandler.shiftmanagement.Api;
import com.miscos.staffhandler.shiftmanagement.adapters.WifiAdapter;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonDataForWifi;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonResponse;
import com.fivemin.chief.nonetworklibrary.networkBroadcast.NoNet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/*Developed under Miscos
 * Developed by Nikhil
 * 15-09-2020
 * */
public class WifiManagement extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {


    private WifiManager wifiManager;
    private final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
    FloatingActionButton addWifi;
    RecyclerView wifiRecycler, wifiNamesRecyclerView;
    WifiAdapter wifiAdapter, existingWifiAdapter;
    List<Boolean> checkList = new ArrayList<>();
    String addWifiNames = "", gpsWifi = "N", alertOnHoliday = "N",employer_id,absentLeave="0",sal_management="Y",leave_management="N",show_sal_report="N";
    List<String> deviceList = new ArrayList<>();
    List<String> addWifiNamesList = new ArrayList<>();
    List<String> wifiNamesList = new ArrayList<>();
    List<ScanResult> wifiList = new ArrayList<>();
    public static CardView existingWifiCardView, optionsWifiCardView;
    SwipeRefreshLayout swipeRefreshLayout;
    GetJsonDataForWifi getJsonDataForWifi;
    LocationManager lm;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    RadioGroup wifiGpsRadioGroup, alertRadioGroup,rdgroupsalarymanagemnet,rdgroupleavemanagement,rdgroupsalreport;
    RadioButton wifiYes, wifiNo, alertYes, alertNo;
    Button save;
    PreferenceManager preferenceManager;
    public static ProgressDialog progressDialog;
    ImageView tvBack;
    public TextView tvNoData;
    // No Internet Dialog
    private NoNet mNoNet;
    private FragmentManager fm = null;
    Spinner spinabsentsetting;
    ImageView imgInfo;
    ArrayList<String> strings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //  ((MainActivity) SpecialManagement.this).toolbar.setTitle("Admin Settings");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wififragment);

        fm = getSupportFragmentManager();
        mNoNet = new NoNet();
        mNoNet.initNoNet(this, fm);
        preferenceManager =new PreferenceManager(WifiManagement.this);
        employer_id = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID);
        //Progress Bar
        progressDialog = new ProgressDialog(this);
        progressDialog = ProgressDialog.show(this, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.dismiss();


        tvBack =  findViewById(R.id.imBack);
        tvNoData =  findViewById(R.id.tvNodata);
        wifiManager = (WifiManager) WifiManagement.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        spinabsentsetting=findViewById(R.id.spinLeaveabsenttype);
       strings=new ArrayList<>();
        strings.add("0");
        strings.add("1");
        strings.add("2");
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,strings);
        spinabsentsetting.setAdapter(arrayAdapter);
        imgInfo=findViewById(R.id.imginfo);
        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final PrettyDialog prettyDialog = new PrettyDialog(WifiManagement.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Details")
                        .setMessage("00 - Settle this leave if paid leave is available\n" +
                                "01 - Do not settle with the available leaves and deduct salary amount of 01 day on every day absent\n" +
                                "02 - Do not settle with the available leaves and deduct salary amount of 02 day on every day absent")
                        .setIcon(R.drawable.info, R.color.primaryTextColor, null)
                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        lm = (LocationManager) WifiManagement.this.getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(WifiManagement.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(WifiManagement.this, "location turned off", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(WifiManagement.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
            }
        }
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(WifiManagement.this, "Turning WiFi ON...", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            WifiManagement.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        addWifi = findViewById(R.id.add_wifi);
        wifiNamesRecyclerView = findViewById(R.id.wifiNamesRecyclerView);
        existingWifiCardView = findViewById(R.id.existingWifiCardView);
        optionsWifiCardView = findViewById(R.id.optionsWifiCardView);
        optionsWifiCardView.setVisibility(View.VISIBLE);
        wifiGpsRadioGroup = findViewById(R.id.wifiGpsRadioGroup);
        alertRadioGroup = findViewById(R.id.alertRadioGroup);
        rdgroupsalarymanagemnet=findViewById(R.id.rdgroupSalManagement);
        rdgroupleavemanagement=findViewById(R.id.rdgroupleavemangnet);
        rdgroupsalreport=findViewById(R.id.rdgroupsalreport);
        wifiYes = findViewById(R.id.yesRadioButtonWifi);
        wifiNo = findViewById(R.id.noRadioButtonWifi);
        alertYes = findViewById(R.id.yesRadioButtonHoliday);
        alertNo = findViewById(R.id.noRadioButtonHoliday);
        save = findViewById(R.id.btnSave);
        wifiAdapter = new WifiAdapter(this, deviceList, checkList);
        wifiNamesRecyclerView.setLayoutManager(new LinearLayoutManager(WifiManagement.this));
        wifiNamesRecyclerView.addItemDecoration(new DividerItemDecoration(WifiManagement.this, DividerItemDecoration.VERTICAL));
        getWifiList();
        addWifi.setOnClickListener(this);
        save.setOnClickListener(this);
        wifiGpsRadioGroup.setOnCheckedChangeListener(this);
        alertRadioGroup.setOnCheckedChangeListener(this);
        rdgroupleavemanagement.setOnCheckedChangeListener(this);
        rdgroupsalreport.setOnCheckedChangeListener(this);

        rdgroupsalarymanagemnet.setOnCheckedChangeListener(this);

        //Back Button
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(WifiManagement.this,EmployerZone.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        mNoNet.RegisterNoNet();
        super.onResume();
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.btnSave:
                String wifiNames = "";
                for (int i = 0; i < wifiNamesList.size(); i++)
                {
                    if (wifiNames.matches("")) {
                        wifiNames = wifiNamesList.get(i);
                    } else {
                        wifiNames = wifiNames + "," + wifiNamesList.get(i);
                    }
                }
                if (wifiNames.matches("")&&gpsWifi.equalsIgnoreCase("Y"))
                {
                    Toast.makeText(WifiManagement.this, "No Wifi Added", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    (
                            Api.getClient()).setWifi("set_wifi_list", employer_id, wifiNames, gpsWifi, alertOnHoliday,spinabsentsetting.getSelectedItem().toString(),sal_management,leave_management,show_sal_report).enqueue(new Callback<GetJsonResponse>() {
                        @Override
                        public void onResponse(Call<GetJsonResponse> call, Response<GetJsonResponse> response) {
                            try {
                             //   getWifiList();
                                progressDialog.dismiss();
                                if(response.body().getErrorCode()==101)
                                {
                                    preferenceManager.setPreference(PreferenceManager.KEY_SALARY_MANAGEMENT_SETTING,sal_management);
                                    preferenceManager.setPreference(PreferenceManager.KEY_LEAVE_MANAGEMENT,leave_management);
                                    preferenceManager.setPreference(PreferenceManager.KEY_SALARY_REPORT_SETTING,show_sal_report);
                                    Toast.makeText(WifiManagement.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    //startActivity(new Intent(WifiManagement.this,EmployerZone.class));
                                    finish();
                                }
                                else
                                    Toast.makeText(WifiManagement.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<GetJsonResponse> call, Throwable t) {

                        }
                    });
                }
                break;
            case R.id.add_wifi:
                if (ActivityCompat.checkSelfPermission(WifiManagement.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            WifiManagement.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
                } else {
                    wifiManager.startScan();
                }
                final Dialog dialog = new Dialog(WifiManagement.this);
// Include dialog.xml file
                dialog.setContentView(R.layout.dialog_recycler);
                dialog.show();
                TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                wifiRecycler = dialog.findViewById(R.id.rv_description);
                 swipeRefreshLayout = dialog.findViewById(R.id.swipeRefresh);

                final Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                EditText searchField = dialog.findViewById(R.id.search_editText);
                heading.setText("Wifi List");
                btnYes.setText("Select");
                btnNo.setText("Cancel");
                wifiRecycler.setLayoutManager(new LinearLayoutManager(WifiManagement.this));
                wifiRecycler.addItemDecoration(new DividerItemDecoration(WifiManagement.this, DividerItemDecoration.VERTICAL));
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
                this.registerReceiver(receiverWifi, intentFilter);
                getWifi();
                swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                        android.R.color.holo_green_dark,
                        android.R.color.holo_orange_dark,
                        android.R.color.holo_blue_dark);

                swipeRefreshLayout.post(() -> {
                    swipeRefreshLayout.setRefreshing(true);
                    getWifi();

                });
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getWifi();
                    }
                });

                searchField.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        // filter your list from your input
                        filter(s.toString());
                        //you can use runnable postDelayed like 500 ms to delay search text
                    }
                });
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        dialog.dismiss();
                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        dialog.dismiss();

                        addWifiNames = "";
                        for (int i = 0; i < addWifiNamesList.size(); i++) {
                            if (addWifiNames.matches("")) {
                                addWifiNames = addWifiNamesList.get(i);
                            } else {
                                addWifiNames = addWifiNames + "," + addWifiNamesList.get(i);
                            }
                        }
                        if (addWifiNames.matches("")) {
                            Toast.makeText(WifiManagement.this, "Select Wifi First", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.show();
                            (Api.getClient()).setWifi("set_wifi_list", employer_id, addWifiNames, gpsWifi, alertOnHoliday,spinabsentsetting.getSelectedItem().toString(),sal_management,leave_management,show_sal_report).enqueue(new Callback<GetJsonResponse>() {
                                @Override
                                public void onResponse(Call<GetJsonResponse> call, Response<GetJsonResponse> response) {
                                    try {
                                        getWifiList();
                                        progressDialog.dismiss();
                                        Toast.makeText(WifiManagement.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<GetJsonResponse> call, Throwable t) {

                                }
                            });
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mNoNet.unRegisterNoNet();
        try {
            if(gpsWifi.equalsIgnoreCase("Y"))
            WifiManagement.this.unregisterReceiver(receiverWifi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getWifi() {
        swipeRefreshLayout.setRefreshing(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(WifiManagement.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(WifiManagement.this, "location turned off", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(WifiManagement.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
            } else {
                wifiManager.startScan();
                swipeRefreshLayout.setRefreshing(false);
            }
        } else {
            Toast.makeText(WifiManagement.this, "scanning", Toast.LENGTH_SHORT).show();
            wifiManager.startScan();
            swipeRefreshLayout.setRefreshing(false);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(WifiManagement.this, "permission granted", Toast.LENGTH_SHORT).show();
                    wifiManager.startScan();
                } else {
                    Toast.makeText(WifiManagement.this, "permission not granted", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
        }
    }

    BroadcastReceiver receiverWifi = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
                wifiList = wifiManager.getScanResults();
                deviceList = new ArrayList<>();
                checkList = new ArrayList<>();
                for (ScanResult scanResult : wifiList) {
                    deviceList.add(scanResult.SSID);
                    checkList.add(false);
                }
                wifiAdapter = new WifiAdapter(context, deviceList, checkList);
                wifiAdapter.ClickListener(new WifiAdapter.OnButtonClick() {
                    @Override
                    public void addButtonClick(String wifi, boolean check) {
                        if (check) {
                            addWifiNamesList.add(wifi);
                        } else {
                            addWifiNamesList.remove(wifi);
                            optionsWifiCardView.setVisibility(View.VISIBLE);
                        }
                    }
                });
                wifiRecycler.setAdapter(wifiAdapter);
            }
        }
    };

    void getWifiList()
    {
        progressDialog.show();
        (Api.getClient()).getWifi("fetch", employer_id).enqueue(new Callback<GetJsonDataForWifi>()
        {
            @Override
            public void onResponse(Call<GetJsonDataForWifi> call, Response<GetJsonDataForWifi> response)
            {
                try {
                    getJsonDataForWifi = response.body();
                    if (getJsonDataForWifi.getErrorCode() == 101)
                    {
                        wifiNamesList = new ArrayList<>();
                        progressDialog.dismiss();
                        existingWifiCardView.setVisibility(View.VISIBLE);

                        tvNoData.setVisibility(View.GONE);
                        if (getJsonDataForWifi.getWifiNames().contains(","))
                        {
                            String sep[] = getJsonDataForWifi.getWifiNames().split(",");
                            for (int i = 0; i < sep.length; i++)
                            {
                                if(!sep[i].isEmpty())
                                wifiNamesList.add(sep[i]);
                            }
                        } else if(!getJsonDataForWifi.getWifiNames().isEmpty())
                            {

                            wifiNamesList.add(getJsonDataForWifi.getWifiNames());
                        }
                        existingWifiAdapter = new WifiAdapter(WifiManagement.this, wifiNamesList);
                        wifiNamesRecyclerView.setAdapter(existingWifiAdapter);
                        if(wifiNamesList.size()==0)
                            tvNoData.setVisibility(View.VISIBLE);
                        if (getJsonDataForWifi.getGpsWithWifi().matches("Y"))
                        {
                            wifiGpsRadioGroup.check(R.id.yesRadioButtonWifi);
                            gpsWifi = "Y";

                        } else {
                            wifiGpsRadioGroup.check(R.id.noRadioButtonWifi);
                            gpsWifi = "N";
                        }

                        if (getJsonDataForWifi.getAlertOnHoliday().matches("Y"))
                        {
                            alertRadioGroup.check(R.id.yesRadioButtonHoliday);
                            alertOnHoliday = "Y";
                        } else
                            {
                            alertRadioGroup.check(R.id.noRadioButtonHoliday);
                            alertOnHoliday = "N";
                        }
                        //added by nikita on 19-01-2021
                       spinabsentsetting.setSelection(strings.indexOf(getJsonDataForWifi.getAbsent_leave_deduction()));
                        absentLeave=getJsonDataForWifi.getAbsent_leave_deduction();
                        if (getJsonDataForWifi.getEnable_leave_management().matches("Y"))
                        {
                            rdgroupleavemanagement.check(R.id.rdbtlevYes);
                            leave_management = "Y";
                        } else
                        {
                            rdgroupleavemanagement.check(R.id.rdbtnlevNo);
                            leave_management = "N";
                        }
                        if (getJsonDataForWifi.getEnable_salary_management().matches("Y"))
                        {
                            rdgroupsalarymanagemnet.check(R.id.rdbtnsalYes);
                            sal_management = "Y";
                        } else
                        {
                            rdgroupsalarymanagemnet.check(R.id.rdbtnsalNo);
                            sal_management = "N";
                        }
                        if (getJsonDataForWifi.getEnable_show_salary_detail().matches("Y"))
                        {
                            rdgroupsalreport.check(R.id.rdbtsalreportYes);
                            show_sal_report = "Y";
                        } else
                        {
                            rdgroupsalreport.check(R.id.rdbtnsalreportNo);
                            show_sal_report = "N";
                        }
                        save.setVisibility(View.VISIBLE);
                    } else {
                        progressDialog.dismiss();
                        wifiGpsRadioGroup.check(R.id.noRadioButtonWifi);
                        alertRadioGroup.check(R.id.noRadioButtonHoliday);
                        gpsWifi = "N";
                        alertOnHoliday = "N";
                        tvNoData.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    wifiGpsRadioGroup.check(R.id.noRadioButtonWifi);
                    alertRadioGroup.check(R.id.noRadioButtonHoliday);
                }
            }

            @Override
            public void onFailure(Call<GetJsonDataForWifi> call, Throwable t) {

            }
        });
    }


    void filter(String text) {
        List<String> temp = new ArrayList();
        for (String d : wifiNamesList) {
            if (d.contains(text)) {
                temp.add(d);
            }
        }
        //update recyclerview
        wifiAdapter.updateList(temp);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.wifiGpsRadioGroup:
                if (checkedId == R.id.yesRadioButtonWifi) {

                    gpsWifi = "Y";
                } else {

                    gpsWifi = "N";
                }
                break;
            case R.id.alertRadioGroup:
                if (checkedId == R.id.yesRadioButtonHoliday)
                {

                    alertOnHoliday = "Y";
                } else {

                    alertOnHoliday = "N";
                }
                break;
            case R.id.rdgroupSalManagement:
                if (checkedId == R.id.rdbtnsalYes) {

                    sal_management = "Y";
                } else {

                    sal_management = "N";
                }
            case R.id.rdgroupleavemangnet:
                if (checkedId == R.id.rdbtnlevNo) {

                    leave_management = "N";
                } else {

                    leave_management = "Y";
                }
            case R.id.rdgroupsalreport:
                if (checkedId == R.id.rdbtnsalreportNo) {

                    show_sal_report = "N";
                } else {

                    show_sal_report = "Y";
                }
                break;
        }
    }
    //backpress
    protected void exitByBackKey()
    {

        final Dialog dialog = new Dialog(WifiManagement.this);
// Include dialog.xml file
        dialog.setContentView(R.layout.dialog_exit);
        dialog.show();

        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //Intent intent = new Intent(WifiManagement.this, EmployerZone.class);
                //startActivity(intent);
                finish();
            }
        });

// if decline button is clicked, close the custom dialog
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });

    }
    @Override
    public void onBackPressed() {
        exitByBackKey();
    }
}