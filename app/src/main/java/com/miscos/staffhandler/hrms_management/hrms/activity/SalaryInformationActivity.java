package com.miscos.staffhandler.hrms_management.hrms.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miscos.staffhandler.hrms_management.hrms.adapter.DayInfoAdapter;
import com.miscos.staffhandler.hrms_management.hrms.adapter.LeaveDetailsAdapter;
import com.miscos.staffhandler.hrms_management.hrms.adapter.LeaveInfoAdapter;
import com.miscos.staffhandler.hrms_management.hrms.adapter.MonthYearAdapter;
import com.miscos.staffhandler.hrms_management.hrms.adapter.OtherDeductionAdapter;
import com.miscos.staffhandler.hrms_management.hrms.adapter.TaxInfoAdapter;
import com.miscos.staffhandler.hrms_management.hrms.api.ApiCallListener;
import com.miscos.staffhandler.hrms_management.hrms.model.DayInfoData;
import com.miscos.staffhandler.hrms_management.hrms.model.LeaveInfoData;
import com.miscos.staffhandler.hrms_management.hrms.model.MonthYearData;
import com.miscos.staffhandler.hrms_management.hrms.model.TaxInfoData;
import com.miscos.staffhandler.hrms_management.hrms.model.response.LeaveStatusDeatilsResponse;
import com.miscos.staffhandler.hrms_management.hrms.model.response.OtherDeductionResponse;
import com.miscos.staffhandler.hrms_management.hrms.model.response.SalaryAndLeaveData;
import com.miscos.staffhandler.hrms_management.hrms.model.response.TaxDetailsResponse;
import com.miscos.staffhandler.hrms_management.hrms.utility.AppConstant;
import com.miscos.staffhandler.hrms_management.hrms.utility.Helper;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.Urls.App_Urls;
import com.miscos.staffhandler.databinding.ActivitySalaryInformationBinding;
import com.miscos.staffhandler.databinding.DialogLeaveDeductionBinding;
import com.miscos.staffhandler.databinding.DialogOtherDeductionBinding;
import com.miscos.staffhandler.databinding.DialogTaxDetailsBinding;
import com.miscos.staffhandler.employee.helper.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import static com.miscos.staffhandler.hrms_management.hrms.utility.AppConstant.RUPEE_SIGN;


public class SalaryInformationActivity extends AppCompatActivity implements View.OnClickListener, MonthYearAdapter.SetOnMonthListener {

