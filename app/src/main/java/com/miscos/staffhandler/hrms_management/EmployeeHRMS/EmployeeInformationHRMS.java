package com.miscos.staffhandler.hrms_management.EmployeeHRMS;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.miscos.staffhandler.hrms_management.hrms_model.Salary_structure_adapter;
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
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class EmployeeInformationHRMS extends AppCompatActivity implements View.OnClickListener {


    EditText etPF,etPT,edtGST,edtTDS,edtESIC;
    TextView txtsalarynodata,txtleavenodata,txtamount,txtpfvalue,txtptvalue,txtgstvalue,txtesicvalue,txttdsvalue;
    ImageView salAdd,leaveAdd;
    Spinner spPF,spPT,spGST,spTDS,spESIC;
    ArrayList<salary_structure> salary_structures;
    Salary_structure_adapter salaryStructAdapter;
    ArrayList<LeaveStruct> leaveStructs;
    Leave_structure_adapter_for_emp leave_structure_adapter;
    String call_From="";

    Button saveConfig;
    private Dialog dialog;

    String[] amountType = new String[]{
            "Rs",
            "%"
    };
    String[] amountType1 = new String[]{
            "Rs",
            "%"
    };

    String[] Leaves = new String[]{
            "Full Day",
            "Half Day"};
    String[] Leave1 = new String[]{
            "01",
            "02",
            "03",
            "04",
            "05",
            "06",
            "07",
            "08",
            "09"
    };
    RecyclerView recycler_view_leave;
    RecyclerView recyclerView_salary_struct;
    RadioGroup rdPT,rdPF;
    String ptYes,ptNo,pfYes,pfNo,TDS,professional_tax,ESIC,GST,provident_found;
    RadioButton rbptYes,rbptNo,rbpfYes,rbpfNo,rdbtnTDSyes,rdBtnTDSno,rdbtnGSTyes,rdbtnGSTNo,rdbtnESICYes,rdbtnESICNo;
    LinearLayout layoutList,layoutListLeave,lvDefaultPT,lvDefaultPF,lenearSalary,lvdefaultGST,lvDefaultTDS,lvdefaultESIC;
    ProgressDialog progressDialog;
    ImageView imgback;
    String carryForwadable="N",leavetype="monthly";
    PreferenceManager preferenceManager;
    String strFrom,dateCheck,applicableDate,employeeId;
    View view;
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
            applicableDate = yy + "-" + mm + "-" + "01";
                addPolicyConfiguration();
                //  Log.d("TAG", "onDateSetChange:strTo " + selectDate.getText().toString());
        } catch (Exception ex) {
            Log.e("Date picker ", " Exception is " + ex.toString());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_and_configuration);
        // Inflate the layout for this fragment
        preferenceManager = new PreferenceManager(EmployeeInformationHRMS.this);
        //Progress Bar
        progressDialog = new ProgressDialog(EmployeeInformationHRMS.this);
        progressDialog = ProgressDialog.show(EmployeeInformationHRMS.this, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.dismiss();
        salAdd = findViewById(R.id.salAdd);
        salAdd.setVisibility(View.INVISIBLE);
        imgback=findViewById(R.id.imBack);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.txtleavetype).setVisibility(View.VISIBLE);
        findViewById(R.id.imgcarryFrwd).setVisibility(View.GONE);
        txtesicvalue=findViewById(R.id.txtESICvalue);
        txtesicvalue.setText("Value");
        txtgstvalue=findViewById(R.id.txtgstvalue);
        txtgstvalue.setText("Value");
        txtpfvalue=findViewById(R.id.txtPFvalue);
        txtpfvalue.setText("Value");
        txtptvalue=findViewById(R.id.txtPTvalue);
        txtptvalue.setText("Value");
        txttdsvalue=findViewById(R.id.txtTDSvalue);
        txttdsvalue.setText("Value");
        employeeId = getIntent().getStringExtra("employee_id");

        call_From=getIntent().getStringExtra("call_from");
        if(call_From.equalsIgnoreCase("addEmp")&&preferenceManager.getStringPreference(PreferenceManager.KEY_HRMS_CONFIGURATION).equalsIgnoreCase("Y"))
        {
            final PrettyDialog prettyDialog = new PrettyDialog(EmployeeInformationHRMS.this);
            prettyDialog.setCancelable(false);
            prettyDialog
                    .setTitle("SHLite")
                    .setMessage("Configure Salary & Leave settings for "+getIntent().getStringExtra("employee_name"))
                    .setIcon(R.drawable.success, R.color.white, null)
                    .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {

                            prettyDialog.dismiss();

                        }
                    })
                    .show();
        }
        else if(preferenceManager.getStringPreference(PreferenceManager.KEY_HRMS_CONFIGURATION).equalsIgnoreCase("N"))
        {
            final PrettyDialog prettyDialog = new PrettyDialog(EmployeeInformationHRMS.this);
            prettyDialog.setCancelable(false);
            prettyDialog
                    .setTitle("SHLite")
                    .setMessage("You have not configured HRMS Policy & Settings")
                    .setIcon(R.drawable.info, R.color.white, null)
                    .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {

                            prettyDialog.dismiss();
                            finish();

                        }
                    })
                    .show();
        }

        //  salEdit = findViewById(R.id.salEdit);
        leaveAdd = findViewById(R.id.leaveAdd);
        leaveAdd.setVisibility(View.GONE);
        saveConfig = findViewById(R.id.btn_hrms_config_save);
        TextView txtlabel=findViewById(R.id.txtlabel);
        txtlabel.setText( getIntent().getStringExtra("employee_name"));
        leaveStructs=new ArrayList<>();
        txtamount=findViewById(R.id.txtamount);
        txtamount.setVisibility(View.VISIBLE);
        leave_structure_adapter=new Leave_structure_adapter_for_emp(leaveStructs,this,call_From);
        recycler_view_leave=findViewById(R.id.recycler_view_leave_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler_view_leave.setLayoutManager(layoutManager);
        recycler_view_leave.setAdapter(leave_structure_adapter);
        recyclerView_salary_struct=findViewById(R.id.recycler_view_salary_struct);
       /* recyclerView_salary_struct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                showDialogAddSalaryStruct(i);
            }
        });*/
        txtsalarynodata=findViewById(R.id.txtsalrydata);
        txtleavenodata=findViewById(R.id.txtleavedata);
        txtleavenodata.setVisibility(View.GONE);
        txtsalarynodata.setVisibility(View.GONE);


        layoutList = findViewById(R.id.layout_list);
        lvDefaultPT = findViewById(R.id.lvDefaultPT);
        lvDefaultPF = findViewById(R.id.lvDefaultPF);

        rdPT = findViewById(R.id.rdPT);
        rdPF = findViewById(R.id.rdPF);
        rdbtnGSTNo=findViewById(R.id.rdGSTNo);
        rdbtnGSTyes=findViewById(R.id.rdGSTYes);
        rdbtnTDSyes=findViewById(R.id.rdTDSYes);
        rdBtnTDSno=findViewById(R.id.rdTDSNo);
        edtGST=findViewById(R.id.edtGST);
        edtTDS=findViewById(R.id.edtTDS);
        lvdefaultGST=findViewById(R.id.lvDefaultGST);
        lvDefaultTDS=findViewById(R.id.lvDefaultTDS);
        spPF = findViewById(R.id.pfTypes);
        spPT = findViewById(R.id.ptType);
        spGST=findViewById(R.id.GSTtypes);
        spTDS=findViewById(R.id.TDStypes);
        rbptYes = findViewById(R.id.rbPTY);
        rbptNo = findViewById(R.id.rbPTN);
        rbpfYes = findViewById(R.id.rbPFY);
        rbpfNo = findViewById(R.id.rbPFN);

        rdbtnESICNo=findViewById(R.id.rdESICNo);
        rdbtnESICYes=findViewById(R.id.rdESICYes);
        lvdefaultESIC=findViewById(R.id.lvDefaultESIC);
        spESIC=findViewById(R.id.ESICtypes);
        edtESIC=findViewById(R.id.edtESIC);
        etPF = findViewById(R.id.etPF);
        etPT = findViewById(R.id.etPT);
        spESIC.setEnabled(false);
        spTDS.setEnabled(false);
        spGST.setEnabled(false);
        spPT.setEnabled(false);
        spPF.setEnabled(false);
        rbptYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvDefaultPT.setVisibility(View.VISIBLE);
            }
        });
        rbptNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvDefaultPT.setVisibility(View.GONE);
            }
        });
        rbpfYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvDefaultPF.setVisibility(View.VISIBLE);
            }
        });
        rbpfNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvDefaultPF.setVisibility(View.GONE);
            }
        });

        rdbtnTDSyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvDefaultTDS.setVisibility(View.VISIBLE);
            }
        });
        rdBtnTDSno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvDefaultTDS.setVisibility(View.GONE);
            }
        });
        rdbtnGSTyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                lvdefaultGST.setVisibility(View.VISIBLE);
            }
        });
        rdbtnGSTNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvdefaultGST.setVisibility(View.GONE);
            }
        });
        rdbtnESICYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvdefaultESIC.setVisibility(View.VISIBLE);
            }
        });
        rdbtnESICNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvdefaultESIC.setVisibility(View.GONE);
            }
        });
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                EmployeeInformationHRMS.this,R.layout.layout_spinner,amountType );

        spinnerArrayAdapter.setDropDownViewResource(R.layout.layout_spinner);

        spESIC.setAdapter(spinnerArrayAdapter);
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
                EmployeeInformationHRMS.this,R.layout.layout_spinner,amountType );

        spinnerArrayAdapter2.setDropDownViewResource(R.layout.layout_spinner);

        spPF.setAdapter(spinnerArrayAdapter2);

        salary_structures=new ArrayList<>();

        salary_structures=new ArrayList<>();
        salaryStructAdapter=new Salary_structure_adapter(salary_structures,this,"info");
        LinearLayoutManager layoutManager2=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView_salary_struct.setLayoutManager(layoutManager2);
        recyclerView_salary_struct.setAdapter(salaryStructAdapter);


        ArrayAdapter<String> spinnerArrayAdapter3= new ArrayAdapter<String>(
                EmployeeInformationHRMS.this,R.layout.layout_spinner,amountType1 );

        spinnerArrayAdapter3.setDropDownViewResource(R.layout.layout_spinner);
        spPT.setAdapter(spinnerArrayAdapter3);

        spGST.setAdapter(spinnerArrayAdapter3);
        spTDS.setAdapter(spinnerArrayAdapter2);
        saveConfig.setOnClickListener(this);
        salAdd.setOnClickListener(this);
        leaveAdd.setOnClickListener(this);

        fetchPolicyConfiguration();

    }

    public void showDialogAddSalaryStruct(int index)
    {
        dialog = new Dialog(EmployeeInformationHRMS.this);
// Include dialog.xml file
        dialog.setContentView(R.layout.add_salary_struc);
        dialog.show();
        //dialog.setCancelable(false);
        Button buttonSubmit = (Button) dialog.findViewById(R.id.btnaddsalaryStruct);
        Button btncancel = (Button) dialog.findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        EditText edtsal = (EditText) dialog.findViewById(R.id.etTitle);
        EditText edtamount=dialog.findViewById(R.id.edtamount);
        edtamount.setVisibility(View.VISIBLE);
        edtsal.setText(salary_structures.get(index).getStruct_name());
        edtsal.setEnabled(false);
        edtamount.setText(salary_structures.get(index).getAmount());

        buttonSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(edtamount.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(EmployeeInformationHRMS.this, "Please enter valid amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                    /*salary_structures.remove(index);
                salary_structures
                    salary_structures.add(index,edtsal.getText().toString()+" = "+edtamount.getText().toString()+"/-");
                    salaryStructAdapter.notifyDataSetChanged();
                    Toast.makeText(EmployeeInformationHRMS.this, "Updated successfully", Toast.LENGTH_SHORT).show();
*/

                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int selectPT = rdPT.getCheckedRadioButtonId();

        switch (selectPT) {
            case R.id.rbPTY:
                ptYes = "Y";
                lvDefaultPT.setVisibility(View.VISIBLE);
                break;
            case R.id.rbPTN:
                ptNo = "N";
                break;
        }
        int selectPF = rdPF.getCheckedRadioButtonId();

        switch (selectPF) {
            case R.id.rbPFY:
                pfYes = "Y";
                lvDefaultPF.setVisibility(View.VISIBLE);
                break;
            case R.id.rbPFN:
                pfNo = "N";
                break;
        }
        switch (v.getId()){

            case R.id.salAdd:

            showDialogAddSalaryStruct(-1);

                break;
            case R.id.leaveAdd:

                break;
            case R.id.btn_hrms_config_save:
                boolean flag=false;
              for(salary_structure salaryStructItem:salary_structures)
              {
                  if(!salaryStructItem.getAmount().equalsIgnoreCase("0"))
                  {
                      flag=true;
                      break;
                  }
              }
              if(!flag)
              {

              }
                final PrettyDialog dialog2=    new PrettyDialog(EmployeeInformationHRMS.this);
                dialog2.setTitle("Date of Application")
                        .setMessage("Please tell us when these changes will be effective from?")
                        .setIcon(R.drawable.info,R.color.primaryTextColor,null)
                        .addButton("Use current date", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick()
                            {
                                long now = System.currentTimeMillis() - 1000;
                                final Calendar calendar = Calendar.getInstance();
                                int year = calendar.get(Calendar.YEAR);
                                int month = calendar.get(Calendar.MONTH);
                                int day = calendar.get(Calendar.DAY_OF_MONTH);
                                calendar.set(Calendar.DAY_OF_MONTH, day);
                                applicableDate=year + "-" + (month+1) + "-" + "01";
                                addPolicyConfiguration();
                                dialog2.dismiss();
                            }
                        })
                        .addButton("Choose date from calender", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                dialog2.dismiss();
                                long now = System.currentTimeMillis() - 1000;
                                final Calendar calendar = Calendar.getInstance();
                                int year = calendar.get(Calendar.YEAR);
                                int month = calendar.get(Calendar.MONTH);
                                int day = calendar.get(Calendar.DAY_OF_MONTH);
                                calendar.set(Calendar.DAY_OF_MONTH, day);
                                calendar.set(Calendar.DATE, +1);
                                DatePickerDialog datePickerDialog = new DatePickerDialog(EmployeeInformationHRMS.this, AlertDialog.THEME_HOLO_LIGHT,
                                        myDateSetListener, year, month, day);
                                ((ViewGroup) datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
                                datePickerDialog.getDatePicker().setMinDate(now);
                                datePickerDialog.show();
                            }
                        })
                        .show();

                //addPolicyConfiguration();
                break;

        }
    }


    public void  fetchPolicyConfiguration()
    {
        progressDialog.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.EMPLOYEE_HRMS_CONFIG, response -> {
            progressDialog.dismiss();
            try {

                Log.d("response",response);
                JSONObject jsonObject = new JSONObject(response);
                int error_code = jsonObject.getInt("error_code");
                String msg = jsonObject.getString("message");

                if (error_code == 104)
                {
                    Gson gson=new Gson();
                    EmployeeConfigData configData=gson.fromJson(response,EmployeeConfigData.class);
                    if(!configData.getData().getSalaryStruct().equalsIgnoreCase(""))
                    {
                        String[] strings=configData.getData().getSalaryStruct().split(",");
                        for(int i=0;i<strings.length;i++)
                        {
                            salary_structure salary_structure=new salary_structure();
                            String ss=strings[i].split("_")[0];
                            salary_structure.setIndex(ss);
                            salary_structure.setStruct_name(strings[i].split("_")[1].split("@")[0]);
                            salary_structure.setAmount(strings[i].split("_")[1].split("@")[1]);
                            salary_structures.add(salary_structure);
                        }
                        salaryStructAdapter.notifyDataSetChanged();
                        txtsalarynodata.setVisibility(View.GONE);
                    }
                    else
                    {
                        txtsalarynodata.setVisibility(View.VISIBLE);
                    }

                    if(configData.getData().getLeaveStruct().size()>0)
                    {
                        leaveStructs.addAll(configData.getData().getLeaveStruct());
                        leave_structure_adapter.notifyDataSetChanged();
                        txtleavenodata.setVisibility(View.GONE);
                    }
                    else
                    {
                        txtleavenodata.setVisibility(View.VISIBLE);

                    }
                    if(configData.getData().getSettingDeduction()!=null)
                    {


                        if(call_From.equalsIgnoreCase("addEmp"))
                        {  professional_tax=configData.getData().getSettingDeduction().getProfessionalTax().getEmployerAssign();
                            setdata(professional_tax,"@",spPT,rbptYes,rbptNo,lvDefaultPT,etPT);
                        }
                        else
                        {
                            professional_tax=configData.getData().getSettingDeduction().getProfessionalTax().getEmployeeHaving();
                            setdata(professional_tax,"=",spPT,rbptYes,rbptNo,lvDefaultPT,etPT);

                        }
                        if(call_From.equalsIgnoreCase("addEmp"))
                        {   provident_found=configData.getData().getSettingDeduction().getProvidentFound().getEmployerAssign();
                            setdata(provident_found,"@",spPF,rbpfYes,rbpfNo,lvDefaultPF,etPF);
                        }
                        else
                        {
                            provident_found=configData.getData().getSettingDeduction().getProvidentFound().getEmployeeHaving();
                            setdata(provident_found,"=",spPF,rbpfYes,rbpfNo,lvDefaultPF,etPF);

                        }


                        if(call_From.equalsIgnoreCase("addEmp"))
                        {   GST =configData.getData().getSettingDeduction().getGst().getEmployerAssign();
                            setdata(GST,"@",spGST,rdbtnGSTyes,rdbtnGSTNo,lvdefaultGST,edtGST);
                        }
                        else
                        {
                            GST =configData.getData().getSettingDeduction().getGst().getEmployeeHaving();
                            setdata(GST,"=",spGST,rdbtnGSTyes,rdbtnGSTNo,lvdefaultGST,edtGST);

                        }


                        if(call_From.equalsIgnoreCase("addEmp"))
                        { TDS=configData.getData().getSettingDeduction().getTDS().getEmployerAssign();
                            setdata(TDS,"@",spTDS,rdbtnTDSyes,rdBtnTDSno,lvDefaultTDS,edtTDS);
                        }
                        else
                        {
                            TDS=configData.getData().getSettingDeduction().getTDS().getEmployeeHaving();
                            setdata(TDS,"=",spTDS,rdbtnTDSyes,rdBtnTDSno,lvDefaultTDS,edtTDS);

                        }
                        if(call_From.equalsIgnoreCase("addEmp"))
                        { ESIC=configData.getData().getSettingDeduction().getEsic().getEmployerAssign();
                            setdata(ESIC,"@",spESIC,rdbtnESICYes,rdbtnESICNo,lvdefaultESIC,edtESIC);
                        }
                        else
                        {
                            ESIC=configData.getData().getSettingDeduction().getEsic().getEmployeeHaving();
                            setdata(ESIC,"=",spESIC,rdbtnESICYes,rdbtnESICNo,lvdefaultESIC,edtESIC);

                        }

                    }



                } else {

                    txtleavenodata.setVisibility(View.VISIBLE);
                    txtsalarynodata.setVisibility(View.VISIBLE);
                   /* final PrettyDialog prettyDialog = new PrettyDialog(PolicyAndConfiguration.this);
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
                            .show();*/

                    progressDialog.dismiss();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                final Dialog dialog1 = new Dialog(EmployeeInformationHRMS.this);
                // Include dialog.xml file
                dialog1.setContentView(R.layout.dialog_exit);
                dialog1.show();
                TextView heading1 = dialog1.findViewById(R.id.tv_quit_learning);
                TextView description1 = dialog1.findViewById(R.id.tv_description);
                Button btnNo1 = (Button) dialog1.findViewById(R.id.btn_no);
                Button btnYes1 = (Button) dialog1.findViewById(R.id.btn_yes);
                heading1.setText("Error");
                description1.setText("An unfortunate error occurred while parsing data,please try again.");
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
                final PrettyDialog prettyDialog = new PrettyDialog(EmployeeInformationHRMS.this);
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
                params.put("mode", "fetch_employee_hrms_information");
                params.put("employee_id",employeeId);
                params.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
                Log.e("hrms_config_params", "" + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(EmployeeInformationHRMS.this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void  addPolicyConfiguration()
    {
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
                    final PrettyDialog prettyDialog = new PrettyDialog(EmployeeInformationHRMS.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("HRMS Config")
                            .setMessage(msg)
                            .setIcon(R.drawable.success, R.color.white, null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                    startActivity(new Intent(EmployeeInformationHRMS.this,EmployerZone.class));
                                    finish();
                                }
                            })
                            .show();

                    // Toast.makeText(PolicyAndConfiguration.this, sal_name_1, Toast.LENGTH_SHORT).show();
                     /*   y_net_payable = subscribeData.getString("created_date");
                        y_net_payable = subscribeData.getString("modified_date");*/

                } else {
                    final PrettyDialog prettyDialog = new PrettyDialog(EmployeeInformationHRMS.this);
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
                final Dialog dialog1 = new Dialog(EmployeeInformationHRMS.this);
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
                final PrettyDialog prettyDialog = new PrettyDialog(EmployeeInformationHRMS.this);
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
                params.put("salary_struct", getSalaryStruct());
                params.put("leave_struct", getLeaveStruct());
                params.put("setting_struct", getSettings());
                params.put("applicable_from_date",applicableDate);
                params.put("employee_id", employeeId);
                params.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
                Log.e("hrms_config_params", "" + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(EmployeeInformationHRMS.this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
    String getSalaryStruct()
    {
        String sal="";

        for(int i=0;i<salary_structures.size();i++)
        {
            sal=sal+salary_structures.get(i).getIndex()+"_"+salary_structures.get(i).getAmount()+",";
        }
      /*  for(int i=salary_structures.size();i<10;i++)
        {
            sal=sal+(i+1)+"_0"+",";
        }*/
        return sal;
    }
    String getLeaveStruct()
    {
        String sal="";

        for(int i=0;i<leave_structure_adapter.leaveStructs.size();i++)
        {
            if(leave_structure_adapter.leaveStructs.get(i).getEmployeeHavingNoOfLeaves().isEmpty()||leave_structure_adapter.leaveStructs.get(i).getEmployeeHavingNoOfLeaves().equalsIgnoreCase("0"))
            sal=sal+leave_structure_adapter.leaveStructs.get(i).getIndex()+"_"+leave_structure_adapter.leaveStructs.get(i).getEmployerAssignNoOfLeaves()+",";
            else
                sal=sal+leave_structure_adapter.leaveStructs.get(i).getIndex()+"_"+leave_structure_adapter.leaveStructs.get(i).getEmployeeHavingNoOfLeaves()+",";

        }
       /* for(int i=leave_structure_adapter.leaveStructs.size();i<10;i++)
        {
            sal=sal+(i+1)+"_"+"0,";
        }*/
        return sal;
    }
    String getSettings()
    {
         String setting="";
         if(rbpfYes.isChecked())
         {
             setting=setting+"provident_found-"+spPF.getSelectedItem().toString()+"="+etPF.getText().toString();
         }
         else
             {
                 setting=setting+"provident_found-"+spPF.getSelectedItem().toString()+"=0";
         }
        if(rbptYes.isChecked())
        {
            setting=setting+",professional_tax-"+spPT.getSelectedItem().toString()+"="+etPT.getText().toString();
        }
        else
        {
            setting=setting+",professional_tax-"+spPT.getSelectedItem().toString()+"=0";
        }
        if(rdbtnGSTyes.isChecked())
        {
            setting=setting+",GST-"+spGST.getSelectedItem().toString()+"="+edtGST.getText().toString();
        }
        else
        {
            setting=setting+",GST-"+spGST.getSelectedItem().toString()+"=0";
        }
        if(rdbtnTDSyes.isChecked())
        {
            setting=setting+",TDS-"+spTDS.getSelectedItem().toString()+"="+edtTDS.getText().toString();
        }
        else
        {
            setting=setting+",TDS-"+spTDS.getSelectedItem().toString()+"=0";;
        }

        if(rdbtnESICYes.isChecked())
        {
            setting=setting+",ESIC-"+spESIC.getSelectedItem().toString()+"="+edtESIC.getText().toString();
        }
        else
        {
            setting=setting+",ESIC-"+spESIC.getSelectedItem().toString()+"=0";
        }

        return setting;
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    void setdata(String str,String sep,Spinner sp,RadioButton rdbtnyes,RadioButton rdbtnno,LinearLayout lvdefault,EditText edt)
    {
        if (!str.isEmpty())
        {
            rdbtnyes.setChecked(true);
            String pt=str.split(sep)[1];
            String amount="";
            if(pt.contains("("))
            {
                amount=pt.substring(1,pt.length()-1);
            }
            else
            {
                amount=pt;
            }
            if(amount.equals("0"))
            {
                rdbtnno.setChecked(true);
                lvdefault.setVisibility(View.GONE);
            }
            else
            {
                lvdefault.setVisibility(View.VISIBLE);
            }


            if (str.contains("Rs"))
            {
                edt.setText(amount);

                sp.setVisibility(View.VISIBLE);
                sp.setSelection(0);

            }else if(str.contains("%"))
            {
                edt.setText(amount);
                sp.setVisibility(View.VISIBLE);
                sp.setSelection(1);
            }

        }
        else {
            rdbtnno.setChecked(true);
        }
    }
}