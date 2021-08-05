package com.miscos.staffhandler.hrms_management;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miscos.staffhandler.hrms_management.hrms_model.salary_structure;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.Urls.App_Urls;
import com.miscos.staffhandler.activities.EmployerZone;
import com.miscos.staffhandler.employee.helper.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeInformation extends AppCompatActivity implements View.OnClickListener{

    RadioGroup rdPT,rdPF;
    String basic,HRA,ptYes,ptNo,pfYes,pfNo,TDS,professional_tax,ESIC,GST,provident_found,sal_name_1,sal_name_2,sal_name_3,sal_name_4,sal_name_5,sal_name_6,sal_name_7,sal_name_8,sal_name_9,sal_name_10
            ,leave_name_1,leave_name_2,leave_name_3,leave_name_4,leave_name_5,leave_name_6,leave_name_7,leave_name_8,leave_name_9,leave_name_10;
    RadioButton rbptYes,rbptNo,rbpfYes,rbpfNo;
    ArrayList<salary_structure> salary_structures = new ArrayList<>();
    LinearLayout layoutList,layoutListLeave,lvDefaultPT,lvDefaultPF,lenearSalary;
    ProgressDialog progressDialog;
    PreferenceManager preferenceManager;
    Spinner spLeave,spPaidL,spPF,spPT;
    EditText etBasic,etHra;
    String strFrom,dateCheck,applicableDate,employeeId;
    Button btnEmpSave;
    String[] amountType = new String[]{
            "Rs",
            "%"
    };
    String[] amountType1 = new String[]{
            "Rs",
            "%"
    };
    String[] Leaves = new String[]{
            "Half",
            "01",
            "02",
            "03",
            "04",
            "05",
            "06",
            "07",
            "08",
            "09",
            "10"
    };
    String[] Leave = new String[]{
            "01",
            "02",
            "03",
            "04",
            "05",
            "06",
            "07",
            "08",
            "09",
            "10"
    };
    ImageView imgBack;
    private String dd = "00", mm = "00", yy = "0000";
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_employe_information);
        // Inflate the layout for this fragment

        preferenceManager = new PreferenceManager(this);
        //Progress Bar
        progressDialog = new ProgressDialog(EmployeInformation.this);
        progressDialog = ProgressDialog.show(EmployeInformation.this, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.dismiss();
        spLeave = findViewById(R.id.spLeave);
        spPaidL = findViewById(R.id.spPaidL);
        spPF = findViewById(R.id.pfType);
        spPT = findViewById(R.id.tdsType);
        etBasic = findViewById(R.id.etBasic);
        etHra = findViewById(R.id.tvHRA);
        imgBack=findViewById(R.id.imBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnEmpSave = findViewById(R.id.btnEmpSave);
        employeeId = getIntent().getStringExtra("employee_id");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                EmployeInformation.this,R.layout.layout_spinner,Leaves );

        spinnerArrayAdapter.setDropDownViewResource(R.layout.layout_spinner);

        spLeave.setAdapter(spinnerArrayAdapter);

        ArrayAdapter<String> spinnerArrayAdapte1r = new ArrayAdapter<String>(
                EmployeInformation.this,R.layout.layout_spinner,Leave );

        spinnerArrayAdapte1r.setDropDownViewResource(R.layout.layout_spinner);

        spPaidL.setAdapter(spinnerArrayAdapte1r);

        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
                EmployeInformation.this,R.layout.layout_spinner,amountType );

        spinnerArrayAdapter2.setDropDownViewResource(R.layout.layout_spinner);

        spPF.setAdapter(spinnerArrayAdapter2);
        spPF.setEnabled(false);

        ArrayAdapter<String> spinnerArrayAdapter3= new ArrayAdapter<String>(
                EmployeInformation.this,R.layout.layout_spinner,amountType );

        spinnerArrayAdapter3.setDropDownViewResource(R.layout.layout_spinner);

        spPT.setAdapter(spinnerArrayAdapter3);
        spPT.setEnabled(false);
        btnEmpSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis() - 1000;
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.DATE, +1);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EmployeInformation.this, myDateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 24));
                datePickerDialog.getDatePicker().setMaxDate(now);
                datePickerDialog.show();
                dateCheck = "From";
            }
        });

        fetchPolicyConfiguration();
    }

    @Override
    public void onClick(View v) {

    }
    private void onDateSetChange(DatePicker datePicker, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTimeInMillis(0);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
            Log.d(" PICK TIME ", " " + cal.getTime());
            cal.set(year, month, day);
            cal.clear(Calendar.HOUR_OF_DAY);
            cal.clear(Calendar.AM_PM);
            cal.clear(Calendar.MINUTE);
            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.MILLISECOND);
            Log.e("Date", " 1");
            Date chosenDate = cal.getTime();
            Log.e("Date", " 2");
            String strDateFrom = df.format(chosenDate);
            Log.e("Date", " 3");
            if (dateCheck.equalsIgnoreCase("From")) {
                applicableDate = yy + "-" + mm + "-" + "01";
                strFrom = strDateFrom;
                addPolicyConfiguration(applicableDate);
              //  Log.d("TAG", "onDateSetChange:strTo " + selectDate.getText().toString());
            }
        } catch (Exception ex) {
            Log.e("Date picker ", " Exception is " + ex.toString());
        }
    }

    private void allSpinner(){
        final List<String> bathrooms = new ArrayList<String>();
        bathrooms.add(0, "Select Leave");
        bathrooms.add("Half");
        bathrooms.add("1");
        bathrooms.add("2");
        bathrooms.add("3");
        bathrooms.add("4");
        bathrooms.add("5");

        ArrayAdapter<String> bathroomsAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,
                bathrooms) {
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
        bathroomsAdapter.setDropDownViewResource(R.layout.spinner_item);
        spLeave.setAdapter(bathroomsAdapter);
        spLeave.setSelection(0);
        spLeave.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void  addPolicyConfiguration(final String applicabledate){
        progressDialog.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.EMPLOYEE_HRMS_CONFIG, response -> {
            progressDialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(response);
                Log.e("hrms_config_response", " " + response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                int error_code = jsonObject.getInt("error_code");
                String msg = jsonObject.getString("message");

                if (error_code == 104) {
                    final PrettyDialog prettyDialog = new PrettyDialog(EmployeInformation.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("HRMS Config")
                            .setMessage(msg)
                            .setIcon(R.drawable.success, R.color.white, null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            })
                            .show();

                    // Toast.makeText(PolicyAndConfiguration.this, sal_name_1, Toast.LENGTH_SHORT).show();
                     /*   y_net_payable = subscribeData.getString("created_date");
                        y_net_payable = subscribeData.getString("modified_date");*/

                } else {
                    final PrettyDialog prettyDialog = new PrettyDialog(EmployeInformation.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Employee Information")
                            .setMessage(msg)
                            .setIcon(R.drawable.cross, R.color.white, null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            })
                            .show();

                    progressDialog.dismiss();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                final Dialog dialog1 = new Dialog(EmployeInformation.this);
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
                final PrettyDialog prettyDialog = new PrettyDialog(EmployeInformation.this);
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
                params.put("mode", "add_employee_hrms_information");
                params.put("salary_struct", "1_8450,2_1500");
                params.put("leave_struct", "1_02,2_Half");
                params.put("setting_struct", "provident_found-rs=200,TDS-%=02");
                params.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
                params.put("employee_id", employeeId);
                params.put("applicable_from_date", applicabledate);
                Log.e("hrms_config_params", "" + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(EmployeInformation.this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
    public void  fetchPolicyConfiguration(){
        progressDialog.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.EMPLOYEE_HRMS_CONFIG, response -> {
            progressDialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(response);
                Log.e("hrms_config_response", " " + response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                int error_code = jsonObject.getInt("error_code");
                String msg = jsonObject.getString("message");

                if (error_code == 104) {
                    lenearSalary.setVisibility(View.VISIBLE);
                    JSONObject jsonObject1 =  jsonObject.getJSONObject("Configuration_structure_list");
                    sal_name_1 = jsonObject1.getString("1_sal_name");
                    sal_name_2 = jsonObject1.getString("2_sal_name");
                    sal_name_3 = jsonObject1.getString("3_sal_name");
                    sal_name_4 = jsonObject1.getString("4_sal_name");
                    sal_name_5 = jsonObject1.getString("5_sal_name");
                    sal_name_6 = jsonObject1.getString("6_sal_name");
                    sal_name_7 = jsonObject1.getString("7_sal_name");
                    sal_name_8 = jsonObject1.getString("8_sal_name");
                    sal_name_9 = jsonObject1.getString("9_sal_name");
                    sal_name_10 = jsonObject1.getString("10_sal_name");
                    leave_name_1 = jsonObject1.getString("1_leave_name");
                    leave_name_2 = jsonObject1.getString("2_leave_name");
                    leave_name_3 = jsonObject1.getString("3_leave_name");
                    leave_name_4 = jsonObject1.getString("4_leave_name");
                    leave_name_5 = jsonObject1.getString("5_leave_name");
                    leave_name_6 = jsonObject1.getString("6_leave_name");
                    leave_name_7 = jsonObject1.getString("7_leave_name");
                    leave_name_8 = jsonObject1.getString("8_leave_name");
                    leave_name_9 = jsonObject1.getString("9_leave_name");
                    leave_name_10 = jsonObject1.getString("10_leave_name");
                    provident_found = jsonObject1.getString("provident_found");
                    professional_tax = jsonObject1.getString("professional_tax");
                    ESIC = jsonObject1.getString("ESIC");
                    GST = jsonObject1.getString("GST");
                    TDS = jsonObject1.getString("TDS");
                    lenearSalary.setEnabled(false);
                  /*  etSal1.setVisibility(View.VISIBLE);
                    etSal2.setVisibility(View.VISIBLE);
                    etSal3.setVisibility(View.VISIBLE);
                    etSal1.setText(sal_name_1);
                    etSal2.setText(sal_name_2);
                    etSal3.setText(sal_name_3);

                    if (!professional_tax.isEmpty()){
                        rbptYes.setChecked(true);
                        lvDefaultPT.setVisibility(View.VISIBLE);
                        etPT.setText(professional_tax);
                    }else {
                        rbptNo.setChecked(true);
                    }
                    if (!provident_found.isEmpty()){
                        rbpfYes.setChecked(true);
                        lvDefaultPF.setVisibility(View.VISIBLE);
                        if (provident_found.equalsIgnoreCase("%@(02)"))
                        {
                            etPF.setText("02 %");
                        }

                    }else {
                        rbpfNo.setChecked(true);
                    }*/
                    // Toast.makeText(PolicyAndConfiguration.this, sal_name_1, Toast.LENGTH_SHORT).show();
                     /*   y_net_payable = subscribeData.getString("created_date");
                        y_net_payable = subscribeData.getString("modified_date");*/

                } else {
                    final PrettyDialog prettyDialog = new PrettyDialog(EmployeInformation.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("HRMS Config")
                            .setMessage(msg)
                            .setIcon(R.drawable.cross, R.color.white, null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            })
                            .show();

                    progressDialog.dismiss();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                final Dialog dialog1 = new Dialog(EmployeInformation.this);
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
                final PrettyDialog prettyDialog = new PrettyDialog(EmployeInformation.this);
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
                params.put("mode", "fetch");
               /* params.put("salary_struct", "1_Basic,2_TA,3_HRA");
                params.put("leave_struct", "1_Paid=F@2$1,2_Sick=N@3$0.5");
                params.put("setting_struct", "professional_tax=Rs@(400),provident_found=%@(02)");*/
                params.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
                Log.e("hrms_config_params", "" + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(EmployeInformation.this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EmployeInformation.this, EmployerZone.class));
        finish();
    }
}