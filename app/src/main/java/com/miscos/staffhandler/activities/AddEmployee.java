package com.miscos.staffhandler.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fivemin.chief.nonetworklibrary.networkBroadcast.NoNet;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miscos.staffhandler.Adapters.organizationAdpater;
import com.miscos.staffhandler.hrms_management.EmployeeHRMS.EmployeeInformationHRMS;
import com.miscos.staffhandler.hrms_management.hrms.utility.Helper;
import com.miscos.staffhandler.MainActivity;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.Urls.App_Urls;
import com.miscos.staffhandler.databinding.ActivityAddEmployeeBinding;
import com.miscos.staffhandler.employee.Model.DList;
import com.miscos.staffhandler.employee.Model.Department;
import com.miscos.staffhandler.employee.Model.DepartmentResponse;
import com.miscos.staffhandler.employee.Model.DesignationArr;
import com.miscos.staffhandler.employee.Model.DesignationResponse;
import com.miscos.staffhandler.employee.Model.orgModel;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.miscos.staffhandler.employee.helper.VolleyMultipartRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


public class AddEmployee extends AppCompatActivity implements View.OnClickListener
{

    public static final int REQUEST_IMAGE_GALLERY = 100;
    public static final int REQUEST_IMAGE = 200;
    PreferenceManager preferenceManager;
    Button btnSubmit;
    String employee_id="",desgn_id="",dept_id="";
    ActivityAddEmployeeBinding binding;
    RadioGroup genderRadioGroup, allowemergencyRd, employeeTypeRd,rdgroupemployement_type,rdgroupsmartphn,rdgroupContractType;
    CoordinatorLayout coordinatorLayout;
    String name, mobile, empoyee_no,smarphn_type="",address,contract_type="", userage="",employment_type="", lastname, gender="", employer_id, access_code, email_id, employeeType, emergencyType, encodedImage, orgnization_value;
    TextView addInBulk,userAge,edtDOJ,txtverify;
    ImageView tvBack;
    organizationAdpater organizationAdpater;
    FaceDetector detector;
    boolean isFaceDetected = false;
    private EditText userName, userLName, userMobile, userAddress,edtadharNo,edtPanNo,edtdesignation,edtpermanentAddress,edtemail;
    private int maxage = 16;
    private int age;
    TextView txtemployeeradio;
    private ProgressDialog progressDialog;
    private Dialog dialog;
    private List<orgModel> orgModels = new ArrayList<>();
    // No Internet Dialog
    private NoNet mNoNet;
    private FragmentManager fm = null;
    private CircleImageView imageEmp;
    private byte[] byteAddOwnerImg;
    private boolean fileStatusProfilePic,isAdharVerified=false;
    EditText edtemployeeNo,edtcontractype;
    TextView txtemployeeno,txtcontractName;
    String panNumbe="";
    int count = 0,selected_dept_pos=0;
    ArrayList<String>deptnames=new ArrayList<>(),designationnames=new ArrayList<>();
    ArrayAdapter<String> designationAdapter,deptAdapter;
    List<DList> deptList=new ArrayList<>();
    public static byte[] getFileDataFromString(String path)
    {
        File file = new File(path);
        byte[] fileContent = new byte[(int) file.length()];
        try {
            FileInputStream fin = new FileInputStream(file);
            fin.read(fileContent);
            Log.d("SUCCESS", "READ");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }
    CheckBox chkCheckBox;
    String employeeNo="";

    Dialog dialog_empno;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_add_employee);
        fm = getSupportFragmentManager();
        mNoNet = new NoNet();
        mNoNet.initNoNet(this, fm);
        preferenceManager = new PreferenceManager(AddEmployee.this);

