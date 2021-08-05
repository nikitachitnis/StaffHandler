package com.miscos.staffhandler.shiftmanagement.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.miscos.staffhandler.shiftmanagement.Api;
import com.miscos.staffhandler.shiftmanagement.adapters.AdminAdapter;
import com.miscos.staffhandler.shiftmanagement.adapters.AuthorityAdapter;
import com.miscos.staffhandler.shiftmanagement.adapters.EmployeeAdapter;
import com.miscos.staffhandler.shiftmanagement.models.Admin;
import com.miscos.staffhandler.shiftmanagement.models.AuthorityData;
import com.miscos.staffhandler.shiftmanagement.models.Employee;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonDataForAdmin;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonDataForAuthority;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonDataForEmployee;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonResponse;
import com.fivemin.chief.nonetworklibrary.networkBroadcast.NoNet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/*Developed under Miscos
 * Developed by Nikhil
 * 15-09-2020
 * */
public class AdminSettings extends AppCompatActivity implements View.OnClickListener {

    List<String> authorities = new ArrayList<>(Arrays.asList("Employee Addition and Suspend", "View Emergency Login/Logout", "Employee reports", "Emergency Duty", "Employee Manual attendance", "Shift Management", "Special Case Management", "Roster Management", "Policy Configuration", "Other Settings", "Holiday Attendance","Holiday Management","View Old Employee Data","Leave management","Salary Payment"));
    List<Boolean> authStatus = new ArrayList<>();
    RecyclerView authorityRecycler, adminRecycler;
    AuthorityAdapter authorityAdapter;
    EmployeeAdapter employeeAdapter;
    PreferenceManager preferenceManager;
    AdminAdapter adminAdapter;
    List<String> employeeNameList = new ArrayList<>(), adminNameList = new ArrayList<>();
    List<String> employeeIdList = new ArrayList<>(), adminIdList = new ArrayList<>();
    GetJsonDataForEmployee getJsonDataForEmployee;
    GetJsonDataForAdmin getJsonDataForAdmin;
    private ProgressDialog progressDialog;
    CardView adminCardView, permissionsCardView;
    FloatingActionButton addAdminBut;
    List<Employee> employees = new ArrayList<>();
    List<Admin> admins = new ArrayList<>();
    Handler handler;
    int selectedEmployee = -1, selectedAdmin = -1;
    Button btnGo, btnSave;
    EditText searchAdmin;
    GetJsonDataForAuthority getJsonDataForAuthority;
    AuthorityData authorityData;
    ImageView tvBack;
   public static  TextView tvNoData;
    String givenAuthorities = "", adminId = "", adminName = "", empId = "",employer_id;
    // No Internet Dialog
    private NoNet mNoNet;
    private FragmentManager fm = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      //  ((MainActivity) getActivity()).toolbar.setTitle("Admin Settings");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminsettings);
        preferenceManager =new PreferenceManager(AdminSettings.this);
        employer_id = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID);
        fm = getSupportFragmentManager();
        mNoNet = new NoNet();
        mNoNet.initNoNet(this, fm);