    private ActivitySalaryInformationBinding mBinding;
    private LeaveDetailsAdapter leaveDetailsAdapter;
    private DayInfoAdapter dayInfoAdapter;
    private LeaveInfoAdapter leaveInfoAdapter;
    private TaxInfoAdapter taxInfoAdapter;
    private MonthYearAdapter monthYearAdapter;
    private ArrayList<MonthYearData> monthList = new ArrayList<>();
    private String salaryOf = "";
    private String selectedMonthDate = "";
    private String employeeName="",employeeId="",employeerId="";
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_salary_information);
        preferenceManager=new PreferenceManager(this);
        init();
    }

    private void init() {
        setToolbarData();
        setSalaryDetailsData();
        monthList = Helper.getMonthList();
        setMonthYearData();
    }

    private void setToolbarData() {
        mBinding.lltoolbar.toolbar.setTitle(R.string.lbl_salary_leave_informatin);

        mBinding.lltoolbar.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        mBinding.lltoolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setLeaveDetailsSData(ArrayList<SalaryAndLeaveData.LeaveDetail> leaveDetailsDataArrayList) {
        leaveDetailsAdapter = new LeaveDetailsAdapter(this, leaveDetailsDataArrayList);
        mBinding.rvLeaveDetails.setAdapter(leaveDetailsAdapter);
    }

    private void openLeaveDeducationDialog(ArrayList<DayInfoData> dayInfoDataArrayList, ArrayList<LeaveInfoData> leaveInfoDataArrayList, String deductAmount) {
        Dialog dialog = new Dialog(this);
        DialogLeaveDeductionBinding binding = DialogLeaveDeductionBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
       /* Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);*/
        dayInfoAdapter = new DayInfoAdapter(this, dayInfoDataArrayList);
        binding.rvDayInfo.setAdapter(dayInfoAdapter);
        leaveInfoAdapter = new LeaveInfoAdapter(this, leaveInfoDataArrayList);
        binding.rvLeaveInfo.setAdapter(leaveInfoAdapter);
        binding.tvDeductAmount.setText(deductAmount);
        binding.tvEmployeeName.setText(employeeName);
        binding.tvEmployeeId.setText(employeeId);
        binding.tvMonthName.setText(salaryOf);
        binding.ivClose.setOnClickListener(view -> dialog.dismiss());
        dialog.show();

    }

    private void openTaxDetailsDialog(ArrayList<TaxInfoData> taxInfoDataArrayList, String total) {
        Dialog dialog = new Dialog(this);
        DialogTaxDetailsBinding binding = DialogTaxDetailsBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(binding.getRoot());
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        taxInfoAdapter = new TaxInfoAdapter(this, taxInfoDataArrayList);
        binding.rvTaxInfo.setAdapter(taxInfoAdapter);
        binding.tvTotal.setText(total);
        binding.tvEmployeeName.setText(employeeName);
        binding.tvEmployeeId.setText(employeeId);

        binding.ivClose.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public void  getTaxDetails()
    {
        Helper.showProgress(this);
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.EMPLOYEE_HRMS_SALARY_TRANSFER, response -> {

            try {
                JSONArray jsonArray = new JSONArray(response);
                Log.e("payment_response", " " + response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                int error_code = jsonObject.getInt("error_code");
                String msg = jsonObject.getString("message");

                if (error_code == 104)
                {
                  Helper.hideProgress();


                        JSONObject jsonObject1=jsonObject.getJSONObject("additional_payment_detail");
                        JSONArray jsonArray1=jsonObject1.getJSONArray("data");
                        String total=jsonObject1.getString("total");
                        ArrayList<String> strings=new ArrayList<>();
                        if(jsonArray1.length()>0)
                        {
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                strings.add(jsonArray1.getJSONObject(i).getString("name")+"= "+RUPEE_SIGN+jsonArray1.getJSONObject(i).getString("amount"));
                            }
                            showAdditionalPayments(strings,total,employeeName,employeeId);
                        }


                } else {
                   Helper.hideProgress();
                    final PrettyDialog prettyDialog = new PrettyDialog(this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("SHLite")
                            .setMessage(msg)
                            .setIcon(R.drawable.cross, R.color.white, null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            })
                            .show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
                final Dialog dialog1 = new Dialog(this);
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
            public void onErrorResponse(VolleyError error)
            {
               Helper.hideProgress();
                final PrettyDialog prettyDialog = new PrettyDialog(SalaryInformationActivity.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Connection Error")
                        .setMessage("Please try again")
                        .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                                /*startActivity(new Intent(PolicyAndConfiguration.this, MainActivity.class));
                                getActivity().finish();*/
                            }
                        })
                        .show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mode", "get_additional_payment_detail");
                params.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
                params.put("employee_id",preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEEID));
                params.put("month_of",selectedMonthDate);

                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
    void showAdditionalPayments(ArrayList<String> strings,String deductedamt,String emp_name,String emp_id)
    {
        double total_paid=0.0;
        ArrayList<String> arrayList=new ArrayList<>();
        arrayList.addAll(strings);
        Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_for_other_deductions);
        LinearLayout linearLayout=dialog.findViewById(R.id.lv_add_otherdetails);
        EditText edtdetails,edtamount;
        TextView txtempname=dialog.findViewById(R.id.txtempname);
        TextView txtempid=dialog.findViewById(R.id.txtempid);
        TextView txtlabel=dialog.findViewById(R.id.txtlabel);
        txtlabel.setText("Additional Payment Details");
        ImageView imageView=dialog.findViewById(R.id.imgcancel);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        txtempid.setText(emp_id);
        txtempname.setText(emp_name);
        AppCompatButton btnadd,btncancel;
        TextView txtdeductedamount =dialog.findViewById(R.id.txtdeductedtax);
        btnadd=dialog.findViewById(R.id.btnadd);
        btnadd.setVisibility(View.GONE);
        Button btnpay=dialog.findViewById(R.id.btnadditionalpayments);
        btnpay.setVisibility(View.GONE);

        ListView listView=dialog.findViewById(R.id.other_deduction_list);

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        txtdeductedamount.setText("Total Amount Paid : ₹"+deductedamt+"/-");

        btncancel=dialog.findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linearLayout.setVisibility(View.GONE);
            }
        });
        TextView txtadd=dialog.findViewById(R.id.txtaddOtherdetails);
        txtadd.setText("Click here to add payment details +");
        txtadd.setVisibility(View.GONE);
        txtadd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.VISIBLE);
            }
        });






        dialog.setCancelable(false);

        dialog.show();
    }
    private void openOtherDeductionData(OtherDeductionResponse.OtherDeductionDetail otherDeductionDetail) {
        OtherDeductionAdapter otherDeductionAdapter = new OtherDeductionAdapter(this, otherDeductionDetail.getData());
        Dialog dialog = new Dialog(this);
        DialogOtherDeductionBinding binding = DialogOtherDeductionBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(binding.getRoot());
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        binding.rvOtherDeductions.setAdapter(otherDeductionAdapter);
        binding.tvTotalDeduction.setText(String.valueOf(otherDeductionDetail.getTotal()));
        binding.ivClose.setOnClickListener(view -> dialog.dismiss());
        binding.tvEmployeeName.setText(employeeName);
        binding.tvEmployeeId.setText(employeeId);
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    private void setMonthYearData() {
        monthYearAdapter = new MonthYearAdapter(this, monthList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mBinding.rvMonthYear.setAdapter(monthYearAdapter);
        mBinding.rvMonthYear.setLayoutManager(linearLayoutManager);
    }


    @Override
    public void selectMonth(String month, String year, int position) {
        salaryOf = month + " " + year;
        mBinding.tvSalaryOf.setText(salaryOf);
        selectedMonthDate = "01-" + Helper.getMonthNumber(month) + "-" + year;
        getSalaryAndLeaveInfo(this, selectedMonthDate);
    }

    private void getSalaryAndLeaveInfo(Context context, String monthOf) {
        Helper.showProgress(context);
        final HashMap<String, String> paramiter = new HashMap<>();
        paramiter.put("mode", AppConstant.MODE_SALARY_LEAVE_REPORT);
        paramiter.put("month_of", monthOf);
        paramiter.put("employee_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEEID));
        paramiter.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
        new ApiCallListener(this).executeApiCall(paramiter,
                AppConstant.BASE_URL + AppConstant.Leave_MANAGEMENT, AppConstant.MODE_SALARY_LEAVE_REPORT, new ApiCallListener.ApiCallInterface() {
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
                                    ArrayList<SalaryAndLeaveData> salaryAndLeaveData = new Gson().fromJson(response.toString(),
                                            new TypeToken<ArrayList<SalaryAndLeaveData>>() {
                                            }.getType()
                                    );

                                    if (salaryAndLeaveData != null && salaryAndLeaveData.size() != 0) {
                                        mBinding.clMain.setVisibility(View.VISIBLE);
                                        mBinding.tvNoData.setVisibility(View.GONE);

                                        setSalaryData(salaryAndLeaveData.get(0).getReportList().getSalaryDetail().get(0));
                                        setLeaveDetailsSData(salaryAndLeaveData.get(0).getReportList().getLeaveDetail());
                                    }
                                } else {
                                    mBinding.clMain.setVisibility(View.GONE);
                                    mBinding.tvNoData.setVisibility(View.VISIBLE);
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

    private void setSalaryDetailsData() {
        mBinding.llMonthlySalary.ivInfo.setVisibility(View.INVISIBLE);
        mBinding.llLeaveDeduction.ivInfo.setVisibility(View.VISIBLE);
        mBinding.llTax.ivInfo.setVisibility(View.VISIBLE);
        mBinding.llOtherDeductions.ivInfo.setVisibility(View.VISIBLE);
        mBinding.llNetSalary.ivInfo.setVisibility(View.INVISIBLE);
        mBinding.llRemainingSalary.ivInfo.setVisibility(View.INVISIBLE);

        mBinding.llMonthlySalary.tvSalaryTitle.setText(getString(R.string.lbl_monthly_salary_rs));
        mBinding.llLeaveDeduction.tvSalaryTitle.setText(getString(R.string.lbl_leave_salary_rs));
        mBinding.llTax.tvSalaryTitle.setText(getString(R.string.lbl_taxes_rs));
        mBinding.llOtherDeductions.tvSalaryTitle.setText(getString(R.string.lbl_other_deductions));
        mBinding.llNetSalary.tvSalaryTitle.setText(getString(R.string.lbl_net_salary_rs));
        mBinding.llRemainingSalary.tvSalaryTitle.setText(getString(R.string.lbl_remaining_salary_rs));

        mBinding.llTax.ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTaxDetailsData(SalaryInformationActivity.this);

            }
        });

        mBinding.llLeaveDeduction.ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLeaveDetailsData(SalaryInformationActivity.this);
            }
        });

        mBinding.llOtherDeductions.ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOtherDeductionData(SalaryInformationActivity.this);
            }
        });
        mBinding.llAdditionalPayment.ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTaxDetails();

            }
        });

    }

    private void setSalaryData(SalaryAndLeaveData.SalaryDetail salaryData)
    {
        employeeName=salaryData.getEmployeeName();
        employeeId=salaryData.getEmployeeId();
        employeerId=salaryData.getEmployerId();
        mBinding.tvSalaryOf.setText(salaryOf);
        mBinding.llLeaveDeduction.tvSalary.setText(RUPEE_SIGN + "" + salaryData.getLeaveDeduction());
        mBinding.llTax.tvSalary.setText(RUPEE_SIGN + "" + salaryData.getTaxs());
        mBinding.llOtherDeductions.tvSalary.setText(RUPEE_SIGN + "" + salaryData.getOtherDeductionAmt());
        mBinding.llNetSalary.tvSalary.setText(RUPEE_SIGN + "" + salaryData.getNetSalary());
        mBinding.llRemainingSalary.tvSalary.setText(RUPEE_SIGN + "" + salaryData.getRemainingSalary());
        mBinding.llMonthlySalary.tvSalary.setText(RUPEE_SIGN + "" + salaryData.getActualSalary());
        mBinding.llAdditionalPayment.tvSalaryTitle.setText("Additional Payment ");
        mBinding.llAdditionalPayment.tvSalary.setText(RUPEE_SIGN + "" + salaryData.getAdditional_payment());
        mBinding.tvTotalAmount.setText(RUPEE_SIGN + "" + salaryData.getRemainingSalary());
    }

    private void getTaxDetailsData(Context context) {
        Helper.showProgress(context);
        final HashMap<String, String> paramiter = new HashMap<>();
        paramiter.put("mode", AppConstant.MODE_SHOW_TAX_DETAILS);
        paramiter.put("month_of",selectedMonthDate);
        paramiter.put("employee_id", employeeId);
        paramiter.put("employer_id", employeerId);
        new ApiCallListener(this).executeApiCall(paramiter,
                AppConstant.BASE_URL + AppConstant.SALARY_CALCULATIONS, AppConstant.MODE_SHOW_TAX_DETAILS, new ApiCallListener.ApiCallInterface() {
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
                                    ArrayList<TaxDetailsResponse> responseData = new Gson().fromJson(response.toString(),
                                            new TypeToken<ArrayList<TaxDetailsResponse>>() {
                                            }.getType()
                                    );
                                    setTaxDetailsData(responseData.get(0).getTaxDetailList());
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

    private void setTaxDetailsData(TaxDetailsResponse.TaxDetailList taxDetailsData) {
        double total = 0.0;
        ArrayList<TaxInfoData> taxInfoDataArrayList = new ArrayList<>();
        String pf = taxDetailsData.getProvidentFound().replace("=", "");
        String pf1 = pf.split("#")[0];
        String pf2 = pf.split("#")[1];
        total = total + Double.parseDouble(pf2);

        if (pf1.equalsIgnoreCase("")){
            pf1="0";
        }

        if (pf2.equalsIgnoreCase("")){
            pf2="0";
        }
        TaxInfoData pftax = new TaxInfoData("Provident Found", pf1, pf2);
        taxInfoDataArrayList.add(pftax);

        String pt = taxDetailsData.getProfessionalTax().replace("=", "");
        String pt1 = pt.split("#")[0];
        String pt2 = pt.split("#")[1];
        total = total + Double.parseDouble(pt2);
        if (pt1.equalsIgnoreCase("")){
            pt1="0";
        }

        if (pt2.equalsIgnoreCase("")){
            pt2="0";
        }
        TaxInfoData pttax = new TaxInfoData("Professional Tax", pt1, pt2);
        taxInfoDataArrayList.add(pttax);


        String esic = taxDetailsData.getESIC().replace("=", "");
        String esic1 = esic.split("#")[0];
        String esic2 = esic.split("#")[1];
        total = total + Double.parseDouble(esic2);
        if (esic1.equalsIgnoreCase("")){
            esic1="0";
        }

        if (esic2.equalsIgnoreCase("")){
            esic2="0";
        }
        TaxInfoData esictax = new TaxInfoData("ESIC", esic1, esic2);
        taxInfoDataArrayList.add(esictax);

        String GST = taxDetailsData.getGST().replace("=", "");
        String GST1 = GST.split("#")[0];
        String GST2 = GST.split("#")[1];
        total = total + Double.parseDouble(GST2);
        if (GST1.equalsIgnoreCase("")){
            GST1="0";
        }

        if (GST2.equalsIgnoreCase("")){
            GST2="0";
        }
        TaxInfoData GSTx = new TaxInfoData("GST", GST1, GST2);
        taxInfoDataArrayList.add(GSTx);

        String TDS = taxDetailsData.getTDS().replace("=", "");
        String TDS1 = TDS.split("#")[0];
        String TDS2 = TDS.split("#")[1];
        total = total + Double.parseDouble(TDS2);
        if (TDS1.equalsIgnoreCase("")){
            TDS1="0";
        }

        if (TDS2.equalsIgnoreCase("")){
            TDS2="0";
        }
        TaxInfoData TDSx = new TaxInfoData("TDS", TDS1, TDS2);
        taxInfoDataArrayList.add(TDSx);

        openTaxDetailsDialog(taxInfoDataArrayList, String.valueOf(total));
    }


    private void getOtherDeductionData(Context context) {
        Helper.showProgress(context);
        final HashMap<String, String> paramiter = new HashMap<>();
        paramiter.put("mode", AppConstant.MODE_OTHER_DEDUCTION_DETAILS);
        paramiter.put("month_of", selectedMonthDate);
        paramiter.put("employee_id", employeeId);
        paramiter.put("employer_id", employeerId);
        new ApiCallListener(this).executeApiCall(paramiter,
                AppConstant.BASE_URL + AppConstant.SALARY_CALCULATIONS, AppConstant.MODE_OTHER_DEDUCTION_DETAILS, new ApiCallListener.ApiCallInterface() {
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
                                    ArrayList<OtherDeductionResponse> responsesData = new Gson().fromJson(response.toString(),
                                            new TypeToken<ArrayList<OtherDeductionResponse>>() {
                                            }.getType()
                                    );

                                    if (responsesData.get(0).getOtherDeductionDetail().getData().size() != 0) {
                                        openOtherDeductionData(responsesData.get(0).getOtherDeductionDetail());
                                    } else {
                                        Helper.showMessage(context, message);
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

    private void getLeaveDetailsData(Context context) {
        Helper.showProgress(context);
        final HashMap<String, String> paramiter = new HashMap<>();
        paramiter.put("mode", AppConstant.MODE_SHOW_LEAVE_DETAILS);
        paramiter.put("month_of", selectedMonthDate);
        paramiter.put("employee_id", employeeId);
        paramiter.put("employer_id", employeerId);
        new ApiCallListener(this).executeApiCall(paramiter,
                AppConstant.BASE_URL + AppConstant.SALARY_CALCULATIONS, AppConstant.MODE_SHOW_LEAVE_DETAILS, new ApiCallListener.ApiCallInterface() {
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
                                    ArrayList<LeaveStatusDeatilsResponse> responseData = new Gson().fromJson(response.toString(),
                                            new TypeToken<ArrayList<LeaveStatusDeatilsResponse>>() {
                                            }.getType()
                                    );
                                    setLeaveDetailsData(responseData.get(0));
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

    private void setLeaveDetailsData(LeaveStatusDeatilsResponse leaveDetailsData) {
        if (leaveDetailsData.getLeaveDeatils() != null && !leaveDetailsData.getLeaveDeatils().equalsIgnoreCase("")) {
            ArrayList<DayInfoData> dayInfoDataArrayList = new ArrayList<>();
            String[] dayData = leaveDetailsData.getLeaveDeatils().split(",");
            String wDay = dayData[0];
            String wDay1 = wDay.split("=")[0];
            String wDay2 = wDay.split("=")[1];
            DayInfoData dwDayData = new DayInfoData(wDay1, wDay2);
            dayInfoDataArrayList.add(dwDayData);

            String pDay = dayData[1];
            String pDay1 = pDay.split("=")[0];
            String pDay2 = pDay.split("=")[1];
            DayInfoData pDayData = new DayInfoData(pDay1, pDay2);
            dayInfoDataArrayList.add(pDayData);

            String ADay = dayData[2];
            String ADay1 = ADay.split("=")[0];
            String ADay2 = ADay.split("=")[1];
            DayInfoData ADayData = new DayInfoData(ADay1, ADay2);
            dayInfoDataArrayList.add(ADayData);

            String hDay = dayData[3];
            String hDay1 = hDay.split("=")[0];
            String hDay2 = hDay.split("=")[1];
            DayInfoData hDayData = new DayInfoData(hDay1, hDay2);
            dayInfoDataArrayList.add(hDayData);

            ArrayList<LeaveInfoData> leaveInfoDataArrayList = new ArrayList<>();
            if (dayData.length > 4) {
                for (int i = 4; i < dayData.length; i++) {
                    String leaveData = dayData[i];
                    String leaveData1 = leaveData.split("=")[0];
                    String leaveData2 = leaveData.split("=")[1].split("#")[0];
                    String leaveData3 = leaveData.split("=")[1].split("#")[1];
                    LeaveInfoData leaveInfoData = new LeaveInfoData(leaveData1, leaveData2, leaveData3);
                    leaveInfoDataArrayList.add(leaveInfoData);
                }
            }
            openLeaveDeducationDialog(dayInfoDataArrayList, leaveInfoDataArrayList, String.valueOf(leaveDetailsData.getDeductedAmount()));
        }
    }
}