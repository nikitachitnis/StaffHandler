package com.miscos.staffhandler.hrms_management.hrms.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miscos.staffhandler.hrms_management.hrms.adapter.EmployeeAdapter;
import com.miscos.staffhandler.hrms_management.hrms.adapter.RequestLeaveAdapter;
import com.miscos.staffhandler.hrms_management.hrms.api.ApiCallListener;
import com.miscos.staffhandler.hrms_management.hrms.model.response.EmployeeLIst;
import com.miscos.staffhandler.hrms_management.hrms.model.response.LeaveCount;
import com.miscos.staffhandler.hrms_management.hrms.model.response.OldRecordResponse;
import com.miscos.staffhandler.hrms_management.hrms.model.response.PendingLeaveRequestResponse;
import com.miscos.staffhandler.hrms_management.hrms.utility.AppConstant;
import com.miscos.staffhandler.hrms_management.hrms.utility.Helper;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.ActivityLeaveAuthorityBinding;
import com.miscos.staffhandler.databinding.DialogLeaveApproveRejectBinding;
import com.miscos.staffhandler.databinding.DialogOldRecordBinding;
import com.miscos.staffhandler.employee.helper.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class LeaveAuthorityActivity extends AppCompatActivity implements RequestLeaveAdapter.SetSelectLeave, EmployeeAdapter.EmployeeListener {

    private ActivityLeaveAuthorityBinding mBinding;
    private EmployeeAdapter employeeAdapter;
    private RequestLeaveAdapter requestLeaveAdapter;
    ArrayList<EmployeeLIst.EmployeeData> employeeDataArrayList;
    PreferenceManager preferenceManager;

    private String employeeId = "";
    private String employee_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_leave_authority);
        preferenceManager=new PreferenceManager(this);
        employeeDataArrayList=new ArrayList<>();
        init();
    }

    private void init() {
        setToolbarData();
        getPendingEmployeeList(this);

mBinding.chAllEmp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b)
            getEmployeeList(LeaveAuthorityActivity.this);
        else
            getPendingEmployeeList(LeaveAuthorityActivity.this);

    }
});
        mBinding.edtSearch.addTextChangedListener(new TextWatcher() {
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
                if (s.toString().equalsIgnoreCase("")) {
                    if (employeeDataArrayList.size() != 0) {
                        employeeAdapter.updateList(employeeDataArrayList);
                    }
                } else {
                    if (employeeDataArrayList.size() != 0) {
                        filter(s.toString(), employeeDataArrayList);
                    }
                }
            }
        });
        mBinding.btnOldRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
           getOldRecord(employeeId);
            }
        });
    }

    void dialogForOldRecord(List<LeaveCount> leaveCounts)
    {

        Dialog dialog = new Dialog(this);
        DialogOldRecordBinding binding = DialogOldRecordBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(binding.getRoot());
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        binding.textView5.setText("Showing old record for "+employee_name);
        binding.btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();
        for (int i=0;i<leaveCounts.size();i++)
        {
            HashMap<String,String> hashMap=new HashMap<>();//create a hashmap to store the data in key value pair
            hashMap.put("month_name",leaveCounts.get(i).getMonthName());
            hashMap.put("data","Approved Leaves - "+leaveCounts.get(i).getApprovedCnt()+",Rejected Leaves - "+leaveCounts.get(i).getRejectedCnt()+",Pending Leaves - "+leaveCounts.get(i).getPendingCnt());
            arrayList.add(hashMap);//add the hashmap into arrayList
        }
        String[] from={"month_name","data"};//string array
        int[] to={R.id.txtmonthName,R.id.txtdata};//int array of views id's
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,arrayList,R.layout.row_old_record,from,to);//Create object and set the parameters for simpleAdapter
        binding.simpleListView.setAdapter(simpleAdapter);//sets the adapter for listView

        dialog.show();
    }
    private void setToolbarData()
    {
        mBinding.lltoolbar.toolbar.setTitle(R.string.lbl_leave_authority);
        mBinding.lltoolbar.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        mBinding.lltoolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setEmployeeData() {
        employeeAdapter = new EmployeeAdapter(this, employeeDataArrayList, this);
        mBinding.rvEmp.setVisibility(View.VISIBLE);
        mBinding.rvEmp.setAdapter(employeeAdapter);
        employeeId = employeeDataArrayList.get(0).getEmployeeId();
        employee_name=employeeDataArrayList.get(0).getName();
        mBinding.tvLeaveRequestTitle.setText(getString(R.string.lbl_leave_request_list) + " for " + employeeDataArrayList.get(0).getName());
        getPendingLeaveRequest(this, employeeDataArrayList.get(0).getEmployeeId());
    }

    private void setRequestLeaveList(ArrayList<PendingLeaveRequestResponse.PendingLeaveData> pendingLeaveDataArrayList) {
        requestLeaveAdapter = new RequestLeaveAdapter(this, pendingLeaveDataArrayList, this);
        mBinding.rvRequestLeave.setAdapter(requestLeaveAdapter);
    }

    @Override
    public void selectLeaveData(PendingLeaveRequestResponse.PendingLeaveData leaveRequestData) {
        openApproveRejectDialog(leaveRequestData);
    }

    private void openApproveRejectDialog(PendingLeaveRequestResponse.PendingLeaveData leaveRequestData)
    {
        Dialog dialog = new Dialog(this);
        DialogLeaveApproveRejectBinding binding = DialogLeaveApproveRejectBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(binding.getRoot());
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        binding.tvFromDate.setText(parseDateToddMMyyyy(leaveRequestData.getLeaveFromDate()));
        binding.tvToDate.setText(parseDateToddMMyyyy(leaveRequestData.getLeaveToDate()));
        binding.edtRequestDetails.setText(leaveRequestData.getLeaveDescription());
        String fromDate = leaveRequestData.getLeaveFromDate();
        String toDate = leaveRequestData.getLeaveToDate();
        binding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                callApproveRejectLeave(LeaveAuthorityActivity.this, "approved", fromDate, toDate);
            }
        });

        binding.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
                callApproveRejectLeave(LeaveAuthorityActivity.this, "rejected", fromDate, toDate);
            }
        });
        dialog.show();
    }
    private void getPendingEmployeeList(Context context)
    {
        Helper.showProgress(context);
        final HashMap<String, String> paramiter = new HashMap<>();
        paramiter.put("mode", AppConstant.MODE_PENDING_LIST);
        paramiter.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));


        new ApiCallListener(this).executeApiCall(paramiter,
                AppConstant.BASE_URL + AppConstant.Leave_MANAGEMENT, AppConstant.MODE_PENDING_LIST, new ApiCallListener.ApiCallInterface() {
                    @Override
                    public void onSuccess(String response, String apiName) {
                        Helper.hideProgress();
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject =jsonArray.getJSONObject(0);
                                String error_code = jsonObject.getString("error_code");
                                String message = jsonObject.getString("message");
                                if (error_code.equalsIgnoreCase("104"))
                                {
                                    List<EmployeeLIst> responseData = new Gson().fromJson(response,new TypeToken<List<EmployeeLIst>>(){}.getType());

                                    if (responseData != null) {
                                        if (responseData.get(0).getPendingemployeeData().size() != 0)
                                        {
                                            employeeDataArrayList.clear();
                                            employeeDataArrayList.addAll(responseData.get(0).getPendingemployeeData());
                                            setEmployeeData();
                                        } else {
                                           Helper.showMessage(context, "No pending leave requests");
                                        }
                                    }

                                } else {
                                    Helper.showMessage(context, "No pending requests");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFail(String errortype, String apiName) {
                        Helper.hideProgress();
                        Helper.showMessage(context, errortype);
                    }
                });
    }
    private void getOldRecord(String employee_id)
    {
        Helper.showProgress(this);
        final HashMap<String, String> paramiter = new HashMap<>();
        paramiter.put("mode", AppConstant.MODE_OLD_RECORD);
        paramiter.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
        paramiter.put("employee_id", employee_id);

        new ApiCallListener(this).executeApiCall(paramiter,
                AppConstant.BASE_URL + AppConstant.Leave_MANAGEMENT, AppConstant.MODE_OLD_RECORD, new ApiCallListener.ApiCallInterface() {
                    @Override
                    public void onSuccess(String response, String apiName) {
                        Helper.hideProgress();
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject =jsonArray.getJSONObject(0);
                                String error_code = jsonObject.getString("error_code");
                                String message = jsonObject.getString("message");
                                if (error_code.equalsIgnoreCase("104"))
                                {
                                    List<OldRecordResponse> responseData = new Gson().fromJson(response, new TypeToken<List<OldRecordResponse>>() {
                                    }.getType());

                                    if (responseData != null)
                                    {

                dialogForOldRecord(responseData.get(0).getLeaveCount());

                                    }
                                }
                                else
                                Helper.showMessage(LeaveAuthorityActivity.this,"No old data found");


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFail(String errortype, String apiName) {
                        Helper.hideProgress();
                        Helper.showMessage(LeaveAuthorityActivity.this, errortype);
                    }
                });
    }
    private void getEmployeeList(Context context)
    {
        Helper.showProgress(context);
        final HashMap<String, String> paramiter = new HashMap<>();
        paramiter.put("mode", AppConstant.MODE_FETCH);
        paramiter.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
        paramiter.put("shift_no", "0");
        paramiter.put("type", "");
        paramiter.put("operation_type", "");

        new ApiCallListener(this).executeApiCall(paramiter,
                AppConstant.BASE_URL + AppConstant.EMPLOYEE_MANAGEMENT, AppConstant.MODE_FETCH, new ApiCallListener.ApiCallInterface() {
                    @Override
                    public void onSuccess(String response, String apiName)
                    {
                        Helper.hideProgress();
                        if (!TextUtils.isEmpty(response))
                        {
                            try {
                                //JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = new JSONObject(response);
                                String error_code = jsonObject.getString("error_code");
                                String message = jsonObject.getString("message");
                                if (error_code.equalsIgnoreCase("101"))
                                {
                                    EmployeeLIst responseData = new Gson().fromJson(response.toString(),
                                            new TypeToken<EmployeeLIst>() {
                                            }.getType()
                                    );

                                    if (responseData != null)
                                    {
                                        if (responseData.getEmployeeData().size() != 0)
                                        {
                                            employeeDataArrayList.clear();
                                            employeeDataArrayList = responseData.getEmployeeData();
                                            setEmployeeData();
                                        } else {
                                            Helper.showMessage(context, "No pending leave requests found");
                                        }
                                    }

                                } else {
                                    Helper.showMessage(context, message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFail(String errortype, String apiName) {
                        Helper.hideProgress();
                        Helper.showMessage(context, errortype);
                    }
                });
    }


    private void callApproveRejectLeave(Context context, String status, String fromDate, String toDate) {
        Helper.showProgress(context);
        final HashMap<String, String> paramiter = new HashMap<>();
        paramiter.put("mode", AppConstant.MODE_APPROVE_REJECT_LEAVE);
        paramiter.put("employee_id", employeeId);
        paramiter.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
        paramiter.put("leave_from_date", fromDate);
        paramiter.put("leave_to_date", toDate);
        paramiter.put("action_taken_by_id",preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEEID));
        paramiter.put("leave_status", status);

        new ApiCallListener(this).executeApiCall(paramiter,
                AppConstant.BASE_URL + AppConstant.Leave_MANAGEMENT, AppConstant.MODE_APPROVE_REJECT_LEAVE, new ApiCallListener.ApiCallInterface() {
                    @Override
                    public void onSuccess(String response, String apiName) {
                        Helper.hideProgress();
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = new JSONObject(jsonArray.getString(0));
                                String error_code = jsonObject.getString("error_code");
                                String message = jsonObject.getString("message");
                                if (error_code.equalsIgnoreCase("104"))
                                {
                                    //Helper.showSucessMessage(context, message);
                                    PrettyDialog prettyDialog = new PrettyDialog(context);
                                    prettyDialog.setCancelable(false);
                                    prettyDialog
                                            .setTitle(context.getString(R.string.app_name))
                                            .setMessage(message)
                                            .setIcon(R.drawable.ic_done, R.color.color_white, null)
                                            .addButton("Okay", R.color.white, R.color.blue, new PrettyDialogCallback() {
                                                @Override
                                                public void onClick() {
                                                    getPendingLeaveRequest(LeaveAuthorityActivity.this,employeeId);
                                                    prettyDialog.dismiss();
                                                }
                                            })
                                            .show();

                                } else {
                                    Helper.showMessage(context, message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFail(String errortype, String apiName) {
                        Helper.hideProgress();
                        Helper.showMessage(context, errortype);
                    }
                });
    }

    private void getPendingLeaveRequest(Context context, String employeeID) {
        Helper.showProgress(context);
        final HashMap<String, String> paramiter = new HashMap<>();
        paramiter.put("mode", AppConstant.MODE_FETCH_PENDING_LEAVE_REQUEST);
        paramiter.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
        paramiter.put("employee_id", employeeID);

        new ApiCallListener(this).executeApiCall(paramiter,
                AppConstant.BASE_URL + AppConstant.Leave_MANAGEMENT, AppConstant.MODE_FETCH_PENDING_LEAVE_REQUEST, new ApiCallListener.ApiCallInterface() {
                    @Override
                    public void onSuccess(String response, String apiName) {
                        Helper.hideProgress();
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = new JSONObject(jsonArray.getString(0));
                                String error_code = jsonObject.getString("error_code");
                                String message = jsonObject.getString("message");
                                if (error_code.equalsIgnoreCase("104")) {
                                    ArrayList<PendingLeaveRequestResponse> responseData = new Gson().fromJson(response.toString(),
                                            new TypeToken<ArrayList<PendingLeaveRequestResponse>>() {
                                            }.getType()
                                    );

                                    if (responseData.size() != 0) {
                                        mBinding.rvRequestLeave.setVisibility(View.VISIBLE);
                                        mBinding.tvNoLeave.setVisibility(View.GONE);
                                        setRequestLeaveList(responseData.get(0).getPendingLeaveData());
                                    }

                                } else {
                                    mBinding.rvRequestLeave.setVisibility(View.GONE);
                                    mBinding.tvNoLeave.setVisibility(View.VISIBLE);
                                    //Helper.showMessage(context, message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFail(String errortype, String apiName) {
                        Helper.hideProgress();
                        Helper.showMessage(context, errortype);
                    }
                });
    }

    @Override
    public void selectEmployee(EmployeeLIst.EmployeeData data) {
        employeeId = data.getEmployeeId();
        mBinding.tvLeaveRequestTitle.setText(getString(R.string.lbl_leave_request_list) + " for " + data.getName());
        getPendingLeaveRequest(this, data.getEmployeeId());
    }

    void filter(String text, ArrayList<EmployeeLIst.EmployeeData> data) {
        ArrayList<EmployeeLIst.EmployeeData> temp = new ArrayList();
        for (EmployeeLIst.EmployeeData d : data) {
            if (d.getName() != null || d.getEmployeeId() != null) {
                if (d.getName().toLowerCase().contains(text) || d.getEmployeeId().toLowerCase().contains(text)) {
                    temp.add(d);
                }
            }
        }
        employeeAdapter.updateList(temp);
    }
    public String parseDateToddMMyyyy(String time)
    {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}