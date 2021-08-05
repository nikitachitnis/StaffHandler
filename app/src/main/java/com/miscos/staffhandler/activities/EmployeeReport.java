package com.miscos.staffhandler.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.fivemin.chief.nonetworklibrary.networkBroadcast.NoNet;
import com.github.lguipeng.library.animcheckbox.AnimCheckBox;
import com.miscos.staffhandler.Adapters.EmployeeReportAdapter;
import com.miscos.staffhandler.MainActivity;
import com.miscos.staffhandler.Model.ReportModel;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.Urls.App_Urls;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

/*Developed under Miscos
 * Developed by Nikhil
 * 28-05-2020
 * */

public class EmployeeReport extends AppCompatActivity implements AnimCheckBox.OnCheckedChangeListener, PaymentResultListener {
    ImageView imSend, tvBack;
    Toolbar toolbar;
    RelativeLayout rlMain;
    TextView tvNoData;
    int mapRequest = 100;
    AnimCheckBox cbPerDay, cbYearly;
    PreferenceManager preferenceManager;
    ProgressDialog progressDialog;
    private RecyclerView rcReports;
    private EmployeeReportAdapter employeeReportAdapter;
    private List<ReportModel> reportModel = new ArrayList<>();
    private SwipeRefreshLayout pullToRefresh;
    private String emp_count, date_count, employee_name, report_for, employee_id, from_date, to_date, employer_id, order_id, emailID, email, emailcc, orderamount, subPlan, transaction_id,
            employeeMode, employeeCount, dateCount, initial_amt, par_disscount, par_diss_amt, y_net_payable, y_diss_amt, par_net_payable, y_disscount, y_initials_amt;
    private Dialog dialog;
    private Dialog dialogForSub;
    private CardView perDay, cbMonthly, cvYearly;
    // No Internet Dialog
    private NoNet mNoNet;
    private FragmentManager fm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_report);
        fm = getSupportFragmentManager();
        mNoNet = new NoNet();
        mNoNet.initNoNet(this, fm);

        dialog = new Dialog(EmployeeReport.this);
        dialogForSub = new Dialog(EmployeeReport.this);

        settingToolBar();
        preferenceManager = new PreferenceManager(this);
        employer_id = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID);
        Intent intent = getIntent();
        employeeMode = intent.getStringExtra("mode");
        employee_id = intent.getStringExtra("employee_id");
        from_date = intent.getStringExtra("from_date");
        to_date = intent.getStringExtra("to_date");
        employee_name = intent.getStringExtra("selected_employee");
        report_for = intent.getStringExtra("report_for");
        rlMain = findViewById(R.id.rlMain);
        imSend = findViewById(R.id.imSend);
        tvBack = findViewById(R.id.imBack);
        //Progress Bar
        progressDialog = new ProgressDialog(EmployeeReport.this);
        progressDialog = ProgressDialog.show(EmployeeReport.this, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.dismiss();
        rcReports = findViewById(R.id.rcEmployeeReport);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rcReports.setLayoutManager(mLayoutManager);
        rcReports.setItemAnimator(new DefaultItemAnimator());
        employeeReportAdapter = new EmployeeReportAdapter(this, reportModel);
        //  pullToRefresh = findViewById(R.id.pullToRefresh);
        //  pullToRefresh.setOnRefreshListener(this);
        tvNoData = findViewById(R.id.tvNodata);
        Log.d("TAG", "onCreate: " + employeeMode);
        EmployeeReportList();

        imSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (y_net_payable.equalsIgnoreCase("0")) {
                    showDialogForSend();
                } else {
                    viewForSubscription();
                }

            }
        });


        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployeeReport.this, MainActivity.class));
                finish();
            }
        });

    }


    //sending report to mail
    public void showDialogForSend() {
// Include dialog.xml file
        dialog.setContentView(R.layout.dialog_send);
        dialog.show();
        dialog.setCancelable(false);
        LinearLayout lvMain = dialog.findViewById(R.id.lvMain);
        LinearLayout lvMain1 = dialog.findViewById(R.id.lvMain1);
        Button declineButton = (Button) dialog.findViewById(R.id.btn_cancel);
        Button buttonSend = (Button) dialog.findViewById(R.id.btnSend);
        EditText etEmail = dialog.findViewById(R.id.etEmail);
        EditText etEmailCC = dialog.findViewById(R.id.etEmailCC);
        Button yesButton = (Button) dialog.findViewById(R.id.btn_video);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                email = etEmail.getText().toString();
                emailcc = etEmailCC.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    Toast.makeText(getApplicationContext(), "Enter valid email address", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(EmployeeReport.this, "Send to " + email, Toast.LENGTH_SHORT).show();
                    if (emailcc.isEmpty()) {
                        EmployeeReportListToEmail(email, emailcc);
                        dialog.dismiss();
                    } else if (!emailcc.matches(emailPattern)) {
                        Toast.makeText(getApplicationContext(), "Enter valid email address", Toast.LENGTH_SHORT).show();
                    } else {
                        EmployeeReportListToEmail(email, emailcc);
                        dialog.dismiss();
                    }

                }

            }
        });

