package com.miscos.staffhandler.employee.employeemodule;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.miscos.staffhandler.Urls.App_Urls;
import com.miscos.staffhandler.activities.EmployeeZone;
import com.miscos.staffhandler.activities.EmployerZone;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.miscos.staffhandler.employee.network.LocationTrack;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonDataForWifi;
import com.fivemin.chief.nonetworklibrary.networkBroadcast.NoNet;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
/*Developed under Miscos
 * Developed by Nikhil
 * 05-06-2020
 * */
public class EmployeeActivity extends AppCompatActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 7;
    private static final int REQUEST_LOCATION = 1;
    double office_latitude, office_lognitude;
    //double slatitude = 18.4450749, slognitude = 73.8345708;
    Context context;
    PreferenceManager preferenceManager;
    LocationTrack locationTrack;
    // inside Activity
    ExpandableRelativeLayout expandableLayout1, emergencyExpandable,specialExpandable;

    String statusCode;
    double lat;
    double longi;
    String latLang,employeLAtLang;
    String employeeLat,employeelongi;
    CardView cardViewDayIn,cardViewDayIn1,cardViewDayIn2, cardViewDayout,cardViewDayout1,cardViewDayout2,cardViewEmergency,cardViewSpecialCase;
    LocationManager locationManager;
    String emplpoyee_status, office_status, msg,user_name,user_pass,setUpPin,latitude, longitude, wifinames,type_of_attendance,attendance_type,attendancetype, permittedDistance, employerId, employeeId, status, offStatus, emSatsus,attendanceType;
    double dist;
    double compareValue;
    private WifiManager wifiManager;
    private final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
    SwipeRefreshLayout swipeRefreshLayout;
    GetJsonDataForWifi getJsonDataForWifi;
    private ProgressDialog progressDialog;
    LocationManager lm;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    List<String> deviceList = new ArrayList<>();
    List<String> addWifiNamesList = new ArrayList<>();
    List<String> wifiNamesList = new ArrayList<>();
    List<ScanResult> wifiList = new ArrayList<>();
    TextView employerZone,txtorgname;
    StringBuilder sb;
    String employee, employer,employeeType,addWifiNames;
    TextView officeStatus, employeeStatus,txtname;
    // No Internet Dialog
    private NoNet mNoNet;
    private FragmentManager fm = null;
    PackageInfo pinfo;
    int versionNumber ;
    String versionName,latestVersion,noti_type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        fm = getSupportFragmentManager();
        mNoNet = new NoNet();
        mNoNet.initNoNet(this, fm);
       // loginDetails();
        preferenceManager = new PreferenceManager(EmployeeActivity.this);
        progressDialog = new ProgressDialog(EmployeeActivity.this);
        //Progress Bar
        progressDialog = new ProgressDialog(EmployeeActivity.this);
        progressDialog = ProgressDialog.show(EmployeeActivity.this, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.dismiss();
        txtname=findViewById(R.id.txtsupervisorname);
        txtname.setText("For marking manual attendance you can contact with "+preferenceManager.getStringPreference(PreferenceManager.KEY_SUPERVISOR_NAME));

        cardViewDayIn = findViewById(R.id.card_view_dayIn);
        cardViewDayout = findViewById(R.id.card_view_dayOut);
        txtorgname=findViewById(R.id.txtorgname);
        cardViewDayIn1 = findViewById(R.id.card_view_dayIn1);
        cardViewDayout1 = findViewById(R.id.card_view_dayOut1);

        cardViewDayIn2 = findViewById(R.id.card_view_dayIn2);
        cardViewDayout2 = findViewById(R.id.card_view_dayOut2);

        cardViewEmergency = findViewById(R.id.card_view_emergency);
        cardViewSpecialCase = findViewById(R.id.card_view_special);
        employerZone = findViewById(R.id.tvEmployerZone);
        officeStatus = findViewById(R.id.tvOffStatus);
        employeeStatus = findViewById(R.id.tvEmStatus);
        

       /* "holiday";*/
        offStatus = preferenceManager.getStringPreference(PreferenceManager.KEY_OFFICE_STATUS);
        emSatsus = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_STATUS);
        employee = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEEID);
        employer = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID);
        setUpPin = preferenceManager.getStringPreference(PreferenceManager.KEY_SET_UP_PIN);
        user_name = preferenceManager.getStringPreference(PreferenceManager.KEY_USERNAME);
        user_pass = preferenceManager.getStringPreference(PreferenceManager.KEY_PASSWORD);
        wifinames = preferenceManager.getStringPreference(PreferenceManager.KEY_WIFI_NAMES);
        employeeType = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_TYPE);
        employeeStatus.setText(emSatsus);
        txtorgname.setText("Welcome to "+preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYER_NAME));

        if(offStatus.isEmpty()){
            officeStatus.setText("Not Working");
        }
        officeStatus.setText(offStatus);
        try {
            pinfo= getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        versionNumber = pinfo.versionCode;
        versionName =  pinfo.versionName;;
        Log.d("TAG", "onCreate: " + versionName);



        checkVersion();
        if (emSatsus.equalsIgnoreCase("Out")) {
            employeeStatus.setTextColor(getResources().getColor(R.color.red));
        }

        //condition for zone according to their id's
        if (employee.equalsIgnoreCase(employer))
        {
            employerZone.setText(R.string.employer_zone);
            txtname.setVisibility(View.GONE);

        }else if(preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_TYPE).equalsIgnoreCase("admin"))
        {
            employerZone.setText(R.string.employee_zone_admin_settings);
            txtname.setVisibility(View.GONE);
        }
        else
        {
            employerZone.setText("Employee Zone");
            txtname.setVisibility(View.VISIBLE);
        }

        //Selection of employee and employer
        employerZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (employee.equalsIgnoreCase(employer))
                {
                    startActivity(new Intent(EmployeeActivity.this, EmployerZone.class));
                   ;

                }else if(preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_TYPE).equalsIgnoreCase("admin"))
                {
                    EmployeeType();
                }
                else
                {
                    startActivity(new Intent(EmployeeActivity.this, EmployeeZone.class));

                }

            }
        });

        //locationTrack = new LocationTrack(EmployeeActivity.this);


            longi = LocationTrack.getLongitude();
            lat = LocationTrack.getLatitude();

        latLang = lat+","+longi;
       // Toast.makeText(this, latLang, Toast.LENGTH_SHORT).show();
        preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_LATITUDE_LONGITUDE, latLang);

       /* longi = Double.parseDouble(employeelongi);
        lat = Double.parseDouble(employeeLat);
        Log.d("TAG", "location"+ longi);*/

        attendance_type = preferenceManager.getStringPreference(PreferenceManager.KEY_ATTENDANCETYPE);
        permittedDistance = preferenceManager.getStringPreference(PreferenceManager.KEY_PERMITTEDDISTANCE);
        employerId = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID);
        employeeId = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEEID);
        latitude = preferenceManager.getStringPreference(PreferenceManager.KEY_LATITUDE);
        longitude = preferenceManager.getStringPreference(PreferenceManager.KEY_LONGITUDE);

        office_latitude = Double.parseDouble(latitude);
        office_lognitude = Double.parseDouble(longitude);
        if(attendance_type.equalsIgnoreCase("gps_with_wifi")){
            turnOnWifi();
            getWifi();
        }
        init();


        compareValue = Double.parseDouble(permittedDistance);

        //emergency day in day out
        cardViewEmergency.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                    //Day In Click
                cardViewDayIn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (preferenceManager.getStringPreference(PreferenceManager.KEY_PERMITTED_FOR_EMER).equalsIgnoreCase("Y")) {
                                status = "day_in";
                                type_of_attendance = "emergency";
                                if (attendance_type.equals("QR")) {
                                    Intent intent = new Intent(EmployeeActivity.this, Activity_ScanQrCode.class);
                                    intent.putExtra("Day", status);
                                    intent.putExtra(Activity_ScanQrCode.CALL_FROM,"regular");
                                    intent.putExtra("attendance_type", type_of_attendance);
                                    startActivity(intent);

                                } else if (attendance_type.equals("qr")) {
                                    Intent intent = new Intent(EmployeeActivity.this, Activity_ScanQrCode.class);
                                    intent.putExtra("Day", status);
                                    intent.putExtra(Activity_ScanQrCode.CALL_FROM,"regular");
                                    intent.putExtra("attendance_type", type_of_attendance);
                                    startActivity(intent);

                                } else if (attendance_type.equals("both")||attendance_type.equals("gps")) {
                                    if (dist <= compareValue &&preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("geo_permitted_distance")){
                                        callSuccessdialog();
                                    }else if (preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("gps_plotting")){
                                        checkGPS();
                                    }else {
                                        errorDialog();
                                    }
                                }else if(attendance_type.equals("gps_with_wifi")){
                                    if (preferenceManager.getStringPreference(PreferenceManager.KEY_WIFI_NAMES).equals("")){
                                        noWifiAdded();
                                    }else {
                                        if (dist <= compareValue && preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("geo_permitted_distance")) {
                                            callSuccessdialogWifi();
                                        }else if(preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("gps_plotting")) {
                                            checkGPS();
                                        }else {
                                            errorDialog();
                                        }
                                    }
                                }else {
                                    errorDialog();
                                }
                            }else{
                                allow_access();
                            }

                        }
                    });
                    //Day out Click
                    cardViewDayout1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (preferenceManager.getStringPreference(PreferenceManager.KEY_PERMITTED_FOR_EMER).equalsIgnoreCase("Y")) {
                                status = "day_out";
                                type_of_attendance = "emergency";
                                if (attendance_type.equals("QR")) {
                                    Intent intent = new Intent(EmployeeActivity.this, Activity_ScanQrCode.class);
                                    intent.putExtra("Day", status);
                                    intent.putExtra(Activity_ScanQrCode.CALL_FROM,"regular");
                                    intent.putExtra("attendance_type", type_of_attendance);
                                    startActivity(intent);

                                } else if (attendance_type.equals("qr")) {
                                    Intent intent = new Intent(EmployeeActivity.this, Activity_ScanQrCode.class);
                                    intent.putExtra("Day", status);
                                    intent.putExtra(Activity_ScanQrCode.CALL_FROM,"regular");
                                    intent.putExtra("attendance_type", type_of_attendance);
                                    startActivity(intent);

                                } else if (attendance_type.equals("both")||attendance_type.equals("gps")) {
                                    if (dist <= compareValue &&preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("geo_permitted_distance")){
                                        callSuccessdialog();
                                    }else if (preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("gps_plotting")){
                                        checkGPS();
                                    }else {
                                        errorDialog();
                                    }
                                }else if(attendance_type.equals("gps_with_wifi")){
                                    if (preferenceManager.getStringPreference(PreferenceManager.KEY_WIFI_NAMES).equals("")){
                                        noWifiAdded();
                                    }else {
                                        if (dist <= compareValue && preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("geo_permitted_distance")) {
                                            callSuccessdialogWifi();
                                        }else if(preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("gps_plotting")) {
                                            checkGPS();
                                        }else {
                                            errorDialog();
                                        }
                                    }
                                }else {
                                    errorDialog();
                                }
                            }else{
                                allow_access();
                            }
                        }
                    });
                }
        });

        //Special Duty
        cardViewSpecialCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Day In Click
                cardViewDayIn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (preferenceManager.getStringPreference(PreferenceManager.KEY_SPECIAL_DUTY).equalsIgnoreCase("Y")&&isValidemp(preferenceManager.getStringPreference(PreferenceManager.KEY_SPECIAL_DUTY_APPLICABLE_DATE)))
                        {
                            status = "day_in";
                            type_of_attendance = "special";
                            if (attendance_type.equals("QR")) {
                                Intent intent = new Intent(EmployeeActivity.this, Activity_ScanQrCode.class);
                                intent.putExtra("Day", status);
                                intent.putExtra(Activity_ScanQrCode.CALL_FROM,"regular");
                                intent.putExtra("attendance_type", type_of_attendance);
                                startActivity(intent);

                            } else if (attendance_type.equals("qr")) {
                                Intent intent = new Intent(EmployeeActivity.this, Activity_ScanQrCode.class);
                                intent.putExtra("Day", status);
                                intent.putExtra(Activity_ScanQrCode.CALL_FROM,"regular");
                                intent.putExtra("attendance_type", type_of_attendance);
                                startActivity(intent);

                            } else if (attendance_type.equals("both")||attendance_type.equals("gps")) {
                                if (dist <= compareValue &&preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("geo_permitted_distance")){
                                    callSuccessdialog();
                                }else if (preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("gps_plotting")){
                                    checkGPS();
                                }else {
                                    errorDialog();
                                }
                            }else if(attendance_type.equals("gps_with_wifi")){
                                if (preferenceManager.getStringPreference(PreferenceManager.KEY_WIFI_NAMES).equals("")){
                                    noWifiAdded();
                                }else {
                                    if (dist <= compareValue && preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("geo_permitted_distance")) {
                                        callSuccessdialogWifi();
                                    }else if(preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("gps_plotting")) {
                                        checkGPS();
                                    }else {
                                        errorDialog();
                                    }
                                }
                            }else {
                                errorDialog();
                            }
                        }else{
                            allow_access();
                        }

                    }
                });

                //Special Day out Click
                cardViewDayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (preferenceManager.getStringPreference(PreferenceManager.KEY_SPECIAL_DUTY).equalsIgnoreCase("Y")&&isValidemp(preferenceManager.getStringPreference(PreferenceManager.KEY_SPECIAL_DUTY_APPLICABLE_DATE)))
                        {
                            status = "day_out";
                            type_of_attendance = "special";
                            if (attendance_type.equals("QR")) {
                                Intent intent = new Intent(EmployeeActivity.this, Activity_ScanQrCode.class);
                                intent.putExtra("Day", status);
                                intent.putExtra("attendance_type", type_of_attendance);
                                intent.putExtra(Activity_ScanQrCode.CALL_FROM,"regular");
                                startActivity(intent);

                            } else if (attendance_type.equals("qr")) {
                                Intent intent = new Intent(EmployeeActivity.this, Activity_ScanQrCode.class);
                                intent.putExtra("Day", status);
                                intent.putExtra("attendance_type", type_of_attendance);
                                intent.putExtra(Activity_ScanQrCode.CALL_FROM,"regular");
                                startActivity(intent);

                            } else if (attendance_type.equals("both")||attendance_type.equals("gps")) {
                                if (dist <= compareValue &&preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("geo_permitted_distance")){
                                    callSuccessdialog();
                                }else if (preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("gps_plotting")){
                                    checkGPS();
                                }else {
                                    errorDialog();
                                }
                            }else if(attendance_type.equals("gps_with_wifi")){
                                if (preferenceManager.getStringPreference(PreferenceManager.KEY_WIFI_NAMES).equals("")){
                                    noWifiAdded();
                                }else {
                                    if (dist <= compareValue && preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("geo_permitted_distance")) {
                                        callSuccessdialogWifi();
                                    }else if(preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("gps_plotting")) {
                                        checkGPS();
                                    }else {
                                        errorDialog();
                                    }
                                }
                            }else {
                                errorDialog();
                            }
                        }else{
                            allow_access();
                        }
                    }
                });
            }
        });

        //Regular Employee Attendance Marking
        //Day In Click
        cardViewDayIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_TYPE).equalsIgnoreCase("owner")||preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_TYPE).equalsIgnoreCase("admin")||preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_TYPE).equalsIgnoreCase("employee"))
                {
                    status = "day_in";
                    type_of_attendance = "normal";
                    if (attendance_type.equals("QR"))
                    {
                        Intent intent = new Intent(EmployeeActivity.this, Activity_ScanQrCode.class);
                        intent.putExtra("Day", status);
                        intent.putExtra("attendance_type", type_of_attendance);
                        startActivity(intent);

                    } else if (attendance_type.equals("qr"))
                    {
                        Intent intent = new Intent(EmployeeActivity.this, Activity_ScanQrCode.class);
                        intent.putExtra("Day", status);
                        intent.putExtra("attendance_type", type_of_attendance);
                        startActivity(intent);

                    } else if (attendance_type.equals("both")||attendance_type.equals("gps"))
                    {
                        if (dist <= compareValue &&preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("geo_permitted_distance")){
                            callSuccessdialog();
                        }else if (preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("gps_plotting")){
                            checkGPS();
                        }else {
                            errorDialog();
                        }
                    }else if(attendance_type.equals("gps_with_wifi")){
                        if (preferenceManager.getStringPreference(PreferenceManager.KEY_WIFI_NAMES).equals("")){
                            noWifiAdded();
                        }else {
                            if (dist <= compareValue && preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("geo_permitted_distance")) {
                                callSuccessdialogWifi();
                            }else if(preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("gps_plotting")) {
                                checkGPS();
                            }else {
                                errorDialog();
                            }
                        }
                    }else {
                        errorDialog();
                    }
                }
                else if(preferenceManager.getStringPreference(PreferenceManager.KEY_PERMITTED_FOR_EMER).equalsIgnoreCase("N")||preferenceManager.getStringPreference(PreferenceManager.KEY_SPECIAL_DUTY).equalsIgnoreCase("")){
                    status = "day_in";
                    type_of_attendance = "normal";
                    if (attendance_type.equals("QR"))
                    {
                        Intent intent = new Intent(EmployeeActivity.this, Activity_ScanQrCode.class);
                        intent.putExtra("Day", status);
                        intent.putExtra("attendance_type", type_of_attendance);
                        intent.putExtra(Activity_ScanQrCode.CALL_FROM,"regular");
                        startActivity(intent);

                    }
                    else if (attendance_type.equals("qr"))
                    {
                        Intent intent = new Intent(EmployeeActivity.this, Activity_ScanQrCode.class);
                        intent.putExtra("Day", status);
                        intent.putExtra("attendance_type", type_of_attendance);
                        intent.putExtra(Activity_ScanQrCode.CALL_FROM,"regular");
                        startActivity(intent);

                    }
                    else if (attendance_type.equals("both")||attendance_type.equals("gps"))
                    {
                        if (dist <= compareValue &&preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("geo_permitted_distance")){
                            callSuccessdialog();
                        }else if (preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("gps_plotting")){
                            checkGPS();
                        }else {
                            errorDialog();
                        }
                    }else if(attendance_type.equals("gps_with_wifi"))
                    {
                        if (preferenceManager.getStringPreference(PreferenceManager.KEY_WIFI_NAMES).equals("")){
                            noWifiAdded();
                        }else {
                            if (dist <= compareValue && preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("geo_permitted_distance")) {
                                callSuccessdialogWifi();
                            }else if(preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("gps_plotting")) {
                                checkGPS();
                            }else {
                                errorDialog();
                            }
                        }
                    }else {
                        errorDialog();
                    }
                }
            }
        });



        //Day out Click
        cardViewDayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_TYPE).equalsIgnoreCase("owner")||preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_TYPE).equalsIgnoreCase("admin")||preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_TYPE).equalsIgnoreCase("employee"))
                {
                    status = "day_out";
                    type_of_attendance = "normal";
                    if (attendance_type.equals("QR"))
                    {
                        Intent intent = new Intent(EmployeeActivity.this, Activity_ScanQrCode.class);
                        intent.putExtra("Day", status);
                        intent.putExtra("attendance_type", type_of_attendance);
                        intent.putExtra(Activity_ScanQrCode.CALL_FROM,"regular");
                        startActivity(intent);

                    } else if (attendance_type.equals("qr"))
                    {
                        Intent intent = new Intent(EmployeeActivity.this, Activity_ScanQrCode.class);
                        intent.putExtra("Day", status);
                        intent.putExtra("attendance_type", type_of_attendance);
                        intent.putExtra(Activity_ScanQrCode.CALL_FROM,"regular");
                        startActivity(intent);

                    } else if (attendance_type.equals("both")||attendance_type.equals("gps"))
                    {
                        if (dist <= compareValue &&preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("geo_permitted_distance")){
                            callSuccessdialog();
                        }else if (preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("gps_plotting")){
                            checkGPS();
                        }else {
                            errorDialog();
                        }
                    }else if(attendance_type.equals("gps_with_wifi"))
                    {
                        if (preferenceManager.getStringPreference(PreferenceManager.KEY_WIFI_NAMES).equals("")){
                            noWifiAdded();
                        }else {
                            if (dist <= compareValue && preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("geo_permitted_distance")) {
                                callSuccessdialogWifi();
                            }else if(preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("gps_plotting")) {
                                checkGPS();
                            }else {
                                errorDialog();
                            }
                        }
                    }else {
                        errorDialog();
                    }
                }else if(preferenceManager.getStringPreference(PreferenceManager.KEY_PERMITTED_FOR_EMER).equalsIgnoreCase("N")||preferenceManager.getStringPreference(PreferenceManager.KEY_SPECIAL_DUTY).equalsIgnoreCase("")){
                    status = "day_out";
                    type_of_attendance = "normal";
                    if (attendance_type.equals("QR"))
                    {
                        Intent intent = new Intent(EmployeeActivity.this, Activity_ScanQrCode.class);
                        intent.putExtra("Day", status);
                        intent.putExtra("attendance_type", type_of_attendance);
                        intent.putExtra(Activity_ScanQrCode.CALL_FROM,"regular");
                        startActivity(intent);

                    } else if (attendance_type.equals("qr"))
                    {
                        Intent intent = new Intent(EmployeeActivity.this, Activity_ScanQrCode.class);
                        intent.putExtra("Day", status);
                        intent.putExtra("attendance_type", type_of_attendance);
                        intent.putExtra(Activity_ScanQrCode.CALL_FROM,"regular");
                        startActivity(intent);

                    } else if (attendance_type.equals("both"))
                    {
                        if (dist <= compareValue &&preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("geo_permitted_distance")){
                            callSuccessdialog();
                        }else if (preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("gps_plotting")){
                            checkGPS();
                        }else {
                            errorDialog();
                        }
                    }else if(attendance_type.equals("gps_with_wifi"))
                    {
                        if (preferenceManager.getStringPreference(PreferenceManager.KEY_WIFI_NAMES).equals("")){
                            noWifiAdded();
                        }else
                            {
                            if (dist <= compareValue && preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("geo_permitted_distance")) {
                                callSuccessdialogWifi();
                            }else if(preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("gps_plotting")) {
                                checkGPS();
                            }else {
                                errorDialog();
                            }
                        }
                    }else {
                        errorDialog();
                    }
                }
            }
        });

        if (!offStatus.equalsIgnoreCase("Working"))
        {

            cardViewDayIn.setCardBackgroundColor(getResources().getColor(R.color.grey_300));
            cardViewDayout.setCardBackgroundColor(getResources().getColor(R.color.grey_300));

            cardViewDayIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PrettyDialog prettyDialog = new PrettyDialog(EmployeeActivity.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Day In")
                            .setMessage("Today is Holiday, Can not mark day in")
                            .setIcon(R.drawable.cross)
                            .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            })

                            .show();
                }
            });
            cardViewDayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PrettyDialog prettyDialog = new PrettyDialog(EmployeeActivity.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Day Out")
                            .setMessage("Today is Holiday, Can not mark Day out")
                            .setIcon(R.drawable.cross)
                            .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            })

                            .show();
                }
            });
        }

    }

    public void turnOnWifi(){

        wifiManager = (WifiManager) EmployeeActivity.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        lm = (LocationManager) EmployeeActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(EmployeeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(EmployeeActivity.this, "location turned off", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(EmployeeActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
            }
        }
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(EmployeeActivity.this, "Turning WiFi ON...", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }else{
            Toast.makeText(EmployeeActivity.this, "Turn On Wifi First", Toast.LENGTH_SHORT).show();
        }
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            EmployeeActivity.this.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        this.registerReceiver(receiverWifi, intentFilter);
    }
    private void getWifi() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(EmployeeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(EmployeeActivity.this, "location turned off", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(EmployeeActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
            } else {
                wifiManager.startScan();
            }
        } else {
            Toast.makeText(EmployeeActivity.this, "scanning", Toast.LENGTH_SHORT).show();
            wifiManager.startScan();

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(EmployeeActivity.this, "permission granted", Toast.LENGTH_SHORT).show();
                    wifiManager.startScan();
                } else {
                    Toast.makeText(EmployeeActivity.this, "permission not granted", Toast.LENGTH_SHORT).show();
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
                for (ScanResult scanResult : wifiList) {
                    deviceList.add(scanResult.SSID);
                    addWifiNames = "";
                    for (int i = 0; i < deviceList.size(); i++) {
                        if (addWifiNames.matches("")) {
                            addWifiNames = deviceList.get(i);
                        } else {
                            addWifiNames = addWifiNames + "," + deviceList.get(i);
                        }
                    }
                    //Toast.makeText(EmployeeActivity.this, addWifiNames, Toast.LENGTH_SHORT).show();
                }

            }
        }
    };
    //dialog for attendance
    public void EmployeeType(){
        View itemLayoutView = LayoutInflater.from(EmployeeActivity.this).inflate(
                R.layout.dialogforadmin, null);
        final Button btnCancel = itemLayoutView.findViewById(R.id.btnCancel);
        final TextView adminZone = itemLayoutView.findViewById(R.id.tvADminZone);
        final TextView employeeZone = itemLayoutView.findViewById(R.id.tvEmployeeZone);
        final TextView wifiManage = itemLayoutView.findViewById(R.id.tvWifi);

        new BottomSheetDialog(EmployeeActivity.this);
        BottomSheetDialog dialog;
        dialog = new BottomSheetDialog(EmployeeActivity.this,R.style.TransparentDialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(itemLayoutView);
        dialog.setCancelable(true);
        BottomSheetDialog finalDialog = dialog;

        adminZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finalDialog.dismiss();
                startActivity(new Intent(EmployeeActivity.this, EmployerZone.class));

            }
        });

        employeeZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDialog.dismiss();
                startActivity(new Intent(EmployeeActivity.this, EmployeeZone.class));

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDialog.dismiss();
            }
        });

        dialog.show();
    }
    private void allow_access(){
        final PrettyDialog prettyDialog= new PrettyDialog(EmployeeActivity.this);
        prettyDialog.setCancelable(false);
        prettyDialog
                .setTitle("Access Denied")
                .setMessage("You are not allow to access this.")
                .setIcon(R.drawable.cross,R.color.white,null)
                .addButton("Close", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        prettyDialog.dismiss();
                    }
                })
                .show();

    }
    private void noWifiAdded()
    {
        final PrettyDialog prettyDialog= new PrettyDialog(EmployeeActivity.this);
        prettyDialog.setCancelable(false);
        prettyDialog
                .setTitle("Wifi")
                .setMessage("Please add Wifi from Wifi management")
                .setIcon(R.drawable.cross,R.color.white,null)
                .addButton("Close", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        prettyDialog.dismiss();
                    }
                })
                .show();

    }
    /**
     * onClick handler
     */
    public void expandableButton1(View view) {
        expandableLayout1 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout1);
        expandableLayout1.toggle(); // toggle expand and collapse
    }
    public void expandableButton2(View view) {
        specialExpandable = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);
        specialExpandable.toggle(); // toggle expand and collapse
    }
    public void expandableButton3(View view) {
        emergencyExpandable = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout3);
        emergencyExpandable.toggle(); // toggle expand and collapse
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

    private void callSuccessdialog()
    {
        Intent intent = new Intent(EmployeeActivity.this, Activity_ScanQrCode.class);
        intent.putExtra("Day", status);
        intent.putExtra("attendance_type", type_of_attendance);
        startActivity(intent);

     /*   final PrettyDialog prettyDialog = new PrettyDialog(EmployeeActivity.this);
        prettyDialog.setCancelable(false);
        prettyDialog
                .setTitle("Congratulation")
                .setMessage("You are in the Office Premises Click Continue to Scan QR")
                .setIcon(R.drawable.right)
                .addButton("Continue", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        prettyDialog.dismiss();


                    }
                })
                .show();*/

    }
    private void callSuccessdialogWifi()
    {
        placeAttendance(status,type_of_attendance);
      /*  final PrettyDialog prettyDialog = new PrettyDialog(EmployeeActivity.this);
        prettyDialog.setCancelable(false);
        prettyDialog
                .setTitle("Congratulation")
                .setMessage("You are in the Office Premises Click Continue.")
                .setIcon(R.drawable.right)
                .addButton("Continue", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        prettyDialog.dismiss();

                    }
                })
                .show();*/

    }

    private void errorDialog() {

        final PrettyDialog prettyDialog = new PrettyDialog(EmployeeActivity.this);
        prettyDialog
                .setTitle("Oops")
                .setMessage("You Are out of Office Premises.\n \n Your Distance from Office is " + dist+" Meter")
                .setIcon(R.drawable.cross)
                .addButton("Ok", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        prettyDialog.dismiss();

                    }
                })
                .show();
    }


    public void checkVersion(){

        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.VERSION_CHECK, response -> {
            try {
                JSONArray jsonArray=new JSONArray(response);
                Log.e("checkVerison_response", " " + response);
                JSONObject jsonObject=jsonArray.getJSONObject(0);
                int error_code=jsonObject.getInt("error_code");
                String msg=jsonObject.getString("message");
                if (error_code == 101) {
                    JSONArray jsonArrayData=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArrayData.length();i++)
                    {
                        JSONObject versionData =jsonArrayData.getJSONObject(i);
                        latestVersion = versionData.getString("latest_version");
                        noti_type = versionData.getString("notification_type");
                    }
                    CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                            .setTitle("Update Available")
                            .setMessage("Currently you are using old version. For better performance, you need to update your application." + "\n Latest Version is " + latestVersion)
                            .addButton("Update", -1, Color.RED,
                                    CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.CENTER,new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if(noti_type.equalsIgnoreCase("compulsory")){
                                                dialogInterface.dismiss();
                                                String url = "https://play.google.com/store/apps/details?id=com.miscos.staffhandler";
                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                intent.setData(Uri.parse(url));
                                                startActivity(intent);
                                            }else{
                                                dialogInterface.dismiss();
                                            }
                                        }

                                    });

                    // Show the alert
                    builder.show();
                }else {
                    Log.d("TAG", "checkVersion: "+ msg);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EmployeeActivity.this, R.string.connection_error, Toast.LENGTH_LONG).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("device_os", "android");
                params.put("employee_id", preferenceManager.getStringPreference(PreferenceManager.KEY_USERNAME));
                params.put("device_version", versionName);
                Log.e("params is", " " + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(EmployeeActivity.this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void checkGPS()
    {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, App_Urls.CHECK_GPS,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d("check_gps", " "+response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            statusCode = jsonObject.getString("error_code");
                            String msg = jsonObject.getString("message");

                            if (statusCode.equals("102"))
                            {
                                if (attendance_type.equals("gps_with_wifi")) {
                                    placeAttendance(status,type_of_attendance);

                                }else{
                                    Intent intent = new Intent(EmployeeActivity.this, Activity_ScanQrCode.class);
                                    intent.putExtra("Day", status);
                                    intent.putExtra(Activity_ScanQrCode.CALL_FROM,"regular");
                                    intent.putExtra("attendance_type", type_of_attendance);
                                    startActivity(intent);

                                }

                                /*final PrettyDialog prettyDialog = new PrettyDialog(EmployeeActivity.this);
                                prettyDialog
                                        .setTitle("Congratulations")
                                        .setMessage("You are in Office premises")
                                        .setIcon(R.drawable.right)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {

                                            }
                                        })
                                        .show();*/
                            } else{
                                final PrettyDialog prettyDialog = new PrettyDialog(EmployeeActivity.this);
                                prettyDialog
                                        .setTitle("Oops")
                                        .setMessage("You Are out of Office Premises.")
                                        .setIcon(R.drawable.cross)
                                        .addButton("Ok", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            final Dialog dialog1 = new Dialog(EmployeeActivity.this);
// Include dialog.xml file
                            dialog1.setContentView(R.layout.dialog_exit);
                            dialog1.show();

                            TextView heading1 = dialog1.findViewById(R.id.tv_quit_learning);
                            TextView description1 = dialog1.findViewById(R.id.tv_description);
                            Button btnNo1 = (Button) dialog1.findViewById(R.id.btn_no);
                            Button btnYes1 = (Button) dialog1.findViewById(R.id.btn_yes);
                            heading1.setText("Error");
                            description1.setText("An unfortunate error occurred, please try again.");
                            btnNo1.setVisibility(View.GONE);
                            btnYes1.setText("Ok");
                            btnYes1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog1.dismiss();
                                }
                            });
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        final PrettyDialog prettyDialog = new PrettyDialog(EmployeeActivity.this);
                        prettyDialog.setCancelable(false);
                        prettyDialog
                                .setTitle("Connection Error")
                                .setMessage("Please try again")
                                .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                                .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                    @Override
                                    public void onClick() {
                                        prettyDialog.dismiss();
                                    }
                                })
                                .show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                // it will check for day stauts for day in or day out accordingly mode parameter will be passed
                params.put("employer_id", employerId);
                params.put("employee_lat_long", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_LATITUDE_LONGITUDE));
                Log.e("check_gps", " " + params);
                return params;
            }

        };

        requestQueue.add(stringRequest);
    }
