package com.miscos.staffhandler.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miscos.staffhandler.Model.EmployeeListModel;
import com.miscos.staffhandler.Urls.App_Urls;
import com.miscos.staffhandler.activities.EmployeeDayActivity;
import com.miscos.staffhandler.LocalDatabase.SqLiteHelper;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.miscos.staffhandler.employee.network.LocationTrack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;

public class DayActivityFragment extends Fragment implements View.OnClickListener {


    private Spinner employeeSpiner,sp1;
    private SqLiteHelper db;
    private PreferenceManager preferenceManager;
    private String day_status,attendance_date="",emplyoeeID,emplyoerID,strFrom,dateCheck="",statusCode,msg,specialDuty,strtext,strTime,reportFor,attendanceType,type_of_attendance;
    private String dd = "00", mm = "00", yy = "0000";
    private TextView selectDate,selectTime;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    Button btnTime,btndate;
    RadioGroup rdgroup;
    double lat;
    double longi;
    String latLang,latitude, longitude,permittedDistance;
    double office_latitude, office_lognitude;
    LocationTrack locationTrack;
    // No Internet Dialog

    private static final String TAG = DayActivityFragment.class.getCanonicalName();


    //date picker
    private DatePickerDialog.OnDateSetListener myDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            int m = month + 1;
            dd = String.valueOf(dayOfMonth);
            mm = String.valueOf(m);
            yy = String.valueOf(year);
            onDateSetChange(view, year, month, dayOfMonth);
        }};
    public DayActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day_activity, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        //settingToolBar();
       // ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        db =  new SqLiteHelper(getContext());
        preferenceManager = new PreferenceManager(getContext());
        emplyoerID = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID);
        TextView txtFromDate = view.findViewById(R.id.tvSelectDate);
        selectDate  =  view.findViewById(R.id.tvselectedDate);
        toolbar = view.findViewById(R.id.toolbar);
        employeeSpiner = view.findViewById(R.id.spinnerEmployee);
        sp1 = view.findViewById(R.id.spinnerEmployee1);
        selectTime = view.findViewById(R.id.tvSetTime);
        reportFor="both";
        btndate=view.findViewById(R.id.tvDate);
        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                long now = System.currentTimeMillis() - 1000;
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.DATE, +1);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), myDateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 24));
                datePickerDialog.getDatePicker().setMaxDate(now);
                datePickerDialog.show();
                dateCheck="attendance";


            }
        });
        rdgroup=view.findViewById(R.id.alertRadioGroup);
        longi = LocationTrack.getLongitude();
        lat = LocationTrack.getLatitude();
       permittedDistance = preferenceManager.getStringPreference(PreferenceManager.KEY_PERMITTEDDISTANCE);
        latLang = lat+","+longi;
        latitude = preferenceManager.getStringPreference(PreferenceManager.KEY_LATITUDE);
        longitude = preferenceManager.getStringPreference(PreferenceManager.KEY_LONGITUDE);

        office_latitude = Double.parseDouble(latitude);
        office_lognitude = Double.parseDouble(longitude);




        rdgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                switch (radioGroup.getCheckedRadioButtonId())
                {
                    case R.id.rdbtnofficestaff:
                        reportFor="office";
                        break;
                    case R.id.rdbtnshiftstaff:
                        reportFor="shift";
                        break;
                    case R.id.rdbtnBoth:
                        reportFor="both";
                        break;
                }

            }
        });
        Button btnGo = view.findViewById(R.id.btnGoForDateSelect);
        Button btnDayIn = view.findViewById(R.id.btnDayIn);
        Button btnDayOut = view.findViewById(R.id.btnDayOut);
        btnTime = view.findViewById(R.id.btnTime);
        ImageView tvBack = view.findViewById(R.id.imBack);


        //set spinner text if no data
        strtext=preferenceManager.getStringPreference(PreferenceManager.KEY_DATA_SPINNER);


        //Progress Bar
        progressDialog = new ProgressDialog(getContext());
        progressDialog = ProgressDialog.show(getContext(), null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.dismiss();


        txtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                long now = System.currentTimeMillis() - 1000;
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.DATE, +1);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), myDateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 24));
                datePickerDialog.getDatePicker().setMaxDate(now);
                datePickerDialog.show();
                dateCheck = "From";
            }
        });


        btnTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                int second = mcurrentTime.get(Calendar.SECOND);
                TimePickerDialog mTimePicker;


                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        btnTime.setText("" + selectedHour + ":" + selectedMinute + ":" +second);
                        selectTime.setText(selectedHour + ":" + selectedMinute + ":" + second );
                        strTime = selectTime.getText().toString();
                    }
                }, hour, minute, true);//Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        //set spinner text if no data
        if (strtext.equals("nodata"))
        {


        }else if(strtext.equals("data"))
        {

        }


        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getActivity().finish();
            }
        });


        btnGo.setOnClickListener(this);
        btnDayIn.setOnClickListener(this);
        btnDayOut.setOnClickListener(this);
        return view;

    }
    private double showDistance()
    {

        Location locationA = new Location("Location A");

        locationA.setLatitude(office_latitude);

        locationA.setLongitude(office_lognitude);

        if(lat==0.0||longi==0.0)
        {
            double dist=-1;
            return dist;
        }
        Location locationB = new Location("Location B");

        locationB.setLatitude(lat);

        locationB.setLongitude(longi);
        double distance=locationA.distanceTo(locationB);
        Log.d("distance",distance+"");

        return distance ;

    }
    @Override
    public void onResume() {
        super.onResume();
        EmployeeActivity();
    }

    public void EmployeeActivity()
    {

        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.Employee_Management, response -> {
            Log.e("show_employee_data", " " + response);

            try {
                JSONArray jsonArray=new JSONArray(response);
                JSONObject jsonObject=jsonArray.getJSONObject(0);
                int error_code=jsonObject.getInt("error_code");
                String msg=jsonObject.getString("message");
                if (error_code == 101)
                {

                    db.deleteEmployee();
                    JSONArray jsonArrayData=jsonObject.getJSONArray("employee_data");
                    for(int i=0;i<jsonArrayData.length();i++){
                        JSONObject dayActivityList =jsonArrayData.getJSONObject(i);
                        EmployeeListModel employeeListModel= new EmployeeListModel();
                        employeeListModel.setEm_id(dayActivityList.getString("id"));
                        employeeListModel.setEmployer_id(dayActivityList.getString("employer_id"));
                        employeeListModel.setEmployee_id(dayActivityList.getString("employee_id"));
                        employeeListModel.setPassword(dayActivityList.getString("password"));
                        employeeListModel.setLogin_pin(dayActivityList.getString("login_pin"));
                        employeeListModel.setMobile_no(dayActivityList.getString("mobile_no"));
                        employeeListModel.setEmployee_name(dayActivityList.getString("name"));
                        employeeListModel.setDesignation(dayActivityList.getString("designation"));
                        employeeListModel.setAddress(dayActivityList.getString("address"));
                        employeeListModel.setEmail_id(dayActivityList.getString("email_id"));
                        employeeListModel.setEmployee_pic(dayActivityList.getString("employee_pic"));
                        employeeListModel.setCreated_on(dayActivityList.getString("created_on"));
                        employeeListModel.setAge(dayActivityList.getString("age"));
                        employeeListModel.setGender(dayActivityList.getString("gender"));
                        employeeListModel.setStatus(dayActivityList.getString("status"));
                        employeeListModel.setDevice_id(dayActivityList.getString("device_id"));
                        employeeListModel.setLogin_status(dayActivityList.getString("login_status"));
                        employeeListModel.setDevice_os(dayActivityList.getString("device_os"));
                        employeeListModel.setDevice_version(dayActivityList.getString("device_version"));
                        employeeListModel.setNotification_status(dayActivityList.getString("notification_status"));
                        employeeListModel.setWeek_off(dayActivityList.getString("week_off"));
                        employeeListModel.setNew_arrangement_list(dayActivityList.getString("new_arrangement_list"));
                        employeeListModel.setIn_timing(dayActivityList.getString("in_timing"));
                        employeeListModel.setOut_timing(dayActivityList.getString("out_timing"));
                        employeeListModel.setPre_start_timing(dayActivityList.getString("pre_start_timing"));
                        employeeListModel.setPost_close_timing(dayActivityList.getString("post_close_timing"));
                        employeeListModel.setSpecial_duty(dayActivityList.getString("special_duty"));
                        employeeListModel.setEmployeeType(dayActivityList.getString("type"));
                        employeeListModel.setRegister_new_emp(dayActivityList.getString("register_new_emp"));
                        employeeListModel.setEmployee_report(dayActivityList.getString("employee_report"));
                        employeeListModel.setPolicy_configuration(dayActivityList.getString("policy_configuration"));
                        employeeListModel.setShift_management(dayActivityList.getString("shift_management"));
                        employeeListModel.setSpecial_management(dayActivityList.getString("special_management"));
                        employeeListModel.setRoster_management(dayActivityList.getString("roster_management"));
                        employeeListModel.setHoliday_attendance(dayActivityList.getString("holiday_attendance"));
                        employeeListModel.setHoliday_management(dayActivityList.getString("holiday_management"));
                        employeeListModel.setOther_settings(dayActivityList.getString("other_settings"));
                        employeeListModel.setView_emergency_login_logout(dayActivityList.getString("view_emergency_login_logout"));
                        employeeListModel.setPermitted_for_emergency(dayActivityList.getString("permitted_for_emergency"));
                        employeeListModel.setEmployee_manual_attendance(dayActivityList.getString("employee_manual_attendance"));
                        employeeListModel.setOperation_type(dayActivityList.getString("operation_type"));
                        employeeListModel.setAlert_on_holiday(dayActivityList.getString("alert_on_holiday"));
                        employeeListModel.setShift_assign_list(dayActivityList.getString("shift_assign_list"));
                        employeeListModel.setShift_assign_list(dayActivityList.getString("shift_assign_list"));
                        employeeListModel.setDate_of_joining_date(dayActivityList.getString("date_of_joining"));
                        db.addEmployeeDetails(employeeListModel);
                    }
                    //set spinner text if data available
                    sp1.setVisibility(GONE);
                    spinerData();
                    preferenceManager.setPreference(PreferenceManager.KEY_DATA_SPINNER, "data");


                } else if (error_code == 102)
                {
                    PrettyDialog prettyDialog = new PrettyDialog(getActivity());
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle(getActivity().getString(R.string.app_name))
                            .setMessage("No employees found")
                            .setIcon(R.drawable.ic_close, R.color.color_white, null)
                            .addButton("Okay", R.color.color_white, R.color.blue, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                    getActivity().finish();
                                }
                            })
                            .show();
                    db.deleteEmployee();
                    preferenceManager.setPreference(PreferenceManager.KEY_DATA_SPINNER,"nodata");
                    sp1.setEnabled(false);
                    sp1.setVisibility(GONE);

                    final List<String> flatType = new ArrayList<String>();
                    flatType.add(0, "No Employees");
                    ArrayAdapter<String> flatListAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item,
                            flatType) {
                        @Override
                        public View getDropDownView(int position, View convertView,
                                                    ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);

                            TextView tv = (TextView) view;
                            if (position == 0) {
                                // Set the hint text color gray
                                tv.setTextColor(Color.GRAY);
                            } else {
                                tv.setTextColor(Color.BLACK);
                            }
                            return view;
                        }
                    };

                    flatListAdapter.setDropDownViewResource(R.layout.spinner_item);
                    sp1.setAdapter(flatListAdapter);
                    sp1.setSelection(0);
                } else if (error_code == 103) {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                final Dialog dialog1 = new Dialog(getContext());
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

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                final PrettyDialog prettyDialog = new PrettyDialog(getContext());
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
                Map<String, String> params = new HashMap<>();
                params.put("mode", "show");
                params.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
                Log.e("show employee params", " " + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //setting spinner
    private void spinerData(){
        ArrayList<String> buildingNameList = new ArrayList<>();
        final ArrayList<String> buildingIDList = new ArrayList<>();
        Cursor cursor = db.getEmployeelist();
        if (cursor != null) {
            Log.e("count", cursor.getCount() + "");
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast())
                {
                    String doj=cursor.getString(cursor.getColumnIndex(SqLiteHelper.COLUMN_EMPLOYEE_DATE_OF_JOINING));
                    if(isValidemp(doj))
                    {
                        buildingNameList.add(cursor.getString(cursor.getColumnIndex(SqLiteHelper.COLUMN_EMPLOYEE_NAME)));
                        buildingIDList.add(cursor.getString(cursor.getColumnIndex(SqLiteHelper.COLUMN_EMPLOYEE_ID)));
                    }

                    cursor.moveToNext();
                }
            }
        }

        ArrayAdapter<String> latrinesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_item,
                buildingNameList) {
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);

                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        latrinesAdapter.setDropDownViewResource(R.layout.spinner_items2);
        employeeSpiner.setAdapter(latrinesAdapter);


        buildingNameList.add(0,"Select Employee");
        employeeSpiner.setSelection(0);


        employeeSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if (position!=0)
                {
                    emplyoeeID = buildingIDList.get(position-1);
                    strTime="";
                    attendance_date="";
                    btndate.setText("Select Date");
                    btnTime.setText("Select Time");
                    Log.d(TAG, "onItemSelected: " + emplyoeeID);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void onDateSetChange(DatePicker datePicker, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTimeInMillis(0);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Log.d(" PICK TIME ", " " + cal.getTime());
            cal.set(year, month, day);
            cal.clear(Calendar.HOUR_OF_DAY);
            cal.clear(Calendar.AM_PM);
            cal.clear(Calendar.MINUTE);
            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.MILLISECOND);

            Date chosenDate = cal.getTime();

            String strDateFrom = df.format(chosenDate);

            if (dateCheck.equalsIgnoreCase("From")) {
                selectDate.setText(dd + "-" + mm + "-" + yy);
                strFrom = strDateFrom;
                Log.d(TAG, "onDateSetChange:strTo " + selectDate.getText().toString());
            }
            else
            {

                btndate.setText(dd + "-" + mm + "-" + yy);
                attendance_date=strDateFrom;
            }

        } catch (Exception ex) {
            Log.e("Date picker ", " Exception is " + ex.toString());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

//Day in
    private void DayAttendance(final String status,final String attendance_type)
    {
        progressDialog.show();
       /* getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, App_Urls.EMPLOYEE_ATTENDANCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d("day_activity response", " " + response);
                     //Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String statusCode = jsonObject.getString("error_code");
                            String   msg = jsonObject.getString("message");
                             employeeSpiner.setSelection(0);
                             btndate.setText("Select Date");
                             attendance_date="";
                            btnTime.setText("Select Time");
                            strTime="";

                        progressDialog.dismiss();
                        switch (statusCode)
                        {
                            case "101":
                                final PrettyDialog prettyDialog= new PrettyDialog(getContext());
                                prettyDialog.setCancelable(false);
                                prettyDialog
                                        .setTitle("Day Status")
                                        .setMessage(msg)
                                        .setIcon(R.drawable.check,R.color.primaryTextColor,null)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback()
                                        {
                                            @Override
                                            public void onClick() {

                                                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_STATUS, "in");
                                                prettyDialog.dismiss();
                                            }
                                        })
                                        .show();
                                break;
                            case "100":
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                break;
                            case "102":
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                break;
                            case "103":
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                break;
                            case "104":
                                final PrettyDialog prettyDialog2= new PrettyDialog(getContext());
                                prettyDialog2.setCancelable(false);
                                prettyDialog2
                                        .setTitle("Day Status")
                                        .setMessage(msg)
                                        .setIcon(R.drawable.check,R.color.white,null)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog2.dismiss();
                                            }
                                        })
                                        .show();
                                break;
                            case "105":
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                break;
                            case "106":
                                final PrettyDialog prettyDialog1= new PrettyDialog(getContext());
                                prettyDialog1.setCancelable(false);
                                prettyDialog1
                                        .setTitle("Day Status")
                                        .setMessage(msg)
                                        .setIcon(R.drawable.cross,R.color.white,null)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog1.dismiss();
                                            }
                                        })
                                        .show();
                                break;
                            case "107":
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                break;
                            case "110":
                                final PrettyDialog prettyDialog3= new PrettyDialog(getContext());
                                prettyDialog3.setCancelable(false);
                                prettyDialog3
                                        .setTitle("Day Status")
                                        .setMessage(msg)
                                        .setIcon(R.drawable.cross,R.color.white,null)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog3.dismiss();
                                            }
                                        })
                                        .show();
                                break;
                            default:
                                final PrettyDialog prettyDialog4= new PrettyDialog(getContext());
                                prettyDialog4.setCancelable(false);
                                prettyDialog4
                                        .setTitle("Day Status")
                                        .setMessage(msg)
                                        .setIcon(R.drawable.cross,R.color.white,null)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog4.dismiss();
                                            }
                                        })
                                        .show();
                                break;
                        }
                        } catch (JSONException e) {
                            final PrettyDialog prettyDialog2= new PrettyDialog(getContext());
                            prettyDialog2.setCancelable(false);
                            prettyDialog2
                                    .setMessage("Error Parsing Data")
                                    .setIcon(R.drawable.cross,R.color.white,null)
                                    .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            prettyDialog2.dismiss();
                                        }
                                    })
                                    .show();
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // it will check for day status for day in or day out accordingly mode parameter will be passed
                if (status.equals("day_in")) {
                    params.put("mode", "day_in");
                }
                if (status.equals("day_out")) {
                    params.put("mode", "day_out");
                }
                params.put("employer_id", emplyoerID);
                params.put("employee_id", emplyoeeID);
                params.put("date", attendance_date);
                params.put("attendance_marked_by", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEEID));
                params.put("force", "1");
                params.put("time", strTime);
                params.put("operation_type", preferenceManager.getStringPreference(PreferenceManager.KEY_OPERATION_TYPE));
                params.put("type_of_attendance", attendance_type);
                params.put("employee_lat_long", "");

                params.put("wifi_name", "");

                if (preferenceManager.getStringPreference(PreferenceManager.KEY_OPERATION_TYPE).equalsIgnoreCase("office")) {
                    params.put("shift_no", "");

                }else {
                    params.put("shift_no", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_SHIFTS));
                }
                Log.e("dayInOut params is", " " + params);
                return params;
            }

        };

        requestQueue.add(stringRequest);
    }

    public void reportTypeDailog(){
        View itemLayoutView = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialogforreporttype, null);
        final Button btnCancel = itemLayoutView.findViewById(R.id.btnCancel);
        final TextView shiftStaff = itemLayoutView.findViewById(R.id.shiftStaffReport);
        final TextView officeStaff = itemLayoutView.findViewById(R.id.offStaffType);
        final TextView bothStaff = itemLayoutView.findViewById(R.id.bothStaffReport);

        new BottomSheetDialog(getContext());
        BottomSheetDialog dialog;
        dialog = new BottomSheetDialog(getActivity(),R.style.TransparentDialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(itemLayoutView);
        dialog.setCancelable(true);
        BottomSheetDialog finalDialog = dialog;

        shiftStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportFor = "shift";
                attendanceType = "all";
                send_data();
                finalDialog.dismiss();
            }
        });

        officeStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportFor = "office";
                attendanceType = "all";
                send_data();
                finalDialog.dismiss();
            }
        });
        bothStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportFor = "both";
                attendanceType = "all";
                send_data();
                finalDialog.dismiss();
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

    //attendance_type_selection
    public void attendance_type_dialog_day(){
        View itemLayoutView = LayoutInflater.from(getActivity()).inflate(
                R.layout.attendace_type1, null);
        final Button btnCancel = itemLayoutView.findViewById(R.id.btnCancel);
        final TextView normalAttendance = itemLayoutView.findViewById(R.id.normalAttendance);
        final TextView specialAttendance = itemLayoutView.findViewById(R.id.specialAttendance);
        final TextView emergencyAttendance = itemLayoutView.findViewById(R.id.emergencyAttendance);

        new BottomSheetDialog(getContext());
        BottomSheetDialog dialog;
        dialog = new BottomSheetDialog(getActivity(),R.style.TransparentDialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(itemLayoutView);
        dialog.setCancelable(true);
        BottomSheetDialog finalDialog = dialog;

        normalAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!preferenceManager.getStringPreference(PreferenceManager.KEY_OFFICE_STATUS).equalsIgnoreCase("Working")){
                    final PrettyDialog prettyDialog = new PrettyDialog(getContext());
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Attendance Marking")
                            .setMessage("Today is Holiday, Can not mark attendance")
                            .setIcon(R.drawable.cross)
                            .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                    finalDialog.dismiss();
                                }
                            })

                            .show();
                }else{
                    if (preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_TYPE).equalsIgnoreCase("owner")||preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_TYPE).equalsIgnoreCase("admin")||preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_TYPE).equalsIgnoreCase("employee")) {
                        attendanceType = "normal";
                        DayAttendance(day_status,attendanceType);
                        finalDialog.dismiss();
                    }else if(preferenceManager.getStringPreference(PreferenceManager.KEY_PERMITTED_FOR_EMER).equalsIgnoreCase("N")||preferenceManager.getStringPreference(PreferenceManager.KEY_SPECIAL_DUTY).equalsIgnoreCase("")){
                        attendanceType = "normal";
                        DayAttendance(day_status,attendanceType);
                        finalDialog.dismiss();
                    }
                }


            }
        });

        specialAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferenceManager.getStringPreference(PreferenceManager.KEY_SPECIAL_DUTY).equalsIgnoreCase("Y")){
                    attendanceType = "special";
                    DayAttendance(day_status,attendanceType);
                    finalDialog.dismiss();
                }else{
                    final PrettyDialog prettyDialog= new PrettyDialog(getContext());
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Access Denied")
                            .setMessage("You don't have authority to mark attendance.")
                            .setIcon(R.drawable.cross,R.color.white,null)
                            .addButton("Close", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            })
                            .show();
                }

            }
        });
        emergencyAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferenceManager.getStringPreference(PreferenceManager.KEY_PERMITTED_FOR_EMER).equalsIgnoreCase("Y")){
                    attendanceType = "emergency";
                    DayAttendance(day_status,attendanceType);
                    finalDialog.dismiss();
                }else {
                    final PrettyDialog prettyDialog= new PrettyDialog(getContext());
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Access Denied")
                            .setMessage("You don't have authority to mark attendance.")
                            .setIcon(R.drawable.cross,R.color.white,null)
                            .addButton("Close", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            })
                            .show();
                }

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
    private void settingToolBar()  {
        toolbar.setTitle("Employee Entries");
        toolbar.setLogo(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    public void send_data(){
            Intent i = new Intent(getActivity(), EmployeeDayActivity.class);
            i.putExtra("date", strFrom);
            i.putExtra("report_for", reportFor);
            i.putExtra("attendance_type", attendanceType);
            startActivity(i);

    }

    private void allow_access(){
        final PrettyDialog prettyDialog= new PrettyDialog(getContext());
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
    //on click events
    @Override
    public void onClick(View v) {
        int id1 = v.getId();
        if (id1 == R.id.btnGoForDateSelect) {
            if (strFrom != null && strFrom.length() > 3)
            {
            //reportTypeDailog();
                attendanceType="all";
                send_data();
            } else {
                Toast.makeText(getContext(), "Please Select Date", Toast.LENGTH_LONG).show();
            }
        }

        if (id1 == R.id.btnDayIn) {

            if(preferenceManager.getStringPreference(PreferenceManager.KEY_MANUAL_ATTENDANCE_TYPE).equalsIgnoreCase("shift"))
            {
                double office_dist=showDistance();
                if(office_dist!=-1&&office_dist>Double.parseDouble(permittedDistance))
                {
                    final PrettyDialog prettyDialog = new PrettyDialog(getActivity());
                    prettyDialog
                            .setTitle("Oops")
                            .setMessage("You Are out of Office Premises.\n \n Your Distance from Office is " + office_dist+" Meters")
                            .setIcon(R.drawable.cross)
                            .addButton("Ok", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();

                                }
                            })
                            .show();
                    return;
                }
            }
            if(Validation())
            {
                day_status = "day_in";
                    attendance_type_dialog_day();

            }


        }
        if (id1 == R.id.btnDayOut) {
            if(preferenceManager.getStringPreference(PreferenceManager.KEY_MANUAL_ATTENDANCE_TYPE).equalsIgnoreCase("shift"))
            {
                double office_dist=showDistance();
                if(office_dist!=-1&&office_dist>Double.parseDouble(permittedDistance))
                {
                    final PrettyDialog prettyDialog = new PrettyDialog(getActivity());
                    prettyDialog
                            .setTitle("Oops")
                            .setMessage("You Are out of Office Premises.\n \n Your Distance from Office is " + office_dist+" Meters")
                            .setIcon(R.drawable.cross)
                            .addButton("Ok", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();

                                }
                            })
                            .show();
                    return;
                }
            }
                if (Validation())
                {
                    day_status = "day_out";
                    attendance_type_dialog_day();
                }
            }
    }

    private boolean Validation()
    {
        if (employeeSpiner.getSelectedItem()== null)
        {
            Toast.makeText(getContext(), "No Employees", Toast.LENGTH_SHORT).show();
            return  false;
        }
        if (employeeSpiner.getSelectedItem().toString().trim().equals("Select Employee")) {
            Toast.makeText(getContext(), "Select Employee", Toast.LENGTH_SHORT).show();
            return  false;
        }

        if (attendance_date.equalsIgnoreCase("")) {
            Toast.makeText(getContext(), "Select date to mark attendance for", Toast.LENGTH_SHORT).show();
            return  false;
        }
        if (strTime.equalsIgnoreCase("")) {
            Toast.makeText(getContext(), "Select time", Toast.LENGTH_SHORT).show();
            return  false;
        }
        return true;
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
