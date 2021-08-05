package com.miscos.staffhandler.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miscos.staffhandler.activities.AddEmployee;
import com.miscos.staffhandler.activities.EmployerZone;
import com.miscos.staffhandler.Adapters.EmployeeAdapter;
import com.miscos.staffhandler.LocalDatabase.SqLiteHelper;
import com.miscos.staffhandler.Model.EmployeeListModel;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.Urls.App_Urls;
import com.miscos.staffhandler.employee.Model.EmployeeList;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.fivemin.chief.nonetworklibrary.networkBroadcast.NoNet;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeManagementFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    EmployeeListModel employeeListModel;
    private List<EmployeeListModel> employeeListModels = new ArrayList<>();
    private List<EmployeeListModel> filetremployeeListModels = new ArrayList<>();
    private SwipeRefreshLayout pullToRefresh;
    private EmployeeAdapter employeeAdapter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private String employerID,noEmployee = "",operationType;
    private BottomSheetBehavior mBehavior;
    CoordinatorLayout coordinatorLayout;
    LinearLayout layoutBottomSheet;
    private SqLiteHelper db;
    private Button addEmployee;
    private RelativeLayout rlMain;
    EditText edtsearch;
    private TextView tvNoData;
    // No Internet Dialog
    private NoNet mNoNet;
    private FragmentManager fm = null;
    private PreferenceManager preferenceManager;
    public EmployeeManagementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employee_management, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        db = new SqLiteHelper(getContext());
        preferenceManager = new PreferenceManager(getContext());
        //settingToolBar();
        tvNoData = view.findViewById(R.id.tvNodata);
        addEmployee = view.findViewById(R.id.addEmployee);
        rlMain= view.findViewById(R.id.rlMain);
        ImageView tvBack = view.findViewById(R.id.imBack);
        TextView imAdd = view.findViewById(R.id.imAddEmployee);
        employerID = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID);
        recyclerView = view.findViewById(R.id.rcEmployeeList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        employeeAdapter = new EmployeeAdapter(getContext(), filetremployeeListModels);
        edtsearch=view.findViewById(R.id.search_emp_editText);
        edtsearch.addTextChangedListener(new TextWatcher() {
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
                filterAdmin(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(this::EmployeeActivity);
        //EmployeeActivity();
        pullToRefresh.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        pullToRefresh.post(() -> {
            pullToRefresh.setRefreshing(true);
            EmployeeActivity();
            employeeAdapter.notifyDataSetChanged();
        });

        // coordinatorLayout=findViewById(R.id.coordinatorLayout);
        // sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);


        imAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddEmployee.class));
                getActivity().finish();
            }
        });
        addEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddEmployee.class));
                getActivity().finish();
            }
        });
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(getContext(), EmployerZone.class));
                getActivity().finish();
            }
        });

        // No Internet Dialog

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
    void filterAdmin(String text)
    {
        if(text.isEmpty())
        {
            filetremployeeListModels.clear();
            filetremployeeListModels.addAll(employeeListModels);
            employeeAdapter.notifyDataSetChanged();
            return;
        }
        filetremployeeListModels.clear();
        for (EmployeeListModel d : employeeListModels)
        {
            if (d.getEmployee_name().toLowerCase().contains(text.toLowerCase())||d.getDepartment_name().toLowerCase().startsWith(text.toLowerCase())||d.getDesignation_name().toLowerCase().startsWith(text.toLowerCase()))
            {
                filetremployeeListModels.add(d);
            }
        }
        employeeAdapter.notifyDataSetChanged();
    }
    @Override
    public void onRefresh() {
        EmployeeActivity();
        employeeAdapter.notifyDataSetChanged();
    }

    public void EmployeeActivity()
    {
        pullToRefresh.setRefreshing(true);
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
                    employeeListModels.clear();
                    filetremployeeListModels.clear();
                    db.deleteEmployee();
                    JSONArray jsonArrayData=jsonObject.getJSONArray("employee_data");
                    for(int i=0;i<jsonArrayData.length();i++)
                    {
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
                        employeeListModel.setEmployee_no(dayActivityList.getString("employee_no"));
                        employeeListModel.setPermanent_address(dayActivityList.getString("parment_address"));
                        employeeListModel.setAadhar_no(dayActivityList.getString("adhar_no"));
                        employeeListModel.setIsVerified(dayActivityList.getString("adhar_no_verified"));
                        employeeListModel.setEmployement_type(dayActivityList.getString("employment_type"));
                        employeeListModel.setPanNo(dayActivityList.getString("pan_no"));
                        employeeListModel.setDate_of_birth(dayActivityList.getString("date_of_birth"));
                        employeeListModel.setDesignation(dayActivityList.getString("designation"));
                        employeeListModel.setContarct_name(dayActivityList.getString("contract_company_name"));
                        employeeListModel.setContract_type(dayActivityList.getString("contract_type"));
                        employeeListModel.setIsHavingSmartPhone(dayActivityList.getString("having_smart_phone"));
                        employeeListModel.setDepartment_id(dayActivityList.getString("department_id"));
                        employeeListModel.setDesignation_name(dayActivityList.getString("designation_name"));
                        employeeListModel.setDepartment_name(dayActivityList.getString("department_name"));
                        db.addEmployeeDetails(employeeListModel);
                        employeeListModels.add(employeeListModel);
                    }
                    preferenceManager.setPreference(PreferenceManager.KEY_DATA_SPINNER, "data");
                    filetremployeeListModels.addAll(employeeListModels);
                    employeeAdapter = new EmployeeAdapter(getContext(),filetremployeeListModels);
                    recyclerView.setAdapter(employeeAdapter);
                    employeeAdapter.notifyDataSetChanged();
                    if (employeeListModels.size() == 0) {
                        tvNoData.setVisibility(View.VISIBLE);
                        addEmployee.setVisibility(View.VISIBLE);
                        rlMain.setVisibility(View.GONE);
                    }else {
                        rlMain.setVisibility(View.VISIBLE);
                        tvNoData.setVisibility(View.GONE);
                        addEmployee.setVisibility(View.GONE);
                    }
                } else if (error_code == 102) {
                    db.deleteEmployee();
                    tvNoData.setVisibility(View.VISIBLE);
                    addEmployee.setVisibility(View.VISIBLE);
                    rlMain.setVisibility(View.GONE);
                    preferenceManager.setPreference(PreferenceManager.KEY_DATA_SPINNER,"nodata");
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
            pullToRefresh.setRefreshing(false);
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pullToRefresh.setRefreshing(false);
                tvNoData.setVisibility(View.VISIBLE);
                rlMain.setVisibility(View.GONE);
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
                params.put("employer_id", employerID);
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

    private void settingToolBar()
    {
        toolbar.setTitle("Employee Entries");
        toolbar.setLogo(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

}
