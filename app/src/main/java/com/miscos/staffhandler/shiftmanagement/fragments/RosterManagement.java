package com.miscos.staffhandler.shiftmanagement.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.miscos.staffhandler.Adapters.TextAdapter;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.activities.EmployerZone;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.miscos.staffhandler.shiftmanagement.Api;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonDataForEmployee;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonDataForShift;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonResponse;
import com.miscos.staffhandler.shiftmanagement.models.Roster;
import com.miscos.staffhandler.shiftmanagement.models.Shift;
import com.fivemin.chief.nonetworklibrary.networkBroadcast.NoNet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*Developed under Miscos
 * Developed by Nikhil
 * 15-09-2020
 * */
public class RosterManagement extends AppCompatActivity implements View.OnClickListener {

    LayerDrawable shape, shape1, shape2;
    GradientDrawable gradientDrawable, gradientDrawable1, gradientDrawable2;
    AutoCompleteTextView shift;
    EditText employeeName, employeeId;
    ArrayList<String> employeeNameList = new ArrayList<>();
    ArrayList<String> employeeIdList = new ArrayList<>();
    List<String> daylist = new ArrayList<>();
    TextAdapter adapter,adapter1;
    List<String> shiftList = new ArrayList<>();
    Handler handler;
    CardView cardViewCurrentArrangement,cardViewnewArrangement;
    LinearLayout appliedfromlay;
    GetJsonDataForEmployee getJsonDataForEmployee;
    Button btnGo, btnSave;
    Roster roster;
    List<String> employeeIdList2 = new ArrayList<>();
    PreferenceManager preferenceManager;
    Button newArrangementText;
    TextView textSun, textMon, textTue, textWed, textThu, textFri, textSat, actionText,  applyFromDate,txtemployeeidhidden;
    TextView editTextSun, editTextMon, editTextTue, editTextWed, editTextThu, editTextFri, editTextSat;
    boolean editArrangement = false;
    ArrayAdapter<String> shiftAdapter;
    Spinner spinshift;
    List<String> noOfShifts1 = new ArrayList<>();
    int mYear, mMonth, mDay, totalShift = 0;
    Date from, currentDate, currentTime, fromTime, extraTime;
    String date, time, date1, oldList = "", newList,employer_id,new_arrangement="";
    SimpleDateFormat simpleDateFormat, simpleDateFormat1, simpleDateFormat2,simpleDateFormat4;
    Calendar calendar;
    GetJsonDataForShift getJsonDataForShift;
    Shift shiftData;
    private ProgressDialog progressDialog;
    ImageView tvBack;
    // No Internet Dialog
    private NoNet mNoNet;
    private FragmentManager fm = null;
    boolean flag=false;
    SpinnerDialog spinnerDialog1,spinnerDialog2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //  ((MainActivity) RosterManagement.this).toolbar.setTitle("Admin Settings");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rostermanagment);

