package com.miscos.staffhandler.employee.employeemodule;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.miscos.staffhandler.Adapters.organizationAdpater;
import com.miscos.staffhandler.MainActivity;
import com.miscos.staffhandler.MyFirebaseMessagingService;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.Urls.App_Urls;
import com.miscos.staffhandler.activities.AddEmployee;
import com.miscos.staffhandler.activities.UpdateEmployee;
import com.miscos.staffhandler.employee.Model.CompanyList;
import com.miscos.staffhandler.employee.Model.Employee;
import com.miscos.staffhandler.employee.Model.Manual;
import com.miscos.staffhandler.employee.Model.MutiOrganizationResponse;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.fivemin.chief.nonetworklibrary.networkBroadcast.NoNet;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.miscos.staffhandler.employee.network.LocationTrack;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import static com.miscos.staffhandler.employee.helper.PreferenceManager.KEY_DEVICE_TOKEN;
import static com.miscos.staffhandler.employee.helper.PreferenceManager.KEY_MOBILE_NUMBER;

/*Developed under Miscos
 * Developed by Nikhil
 * 05-06-2020
 * */
public class Activity_PinLogin extends AppCompatActivity {

    private ProgressDialog progressDialog;
    PinView edit_LoginPinEnter;
    Button btn_btnSubmit;
    String emplpoyee_status, office_status, msg,shiftNo, OTP,pdfA,pdfB,pdfC,pdfD,pdfE,pdfF,pdfG,pdfH,pdfI,pdfJ,pdfK,pdfL,pdfM;
    String user_name;
    String user_pass;
    String statusCode;
    PreferenceManager preferenceManager;
    TextView textForgotpin,tvVersion;
    LocationTrack locationTrack;
    // No Internet Dialog
    private NoNet mNoNet;
    SharedPreferences.Editor editor;
    SharedPreferences sharedpreferences;
    PackageInfo pinfo;
    int versionNumber;
    String versionName, latestVersion,device_token;
    private FragmentManager fm = null;
    Dialog dialog_verify;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpin);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.fcm_message);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_DEFAULT));
        }
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("TAG", "Key: " + key + " Value: " + value);
            }
        }
        Log.d("TAG", "Subscribing to topic");
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("SHLite")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d("TAG", msg);
                    }
                });
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        device_token = task.getResult().getToken();
                        // Log and toast
                       // String msg = getString(R.string.msg_subscribed, device_token);
                        preferenceManager.setPreference(KEY_DEVICE_TOKEN,device_token);
                        startService(new Intent(Activity_PinLogin.this, MyFirebaseMessagingService.class));
                        Log.d("TAG", "");
                    }
                });
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        fm = getSupportFragmentManager();
        mNoNet = new NoNet();
        mNoNet.initNoNet(this, fm);
        preferenceManager = new PreferenceManager(Activity_PinLogin.this);
        btn_btnSubmit = findViewById(R.id.btnSubmit);
        tvVersion = findViewById(R.id.tvVersion);
        textForgotpin = findViewById(R.id.textForgotpin);
        edit_LoginPinEnter = findViewById(R.id.editEnterLoginpin);
        progressDialog = new ProgressDialog(Activity_PinLogin.this);
        progressDialog = ProgressDialog.show(Activity_PinLogin.this, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.dismiss();
        versionNumber = pinfo.versionCode;
        versionName = pinfo.versionName;
        Log.d("TAG", "onCreate: " + versionName);
        tvVersion.setText("Version "+versionName);
        user_name = preferenceManager.getStringPreference(PreferenceManager.KEY_USERNAME);
        user_pass = preferenceManager.getStringPreference(PreferenceManager.KEY_PASSWORD);
//
//        if (user_name == "") {
//            Intent intent = new Intent(Activity_PinLogin.this, RegisterActivity.class);
//            startActivity(intent);
//            finish();
//
//        }

        // No Internet Dialog

        textForgotpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final PrettyDialog prettyDialog= new PrettyDialog(Activity_PinLogin.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Forgot Password")
                        .setMessage("OTP will be sent to XXXXXXX"+preferenceManager.getStringPreference(PreferenceManager.KEY_MOBILE_NUMBER).substring(7,10)+"\nDo you want to continue?")
                        .setIcon(R.drawable.info,R.color.white,null)
                        .addButton("Yes", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                                sendCredentials();

                            }
                        })
                        .addButton("No", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                            }
                        })
                        .show();

            }
        });

        btn_btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pin = edit_LoginPinEnter.getText().toString();
                if (TextUtils.isEmpty(pin) || pin.length() < 4) {
                    Toast.makeText(Activity_PinLogin.this, "Enter Valid Pin", Toast.LENGTH_SHORT).show();
                }
                else if(!pin.equalsIgnoreCase(preferenceManager.getStringPreference(PreferenceManager.KEY_SET_UP_PIN)))
                {
                    Toast.makeText(Activity_PinLogin.this, "Enter Correct Pin", Toast.LENGTH_SHORT).show();

                }else if (user_name == null || user_pass == null) {
                    Intent intent = new Intent(Activity_PinLogin.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //callPinlogin();
                    checkMultiOrg(pin);
                }

            }
        });
        locationTrack = new LocationTrack(Activity_PinLogin.this);
        locationTrack.getLocation();

        if (locationTrack.canGetLocation())
        {
            String latLang = locationTrack.getLatitude()+","+locationTrack.getLongitude();
            // Toast.makeText(this, latLang, Toast.LENGTH_SHORT).show();
            preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_LATITUDE_LONGITUDE, latLang);
        } else {
            locationTrack.showSettingsAlert();
        }

    }


    //login details
    private void callPinlogin()
    {
        progressDialog.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.VERIFY_USER, response -> {
            Log.d("login response is", " " + response);
            if(!(response == null)){

            }
            progressDialog.dismiss();
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            Employee employee = gson.fromJson(response, Employee.class);
            Manual manual = gson.fromJson(response, Manual.class);

            int errorcode = employee.getErrorCode();
            msg = employee.getMessage();
            shiftNo = employee.getShiftNo();
            office_status = employee.getOfficeStatus();
            emplpoyee_status = employee.getEmployeeStatus();
            if (errorcode == 101)
            {
                String officeLatLong = employee.getEmployerData().get(0).getOfficeGpsLocation();
                String[] str = officeLatLong.split(",");
                String officaLat = str[0];
                String officeLongi = str[1];
                double latitude = Double.parseDouble(officaLat);
                double longitude = Double.parseDouble(officeLongi);

                preferenceManager.setPreference(PreferenceManager.KEY_LATITUDE, String.valueOf(latitude));
                preferenceManager.setPreference(PreferenceManager.KEY_LONGITUDE, String.valueOf(longitude));
                preferenceManager.setPreference(PreferenceManager.KEY_ATTENDANCETYPE, employee.getEmployerData().get(0).getAttendanceType());
                preferenceManager.setPreference(PreferenceManager.KEY_CURRENT_QR, employee.getEmployerData().get(0).getCurrentQr());
                preferenceManager.setPreference(PreferenceManager.KEY_PERMITTEDDISTANCE, employee.getEmployerData().get(0).getGeoPermittedDistance());
                preferenceManager.setPreference(PreferenceManager.KEY_OFFICE_GPS_PLOTTING, employee.getEmployerData().get(0).getOfficeGpsPlotting());
                preferenceManager.setPreference(PreferenceManager.KEY_GPS_METHOD, employee.getEmployerData().get(0).getGpsMethod());
                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYERID, employee.getEmployeeData().getEmployerId());
                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEEID, employee.getEmployeeData().getEmployeeId());
                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYER_NAME, employee.getEmployerData().get(0).getCompanyName());
                preferenceManager.setPreference(PreferenceManager.KEY_IN_TIMING, employee.getEmployerData().get(0).getInTiming());
                preferenceManager.setPreference(PreferenceManager.KEY_OUT_TIMING, employee.getEmployerData().get(0).getOutTiming());
                preferenceManager.setPreference(PreferenceManager.KEY_PRE_START, employee.getEmployerData().get(0).getPreStartTiming());
                preferenceManager.setPreference(PreferenceManager.KEY_POST_CLOSE, employee.getEmployerData().get(0).getPostCloseTiming());
                preferenceManager.setPreference(PreferenceManager.KEY_AADHAR_VERIFY_POLICY, employee.getEmployerData().get(0).getWantAdharVerification());
                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_NO_TYPE, employee.getEmployerData().get(0).getEmployeeNumberType());
                preferenceManager.setPreference(PreferenceManager.KEY_SHIFT_COUNT, employee.getEmployerData().get(0).getShiftCount());
                preferenceManager.setPreference(PreferenceManager.KEY_ENABLE_EMPLOYEE_FEEDBACK, employee.getEmployerData().get(0).getEnableSaveFeedbackEmployee());


                preferenceManager.setPreference(PreferenceManager.KEY_HRMS_CONFIGURATION, employee.getHrms_congifured());
                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_STATUS, emplpoyee_status);
                preferenceManager.setPreference(PreferenceManager.KEY_OFFICE_STATUS, office_status);
                preferenceManager.setPreference(PreferenceManager.KEY_GETA, employee.getManualList().get(0).getA());
                preferenceManager.setPreference(PreferenceManager.KEY_GETB, employee.getManualList().get(0).getB());
                preferenceManager.setPreference(PreferenceManager.KEY_GETC, employee.getManualList().get(0).getC());
                preferenceManager.setPreference(PreferenceManager.KEY_GETD, employee.getManualList().get(0).getD());
                preferenceManager.setPreference(PreferenceManager.KEY_GETE, employee.getManualList().get(0).getE());
                preferenceManager.setPreference(PreferenceManager.KEY_GETF, employee.getManualList().get(0).getF());
                preferenceManager.setPreference(PreferenceManager.KEY_GETG, employee.getManualList().get(0).getG());
                preferenceManager.setPreference(PreferenceManager.KEY_GETH, employee.getManualList().get(0).getH());
                preferenceManager.setPreference(PreferenceManager.KEY_GETI, employee.getManualList().get(0).getI());
                preferenceManager.setPreference(PreferenceManager.KEY_GETJ, employee.getManualList().get(0).getJ());
                preferenceManager.setPreference(PreferenceManager.KEY_GETK, employee.getManualList().get(0).getK());
                preferenceManager.setPreference(PreferenceManager.KEY_GETL, employee.getManualList().get(0).getL());
                preferenceManager.setPreference(PreferenceManager.KEY_GETM, employee.getManualList().get(0).getM());
                preferenceManager.setPreference(PreferenceManager.KEY_WEEK_OFF, employee.getEmployeeData().getWeekOff());
                preferenceManager.setPreference(PreferenceManager.KEY_NEW_ARRANGEMENT_LIST, employee.getEmployeeData().getNewArrangementList());
                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_IN_TIMING, employee.getEmployeeData().getInTiming());
                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_OUT_TIMING, employee.getEmployeeData().getOutTiming());
                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_PRE_START, employee.getEmployeeData().getPreStartTiming());
                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_POST_CLOSE, employee.getEmployeeData().getPostCloseTiming());
                preferenceManager.setPreference(PreferenceManager.KEY_SPECIAL_DUTY, employee.getEmployeeData().getSpecialDuty());
                preferenceManager.setPreference(PreferenceManager.KEY_SPECIAL_DUTY_APPLICABLE_DATE, employee.getEmployeeData().getSpecial_duty_applicable_from_date());
                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_TYPE, employee.getEmployeeData().getType());
                preferenceManager.setPreference(PreferenceManager.KEY_REGISTER_NEW_EMPLOYEE, employee.getEmployeeData().getRegisterNewEmp());
                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_REPORT, employee.getEmployeeData().getEmployeeReport());
                preferenceManager.setPreference(PreferenceManager.KEY_POLICY_CONFIGURATION, employee.getEmployeeData().getPolicyConfiguration());
                preferenceManager.setPreference(PreferenceManager.KEY_SHIFT_MANAGEMENT, employee.getEmployeeData().getShiftManagement());
                preferenceManager.setPreference(PreferenceManager.KEY_SPECIAL_MANAGEMENT, employee.getEmployeeData().getSpecialManagement());
                preferenceManager.setPreference(PreferenceManager.KEY_ROSTER_MANAGEMENT, employee.getEmployeeData().getRosterManagement());
                preferenceManager.setPreference(PreferenceManager.KEY_HOLIDAY_ATTENDANCE, employee.getEmployeeData().getHolidayAttendance());
                preferenceManager.setPreference(PreferenceManager.KEY_HOLIDAY_MANAGEMENT, employee.getEmployeeData().getHolidayManagement());
                preferenceManager.setPreference(PreferenceManager.KEY_OTHER_SETTINGS, employee.getEmployeeData().getOtherSettings());
                preferenceManager.setPreference(PreferenceManager.KEY_EMER_LOGIN_LOGOUT, employee.getEmployeeData().getViewEmergencyLoginLogout());
                preferenceManager.setPreference(PreferenceManager.KEY_PERMITTED_FOR_EMER, employee.getEmployeeData().getPermittedForEmergency());
                preferenceManager.setPreference(PreferenceManager.KEY_OPERATION_TYPE, employee.getEmployeeData().getOperationType());
                preferenceManager.setPreference(PreferenceManager.KEY_ALERT_ON_HOLIDAY, employee.getEmployeeData().getAlertOnHoliday());
                preferenceManager.setPreference(PreferenceManager.KEY_WIFI_NAMES, employee.getEmployerData().get(0).getWifiNames());
                preferenceManager.setPreference(PreferenceManager.KEY_WIFI_MANAGEMENT, employee.getEmployeeData().getWifiManagement());
                preferenceManager.setPreference(PreferenceManager.KEY_LEAVE_MANAGEMENT, employee.getEmployeeData().getLeaveManagement());
                preferenceManager.setPreference(PreferenceManager.KEY_SALARY_MANAGEMENT, employee.getEmployeeData().getSalaryPayment());
                preferenceManager.setPreference(PreferenceManager.KEY_OLD_EMP_DATA, employee.getEmployeeData().getViewOldEmployeeData());
                preferenceManager.setPreference(PreferenceManager.KEY_MANUAL_ATTENDANCE, employee.getEmployeeData().getEmployeeManualAttendance());
                preferenceManager.setPreference(PreferenceManager.KEY_MANUAL_ATTENDANCE_TYPE, employee.getEmployeeData().getEmployeeManualAttendanceType());
                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_SHIFTS, shiftNo);
                preferenceManager.setPreference(PreferenceManager.KEY_SUPERVISOR_NAME, employee.getEmployeeData().getShift_incharge_name());

                Intent intent = new Intent(Activity_PinLogin.this, EmployeeActivity.class);
                startActivity(intent);
                finish();
            } else if (errorcode == 102) {
                final PrettyDialog prettyDialog = new PrettyDialog(Activity_PinLogin.this);
                prettyDialog
                        .setTitle("Incorrect")
                        .setMessage(msg)
                        .setIcon(R.drawable.cross)
                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                            }
                        })
                        .show();
            } else if (errorcode == 105) {
                final PrettyDialog prettyDialog = new PrettyDialog(Activity_PinLogin.this);

                prettyDialog
                        .setTitle("Status")
                        .setMessage(msg)
                        .setIcon(R.drawable.sad)
                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {

                                prettyDialog.dismiss();
                            }
                        })
                        .show();
            }else{
                final PrettyDialog prettyDialog= new PrettyDialog(Activity_PinLogin.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Login")
                        .setMessage(msg)
                        .setIcon(R.drawable.cross,R.color.white,null)
                        .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                            }
                        })
                        .show();
            }


        }, error -> {
            progressDialog.dismiss();
            final PrettyDialog prettyDialog = new PrettyDialog(Activity_PinLogin.this);
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
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mode", "login");
                params.put("login_pin", edit_LoginPinEnter.getText().toString());
                params.put("username", user_name);
                params.put("password", user_pass);
                params.put("organization_id", "");
                params.put("token", device_token);
                Log.e("login pin params is", " " + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(Activity_PinLogin.this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    private void checkMultiOrg(String pin)
    {
        progressDialog.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.VERIFY_USER, response -> {
            Log.d("login response is", " " + response);
           String msg="";
           int errorcode=0;
            progressDialog.dismiss();
            try {
                JSONObject jsonObject=new JSONObject(response);
                errorcode=jsonObject.getInt("error_code");
                msg=jsonObject.getString("message");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (errorcode == 101)
            {  GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                MutiOrganizationResponse  mutiOrganizationResponse= gson.fromJson(response, MutiOrganizationResponse.class);

                ArrayList<CompanyList>companyLists=new ArrayList<>();
                if(mutiOrganizationResponse.getCompanyList().size()>1)
                {
                    for(CompanyList companyList:mutiOrganizationResponse.getCompanyList())
                    {
                        if(companyList.getDeviceId().isEmpty())
                            companyLists.add(companyList);

                    }
                    Dialog  dialog = new Dialog(Activity_PinLogin.this);
                    if(companyLists.size()==0)
                    {
                        companyLists.clear();
                        companyLists.addAll(mutiOrganizationResponse.getCompanyList());
                        organizationAdpater organizationAdpater = new organizationAdpater(this, null,"login",companyLists);

                        // Include dialog.xml file
                        dialog.setContentView(R.layout.multipleorg);
                        dialog.setCancelable(false);
                        Button buttonSubmit = (Button) dialog.findViewById(R.id.btn_yes);
                        Button buttonNo = (Button) dialog.findViewById(R.id.btn_no);
                        TextView tvMain=dialog.findViewById(R.id.tvMain);
                        tvMain.setText("Please choose organization to proceed for login");
                        buttonSubmit.setVisibility(View.GONE);
                        buttonNo.setVisibility(View.GONE);
                        ImageView imgCancel = (ImageView) dialog.findViewById(R.id.imCancel);
                        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.relative_org);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(organizationAdpater);

                        imgCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                    }
                    else
                    {

                        for(CompanyList companyList:mutiOrganizationResponse.getCompanyList())
                        {
                            if(companyList.getDeviceId().isEmpty())
                            {
                                final PrettyDialog prettyDialog= new PrettyDialog(Activity_PinLogin.this);
                                prettyDialog.setCancelable(false);
                                prettyDialog
                                        .setTitle("Login")
                                        .setMessage("New registration with employer "+companyList.getCompanyName()+" is found,Do you want to continue to verify credentials?")
                                        .setIcon(R.drawable.info,R.color.white,null)
                                        .addButton("Maybe later", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {

                                                prettyDialog.dismiss();
                                                companyLists.clear();
                                                for(CompanyList companyList:mutiOrganizationResponse.getCompanyList())
                                                {
                                                    if(!companyList.getDeviceId().isEmpty())
                                                    {
                                                        companyLists.add(companyList);
                                                    }
                                                }
                                                if(companyLists.size()==1)
                                                {
                                                    callPinlogin();
                                                }
                                                else
                                                    {

                                                        organizationAdpater organizationAdpater = new organizationAdpater(Activity_PinLogin.this, null,"login",companyLists);

                                                        // Include dialog.xml file
                                                        dialog.setContentView(R.layout.multipleorg);
                                                        dialog.setCancelable(false);
                                                        Button buttonSubmit = (Button) dialog.findViewById(R.id.btn_yes);
                                                        Button buttonNo = (Button) dialog.findViewById(R.id.btn_no);
                                                        TextView tvMain=dialog.findViewById(R.id.tvMain);
                                                        tvMain.setText("Please choose organization to proceed for login");
                                                        buttonSubmit.setVisibility(View.GONE);
                                                        buttonNo.setVisibility(View.GONE);
                                                        ImageView imgCancel = (ImageView) dialog.findViewById(R.id.imCancel);
                                                        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.relative_org);
                                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Activity_PinLogin.this);
                                                        recyclerView.setLayoutManager(mLayoutManager);
                                                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                                                        recyclerView.setAdapter(organizationAdpater);

                                                        imgCancel.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                        dialog.show();
                                                }
                                            }
                                        }).addButton("Yes", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                    @Override
                                    public void onClick() {
                                        prettyDialog.dismiss();
                                        //show uname password dialog
                                        showdialog();
                                    }
                                })
                                        .show();
                                break;
                            }

                        }


                    }

                }
                else if(mutiOrganizationResponse.getCompanyList().size()==1)
                {
                    callPinlogin();
                }

            } else if (errorcode == 102) {
                final PrettyDialog prettyDialog = new PrettyDialog(Activity_PinLogin.this);
                prettyDialog
                        .setTitle("Login")
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
                final PrettyDialog prettyDialog= new PrettyDialog(Activity_PinLogin.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Login")
                        .setMessage(msg)
                        .setIcon(R.drawable.cross,R.color.white,null)
                        .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                            }
                        })
                        .show();
            }


        }, error -> {
            progressDialog.dismiss();
            final PrettyDialog prettyDialog = new PrettyDialog(Activity_PinLogin.this);
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
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mode", "get_multiple_organization");
                params.put("mobile_no", preferenceManager.getStringPreference(PreferenceManager.KEY_MOBILE_NUMBER));
                params.put("login_pin", pin);
                Log.e("org  params is", " " + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(Activity_PinLogin.this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
void showdialog()
{
    dialog_verify = new Dialog(Activity_PinLogin.this);
    // Include dialog.xml file
    dialog_verify.setContentView(R.layout.dialog_for_verify_credentials);
    dialog_verify.setCancelable(false);
    Button buttonSubmit = (Button) dialog_verify.findViewById(R.id.btnsubmit);
    TextInputLayout txtusername,txtpassword;
    txtpassword=dialog_verify.findViewById(R.id.editPassword);
    txtusername=dialog_verify.findViewById(R.id.editUsername);

    buttonSubmit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String uname = txtusername.getEditText().getText().toString();

           String password = txtpassword.getEditText().getText().toString();
            if (uname.length() == 0) {
                txtusername.setError("Please enter Username");
                // Toast.makeText(SignUpActivity.this,"Please enter valid phone number",Toast.LENGTH_SHORT).show();
                return;
            } else {
                txtusername.setError(null);
            }

            if (password.length() == 0) {
                txtpassword.setError("Please Enter Password");
                return;
            } else {
                txtpassword.setError(null);
            }
            userRegistration(uname,password);
        }
    });



    dialog_verify.show();
}
    private void userRegistration(String uname,String pass) {

        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
/*        preferenceManager.setPreference(PreferenceManager.KEY_USERNAME, user_name);
        preferenceManager.setPreference(PreferenceManager.KEY_PASSWORD, pass);*/
        StringRequest stringRequest = new StringRequest(Request.Method.POST, App_Urls.VERIFY_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("register_response", " " + response);
                        progressDialog.dismiss();
                        String verifystatusCode="",message="";
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            verifystatusCode= jsonObject.getString("error_code");
                           message = jsonObject.getString("message");
                          //  OTP = jsonObject.getString("otp");



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        switch (verifystatusCode) {
                            case "101":
                                dialog_verify.dismiss();
                                final PrettyDialog prettyDialog = new PrettyDialog(Activity_PinLogin.this);
                                prettyDialog.setCancelable(false);
                                prettyDialog
                                        .setTitle("Verified")
                                        .setMessage("Verified Successfully")
                                        .setIcon(R.drawable.check)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog.dismiss();
                                            }
                                        })
                                        .show();
                                break;
                            case "100":
                                Toast.makeText(Activity_PinLogin.this, "Parameter Missing", Toast.LENGTH_SHORT).show();
                                break;
                            case "102":
                                Toast.makeText(Activity_PinLogin.this, message, Toast.LENGTH_SHORT).show();
                                break;
                            case "103":
                                Toast.makeText(Activity_PinLogin.this, message, Toast.LENGTH_SHORT).show();
                                break;
                            case "104":
                                Toast.makeText(Activity_PinLogin.this, message, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(Activity_PinLogin.this, message, Toast.LENGTH_SHORT).show();
                                break;
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        final PrettyDialog prettyDialog = new PrettyDialog(Activity_PinLogin.this);
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
                params.put("mode", "verify");
                params.put("contact", preferenceManager.getStringPreference(PreferenceManager.KEY_MOBILE_NUMBER));
                params.put("username",uname);
                // params.put("device_id", "");
                params.put("password", pass);
                Log.e("verify params is", " " + params);
                return params;
            }

        };

        requestQueue.add(stringRequest);


    }
    private void sendCredentials()
    {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, App_Urls.VERIFY_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("otp response is", " " + response);
                        progressDialog.dismiss();
//                        Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            statusCode = jsonObject.getString("error_code");
                            OTP = jsonObject.getString("otp");
                            switch (statusCode) {
                                case "101":
                                    send_dialog();
                                    break;
                                case "102":
                                    Toast.makeText(Activity_PinLogin.this, "Resend OTP", Toast.LENGTH_SHORT).show();
                                    break;
                                case "103":
                                    Toast.makeText(Activity_PinLogin.this, "Contact Is Empty", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Activity_PinLogin.this, R.string.connection_error, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mode", "send_otp");
                params.put("contact", preferenceManager.getStringPreference(PreferenceManager.KEY_MOBILE_NUMBER));
                Log.e("register params is", " " + params);
                return params;
            }

        };
        // Adding request to request queue
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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
    private void send_dialog() {
        otp_dialog();
       /* final PrettyDialog prettyDialog = new PrettyDialog(Activity_PinLogin.this);
        prettyDialog.setCancelable(false);
        prettyDialog
                .setTitle("Password")
                .setMessage("OTP has been Sent Successfully.")
                .setIcon(R.drawable.send, R.color.primaryTextColor, null)
                .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        prettyDialog.dismiss();
                        otp_dialog();
                    }
                })
                .show();*/
    }

    private void otp_dialog()
    {
        final Dialog dialog = new Dialog(Activity_PinLogin.this);
// Include dialog.xml file
        dialog.setContentView(R.layout.otp_dialog);
        dialog.setCancelable(false);
        dialog.show();
        final TextView txtresend=dialog.findViewById(R.id.txtresend);
        txtresend.setEnabled(false);

        //progressBar=dialog.findViewById(R.id.circularCountdown);
       CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                txtresend.setTextColor(getResources().getColor(R.color.gray));
                txtresend.setEnabled(false);
                txtresend.setText("Resend (" + millisUntilFinished / 1000 + ")");
            }

            public void onFinish() {
                txtresend.setText("Resend");
                txtresend.setTextColor(getResources().getColor(R.color.colorPrimary));
                txtresend.setClickable(true);
                txtresend.setEnabled(true);
            }
        };
        countDownTimer.start();
        txtresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                txtresend.setEnabled(false);
                dialog.dismiss();
                sendCredentials();


            }
        });
        PinView etOTP =  dialog.findViewById(R.id.edtOTP);
        Button btnVerify = (Button) dialog.findViewById(R.id.btnverify);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = etOTP.getText().toString();
                if (otp.equals(OTP)) {
                    dialog.dismiss();
                    final PrettyDialog prettyDialog = new PrettyDialog(Activity_PinLogin.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("OTP")
                            .setMessage("Contact Verified Successfully.")
                            .setIcon(R.drawable.send, R.color.primaryTextColor, null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    Intent intent = new Intent(Activity_PinLogin.this, Activity_SetupPin.class);
                                    startActivity(intent);
                                    finish();
                                    prettyDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    Toast.makeText(Activity_PinLogin.this, "OTP does not matched", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // or finish();
    }
}