//mark attendance
    private void placeAttendance(final String status,String typeAttendance)
    {
        progressDialog.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.EMPLOYEE_ATTENDANCE, response -> {
            progressDialog.dismiss();
            Log.d("attendance_mark", " "+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int error_code = jsonObject.getInt("error_code");
                            String msg = jsonObject.getString("message");
                            if(error_code ==101) {
                                final PrettyDialog prettyDialog = new PrettyDialog(EmployeeActivity.this);
                                prettyDialog
                                        .setTitle("Day Status")
                                        .setMessage(msg)
                                        .setIcon(R.drawable.right)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_STATUS, "in");
                                                prettyDialog.dismiss();
                                            }
                                        })
                                        .show();
                            } else if (error_code ==106) {
                                final PrettyDialog prettyDialog = new PrettyDialog(EmployeeActivity.this);
                                prettyDialog.setCancelable(false);
                                prettyDialog
                                        .setTitle("Day Status")
                                        .setMessage(msg)
                                        .setIcon(R.drawable.cross)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog.dismiss();
                                            }
                                        })
                                        .show();
                            } else if (error_code == 104) {
                                final PrettyDialog prettyDialog = new PrettyDialog(EmployeeActivity.this);
                                prettyDialog
                                        .setTitle("Day Status")
                                        .setMessage(msg)
                                        .setIcon(R.drawable.right)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_STATUS, "Out");
                                                prettyDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }if (error_code == 102) {
                                final PrettyDialog prettyDialog = new PrettyDialog(EmployeeActivity.this);
                                prettyDialog
                                        .setTitle("Office Area")
                                        .setMessage("You are not connect to the office wifi")
                                        .setIcon(R.drawable.cross)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }else if (error_code ==107) {
                                final PrettyDialog prettyDialog = new PrettyDialog(EmployeeActivity.this);

                                prettyDialog
                                        .setTitle("Day Status")
                                        .setMessage(msg)
                                        .setIcon(R.drawable.cross)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }else if (error_code ==110) {
                                final PrettyDialog prettyDialog = new PrettyDialog(EmployeeActivity.this);

                                prettyDialog
                                        .setTitle("Day Status")
                                        .setMessage(msg)
                                        .setIcon(R.drawable.cross)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }else{
                                final PrettyDialog prettyDialog = new PrettyDialog(EmployeeActivity.this);
                                prettyDialog
                                        .setTitle("Day Status")
                                        .setMessage(msg)
                                        .setIcon(R.drawable.cross)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            final PrettyDialog prettyDialog = new PrettyDialog(EmployeeActivity.this);
                            prettyDialog.setCancelable(false);
                            prettyDialog
                                    .setTitle("Connection Error")
                                    .setMessage("Please try again")
                                    .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                                    .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            prettyDialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                final PrettyDialog prettyDialog = new PrettyDialog(EmployeeActivity.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Connection Error")
                        .setMessage("Please try again")
                        .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                            }
                        })
                        .show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                // it will check for day stauts for day in or day out accordingly mode parameter will be passed
                if (status.equals("day_in")) {
                    params.put("mode", "day_in");
                }
                if (status.equals("day_out")) {
                    params.put("mode", "day_out");
                }
                params.put("force", "0");
                params.put("employer_id", employerId);

                params.put("employee_id", employeeId);
                params.put("operation_type", preferenceManager.getStringPreference(PreferenceManager.KEY_OPERATION_TYPE));

                if (preferenceManager.getStringPreference(PreferenceManager.KEY_OPERATION_TYPE).equalsIgnoreCase("office")) {
                    params.put("shift_no", "");

                }else {
                    params.put("shift_no", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_SHIFTS));
                }

                if (attendance_type.equals("gps_with_wifi")) {
                    if (preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equals("geo_permitted_distance")) {
                        params.put("employee_lat_long","");
                    }else{
                        params.put("employee_lat_long", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_LATITUDE_LONGITUDE));
                    }
                 }else{
                    params.put("employee_lat_long","");
                }
                params.put("type_of_attendance", typeAttendance);

                if (attendance_type.equals("gps_with_wifi")) {
                    params.put("wifi_name", wifinames);
                }else{
                    params.put("wifi_name", "");
                }
                Log.e("attendance params is", " " + params);
                return params;
            }

        };

        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(EmployeeActivity.this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    private void init()
    {

        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
       // calculateDistance();
        dist=showDistance();
        if(dist==-1)
        {
            final PrettyDialog prettyDialog = new PrettyDialog(EmployeeActivity.this);
            prettyDialog
                    .setTitle("Oops")
                    .setMessage("Error getting location , please try again later")
                    .setIcon(R.drawable.cross)
                    .addButton("retry", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                        @Override
                        public void onClick()
                        {

                            if(locationTrack==null)
                            {
                                locationTrack = new LocationTrack(EmployeeActivity.this);
                            }


                            if (locationTrack.canGetLocation())
                            {
                                longi = locationTrack.getLongitude();
                                lat = locationTrack.getLatitude();
                                latLang = lat+","+longi;
                                // Toast.makeText(this, latLang, Toast.LENGTH_SHORT).show();
                                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_LATITUDE_LONGITUDE, latLang);
                                init();
                            } else {
                                prettyDialog.dismiss();
                                locationTrack.showSettingsAlert();
                            }



                          prettyDialog.dismiss();

                            //finish();

                        }
                    }) .addButton("close", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                @Override
                public void onClick() {
                    prettyDialog.dismiss();

                }
            })
                    .show();
        }
    }
    private double showDistance()
    {

        Location locationA = new Location("Location A");

        locationA.setLatitude(office_latitude);

        locationA.setLongitude(office_lognitude);

        if(lat==0.0||longi==0.0)
        {
            dist=-1;
            return dist;
        }
        Location locationB = new Location("Location B");

        locationB.setLatitude(lat);

        locationB.setLongitude(longi);
        double distance=locationA.distanceTo(locationB);
        Log.d("distance",distance+"");

      return distance ;

    }

    //Calculate distance with permitted distance of office premises
    private double calculateDistance()
    {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat - office_latitude);
        double dLng = Math.toRadians(longi - office_lognitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(office_latitude)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        dist = (float) (earthRadius * c);

        return dist;
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //Exit App
    protected void exitByBackKey() {

        final Dialog dialog = new Dialog(EmployeeActivity.this);
// Include dialog.xml file
        dialog.setContentView(R.layout.dialog_exit);
        dialog.show();
        TextView textView=(TextView)dialog.findViewById(R.id.tv_quit_learning);
        textView.setText("Logout");
        TextView textView2=(TextView)dialog.findViewById(R.id.tv_description);
        textView2.setText("Do you want to logout");
        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { dialog.dismiss();
                Intent intent = new Intent(EmployeeActivity.this, Activity_PinLogin.class);
                startActivity(intent);
                finish();
            }
        });

// if decline button is clicked, close the custom dialog
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Close dialog
                dialog.dismiss();
            }
        });

    }
    public boolean isValidemp(String date_of_join)
    {
        String inputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);


        Date date = null;
        Date today=new Date();

        try {
            date = inputFormat.parse(date_of_join);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.before(today);
    }



}