        fm = getSupportFragmentManager();
        mNoNet = new NoNet();
        mNoNet.initNoNet(this, fm);

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
        preferenceManager =new PreferenceManager(RosterManagement.this);
        employer_id = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID);
        txtemployeeidhidden=findViewById(R.id.txtemployeehiddenid);
     //   ((MainActivity) RosterManagement.this).toolbar.setTitle("Roster Management");
     //   progressBar = ((MainActivity) RosterManagement.this).progressBar;
        handler = new Handler(Looper.getMainLooper());
        tvBack = findViewById(R.id.imBack);
        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        simpleDateFormat2 = new SimpleDateFormat("HH:mm");
        simpleDateFormat1 = new SimpleDateFormat("HH:mm:ss");
        date = simpleDateFormat.format(calendar.getTime());
        time = simpleDateFormat2.format(calendar.getTime());
        try {
            currentTime = simpleDateFormat2.parse(time);
            currentDate = simpleDateFormat.parse(date);
            String current = simpleDateFormat2.format(currentTime);
            Log.e("Current Time", current);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Back Button
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        shape = (LayerDrawable) ContextCompat.getDrawable(RosterManagement.this, R.drawable.border1);
        gradientDrawable = (GradientDrawable) shape.findDrawableByLayerId(R.id.shape);
        gradientDrawable.setColor(ContextCompat.getColor(RosterManagement.this, R.color.red));

        shape2 = (LayerDrawable) ContextCompat.getDrawable(RosterManagement.this, R.drawable.border1);
        gradientDrawable2 = (GradientDrawable) shape2.findDrawableByLayerId(R.id.shape);
        gradientDrawable2.setColor(ContextCompat.getColor(RosterManagement.this, R.color.green_500));

        shape1 = (LayerDrawable) ContextCompat.getDrawable(RosterManagement.this, R.drawable.border1);
        gradientDrawable1 = (GradientDrawable) shape1.findDrawableByLayerId(R.id.shape);
        gradientDrawable1.setColor(ContextCompat.getColor(RosterManagement.this, R.color.transparent));

      /*  ((MainActivity)RosterManagement.this).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)RosterManagement.this).getSupportActionBar().setDisplayShowHomeEnabled(true);*/
        employeeName = findViewById(R.id.employeeName);
        employeeId = findViewById(R.id.employeeId);
        spinshift=findViewById(R.id.spinshift);
spinshift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
{
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {

        if(!flag)
        {
            return;
        }
        (Api.getClient()).getEmployeeList("fetch", employer_id, spinshift.getSelectedItemPosition()+"", "employee","shift").enqueue(new Callback<GetJsonDataForEmployee>() {
            @Override
            public void onResponse(Call<GetJsonDataForEmployee> call, Response<GetJsonDataForEmployee> response)
            {
                try {
                    getJsonDataForEmployee = response.body();
                    if (getJsonDataForEmployee.getErrorCode() == 101)
                    {
                        if(getJsonDataForEmployee.getEmployeeData().size()==0)
                        {
                            final PrettyDialog prettyDialog = new PrettyDialog(RosterManagement.this);
                            prettyDialog.setCancelable(false);
                            prettyDialog
                                    .setMessage("No employees found for this shift")
                                    .setIcon(R.drawable.info, R.color.white, null);

                            prettyDialog.addButton("Ok", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick()
                                {

                                    prettyDialog.dismiss();
                                    employeeId.setText("");
                                    employeeName.setText("");
                                    cardViewCurrentArrangement.setVisibility(View.GONE);
                                    cardViewnewArrangement.setVisibility(View.GONE);

                                    appliedfromlay.setVisibility(View.GONE);
                                    /*employeeNameList.clear();
                                    employeeIdList.clear();
                                    employeeIdList2.clear();
                                    adapter.notifyDataSetChanged();
                                    adapter1.notifyDataSetChanged();*/

                                }
                            });

                            prettyDialog.show();
                        }
                        else
                        {

                         /*   cardViewCurrentArrangement.setVisibility(View.VISIBLE);
                            cardViewnewArrangement.setVisibility(View.VISIBLE);
                            applyFromDate.setVisibility(View.VISIBLE);
                            btnSave.setVisibility(View.VISIBLE);*/
                        }
                        employeeNameList.clear();
                        employeeIdList.clear();
                        employeeIdList2.clear();
                        for (int i = 0; i < getJsonDataForEmployee.getEmployeeData().size(); i++)
                        {
                            employeeNameList.add(getJsonDataForEmployee.getEmployeeData().get(i).getName());
                            employeeIdList.add(getJsonDataForEmployee.getEmployeeData().get(i).getEmployeeNo());
                            employeeIdList2.add(getJsonDataForEmployee.getEmployeeData().get(i).getEmployeeId());
                        }



                    } else {
                        progressDialog.dismiss();
                        final PrettyDialog prettyDialog = new PrettyDialog(RosterManagement.this);
                        prettyDialog.setCancelable(false);
                        prettyDialog
                                .setMessage("No shift employees found for this shift")
                                .setIcon(R.drawable.info, R.color.white, null);

                        prettyDialog.addButton("Ok", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick()
                            {
                                employeeId.setText("");
                                employeeName.setText("");
                                cardViewCurrentArrangement.setVisibility(View.GONE);
                                cardViewnewArrangement.setVisibility(View.GONE);
                                prettyDialog.dismiss();


                            }
                        });

                        prettyDialog.show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetJsonDataForEmployee> call, Throwable t)
            {
                final PrettyDialog prettyDialog = new PrettyDialog(RosterManagement.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setMessage("No Shift Employee Found")
                        .setIcon(R.drawable.info, R.color.white, null);

                prettyDialog.addButton("Ok", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                    @Override
                    public void onClick()
                    {

                        prettyDialog.dismiss();


                    }
                });

                prettyDialog.show();
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {

    }
});
        newArrangementText = findViewById(R.id.newArrangementText);
        cardViewCurrentArrangement = findViewById(R.id.cardViewCurrentArrangement);
        cardViewnewArrangement = findViewById(R.id.cardViewnewArrangement);
        appliedfromlay = findViewById(R.id.appliedfromlay);
        btnGo = findViewById(R.id.btnGo);
        btnSave = findViewById(R.id.btnSave);
        textSun = findViewById(R.id.textViewSun);
        textMon = findViewById(R.id.textViewMon);
        textTue = findViewById(R.id.textViewTue);
        textWed = findViewById(R.id.textViewWed);
        textThu = findViewById(R.id.textViewThu);
        textFri = findViewById(R.id.textViewFri);
        textSat = findViewById(R.id.textViewSat);
        editTextSun = findViewById(R.id.editTextSun);
        editTextMon = findViewById(R.id.editTextMon);
        editTextTue = findViewById(R.id.editTextTue);
        editTextWed = findViewById(R.id.editTextWed);
        editTextThu = findViewById(R.id.editTextThu);
        editTextFri = findViewById(R.id.editTextFri);
        editTextSat = findViewById(R.id.editTextSat);
        actionText = findViewById(R.id.actionTextClearnewArrangement);
        applyFromDate = findViewById(R.id.applyFromDate);
        shift = findViewById(R.id.applyFromShift);

     /*   adapter = new TextAdapter
                (RosterManagement.this, android.R.layout.simple_list_item_1, R.id.txtid,employeeNameList);
        adapter1= new TextAdapter
                (RosterManagement.this, android.R.layout.simple_list_item_1, R.id.txtid,employeeIdList);
        employeeName.setAdapter(adapter);
        employeeName.setThreshold(0);
        employeeId.setAdapter(adapter1);
        employeeId.setThreshold(0);*/
        spinnerDialog1=new SpinnerDialog(RosterManagement.this,employeeNameList,"Select or Search Employee",R.style.DialogAnimations_SmileWindow,"close");// With 	Animation

        spinnerDialog1.setCancellable(true); // for cancellable
        spinnerDialog1.setShowKeyboard(false);// for open keyboard by default


        employeeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(employeeNameList.size()==0)
                {
                    Toast.makeText(RosterManagement.this, "No employee found", Toast.LENGTH_SHORT).show();
                    return;
                }
                spinnerDialog1=new SpinnerDialog(RosterManagement.this,employeeNameList,"Select or Search Employee",R.style.DialogAnimations_SmileWindow,"close");// With 	Animation

                spinnerDialog1.setCancellable(true); // for cancellable
                spinnerDialog1.setShowKeyboard(false);// for open keyboard by default
                spinnerDialog1.showSpinerDialog();
                spinnerDialog1.bindOnSpinerListener(new OnSpinerItemClick()
                {
                    @Override
                    public void onClick(String item, int position)
                    {
                        //Toast.makeText(UserRegistrationActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                        int pos = employeeNameList.indexOf(item);
                        if(pos!=-1)
                        {
                            employeeId.setText(employeeIdList.get(pos));
                            txtemployeeidhidden.setText(employeeIdList2.get(pos));
                            employeeName.setText(item);

                        }

                    }

                });
            }
        });


        spinnerDialog2=new SpinnerDialog(RosterManagement.this,employeeIdList,"Select or Search Employee",R.style.DialogAnimations_SmileWindow,"close");// With 	Animation

        spinnerDialog2.setCancellable(true); // for cancellable
        spinnerDialog2.setShowKeyboard(false);// for open keyboard by default
        employeeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(employeeIdList.size()==0)
                {
                    Toast.makeText(RosterManagement.this, "No employee found", Toast.LENGTH_SHORT).show();
                    return;
                }
                spinnerDialog2=new SpinnerDialog(RosterManagement.this,employeeIdList,"Select or Search Employee",R.style.DialogAnimations_SmileWindow,"close");// With 	Animation

                spinnerDialog2.setCancellable(true); // for cancellable
                spinnerDialog2.setShowKeyboard(false);// for open keyboard by default
                spinnerDialog2.showSpinerDialog();
                spinnerDialog2.bindOnSpinerListener(new OnSpinerItemClick()
                {
                    @Override
                    public void onClick(String item, int position)
                    {
                        int pos1 = employeeIdList.indexOf(item);
                        if(pos1!=-1)
                        {
                            employeeName.setText(employeeNameList.get(pos1));
                            txtemployeeidhidden.setText(employeeIdList2.get(pos1));
                            employeeId.setText(item);
                        }

                    }

                });

            }
        });





        handler.postDelayed(new Runnable()
        {
            @Override
            public void run() {
                progressDialog.show();
                (Api.getClient()).getShiftData("show", employer_id).enqueue(new Callback<GetJsonDataForShift>()
                {
                    @Override
                    public void onResponse(Call<GetJsonDataForShift> call, Response<GetJsonDataForShift> response) {
                        getJsonDataForShift = response.body();
                        if (getJsonDataForShift.getErrorCode() == 101)
                        {
                            try {
                                shiftData = getJsonDataForShift.getShiftData();
                                totalShift = Integer.parseInt(shiftData.getShiftCount());
                                if(totalShift==0)
                                {   //Toast.makeText(RosterManagement.this, "", Toast.LENGTH_SHORT).show();
                                    final PrettyDialog prettyDialog = new PrettyDialog(RosterManagement.this);
                                    prettyDialog.setCancelable(false);
                                    prettyDialog
                                            .setMessage("No shifts configured")
                                            .setIcon(R.drawable.info, R.color.white, null);

                                    prettyDialog.addButton("Configure Shift", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick()
                                        {

                                            startActivity(new Intent(RosterManagement.this,ShiftManagement.class));
                                            finish();
                                            prettyDialog.dismiss();


                                        }
                                    });
                                    prettyDialog.addButton("Cancel", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick()
                                        {


                                            prettyDialog.dismiss();
                                            finish();


                                        }
                                    });

                                    prettyDialog.show();

                                    progressDialog.dismiss();}

                                else
                                {
                                    ArrayList<String> shiftNames=new ArrayList<>();
                                    shiftNames.add("Select Shift");
                                    for (int i = 1; i <= totalShift; i++)
                                    {
                                        noOfShifts1.add(String.valueOf(i));
                                        switch (i)
                                        {
                                            case 1:
                                                shiftNames.add(shiftData.getShift1name());
                                                break;
                                            case 2:
                                                shiftNames.add(shiftData.getShift2name());
                                                break;
                                            case 3:
                                                shiftNames.add(shiftData.getShift3name());
                                                break;
                                        }


                                    }
                                    shiftAdapter = new ArrayAdapter<String>(RosterManagement.this, R.layout.spinner_item1, R.id.spinnerText1, shiftNames);
                                    spinshift.setAdapter(shiftAdapter);

                                    progressDialog.dismiss();
                                    //check for shift employee
                                    (Api.getClient()).getEmployeeList("fetch", employer_id, "0", "employee","shift").enqueue(new Callback<GetJsonDataForEmployee>() {
                                        @Override
                                        public void onResponse(Call<GetJsonDataForEmployee> call, Response<GetJsonDataForEmployee> response)
                                        {
                                            try {
                                                getJsonDataForEmployee = response.body();
                                                flag=true;
                                                if (getJsonDataForEmployee.getErrorCode() == 101)
                                                {
                                                    employeeIdList.clear();
                                                    employeeNameList.clear();
                                                    employeeIdList2.clear();

                                                    if(getJsonDataForEmployee.getEmployeeData().size()==0)
                                                    {
                                                        final PrettyDialog prettyDialog = new PrettyDialog(RosterManagement.this);
                                                        prettyDialog.setCancelable(false);
                                                        prettyDialog
                                                                .setMessage("No Shift Employee Found")
                                                                .setIcon(R.drawable.info, R.color.white, null);

                                                        prettyDialog.addButton("Ok", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                                            @Override
                                                            public void onClick()
                                                            {

                                                               finish();
                                                                prettyDialog.dismiss();


                                                            }
                                                        });
                                                    }
                                                    for (int i = 0; i < getJsonDataForEmployee.getEmployeeData().size(); i++)
                                                    {
                                                        employeeNameList.add(getJsonDataForEmployee.getEmployeeData().get(i).getName());
                                                        employeeIdList.add(getJsonDataForEmployee.getEmployeeData().get(i).getEmployeeNo());
                                                        employeeIdList2.add(getJsonDataForEmployee.getEmployeeData().get(i).getEmployeeId());
                                                    }
                                                    adapter.notifyDataSetChanged();
                                                    adapter1.notifyDataSetChanged();
                                                } else {
                                                    progressDialog.dismiss();
                                                    final PrettyDialog prettyDialog = new PrettyDialog(RosterManagement.this);
                                                    prettyDialog.setCancelable(false);
                                                    prettyDialog
                                                            .setMessage("No Shift Employee Found")
                                                            .setIcon(R.drawable.info, R.color.white, null);

                                                    prettyDialog.addButton("Ok", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                                        @Override
                                                        public void onClick()
                                                        {

                                                            finish();
                                                            prettyDialog.dismiss();


                                                        }
                                                    });

                                                    prettyDialog.show();

                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<GetJsonDataForEmployee> call, Throwable t)
                                        {

                                        }
                                    });
                                }

                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        } else {
                                 progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetJsonDataForShift> call, Throwable t) {

                    }
                });


            }
        }, 1000);

        btnGo.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        actionText.setOnClickListener(this);
        newArrangementText.setOnClickListener(this);
        editTextSun.setOnClickListener(this);
        editTextMon.setOnClickListener(this);
        editTextTue.setOnClickListener(this);
        editTextWed.setOnClickListener(this);
        editTextThu.setOnClickListener(this);
        editTextFri.setOnClickListener(this);
        editTextSat.setOnClickListener(this);
        applyFromDate.setOnClickListener(this);
      /*  spinshift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                //check for shift employee
                (Api.getClient()).getEmployeeList("fetch", employer_id, spinshift.getSelectedItemPosition()+"", "employee","shift").enqueue(new Callback<GetJsonDataForEmployee>() {
                    @Override
                    public void onResponse(Call<GetJsonDataForEmployee> call, Response<GetJsonDataForEmployee> response)
                    {
                        try {
                            getJsonDataForEmployee = response.body();
                            if (getJsonDataForEmployee.getErrorCode() == 101)
                            {
                                employeeNameList.clear();
                                employeeIdList.clear();
                                employeeIdList2.clear();

                                if(getJsonDataForEmployee.getEmployeeData().size()==0)
                                {
                                    final PrettyDialog prettyDialog = new PrettyDialog(RosterManagement.this);
                                    prettyDialog.setCancelable(false);
                                    prettyDialog
                                            .setMessage("No employee found this shift")
                                            .setIcon(R.drawable.info, R.color.white, null);

                                    prettyDialog.addButton("Ok", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick()
                                        {
                                            prettyDialog.dismiss();


                                        }
                                    });
                                }
                                for (int i = 0; i < getJsonDataForEmployee.getEmployeeData().size(); i++)
                                {
                                    employeeNameList.add(getJsonDataForEmployee.getEmployeeData().get(i).getName());
                                    employeeIdList.add(getJsonDataForEmployee.getEmployeeData().get(i).getEmployeeNo());
                                    employeeIdList2.add(getJsonDataForEmployee.getEmployeeData().get(i).getEmployeeId());
                                }
                                adapter1.notifyDataSetChanged();
                                adapter.notifyDataSetChanged();

                            } else {
                                Toast.makeText(RosterManagement.this, "No shift employees found", Toast.LENGTH_SHORT).show();

                                progressDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetJsonDataForEmployee> call, Throwable t)
                    {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
    }
    @Override
    protected void onResume() {
        mNoNet.RegisterNoNet();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mNoNet.unRegisterNoNet();
        super.onPause();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                if (editTextSun.getText().toString().matches("H") && editTextMon.getText().toString().matches("H") && editTextTue.getText().toString().matches("H") && editTextWed.getText().toString().matches("H") && editTextThu.getText().toString().matches("H") && editTextFri.getText().toString().matches("H") && editTextFri.getText().toString().matches("H")) {
                    Toast.makeText(RosterManagement.this, "Atleast One day should be working", Toast.LENGTH_SHORT).show();
                } else {
                    if (editTextSun.getText().toString().matches("") || editTextMon.getText().toString().matches("") || editTextTue.getText().toString().matches("") || editTextWed.getText().toString().matches("") || editTextThu.getText().toString().matches("") || editTextFri.getText().toString().matches("") || editTextFri.getText().toString().matches("") || employeeId.getText().toString().matches("") || shift.getText().toString().matches("") || applyFromDate.getText().toString().matches("")) {
                        if (editTextSun.getText().toString().matches("") || editTextMon.getText().toString().matches("") || editTextTue.getText().toString().matches("") || editTextWed.getText().toString().matches("") || editTextThu.getText().toString().matches("") || editTextFri.getText().toString().matches("") || editTextFri.getText().toString().matches("")) {
                            Toast.makeText(RosterManagement.this, "Add Schedule for All days", Toast.LENGTH_SHORT).show();
                       return;
                        }
                        if (employeeId.getText().toString().matches("")) {
                            Toast.makeText(RosterManagement.this, "Select Employee", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (shift.getText().toString().matches("")) {
                            Toast.makeText(RosterManagement.this, "Select Shift", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (applyFromDate.getText().toString().matches("")) {
                            Toast.makeText(RosterManagement.this, "Select Date", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        if (Integer.parseInt(shift.getText().toString()) > totalShift) {
                            Toast.makeText(RosterManagement.this, "Select Valid Shift", Toast.LENGTH_SHORT).show();
                        } else {
                            final Dialog dialog = new Dialog(RosterManagement.this);
// Include dialog.xml file
                            dialog.setContentView(R.layout.dialog_exit);
                            dialog.show();

                            TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                            TextView description = dialog.findViewById(R.id.tv_description);
                            Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                            Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                            heading.setText("Save");
                            description.setText("Are You Sure to save details?");
                            btnYes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    Log.e("Shift", shift.getText().toString());
                                    if (shift.getText().toString().matches("1")) {
                                        try {
                                            extraTime = simpleDateFormat1.parse(shiftData.getSec1InTiming());
                                            String time1 = simpleDateFormat2.format(extraTime);
                                            fromTime = simpleDateFormat2.parse(time1);
                                            Log.e("InShift", time1 + " " + shift.getText().toString());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (shift.getText().toString().matches("2")) {
                                        try {
                                            extraTime = simpleDateFormat1.parse(shiftData.getSec2InTiming());
                                            String time1 = simpleDateFormat2.format(extraTime);
                                            fromTime = simpleDateFormat2.parse(time1);
                                            Log.e("InShift", time1 + " " + shift.getText().toString());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (shift.getText().toString().matches("3")) {
                                        try {
                                            extraTime = simpleDateFormat1.parse(shiftData.getSec3InTiming());
                                            String time1 = simpleDateFormat2.format(extraTime);
                                            fromTime = simpleDateFormat2.parse(time1);
                                            Log.e("InShift", time1 + " " + shift.getText().toString());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    newList = "sun:" + editTextSun.getText().toString() + ",mon:" + editTextMon.getText().toString() + ",tue:" + editTextTue.getText().toString() + ",wed:" + editTextWed.getText().toString() + ",thu:" + editTextThu.getText().toString() + ",fri:" + editTextFri.getText().toString() + ",sat:" + editTextSat.getText().toString();
                                    Log.e("NEWLIST", newList);
                                    if (currentDate.equals(from)) {
                                        if (currentTime.after(fromTime)) {
                                            Toast.makeText(RosterManagement.this, "Cannot assign past shift", Toast.LENGTH_SHORT).show();
                                        } else {
                                            progressDialog.show();
                                            (Api.getClient()).setRoster("update", employer_id, txtemployeeidhidden.getText().toString(), oldList, newList, applyFromDate.getText().toString(), shift.getText().toString()).enqueue(new Callback<GetJsonResponse>() {
                                                @Override
                                                public void onResponse(Call<GetJsonResponse> call, Response<GetJsonResponse> response) {
                                                    try {
                                                        if (response.body().getErrorCode() == 101) {
                                                            progressDialog.dismiss();
                                                           // startActivity(new Intent(RosterManagement.this, EmployerZone.class));
                                                            finish();
                                                            Toast.makeText(RosterManagement.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(RosterManagement.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
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
                                    else {
                                        if (currentDate.after(from)) {
                                            Toast.makeText(RosterManagement.this, "Cannot assign past shift", Toast.LENGTH_SHORT).show();
                                        } else {
                                            (Api.getClient()).setRoster("update", employer_id, txtemployeeidhidden.getText().toString(), oldList, newList, applyFromDate.getText().toString(), shift.getText().toString()).enqueue(new Callback<GetJsonResponse>() {
                                                @Override
                                                public void onResponse(Call<GetJsonResponse> call, Response<GetJsonResponse> response) {
                                                    try {
                                                        if (response.body().getErrorCode() == 101) {
                                                           // startActivity(new Intent(RosterManagement.this,EmployerZone.class));
                                                            finish();
                                                            Toast.makeText(RosterManagement.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(RosterManagement.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
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
                                }
                            });
                            btnNo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                }
                break;
            case R.id.applyFromDate:
                DatePickerDialog datePickerDialog = new DatePickerDialog(RosterManagement.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                try {
                                    String codate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                    Log.e("Date", codate);
                                    date1 = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                    from = simpleDateFormat.parse(codate);
                                    applyFromDate.setText(codate);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
            case R.id.actionText:
                if (editArrangement)
                {
                    Toast.makeText(this, "testt", Toast.LENGTH_SHORT).show();
                    editTextSun.setText("");
                    editTextMon.setText("");
                    editTextTue.setText("");
                    editTextWed.setText("");
                    editTextThu.setText("");
                    editTextFri.setText("");
                    editTextSat.setText("");
                }
                else
                {
                    Toast.makeText(this, "Click on New Schedule to edit", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.editTextSun:
                if (editArrangement) {
                    final Dialog dialog = new Dialog(RosterManagement.this);
// Include dialog.xml file
                    dialog.setContentView(R.layout.dialog_text_spinner);
                    dialog.show();
                    TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                    TextView description = dialog.findViewById(R.id.tv_description);
                    final Spinner spinner = dialog.findViewById(R.id.shiftNoSpinner);
                    Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                    Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                    heading.setText("Sunday");
                    description.setText("Select Shift or Mark Holiday");
                    btnYes.setText("Assign Shift");
                    btnNo.setText("Mark Holiday");
                    spinner.setAdapter(shiftAdapter);
                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            editTextSun.setText("H");
                            editTextSun.setBackground(shape);
                        }
                    });
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(spinner.getSelectedItemPosition()==0)
                            {
                                Toast.makeText(RosterManagement.this, "Please select shift", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            dialog.dismiss();
                            editTextSun.setText(spinner.getSelectedItemPosition()+"");
                            editTextSun.setBackground(shape1);
                        }
                    });
                }
                else
                {
                    Toast.makeText(this, "Click on New Schedule to edit", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.editTextMon:
                if (editArrangement)
                {
                    final Dialog dialog = new Dialog(RosterManagement.this);
// Include dialog.xml file
                    dialog.setContentView(R.layout.dialog_text_spinner);
                    dialog.show();
                    TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                    TextView description = dialog.findViewById(R.id.tv_description);
                    final Spinner spinner = dialog.findViewById(R.id.shiftNoSpinner);
                    Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                    Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                    heading.setText("Monday");
                    description.setText("Select Shift or Mark Holiday");
                    btnYes.setText("Assign Shift");
                    btnNo.setText("Mark Holiday");
                    spinner.setAdapter(shiftAdapter);
                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            editTextMon.setText("H");
                            editTextMon.setBackground(shape);
                        }
                    });
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(spinner.getSelectedItemPosition()==0)
                            {
                                Toast.makeText(RosterManagement.this, "Please select shift", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            dialog.dismiss();
                            editTextMon.setText(spinner.getSelectedItemPosition()+"");
                            editTextMon.setBackground(shape1);
                        }
                    });
                }
                else
                {
                    Toast.makeText(this, "Click on New Schedule to edit", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.editTextTue:
                if (editArrangement) {
                    final Dialog dialog = new Dialog(RosterManagement.this);
// Include dialog.xml file
                    dialog.setContentView(R.layout.dialog_text_spinner);
                    dialog.show();
                    TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                    TextView description = dialog.findViewById(R.id.tv_description);
                    final Spinner spinner = dialog.findViewById(R.id.shiftNoSpinner);
                    Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                    Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                    heading.setText("Sunday");
                    description.setText("Select Shift or Mark Holiday");
                    btnYes.setText("Assign Shift");
                    btnNo.setText("Mark Holiday");
                    spinner.setAdapter(shiftAdapter);
                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            editTextTue.setText("H");
                            editTextTue.setBackground(shape);
                        }
                    });
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(spinner.getSelectedItemPosition()==0)
                            {
                                Toast.makeText(RosterManagement.this, "Please select shift", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            dialog.dismiss();
                            editTextTue.setText(spinner.getSelectedItemPosition()+"");
                            editTextTue.setBackground(shape1);
                        }
                    });
                }
                else
                {
                    Toast.makeText(this, "Click on New Schedule to edit", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.editTextWed:
                if (editArrangement) {
                    final Dialog dialog = new Dialog(RosterManagement.this);
// Include dialog.xml file
                    dialog.setContentView(R.layout.dialog_text_spinner);
                    dialog.show();
                    TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                    TextView description = dialog.findViewById(R.id.tv_description);
                    final Spinner spinner = dialog.findViewById(R.id.shiftNoSpinner);
                    Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                    Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                    heading.setText("Wednesday");
                    description.setText("Select Shift or Mark Holiday");
                    btnYes.setText("Assign Shift");
                    btnNo.setText("Mark Holiday");
                    spinner.setAdapter(shiftAdapter);
                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            editTextWed.setText("H");
                            editTextWed.setBackground(shape);
                        }
                    });
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(spinner.getSelectedItemPosition()==0)
                            {
                                Toast.makeText(RosterManagement.this, "Please select shift", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            dialog.dismiss();
                            editTextWed.setText(spinner.getSelectedItemPosition()+"");
                            editTextWed.setBackground(shape1);
                        }
                    });
                }
                else
                {
                    Toast.makeText(this, "Click on New Schedule to edit", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.editTextThu:
                if (editArrangement) {
                    final Dialog dialog = new Dialog(RosterManagement.this);
// Include dialog.xml file
                    dialog.setContentView(R.layout.dialog_text_spinner);
                    dialog.show();
                    TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                    TextView description = dialog.findViewById(R.id.tv_description);
                    final Spinner spinner = dialog.findViewById(R.id.shiftNoSpinner);
                    Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                    Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                    heading.setText("Thursday");
                    description.setText("Select Shift or Mark Holiday");
                    btnYes.setText("Assign Shift");
                    btnNo.setText("Mark Holiday");
                    spinner.setAdapter(shiftAdapter);
                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            editTextThu.setText("H");
                            editTextThu.setBackground(shape);
                        }
                    });
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(spinner.getSelectedItemPosition()==0)
                            {
                                Toast.makeText(RosterManagement.this, "Please select shift", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            dialog.dismiss();
                            editTextThu.setText(spinner.getSelectedItemPosition()+"");
                            editTextThu.setBackground(shape1);
                        }
                    });
                }
                else
                {
                    Toast.makeText(this, "Click on New Schedule to edit", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.editTextFri:
                if (editArrangement) {
                    final Dialog dialog = new Dialog(RosterManagement.this);
// Include dialog.xml file
                    dialog.setContentView(R.layout.dialog_text_spinner);
                    dialog.show();
                    TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                    TextView description = dialog.findViewById(R.id.tv_description);
                    final Spinner spinner = dialog.findViewById(R.id.shiftNoSpinner);
                    Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                    Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                    heading.setText("Friday");
                    description.setText("Select Shift or Mark Holiday");
                    btnYes.setText("Assign Shift");
                    btnNo.setText("Mark Holiday");
                    spinner.setAdapter(shiftAdapter);
                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            editTextFri.setText("H");
                            editTextFri.setBackground(shape);
                        }
                    });
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(spinner.getSelectedItemPosition()==0)
                            {
                                Toast.makeText(RosterManagement.this, "Please select shift", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            dialog.dismiss();
                            editTextFri.setText(spinner.getSelectedItemPosition()+"");
                            editTextFri.setBackground(shape1);
                        }
                    });
                }
                else
                {
                    Toast.makeText(this, "Click on New Schedule to edit", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.editTextSat:
                if (editArrangement) {
                    final Dialog dialog = new Dialog(RosterManagement.this);
// Include dialog.xml file
                    dialog.setContentView(R.layout.dialog_text_spinner);
                    dialog.show();
                    TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                    TextView description = dialog.findViewById(R.id.tv_description);
                    final Spinner spinner = dialog.findViewById(R.id.shiftNoSpinner);
                    Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                    Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                    heading.setText("Saturday");
                    description.setText("Select Shift or Mark Holiday");
                    btnYes.setText("Assign Shift");
                    btnNo.setText("Mark Holiday");
                    spinner.setAdapter(shiftAdapter);
                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            editTextSat.setText("H");
                            editTextSat.setBackground(shape);
                        }
                    });
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(spinner.getSelectedItemPosition()==0)
                            {
                                Toast.makeText(RosterManagement.this, "Please select shift", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            dialog.dismiss();
                            editTextSat.setText(spinner.getSelectedItemPosition()+"");
                            editTextSat.setBackground(shape1);
                        }
                    });
                }
                else
                {
                    Toast.makeText(this, "Click on New Schedule to edit", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.newArrangementText:
                if (editArrangement) {
                    editArrangement = false;
                    newArrangementText.setBackgroundColor((getResources().getColor(R.color.blue_600)));
                } else {
                    editArrangement = true;
                    newArrangementText.setBackgroundColor((getResources().getColor(R.color.green_500)));
                }
                break;
            case R.id.btnGo:
                if (employeeId.getText().toString().matches(""))
                {

                        Toast.makeText(RosterManagement.this, "Select Employee", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!employeeIdList.contains(employeeId.getText().toString())||!employeeNameList.contains(employeeName.getText().toString()))
                {
                    Toast.makeText(RosterManagement.this, "Invalid Employee", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    progressDialog.show();
                    (Api.getClient()).getRoster("fetch_all", employer_id, txtemployeeidhidden.getText().toString()).enqueue(new Callback<Roster>() {
                        @Override
                        public void onResponse(Call<Roster> call, Response<Roster> response) {
                            try {
                                roster = response.body();
                                if (roster.getErrorCode() == 101)
                                {
                                    oldList = roster.getEmployeeData();
                                    new_arrangement=roster.getNewArrangeList().getList();

                                    if(!new_arrangement.isEmpty())
                                    {
                                        applyFromDate.setText(roster.getNewArrangeList().getAppliedDate());
                                        from = simpleDateFormat.parse(roster.getNewArrangeList().getAppliedDate());
                                        shift.setText(roster.getNewArrangeList().getShiftNo());
                                    }
                                    daylist.clear();
                                    shiftList.clear();
                                    String[] sep = roster.getEmployeeData().split(",");
                                    for (int i = 0; i < sep.length; i++)
                                    {
                                        String[] data = sep[i].split(":");
                                        daylist.add(data[0].trim());
                                        shiftList.add(data[1].trim());
                                    }
                                    for (int i = 0; i < daylist.size(); i++)
                                    {
                                        Log.e("DAYSHIFT", daylist.get(i) + ";" + shiftList.get(i));
                                    }
                                    textSun.setText(shiftList.get(daylist.indexOf("sun")));
                                    textMon.setText(shiftList.get(daylist.indexOf("mon")));
                                    textTue.setText(shiftList.get(daylist.indexOf("tue")));
                                    textWed.setText(shiftList.get(daylist.indexOf("wed")));
                                    textThu.setText(shiftList.get(daylist.indexOf("thu")));
                                    textFri.setText(shiftList.get(daylist.indexOf("fri")));
                                    textSat.setText(shiftList.get(daylist.indexOf("sat")));
                                    if (shiftList.get(daylist.indexOf("sun")).matches("H")) {
                                        textSun.setBackground(shape);
                                    } else {
                                        textSun.setBackground(shape1);
                                    }
                                    if (shiftList.get(daylist.indexOf("thu")).matches("H")) {
                                        textThu.setBackground(shape);
                                    } else {
                                        textThu.setBackground(shape1);
                                    }
                                    if (shiftList.get(daylist.indexOf("mon")).matches("H")) {
                                        textMon.setBackground(shape);
                                    } else {
                                        textMon.setBackground(shape1);
                                    }
                                    if (shiftList.get(daylist.indexOf("tue")).matches("H")) {
                                        textTue.setBackground(shape);
                                    } else {
                                        textTue.setBackground(shape1);
                                    }
                                    if (shiftList.get(daylist.indexOf("wed")).matches("H")) {
                                        textWed.setBackground(shape);
                                    } else {
                                        textWed.setBackground(shape1);
                                    }
                                    if (shiftList.get(daylist.indexOf("fri")).matches("H")) {
                                        textFri.setBackground(shape);
                                    } else {
                                        textFri.setBackground(shape1);
                                    }
                                    if (shiftList.get(daylist.indexOf("sat")).matches("H")) {
                                        textSat.setBackground(shape);
                                    } else {
                                        textSat.setBackground(shape1);
                                    }
                                    daylist.clear();
                                    shiftList.clear();
                                    //new arrangement data if available
                                    if(!new_arrangement.isEmpty())
                                    {
                                        String[] days=new_arrangement.split(",");
                                        for (int i = 0; i < days.length; i++)
                                        {
                                            String[] data = days[i].split(":");
                                            daylist.add(data[0].trim());
                                            shiftList.add(data[1].trim());
                                        }
                                        for (int i = 0; i < daylist.size(); i++)
                                        {
                                            Log.e("DAYSHIFT", daylist.get(i) + ";" + shiftList.get(i));
                                        }
                                        editTextSun.setText(shiftList.get(daylist.indexOf("sun")));
                                        editTextMon.setText(shiftList.get(daylist.indexOf("mon")));
                                        editTextTue.setText(shiftList.get(daylist.indexOf("tue")));
                                        editTextWed.setText(shiftList.get(daylist.indexOf("wed")));
                                        editTextThu.setText(shiftList.get(daylist.indexOf("thu")));
                                        editTextFri.setText(shiftList.get(daylist.indexOf("fri")));
                                        editTextSat.setText(shiftList.get(daylist.indexOf("sat")));
                                        if (shiftList.get(daylist.indexOf("sun")).matches("H")) {
                                            editTextSun.setBackground(shape);
                                        } else {
                                            editTextSun.setBackground(shape1);
                                        }
                                        if (shiftList.get(daylist.indexOf("thu")).matches("H")) {
                                            editTextThu.setBackground(shape);
                                        } else {
                                            editTextThu.setBackground(shape1);
                                        }
                                        if (shiftList.get(daylist.indexOf("mon")).matches("H")) {
                                            editTextMon.setBackground(shape);
                                        } else {
                                            editTextMon.setBackground(shape1);
                                        }
                                        if (shiftList.get(daylist.indexOf("tue")).matches("H")) {
                                            editTextTue.setBackground(shape);
                                        } else {
                                            editTextTue.setBackground(shape1);
                                        }
                                        if (shiftList.get(daylist.indexOf("wed")).matches("H")) {
                                            editTextWed.setBackground(shape);
                                        } else {
                                            editTextWed.setBackground(shape1);
                                        }
                                        if (shiftList.get(daylist.indexOf("fri")).matches("H")) {
                                            editTextFri.setBackground(shape);
                                        } else {
                                            editTextFri.setBackground(shape1);
                                        }
                                        if (shiftList.get(daylist.indexOf("sat")).matches("H")) {
                                            editTextSat.setBackground(shape);
                                        } else {
                                            editTextSat.setBackground(shape1);
                                        }
                                    }

                                    progressDialog.dismiss();
                                    cardViewnewArrangement.setVisibility(View.VISIBLE);
                                    cardViewCurrentArrangement.setVisibility(View.VISIBLE);
                                    appliedfromlay.setVisibility(View.VISIBLE);
                                    btnSave.setVisibility(View.VISIBLE);

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(RosterManagement.this, roster.getMessage(), Toast.LENGTH_SHORT).show();
                                    cardViewnewArrangement.setVisibility(View.GONE);
                                    cardViewCurrentArrangement.setVisibility(View.GONE);
                                    appliedfromlay.setVisibility(View.GONE);
                                    btnSave.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<Roster> call, Throwable t) {

                        }
                    });
                }
                break;
        }
    }

    //backpress
    protected void exitByBackKey() {

        final Dialog dialog = new Dialog(RosterManagement.this);
// Include dialog.xml file
        dialog.setContentView(R.layout.dialog_exit);
        dialog.show();

        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(RosterManagement.this, EmployerZone.class);
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