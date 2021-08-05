package com.miscos.staffhandler.employee.employeemodule;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.Urls.App_Urls;
import com.miscos.staffhandler.activities.StartActivity;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.fivemin.chief.nonetworklibrary.networkBroadcast.NoNet;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
/*Developed under Miscos
 * Developed by Nikhil
 * 05-06-2020
 * */
public class Activity_SetupPin extends AppCompatActivity {

    Button btn_Setup;
    //    TextInputLayout textInput_password;
    PinView textInput_Loginpin;
    PinView textInput_Loginpinreenter;

    String statusCode;
    String msg;
    String userID;
    String user_name;
    String user_pass;
    private ProgressDialog progressDialog;

    PreferenceManager preferenceManager;
    // No Internet Dialog
    private NoNet mNoNet;
    private FragmentManager fm = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setuppin);
        fm = getSupportFragmentManager();
        mNoNet = new NoNet();
        mNoNet.initNoNet(this, fm);
        preferenceManager = new PreferenceManager(Activity_SetupPin.this);

        btn_Setup = findViewById(R.id.btn_Setup);
        Bitmap wpIcon = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);



        textInput_Loginpin = findViewById(R.id.edit_loginPin);
        textInput_Loginpinreenter = findViewById(R.id.edit_loginPinreenter);

        //Progress Bar
        progressDialog = new ProgressDialog(Activity_SetupPin.this);
        progressDialog = ProgressDialog.show(Activity_SetupPin.this, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.dismiss();

        user_name = preferenceManager.getStringPreference(PreferenceManager.KEY_USERNAME);
        user_pass = preferenceManager.getStringPreference(PreferenceManager.KEY_PASSWORD);


        btn_Setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pin1 = textInput_Loginpin.getText().toString();
                String pin2 = textInput_Loginpinreenter.getText().toString();
                if (TextUtils.isEmpty(pin1) && TextUtils.isEmpty(pin2)) {
                    Toast.makeText(Activity_SetupPin.this, "Enter Pin", Toast.LENGTH_SHORT).show();
                }else if (pin1.equals(pin2)) {
                    callSetupPin();
                } else {
                    Toast.makeText(Activity_SetupPin.this, "Login Pin Not Match", Toast.LENGTH_SHORT).show();
                }

//                Intent intent = new Intent(Activity_SetupPin.this,Activity_PinLogin.class);
//                startActivity(intent);
//                finish();


            }
        });
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
    private void callSetupPin() {

        progressDialog.show();
        // String url = "http://premisafe.com/mini_staff_handler/index.php/verify/verify";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final String loginpin = textInput_Loginpin.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, App_Urls.VERIFY_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("setup pin", " " + response);
                        progressDialog.dismiss();


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            statusCode = jsonObject.getString("error_code");
                            msg = jsonObject.getString("message");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        if (statusCode.equals("103")) {
                            final PrettyDialog prettyDialog= new PrettyDialog(Activity_SetupPin.this);
                            prettyDialog.setCancelable(false);
                            prettyDialog
                                    .setTitle("Incorrect")
                                    .setMessage(msg)
                                    .setIcon(R.drawable.cross)
                                    .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            prettyDialog.dismiss();
                                        }
                                    })
                                    .show();
                        }

                        switch (statusCode) {
                            case "100":
                                Toast.makeText(Activity_SetupPin.this, "ParameterMissing", Toast.LENGTH_SHORT).show();
                                break;
                            case "101":
                                Intent intent = new Intent(Activity_SetupPin.this, Activity_PinLogin.class);
                                String setuppin = textInput_Loginpin.getText().toString();
                                preferenceManager.setPreference(PreferenceManager.KEY_SET_UP_PIN, setuppin);
                                startActivity(intent);
                                finish();
                                break;
                            case "102":
                                Toast.makeText(Activity_SetupPin.this, msg, Toast.LENGTH_SHORT).show();
                                break;
                            case "105":
                                Toast.makeText(Activity_SetupPin.this, msg, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(Activity_SetupPin.this, msg, Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Toast.makeText(Activity_SetupPin.this,R.string.connection_error, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mode", "setup");
                params.put("login_pin", loginpin);
                params.put("device_os", "android");
                params.put("username", user_name);
                params.put("password", user_pass);
                Log.e("setup pin params is", " " + params);
                return params;
            }

        };

        requestQueue.add(stringRequest);


    }

    @Override
    public void onBackPressed() {
        exitByBackKey();
    }

    //Exit App
    protected void exitByBackKey() {

        final Dialog dialog = new Dialog(Activity_SetupPin.this);
// Include dialog.xml file
        dialog.setContentView(R.layout.cancel_registration);
        dialog.show();

        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_SetupPin.this, StartActivity.class);
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

}
