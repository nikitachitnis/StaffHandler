package com.miscos.staffhandler.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.miscos.staffhandler.Model.DayActivityModel;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.Urls.App_Urls;
import com.miscos.staffhandler.employee.Model.CompanyList;
import com.miscos.staffhandler.employee.Model.Employee;
import com.miscos.staffhandler.employee.Model.Manual;
import com.miscos.staffhandler.employee.Model.orgModel;
import com.miscos.staffhandler.employee.employeemodule.Activity_PinLogin;
import com.miscos.staffhandler.employee.employeemodule.EmployeeActivity;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

/*Developed under Miscos
 * Developed by Nikhil
 * 01-06-2020
 * */
public class organizationAdpater extends RecyclerView.Adapter<organizationAdpater.organizationAdapterHolder> {

    private List<orgModel> orgModelArrayList = new ArrayList<>();
    List<CompanyList> companyLists;
    private  Context context;
    String call_from;
    PreferenceManager preferenceManager;
    ProgressDialog progressDialog;

    public organizationAdpater(Context context, List<orgModel> orgModels, String call_from, ArrayList<CompanyList> companyLists) {
        this.context = context;
        this.orgModelArrayList = orgModels;
        this.call_from=call_from;
        this.companyLists=companyLists;
        preferenceManager=new PreferenceManager(context);
    }