        //Progress Bar
        progressDialog = new ProgressDialog(AddEmployee.this);
        progressDialog = ProgressDialog.show(AddEmployee.this, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.dismiss();

        designationAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,designationnames);
        binding.spindesign.setAdapter(designationAdapter);
        deptAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,deptnames);
        binding.spindept.setAdapter(deptAdapter);
        binding.spindept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                dept_id=deptList.get(i).getDeptId();
                selected_dept_pos=i;
                designationnames.clear();
                designationnames.add("select");
                for(DesignationArr designationArr:deptList.get(i).getDesignationArr())
                {
                    designationnames.add(designationArr.getName());
                }
                designationAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
        binding.spindesign.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(i!=0)
                {
                    DList dList=deptList.get(selected_dept_pos);
                    desgn_id=dList.getDesignationArr().get(i-1).getId();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        detector = new FaceDetector.Builder(AddEmployee.this)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();

        employer_id = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID);
        userName = findViewById(R.id.name);
        userLName = findViewById(R.id.Lname);
        userMobile = findViewById(R.id.mobile);
        userAddress = findViewById(R.id.etHomeAddress);
        edtcontractype=findViewById(R.id.edtcontracttype);
        edtpermanentAddress=findViewById(R.id.edtPermanentAddress);
        userAge = findViewById(R.id.age);
        edtemail=findViewById(R.id.edtemail);
        txtcontractName=findViewById(R.id.contractype);

        edtadharNo=findViewById(R.id.edtAddharno);
        txtemployeeradio=findViewById(R.id.employee_type);
        rdgroupContractType=findViewById(R.id.rdgroupContracttype);
        edtpermanentAddress=findViewById(R.id.edtPermanentAddress);
        edtPanNo=findViewById(R.id.edtPanNo);
        rdgroupsmartphn=findViewById(R.id.rdgroupEmptype3);
        rdgroupsmartphn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
         {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                switch (radioGroup.getCheckedRadioButtonId())
                {
                    case R.id.rdbtnyes_smartphn:
                        smarphn_type="Y";
                        break;
                    case R.id.rdbtnno_smartphn:
                        smarphn_type="N";
                        break;
                }

            }
        });
        rdgroupContractType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                switch (radioGroup.getCheckedRadioButtonId())
                {
                    case R.id.rdbtnself:
                        contract_type="self";
                        txtcontractName.setVisibility(View.GONE);
                        edtcontractype.setVisibility(View.GONE);

                        break;
                    case R.id.rdbtnthirdparty:
                        edtcontractype.setVisibility(View.VISIBLE);
                        txtcontractName.setVisibility(View.VISIBLE);
                        contract_type="third-party";
                        break;
                }

            }
        });
        edtPanNo.addTextChangedListener(new TextWatcher()

        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
        });
        txtverify=findViewById(R.id.txtverify);
        txtverify.setVisibility(View.GONE);
        rdgroupemployement_type=findViewById(R.id.rdgroupEmptype2);
        rdgroupemployement_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                int id=radioGroup.getCheckedRadioButtonId();
                switch (id)
                {
                    case R.id.rdbtnpayrole:
                        employment_type="payrole";
                        rdgroupContractType.setVisibility(View.GONE);
                        break;
                    case R.id.rdbtncontract:
                        employment_type="contract";
                        rdgroupContractType.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        chkCheckBox=findViewById(R.id.chkpermanentAddress);
        chkCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if(b)
                {
                    if(userAddress.getText().toString().equalsIgnoreCase(""))
                    {
                        Toast.makeText(AddEmployee.this, "Please enter your local address", Toast.LENGTH_SHORT).show();
                        userAddress.requestFocus();
                        return;
                    }
                    edtpermanentAddress.setText(userAddress.getText().toString());

                }else
                {
                    edtpermanentAddress.setText("");
                }
            }
        });
        edtadharNo.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edtadharNo.getText().toString().length() == 14&&preferenceManager.getStringPreference(PreferenceManager.KEY_AADHAR_VERIFY_POLICY).equalsIgnoreCase("y"))
                {
                    txtverify.setVisibility(View.VISIBLE);

                }

                if (edtadharNo.getText().toString().length() != 14)
                {
                    txtverify.setVisibility(View.GONE);
                    isAdharVerified=false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        txtverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Helper.showProgress(AddEmployee.this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       Helper.hideProgress();
                        Toast.makeText(AddEmployee.this, "Adhar Verified ", Toast.LENGTH_SHORT).show();
                        txtverify.setText("Verified");
                        txtverify.setEnabled(false);

                        isAdharVerified=true;
                    }
                }, 300);
            }
        });
        userAge.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                openDatePickerDialog(AddEmployee.this,userAge);
            }
        });
        edtDOJ=findViewById(R.id.doj);
        edtDOJ.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                openDatePickerDialog(AddEmployee.this,edtDOJ);
            }
        });
        addInBulk = findViewById(R.id.tvAddBulk);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        allowemergencyRd = findViewById(R.id.allowEmergency);
        employeeTypeRd = findViewById(R.id.employeeRadio);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvBack = findViewById(R.id.imBack);
        imageEmp = findViewById(R.id.empImage);

        userName.setHint(Html.fromHtml(getColoredSpanned("*", "#FF0000") + " First Name"));
        userLName.setHint(Html.fromHtml(getColoredSpanned("*", "#FF0000") + " Last Name"));
        userMobile.setHint(Html.fromHtml(getColoredSpanned("*", "#FF0000") + " Mobile"));
        edtadharNo.setHint(Html.fromHtml(getColoredSpanned("*", "#FF0000") + " Adhar Number"));
        edtDOJ.setHint(Html.fromHtml(getColoredSpanned("*", "#FF0000") + " Date of Joining"));
        userAge.setHint(Html.fromHtml(getColoredSpanned("*", "#FF0000") + " Date of Birth"));

       // edtdesignation.setHint(Html.fromHtml(getColoredSpanned("*", "#FF0000") + " Designation"));
        edtadharNo.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) { /*Empty*/}

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) { /*Empty*/ }

            @Override
            public void afterTextChanged(Editable s) {

                int inputlength = edtadharNo.getText().toString().length();

                if (count <= inputlength &&(inputlength == 4 || inputlength == 9))
                {

                    edtadharNo.setText(edtadharNo.getText().toString() + " ");

                    int pos = edtadharNo.getText().length();
                    edtadharNo.setSelection(pos);

                } /*else if (count >= inputlength && (inputlength == 4 ||
                        inputlength == 9 || inputlength == 14)) {
                    edtadharNo.setText(edtadharNo.getText().toString()
                            .substring(0, edtadharNo.getText()
                                    .toString().length() - 1));

                    int pos = edtadharNo.getText().length();
                    edtadharNo.setSelection(pos);
                }*/
                count = edtadharNo.getText().toString().length();
            }
        });
        call_bulk1();
