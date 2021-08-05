package com.miscos.staffhandler.hrms_management.hrms.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miscos.staffhandler.hrms_management.hrms.adapter.LeaveRequestAdapter;
import com.miscos.staffhandler.hrms_management.hrms.adapter.LeaveStatusAdapter;
import com.miscos.staffhandler.hrms_management.hrms.adapter.LeaveTypeAdapter;
import com.miscos.staffhandler.hrms_management.hrms.api.ApiCallListener;
import com.miscos.staffhandler.hrms_management.hrms.model.MonthYearData;
import com.miscos.staffhandler.hrms_management.hrms.model.response.CurrentLeaveStatusData;
import com.miscos.staffhandler.hrms_management.hrms.model.response.FetchApplyLeaveData;
import com.miscos.staffhandler.hrms_management.hrms.model.response.LeaveData;
import com.miscos.staffhandler.hrms_management.hrms.utility.AppConstant;
import com.miscos.staffhandler.hrms_management.hrms.utility.Helper;
import com.miscos.staffhandler.hrms_management.hrms.utility.MessageUtility;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.ActivityApplyLeaveBinding;
import com.miscos.staffhandler.databinding.DialogLeaveStatusBinding;
import com.miscos.staffhandler.employee.helper.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ApplyLeaveActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityApplyLeaveBinding mBinding;
    private ArrayList<String> leaveTypeList = new ArrayList<>();
    private LeaveTypeAdapter leaveTypeAdapter;
    private ArrayList<FetchApplyLeaveData> fetchApplyLeaveData;
    private String selectedLeaveDayType = "", selectedReason = "";
    private ArrayList<MonthYearData> monthList = new ArrayList<>();
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_apply_leave);
        preferenceManager=new PreferenceManager(this);
        init();
    }

    private void init() {
        callGetLeaveReason(this);
        callGetApplyLeaveData(this);
        setToolbarData();
        mBinding.llReasonForLeave.tvSpinnerTitle.setText(R.string.lbl_reason_fro_leave);
        mBinding.btnSubmit.setOnClickListener(this);
        mBinding.tvLeaveStaus.setOnClickListener(this);
        mBinding.tvFromDate.setOnClickListener(this);
        mBinding.tvToDate.setOnClickListener(this);

        mBinding.rgbDay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) radioGroup.findViewById(checkedId);
                if (checkedId == R.id.rbFullDay) {
                    selectedLeaveDayType = "full";
                }

                if (checkedId == R.id.rbHalfDay) {
                    selectedLeaveDayType = "half";
                }
            }
        });
       // setLeaveReasonData();

    }


    private void setToolbarData()
    {
        mBinding.lltoolbar.toolbar.setTitle(R.string.lbl_apply_for_the_leave);
        mBinding.lltoolbar.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mBinding.lltoolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                applyLeave(this);
                break;

            case R.id.tvLeaveStaus: {
                getCurrentLeaveStatus(this);

                break;
            }

            case R.id.tvFromDate:
                Helper.openFromDatePickerDialog(this, mBinding.tvFromDate,mBinding.tvToDate);
                mBinding.tvToDate.setText("");
                break;
            case R.id.tvToDate:
                if (mBinding.tvFromDate.getText().toString().equalsIgnoreCase("")) {
                    MessageUtility.showToast(this, getString(R.string.please_select_from_date));
                } else {
                    Helper.openDatePickerDialog(this, mBinding.tvToDate, mBinding.tvFromDate.getText().toString());
                }
                break;
        }
    }


    private void openLeaveStatusDialog(ArrayList<LeaveData> leaveStatusDataArrayList) {
        Dialog dialog = new Dialog(this);
        DialogLeaveStatusBinding binding = DialogLeaveStatusBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(binding.getRoot());
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LeaveStatusAdapter adapter = new LeaveStatusAdapter(this, leaveStatusDataArrayList);
        binding.rvLeaveStatus.setAdapter(adapter);
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void setLeaveReasonData()
    {
        leaveTypeList.add("Medical reason");
        leaveTypeList.add("Personal reason");
        leaveTypeAdapter = new LeaveTypeAdapter(this, leaveTypeList);
        mBinding.llReasonForLeave.spinner.setAdapter(leaveTypeAdapter);
        mBinding.llReasonForLeave.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedReason = leaveTypeList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void callGetLeaveReason(Context context) {
        Helper.showProgress(context);
        final HashMap<String, String> paramiter = new HashMap<>();
        paramiter.put("mode", AppConstant.MODE_FETCH_REASON_FOR_LEAVE);
        paramiter.put("employee_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEEID));
        paramiter.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
        new ApiCallListener(this).executeApiCall(paramiter,
                AppConstant.BASE_URL+AppConstant.Leave_MANAGEMENT, "fetch_reason_for_leave", new ApiCallListener.ApiCallInterface() {
                    @Override
                    public void onSuccess(String response, String apiName) {
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = new JSONObject(jsonArray.getString(0));
                                String error_code = jsonObject.getString("error_code");
                                String message = jsonObject.getString("message");
                                if (error_code.equalsIgnoreCase("104")) {
                                    JSONObject leave_data = jsonObject.getJSONObject("leave_data");
                                    String leave_name = leave_data.getString("leave_name");
                                    String[] part = leave_name.split(",");
                                    for (String a : part) {
                                        leaveTypeList.add(a);
                                    }
                                    setLeaveReasonData();

                                } else {
                                   // Helper.showMessage(context, message);
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

    private void callGetApplyLeaveData(Context context) {
        final HashMap<String, String> paramiter = new HashMap<>();
        paramiter.put("mode", AppConstant.MODE_FETCH_APPLY_LEAVE_DATA);
        paramiter.put("employee_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEEID));
        paramiter.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
        new ApiCallListener(this).executeApiCall(paramiter,
                AppConstant.BASE_URL+AppConstant.Leave_MANAGEMENT, AppConstant.MODE_FETCH_APPLY_LEAVE_DATA, new ApiCallListener.ApiCallInterface() {
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
                                    fetchApplyLeaveData = new Gson().fromJson(response.toString(),
                                            new TypeToken<ArrayList<FetchApplyLeaveData>>() {
                                            }.getType()
                                    );

                                    if (fetchApplyLeaveData != null && fetchApplyLeaveData.size() != 0) {
                                        if (fetchApplyLeaveData.get(0).getLeaveApplyData() != null && fetchApplyLeaveData.get(0).getLeaveApplyData().size() != 0) {
                                            setLeaveRequestData(fetchApplyLeaveData.get(0).getLeaveApplyData());
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

    private void setLeaveRequestData(ArrayList<FetchApplyLeaveData.LeaveApplyDatum> leaveRequestDataArrayList) {
        LeaveRequestAdapter adapter = new LeaveRequestAdapter(this, leaveRequestDataArrayList);
        mBinding.rvLeave.setAdapter(adapter);
    }

    private void applyLeave(Context context) {
        if (selectedLeaveDayType.equalsIgnoreCase("")) {
            MessageUtility.showToast(this, getString(R.string.please_select_day_type));
            return;
        }
        if (mBinding.tvFromDate.getText().toString().equalsIgnoreCase("")) {
            MessageUtility.showToast(this, getString(R.string.please_select_from_date));
            return;
        }
        if (mBinding.tvToDate.getText().toString().equalsIgnoreCase("")) {
            MessageUtility.showToast(this, getString(R.string.please_select_to_date));
            return;
        }

        if (mBinding.edtRequestDetails.getText().toString().equalsIgnoreCase("")) {
            MessageUtility.showToast(this, getString(R.string.please_enter_request_details));
            return;
        }
        Helper.showProgress(context);
        final HashMap<String, String> paramiter = new HashMap<>();
        paramiter.put("mode", AppConstant.MODE_APPLY_LEAVE);
        paramiter.put("employee_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEEID));
        paramiter.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
        paramiter.put("type", selectedLeaveDayType);
        paramiter.put("leave_from_date", mBinding.tvFromDate.getText().toString());
        paramiter.put("leave_to_date", mBinding.tvToDate.getText().toString());
        paramiter.put("reason_for_leave", selectedReason);
        paramiter.put("leave_description", mBinding.edtRequestDetails.getText().toString());
        new ApiCallListener(this).executeApiCall(paramiter,
                AppConstant.BASE_URL+AppConstant.Leave_MANAGEMENT, AppConstant.MODE_APPLY_LEAVE, new ApiCallListener.ApiCallInterface() {
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
                                    Helper.showSucessMessage(context, message);
                                    clearData();
                                    callGetApplyLeaveData(context);
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

    private void getCurrentLeaveStatus(Context context) {
        Helper.showProgress(context);
        final HashMap<String, String> paramiter = new HashMap<>();
        paramiter.put("mode", AppConstant.MODE_CURRENT_LEAVE_STATUS);
        paramiter.put("employee_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEEID));
        paramiter.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
        new ApiCallListener(this).executeApiCall(paramiter,
                AppConstant.BASE_URL+AppConstant.Leave_MANAGEMENT, AppConstant.MODE_CURRENT_LEAVE_STATUS, new ApiCallListener.ApiCallInterface() {
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
                                    ArrayList<CurrentLeaveStatusData> currentLeaveStatusData = new Gson().fromJson(response.toString(),
                                            new TypeToken<ArrayList<CurrentLeaveStatusData>>() {
                                            }.getType()
                                    );
                                    openLeaveStatusDialog(currentLeaveStatusData.get(0).getLeaveData());
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

    private void clearData(){
        mBinding.tvFromDate.setText("");
        mBinding.tvToDate.setText("");
        mBinding.edtRequestDetails.setText("");
    }

}