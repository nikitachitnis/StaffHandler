package com.miscos.staffhandler.hrms_management.hrms.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miscos.staffhandler.hrms_management.hrms.adapter.EmployeeAdapter;
import com.miscos.staffhandler.hrms_management.hrms.api.ApiCallListener;
import com.miscos.staffhandler.hrms_management.hrms.model.response.EmployeeLIst;
import com.miscos.staffhandler.hrms_management.hrms.utility.AppConstant;
import com.miscos.staffhandler.hrms_management.hrms.utility.Helper;
import com.miscos.staffhandler.hrms_management.hrms.utility.MessageUtility;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.ActivityEmployeeFeedbackBinding;
import com.miscos.staffhandler.employee.helper.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class EmployeeFeedbackActivity extends AppCompatActivity implements EmployeeAdapter.EmployeeListener
{

    private ActivityEmployeeFeedbackBinding mBinding;
    private int jobKnowledge = 0;
    private int workQuality = 0;
    private int attendance = 0;
    private int initiative = 0;
    private int technicalSkill = 0;
    private int dependability = 0;
    PreferenceManager preferenceManager;
    ArrayList<String> employeeDataArrayList;
    private ArrayAdapter<String> employeeAdapter;
    String emp_id="",emp_name="",date_of_joining="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_employee_feedback);
        preferenceManager=new PreferenceManager(this);
        Intent callingIntent=getIntent();
     employeeDataArrayList=new ArrayList<>();
        if(callingIntent!=null)
        {
            emp_id=callingIntent.getStringExtra("emp_id");
            emp_name=callingIntent.getStringExtra("emp_name");
            date_of_joining=callingIntent.getStringExtra("emp_doj");

        }
        init();
    }
    private void setEmployeeData()
    {
        employeeAdapter = new ArrayAdapter<>(this,android.R.layout.simple_expandable_list_item_1,employeeDataArrayList);
        mBinding.llEmpSName.spinreportingPerson.setAdapter(employeeAdapter);

    }

    private void init()
    {
        setToolbarData();
        setEmployeeInfoData();
        setRatingData();
        setClickData();
        getOverallRating();
        getEmployeeList(this);
        mBinding.chkconfirm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                /*if(b)
            {
                mBinding.btnSubmit.setEnabled(true);
            }
            else
                mBinding.btnSubmit.setEnabled(false);*/

            }
        });
        mBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (checkValidation())
                {
                    if(!mBinding.chkconfirm.isChecked())
                    {
                        Toast.makeText(EmployeeFeedbackActivity.this, "Please confirm the submitted details", Toast.LENGTH_SHORT).show();
                  return;
                    }
                    final PrettyDialog prettyDialog = new PrettyDialog(EmployeeFeedbackActivity.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Confirm")
                            .setMessage("You are submitting feedback for "+mBinding.llEmpName.tvInfoValue.getText().toString()+",\nWorking Period - "+mBinding.llEmpDOJoin.tvInfoValue.getText().toString()+" to "+
                                    mBinding.llEmpDOLeave.tvInfoValue.getText().toString()+"\nThis will disable all credentials of employee & he/she will not be able to mark attendance\nAre you sure to proceed?")
                            .setIcon(R.drawable.info, R.color.white, null);

                        prettyDialog.addButton("Yes", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick()
                            {

                                sendFeedbackData(EmployeeFeedbackActivity.this);
                                prettyDialog.dismiss();

                            }
                        });
                    prettyDialog.addButton("No", R.color.whiteTextColor, R.color.red, new PrettyDialogCallback() {
                        @Override
                        public void onClick()
                        {
                            prettyDialog.dismiss();

                        }
                    });
                    prettyDialog.show();

                }
            }
        });
    }

    private void setToolbarData() {
        mBinding.lltoolbar.toolbar.setTitle(R.string.lbl_employee_evaluatio);
        mBinding.lltoolbar.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        mBinding.lltoolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void setEmployeeInfoData() {
        mBinding.llEmpName.tvInfoTitle.setText(R.string.lbl_emp_name);
        mBinding.llEmpName.tvInfoValue.setText(emp_name);

        mBinding.llEmpNo.tvInfoTitle.setText(R.string.lbl_emp_id);
        mBinding.llEmpNo.tvInfoValue.setText(emp_id);

        mBinding.llEmpDJoin.tvInfoTitle.setText(R.string.lbl_designation_time_join);
        mBinding.llEmpDJoin.edtInfoValue.setHint("Enter Designation");
        mBinding.llEmpDJoin.edtInfoValue.setVisibility(View.VISIBLE);
        mBinding.llEmpDJoin.tvInfoValue.setVisibility(View.GONE);

        mBinding.llEmpDLeave.tvInfoTitle.setText(R.string.lbl_designation_time_leaving);
        mBinding.llEmpDLeave.edtInfoValue.setHint("Enter Designation");
        mBinding.llEmpDLeave.edtInfoValue.setVisibility(View.VISIBLE);
        mBinding.llEmpDLeave.tvInfoValue.setVisibility(View.GONE);

        mBinding.llEmpDOJoin.tvInfoTitle.setText(R.string.lbl_date_of_join);

        mBinding.llEmpDOJoin.tvInfoTitle.setText(R.string.lbl_date_of_join);
        mBinding.llEmpDOJoin.edtInfoValue.setHint(R.string.lbl_date_of_join);
        //mBinding.llEmpDOJoin.edtInfoValue.setVisibility(View.GONE);
        mBinding.llEmpDOJoin.tvInfoValue.setVisibility(View.VISIBLE);
        mBinding.llEmpDOJoin.tvInfoValue.setText(parseDateToddMMyyyy(date_of_joining));
        mBinding.llEmpDOJoin.tvInfoValue.setEnabled(false);
       /* mBinding.llEmpDOJoin.tvInfoValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.openDatePickerDialog(EmployeeFeedbackActivity.this,mBinding.llEmpDOJoin.tvInfoValue);
            }
        });
*/

        mBinding.llEmpDOLeave.tvInfoTitle.setText(R.string.lbl_date_of_leaving);
        mBinding.llEmpDOLeave.edtInfoValue.setHint(R.string.lbl_date_of_leaving);
        //mBinding.llEmpDOLeave.edtInfoValue.setVisibility(View.GONE);
        mBinding.llEmpDOLeave.tvInfoValue.setVisibility(View.VISIBLE);
        mBinding.llEmpDOLeave.tvInfoValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.openDatePickerDialog(EmployeeFeedbackActivity.this,mBinding.llEmpDOLeave.tvInfoValue);
            }
        });

        mBinding.llEmpGrossSalary.tvInfoTitle.setText(R.string.lbl_gross_salary);
        mBinding.llEmpGrossSalary.edtInfoValue.setHint(R.string.lbl_gross_salary);
        mBinding.llEmpGrossSalary.edtInfoValue.setVisibility(View.VISIBLE);
        mBinding.llEmpGrossSalary.edtInfoValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        mBinding.llEmpGrossSalary.tvInfoValue.setVisibility(View.GONE);
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
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                //JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = new JSONObject(response);
                                String error_code = jsonObject.getString("error_code");
                                String message = jsonObject.getString("message");
                                if (error_code.equalsIgnoreCase("101")) {
                                    EmployeeLIst responseData = new Gson().fromJson(response.toString(),
                                            new TypeToken<EmployeeLIst>() {
                                            }.getType()
                                    );

                                    if (responseData != null)
                                    {
                                        if (responseData.getEmployeeData().size() != 0)
                                        {
                                            employeeDataArrayList.clear();
                                            for(int i=0;i<responseData.getEmployeeData().size();i++)
                                            {
                                               employeeDataArrayList.add(responseData.getEmployeeData().get(i).getName()+","+responseData.getEmployeeData().get(i).getDesignation());
                                            }

                                            setEmployeeData();
                                        } else {
                                            Helper.showMessage(context, "No Data Found");
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


    private void setRatingData() {
        mBinding.llJobKnowledge.tvRatingsTitle.setText(R.string.lbl_job_knowledge);
        mBinding.llWorkQuality.tvRatingsTitle.setText(R.string.lbl_work_quality);
        mBinding.llAttendance.tvRatingsTitle.setText(R.string.lbl_attendance_punctuality);
        mBinding.llInitiative.tvRatingsTitle.setText(R.string.lbl_initiative);
        mBinding.llTechnicalSkill.tvRatingsTitle.setText(R.string.lbl_technical_skills);
        mBinding.llDependability.tvRatingsTitle.setText(R.string.lbl_dependability);
    }

    private void setClickData() {
        mBinding.llJobKnowledge.rgbRating.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.chPoor:
                        jobKnowledge = 1;
                        break;

                    case R.id.chBelowAvg:
                        jobKnowledge = 2;
                        break;

                    case R.id.chAvg:
                        jobKnowledge = 3;
                        break;

                    case R.id.chGood:
                        jobKnowledge = 4;
                        break;

                    case R.id.chExcellent:
                        jobKnowledge = 5;
                        break;
                }

                getOverallRating();
            }
        });


        mBinding.llWorkQuality.rgbRating.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.chPoor:
                        workQuality = 1;
                        break;

                    case R.id.chBelowAvg:
                        workQuality = 2;
                        break;

                    case R.id.chAvg:
                        workQuality = 3;
                        break;

                    case R.id.chGood:
                        workQuality = 4;
                        break;

                    case R.id.chExcellent:
                        workQuality = 5;
                        break;
                }

                getOverallRating();
            }
        });


        mBinding.llAttendance.rgbRating.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.chPoor:
                        attendance = 1;
                        break;

                    case R.id.chBelowAvg:
                        attendance = 2;
                        break;

                    case R.id.chAvg:
                        attendance = 3;
                        break;

                    case R.id.chGood:
                        attendance = 4;
                        break;

                    case R.id.chExcellent:
                        attendance = 5;
                        break;
                }

                getOverallRating();
            }
        });

        mBinding.llInitiative.rgbRating.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.chPoor:
                        initiative = 1;
                        break;

                    case R.id.chBelowAvg:
                        initiative = 2;
                        break;

                    case R.id.chAvg:
                        initiative = 3;
                        break;

                    case R.id.chGood:
                        initiative = 4;
                        break;

                    case R.id.chExcellent:
                        initiative = 5;
                        break;
                }

                getOverallRating();
            }
        });


        mBinding.llTechnicalSkill.rgbRating.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                switch (i)
                {
                    case R.id.chPoor:
                        technicalSkill = 1;
                        break;

                    case R.id.chBelowAvg:
                        technicalSkill = 2;
                        break;

                    case R.id.chAvg:
                        technicalSkill = 3;
                        break;

                    case R.id.chGood:
                        technicalSkill = 4;
                        break;

                    case R.id.chExcellent:
                        technicalSkill = 5;
                        break;
                }
                getOverallRating();
            }
        });

        mBinding.llDependability.rgbRating.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.chPoor:
                        dependability = 1;
                        break;

                    case R.id.chBelowAvg:
                        dependability = 2;
                        break;

                    case R.id.chAvg:
                        dependability = 3;
                        break;

                    case R.id.chGood:
                        dependability = 4;
                        break;

                    case R.id.chExcellent:
                        dependability = 5;
                        break;
                }

                getOverallRating();
            }
        });
    }

    private void sendFeedbackData(Context context) {
        Helper.showProgress(context);
        final HashMap<String, String> paramiter = new HashMap<>();
        paramiter.put("mode", AppConstant.MODE_EMP_FEEDBACK);
        paramiter.put("employer_id",preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
        paramiter.put("employee_id", emp_id);
        paramiter.put("session_employee_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEEID));
        paramiter.put("designation_at_the_time_joining", mBinding.llEmpDJoin.edtInfoValue.getText().toString());
        paramiter.put("designation_at_the_time_leaving", mBinding.llEmpDLeave.edtInfoValue.getText().toString());
        paramiter.put("date_of_joining", mBinding.llEmpDOJoin.tvInfoValue.getText().toString());
        paramiter.put("date_of_leaving", mBinding.llEmpDOLeave.tvInfoValue.getText().toString());
        paramiter.put("gross_salary", mBinding.llEmpGrossSalary.edtInfoValue.getText().toString());
        paramiter.put("supervisor_name_designation", mBinding.llEmpSName.spinreportingPerson.getSelectedItem().toString());
        paramiter.put("job_knowledge", "" + jobKnowledge);
        paramiter.put("work_quality", "" + workQuality);
        paramiter.put("punctuality", "" + attendance);
        paramiter.put("initiative", "" + initiative);
        paramiter.put("technical_skill", "" + technicalSkill);
        paramiter.put("dependability", "" + dependability);
        paramiter.put("overall_rating", "" + getOverallRating());
        paramiter.put("reason_for_leaving", mBinding.edtReasonLeaving.getText().toString());
        paramiter.put("is_the_exit_formalities_completed", mBinding.edtFormalities.getText().toString());
        paramiter.put("additional_comments", mBinding.edtAdditional.getText().toString());

        new ApiCallListener(this).executeApiCall(paramiter,
                AppConstant.BASE_URL + AppConstant.EMPLOYEE_MANAGEMENT_New, AppConstant.MODE_EMP_FEEDBACK, new ApiCallListener.ApiCallInterface() {
                    @Override
                    public void onSuccess(String response, String apiName) {
                        Helper.hideProgress();
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = new JSONObject(jsonArray.getString(0));
                                String error_code = jsonObject.getString("error_code");
                                String message = jsonObject.getString("message");
                                if (error_code.equalsIgnoreCase("101")) {
                                    PrettyDialog prettyDialog = new PrettyDialog(context);
                                    prettyDialog.setCancelable(false);
                                    prettyDialog
                                            .setTitle(context.getString(R.string.app_name))
                                            .setMessage(message)
                                            .setIcon(R.drawable.ic_done, R.color.color_white, null)
                                            .addButton("Okay", R.color.color_white, R.color.blue, new PrettyDialogCallback() {
                                                @Override
                                                public void onClick() {
                                                    prettyDialog.dismiss();
                                                    finish();
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

    private boolean checkValidation() {
        if (mBinding.llEmpDJoin.edtInfoValue.getText().toString().equalsIgnoreCase("")){
            MessageUtility.showToast(this, "Please enter Designation at the time of Joining");
            return false;
        }

        if (mBinding.llEmpDLeave.edtInfoValue.getText().toString().equalsIgnoreCase("")){
            MessageUtility.showToast(this, "Please enter Designation at the time of Leaving");
            return false;
        }
        if (mBinding.llEmpDOJoin.tvInfoValue.getText().toString().equalsIgnoreCase("")){
            MessageUtility.showToast(this, "Please enter Date of Joining");
            return false;
        }
        if (mBinding.llEmpDOLeave.tvInfoValue.getText().toString().equalsIgnoreCase("")){
            MessageUtility.showToast(this, "Please enter Date of Leaving");
            return false;
        }

        if (mBinding.llEmpGrossSalary.edtInfoValue.getText().toString().equalsIgnoreCase("")){
            MessageUtility.showToast(this, "Please enter Gross Salary");
            return false;
        }

        if (mBinding.llEmpSName.spinreportingPerson.getSelectedItem().toString().equalsIgnoreCase("")){
            MessageUtility.showToast(this, "Please enter Supervisors Name & Designation");
            return false;
        }


        if (jobKnowledge == 0) {
            MessageUtility.showToast(this, "Select Job knowledge");
            return false;
        }

        if (workQuality == 0) {
            MessageUtility.showToast(this, "Select Work Quality");
            return false;
        }

        if (attendance == 0) {
            MessageUtility.showToast(this, "Select Attendance / Punctuality");
            return false;
        }

        if (initiative == 0) {
            MessageUtility.showToast(this, "Select Initiative");
            return false;
        }

        if (technicalSkill == 0) {
            MessageUtility.showToast(this, "Select Technical Skills");
            return false;
        }

        if (dependability == 0) {
            MessageUtility.showToast(this, "Select Dependability");
            return false;
        }

        if (mBinding.edtReasonLeaving.getText().toString().equalsIgnoreCase("")) {
            MessageUtility.showToast(this, "Enter Reason for leaving");
            return false;
        }
        if (mBinding.edtFormalities.getText().toString().equalsIgnoreCase("")) {
            MessageUtility.showToast(this, "Enter Exit formalities Completed");
            return false;
        }

      /*  if (mBinding.edtAdditional.getText().toString().equalsIgnoreCase("")) {
            MessageUtility.showToast(this, "Enter Additional Comments");
            return false;
        }*/

        return true;
    }

    private float getOverallRating()
    {
        float total=jobKnowledge+workQuality+attendance+initiative+technicalSkill+dependability;
        float rating=total/6;
        mBinding.tvOverallRatings.setText(String.valueOf(rating));
        return rating;
    }

    @Override
    public void selectEmployee(EmployeeLIst.EmployeeData data) {

    }
    public String parseDateToddMMyyyy(String time)
    {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MM-yyyy";
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