getDesignations();
        //Back Button
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddEmployee.this, MainActivity.class));
                finish();
            }
        });
        imageEmp.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        addInBulk.setOnClickListener(this);
        if(preferenceManager.getStringPreference(PreferenceManager.KEY_SHIFT_COUNT).equalsIgnoreCase("0"))
        {
            txtemployeeradio.setVisibility(View.GONE);
            employeeTypeRd.setVisibility(View.GONE);
            employeeType = "office";
        }
        else
        {
            txtemployeeradio.setVisibility(View.VISIBLE);
            employeeTypeRd.setVisibility(View.VISIBLE);


        }
        /*if(preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_NO_TYPE).equalsIgnoreCase(""))
        {
            final PrettyDialog prettyDialog = new PrettyDialog(AddEmployee.this);
            prettyDialog.setCancelable(false);
            prettyDialog

                    .setMessage("Please set policies & configuration before adding employee")
                    .setIcon(R.drawable.cross, R.color.white, null)
                    .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            prettyDialog.dismiss();
                            Intent i = new Intent(AddEmployee.this, com.miscos.staffhandler.employer_java.activity.MainActivity.class);
                            i.putExtra("type", "form3");
                            i.putExtra("employerId", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
                            startActivity(i);
                            finish();
                        }
                    })
                    .show();
        }*/
    }

    @Override
    public void onClick(View v) {
        int selectGender = genderRadioGroup.getCheckedRadioButtonId();

        switch (selectGender) {
            case R.id.male:
                gender = "male";
                break;
            case R.id.female:
                gender = "female";
                break;
            case R.id.Other:
                gender = "other";
                break;
        }
        int emergencyDuty = allowemergencyRd.getCheckedRadioButtonId();

        switch (emergencyDuty) {
            case R.id.yes:
                emergencyType = "Y";
                break;
            case R.id.no:
                emergencyType = "N";
                break;
        }
        int employeetype = employeeTypeRd.getCheckedRadioButtonId();
        switch (employeetype) {
            case R.id.officeStaff:
                employeeType = "office";
                break;
            case R.id.shiftStaff:
                employeeType = "shift";
                break;
        }
        int id1 = v.getId();
        if (id1 == R.id.empImage) {
            dialogToSelectImage(REQUEST_IMAGE, REQUEST_IMAGE_GALLERY);
        }

        if (id1 == R.id.btnSubmit) {

                if (Validation()) {
                    progressDialog.show();
                    showEmpNoDialog();
                    //addEmployees();
                }
            /*else {
                progressDialog.show();
                addEmployees();
            }*/
        }
        if (id1 == R.id.tvAddBulk) {

            showDialogForSend();
        }


    }
    void showEmpNoDialog()
    {
        dialog_empno=new Dialog(this);
        dialog_empno.setContentView(R.layout.dialogformore);
       txtemployeeno=dialog_empno.findViewById(R.id.txtemployeeno);
       edtemployeeNo=dialog_empno.findViewById(R.id.edtemployeeno);
        if(preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_NO_TYPE).equalsIgnoreCase("Y"))
        {
            edtemployeeNo.setEnabled(true);
            edtemployeeNo.setVisibility(View.VISIBLE);
            txtemployeeno.setVisibility(View.GONE);
        }
        else
        {
            edtemployeeNo.setEnabled(false);
            edtemployeeNo.setVisibility(View.GONE);
            txtemployeeno.setVisibility(View.VISIBLE);
            employeeNumber("","save");
        }
        AppCompatButton btnsubmit=dialog_empno.findViewById(R.id.btnproceed);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(edtemployeeNo.getVisibility()==View.VISIBLE)
                {
                    if(edtemployeeNo.getText().toString().equalsIgnoreCase(""))
                    {
                        Toast.makeText(AddEmployee.this, "Employee Number is invalid", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    employeeNo=edtemployeeNo.getText().toString();
                }

                final PrettyDialog prettyDialog= new PrettyDialog(AddEmployee.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Add Employee")
                        .setMessage("Are you sure to proceed with this employee number?")
                        .setIcon(R.drawable.info,R.color.white,null)
                        .addButton("Continue", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                            @Override
                            public void onClick()
                            {

                                if(preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_NO_TYPE).equalsIgnoreCase("Y"))
                                {
                                    employeeNumber(edtemployeeNo.getText().toString(),"save");
                                }
                                else
                                {
                                  employeeNo=empoyee_no;
                                    dialog_empno.dismiss();
                                    addEmployees();

                                }

                                prettyDialog.dismiss();
                            }
                        })
                        .addButton("Back", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                                dialog_empno.dismiss();
                            }
                        })
                        .show();
            }
        });
        dialog_empno.setCancelable(false);

        if(preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_NO_TYPE).equalsIgnoreCase("y"))
            dialog_empno.show();




    }
    private void employeeNumber(String emp_no,String save_flag)
    {
        progressDialog.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.Employee_Management, response -> {
            Log.d("login response is", " " + response);
            String msg="";
            int errorcode=0;
            progressDialog.dismiss();
            try {
                JSONArray jsonArray=new JSONArray(response);
                JSONObject jsonObject=jsonArray.getJSONObject(0);
                errorcode=jsonObject.getInt("error_code");
                msg=jsonObject.getString("message");


            if (errorcode == 101)
            {
                empoyee_no=jsonObject.getString("employee_no");
                if(preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_NO_TYPE).equalsIgnoreCase("y"))
                {
                   dialog_empno.dismiss();
                    addEmployees();
                }

                else
                {
                    txtemployeeno.setText("Employee number will be :\n "+empoyee_no);
                    dialog_empno.show();
                }



            } else if (errorcode == 106)
            {
                final PrettyDialog prettyDialog = new PrettyDialog(AddEmployee.this);
                prettyDialog
                        .setTitle("Add Employee")
                        .setMessage(msg)
                        .setIcon(R.drawable.cross)
                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                                Intent i = new Intent(AddEmployee.this, MainActivity.class);
                                i.putExtra("type", "form3");
                                i.putExtra("employerId", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
                                startActivity(i);
                                finish();
                            }
                        })
                        .show();

            }else if (errorcode == 103) {
                Toast.makeText(AddEmployee.this, msg, Toast.LENGTH_SHORT).show();
            } else if (errorcode == 102) {
                Toast.makeText(AddEmployee.this, msg, Toast.LENGTH_SHORT).show();
            } else if (errorcode == 100) {
                Toast.makeText(AddEmployee.this, msg, Toast.LENGTH_SHORT).show();
            } else if (errorcode == 104) {
                final PrettyDialog prettyDialog = new PrettyDialog(AddEmployee.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Add Employee")
                        .setMessage(msg)
                        .setIcon(R.drawable.cross, R.color.white, null)
                        .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                                startActivity(new Intent(AddEmployee.this, MainActivity.class));
                                finish();
                            }
                        })
                        .show();
            } else if (errorcode == 105) {
                final PrettyDialog prettyDialog = new PrettyDialog(AddEmployee.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Already Exist")
                        .setMessage(msg)
                        .setIcon(R.drawable.cross, R.color.white, null)
                        .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                            }
                        })
                        .show();
            } else if (errorcode == 106)
            {
                final PrettyDialog prettyDialog = new PrettyDialog(AddEmployee.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Employee")
                        .setMessage(msg)
                        .setIcon(R.drawable.cross, R.color.white, null)
                        .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                            }
                        })
                        .show();
            } else if(errorcode==109)
            {
                orgModels.clear();
                JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArrayData.length(); i++) {
                    JSONObject dayActivityList = jsonArrayData.getJSONObject(i);
                    orgModel reportModel1 = new orgModel();
                    reportModel1.setName(dayActivityList.getString("name"));
                    reportModel1.setCompanyName(dayActivityList.getString("company_name"));
                    reportModel1.setEmployeeImage(dayActivityList.getString("employee_pic"));
                    reportModel1.setEmployer_id(dayActivityList.getString("employer_id"));
                    orgModels.add(reportModel1);
                }
                organizationAdpater = new organizationAdpater(this, orgModels,"add",null);
                dialog = new Dialog(AddEmployee.this);
                // Include dialog.xml file
                dialog.setContentView(R.layout.multipleorg);
                dialog.show();
                dialog.setCancelable(false);
                Button buttonSubmit = (Button) dialog.findViewById(R.id.btn_yes);
                Button buttonNo = (Button) dialog.findViewById(R.id.btn_no);
                ImageView imgCancel = (ImageView) dialog.findViewById(R.id.imCancel);
                RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.relative_org);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(organizationAdpater);

                imgCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                buttonNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                buttonSubmit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                        progressDialog.show();

                        employeeNumber(emp_no,"continue");


                    }
                });
            }
            else
            {
                final PrettyDialog prettyDialog = new PrettyDialog(AddEmployee.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Employee")
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
            }
        }, error -> {
            progressDialog.dismiss();
            final PrettyDialog prettyDialog = new PrettyDialog(AddEmployee.this);
            prettyDialog.setCancelable(false);
            prettyDialog
                    .setTitle("Connection Error")
                    .setMessage("Please try again")
                    .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                    .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            prettyDialog.dismiss();
                        }
                    })
                    .show();
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mode", "generate_employee_no");
                params.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
                if(preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_NO_TYPE).equalsIgnoreCase("y"))
                params.put("employee_no", emp_no);
                else
                    params.put("employee_no", "");
                params.put("mobile_no", userMobile.getText().toString());
                params.put("save_flag", save_flag);
                Log.e("org  params is", " " + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(AddEmployee.this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void addEmployees()
    {
        progressDialog.show();
        //Log.e("url is", "" + Url_Class.URL_STAFF_ENTRY_EXIT);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                App_Urls.Employee_Management, response -> {
            String resultResponse = new String(response.data);
            Log.d("response", resultResponse);
            try {
               // JSONArray jsonArray = new JSONArray(resultResponse);
                JSONObject jsonObject = new JSONObject(resultResponse);
                int error_code = jsonObject.getInt("error_code");
                String msg = jsonObject.getString("message");
                progressDialog.dismiss();
                if (error_code == 101) {
                    employee_id=jsonObject.getString("employee_id");
                    add_Dialog();
                } else if (error_code == 103) {
                    Toast.makeText(AddEmployee.this, msg, Toast.LENGTH_SHORT).show();
                } else if (error_code == 102) {
                    Toast.makeText(AddEmployee.this, msg, Toast.LENGTH_SHORT).show();
                } else if (error_code == 100) {
                    Toast.makeText(AddEmployee.this, msg, Toast.LENGTH_SHORT).show();
                } else if (error_code == 104) {
                    final PrettyDialog prettyDialog = new PrettyDialog(AddEmployee.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Add Employee")
                            .setMessage(msg)
                            .setIcon(R.drawable.cross, R.color.white, null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                    startActivity(new Intent(AddEmployee.this, MainActivity.class));
                                    finish();
                                }
                            })
                            .show();
                } else if (error_code == 105) {
                    final PrettyDialog prettyDialog = new PrettyDialog(AddEmployee.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Already Exist")
                            .setMessage(msg)
                            .setIcon(R.drawable.cross, R.color.white, null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            })
                            .show();
                } else if (error_code == 106)
                {
                    final PrettyDialog prettyDialog = new PrettyDialog(AddEmployee.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Employee")
                            .setMessage(msg)
                            .setIcon(R.drawable.cross, R.color.white, null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    orgModels.clear();
                    JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArrayData.length(); i++)
                    {
                        JSONObject dayActivityList = jsonArrayData.getJSONObject(i);
                        orgModel reportModel1 = new orgModel();
                        reportModel1.setName(dayActivityList.getString("name"));
                        reportModel1.setCompanyName(dayActivityList.getString("company_name"));
                        reportModel1.setEmployeeImage(dayActivityList.getString("employee_pic"));
                        reportModel1.setEmployer_id(dayActivityList.getString("employer_id"));
                        orgModels.add(reportModel1);
                    }
                    organizationAdpater = new organizationAdpater(this, orgModels,"add",null);
                    dialog = new Dialog(AddEmployee.this);
                    // Include dialog.xml file
                    dialog.setContentView(R.layout.multipleorg);
                    dialog.show();
                    dialog.setCancelable(false);
                    Button buttonSubmit = (Button) dialog.findViewById(R.id.btn_yes);
                    Button buttonNo = (Button) dialog.findViewById(R.id.btn_no);
                    ImageView imgCancel = (ImageView) dialog.findViewById(R.id.imCancel);
                    RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.relative_org);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(organizationAdpater);

                    imgCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    buttonNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    buttonSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            progressDialog.show();
                            addEmployees1();

                        }
                    });
                }
            } catch (JSONException ex) {
                Log.e("Check", "JSONEXCEPTION" + ex);
                ex.printStackTrace();
                final Dialog dialog1 = new Dialog(AddEmployee.this);
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
                        startActivity(new Intent(AddEmployee.this, MainActivity.class));
                        finish();
                    }
                });
            }
            progressDialog.dismiss();
            Log.d("RESPONSE", resultResponse);
        }, error -> {
            progressDialog.dismiss();
            final PrettyDialog prettyDialog = new PrettyDialog(AddEmployee.this);
            prettyDialog.setCancelable(false);
            prettyDialog
                    .setTitle("Connection Error")
                    .setMessage("Please try again")
                    .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                    .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            prettyDialog.dismiss();
                            startActivity(new Intent(AddEmployee.this, MainActivity.class));
                            finish();
                        }
                    })
                    .show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mode", "insert");
                params.put("employer_id", employer_id);
                params.put("first_name", userName.getText().toString());
                params.put("last_name", userLName.getText().toString());
                params.put("contact", userMobile.getText().toString());
                params.put("address", userAddress.getText().toString());
                params.put("gender", gender);
                params.put("date_of_birth", userAge.getText().toString());
                params.put("age",userage);
                params.put("date_of_joining", edtDOJ.getText().toString());
                params.put("adhar_no",edtadharNo.getText().toString().trim());
                params.put("pan_no",panNumbe);
                params.put("employment_type", employment_type);
                params.put("emergency_duty", emergencyType);
                params.put("parment_address", edtpermanentAddress.getText().toString());
                params.put("operation_type", employeeType);
                if(isAdharVerified)
                    params.put("adhar_no_verified", "Y");
                else
                    params.put("adhar_no_verified", "N");

                params.put("designation",desgn_id);
                params.put("department_id",dept_id);
                params.put("save_flag", "continue");
                params.put("employee_no", employeeNo);
                params.put("having_smart_phone", smarphn_type);
                params.put("email_id", edtemail.getText().toString());
                params.put("contract_type",contract_type);
                params.put("contract_company_name",edtcontractype.getText().toString());
                Log.d("params is", " " + params);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                if (!fileStatusProfilePic) {
                    byteAddOwnerImg = null;
                } else {
                    String imagename = "employeeImage";
                    params.put("employee_pic", new DataPart(imagename + ".png", byteAddOwnerImg));
                }
                Log.d("params is", " " + params);
                return params;
            }
        };
        multipartRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(multipartRequest);
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void addEmployees1()
    {
        //Log.e("url is", "" + Url_Class.URL_STAFF_ENTRY_EXIT);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                App_Urls.Employee_Management, response ->
        {
            String resultResponse = new String(response.data);
            Log.d("response", resultResponse);
            try {
                JSONArray jsonArray = new JSONArray(resultResponse);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                int error_code = jsonObject.getInt("error_code");
                String msg = jsonObject.getString("message");
                if (error_code == 101)
                {
                    employee_id=jsonObject.getString("employee_id");
                    add_Dialog();
                } else if (error_code == 103) {
                    Toast.makeText(AddEmployee.this, msg, Toast.LENGTH_SHORT).show();
                } else if (error_code == 102) {
                    Toast.makeText(AddEmployee.this, msg, Toast.LENGTH_SHORT).show();
                } else if (error_code == 100) {
                    Toast.makeText(AddEmployee.this, msg, Toast.LENGTH_SHORT).show();
                } else if (error_code == 104) {
                    final PrettyDialog prettyDialog = new PrettyDialog(AddEmployee.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Add Employee")
                            .setMessage(msg)
                            .setIcon(R.drawable.cross, R.color.white, null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                    startActivity(new Intent(AddEmployee.this, MainActivity.class));
                                    finish();
                                }
                            })
                            .show();
                } else if (error_code == 105) {
                    final PrettyDialog prettyDialog = new PrettyDialog(AddEmployee.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Add Employee")
                            .setMessage(msg)
                            .setIcon(R.drawable.cross, R.color.white, null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            })
                            .show();
                } else if (error_code == 106) {
                    final PrettyDialog prettyDialog = new PrettyDialog(AddEmployee.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Employee")
                            .setMessage(msg)
                            .setIcon(R.drawable.cross, R.color.white, null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                   // Toast.makeText(this, "Null array", Toast.LENGTH_SHORT).show();
                    JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                    JSONObject jsonObject1 = jsonArrayData.getJSONObject(0);
                    String name = jsonObject1.getString("name");

                    final PrettyDialog prettyDialog = new PrettyDialog(AddEmployee.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Organization")
                            .setMessage("Add Employee")
                            .setIcon(R.drawable.cross, R.color.white, null)
                            .addButton("Yes", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            })
                            .addButton("No", R.color.whiteTextColor, R.color.red_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    orgnization_value = "save";
                                    prettyDialog.dismiss();
                                }
                            })
                            .show();
                }
            } catch (JSONException ex) {
                Log.e("Check", "JSONEXCEPTION" + ex);
                ex.printStackTrace();
                final Dialog dialog1 = new Dialog(AddEmployee.this);
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
                        startActivity(new Intent(AddEmployee.this, MainActivity.class));
                        finish();
                    }
                });
            }
            progressDialog.dismiss();
            Log.d("RESPONSE", resultResponse);
        }, error -> {
            progressDialog.dismiss();
            final PrettyDialog prettyDialog = new PrettyDialog(AddEmployee.this);
            prettyDialog.setCancelable(false);
            prettyDialog
                    .setTitle("Connection Error")
                    .setMessage("Please try again")
                    .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                    .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            prettyDialog.dismiss();
                            startActivity(new Intent(AddEmployee.this, MainActivity.class));
                            finish();
                        }
                    })
                    .show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mode", "insert");
                params.put("employer_id", employer_id);
                params.put("first_name", userName.getText().toString());
                params.put("last_name", userLName.getText().toString());
                params.put("contact", userMobile.getText().toString());
                params.put("address", userAddress.getText().toString());
                params.put("date_of_birth", userAge.getText().toString());
                params.put("age",userage);
                params.put("date_of_joining", edtDOJ.getText().toString());
                params.put("adhar_no",edtadharNo.getText().toString().trim());
                params.put("pan_no",panNumbe);
                params.put("designation",desgn_id);
                params.put("department_id",dept_id);
                params.put("gender", gender);
                params.put("emergency_duty", emergencyType);
                params.put("operation_type", employeeType);
                params.put("employment_type", employment_type);
                params.put("employee_no", employeeNo);
                params.put("parment_address", edtpermanentAddress.getText().toString());
                if(isAdharVerified)
                params.put("adhar_no_verified", "Y");
                else
                    params.put("adhar_no_verified", "N");

                params.put("save_flag", "continue");
                params.put("having_smart_phone", smarphn_type);
                params.put("email_id", edtemail.getText().toString());
                params.put("contract_type",contract_type);
                params.put("contract_company_name",edtcontractype.getText().toString());
                Log.d("params is", " " + params);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                Log.d("TAG", "getImageInByteArray: encoded12" + encodedImage);
                if (!fileStatusProfilePic) {
                    byteAddOwnerImg = null;
                } else {
                    String imagename = "employeeImage";
                    params.put("employee_pic", new DataPart(imagename + ".png", byteAddOwnerImg));
                }
                Log.d("params is", " " + params);
                return params;
            }
        };
        multipartRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(multipartRequest);
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void dialogToSelectImage(final int requestImageCamera, final int requestImageGallary)
    {
        LayoutInflater factory = LayoutInflater.from(this);
        final View dialogToSelectImage = factory.inflate(R.layout.dialog_selectphoto, null);
        final AlertDialog selectImage = new AlertDialog.Builder(this).create();
        selectImage.setView(dialogToSelectImage);
        TextView fromGallary = dialogToSelectImage.findViewById(R.id.dialogChoosePhoto); //
        TextView fromCamera = dialogToSelectImage.findViewById(R.id.dialogOpenCamera); //
        TextView cancel = dialogToSelectImage.findViewById(R.id.cancel_dialog); //

        fromCamera.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                int request = requestImageCamera;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(AddEmployee.this.getPackageManager()) != null)
                {
                    startActivityForResult(takePictureIntent, request);
                }
                selectImage.dismiss();
            }
        });


        fromGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, requestImageGallary);

                selectImage.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage.dismiss();
            }
        });

        selectImage.show();
        selectImage.setCancelable(true);

    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(getContext(), "Image captured", Toast.LENGTH_SHORT).show();

        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {

            if (data != null) {
                try {
                    Bundle extras = data.getExtras();
                    Bitmap profileBitmap = (Bitmap) extras.get("data");
                    assert profileBitmap != null;
                    faceDetection(profileBitmap);
                    byteAddOwnerImg = getImageInByteArray(profileBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fileStatusProfilePic = true;
            }

        }
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    Uri uri = data.getData();
                    Bitmap bitmapProfiles = decodeUri(uri, 400);
                    assert bitmapProfiles != null;
                    faceDetection(bitmapProfiles);
                    byteAddOwnerImg = getImageInByteArray(bitmapProfiles);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fileStatusProfilePic = true;
            }
        }
    }

    private byte[] getImageInByteArray(Bitmap imageBitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byteAddOwnerImg = stream.toByteArray();

        encodedImage = Base64.encodeToString(byteAddOwnerImg, Base64.DEFAULT);

        return byteAddOwnerImg;
    }

    //COnvert and resize our image to 400dp for faster uploading our images to DB
    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {

        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(AddEmployee.this.getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(AddEmployee.this.getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    void faceDetection(Bitmap bitmap) {

        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Face> faces = detector.detect(frame);
        if (faces.size() == 1) {
            //isFaceDetected=true;
            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);

            Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(mutableBitmap);
            Log.d("TAG", "Faces detected: " + String.valueOf(faces.size()));
            for (int i = 0; i < faces.size(); ++i) {
                Face face = faces.valueAt(i);
                for (Landmark landmark : face.getLandmarks()) {
                    int cx = (int) (landmark.getPosition().x);
                    int cy = (int) (landmark.getPosition().y);
                    canvas.drawCircle(cx, cy, 10, paint);
                }

                Path path = new Path();
                path.moveTo(face.getPosition().x, face.getPosition().y);
                path.lineTo(face.getPosition().x + face.getWidth(), face.getPosition().y);
                path.lineTo(face.getPosition().x + face.getWidth(), face.getPosition().y + face.getHeight());
                path.lineTo(face.getPosition().x, face.getPosition().y + face.getHeight());
                path.close();

                Paint redPaint = new Paint();
                redPaint.setColor(0XFFFF0000);
                redPaint.setStyle(Paint.Style.STROKE);
                redPaint.setStrokeWidth(8.0f);
                canvas.drawPath(path, redPaint);
            }

            imageEmp.setImageBitmap(mutableBitmap);
        } else if (faces.size() > 1) {
            isFaceDetected = false;
            Toast.makeText(this, "More than one face detected , please select/capture another image", Toast.LENGTH_SHORT).show();
        } else {
            isFaceDetected = false;
            Toast.makeText(this, "No face detected , please select/capture another image", Toast.LENGTH_SHORT).show();
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


    private void add_Dialog()
    {
        final PrettyDialog prettyDialog = new PrettyDialog(AddEmployee.this);
        prettyDialog.setCancelable(false);
        prettyDialog
                .setTitle("Success")
                .setMessage("Employee Added Successfully")
                .setIcon(R.drawable.success, R.color.primaryTextColor, null)
                .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        prettyDialog.dismiss();
                        Intent mintent=new Intent(AddEmployee.this, EmployeeInformationHRMS.class);
                        mintent.putExtra("employee_id", employee_id);
                        mintent.putExtra("employee_name", userName.getText().toString());
                        mintent.putExtra("call_from","addEmp");
                        startActivity(mintent);
                        finish();
                    }
                })
                .show();

    }

    //add employess in bulk
    public void showDialogForSend() {
        dialog = new Dialog(AddEmployee.this);
// Include dialog.xml file
        dialog.setContentView(R.layout.add_bulk);
        dialog.show();
        //dialog.setCancelable(false);
        Button buttonSubmit = (Button) dialog.findViewById(R.id.btn_ok);
        ImageView imgCancel = (ImageView) dialog.findViewById(R.id.imCancel);
        EditText emailID = (EditText) dialog.findViewById(R.id.etEmail);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_id = emailID.getText().toString();
                call_bulk(email_id);
            }
        });
    }

    public String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    //Add_Bulk_Employee
    private void call_bulk(final String email_id) {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, App_Urls.BULK_SEND,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e("bulk employee", " " + response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            int error_code = jsonObject.getInt("error_code");
                            String msg = jsonObject.getString("message");
                            if (error_code == 101) {
                                dialog.dismiss();
                                final PrettyDialog prettyDialog = new PrettyDialog(AddEmployee.this);
                                prettyDialog.setCancelable(false);
                                prettyDialog
                                        .setTitle("Sent")
                                        .setMessage(msg)
                                        .setIcon(R.drawable.send, R.color.primaryTextColor, null)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog.dismiss();
                                            }
                                        })
                                        .show();
                            } else if (error_code == 103) {
                                Toast.makeText(AddEmployee.this, msg, Toast.LENGTH_SHORT).show();
                            } else if (error_code == 102) {
                                Toast.makeText(AddEmployee.this, msg, Toast.LENGTH_SHORT).show();
                            } else if (error_code == 104) {
                                Toast.makeText(AddEmployee.this, msg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(AddEmployee.this, R.string.connection_error, Toast.LENGTH_LONG).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mode", "sample");
                params.put("email_id", email_id);
                Log.e("bulk params is", " " + params);
                return params;
            }
        };
        // Adding request to request queue
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(AddEmployee.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void call_bulk1()
    {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, App_Urls.BULK_SEND,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("bulk employee", " " + response);
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            int error_code = jsonObject.getInt("error_code");
                            String msg = jsonObject.getString("message");
                            access_code = jsonObject.getString("code");
                            if (error_code == 103) {
                                Toast.makeText(AddEmployee.this, msg, Toast.LENGTH_SHORT).show();
                            } else if (error_code == 102) {
                                Toast.makeText(AddEmployee.this, msg, Toast.LENGTH_SHORT).show();
                            } else if (error_code == 104) {
                                Toast.makeText(AddEmployee.this, msg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(AddEmployee.this, R.string.connection_error, Toast.LENGTH_LONG).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mode", "code");
                params.put("user_id", employer_id);
                Log.e("bulk params is", " " + params);
                return params;
            }
        };
        // Adding request to request queue
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(AddEmployee.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public boolean Validation()
    {
        name = userName.getText().toString();
        mobile = userMobile.getText().toString();
        lastname = userLName.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Enter First Name", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        if (TextUtils.isEmpty(lastname))
        {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Enter Last Name", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        if (TextUtils.isEmpty(mobile) || !mobile.matches("^[6-9][0-9]{9}$")) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Enter Valid Mobile Number", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        //
        if (!TextUtils.isEmpty(edtemail.getText().toString()) )
        {
            if (!Patterns.EMAIL_ADDRESS.matcher(edtemail.getText().toString()).matches())
            {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Enter Valid Mobile Number", Snackbar.LENGTH_LONG);
                snackbar.show();
                return false;
            }

        }

        //
        if (userAge.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Enter Date of Birth", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }

        if (edtDOJ.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Enter Date of Joining", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        if (edtadharNo.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Enter Adhar Number", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        if (!Helper.isValidAadharNumber(edtadharNo.getText().toString().trim()))
        {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Enter Valid Adhar Number", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        if (!edtPanNo.getText().toString().isEmpty())
        {
            // get your editext value here
            Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
            Matcher matcher = pattern.matcher(edtPanNo.getText().toString());
            // Check if pattern matches
            if (matcher.matches()) {
                panNumbe = edtPanNo.getText().toString();
            } else {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Enter Valid PAN Number", Snackbar.LENGTH_LONG);
                snackbar.show();
                return false;
            }
        }
        if (desgn_id.isEmpty())
        {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Select Employee Designation", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        if(gender.equalsIgnoreCase(""))
        {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Select Gender", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        if (allowemergencyRd.getCheckedRadioButtonId() == -1) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Select Allow Emergency Duty", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        if (employeeTypeRd.getCheckedRadioButtonId() == -1&&employeeType.equalsIgnoreCase("")) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Select Employee Type", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        if (rdgroupemployement_type.getCheckedRadioButtonId() == -1) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Select Employment Type", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
       if(preferenceManager.getStringPreference(PreferenceManager.KEY_AADHAR_VERIFY_POLICY).equalsIgnoreCase("y")&&!isAdharVerified)
       {
           Snackbar snackbar = Snackbar
                   .make(coordinatorLayout, "Please verify adhar number", Snackbar.LENGTH_LONG);
           snackbar.show();
           return false;
       }
        if(employment_type.equalsIgnoreCase("contract")&&edtcontractype.getText().toString().isEmpty())
        {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Please enter contract type", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        return true;
    }
    public boolean getAge(int year, int month, int day) {
        try {
            Calendar dob = Calendar.getInstance();
            Calendar today = Calendar.getInstance();
            dob.set(year, month, day);
            int monthToday = today.get(Calendar.MONTH) + 1;
            int monthDOB = dob.get(Calendar.MONTH)+1;
            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            Log.d("age",age+"");

            if (age>17&&age<=100)
            {
                userage=age+"";
                if (monthDOB > monthToday)
                {

                    if((age-1)>17&&(age-1)<=100)
                    {
                        userage=(age-1)+"";
                        return true;
                    }
                    else
                    return false;
                } else if (monthDOB == monthToday)
                {
                    int todayDate = today.get(Calendar.DAY_OF_MONTH);
                    int dobDate = dob.get(Calendar.DAY_OF_MONTH);
                    if (dobDate >todayDate)
                    {
                        if((age-1)>17&&(age-1)<=100)
                    {
                        userage=(age-1)+"";
                        return true;
                    }
                    else
                        return false;

                    }
                    else
                        return true;
                } else {

                    return true;
                }

            }  else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public  void openDatePickerDialog(Context context, TextView textView)
    {
        final Calendar c = Calendar.getInstance();
        Date date=new Date();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

       /* try {
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            date = format.parse(fromDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(context,
                (view, year, monthOfYear, dayOfMonth) -> {
                   // textView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    if(textView.getId()==R.id.doj)
                    {
                        textView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        return;
                    }
                    if(getAge(year,monthOfYear,dayOfMonth))
                    {
                        textView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                    else
                    {
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "Employee Age is between 18 to 100", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        return;
                    }
                }, mYear, mMonth, mDay);
        if(!(textView.getId()==R.id.doj))
        dpd.getDatePicker().setMaxDate(date.getTime());

        dpd.show();
    }
    //

    protected void exitByBackKey() {

        final Dialog dialog = new Dialog(AddEmployee.this);
// Include dialog.xml file
        dialog.setContentView(R.layout.dialog_exit);
        dialog.show();

        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
               /* Intent intent = new Intent(AddEmployee.this, MainActivity.class);
                startActivity(intent);*/
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
    public void getDepartments()
    {

        progressDialog.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.DEPT_MNGNT, response -> {
            try {
                JSONArray jsonArray=new JSONArray(response);
                JSONObject jsonObject=jsonArray.getJSONObject(0);
                int error_code=jsonObject.getInt("error_code");
                String msg=jsonObject.getString("message");
                if (error_code == 104)
                {
                    progressDialog.dismiss();
                    Gson gson=new Gson();
                    List<DepartmentResponse>  departmentResponses= gson.fromJson(response,new TypeToken<List<DepartmentResponse>>(){}.getType());
                    if(departmentResponses.get(0).getDepartmentList().size()>0)
                    {
                        deptnames.clear();

                                for(Department department:departmentResponses.get(0).getDepartmentList())
                                {
                                    deptnames.add(department.getDepartmentName());
                                }

                                deptAdapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    progressDialog.dismiss();

                }

            } catch (JSONException e) {
                e.printStackTrace();
                progressDialog.dismiss();
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
            public void onErrorResponse(VolleyError error) {

                final PrettyDialog prettyDialog = new PrettyDialog(AddEmployee.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Connection Error")
                        .setMessage("Please try again")
                        .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                            }
                        })
                        .show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mode", "department_list");
                params.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
                Log.e("deptlist", " " + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    public void getDesignations()
    {

        progressDialog.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.DESGN_MNGNT, response -> {
            Log.e("deptlist", " " + response);
            try {
                JSONArray jsonArray=new JSONArray(response);
                JSONObject jsonObject=jsonArray.getJSONObject(0);
                int error_code=jsonObject.getInt("error_code");
                String msg=jsonObject.getString("message");
                if (error_code == 104)
                {
                    progressDialog.dismiss();
                    Gson gson=new Gson();
                    List<DesignationResponse>  designationResponses= gson.fromJson(response,new TypeToken<List<DesignationResponse>>(){}.getType());
                    if(designationResponses.get(0).getList().size()>0)
                    {
                        deptList.addAll(designationResponses.get(0).getList());
                        deptnames.clear();

                        for(DList list:designationResponses.get(0).getList())
                        {
                            deptnames.add(list.getDeptName());
                        }
                        deptAdapter.notifyDataSetChanged();
                        binding.spindept.setSelection(0);
                    }
                }
                else
                {
                    progressDialog.dismiss();

                }

            } catch (JSONException e) {
                e.printStackTrace();
                progressDialog.dismiss();
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
            public void onErrorResponse(VolleyError error) {

                final PrettyDialog prettyDialog = new PrettyDialog(AddEmployee.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Connection Error")
                        .setMessage("Please try again")
                        .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                            }
                        })
                        .show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mode", "fetch_designation");
                params.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
                Log.e("deptlist", " " + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    @Override
    public void onBackPressed() {
        exitByBackKey();
    }
}