/*        progressBar = ((MainActivity) getActivity()).progressBar;*/
        //Progress Bar
        progressDialog = new ProgressDialog(this);
        progressDialog = ProgressDialog.show(AdminSettings.this, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();
        employeeAdapter = new EmployeeAdapter(AdminSettings.this, employees, this);
        authorityRecycler = findViewById(R.id.authorityRecycler);
        addAdminBut = findViewById(R.id.add_admin);
        searchAdmin = findViewById(R.id.search_admin_editText);
        adminRecycler = findViewById(R.id.permittedAdminsRecyclerView);
        adminCardView = findViewById(R.id.adminsCardView);
        tvBack =  findViewById(R.id.imBack);
        tvNoData =  findViewById(R.id.tvNodata);
        btnSave = findViewById(R.id.btnSave);
        permissionsCardView = findViewById(R.id.permissionsCardView);

        for (int i = 0; i < authorities.size(); i++) {
            authStatus.add(false);
        }
        authorityAdapter = new AuthorityAdapter(AdminSettings.this, authorities);
        authorityRecycler.setLayoutManager(new LinearLayoutManager(this));
        adminRecycler.setLayoutManager(new LinearLayoutManager(this));
        adminRecycler.addItemDecoration(new DividerItemDecoration(AdminSettings.this, DividerItemDecoration.VERTICAL));
        authorityRecycler.setAdapter(authorityAdapter);
        handler = new Handler(Looper.getMainLooper());
       /* ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);*/
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getAdmins();
            }
        }, 1000);
        addAdminBut.setOnClickListener(this);
        //btnGo.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        //Back Button
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_admin:
                getAdmins();
                if(employees.isEmpty())
                {
                    Toast.makeText(this, "No Employee Found,Please Add employee from employee entries.", Toast.LENGTH_SHORT).show();
                }else{
                    //employees.clear();
                    try {
                        for (int i = 0; i < employees.size(); i++) {
                            employeeNameList.add(employees.get(i).getName());
                            employeeIdList.add(employees.get(i).getEmployeeId());
                        }
                        employeeAdapter = new EmployeeAdapter(AdminSettings.this, employees, this);
                        final Dialog dialog = new Dialog(AdminSettings.this);
// Include dialog.xml file
                        dialog.setContentView(R.layout.dialog_recycler);

                        TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                        RecyclerView employeeRecycler = dialog.findViewById(R.id.rv_description);
                        final Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                        EditText searchField = dialog.findViewById(R.id.search_editText);
                        heading.setText("Employee List");
                        btnYes.setText("Add Admin");
                        btnNo.setText("Cancel");
                        employeeRecycler.setLayoutManager(new LinearLayoutManager(this));
                        employeeRecycler.addItemDecoration(new DividerItemDecoration(AdminSettings.this, DividerItemDecoration.VERTICAL));
                        employeeRecycler.setAdapter(employeeAdapter);
                        dialog.show();
                        searchField.setHint("Search employee by name");
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
                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                PrettyDialog prettyDialog = new PrettyDialog(AdminSettings.this);
                                prettyDialog.setCancelable(false);
                                prettyDialog
                                        .setTitle(AdminSettings.this.getString(R.string.app_name))
                                        .setMessage("Are you sure to proceed?")
                                        .setIcon(R.drawable.ic_done, R.color.color_white, null)
                                        .addButton("Yes", R.color.color_white, R.color.colorPrimary, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick()
                                            {
                                                prettyDialog.dismiss();
                                                employees = employeeAdapter.employeeArray;
                                                for (int i = 0; i < employees.size(); i++) {
                                                    if (employees.get(i).isSelected()) {
                                                        if (empId.matches("")) {
                                                            empId = employees.get(i).getEmployeeId();
                                                        } else {
                                                            empId = empId + "," + employees.get(i).getEmployeeId();
                                                        }
                                                    }
                                                }
                                                if (empId.matches(""))
                                                {
                                                    Toast.makeText(AdminSettings.this, "Select Employee", Toast.LENGTH_SHORT).show();
                                                } else
                                                    {
                                                    dialog.dismiss();
                                                    progressDialog.show();
                                                    (Api.getClient()).setAdmin("set_admin", employer_id, empId).enqueue(new Callback<GetJsonResponse>() {
                                                        @Override
                                                        public void onResponse(Call<GetJsonResponse> call, Response<GetJsonResponse> response) {
                                                            try {
                                                                if (response.body().getErrorCode() == 101) {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(AdminSettings.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                                    getAdmins();
                                                                } else {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(AdminSettings.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                                progressDialog.dismiss();
                                                                final Dialog dialog1 = new Dialog(AdminSettings.this);
                                                                dialog1.setContentView(R.layout.dialog_exit);
                                                                dialog1.show();

                                                                TextView heading1 = dialog1.findViewById(R.id.tv_quit_learning);
                                                                TextView description1 = dialog1.findViewById(R.id.tv_description);
                                                                Button btnNo1 = (Button) dialog1.findViewById(R.id.btn_no);
                                                                Button btnYes1 = (Button) dialog1.findViewById(R.id.btn_yes);
                                                                heading1.setText("Error");
                                                                description1.setText("An unfortunate error occured, please try again.");
                                                                btnNo1.setVisibility(View.GONE);
                                                                btnYes1.setText("Ok");
                                                                btnYes1.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        dialog1.dismiss();
                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<GetJsonResponse> call, Throwable t) {

                                                        }
                                                    });
                                                }
                                            }
                                        })
                                        .addButton("No", R.color.color_white, R.color.red_800, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick()
                                            {
prettyDialog.dismiss();
                                            }
                                        })
                                        .show();

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
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(AdminSettings.this, "Error getting Employee Data", Toast.LENGTH_SHORT).show();
                    }
                }


                break;
            case R.id.btnGo:
                if (adminId.matches(""))
                {
                    Toast.makeText(AdminSettings.this, "Select Employee", Toast.LENGTH_SHORT).show();
                } else
                    {
                    progressDialog.show();
                    (Api.getClient()).getAuthority("fetch", employer_id, adminId).enqueue(new Callback<GetJsonDataForAuthority>() {
                        @Override
                        public void onResponse(Call<GetJsonDataForAuthority> call, Response<GetJsonDataForAuthority> response) {
                            try {
                                getJsonDataForAuthority = response.body();
                                if (getJsonDataForAuthority.getErrorCode() == 101)
                                {
                                    authorityData = getJsonDataForAuthority.getAuthorityData();
                                    if (authorityData.getRegister_new_emp().matches("") || authorityData.getRegister_new_emp().matches("N")) {
                                        authStatus.set(0, false);
                                    } else {
                                        authStatus.set(0, true);
                                    }
                                    if (authorityData.getView_emergency_login_logout().matches("") || authorityData.getView_emergency_login_logout().matches("N")) {
                                        authStatus.set(1, false);
                                    } else {
                                        authStatus.set(1, true);
                                    }
                                    if (authorityData.getEmployee_report().matches("") || authorityData.getEmployee_report().matches("N")) {
                                        authStatus.set(2, false);
                                    } else {
                                        authStatus.set(2, true);
                                    }
                                    if (authorityData.getPermitted_for_emergency().matches("") || authorityData.getPermitted_for_emergency().matches("N")) {
                                        authStatus.set(3, false);
                                    } else {
                                        authStatus.set(3, true);
                                    }
                                    if (authorityData.getEmployee_manual_attendance().matches("") || authorityData.getEmployee_manual_attendance().matches("N")) {
                                        authStatus.set(4, false);
                                    } else {
                                        authStatus.set(4, true);
                                    }
                                    if (authorityData.getShift_management().matches("") || authorityData.getShift_management().matches("N")) {
                                        authStatus.set(5, false);
                                    } else {
                                        authStatus.set(5, true);
                                    }
                                    if (authorityData.getSpecial_management().matches("") || authorityData.getSpecial_management().matches("N")) {
                                        authStatus.set(6, false);
                                    } else {
                                        authStatus.set(6, true);
                                    }
                                    if (authorityData.getRoster_management().matches("") || authorityData.getRoster_management().matches("N")) {
                                        authStatus.set(7, false);
                                    } else {
                                        authStatus.set(7, true);
                                    }
                                    if (authorityData.getPolicy_configuration().matches("") || authorityData.getPolicy_configuration().matches("N")) {
                                        authStatus.set(8, false);
                                    } else {
                                        authStatus.set(8, true);
                                    }
                                    if (authorityData.getOther_settings().matches("") || authorityData.getOther_settings().matches("N")) {
                                        authStatus.set(9, false);
                                    } else {
                                        authStatus.set(9, true);
                                    }
                                    if (authorityData.getHoliday_attendance().matches("") || authorityData.getHoliday_attendance().matches("N")) {
                                        authStatus.set(10, false);
                                    } else {
                                        authStatus.set(10, true);
                                    }
                                    if (authorityData.getHoliday_management().matches("") || authorityData.getHoliday_management().matches("N")) {
                                        authStatus.set(11, false);
                                    } else {
                                        authStatus.set(11, true);
                                    }
                                    if (authorityData.getView_old_employee_data().matches("") || authorityData.getView_old_employee_data().matches("N")) {
                                        authStatus.set(12, false);
                                    } else {
                                        authStatus.set(12, true);
                                    }
                                    if (authorityData.getLeave_management().matches("") || authorityData.getLeave_management().matches("N")) {
                                        authStatus.set(13, false);
                                    } else {
                                        authStatus.set(13, true);
                                    }
                                    if (authorityData.getSalary_payment().matches("") || authorityData.getSalary_payment().matches("N")) {
                                        authStatus.set(14, false);
                                    } else {
                                        authStatus.set(14, true);
                                    }
                                    authorityAdapter = new AuthorityAdapter(AdminSettings.this, authorities, authStatus);
                                    progressDialog.dismiss();
                                    permissionsCardView.setVisibility(View.VISIBLE);
                                    btnSave.setVisibility(View.VISIBLE);
                                    tvNoData.setVisibility(View.GONE);
                                    authorityRecycler.setAdapter(authorityAdapter);
                                } else {
                                    Toast.makeText(AdminSettings.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                final Dialog dialog1 = new Dialog(AdminSettings.this);
// Include dialog.xml file
                                dialog1.setContentView(R.layout.dialog_exit);
                                dialog1.show();

                                TextView heading1 = dialog1.findViewById(R.id.tv_quit_learning);
                                TextView description1 = dialog1.findViewById(R.id.tv_description);
                                Button btnNo1 = (Button) dialog1.findViewById(R.id.btn_no);
                                Button btnYes1 = (Button) dialog1.findViewById(R.id.btn_yes);
                                heading1.setText("Error");
                                description1.setText("An unfortunate error occured, please try again.");
                                btnNo1.setVisibility(View.GONE);
                                btnYes1.setText("Ok");
                                btnYes1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog1.dismiss();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<GetJsonDataForAuthority> call, Throwable t) {

                        }
                    });
                }
                break;
            case R.id.btnSave:
                if (adminId.matches("")) {
                    Toast.makeText(AdminSettings.this, "Select Employee", Toast.LENGTH_SHORT).show();
                } else {
                    final Dialog dialog1 = new Dialog(AdminSettings.this);
// Include dialog.xml file
                    dialog1.setContentView(R.layout.dialog_exit);
                    dialog1.show();
                    TextView heading1 = dialog1.findViewById(R.id.tv_quit_learning);
                    TextView description1 = dialog1.findViewById(R.id.tv_description);
                    Button btnNo1 = (Button) dialog1.findViewById(R.id.btn_no);
                    Button btnYes1 = (Button) dialog1.findViewById(R.id.btn_yes);
                    heading1.setText("Permit");
                    description1.setText("Are You Sure ?");
                    btnYes1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                            progressDialog.show();
                            authStatus = authorityAdapter.authStatus;
                            if (authStatus.get(0)) {
                                givenAuthorities = "register_new_emp:Y,";
                            } else {
                                givenAuthorities = "register_new_emp:N,";
                            }
                            if (authStatus.get(1)) {
                                givenAuthorities = givenAuthorities + "view_emergency_login_logout:Y,";
                            } else {
                                givenAuthorities = givenAuthorities + "view_emergency_login_logout:N,";
                            }
                            if (authStatus.get(2)) {
                                givenAuthorities = givenAuthorities + "employee_report:Y,";
                            } else {
                                givenAuthorities = givenAuthorities + "employee_report:N,";
                            }
                            if (authStatus.get(3)) {
                                givenAuthorities = givenAuthorities + "permitted_for_emergency:Y,";
                            } else {
                                givenAuthorities = givenAuthorities + "permitted_for_emergency:N,";
                            }
                            if (authStatus.get(4)) {
                                givenAuthorities = givenAuthorities + "employee_manual_attendance:Y,";
                                givenAuthorities = givenAuthorities + "employee_manual_attendance_type:"+authorityAdapter.manual_attendance+",";
                            } else {
                                givenAuthorities = givenAuthorities + "employee_manual_attendance:N,";
                                givenAuthorities = givenAuthorities + "employee_manual_attendance_type:N,";
                            }
                            if (authStatus.get(5)) {
                                givenAuthorities = givenAuthorities + "shift_management:Y,";
                            } else {
                                givenAuthorities = givenAuthorities + "shift_management:N,";
                            }
                            if (authStatus.get(6)) {
                                givenAuthorities = givenAuthorities + "special_management:Y,";
                            } else {
                                givenAuthorities = givenAuthorities + "special_management:N,";
                            }
                            if (authStatus.get(7)) {
                                givenAuthorities = givenAuthorities + "roster_management:Y,";
                            } else {
                                givenAuthorities = givenAuthorities + "roster_management:N,";
                            }
                            if (authStatus.get(8)) {
                                givenAuthorities = givenAuthorities + "policy_configuration:Y,";
                            } else {
                                givenAuthorities = givenAuthorities + "policy_configuration:N,";
                            }
                            if (authStatus.get(9)) {
                                givenAuthorities = givenAuthorities + "other_settings:Y,";
                            } else {
                                givenAuthorities = givenAuthorities + "other_settings:N,";
                            }
                            if (authStatus.get(10)) {
                                givenAuthorities = givenAuthorities + "holiday_attendance:Y,";
                            } else {
                                givenAuthorities = givenAuthorities + "holiday_attendance:N,";
                            }
                            if (authStatus.get(11)) {
                                givenAuthorities = givenAuthorities + "holiday_management:Y,";
                            } else {
                                givenAuthorities = givenAuthorities + "holiday_management:N,";
                            }
                            if (authStatus.get(14)) {
                                givenAuthorities = givenAuthorities + "salary_payment:Y,";
                            } else {
                                givenAuthorities = givenAuthorities + "salary_payment:N,";
                            }
                            if (authStatus.get(13)) {
                                givenAuthorities = givenAuthorities + "leave_management:Y,";
                            } else {
                                givenAuthorities = givenAuthorities + "leave_management:N,";
                            }
                            if (authStatus.get(12)) {
                                givenAuthorities = givenAuthorities + "view_old_employee_data:Y";
                            } else {
                                givenAuthorities = givenAuthorities + "view_old_employee_data:N";
                            }

                            Log.e("Given Authorities", givenAuthorities);
                            (Api.getClient()).setAuthority("update", employer_id, adminId, givenAuthorities).enqueue(new Callback<GetJsonResponse>() {
                                @Override
                                public void onResponse(Call<GetJsonResponse> call, Response<GetJsonResponse> response) {
                                    try {
                                        if (response.body().getErrorCode() == 101) {
                                            progressDialog.dismiss();

                                            finish();
                                            Toast.makeText(AdminSettings.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(AdminSettings.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        progressDialog.dismiss();
                                        final Dialog dialog1 = new Dialog(AdminSettings.this);
// Include dialog.xml file
                                        dialog1.setContentView(R.layout.dialog_exit);
                                        dialog1.show();

                                        TextView heading1 = dialog1.findViewById(R.id.tv_quit_learning);
                                        TextView description1 = dialog1.findViewById(R.id.tv_description);
                                        Button btnNo1 = (Button) dialog1.findViewById(R.id.btn_no);
                                        Button btnYes1 = (Button) dialog1.findViewById(R.id.btn_yes);
                                        heading1.setText("Error");
                                        description1.setText("An unfortunate error occured, please try again.");
                                        btnNo1.setVisibility(View.GONE);
                                        btnYes1.setText("Ok");
                                        btnYes1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog1.dismiss();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFailure(Call<GetJsonResponse> call, Throwable t) {

                                }
                            });

                        }
                    });
                    btnNo1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                        }
                    });
                }
                break;
        }
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
    public void getAdmins()
    {
        (Api.getClient()).getAdminList("employee_list", employer_id).enqueue(new Callback<GetJsonDataForAdmin>()
        {
            @Override
            public void onResponse(Call<GetJsonDataForAdmin> call, Response<GetJsonDataForAdmin> response) {
                try {
                    getJsonDataForAdmin = response.body();
                    if (getJsonDataForAdmin.getErrorCode() == 101)
                    {
                        progressDialog.dismiss();
                        if (!getJsonDataForAdmin.getNonAdmins().isEmpty())
                        {
                            employees = getJsonDataForAdmin.getNonAdmins();
                            employeeNameList = new ArrayList<>();
                            employeeIdList = new ArrayList<>();
                            for (int i = 0; i < employees.size(); i++) {
                                employeeNameList.add(employees.get(i).getName());
                                employeeIdList.add(employees.get(i).getEmployeeId());
                                employees.get(i).setSelected(false);
                            }
                            employeeAdapter.notifyDataSetChanged();
                            tvNoData.setVisibility(View.GONE);
                        }
                        if (getJsonDataForAdmin.getAdmins().isEmpty()) {
                            tvNoData.setVisibility(View.VISIBLE);
                        }
                        else {
                            admins = getJsonDataForAdmin.getAdmins();
                            adminAdapter = new AdminAdapter(AdminSettings.this, admins, progressDialog);
                            adminAdapter.ClickListener(new AdminAdapter.OnButtonClick() {
                                @Override
                                public void addButtonClick(int pos) {
                                    selectedAdmin = pos;
                                    adminName = admins.get(selectedAdmin).getName();
                                    adminId = admins.get(selectedAdmin).getEmployeeId();
                                    final Dialog dialog = new Dialog(AdminSettings.this);
// Include dialog.xml file
                                    dialog.setContentView(R.layout.dialog_exit);
                                    dialog.show();

                                    TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                    TextView description = dialog.findViewById(R.id.tv_description);
                                    Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                    Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                    heading.setText(adminName);
                                    description.setText("What would you like to do with this employee ?");
                                    btnYes.setText("Remove");
                                    btnNo.setText("View");
                                    btnNo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            progressDialog.show();
                                            (Api.getClient()).getAuthority("fetch", employer_id, adminId).enqueue(new Callback<GetJsonDataForAuthority>() {
                                                @Override
                                                public void onResponse(Call<GetJsonDataForAuthority> call, Response<GetJsonDataForAuthority> response) {
                                                    getJsonDataForAuthority = response.body();
                                                    if (getJsonDataForAuthority.getErrorCode() == 101) {
                                                        authorityData = getJsonDataForAuthority.getAuthorityData();
                                                        if (authorityData.getRegister_new_emp().matches("") || authorityData.getRegister_new_emp().matches("N")) {
                                                            authStatus.set(0, false);
                                                        } else {
                                                            authStatus.set(0, true);
                                                        }
                                                        if (authorityData.getView_emergency_login_logout().matches("") || authorityData.getView_emergency_login_logout().matches("N")) {
                                                            authStatus.set(1, false);
                                                        } else {
                                                            authStatus.set(1, true);
                                                        }
                                                        if (authorityData.getEmployee_report().matches("") || authorityData.getEmployee_report().matches("N")) {
                                                            authStatus.set(2, false);
                                                        } else {
                                                            authStatus.set(2, true);
                                                        }
                                                        if (authorityData.getPermitted_for_emergency().matches("") || authorityData.getPermitted_for_emergency().matches("N")) {
                                                            authStatus.set(3, false);
                                                        } else {
                                                            authStatus.set(3, true);
                                                        }
                                                        if (authorityData.getEmployee_manual_attendance().matches("") || authorityData.getEmployee_manual_attendance().matches("N")) {
                                                            authStatus.set(4, false);
                                                        } else {
                                                            authStatus.set(4, true);
                                                        }
                                                        if (authorityData.getShift_management().matches("") || authorityData.getShift_management().matches("N")) {
                                                            authStatus.set(5, false);
                                                        } else {
                                                            authStatus.set(5, true);
                                                        }
                                                        if (authorityData.getSpecial_management().matches("") || authorityData.getSpecial_management().matches("N")) {
                                                            authStatus.set(6, false);
                                                        } else {
                                                            authStatus.set(6, true);
                                                        }
                                                        if (authorityData.getRoster_management().matches("") || authorityData.getRoster_management().matches("N")) {
                                                            authStatus.set(7, false);
                                                        } else {
                                                            authStatus.set(7, true);
                                                        }
                                                        if (authorityData.getPolicy_configuration().matches("") || authorityData.getPolicy_configuration().matches("N")) {
                                                            authStatus.set(8, false);
                                                        } else {
                                                            authStatus.set(8, true);
                                                        }
                                                        if (authorityData.getOther_settings().matches("") || authorityData.getOther_settings().matches("N")) {
                                                            authStatus.set(9, false);
                                                        } else {
                                                            authStatus.set(9, true);
                                                        }
                                                        if (authorityData.getHoliday_attendance().matches("") || authorityData.getHoliday_attendance().matches("N")) {
                                                            authStatus.set(10, false);
                                                        } else {
                                                            authStatus.set(10, true);
                                                        }
                                                        if (authorityData.getHoliday_management().matches("") || authorityData.getHoliday_management().matches("N")) {
                                                            authStatus.set(11, false);
                                                        } else {
                                                            authStatus.set(11, true);
                                                        }
                                                        if (authorityData.getView_old_employee_data().matches("") || authorityData.getView_old_employee_data().matches("N")) {
                                                            authStatus.set(12, false);
                                                        } else {
                                                            authStatus.set(12, true);
                                                        }
                                                        if (authorityData.getLeave_management().matches("") || authorityData.getLeave_management().matches("N")) {
                                                            authStatus.set(13, false);
                                                        } else {
                                                            authStatus.set(13, true);
                                                        }
                                                        if (authorityData.getSalary_payment().matches("") || authorityData.getSalary_payment().matches("N")) {
                                                            authStatus.set(14, false);
                                                        } else {
                                                            authStatus.set(14, true);
                                                        }
                                                        authorityAdapter = new AuthorityAdapter(AdminSettings.this, authorities, authStatus);
                                                        progressDialog.dismiss();
                                                        permissionsCardView.setVisibility(View.VISIBLE);
                                                        btnSave.setVisibility(View.VISIBLE);
                                                        authorityRecycler.setAdapter(authorityAdapter);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<GetJsonDataForAuthority> call, Throwable t) {

                                                }
                                            });
                                        }
                                    });
                                    btnYes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            progressDialog.show();
                                            (Api.getClient()).removeAdmin("remove_admin_authority", employer_id, adminId).enqueue(new Callback<GetJsonResponse>() {
                                                @Override
                                                public void onResponse(Call<GetJsonResponse> call, Response<GetJsonResponse> response) {
                                                    if (response.body().getErrorCode() == 101)
                                                    {
                                                        progressDialog.dismiss();

                                                        Toast.makeText(AdminSettings.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                        admins.remove(selectedAdmin);
                                                        adminAdapter.notifyDataSetChanged();
                                                        adminRecycler.setAdapter(adminAdapter);
                                                        if (admins.isEmpty()) {
                                                            adminCardView.setVisibility(View.GONE);
                                                        }
                                                        permissionsCardView.setVisibility(View.GONE);
                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(AdminSettings.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<GetJsonResponse> call, Throwable t) {

                                                }
                                            });
                                        }
                                    });

                                }
                            });
                            adminRecycler.setAdapter(adminAdapter);
                            adminIdList = new ArrayList<>();
                            adminNameList = new ArrayList<>();
                            for (int i = 0; i < admins.size(); i++) {
                                adminNameList.add(admins.get(i).getName());
                                adminIdList.add(admins.get(i).getEmployeeId());
                            }
                            adminCardView.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
                            searchAdmin.addTextChangedListener(new TextWatcher() {
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
                        }
                    }else if(getJsonDataForAdmin.getErrorCode() == 102) {
                        progressDialog.dismiss();
                        tvNoData.setVisibility(View.VISIBLE);
                     }else {
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    final Dialog dialog1 = new Dialog(AdminSettings.this);
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
            }

            @Override
            public void onFailure(Call<GetJsonDataForAdmin> call, Throwable t) {

            }
        });
    }

    void filter(String text) {
        List<Employee> temp = new ArrayList();
        for (Employee d : employees) {

            if (d.getName().toLowerCase().contains(text.toLowerCase()))
            {
                temp.add(d);
            }
        }
        //update recyclerview
        employeeAdapter.updateList(temp);
    }

    void filterAdmin(String text)
    {
        List<Admin> temp = new ArrayList();
        for (Admin d : admins) {
            if (d.getName().toLowerCase().contains(text.toLowerCase()))
            {
                temp.add(d);
            }
        }
        //update recyclerview
        adminAdapter.updateList(temp);
    }
    //backpress
    protected void exitByBackKey() {

        final Dialog dialog = new Dialog(AdminSettings.this);
// Include dialog.xml file
        dialog.setContentView(R.layout.dialog_exit);
        dialog.show();

        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

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