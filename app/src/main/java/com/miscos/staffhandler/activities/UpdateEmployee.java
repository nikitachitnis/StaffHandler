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
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
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
import android.widget.RadioButton;
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
import com.chaos.view.PinView;
import com.fivemin.chief.nonetworklibrary.networkBroadcast.NoNet;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miscos.staffhandler.MainActivity;
import com.miscos.staffhandler.Model.EmployeeListModel;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.Urls.App_Urls;
import com.miscos.staffhandler.databinding.ActivityUpdateEmployeeBinding;
import com.miscos.staffhandler.employee.Model.DList;
import com.miscos.staffhandler.employee.Model.DesignationArr;
import com.miscos.staffhandler.employee.Model.DesignationResponse;
import com.miscos.staffhandler.employee.employeemodule.Activity_PinLogin;
import com.miscos.staffhandler.employee.employeemodule.RegisterActivity;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.miscos.staffhandler.employee.helper.VolleyMultipartRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

/*Developed under Miscos
 * Developed by Nikhil
 * 06-06-2020
 * */
public class UpdateEmployee extends AppCompatActivity implements View.OnClickListener {
    RadioButton rbMale, rbFemale, rbOther, rbEmerYes, rbEmerNo, rbOffice, rbShift;
    ImageView tvBack;
    ActivityUpdateEmployeeBinding binding;
    private PreferenceManager preferenceManager;
    private EditText userName, userLName, userMobile, userAddress,edtadharNo,edtPanNo,edtdesignation,edtemail,edtpermanentAddress;
    private Button btnSubmit;
    private RadioGroup genderRadioGroup, operationRadio, emergencyRadio,rdgroupemployement_type,rdgroupsmartphn,rdgroupContractType;
    private CoordinatorLayout coordinatorLayout;
    private String smarphn_type="",contract_type="",name,desgn_id="",dept_id="", encodedImage,employee_pic,mobile,employment_type="", address,  userage="", lastname, gender, employee_id, employer_id, getCredentials, emergencyType, operationType, employeeType, allowEmergency, assign_list;
    private int maxage = 16;
    private ProgressDialog progressDialog;
    // No Internet Dialog
    private NoNet mNoNet;
    private FragmentManager fm = null;
    private CircleImageView imageEmp;
    FaceDetector detector;
    boolean isFaceDetected=false;
    private byte[] byteAddOwnerImg;
    private boolean fileStatusProfilePic;
    public static final int REQUEST_IMAGE_GALLERY = 100;
    public static final int REQUEST_IMAGE = 200;
    TextView txtchangeno,edtDOJ,userAge;
    boolean isverify;
    EmployeeListModel employeeListModel;
    CheckBox chkCheckBox;
    EditText edtemployeeNo,edtcontractype;
    TextView txtemployetype,txtcontractName;
    int count = 0,selected_dept_pos=0;
    ArrayList<String> deptnames=new ArrayList<>(),designationnames=new ArrayList<>();
    ArrayAdapter<String> designationAdapter,deptAdapter;
    List<DList> deptList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_update_employee);

        fm = getSupportFragmentManager();
        mNoNet = new NoNet();
        mNoNet.initNoNet(this, fm);
        preferenceManager = new PreferenceManager(this);
        employeeListModel=getIntent().getParcelableExtra("employee_data");
        employee_id = getIntent().getStringExtra("employee_id");
        name = getIntent().getStringExtra("name");
        lastname = getIntent().getStringExtra("lastname");
        mobile = getIntent().getStringExtra("mobile");
        address = getIntent().getStringExtra("address");
        userage = employeeListModel.getDate_of_birth();
        gender = getIntent().getStringExtra("gender");
        emergencyType = getIntent().getStringExtra("emergency_type");
        operationType = getIntent().getStringExtra("operation_type");
        assign_list = getIntent().getStringExtra("assign_list");
        employee_pic = getIntent().getStringExtra("employee_pic");
        getCredentials = getIntent().getStringExtra("send_credential");

        employer_id = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID);

        detector = new FaceDetector.Builder(UpdateEmployee.this)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();
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
        userName = findViewById(R.id.name);
        txtchangeno=findViewById(R.id.txtchangenumber);
        userLName = findViewById(R.id.Lname);
        userMobile = findViewById(R.id.mobile);
        edtemail=findViewById(R.id.edtemail);
        edtemail.setText(employeeListModel.getEmail_id());
        edtadharNo=findViewById(R.id.edtAddharno);
        edtPanNo=findViewById(R.id.edtPanNo);
        edtcontractype=findViewById(R.id.edtcontracttype);
        userAddress = findViewById(R.id.etHomeAddress);
        edtpermanentAddress=findViewById(R.id.edtPermanentAddress);
        edtpermanentAddress.setText(employeeListModel.getPermanent_address());
        userAge = findViewById(R.id.age);
        rdgroupContractType=findViewById(R.id.rdgroupContracttype);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        operationRadio = findViewById(R.id.employeeRadio);
        emergencyRadio = findViewById(R.id.allowEmergency);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtcontractName=findViewById(R.id.contractype);
        rbMale = findViewById(R.id.male);

        edtDOJ=findViewById(R.id.doj);

        rdgroupsmartphn=findViewById(R.id.rdgroupEmptype3);
        rdgroupsmartphn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
                        break;
                    case R.id.rdbtncontract:
                        employment_type="contract";

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
                        edtcontractype.setVisibility(View.GONE);

                        break;
                    case R.id.rdbtnthirdparty:
                        edtcontractype.setVisibility(View.VISIBLE);
                        contract_type="third-party";
                        break;
                }

            }
        });
        rbFemale = findViewById(R.id.female);
        rbOther = findViewById(R.id.Other);
        rbOffice = findViewById(R.id.officeStaff);
        rbEmerYes = findViewById(R.id.yes);
        rbEmerNo = findViewById(R.id.no);
        rbShift = findViewById(R.id.shiftStaff);
        tvBack = findViewById(R.id.imBack);
        imageEmp =  findViewById(R.id.empImage);
        userName.setHint(Html.fromHtml(getColoredSpanned("*", "#FF0000") + " First Name"));
        userLName.setHint(Html.fromHtml(getColoredSpanned("*", "#FF0000") + " Last Name"));
        userMobile.setHint(Html.fromHtml(getColoredSpanned("*", "#FF0000") + " Mobile"));

        //Back Button
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateEmployee.this, MainActivity.class));
                finish();
            }
        });
        userAge.setText(parseDateToddMMyyyy(employeeListModel.getDate_of_birth()));
        userAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog(UpdateEmployee.this,userAge);
            }
        });
        edtDOJ=findViewById(R.id.doj);
        edtDOJ.setText(parseDateToddMMyyyy(employeeListModel.getDate_of_joining_date()));
        edtDOJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog(UpdateEmployee.this,edtDOJ);
            }
        });
        //Progress Bar
        progressDialog = new ProgressDialog(UpdateEmployee.this);
        progressDialog = ProgressDialog.show(UpdateEmployee.this, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.dismiss();


        txtchangeno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(txtchangeno.getText().toString().equals("Change"))
                {
                    userMobile.setEnabled(true);
                    txtchangeno.setText("Verify");
                }
                else
                {
                    if(!userMobile.getText().toString().isEmpty()&&userMobile.getText().toString().length()==10)
                    {
                        if(userMobile.getText().toString().equals(mobile))
                        {
                            Toast.makeText(UpdateEmployee.this, "Please enter new mobile no & verify", Toast.LENGTH_SHORT).show();
                            return;
                        }
                       //send otp
                        callGenerateOtp(userMobile.getText().toString());
                    }
                }

            }
        });

       // Toast.makeText(this, employee_pic, Toast.LENGTH_SHORT).show();
        if (!employee_pic.isEmpty()) {
            Picasso.get()
                    .load(employee_pic)
                    .placeholder(R.drawable.loading_error)
                    .error(R.drawable.imgnot)
                    .into(imageEmp);
        } else {
            imageEmp.setImageResource(R.drawable.imgnot);
        }

        // Toast.makeText(this, operationType + emergencyType, Toast.LENGTH_SHORT).show();
        if (gender.equalsIgnoreCase("male"))
        {
            rbMale.setChecked(true);
        }
        if (gender.equalsIgnoreCase("female"))
        {
            rbFemale.setChecked(true);
        }
        if (gender.equalsIgnoreCase("other"))
        {
            rbOther.setChecked(true);
        }
        if (operationType.equalsIgnoreCase("office"))
        {
            rbOffice.setChecked(true);
        }
        if (operationType.equalsIgnoreCase("shift"))
        {
            rbShift.setChecked(true);
        }
        if (emergencyType.equalsIgnoreCase("Y"))
        {
            rbEmerYes.setChecked(true);
        }
        if (emergencyType.equalsIgnoreCase("N"))
        {
            rbEmerNo.setChecked(true);
        }
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
                        Toast.makeText(UpdateEmployee.this, "Please enter your local address", Toast.LENGTH_SHORT).show();
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
        assert getCredentials != null;
        if (getCredentials.equalsIgnoreCase("send_credentials"))
        {
            userName.setEnabled(false);
            userLName.setEnabled(false);
            userMobile.setEnabled(true);
            userAddress.setEnabled(false);
            userAge.setEnabled(false);
            rbMale.setEnabled(false);
            rbFemale.setEnabled(false);
            rbOffice.setEnabled(false);
            rbShift.setEnabled(false);
            rbEmerYes.setEnabled(false);
            rbEmerNo.setEnabled(false);
            rbOther.setEnabled(false);
            imageEmp.setEnabled(false);
            userName.setTextColor(getResources().getColor(R.color.grey_40));
            userLName.setTextColor(getResources().getColor(R.color.grey_40));
            userAddress.setTextColor(getResources().getColor(R.color.grey_40));
            userAge.setTextColor(getResources().getColor(R.color.grey_40));
        } else if (getCredentials.equalsIgnoreCase("edit"))
        {
            userName.setEnabled(true);
            userLName.setEnabled(true);
            //userMobile.setEnabled(true);
            userAddress.setEnabled(true);
            userAge.setEnabled(true);
            rbMale.setEnabled(true);
            rbFemale.setEnabled(true);
            rbOther.setEnabled(true);
            imageEmp.setEnabled(true);
            rbOffice.setEnabled(true);
            rbShift.setEnabled(true);
            rbEmerYes.setEnabled(true);
            rbEmerNo.setEnabled(true);
        }
        //set data from intent
        userName.setText(name);
        userLName.setText(lastname);
        userMobile.setText(mobile);
        userAddress.setText(address);
        userAge.setText(parseDateToddMMyyyy(userage));
        btnSubmit.setOnClickListener(this);
        imageEmp.setOnClickListener(this);
        edtadharNo.setText(employeeListModel.getAadhar_no());

        edtPanNo.setText(employeeListModel.getPanNo());
        if(employeeListModel.getIsHavingSmartPhone().equalsIgnoreCase("y"))
            rdgroupsmartphn.check(R.id.rdbtnyes_smartphn);
        else
            rdgroupsmartphn.check(R.id.rdbtnno_smartphn);

        employment_type=employeeListModel.getEmployement_type();
        if(employeeListModel.getEmployement_type().equalsIgnoreCase("payrole"))
        {
            rdgroupemployement_type.check(R.id.rdbtnpayrole);
            rdgroupContractType.setVisibility(View.GONE);
            txtcontractName.setVisibility(View.GONE);

        }
        else
        {
            rdgroupemployement_type.check(R.id.rdbtncontract);
            rdgroupContractType.setVisibility(View.VISIBLE);
            txtcontractName.setVisibility(View.VISIBLE);
        }
        txtemployetype=findViewById(R.id.employee_type);


        if(employeeListModel.getContract_type().equalsIgnoreCase("self"))
        {
            edtcontractype.setVisibility(View.GONE);
            rdgroupContractType.check(R.id.rdbtnself);

        }
        else if(employeeListModel.getContract_type().equalsIgnoreCase("third-party"))
        {
            rdgroupContractType.check(R.id.rdbtnthirdparty);
            edtcontractype.setVisibility(View.VISIBLE);
            edtcontractype.setText(employeeListModel.getContarct_name());
        }
        else
            {
                rdgroupContractType.setVisibility(View.GONE);

        }
        if(preferenceManager.getStringPreference(PreferenceManager.KEY_SHIFT_COUNT).equalsIgnoreCase("0"))
        {
            txtemployetype.setVisibility(View.GONE);
            operationRadio.setVisibility(View.GONE);
            employeeType = "office";
        }
        else
        {
            txtemployetype.setVisibility(View.VISIBLE);
            operationRadio.setVisibility(View.VISIBLE);
            employeeType =employeeListModel.getOperation_type();


        }
        operationRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int employeetype1= operationRadio.getCheckedRadioButtonId();
                switch (employeetype1)
                {
                    case R.id.officeStaff:
                        if(employeeType.equalsIgnoreCase("shift"))
                        {
                            final PrettyDialog prettyDialog = new PrettyDialog(UpdateEmployee.this);
                            prettyDialog.setCancelable(false);
                            prettyDialog
                                    .setMessage("This change will be effective from next working day,Are you sure to change?")
                                    .setIcon(R.drawable.info, R.color.primaryTextColor, null)
                                    .addButton("Yes", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            employeeType = "office";
                                            prettyDialog.dismiss();
                                        }
                                    })
                                    .addButton("No", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            prettyDialog.dismiss();
                                            operationRadio.check(R.id.shiftStaff);
                                        }
                                    })
                                    .show();
                        }

                        break;
                    case R.id.shiftStaff:
                        if(employeeType.equalsIgnoreCase("office"))
                        {
                            final PrettyDialog prettyDialog = new PrettyDialog(UpdateEmployee.this);
                            prettyDialog.setCancelable(false);
                            prettyDialog
                                    .setMessage("This change will be effective from next working day,Are you sure to change?")
                                    .setIcon(R.drawable.info, R.color.primaryTextColor, null)
                                    .addButton("Yes", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            employeeType = "shift";
                                            prettyDialog.dismiss();
                                        }
                                    })
                                    .addButton("No", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            prettyDialog.dismiss();
                                            operationRadio.check(R.id.officeStaff);
                                        }
                                    })
                                    .show();
                        }

                        break;
                }
            }
        });
