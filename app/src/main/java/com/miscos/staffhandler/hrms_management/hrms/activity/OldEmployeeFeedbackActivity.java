package com.miscos.staffhandler.hrms_management.hrms.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miscos.staffhandler.hrms_management.hrms.api.ApiCallListener;
import com.miscos.staffhandler.hrms_management.hrms.model.response.EmployeeFeedbackResponse;
import com.miscos.staffhandler.hrms_management.hrms.utility.AppConstant;
import com.miscos.staffhandler.hrms_management.hrms.utility.Helper;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.ActivityOldEmployeeFeedbackBinding;
import com.miscos.staffhandler.employee.helper.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class OldEmployeeFeedbackActivity extends AppCompatActivity {

    private ActivityOldEmployeeFeedbackBinding mBinding;
    PreferenceManager preferenceManager;
    String emp_id="",emp_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_old_employee_feedback);
        preferenceManager=new PreferenceManager(this);
        Intent calling_intent=getIntent();
        if(calling_intent!=null)
        {
            emp_id=calling_intent.getStringExtra("emp_id");
            emp_name=calling_intent.getStringExtra("emp_name");
        }
        init();
    }

    private void init() {
        setToolbarData();
        setRatingData();
        getFeedbackData(this);
    }

    private void setToolbarData() {
        mBinding.lltoolbar.toolbar.setTitle(R.string.lbl_employee_feedback);
        mBinding.lltoolbar.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        mBinding.lltoolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getFeedbackData(Context context){
        Helper.showProgress(context);
        final HashMap<String, String> paramiter = new HashMap<>();
        paramiter.put("mode", AppConstant.MODE_GET_EMP_FEEDBACK_DATA);
        paramiter.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
        paramiter.put("employee_id", emp_id);


        new ApiCallListener(this).executeApiCall(paramiter,
                AppConstant.BASE_URL + AppConstant.EMPLOYEE_MANAGEMENT_New, AppConstant.MODE_GET_EMP_FEEDBACK_DATA, new ApiCallListener.ApiCallInterface() {
                    @Override
                    public void onSuccess(String response, String apiName) {
                        Helper.hideProgress();
                        if (!TextUtils.isEmpty(response))
                        {
                            try {
                                Log.d("feedback",response);
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = new JSONObject(jsonArray.getString(0));
                                String error_code = jsonObject.getString("error_code");
                                String message = jsonObject.getString("message");
                                if (error_code.equalsIgnoreCase("101")) {
                                    mBinding.tvNoData.setVisibility(View.GONE);
                                    mBinding.clMain.setVisibility(View.VISIBLE);

                                    ArrayList<EmployeeFeedbackResponse> responseData = new Gson().fromJson(response.toString(),
                                            new TypeToken<ArrayList<EmployeeFeedbackResponse>>() {
                                            }.getType()
                                    );

                                    if (responseData.size()!=0){
                                        setEmployeeInfoData(responseData.get(0).getEmployeeData());
                                        if(responseData.get(0).getEmployeeData().getDesignationAtTheTimeOfLeaving().equalsIgnoreCase("")&&responseData.get(0).getEmployeeData().getDesignationAtTheTimeOfJoining().equalsIgnoreCase(""))
                                        {
                                            mBinding.tvNoData.setVisibility(View.VISIBLE);
                                        mBinding.clMain.setVisibility(View.GONE);}
                                    }

                                } else {
                                    mBinding.tvNoData.setVisibility(View.VISIBLE);
                                    mBinding.clMain.setVisibility(View.GONE);
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

    @SuppressLint("SetTextI18n")
    private void setEmployeeInfoData(EmployeeFeedbackResponse.EmployeeFeedBackData feedBackData) {
        mBinding.llEmpName.tvInfoValue.setText(emp_name);
        mBinding.llEmpNo.tvInfoValue.setText(emp_id);
        mBinding.llEmpDJoin.tvInfoValue.setText(feedBackData.getDesignationAtTheTimeOfJoining());
        mBinding.llEmpDLeave.tvInfoValue.setText(feedBackData.getDesignationAtTheTimeOfLeaving());
        mBinding.llEmpDOJoin.tvInfoValue.setText(parseDateToddMMyyyy(feedBackData.getDateOfJoining()));
        mBinding.llEmpDOLeave.tvInfoValue.setText(parseDateToddMMyyyy(feedBackData.getDateOfLeaving()));
        mBinding.llEmpGrossSalary.tvInfoValue.setText(feedBackData.getGrossSalary());
        mBinding.llEmpSName.tvInfoValue.setText(feedBackData.getSupervisorNameDesignation());

        mBinding.edtReasonLeaving.setText(feedBackData.getReasonForLeaving());
        mBinding.edtFormalities.setText(feedBackData.getIsTheExitFormalitiesCompleted());
        mBinding.edtAdditional.setText(feedBackData.getAdditionalComments());
        mBinding.tvOverallRatings.setText(feedBackData.getOverallRating());

        String jobKnowledge=feedBackData.getJobKnowledge();
        switch (jobKnowledge){
            case "1":
                mBinding.llJobKnowledge.rgbRating.check(R.id.chPoor);
                break;
            case "2":
                mBinding.llJobKnowledge.rgbRating.check(R.id.chBelowAvg);
                break;
            case "3":
                mBinding.llJobKnowledge.rgbRating.check(R.id.chAvg);
                break;
            case "4":
                mBinding.llJobKnowledge.rgbRating.check(R.id.chGood);
                break;
            case "5":
                mBinding.llJobKnowledge.rgbRating.check(R.id.chExcellent);
                break;
        }

        String workQuality=feedBackData.getWorkQuality();
        switch (workQuality){
            case "1":
                mBinding.llWorkQuality.rgbRating.check(R.id.chPoor);
                break;
            case "2":
                mBinding.llWorkQuality.rgbRating.check(R.id.chBelowAvg);
                break;
            case "3":
                mBinding.llWorkQuality.rgbRating.check(R.id.chAvg);
                break;
            case "4":
                mBinding.llWorkQuality.rgbRating.check(R.id.chGood);
                break;
            case "5":
                mBinding.llWorkQuality.rgbRating.check(R.id.chExcellent);
                break;
        }


        String attendance=feedBackData.getPunctuality();
        switch (attendance){
            case "1":
                mBinding.llAttendance.rgbRating.check(R.id.chPoor);
                break;
            case "2":
                mBinding.llAttendance.rgbRating.check(R.id.chBelowAvg);
                break;
            case "3":
                mBinding.llAttendance.rgbRating.check(R.id.chAvg);
                break;
            case "4":
                mBinding.llAttendance.rgbRating.check(R.id.chGood);
                break;
            case "5":
                mBinding.llAttendance.rgbRating.check(R.id.chExcellent);
                break;
        }

        String initiative=feedBackData.getInitiative();
        switch (initiative){
            case "1":
                mBinding.llInitiative.rgbRating.check(R.id.chPoor);
                break;
            case "2":
                mBinding.llInitiative.rgbRating.check(R.id.chBelowAvg);
                break;
            case "3":
                mBinding.llInitiative.rgbRating.check(R.id.chAvg);
                break;
            case "4":
                mBinding.llInitiative.rgbRating.check(R.id.chGood);
                break;
            case "5":
                mBinding.llInitiative.rgbRating.check(R.id.chExcellent);
                break;
        }

        String technicalSkill=feedBackData.getTechnicalSkill();
        switch (technicalSkill){
            case "1":
                mBinding.llTechnicalSkill.rgbRating.check(R.id.chPoor);
                break;
            case "2":
                mBinding.llTechnicalSkill.rgbRating.check(R.id.chBelowAvg);
                break;
            case "3":
                mBinding.llTechnicalSkill.rgbRating.check(R.id.chAvg);
                break;
            case "4":
                mBinding.llTechnicalSkill.rgbRating.check(R.id.chGood);
                break;
            case "5":
                mBinding.llTechnicalSkill.rgbRating.check(R.id.chExcellent);
                break;
        }


        String dependability=feedBackData.getDependability();
        switch (dependability){
            case "1":
                mBinding.llDependability.rgbRating.check(R.id.chPoor);
                break;
            case "2":
                mBinding.llDependability.rgbRating.check(R.id.chBelowAvg);
                break;
            case "3":
                mBinding.llDependability.rgbRating.check(R.id.chAvg);
                break;
            case "4":
                mBinding.llDependability.rgbRating.check(R.id.chGood);
                break;
            case "5":
                mBinding.llDependability.rgbRating.check(R.id.chExcellent);
                break;
        }
    }

    private void setRatingData() {
        mBinding.llEmpDOJoin.tvInfoTitle.setText(R.string.lbl_date_of_join);
        mBinding.llEmpDOLeave.tvInfoTitle.setText(R.string.lbl_date_of_leaving);
        mBinding.llEmpGrossSalary.tvInfoTitle.setText(R.string.lbl_gross_salary);
        mBinding.llEmpSName.tvInfoTitle.setText(R.string.lbl_supervisors_name_designation);
        mBinding.llEmpDLeave.tvInfoTitle.setText(R.string.lbl_designation_time_leaving);
        mBinding.llEmpDJoin.tvInfoTitle.setText(R.string.lbl_designation_time_join);
        mBinding.llEmpNo.tvInfoTitle.setText(R.string.lbl_emp_id);
        mBinding.llEmpName.tvInfoTitle.setText(R.string.lbl_emp_name);
        mBinding.llJobKnowledge.tvRatingsTitle.setText(R.string.lbl_job_knowledge);
        mBinding.llWorkQuality.tvRatingsTitle.setText(R.string.lbl_work_quality);
        mBinding.llAttendance.tvRatingsTitle.setText(R.string.lbl_attendance_punctuality);
        mBinding.llInitiative.tvRatingsTitle.setText(R.string.lbl_initiative);
        mBinding.llTechnicalSkill.tvRatingsTitle.setText(R.string.lbl_technical_skills);
        mBinding.llDependability.tvRatingsTitle.setText(R.string.lbl_dependability);

        for (int i = 0; i < mBinding.llJobKnowledge.rgbRating.getChildCount(); i++) {
            mBinding.llJobKnowledge.rgbRating.getChildAt(i).setEnabled(false);
        }

        for (int i = 0; i < mBinding.llWorkQuality.rgbRating.getChildCount(); i++) {
            mBinding.llWorkQuality.rgbRating.getChildAt(i).setEnabled(false);
        }

        for (int i = 0; i < mBinding.llAttendance.rgbRating.getChildCount(); i++) {
            mBinding.llAttendance.rgbRating.getChildAt(i).setEnabled(false);
        }

        for (int i = 0; i < mBinding.llInitiative.rgbRating.getChildCount(); i++) {
            mBinding.llInitiative.rgbRating.getChildAt(i).setEnabled(false);
        }

        for (int i = 0; i < mBinding.llTechnicalSkill.rgbRating.getChildCount(); i++) {
            mBinding.llTechnicalSkill.rgbRating.getChildAt(i).setEnabled(false);
        }

        for (int i = 0; i < mBinding.llDependability.rgbRating.getChildCount(); i++) {
            mBinding.llDependability.rgbRating.getChildAt(i).setEnabled(false);
        }

        mBinding.edtReasonLeaving.setEnabled(false);
        mBinding.edtFormalities.setEnabled(false);
        mBinding.edtAdditional.setEnabled(false);
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