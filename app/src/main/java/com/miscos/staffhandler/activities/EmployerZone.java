package com.miscos.staffhandler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miscos.staffhandler.hrms_management.PolicyAndConfiguration;
import com.miscos.staffhandler.hrms_management.SalTransferNCalculations.SalaryCalculationAndTransfer;
import com.miscos.staffhandler.hrms_management.hrms.activity.LeaveAuthorityActivity;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.Urls.App_Urls;
import com.miscos.staffhandler.databinding.ActivityEmployerZoneBinding;
import com.miscos.staffhandler.employee.Model.Employee;
import com.miscos.staffhandler.employee.employeemodule.EmployeeActivity;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.miscos.staffhandler.employer_java.activity.HolidayManagement;
import com.miscos.staffhandler.employer_java.activity.MainActivity;
import com.miscos.staffhandler.shiftmanagement.fragments.HolidayAttendance;
import com.miscos.staffhandler.shiftmanagement.fragments.AdminSettings;
import com.miscos.staffhandler.shiftmanagement.fragments.RosterManagement;
import com.miscos.staffhandler.shiftmanagement.fragments.ShiftManagement;
import com.miscos.staffhandler.shiftmanagement.fragments.SpecialManagement;
import com.miscos.staffhandler.shiftmanagement.fragments.WifiManagement;
import com.fivemin.chief.nonetworklibrary.networkBroadcast.NoNet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.miscos.staffhandler.ssa.departdesignation.DepartAndDesignationActivity;
import com.miscos.staffhandler.ssa.leavemanagment.LeaveManagementActivity;
import com.miscos.staffhandler.ssa.policynruledocumentation.PolicyAndRuleDocumentActivity;
import com.miscos.staffhandler.ssa.shiftwiseattendance.ShiftWiseAttendActivity;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

/*Developed under Miscos
 * Developed by Nikhil
 * 05-06-2020
 * */

public class EmployerZone extends AppCompatActivity
{

 /*   LinearLayout tvEntries, tvEmInfo, tvPolicy, tvHelp,tvMore,tvAdminsetting,txtmanualAttendance,tvShiftmanagement,tvRostermngnt;*/
    PreferenceManager preferenceManager;
    String employerId,emplpoyee_status, office_status, msg,user_name,user_pass,setUpPin,employee, employer,shiftNo;
    String password;
    ActivityEmployerZoneBinding binding;
    ImageView ImBack;
    // No Internet Dialog
    private NoNet mNoNet;
    private FragmentManager fm = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_employer_zone);
        fm = getSupportFragmentManager();
        mNoNet = new NoNet();
        mNoNet.initNoNet(this, fm);
        preferenceManager = new PreferenceManager(this);
        //loginDetails();

        employee = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEEID);
        employer = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID);

        ImBack = findViewById(R.id.imBack);
        /*tvMore = findViewById(R.id.tvMore);*/

        employerId = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID);
        password = preferenceManager.getStringPreference(PreferenceManager.KEY_PASSWORD);
        user_name = preferenceManager.getStringPreference(PreferenceManager.KEY_USERNAME);
        user_pass = preferenceManager.getStringPreference(PreferenceManager.KEY_PASSWORD);


        showSetting(PreferenceManager.KEY_OLD_EMP_DATA,binding.tvOldEmpdata,binding.dvOldemp);

        showSetting(PreferenceManager.KEY_LEAVE_MANAGEMENT,binding.tvLeaveRequests,binding.dvleaverequet);
        final TextView specialManagement = findViewById(R.id.tvSpeManage);
        showSetting(PreferenceManager.KEY_SPECIAL_MANAGEMENT,specialManagement,binding.dvSpecalMngnt);
        final TextView holidayAttendance =findViewById(R.id.tvHoliAtten);
        showSetting(PreferenceManager.KEY_HOLIDAY_ATTENDANCE,holidayAttendance,binding.dvHolidayAttendance);
        showSetting(PreferenceManager.KEY_WIFI_MANAGEMENT,binding.tvWifi,binding.dvOthersetting);
        showSetting(PreferenceManager.KEY_HOLIDAY_MANAGEMENT,binding.txtholidaymngnt,binding.dvHolidaymanagement);
        binding.txtholidaymngnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(EmployerZone.this, ShiftWiseAttendActivity.class);
                startActivity(intent);
            }
        });
        binding.dvManualLeavemanagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(EmployerZone.this, LeaveManagementActivity.class);
                startActivity(intent);
            }
        });
        showSetting(PreferenceManager.KEY_SHIFT_MANAGEMENT,binding.tvShiftManagement,binding.dvshiftmanagment);
        showSetting(PreferenceManager.KEY_ROSTER_MANAGEMENT,binding.tvRosterManagement,binding.dvroster);
        showSetting(PreferenceManager.KEY_EMPLOYEE_REPORT,binding.tvReports,binding.dvReports);
        binding.tvReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(EmployerZone.this, PolicyAndRuleDocumentActivity.class));
            }
        });

        showSetting(PreferenceManager.KEY_MANUAL_ATTENDANCE,binding.txtmanualAttendance,binding.dvManualAttendance);
        binding.dvshiftmanagment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(EmployerZone.this, ShiftManagement.class));
            }
        });
        binding.dvroster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(EmployerZone.this, RosterManagement.class));
            }
        });
        binding.dvManualAttendance.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(EmployerZone.this, ManualAttendanceActivity.class));
            }
        });
        binding.dvDepartment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(EmployerZone.this, DepartAndDesignationActivity.class));
            }
        });