getDesignations();

    }

    public String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    @Override
    public void onClick(View v)
    {
        int selectGender = genderRadioGroup.getCheckedRadioButtonId();
        switch (selectGender)
        {
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
        int emergencyDuty = emergencyRadio.getCheckedRadioButtonId();

        switch (emergencyDuty)
        {
            case R.id.yes:
                allowEmergency = "Y";
                break;
            case R.id.no:
                allowEmergency = "N";
                break;
        }
        int employeetype1= operationRadio.getCheckedRadioButtonId();
       /* switch (employeetype1)
        {
            case R.id.officeStaff:
                if(employeeType.equalsIgnoreCase("shift"))
                {
                    final PrettyDialog prettyDialog = new PrettyDialog(UpdateEmployee.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setMessage("This change will be effective from next working day,Are you sure to change?")
                            .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                            .addButton("Yes", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    employeeType = "office";
                                    prettyDialog.dismiss();
                                }
                            })
                            .addButton("No", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                    operationRadio.check(R.id.shiftStaff);
                                }
                            })
                            .show();
                }

                break;
            case R.id.shiftStaff:
                if(employeeType.equalsIgnoreCase("office"))
                {
                    final PrettyDialog prettyDialog = new PrettyDialog(UpdateEmployee.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setMessage("This change will be effective from next working day,Are you sure to change?")
                            .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                            .addButton("Yes", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    employeeType = "shift";
                                    prettyDialog.dismiss();
                                }
                            })
                            .addButton("No", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                    operationRadio.check(R.id.officeStaff);
                                }
                            })
                            .show();
                }

                break;
        }*/
        int id1 = v.getId();
        if (id1 == R.id.empImage)
        {
            dialogToSelectImage(REQUEST_IMAGE,REQUEST_IMAGE_GALLERY);
        }
        if (id1 == R.id.btnSubmit)
        {

                if (Validation()) {
                    progressDialog.show();
                    updateEmployees();
                }
        }
    }

    @Override
    protected void onResume()
    {
        mNoNet.RegisterNoNet();
        super.onResume();

    }
    @Override
    protected void onPause()
    {
        mNoNet.unRegisterNoNet();
        super.onPause();
    }
    void showOTPdialog(String OTP)
    {
        Dialog  dialog = new Dialog(UpdateEmployee.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.layout_setuppin);
        dialog.setCancelable(true);
        TextView txtlabel1=dialog.findViewById(R.id.txtlabel);
        dialog.findViewById(R.id.txtlabel2).setVisibility(View.GONE);
        Button buttonSubmit = (Button) dialog.findViewById(R.id.btn_verify);
        buttonSubmit.setVisibility(View.VISIBLE);
        PinView txtOtp=dialog.findViewById(R.id.edit_loginPin);
        dialog.findViewById(R.id.edit_loginPinreenter).setVisibility(View.GONE);
        txtlabel1.setText("Enter received OTP");
        buttonSubmit.setText("Verify");
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtOtp.getText().toString().equals(""))
                {
                    Toast.makeText(UpdateEmployee.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(txtOtp.getText().toString().equals(OTP))
                {
                    //Toast.makeText(UpdateEmployee.this, "Verified Successfully", Toast.LENGTH_SHORT).show();
                    txtchangeno.setText("verified");
                    userMobile.setEnabled(false);
                    txtchangeno.setEnabled(false);
                    isverify=true;
                    changeNo();
                    dialog.dismiss();

                }
                else
                {
                    Toast.makeText(UpdateEmployee.this, "OTP doesn't matched", Toast.LENGTH_SHORT).show();
                }

            }
        });


        dialog.show();
    }
    private void changeNo()
    {

        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());


        StringRequest stringRequest = new StringRequest(Request.Method.POST, App_Urls.Employee_Management,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {

                        progressDialog.dismiss();
//                        Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();

                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String statusCode = jsonObject.getString("error_code");
                            String msg=jsonObject.getString("message");

                            switch (statusCode) {
                                case "101":
                                    Toast.makeText(UpdateEmployee.this, "Mobile number updated succesfully", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(UpdateEmployee.this, msg, Toast.LENGTH_SHORT).show();
                                    break;

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        final PrettyDialog prettyDialog = new PrettyDialog(UpdateEmployee.this);
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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mode", "change_mobile_no");
                params.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
                params.put("employee_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEEID));
                params.put("old_mobile_no", mobile);
                params.put("new_mobile_no", userMobile.getText().toString());
                Log.e("register params is", " " + params);
                return params;
            }

        };

        requestQueue.add(stringRequest);

    }
    private void callGenerateOtp(final String mobile) {

        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());


        StringRequest stringRequest = new StringRequest(Request.Method.POST, App_Urls.VERIFY_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {

                        progressDialog.dismiss();
//                        Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String statusCode = jsonObject.getString("error_code");
                            String OTP = jsonObject.getString("otp");
                            Log.d("OTP",OTP);
                            showOTPdialog(OTP);
                            switch (statusCode) {
                                case "101":
                                    Toast.makeText(UpdateEmployee.this, "OTP sent Successfully", Toast.LENGTH_SHORT).show();
                                    break;
                                case "102":
                                    Toast.makeText(UpdateEmployee.this, "Resend OTP", Toast.LENGTH_SHORT).show();
                                    break;
                                case "103":
                                    Toast.makeText(UpdateEmployee.this, "Contact is Empty", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        final PrettyDialog prettyDialog = new PrettyDialog(UpdateEmployee.this);
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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mode", "send_otp");
                params.put("contact", mobile);
                Log.e("register params is", " " + params);
                return params;
            }

        };

        requestQueue.add(stringRequest);

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
        dpd.getDatePicker().setMaxDate(date.getTime());
        // dpd.getDatePicker().setMaxDate(new Date().getTime());
        dpd.show();
    }
    public boolean getAge(int year, int month, int day) {
        try {
            Calendar dob = Calendar.getInstance();
            Calendar today = Calendar.getInstance();
            dob.set(year, month, day);
            int monthToday = today.get(Calendar.MONTH) + 1;
            int monthDOB = dob.get(Calendar.MONTH)+1;
            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

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
    public void updateEmployees() {
        //Log.e("url is", "" + Url_Class.URL_STAFF_ENTRY_EXIT);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                App_Urls.Employee_Management, response -> {
            String resultResponse = new String(response.data);
            Log.d("update_employee", resultResponse);
            try {
                //JSONArray jsonArray = new JSONArray(resultResponse);
                JSONObject jsonObject = new JSONObject(resultResponse);
                int error_code = jsonObject.getInt("error_code");
                String msg = jsonObject.getString("message");
                if (error_code == 101) {
                    add_Dialog();
                } else if (error_code == 103) {
                    Toast.makeText(UpdateEmployee.this, msg, Toast.LENGTH_SHORT).show();
                } else if (error_code == 102) {
                    Toast.makeText(UpdateEmployee.this, msg, Toast.LENGTH_SHORT).show();
                }else if (error_code == 100) {
                    Toast.makeText(UpdateEmployee.this, msg, Toast.LENGTH_SHORT).show();
                }else if (error_code == 104) {
                    final PrettyDialog prettyDialog= new PrettyDialog(UpdateEmployee.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Employee")
                            .setMessage(msg)
                            .setIcon(R.drawable.cross,R.color.white,null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();

                                }
                            })
                            .show();
                }else if (error_code == 107) {
                    final PrettyDialog prettyDialog= new PrettyDialog(UpdateEmployee.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Employee")
                            .setMessage(msg)
                            .setIcon(R.drawable.cross,R.color.white,null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();

                                }
                            })
                            .show();
                }else if (error_code == 105) {
                    final PrettyDialog prettyDialog= new PrettyDialog(UpdateEmployee.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Employee")
                            .setMessage(msg)
                            .setIcon(R.drawable.cross,R.color.white,null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            })
                            .show();
                }else if (error_code == 106) {
                    final PrettyDialog prettyDialog= new PrettyDialog(UpdateEmployee.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Employee")
                            .setMessage(msg)
                            .setIcon(R.drawable.cross,R.color.white,null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            })
                            .show();
                }else{
                    JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                    JSONObject jsonObject1 = jsonArrayData.getJSONObject(0);
                    String name = jsonObject1.getString("name");

                    final PrettyDialog prettyDialog= new PrettyDialog(UpdateEmployee.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Org")
                            .setMessage("Employee"+ name)
                            .setIcon(R.drawable.cross,R.color.white,null)
                            .addButton("Yes", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    updateEmployees1();
                                    prettyDialog.dismiss();
                                }
                            })
                            .addButton("No", R.color.whiteTextColor, R.color.red_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {

                                    prettyDialog.dismiss();
                                }
                            })
                            .show();
                }
            } catch (JSONException ex) {
                Log.e("Check", "JSONEXCEPTION"+ex);
                ex.printStackTrace();
                final Dialog dialog1 = new Dialog(UpdateEmployee.this);
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
            Log.d("RESPONSE", resultResponse);
        }, error -> {
            progressDialog.dismiss();
            final PrettyDialog prettyDialog = new PrettyDialog(UpdateEmployee.this);
            prettyDialog.setCancelable(false);
            prettyDialog
                    .setTitle("Connection Error")
                    .setMessage("Please try again")
                    .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                    .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            prettyDialog.dismiss();
                            startActivity(new Intent(UpdateEmployee.this,MainActivity.class));
                            finish();
                        }
                    })
                    .show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mode", "update");
                params.put("employer_id", employer_id);
                params.put("employee_id", employee_id);
                params.put("first_name", userName.getText().toString());
                params.put("last_name", userLName.getText().toString());
                params.put("contact", userMobile.getText().toString());
                params.put("address", userAddress.getText().toString());
                params.put("age", userAge.getText().toString());
                params.put("gender", gender);
                params.put("designation",desgn_id);
                params.put("department_id",dept_id);
                params.put("emergency_duty", allowEmergency);
                params.put("operation_type", employeeType);
                if (assign_list.equalsIgnoreCase("null")) {
                    params.put("shift_assign_list", "");
                }
                params.put("shift_assign_list", assign_list); params.put("gender", gender);
                params.put("date_of_birth", userAge.getText().toString());

                params.put("pan_no",edtPanNo.getText().toString());
                params.put("employment_type", employment_type);
                params.put("parment_address", edtpermanentAddress.getText().toString());
                params.put("employee_no", employeeListModel.getEmployee_no());
                params.put("having_smart_phone", smarphn_type);
                params.put("email_id", edtemail.getText().toString());
                params.put("contract_type",contract_type);
                params.put("contract_company_name",edtcontractype.getText().toString());
                params.put("save_flag", "save");
                Log.e("update params", " " + params);

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
               // Log.d("TAG", "getImageInByteArray: encoded12" + encodedImage);
                if (!fileStatusProfilePic){
                    byteAddOwnerImg = null;
                }else{
                    String imagename = "employeeImage";
                    params.put("employee_pic", new DataPart(imagename + ".png", byteAddOwnerImg));
                }
                Log.d("params is", " " + params);;
                return params;
            }
        };
        multipartRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 10, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(multipartRequest);
            }

    public void updateEmployees1()
    {
        //Log.e("url is", "" + Url_Class.URL_STAFF_ENTRY_EXIT);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                App_Urls.Employee_Management, response -> {
            String resultResponse = new String(response.data);
            Log.d("response", resultResponse);
            try {
                JSONArray jsonArray = new JSONArray(resultResponse);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                int error_code = jsonObject.getInt("error_code");
                String msg = jsonObject.getString("message");
                if (error_code == 101) {
                    add_Dialog();
                } else if (error_code == 103) {
                    Toast.makeText(UpdateEmployee.this, msg, Toast.LENGTH_SHORT).show();
                } else if (error_code == 102) {
                    Toast.makeText(UpdateEmployee.this, msg, Toast.LENGTH_SHORT).show();
                }else if (error_code == 100) {
                    Toast.makeText(UpdateEmployee.this, msg, Toast.LENGTH_SHORT).show();
                }else if (error_code == 104) {
                    final PrettyDialog prettyDialog= new PrettyDialog(UpdateEmployee.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Employee")
                            .setMessage(msg)
                            .setIcon(R.drawable.cross,R.color.white,null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();

                                }
                            })
                            .show();
                }else if (error_code == 105) {
                    final PrettyDialog prettyDialog= new PrettyDialog(UpdateEmployee.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Employee")
                            .setMessage(msg)
                            .setIcon(R.drawable.cross,R.color.white,null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            })
                            .show();
                }else if (error_code == 106) {
                    final PrettyDialog prettyDialog= new PrettyDialog(UpdateEmployee.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Employee")
                            .setMessage(msg)
                            .setIcon(R.drawable.cross,R.color.white,null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            })
                            .show();
                }
                else if (error_code == 107) {
                    final PrettyDialog prettyDialog= new PrettyDialog(UpdateEmployee.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Employee")
                            .setMessage(msg)
                            .setIcon(R.drawable.cross,R.color.white,null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();


                                }
                            })
                            .show();
                }else{
                    final PrettyDialog prettyDialog= new PrettyDialog(UpdateEmployee.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Org")
                            .setMessage("Employee"+name)
                            .setIcon(R.drawable.cross,R.color.white,null)
                            .addButton("Yes", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            })
                            .addButton("No", R.color.whiteTextColor, R.color.red_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            })
                            .show();
                }
            } catch (JSONException ex) {
                Log.e("Check", "JSONEXCEPTION"+ex);
                ex.printStackTrace();
                final Dialog dialog1 = new Dialog(UpdateEmployee.this);
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
                        startActivity(new Intent(UpdateEmployee.this,MainActivity.class));
                        finish();
                    }
                });
            }
            progressDialog.dismiss();
            Log.d("RESPONSE", resultResponse);
        }, error -> {
            progressDialog.dismiss();
            final PrettyDialog prettyDialog = new PrettyDialog(UpdateEmployee.this);
            prettyDialog.setCancelable(false);
            prettyDialog
                    .setTitle("Connection Error")
                    .setMessage("Please try again")
                    .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                    .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            prettyDialog.dismiss();
                            startActivity(new Intent(UpdateEmployee.this,MainActivity.class));
                            finish();
                        }
                    })
                    .show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mode", "update");
                params.put("employer_id", employer_id);
                params.put("employee_id", employee_id);
                params.put("first_name", userName.getText().toString());
                params.put("last_name", userLName.getText().toString());
                params.put("contact", userMobile.getText().toString());
                params.put("address", userAddress.getText().toString());
                params.put("age", userMobile.getText().toString());
                params.put("gender", gender);
                params.put("date_of_birth", userAge.getText().toString());

                params.put("pan_no",edtPanNo.getText().toString());
                params.put("employment_type", employment_type);
                params.put("emergency_duty", allowEmergency);
                params.put("operation_type", employeeType);
                if (assign_list.equalsIgnoreCase("null")) {
                    params.put("shift_assign_list", "");
                }
                params.put("shift_assign_list", assign_list);


                params.put("date_of_joining", edtDOJ.getText().toString());

                params.put("designation",desgn_id);
                params.put("department_id",dept_id);
                params.put("parment_address", edtpermanentAddress.getText().toString());
                params.put("operation_type", employeeType);
                params.put("employee_no", employeeListModel.getEmployee_no());
                params.put("having_smart_phone", smarphn_type);
                params.put("email_id", edtemail.getText().toString());
                params.put("contract_type",contract_type);
                params.put("contract_company_name",edtcontractype.getText().toString());
                params.put("save_flag", "continue");
                Log.e("update params", " " + params);


                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                //Log.d("TAG", "getImageInByteArray: encoded12" + encodedImage);
                if (!fileStatusProfilePic){
                    byteAddOwnerImg = null;
                }else{
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


    public void dialogToSelectImage(final int requestImageCamera, final int requestImageGallary) {

        LayoutInflater factory = LayoutInflater.from(this);
        final View dialogToSelectImage = factory.inflate(R.layout.dialog_selectphoto, null);
        final AlertDialog selectImage = new AlertDialog.Builder(this).create();
        selectImage.setView(dialogToSelectImage);

        TextView fromGallary = dialogToSelectImage.findViewById(R.id.dialogChoosePhoto); //
        TextView fromCamera = dialogToSelectImage.findViewById(R.id.dialogOpenCamera); //
        TextView cancel = dialogToSelectImage.findViewById(R.id.cancel_dialog); //

        fromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int request = requestImageCamera;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(UpdateEmployee.this.getPackageManager()) != null) {
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
                    getImageInByteArray(profileBitmap);
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
                    getImageInByteArray(bitmapProfiles);
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
            BitmapFactory.decodeStream(UpdateEmployee.this.getContentResolver().openInputStream(selectedImage), null, o);

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
            return BitmapFactory.decodeStream(UpdateEmployee.this.getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    void faceDetection(Bitmap bitmap)
    {

        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Face> faces = detector.detect(frame);
        if(faces.size()==1)
        {
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
        }
        else if(faces.size()>1)
        {
            isFaceDetected=false;
            Toast.makeText(this, "More than one face detected , please select/capture another image", Toast.LENGTH_SHORT).show();
        }
        else
        {
            isFaceDetected=false;
            Toast.makeText(this, "No face detected , please select/capture another image", Toast.LENGTH_SHORT).show();
        }

    }

    private void add_Dialog()
    {
        final PrettyDialog prettyDialog = new PrettyDialog(UpdateEmployee.this);
        prettyDialog.setCancelable(false);
        prettyDialog
                .setTitle("Update")
                .setMessage("Employee Updated Successfully")
                .setIcon(R.drawable.check, R.color.primaryTextColor, null)
                .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        prettyDialog.dismiss();
                        startActivity(new Intent(UpdateEmployee.this, MainActivity.class));
                        finish();
                    }
                })
                .show();

    }

    public boolean Validation()
    {
        name = userName.getText().toString();
        mobile = userMobile.getText().toString();
        address = userAddress.getText().toString();
        lastname = userLName.getText().toString();
        userage = userAge.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Enter First Name", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }

        if (TextUtils.isEmpty(lastname)) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Enter Last Name", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }

        if (!userMobile.getText().toString().matches("^[6-9][0-9]{9}$")) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Enter Valid Mobile Number", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }

if(!userMobile.getText().toString().equals(mobile)&&!isverify)
{  Snackbar snackbar = Snackbar
        .make(coordinatorLayout, "Please verify mobile number", Snackbar.LENGTH_LONG);
    snackbar.show();
    return false;

}

        //
        if (!TextUtils.isEmpty(edtemail.getText().toString()) )
        {
            if (!Patterns.EMAIL_ADDRESS.matcher(edtemail.getText().toString()).matches())
            {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Enter Valid Email ID", Snackbar.LENGTH_LONG);
                snackbar.show();
                return false;
            }

        }
        if (!edtPanNo.getText().toString().isEmpty())
        {
            // get your editext value here
            Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
            Matcher matcher = pattern.matcher(edtPanNo.getText().toString());
            // Check if pattern matches
            if (!matcher.matches()) {

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

        //
        return true;
    }

    protected void exitByBackKey() {

        final Dialog dialog = new Dialog(UpdateEmployee.this);
// Include dialog.xml file
        dialog.setContentView(R.layout.dialog_exit);
        dialog.show();

        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(UpdateEmployee.this, MainActivity.class);
                startActivity(intent);
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
                        binding.spindept.setSelection(deptnames.indexOf(employeeListModel.getDepartment_name()));
                        dept_id=employeeListModel.getDepartment_id();
                        desgn_id=employeeListModel.getDesignation();
                        designationnames.clear();
                        for(DesignationArr designationArr:deptList.get(deptnames.indexOf(employeeListModel.getDepartment_name())).getDesignationArr())
                        {
                            designationnames.add(designationArr.getName());
                        }
                        designationAdapter.notifyDataSetChanged();
                        binding.spindesign.setSelection(designationnames.indexOf(employeeListModel.getDesignation_name())-1);

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

                final PrettyDialog prettyDialog = new PrettyDialog(UpdateEmployee.this);
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
