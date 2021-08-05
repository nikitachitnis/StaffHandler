package com.miscos.staffhandler.hrms_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.miscos.staffhandler.hrms_management.hrms_model.LeaveStruct;
import com.miscos.staffhandler.hrms_management.hrms_model.Leave_structure_adapter;
import com.miscos.staffhandler.hrms_management.hrms_model.PolicyResponse;
import com.miscos.staffhandler.hrms_management.hrms_model.Salary_structure_adapter;
import com.miscos.staffhandler.hrms_management.hrms_model.salary_structure;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.Urls.App_Urls;
import com.miscos.staffhandler.activities.EmployerZone;
import com.miscos.staffhandler.employee.helper.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PolicyAndConfiguration extends AppCompatActivity implements View.OnClickListener {


    EditText etPF,etPT,edtGST,edtTDS,edtESIC;
    TextView txtsalarynodata,txtleavenodata;
    ImageView salAdd,leaveAdd;
    Spinner spPF,spPT,spGST,spTDS,spESIC;
    public ArrayList<salary_structure> salary_structures,temp_sal_structures;
    Salary_structure_adapter salaryStructAdapter;
    public ArrayList<LeaveStruct> leaveStructs,temp_leave_structures;
    Leave_structure_adapter leave_structure_adapter;

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
    String carryForwadable="N",leavetype="monthly";
    PreferenceManager preferenceManager;
    View view;
    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_and_configuration);
        // Inflate the layout for this fragment
        preferenceManager = new PreferenceManager(PolicyAndConfiguration.this);
        //Progress Bar
        progressDialog = new ProgressDialog(PolicyAndConfiguration.this);
        progressDialog = ProgressDialog.show(PolicyAndConfiguration.this, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.dismiss();
        imgBack=findViewById(R.id.imBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        salAdd = findViewById(R.id.salAdd);
        salAdd.setVisibility(View.VISIBLE);
        //  salEdit = findViewById(R.id.salEdit);
        leaveAdd = findViewById(R.id.leaveAdd);
        saveConfig = findViewById(R.id.btn_hrms_config_save);
        leaveStructs=new ArrayList<>();
        temp_leave_structures=new ArrayList<>();
        leave_structure_adapter=new Leave_structure_adapter(temp_leave_structures,this);
        recycler_view_leave=findViewById(R.id.recycler_view_leave_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler_view_leave.setLayoutManager(layoutManager);
        recycler_view_leave.setAdapter(leave_structure_adapter);
        recyclerView_salary_struct=findViewById(R.id.recycler_view_salary_struct);
        salary_structures=new ArrayList<>();
        temp_sal_structures=new ArrayList<>();

        salaryStructAdapter=new Salary_structure_adapter(temp_sal_structures,this,"add");
        LinearLayoutManager layoutManager2=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView_salary_struct.setLayoutManager(layoutManager2);
        recyclerView_salary_struct.setAdapter(salaryStructAdapter);

        txtsalarynodata=findViewById(R.id.txtsalrydata);
        txtleavenodata=findViewById(R.id.txtleavedata);


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
                PolicyAndConfiguration.this,R.layout.layout_spinner,amountType );

        spinnerArrayAdapter.setDropDownViewResource(R.layout.layout_spinner);

        spESIC.setAdapter(spinnerArrayAdapter);
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
                PolicyAndConfiguration.this,R.layout.layout_spinner,amountType );

        spinnerArrayAdapter2.setDropDownViewResource(R.layout.layout_spinner);

        spPF.setAdapter(spinnerArrayAdapter2);


        ArrayAdapter<String> spinnerArrayAdapter3= new ArrayAdapter<String>(
                PolicyAndConfiguration.this,R.layout.layout_spinner,amountType1 );

        spinnerArrayAdapter3.setDropDownViewResource(R.layout.layout_spinner);
        spPT.setAdapter(spinnerArrayAdapter3);

        spGST.setAdapter(spinnerArrayAdapter3);
        spTDS.setAdapter(spinnerArrayAdapter2);
        saveConfig.setOnClickListener(this);
        salAdd.setOnClickListener(this);
        leaveAdd.setOnClickListener(this);

        fetchPolicyConfiguration();

    }

    //add employess in bulk
    public void showDialogAddSalaryStruct(int index)
    {
        dialog = new Dialog(PolicyAndConfiguration.this);
// Include dialog.xml file
        dialog.setContentView(R.layout.add_salary_struc);
        dialog.setCancelable(false);
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
        buttonSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(edtsal.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(PolicyAndConfiguration.this, "Please add title", Toast.LENGTH_SHORT).show();
                    return;
                }

                salary_structure salary_structure=new salary_structure();
                salary_structure.setStruct_name(edtsal.getText().toString());
                salary_structure.setStatus("active");
                 salary_structures.add(salary_structure);
                 temp_sal_structures.add(salary_structure);
                txtsalarynodata.setVisibility(View.GONE);
                    salaryStructAdapter.notifyDataSetChanged();
                    Toast.makeText(PolicyAndConfiguration.this, "Added", Toast.LENGTH_SHORT).show();
                if(salary_structures.size()==10)
                {
                    salAdd.setVisibility(View.GONE);

                }
                else
                {
                    salAdd.setVisibility(View.VISIBLE);

                }
                dialog.dismiss();
            }
        });
    }
    public void showDialogAddLeaveStruct()
    {
        Dialog dialog = new Dialog(PolicyAndConfiguration.this);
        dialog.setContentView(R.layout.addleave);
        dialog.setCancelable(false);
        EditText edtleavename=dialog.findViewById(R.id.edtleavename);
        Spinner spinleaves=dialog.findViewById(R.id.spinNoOfleaves);
        Spinner spinday=dialog.findViewById(R.id.spindays);
        LinearLayout lv_resetdate=dialog.findViewById(R.id.lv_resetdate);
        RadioGroup rdgroup1,rdgroup2;
        ArrayList<String> strings=new ArrayList<>();
        strings.addAll(Arrays.asList(Leave1));
        for(int i=10;i<=90;i+=10)
        {
            strings.add(i+"");
        }
        ArrayAdapter<String> spinnerArrayAdapter3= new ArrayAdapter<String>(
                PolicyAndConfiguration.this,R.layout.layout_spinner,strings);

        spinnerArrayAdapter3.setDropDownViewResource(R.layout.layout_spinner);
        spinleaves.setAdapter(spinnerArrayAdapter3);
        ArrayAdapter<String> spinadapter= new ArrayAdapter<String>(
                PolicyAndConfiguration.this,R.layout.layout_spinner,Leaves);
        EditText editText=dialog.findViewById(R.id.edtresetdate);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(PolicyAndConfiguration.this,
                        new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                            {
                                editText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        spinadapter.setDropDownViewResource(R.layout.layout_spinner);
        spinday.setAdapter(spinadapter);
        rdgroup1=dialog.findViewById(R.id.rdgroupCF);
        rdgroup2=dialog.findViewById(R.id.rdgroupleaveType);
        Button btnsubmit,btncancel;
        btnsubmit=dialog.findViewById(R.id.btnadd);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(edtleavename.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(PolicyAndConfiguration.this, "Leave name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }


                    LeaveStruct leaveStruct = new LeaveStruct();
                    leaveStruct.setLeaveName(edtleavename.getText().toString());
                    leaveStruct.setCarryForwardable(carryForwadable);
                    leaveStruct.setLeaveType(leavetype);
                    leaveStruct.setLeaveStatus("active");
                    leaveStruct.setNoOfLeaves(spinleaves.getSelectedItem().toString());
                    if (spinday.getSelectedItem().toString().equalsIgnoreCase("full day"))
                        leaveStruct.setType("1");
                    else
                        leaveStruct.setType("0.5");
                    leaveStruct.setReset_date(editText.getText().toString());
                    leaveStructs.add(leaveStruct);

                    getLeaveStructlist();

                    leave_structure_adapter.notifyDataSetChanged();
                    txtleavenodata.setVisibility(View.GONE);
                    Toast.makeText(PolicyAndConfiguration.this, "Added", Toast.LENGTH_SHORT).show();
                if(leaveStructs.size()==10)
                {
                    leaveAdd.setVisibility(View.GONE);

                }
                else
                {
                    leaveAdd.setVisibility(View.VISIBLE);

                }

                dialog.dismiss();
            }
        });
        btncancel=dialog.findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        rdgroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                switch (radioGroup.getCheckedRadioButtonId())
                {
                    case R.id.rdbtnYes:
                        carryForwadable="F";
                        lv_resetdate.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rdbtnno:
                        carryForwadable="N";
                        lv_resetdate.setVisibility(View.GONE);
                        break;
                }
            }
        });
        rdgroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                switch (radioGroup.getCheckedRadioButtonId())
                {
                    case R.id.rdbtnmonthly:
                        leavetype="Monthly";
                        break;
                    case R.id.rdbtnasrequired:
                        leavetype="As-required";
                        break;
                }
            }
        });



        dialog.show();





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
            showDialogAddLeaveStruct();

                break;
            case R.id.btn_hrms_config_save:
              if(salary_structures.size()==0)
              {
                  Toast.makeText(this, "Please add salary structure", Toast.LENGTH_SHORT).show();
                  return;
              }if(leaveStructs.size()==0)
            {
                Toast.makeText(this, "Please add leave structure", Toast.LENGTH_SHORT).show();
                return;
            }

                addPolicyConfiguration();
                break;

        }
    }


    public void  fetchPolicyConfiguration()
    {
        progressDialog.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.EMPLOYEE_HRMS_CONFIG, response ->
        {
            progressDialog.dismiss();
            try {

                JSONObject jsonObject = new JSONObject(response);
                int error_code = jsonObject.getInt("error_code");
                String msg = jsonObject.getString("message");

                if (error_code == 104)
                {
                    Gson gson=new Gson();
                    PolicyResponse configData=gson.fromJson(response,PolicyResponse.class);
                    if(configData.getConfigData().getSalaryStruct().size()>0)
                    {
                        salary_structures.addAll(configData.getConfigData().getSalaryStruct());

                       getsalStruct();

                        txtsalarynodata.setVisibility(View.GONE);
                    }
                    else
                    {
                        txtsalarynodata.setVisibility(View.VISIBLE);
                    }

                    if(configData.getConfigData().getLeaveStruct().size()>0)
                    {
                        leaveStructs.addAll(configData.getConfigData().getLeaveStruct());
                        if(leaveStructs.size()==10)
                        {
                            leaveAdd.setVisibility(View.GONE);

                        }
                        else
                        {
                            leaveAdd.setVisibility(View.VISIBLE);

                        }
                        getLeaveStructlist();

                        //leave_structure_adapter.notifyDataSetChanged();
                        txtleavenodata.setVisibility(View.GONE);
                    }
                    else
                    {
                        txtleavenodata.setVisibility(View.VISIBLE);

                    }
                    if(configData.getConfigData().getSettingDeduction()!=null)
                    {
                        professional_tax=configData.getConfigData().getSettingDeduction().getProfessionalTax();
                        if (!professional_tax.isEmpty())
                        {
                            rbptYes.setChecked(true);
                            lvDefaultPT.setVisibility(View.VISIBLE);
                            String pt=professional_tax.split("@")[1];
                            if(pt.substring(1,pt.length()-1).equalsIgnoreCase("0"))
                            {
                                rbptNo.setChecked(true);
                                lvDefaultPT.setVisibility(View.GONE);
                            }
                            if (professional_tax.contains("Rs"))
                            {

                                etPT.setText(pt.substring(1,pt.length()-1));
                                spPT.setVisibility(View.VISIBLE);
                                spPT.setSelection(0);

                            }else if(professional_tax.contains("%"))
                            {
                                etPT.setText(pt.substring(1,pt.length()-1));
                                spPT.setVisibility(View.VISIBLE);
                                spPT.setSelection(1);
                            }

                        }
                        else {
                            rbptNo.setChecked(true);
                        }
                        provident_found=configData.getConfigData().getSettingDeduction().getProvidentFound();
                        if (!provident_found.isEmpty())
                        {
                            rbpfYes.setChecked(true);
                            lvDefaultPF.setVisibility(View.VISIBLE);
                            String pt=provident_found.split("@")[1];
                            Log.d("pf",pt);

                            if(pt.substring(1,pt.length()-1).equalsIgnoreCase("0"))
                            {
                                rbpfNo.setChecked(true);
                                lvDefaultPF.setVisibility(View.GONE);
                            }
                            if (provident_found.contains("Rs"))
                            {
                                etPF.setText(pt.substring(1,pt.length()-1));
                                spPF.setVisibility(View.VISIBLE);
                                spPF.setSelection(0);

                            }else if(provident_found.contains("%"))
                            {
                                etPF.setText(pt.substring(1,pt.length()-1));
                                spPF.setVisibility(View.VISIBLE);
                                spPF.setSelection(1);
                            }

                        }
                        else {
                            rbpfNo.setChecked(true);
                        }
                        GST=configData.getConfigData().getSettingDeduction().getGST();
                        if (!GST.isEmpty())
                        {
                            rdbtnGSTyes.setChecked(true);
                            lvdefaultGST.setVisibility(View.VISIBLE);
                            String pt=GST.split("@")[1];
                            Log.d("gst",pt);
                            if(pt.substring(1,pt.length()-1).equalsIgnoreCase("0"))
                            {
                                rdbtnGSTNo.setChecked(true);
                                lvdefaultGST.setVisibility(View.GONE);
                            }
                            if (GST.contains("Rs"))
                            {
                                edtGST.setText(pt.substring(1,pt.length()-1));
                                spGST.setVisibility(View.VISIBLE);
                                spGST.setSelection(0);

                            }else if(GST.contains("%"))
                            {
                                edtGST.setText(pt.substring(1,pt.length()-1));
                                spGST.setVisibility(View.VISIBLE);
                                spGST.setSelection(1);
                            }

                        }
                        else {
                            rdbtnGSTNo.setChecked(true);
                        }
                        TDS=configData.getConfigData().getSettingDeduction().getTDS();
                        if (!TDS.isEmpty())
                        {
                            rdbtnTDSyes.setChecked(true);
                            lvDefaultTDS.setVisibility(View.VISIBLE);
                            String pt=TDS.split("@")[1];
                            if(pt.substring(1,pt.length()-1).equalsIgnoreCase("0"))
                            {
                                rdBtnTDSno.setChecked(true);
                                lvDefaultTDS.setVisibility(View.GONE);
                            }
                            if (TDS.contains("Rs"))
                            {

                                edtTDS.setText(pt.substring(1,pt.length()-1));
                                spTDS.setVisibility(View.VISIBLE);
                                spTDS.setSelection(0);

                            }else if(TDS.contains("%"))
                            {
                                edtTDS.setText(pt.substring(1,pt.length()-1));
                                spTDS.setVisibility(View.VISIBLE);
                                spTDS.setSelection(1);
                            }

                        }
                        else {
                            rdBtnTDSno.setChecked(true);
                        }
                        ESIC=configData.getConfigData().getSettingDeduction().getESIC();
                        if (!ESIC.isEmpty())
                        {
                            rdbtnESICYes.setChecked(true);
                            lvdefaultESIC.setVisibility(View.VISIBLE);
                            String pt=ESIC.split("@")[1];
                            if(pt.substring(1,pt.length()-1).equalsIgnoreCase("0"))
                            {
                                rdbtnESICNo.setChecked(true);
                                lvdefaultESIC.setVisibility(View.GONE);
                            }
                            if (ESIC.contains("Rs"))
                            {

                                edtESIC.setText(pt.substring(1,pt.length()-1));
                                spESIC.setVisibility(View.VISIBLE);
                                spESIC.setSelection(0);

                            }else if(ESIC.contains("%"))
                            {
                                edtESIC.setText(pt.substring(1,pt.length()-1));
                                spESIC.setVisibility(View.VISIBLE);
                                spESIC.setSelection(1);
                            }

                        }
                        else {
                            rdbtnESICNo.setChecked(true);
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
                final Dialog dialog1 = new Dialog(PolicyAndConfiguration.this);
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
                final PrettyDialog prettyDialog = new PrettyDialog(PolicyAndConfiguration.this);
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
                params.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
                Log.e("hrms_config_params", "" + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(PolicyAndConfiguration.this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
public void getsalStruct()
{
    temp_sal_structures.clear();
    for(salary_structure s:salary_structures)
    {
        if(!s.getStatus().equalsIgnoreCase("deactive"))
        {
            temp_sal_structures.add(s);
        }

    }
    salaryStructAdapter.notifyDataSetChanged();
}
    public void getLeaveStructlist()
    {
        temp_leave_structures.clear();
        for(LeaveStruct s:leaveStructs)
        {
            if(s.getLeaveStatus().equalsIgnoreCase("active"))
            {
                temp_leave_structures.add(s);
            }

        }
        leave_structure_adapter.notifyDataSetChanged();
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

                if (error_code == 104)
                {
                    final PrettyDialog prettyDialog = new PrettyDialog(PolicyAndConfiguration.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("HRMS Config")
                            .setMessage(msg)
                            .setIcon(R.drawable.success, R.color.white, null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                    preferenceManager.setPreference(PreferenceManager.KEY_HRMS_CONFIGURATION,"Y");
                                    //startActivity(new Intent(PolicyAndConfiguration.this,EmployerZone.class));
                                    finish();
                                }
                            })
                            .show();

                    // Toast.makeText(PolicyAndConfiguration.this, sal_name_1, Toast.LENGTH_SHORT).show();
                     /*   y_net_payable = subscribeData.getString("created_date");
                        y_net_payable = subscribeData.getString("modified_date");*/

                } else {
                    final PrettyDialog prettyDialog = new PrettyDialog(PolicyAndConfiguration.this);
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
                final Dialog dialog1 = new Dialog(PolicyAndConfiguration.this);
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
                final PrettyDialog prettyDialog = new PrettyDialog(PolicyAndConfiguration.this);
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
                params.put("mode", "add");
                params.put("salary_struct", getSalaryStruct());
                params.put("leave_struct", getLeaveStruct());
                params.put("setting_struct", getSettings());
                params.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
                Log.e("hrms_config_params", "" + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(PolicyAndConfiguration.this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
    String getSalaryStruct()
    {
        String sal="";

        for(int i=0;i<salary_structures.size();i++)
        {
            sal=sal+(i+1)+"_"+salary_structures.get(i).getStruct_name()+"@"+salary_structures.get(i).getStatus()+",";
        }
        for(int i=salary_structures.size();i<10;i++)
        {
            sal=sal+(i+1)+"_"+",";
        }
        return sal;
    }
    String getLeaveStruct()
    {
        String sal="";

        for(int i=0;i<leaveStructs.size();i++)
        {
            sal=sal+(i+1)+"_"+leaveStructs.get(i).getLeaveName()+"="+leaveStructs.get(i).getCarryForwardable()+"@"+leaveStructs.get(i).getNoOfLeaves()+"$"+leaveStructs.get(i).getType()+"#"+leaveStructs.get(i).getLeaveType()+"!"+leaveStructs.get(i).getLeaveStatus()+"&"+leaveStructs.get(i).getReset_date()+",";
        }
        for(int i=leaveStructs.size();i<10;i++)
        {
            sal=sal+(i+1)+"_"+",";
        }
        return sal;
    }
    String getSettings()
    {
         String setting="";
         if(rbpfYes.isChecked())
         {
             setting=setting+"provident_found="+spPF.getSelectedItem().toString()+"@("+etPF.getText().toString()+")";
         }
         else
             {
                 setting=setting+"provident_found="+spPF.getSelectedItem().toString()+"@(0)";
         }
        if(rbptYes.isChecked())
        {
            setting=setting+",professional_tax="+spPT.getSelectedItem().toString()+"@("+etPT.getText().toString()+")";
        }
        else
        {
            setting=setting+",professional_tax="+spPT.getSelectedItem().toString()+"@(0)";;
        }
        if(rdbtnGSTyes.isChecked())
        {
            setting=setting+",GST="+spGST.getSelectedItem().toString()+"@("+edtGST.getText().toString()+")";
        }
        else
        {
            setting=setting+",GST="+spGST.getSelectedItem().toString()+"@(0)";
        }
        if(rdbtnTDSyes.isChecked())
        {
            setting=setting+",TDS="+spTDS.getSelectedItem().toString()+"@("+edtTDS.getText().toString()+")";
        }
        else
        {
            setting=setting+",TDS="+spTDS.getSelectedItem().toString()+"@(0)";
        }
        if(rdbtnESICYes.isChecked())
        {
            setting=setting+",ESIC="+spESIC.getSelectedItem().toString()+"@("+edtESIC.getText().toString()+")";
        }
        else
        {
            setting=setting+",ESIC="+spESIC.getSelectedItem().toString()+"@(0)";
        }

          return setting;
    }
    //backpress
    protected void exitByBackKey()
    {

        final Dialog dialog = new Dialog(PolicyAndConfiguration.this);
// Include dialog.xml file
        dialog.setContentView(R.layout.dialog_exit);
        dialog.show();

        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(PolicyAndConfiguration.this, EmployerZone.class);
                //startActivity(intent);
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