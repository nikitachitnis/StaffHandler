package com.miscos.staffhandler.employee.employeemodule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fivemin.chief.nonetworklibrary.networkBroadcast.NoNet;
import com.google.android.material.textfield.TextInputLayout;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.Urls.App_Urls;
import com.miscos.staffhandler.activities.StartActivity;
import com.miscos.staffhandler.employee.helper.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

/*Developed under Miscos
 * Developed by Nikhil
 * 05-06-2020
 * */
public class RegisterActivity extends AppCompatActivity {


    Button btn_register;
    TextView txt_generate, txtVerify;

    TextInputLayout text_inputUsername;
    TextInputLayout text_inputMobile;
    TextInputLayout text_inputPassword;
    TextInputLayout text_inputOtp;
    TextInputLayout etDevice_id;
    CheckBox cbTerms;
    TextView tvTerms;
    EditText username, mobilenumber, userpassword, etOtp;
    PreferenceManager preferenceManager;
    int valid = 0;

    String statusCode;
    String verifystatusCode, user_name, user_pass, setup_pin, message;
    String msg;
    String OTP;
    String mobile, name, otp, password;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    LocationManager locationManager;
    private ProgressDialog progressDialog;
    // No Internet Dialog
    private NoNet mNoNet;
    private FragmentManager fm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fm = getSupportFragmentManager();
        mNoNet = new NoNet();
        mNoNet.initNoNet(this, fm);
        preferenceManager = new PreferenceManager(RegisterActivity.this);
        text_inputPassword = findViewById(R.id.editPassword);
        text_inputMobile = findViewById(R.id.editMobileno);

        text_inputUsername = findViewById(R.id.editUsername);
        text_inputOtp = findViewById(R.id.editOtp);
        etDevice_id = findViewById(R.id.etDeviceid);

        txt_generate = findViewById(R.id.btn_Generate);
        txtVerify = findViewById(R.id.btnVerify);
        btn_register = findViewById(R.id.btn_Login);
        cbTerms = findViewById(R.id.cbTerms);
        tvTerms = findViewById(R.id.tvTerms);
        cbTerms.setChecked(false);

        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://staffhandler.com/production/mini_staff_handler/admin_panel/tnc.html";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog = ProgressDialog.show(RegisterActivity.this, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.dismiss();

        text_inputUsername.setEnabled(false);
        text_inputPassword.setEnabled(false);
        text_inputOtp.setEnabled(false);
        txtVerify.setEnabled(false);
        etDevice_id.setEnabled(false);


        // No Internet Dialog


        user_name = preferenceManager.getStringPreference(PreferenceManager.KEY_USERNAME);
        user_pass = preferenceManager.getStringPreference(PreferenceManager.KEY_PASSWORD);
        setup_pin = preferenceManager.getStringPreference(PreferenceManager.KEY_SET_UP_PIN);


        if (!user_name.equals("") && !setup_pin.equals("")) {
            Intent intent = new Intent(RegisterActivity.this, Activity_PinLogin.class);
            startActivity(intent);
            finish();
        }

        txt_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mobile = text_inputMobile.getEditText().getText().toString();
                if (TextUtils.isEmpty(mobile)) {
                    Toast.makeText(RegisterActivity.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                } else if (!mobile.matches("^[6-9][0-9]{9}$")) {
                    Toast.makeText(RegisterActivity.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                } else {
                    text_inputOtp.setEnabled(true);
                    txtVerify.setEnabled(true);
                    callGenerateOtp(mobile);
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {
                    userRegistration();
                }
            }
        });

        txtVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = text_inputOtp.getEditText().getText().toString();
                if (otp.equals(OTP)) {
                    text_inputMobile.setEnabled(false);
                    text_inputOtp.setEnabled(false);
                    text_inputUsername.setEnabled(true);
                    text_inputPassword.setEnabled(true);
                    etDevice_id.setEnabled(true);
                    Toast.makeText(RegisterActivity.this, "Contact Verified Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Entered OTP is Wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void userRegistration() {

        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        preferenceManager.setPreference(PreferenceManager.KEY_USERNAME, text_inputUsername.getEditText().getText().toString());
        preferenceManager.setPreference(PreferenceManager.KEY_PASSWORD, text_inputPassword.getEditText().getText().toString());
        preferenceManager.setPreference(PreferenceManager.KEY_MOBILE_NUMBER, text_inputMobile.getEditText().getText().toString());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, App_Urls.VERIFY_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("register_response", " " + response);
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            verifystatusCode = jsonObject.getString("error_code");
                            message = jsonObject.getString("message");
                            //OTP = jsonObject.getString("otp");
                            Log.d("OTP",otp);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        switch (verifystatusCode) {
                            case "101":
                                preferenceManager.setPreference(PreferenceManager.KEY_MOBILE_NUMBER,mobile);
                                final PrettyDialog prettyDialog = new PrettyDialog(RegisterActivity.this);
                                prettyDialog.setCancelable(false);
                                prettyDialog
                                        .setTitle("Verified")
                                        .setMessage("Verified Successfully")
                                        .setIcon(R.drawable.check)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                Intent intent = new Intent(RegisterActivity.this, Activity_SetupPin.class);
                                                startActivity(intent);
                                                finish();
                                                prettyDialog.dismiss();
                                            }
                                        })
                                        .show();
                                break;
                            case "100":
                                Toast.makeText(RegisterActivity.this, "Parameter Missing", Toast.LENGTH_SHORT).show();
                                break;
                            case "102":
                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                break;
                            case "103":
                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                break;
                            case "104":
                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                break;
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        final PrettyDialog prettyDialog = new PrettyDialog(RegisterActivity.this);
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
                params.put("mode", "verify");
                params.put("contact", mobile);
                params.put("username", text_inputUsername.getEditText().getText().toString());
               // params.put("device_id", "");
                params.put("password", text_inputPassword.getEditText().getText().toString());
                Log.e("verify params is", " " + params);
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
                            statusCode = jsonObject.getString("error_code");
                            OTP = jsonObject.getString("otp");
                            Log.d("OTP",OTP);
                            switch (statusCode) {
                                case "101":
                                    txt_generate.setEnabled(false);

                                    Toast.makeText(RegisterActivity.this, "OTP sent Successfully", Toast.LENGTH_SHORT).show();
                                    break;
                                case "102":
                                    Toast.makeText(RegisterActivity.this, "Resend OTP", Toast.LENGTH_SHORT).show();
                                    break;
                                case "103":
                                    Toast.makeText(RegisterActivity.this, "Contact Is Empty", Toast.LENGTH_SHORT).show();
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
                        final PrettyDialog prettyDialog = new PrettyDialog(RegisterActivity.this);
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

    //Validation for number verification
    public boolean validation() {
        name = text_inputUsername.getEditText().getText().toString();
        mobile = text_inputMobile.getEditText().getText().toString();
        otp = text_inputOtp.getEditText().getText().toString();
        String deviceID = etDevice_id.getEditText().getText().toString();
        password = text_inputPassword.getEditText().getText().toString();

        if (mobile.length() == 0 || !mobile.matches("^[6-9][0-9]{9}$")) {
            text_inputMobile.setError("Enter Mobile Number");
            return false;

        }
        if (otp.length() == 0) {
            text_inputOtp.setError("Please Enter OTP");
            return false;
        }

        if (name.length() == 0) {
            text_inputUsername.setError("Please enter Username");
            // Toast.makeText(SignUpActivity.this,"Please enter valid phone number",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            text_inputUsername.setError(null);
        }

        if (password.length() == 0) {
            text_inputPassword.setError("Please Enter Password");
            return false;
        } else {
            text_inputPassword.setError(null);
        }
       /* if (deviceID.length() == 0) {
            etDevice_id.setError("Please Enter Device id");
            return false;
        } else {
            etDevice_id.setError(null);
        }*/
        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Please Agree with Terms and Condition", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, StartActivity.class));
        finish();
    }
}