showSetting(PreferenceManager.KEY_REGISTER_NEW_EMPLOYEE,binding.tvEntries,binding.dvEmployeelist);
        //showSetting(PreferenceManager.KEY_POLICY_CONFIGURATION,binding.tvPolicy,binding.dvPolicyConfig);
        showSetting(PreferenceManager.KEY_SALARY_MANAGEMENT,binding.tvSalaryAndCal,binding.dvSalTransfer);
        if(preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID).equals(preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEEID)))
        {
            binding.tvPolicyandConfig.setVisibility(View.VISIBLE);
            binding.dvHrmsPolicy.setVisibility(View.VISIBLE);
            binding.tvEmInfo.setVisibility(View.VISIBLE);
            binding.dvEmployerinfo.setVisibility(View.VISIBLE);
            binding.tvAdminSetting.setVisibility(View.VISIBLE);
            binding.dvAdminSetting.setVisibility(View.VISIBLE);
            binding.dvDepartment.setVisibility(View.VISIBLE);


        }
        else
        {
            binding.dvDepartment.setVisibility(View.GONE);
            binding.tvPolicyandConfig.setVisibility(View.VISIBLE);
            binding.dvHrmsPolicy.setVisibility(View.GONE);
            binding.tvEmInfo.setVisibility(View.GONE);
            binding.dvEmployerinfo.setVisibility(View.GONE);
            binding.tvAdminSetting.setVisibility(View.GONE);
            binding.dvAdminSetting.setVisibility(View.GONE);


        }
        binding.dvHolidayAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(EmployerZone.this, HolidayAttendance.class));
                //finish();


            }
        });

        binding.dvSpecalMngnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(EmployerZone.this, SpecialManagement.class));
                //finish();


            }
        });

        binding.dvOthersetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(EmployerZone.this, WifiManagement.class));



            }
        });
        binding.dvHrmsPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployerZone.this, PolicyAndConfiguration.class));
                //finish();

            }
        });
        binding.dvSalTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployerZone.this, SalaryCalculationAndTransfer.class));

                // finish();
            }
        });
        binding.dvOldemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployerZone.this, OldEmpActivity.class));
                //finish();

            }
        });
        binding.dvleaverequet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployerZone.this, LeaveAuthorityActivity.class));

                //finish();
            }
        });
        ImBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployerZone.this, EmployeeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //condition for zone according to their id's

            binding.dvAdminSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (employee.equalsIgnoreCase(employer)) {
                        startActivity(new Intent(EmployerZone.this, AdminSettings.class));
                    }else {
                        allow_access();
                    }

                }
            });

      /*  tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                BottpmSheetDialog();
            }
        });*/

        binding.tvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployerZone.this, HelpandSupport.class));
                finish();
            }
        });

        binding.dvEmployeelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferenceManager.getStringPreference(PreferenceManager.KEY_REGISTER_NEW_EMPLOYEE).equalsIgnoreCase("Y")){
                    startActivity(new Intent(EmployerZone.this, com.miscos.staffhandler.MainActivity.class));


                }else {
                    allow_access();
                }

            }
        });

        binding.dvPolicyConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (preferenceManager.getStringPreference(PreferenceManager.KEY_POLICY_CONFIGURATION).equalsIgnoreCase("Y")) {
                    Intent i = new Intent(EmployerZone.this, MainActivity.class);
                    i.putExtra("type", "form3");
                    i.putExtra("employerId", employerId);
                    startActivity(i);
                }else {
                    allow_access();

                }*/
                Intent i = new Intent(EmployerZone.this, MainActivity.class);
                i.putExtra("type", "form3");
                i.putExtra("employerId", employerId);
                startActivity(i);

            }
        });

        binding.dvEmployerinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_TYPE).equalsIgnoreCase("owner")) {
                    Intent i = new Intent(EmployerZone.this, MainActivity.class);
                    i.putExtra("type", "form2");
                    i.putExtra("employerId", employerId);
                    i.putExtra("password", password);
                    startActivity(i);
                }else{
                    allow_access();
                }
            }
        });



    }
    @Override
    protected void onResume() {
        loginDetails();
        mNoNet.RegisterNoNet();
        super.onResume();

    }

    @Override
    protected void onPause() {
        mNoNet.unRegisterNoNet();
        super.onPause();
    }

    //retrieve login details
    private void loginDetails()
    {
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.VERIFY_USER, response -> {
            Log.e("login_response", " " + response);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            Employee employee = gson.fromJson(response, Employee.class);

            int errorcode = employee.getErrorCode();
            msg = employee.getMessage();
            shiftNo = employee.getShiftNo();
            office_status = employee.getOfficeStatus();
            emplpoyee_status = employee.getEmployeeStatus();
            preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_STATUS, emplpoyee_status);
            preferenceManager.setPreference(PreferenceManager.KEY_OFFICE_STATUS, office_status);

            if (errorcode == 101) {
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
                preferenceManager.setPreference(PreferenceManager.KEY_IN_TIMING, employee.getEmployerData().get(0).getInTiming());
                preferenceManager.setPreference(PreferenceManager.KEY_OUT_TIMING, employee.getEmployerData().get(0).getOutTiming());
                preferenceManager.setPreference(PreferenceManager.KEY_PRE_START, employee.getEmployerData().get(0).getPreStartTiming());
                preferenceManager.setPreference(PreferenceManager.KEY_POST_CLOSE, employee.getEmployerData().get(0).getPostCloseTiming());
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
                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_SHIFTS, shiftNo);
            } else {
                Toast.makeText(EmployerZone.this, msg, Toast.LENGTH_LONG).show();
            }


        }, error -> {
            final PrettyDialog prettyDialog = new PrettyDialog(EmployerZone.this);
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
                params.put("login_pin", preferenceManager.getStringPreference(PreferenceManager.KEY_SET_UP_PIN));
                params.put("username", user_name);
                params.put("password", user_pass);
                params.put("organization_id", "");
                params.put("token", preferenceManager.getStringPreference(PreferenceManager.KEY_DEVICE_TOKEN));
                Log.e("login pin params is", " " + params);
                return params;
            }
        };
        //Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(EmployerZone.this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    private void allow_access()
    {
        final PrettyDialog prettyDialog= new PrettyDialog(EmployerZone.this);
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
    //backpress
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
    void showSetting(String key, View textView, LinearLayout view)
    {
        if(preferenceManager.getStringPreference(key).equalsIgnoreCase("N")||preferenceManager.getStringPreference(key).equalsIgnoreCase(""))
        {
            textView.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }

        else
        {
            textView.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
        }

    }

}