// if decline button is clicked, close the custom dialog
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });


// if decline button is clicked, close the custom dialog
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvMain.setVisibility(View.GONE);
                lvMain1.setVisibility(View.VISIBLE);
            }
        });
    }

    public void viewForSubscription()
    {
        dialogForSub = new Dialog(EmployeeReport.this);
// Include dialog.xml file
        dialogForSub.setContentView(R.layout.subscription_for_pack);
        dialogForSub.show();
        dialogForSub.setCancelable(false);

        Button buttonPayment = (Button) dialogForSub.findViewById(R.id.btn_payment);
        ImageView imgCancel = (ImageView) dialogForSub.findViewById(R.id.imCancel);
        TextView tvDisscountP = dialogForSub.findViewById(R.id.tvDisscountP);
        TextView tvDisscountY = dialogForSub.findViewById(R.id.tvDisscountY);
        TextView tvPAmount = dialogForSub.findViewById(R.id.tvPAmount);
        TextView tvYAmount = dialogForSub.findViewById(R.id.tvYAmount);
        tvDisscountP.setText(par_disscount);
        tvDisscountY.setText(y_disscount);
        tvPAmount.setText("₹" + par_net_payable + " for this report");
        tvYAmount.setText("₹" + y_net_payable + " /year");
        cbPerDay = dialogForSub.findViewById(R.id.cbParDay);
        cbYearly = dialogForSub.findViewById(R.id.cbYearly);
        cbYearly.setOnCheckedChangeListener(this);
        cbPerDay.setOnCheckedChangeListener(this);

        buttonPayment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (cbPerDay.isChecked())
                {
                    subPlan = "parDay";
                    payparPlan();
                    orderamount = par_net_payable;//par_net_payable;
                } else if (cbYearly.isChecked())
                {
                    subPlan = "yearly";
                    orderamount = y_net_payable;
                    yearlyPlan();
                }
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (employeeMode.equalsIgnoreCase("select_all") || employeeMode.equalsIgnoreCase("multiple"))
                {
                    startActivity(new Intent(EmployeeReport.this, MainActivity.class));
                    finish();
                    dialogForSub.dismiss();
                } else {
                    dialogForSub.dismiss();
                }

            }
        });

    }

    public void EmployeeReportList()
    {
        progressDialog.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.Employee_report, response ->
        {
            Log.e("reports_response", " " + response);
            progressDialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                int error_code = jsonObject.getInt("error_code");
                String msg = jsonObject.getString("message");

                preferenceManager.setPreference(PreferenceManager.KEY_DATE_COUNT, date_count);
                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_COUNT, emp_count);

                if (error_code == 101)
                {
                    emp_count = jsonObject.getString("emp_count");
                    date_count = jsonObject.getString("date_count");
                    reportModel.clear();
                    JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                    if (employeeMode.equalsIgnoreCase("select_all") || employeeMode.equalsIgnoreCase("multiple")) {
                        for (int i = 0; i < jsonArrayData.length(); i++) {
                            JSONObject dayActivityList = jsonArrayData.getJSONObject(i);
                            ReportModel reportModel1 = new ReportModel();
                            reportModel1.setS_no(dayActivityList.getInt("s_no"));
                            reportModel1.setEmployee_name(dayActivityList.getString("employee_name"));
                            reportModel1.setTotal_working_days(dayActivityList.getInt("total_working_days"));
                            reportModel1.setDay_in_count(dayActivityList.getInt("day_in_count"));
                            reportModel1.setDay_in_count(dayActivityList.getInt("day_out_count"));
                            reportModel1.setTotal_working_hours(dayActivityList.getInt("total_working_hours"));
                        }
                    } else if (employeeMode.equalsIgnoreCase("single")) {
                        for (int i = 0; i < jsonArrayData.length(); i++) {
                            JSONObject dayActivityList = jsonArrayData.getJSONObject(i);
                            ReportModel reportModel1 = new ReportModel();
                            reportModel1.setS_no(dayActivityList.getInt("s_no"));
                            reportModel1.setEmployee_name(dayActivityList.getString("employee_name"));
                            reportModel1.setDate(dayActivityList.getString("date"));
                            reportModel1.setDay_in(dayActivityList.getString("day_in"));
                            reportModel1.setDay_out(dayActivityList.getString("day_out"));
                            reportModel.add(reportModel1);
                        }
                    }
                    subscriptionPlan();
                    employeeReportAdapter = new EmployeeReportAdapter(this, reportModel);
                    rcReports.setAdapter(employeeReportAdapter);
                    employeeReportAdapter.notifyDataSetChanged();
                    if (employeeMode.equalsIgnoreCase("single")) {
                        if (reportModel.size() == 0) {
                            tvNoData.setVisibility(View.VISIBLE);
                            rlMain.setVisibility(View.GONE);
                        } else {
                            rlMain.setVisibility(View.VISIBLE);
                            tvNoData.setVisibility(View.GONE);
                        }
                    }
                } else if (error_code == 104)
                {
                    if (employeeMode.equalsIgnoreCase("single"))
                    {
                        imSend.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
                    }
                    else
                    {imSend.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);

                    }
                }

            } catch (JSONException e) {
                tvNoData.setVisibility(View.VISIBLE);
                Log.e("Check", "JSONEXCEPTION" + e);
                e.printStackTrace();
                final Dialog dialog1 = new Dialog(EmployeeReport.this);
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
            progressDialog.dismiss();
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                final PrettyDialog prettyDialog = new PrettyDialog(EmployeeReport.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Connection Error")
                        .setMessage("Please try again")
                        .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                                startActivity(new Intent(EmployeeReport.this, MainActivity.class));
                                finish();
                            }
                        })
                        .show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (employeeMode.equalsIgnoreCase("select_all")) {
                    params.put("mode", "multiple");
                    params.put("employer_id", employer_id);
                    params.put("employee_id", "all");
                    params.put("from_date", from_date);
                    params.put("to_date", to_date);
                    params.put("send", "0");
                    params.put("report_for", report_for);
                } else if (employeeMode.equalsIgnoreCase("single")) {
                    params.put("mode", "single");
                    params.put("employer_id", employer_id);
                    params.put("employee_id", employee_id);
                    params.put("from_date", from_date);
                    params.put("to_date", to_date);
                    params.put("send", "0");
                    params.put("report_for", report_for);
                } else if (employeeMode.equalsIgnoreCase("multiple")) {
                    params.put("mode", "multiple");
                    params.put("employer_id", employer_id);
                    params.put("employee_id", employee_id);
                    params.put("from_date", from_date);
                    params.put("to_date", to_date);
                    params.put("send", "0");
                    params.put("report_for", report_for);
                }
                Log.e("params is", " " + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(EmployeeReport.this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    //sending email
    public void EmployeeReportListToEmail(final String email_id, final String mailcc) {
        progressDialog.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.Employee_report, response -> {
            progressDialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(response);
                Log.e("reports_email response", " " + response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                int error_code = jsonObject.getInt("error_code");
                String msg = jsonObject.getString("message");
                if (error_code == 101) {
                    dialog.dismiss();
                    final PrettyDialog prettyDialog = new PrettyDialog(EmployeeReport.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Report Send")
                            .setMessage("Report Send Successfully")
                            .setIcon(R.drawable.send, R.color.primaryTextColor, null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                    startActivity(new Intent(EmployeeReport.this, MainActivity.class));
                                    finish();
                                    dialog.dismiss();
                                }
                            })
                            .show();
                } else if (error_code == 100) {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                final Dialog dialog1 = new Dialog(EmployeeReport.this);
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
            progressDialog.dismiss();
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pullToRefresh.setRefreshing(false);
                progressDialog.dismiss();
                final PrettyDialog prettyDialog = new PrettyDialog(EmployeeReport.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Connection Error")
                        .setMessage("Please try again")
                        .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                                startActivity(new Intent(EmployeeReport.this, MainActivity.class));
                                finish();
                            }
                        })
                        .show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (employeeMode.equalsIgnoreCase("single")) {
                    params.put("mode", "single");
                    params.put("employer_id", employer_id);
                    params.put("employee_id", employee_id);
                    params.put("from_date", from_date);
                    params.put("to_date", to_date);
                    params.put("email_id", email_id);
                    params.put("amount", orderamount);
                    params.put("report_for", report_for);
                    params.put("cc", mailcc);
                    params.put("txn_id", preferenceManager.getStringPreference(PreferenceManager.KEY_PAYMENT_ID));
                    params.put("order_id", preferenceManager.getStringPreference(PreferenceManager.KEY_ORDER_ID));
                    params.put("send", "1");
                } else if (employeeMode.equalsIgnoreCase("multiple")) {
                    params.put("mode", "multiple");
                    params.put("employer_id", employer_id);
                    params.put("employee_id", employee_id);
                    params.put("from_date", from_date);
                    params.put("to_date", to_date);
                    params.put("email_id", email_id);
                    params.put("amount", orderamount);
                    params.put("cc", mailcc);
                    params.put("txn_id", preferenceManager.getStringPreference(PreferenceManager.KEY_PAYMENT_ID));
                    params.put("order_id", preferenceManager.getStringPreference(PreferenceManager.KEY_ORDER_ID));
                    params.put("send", "1");
                    params.put("report_for", report_for);
                } else if (employeeMode.equalsIgnoreCase("select_all")) {
                    params.put("mode", "multiple");
                    params.put("employer_id", employer_id);
                    params.put("employee_id", "all");
                    params.put("from_date", from_date);
                    params.put("to_date", to_date);
                    params.put("email_id", email_id);
                    params.put("amount", orderamount);
                    params.put("cc", mailcc);
                    params.put("txn_id", preferenceManager.getStringPreference(PreferenceManager.KEY_PAYMENT_ID));
                    params.put("order_id", preferenceManager.getStringPreference(PreferenceManager.KEY_ORDER_ID));
                    params.put("send", "1");
                    params.put("report_for", report_for);
                }
                if (y_net_payable.equalsIgnoreCase("0")) {
                    params.put("mode", employeeMode);
                    params.put("employer_id", employer_id);
                    params.put("employee_id", employee_id);
                    params.put("from_date", from_date);
                    params.put("amount", "0");
                    params.put("to_date", to_date);
                    params.put("txn_id", "");
                    params.put("email_id", email_id);
                    params.put("send", "1");
                    params.put("report_for", report_for);
                }
                Log.e("params is", " " + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(EmployeeReport.this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    public void EmployeeReportResponse() {
        progressDialog.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.Employee_report, response -> {
            progressDialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(response);
                Log.e("reports_email_response", " " + response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                int error_code = jsonObject.getInt("error_code");
                String msg = jsonObject.getString("message");

                if (error_code == 101) {
                    showDialogForSend();
                } else if (error_code == 100) {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                final Dialog dialog1 = new Dialog(EmployeeReport.this);
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
            progressDialog.dismiss();
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pullToRefresh.setRefreshing(false);
                progressDialog.dismiss();
                final PrettyDialog prettyDialog = new PrettyDialog(EmployeeReport.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Connection Error")
                        .setMessage("Please try again")
                        .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                                startActivity(new Intent(EmployeeReport.this, MainActivity.class));
                                finish();
                            }
                        })
                        .show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (employeeMode.equalsIgnoreCase("single")) {
                    params.put("mode", "single");
                    params.put("employer_id", employer_id);
                    params.put("employee_id", employee_id);
                    params.put("from_date", from_date);
                    params.put("to_date", to_date);
                    params.put("amount", orderamount);
                    params.put("report_for", report_for);
                    params.put("txn_id", preferenceManager.getStringPreference(PreferenceManager.KEY_PAYMENT_ID));
                    params.put("order_id", preferenceManager.getStringPreference(PreferenceManager.KEY_ORDER_ID));
                    params.put("send", "3");
                } else if (employeeMode.equalsIgnoreCase("multiple")) {
                    params.put("mode", "multiple");
                    params.put("employer_id", employer_id);
                    params.put("employee_id", employee_id);
                    params.put("from_date", from_date);
                    params.put("to_date", to_date);
                    params.put("amount", orderamount);
                    params.put("report_for", report_for);
                    params.put("txn_id", preferenceManager.getStringPreference(PreferenceManager.KEY_PAYMENT_ID));
                    params.put("order_id", preferenceManager.getStringPreference(PreferenceManager.KEY_ORDER_ID));
                    params.put("send", "3");
                } else if (employeeMode.equalsIgnoreCase("select_all")) {
                    params.put("mode", "multiple");
                    params.put("employer_id", employer_id);
                    params.put("employee_id", "all");
                    params.put("from_date", from_date);
                    params.put("to_date", to_date);
                    params.put("report_for", report_for);
                    params.put("amount", orderamount);
                    params.put("txn_id", preferenceManager.getStringPreference(PreferenceManager.KEY_PAYMENT_ID));
                    params.put("order_id", preferenceManager.getStringPreference(PreferenceManager.KEY_ORDER_ID));
                    params.put("send", "3");
                }
                Log.e("params is", " " + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(EmployeeReport.this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    //subscription plan json
    private void subscriptionPlan() {
        progressDialog.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.Report_Subscription, response -> {
            progressDialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(response);
                Log.e("subscription response", " " + response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                int error_code = jsonObject.getInt("error_code");
                String msg = jsonObject.getString("message");

                if (error_code == 101) {
                    JSONArray jsonArrayData = jsonObject.getJSONArray("subscribe_data");
                    for (int i = 0; i < jsonArrayData.length(); i++)
                    {
                        JSONObject subscribeData = jsonArrayData.getJSONObject(i);
                        initial_amt = subscribeData.getString("p_initial_amt");
                        par_disscount = subscribeData.getString("p_discount");
                        par_diss_amt = subscribeData.getString("p_discounted_amt");
                        par_net_payable = subscribeData.getString("p_net_payable");
                        y_disscount = subscribeData.getString("y_discount");
                        y_initials_amt = subscribeData.getString("y_initial_amt");
                        y_diss_amt = subscribeData.getString("y_discounted_amt");
                        y_net_payable = subscribeData.getString("y_net_payable");
                        preferenceManager.setPreference(PreferenceManager.KEY_PAYABLE_AMT, y_net_payable);
                    }
                    imSend.setVisibility(View.VISIBLE);
                    if (employeeMode.equalsIgnoreCase("select_all") || employeeMode.equalsIgnoreCase("multiple")) {
                        imSend.setVisibility(View.GONE);
                        if (y_net_payable.equalsIgnoreCase("0")) {
                            showDialogForSend();
                        } else {
                            Handler handler1 = new Handler();
                            handler1.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    CFAlertDialog.Builder builder = new CFAlertDialog.Builder(EmployeeReport.this)
                                            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                                            .setIcon(R.drawable.info)
                                            .setTitle("Discount")
                                            .setMessage("Congratulations!! " + "\n" + "We Have a special discount for you. Please click Continue for details")
                                            .addButton("Continue", -1, Color.DKGRAY,
                                                    CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.CENTER, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            viewForSubscription();
                                                            dialogInterface.dismiss();
                                                        }

                                                    });

                                    // Show the alert
                                    builder.show();
                                }
                            }, 500);
                        }
                    }
                } else {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                final Dialog dialog1 = new Dialog(EmployeeReport.this);
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
                        //startActivity(new Intent(EmployeeReport.this, MainActivity.class));
                        finish();
                    }
                });
            }
            progressDialog.dismiss();
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pullToRefresh.setRefreshing(false);
                progressDialog.dismiss();
                final PrettyDialog prettyDialog = new PrettyDialog(EmployeeReport.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Connection Error")
                        .setMessage("Please try again")
                        .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                                startActivity(new Intent(EmployeeReport.this, MainActivity.class));
                                finish();
                            }
                        })
                        .show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mode", "fetch");
                params.put("employer_id", employer_id);
                params.put("emp_count", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_COUNT));
                params.put("date_count", preferenceManager.getStringPreference(PreferenceManager.KEY_DATE_COUNT));
                Log.e("params is", " " + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(EmployeeReport.this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void subscriptionPlanUpdate() {
        progressDialog.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.Report_Subscription, response -> {
            progressDialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(response);
                Log.e("subscription response", " " + response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                int error_code = jsonObject.getInt("error_code");
                String msg = jsonObject.getString("message");

                if (error_code == 101) {
                    EmployeeReportResponse();
                } else {
                    Toast.makeText(EmployeeReport.this, msg, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                final Dialog dialog1 = new Dialog(EmployeeReport.this);
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
            progressDialog.dismiss();
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pullToRefresh.setRefreshing(false);
                progressDialog.dismiss();
                final PrettyDialog prettyDialog = new PrettyDialog(EmployeeReport.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Connection Error")
                        .setMessage("Please try again")
                        .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                                startActivity(new Intent(EmployeeReport.this, MainActivity.class));
                                finish();
                            }
                        })
                        .show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mode", "update");
                params.put("employer_id", employer_id);
                Log.e("params is", " " + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(EmployeeReport.this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void requestPayment()
    {
        progressDialog.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.Employee_report, response -> {
            progressDialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(response);
                Log.e("payment req response", " " + response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                int error_code = jsonObject.getInt("error_code");
                String msg = jsonObject.getString("message");

                if (error_code == 101) {
                    order_id = jsonObject.getString("order_id");
                    preferenceManager.setPreference(PreferenceManager.KEY_ORDER_ID, order_id);
                    startPayment();
                } else {
                    Toast.makeText(EmployeeReport.this, msg, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                final Dialog dialog1 = new Dialog(EmployeeReport.this);
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
                pullToRefresh.setRefreshing(false);
                progressDialog.dismiss();
                final PrettyDialog prettyDialog = new PrettyDialog(EmployeeReport.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Connection Error")
                        .setMessage("Please try again")
                        .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                                startActivity(new Intent(EmployeeReport.this, MainActivity.class));
                                finish();
                            }
                        })
                        .show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mode", "request");
                params.put("employer_id", employer_id);
                params.put("amount", orderamount);
                Log.e("params is", " " + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(EmployeeReport.this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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

    private void paymentFailed() {
        progressDialog.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.Employee_report, response -> {
            progressDialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(response);
                Log.e("payment failed response", " " + response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                int error_code = jsonObject.getInt("error_code");
                String msg = jsonObject.getString("message");

                if (error_code == 101) {
                    if (employeeMode.equalsIgnoreCase("multiple") || employeeMode.equalsIgnoreCase("select_all")) {
                        startActivity(new Intent(EmployeeReport.this, MainActivity.class));
                        finish();
                    }
                    Log.d("TAG", "paymentFailed: " + msg);
                } else {
                    Toast.makeText(EmployeeReport.this, msg, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                final Dialog dialog1 = new Dialog(EmployeeReport.this);
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
                progressDialog.dismiss();
                final PrettyDialog prettyDialog = new PrettyDialog(EmployeeReport.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Connection Error")
                        .setMessage("Please try again")
                        .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                                startActivity(new Intent(EmployeeReport.this, MainActivity.class));
                                finish();
                            }
                        })
                        .show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (employeeMode.equalsIgnoreCase("single")) {
                    params.put("mode", "single");
                    params.put("employer_id", employer_id);
                    params.put("employee_id", employee_id);
                    params.put("from_date", from_date);
                    params.put("to_date", to_date);
                    params.put("report_for", report_for);
                    params.put("txn_id", preferenceManager.getStringPreference(PreferenceManager.KEY_PAYMENT_ID));
                    params.put("order_id", preferenceManager.getStringPreference(PreferenceManager.KEY_ORDER_ID));
                    params.put("amount", orderamount);
                    params.put("send", "2");
                } else if (employeeMode.equalsIgnoreCase("multiple")) {
                    params.put("mode", "multiple");
                    params.put("employer_id", employer_id);
                    params.put("employee_id", employee_id);
                    params.put("from_date", from_date);
                    params.put("to_date", to_date);
                    params.put("report_for", report_for);
                    params.put("txn_id", preferenceManager.getStringPreference(PreferenceManager.KEY_PAYMENT_ID));
                    params.put("order_id", preferenceManager.getStringPreference(PreferenceManager.KEY_ORDER_ID));
                    params.put("amount", orderamount);
                    params.put("send", "2");
                } else if (employeeMode.equalsIgnoreCase("select_all")) {
                    params.put("mode", "multiple");
                    params.put("employer_id", employer_id);
                    params.put("employee_id", "all");
                    params.put("from_date", from_date);
                    params.put("to_date", to_date);
                    params.put("report_for", report_for);
                    params.put("txn_id", preferenceManager.getStringPreference(PreferenceManager.KEY_PAYMENT_ID));
                    params.put("order_id", preferenceManager.getStringPreference(PreferenceManager.KEY_ORDER_ID));
                    params.put("amount", orderamount);
                    params.put("send", "2");
                }
                Log.e("payment failed params", " " + params);

                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(EmployeeReport.this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void payparPlan() {
        final Dialog dialog = new Dialog(EmployeeReport.this);
// Include dialog.xml file
        dialog.setContentView(R.layout.dialogpayperplan);
        dialog.show();

        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPayment();
                cbPerDay.setChecked(true);
                boolean animation = true;
                cbYearly.setChecked(false, animation);
                dialog.dismiss();
                dialogForSub.dismiss();
            }
        });

// if decline button is clicked, close the custom dialog
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                boolean animation = true;
                cbPerDay.setChecked(false, animation);
                dialog.dismiss();

            }
        });
    }

    private void yearlyPlan() {
        final Dialog dialog = new Dialog(EmployeeReport.this);
// Include dialog.xml file
        dialog.setContentView(R.layout.yearlyplan);
        dialog.show();

        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPayment();
                cbYearly.setChecked(true);
                boolean animation = true;
                cbPerDay.setChecked(false, animation);
                dialog.dismiss();
                dialogForSub.dismiss();
            }
        });

// if decline button is clicked, close the custom dialog
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                boolean animation = true;
                cbYearly.setChecked(false, animation);
                dialog.dismiss();
            }
        });
    }

    //Payment Mode
    public void startPayment() {
        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();
        co.setImage(R.drawable.splashlogo);
        try {
            JSONObject options = new JSONObject();
            options.put("name", "TouchTech Pvt Ltd.");
            options.put("description", "Daily Report Payment");
            //You can omit the image option to fetch the image from dashboard
            options.put("currency", "INR");
            String payment = orderamount;
            // amount is in paise so please multiple it by 100
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        // payment successfull pay_DGU19rDsInjcF2
        dialogForSub.dismiss();
        Log.e("TAG", " payment successful " + s.toString());
        preferenceManager.setPreference(PreferenceManager.KEY_PAYMENT_ID, s);
        final PrettyDialog prettyDialog = new PrettyDialog(EmployeeReport.this);
        prettyDialog.setCancelable(false);
        prettyDialog
                .setTitle("Payment")
                .setMessage("Payment Successful" + "\n Payment id: " + s)
                .setIcon(R.drawable.check, R.color.primaryTextColor, null)
                .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        if (subPlan.equalsIgnoreCase("yearly")) {
                            subscriptionPlanUpdate();
                        } else {
                            EmployeeReportResponse();
                        }

                        prettyDialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("TAG", "error code " + String.valueOf(i) + " -- Payment failed " + s.toString());
        try {
            dialog.dismiss();
            final PrettyDialog prettyDialog = new PrettyDialog(EmployeeReport.this);
            prettyDialog.setCancelable(false);
            prettyDialog
                    .setTitle(s)
                    .setMessage("Payment Unsuccessful, Please try again")
                    .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                    .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            if (employeeMode.equalsIgnoreCase("multiple")) {
                                dialogForSub.dismiss();
                            }
                            prettyDialog.dismiss();
                            paymentFailed();
                        }
                    })
                    .show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }

    }

    @Override
    public void onChange(AnimCheckBox view, boolean checked) {
        int id = view.getId();

    }

    private void settingToolBar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EmployeeReport.this, MainActivity.class));
        finish();
    }
}