    @NonNull
    @Override
    public organizationAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.multipleorgmodel, parent, false);

        return new organizationAdapterHolder(view);
    }


    @Override
    public void onBindViewHolder(final organizationAdapterHolder holder, int position)
    {


        if(call_from.equalsIgnoreCase("add"))
        {
            holder.empImage.setVisibility(View.VISIBLE);
            holder.empImage.setVisibility(View.GONE);
            holder.txtlabel2.setText("Name : ");
            holder.txtlabel2.setText("Organization: ");
        }
        else
        {
            holder.empImage.setVisibility(View.GONE);
            holder.txtlabel2.setText("Address : ");
            holder.txtlabel2.setVisibility(View.GONE);
            holder.txtlabel1.setVisibility(View.GONE);
            holder.txtlabel1.setText("Organization : ");
        }
        if(call_from.equalsIgnoreCase("add"))
        {
            orgModel orgModel = orgModelArrayList.get(position);
            holder.name.setText(orgModel.getName());
            holder.companyName.setText(orgModel.getCompanyName());
            if (!orgModelArrayList.get(position).getEmployeeImage().isEmpty())
            {
                Picasso.get()
                        .load(orgModel.getEmployeeImage())
                        .placeholder(R.drawable.loading_error)
                        .error(R.drawable.imgnot)
                        .into(holder.empImage);
            }
            else
            {

                holder.empImage.setImageResource(R.drawable.imgnot);
            }
        }
        else
        {
            CompanyList companyList=companyLists.get(position);
            holder.companyName.setText(companyList.getOfficeAddress());
            holder.name.setText(companyList.getCompanyName());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    callPinlogin(companyList);

                }
            });
        }


    }
    //login details
    private void callPinlogin(CompanyList companyList)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog = ProgressDialog.show(context, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
            String msg = employee.getMessage();
            String shiftNo = employee.getShiftNo();
            String office_status = employee.getOfficeStatus();
            String emplpoyee_status = employee.getEmployeeStatus();
            if (errorcode == 101)
            {
                String officeLatLong = employee.getEmployerData().get(0).getOfficeGpsLocation();
                String[] str = officeLatLong.split(",");
                String officaLat = str[0];
                String officeLongi = str[1];
                double latitude = Double.parseDouble(officaLat);
                double longitude = Double.parseDouble(officeLongi);
                preferenceManager.setPreference(PreferenceManager.KEY_USERNAME, companyList.getEmployeeId());
                preferenceManager.setPreference(PreferenceManager.KEY_PASSWORD, companyList.getPassword());
                preferenceManager.setPreference(PreferenceManager.KEY_LATITUDE, String.valueOf(latitude));
                preferenceManager.setPreference(PreferenceManager.KEY_LONGITUDE, String.valueOf(longitude));
                preferenceManager.setPreference(PreferenceManager.KEY_ATTENDANCETYPE, employee.getEmployerData().get(0).getAttendanceType());
                preferenceManager.setPreference(PreferenceManager.KEY_CURRENT_QR, employee.getEmployerData().get(0).getCurrentQr());
                preferenceManager.setPreference(PreferenceManager.KEY_PERMITTEDDISTANCE, employee.getEmployerData().get(0).getGeoPermittedDistance());
                preferenceManager.setPreference(PreferenceManager.KEY_OFFICE_GPS_PLOTTING, employee.getEmployerData().get(0).getOfficeGpsPlotting());
                preferenceManager.setPreference(PreferenceManager.KEY_GPS_METHOD, employee.getEmployerData().get(0).getGpsMethod());
                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYERID, employee.getEmployeeData().getEmployerId());
                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYER_NAME, employee.getEmployerData().get(0).getCompanyName());
                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEEID, employee.getEmployeeData().getEmployeeId());
                preferenceManager.setPreference(PreferenceManager.KEY_IN_TIMING, employee.getEmployerData().get(0).getInTiming());
                preferenceManager.setPreference(PreferenceManager.KEY_OUT_TIMING, employee.getEmployerData().get(0).getOutTiming());
                preferenceManager.setPreference(PreferenceManager.KEY_PRE_START, employee.getEmployerData().get(0).getPreStartTiming());
                preferenceManager.setPreference(PreferenceManager.KEY_POST_CLOSE, employee.getEmployerData().get(0).getPostCloseTiming());
                preferenceManager.setPreference(PreferenceManager.KEY_AADHAR_VERIFY_POLICY, employee.getEmployerData().get(0).getWantAdharVerification());
                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_NO_TYPE, employee.getEmployerData().get(0).getEmployeeNumberType());
                preferenceManager.setPreference(PreferenceManager.KEY_SHIFT_COUNT, employee.getEmployerData().get(0).getShiftCount());
                preferenceManager.setPreference(PreferenceManager.KEY_ENABLE_EMPLOYEE_FEEDBACK, employee.getEmployerData().get(0).getEnableSaveFeedbackEmployee());
                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_STATUS, emplpoyee_status);
                preferenceManager.setPreference(PreferenceManager.KEY_OFFICE_STATUS, office_status);

                preferenceManager.setPreference(PreferenceManager.KEY_HRMS_CONFIGURATION, employee.getHrms_congifured());
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
                Intent intent = new Intent(context, EmployeeActivity.class);
                context.startActivity(intent);
               if(context instanceof Activity_PinLogin)
               {
                   ((Activity_PinLogin) context).finish();
               }
            } else if (errorcode == 102) {
                final PrettyDialog prettyDialog = new PrettyDialog(context);
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
                final PrettyDialog prettyDialog = new PrettyDialog(context);

                prettyDialog
                        .setTitle("Status")
                        .setMessage(msg)
                        .setIcon(R.drawable.sad)
                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                progressDialog.dismiss();
                            }
                        })
                        .show();
            }else{
                final PrettyDialog prettyDialog= new PrettyDialog(context);
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
            final PrettyDialog prettyDialog = new PrettyDialog(context);
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
                params.put("login_pin",preferenceManager.getStringPreference(PreferenceManager.KEY_SET_UP_PIN));
                params.put("username", companyList.getEmployeeId());
                params.put("password", companyList.getPassword());
                params.put("organization_id", companyList.getEmployerId());
                params.put("token", preferenceManager.getStringPreference(PreferenceManager.KEY_DEVICE_TOKEN));
                Log.e("login pin params is", " " + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    public int getItemCount() {
        if(call_from.equalsIgnoreCase("add"))
        return orgModelArrayList.size();
        else
            return  companyLists.size();
    }

    public static class organizationAdapterHolder extends RecyclerView.ViewHolder{
        View itemView;
        TextView name,companyName,txtlabel1,txtlabel2;
        CircleImageView empImage;
       CardView cardView;

        public organizationAdapterHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            name = itemView.findViewById(R.id.tvName);
            companyName = itemView.findViewById(R.id.tvComName);
            empImage = itemView.findViewById(R.id.empPic);
            txtlabel1=itemView.findViewById(R.id.txtlabel1);
            txtlabel2=itemView.findViewById(R.id.txtlabel2);
            cardView=itemView.findViewById(R.id.cardMultiOrg);
        }
    }
}
