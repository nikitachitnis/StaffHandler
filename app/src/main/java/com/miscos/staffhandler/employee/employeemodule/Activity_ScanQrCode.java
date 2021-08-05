package com.miscos.staffhandler.employee.employeemodule;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.miscos.staffhandler.MainActivity;
import com.miscos.staffhandler.R;

import com.miscos.staffhandler.Urls.App_Urls;
import com.miscos.staffhandler.activities.StartActivity;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.miscos.staffhandler.employee.network.LocationTrack;
import com.fivemin.chief.nonetworklibrary.networkBroadcast.NoNet;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import static com.miscos.staffhandler.Urls.App_Urls.EMPLOYEE_ATTENDANCE;

/*Developed under Miscos
 * Developed by Nikhil
 * 05-06-2020
 * */
public class Activity_ScanQrCode extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    LocationTrack locationTrack;
    private String qrString, employerId, employeeId,shiftNo;
    String statusCode;
    String day_Status;
    String dayStatus;
    String setAttendance,wifinames,employeeAttendance;
    String latLong;
    private ProgressDialog progressDialog;
    PreferenceManager preferenceManager;
    String msg,msg1;
    String call_from="";
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    // No Internet Dialog
    public static final String CALL_FROM = "call_from";
    private NoNet mNoNet;
    private FragmentManager fm = null;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanqrcode);
        fm = getSupportFragmentManager();
        mNoNet = new NoNet();
        mNoNet.initNoNet(this, fm);

        //App Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //  checkAndRequestPermissions();
            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    //Toast.makeText(StartActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPermissionDenied(List<String> deniedPermissions) {
                    Toast.makeText(Activity_ScanQrCode.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT)
                            .show();
                }


            };

            TedPermission.with(this)
                    .setPermissionListener(permissionlistener)
                    .setRationaleTitle(R.string.rationale_title)
                    .setRationaleMessage(R.string.rationale_message)
                    .setDeniedTitle("Permission denied")
                    .setDeniedMessage(
                            "If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setGotoSettingButtonText("Ok")
                    .setPermissions(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                    .check();

        }
        preferenceManager=new PreferenceManager(Activity_ScanQrCode.this);

        Intent callingIntent=getIntent();
        if(callingIntent!=null)
        {
            call_from=callingIntent.getStringExtra(CALL_FROM);
            if(call_from==null)
                call_from="";
        }
        //Progress Bar
        progressDialog = new ProgressDialog(Activity_ScanQrCode.this);
        progressDialog = ProgressDialog.show(Activity_ScanQrCode.this, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.dismiss();


        employerId = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID);
        employeeId = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEEID);
        shiftNo = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_SHIFTS);
        if(!preferenceManager.getStringPreference(PreferenceManager.KEY_ATTENDANCETYPE).equalsIgnoreCase("qr"))
        Toast.makeText(this, "Detected in office location", Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        day_Status = intent.getStringExtra("Day");
        employeeAttendance = intent.getStringExtra("attendance_type");
        wifinames = intent.getStringExtra("wifinames");
        //Toast.makeText(this, day_Status, Toast.LENGTH_SHORT).show();
        startScanning();

    }

    //QR Scanning
    private void startScanning() {
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Toast.makeText(Activity_ScanQrCode.this, result.getText(), Toast.LENGTH_SHORT).show();
                        qrString = result.getText();
                        callMarkatendancewithQr(day_Status);
                    }
                });

            }


        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

    }



    //Attendance mark with QR
    private void callMarkatendancewithQr(final String day_Status) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, App_Urls.EMPLOYEE_QRSCAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.d("attendance response", " " + response);
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            statusCode = jsonObject.getString("status");
                            msg1 = jsonObject.getString("message");

                            switch (statusCode) {
                                case "101":
                                    callEmployeeAttendance1(day_Status);
                                    break;
                                case "100":
                                    final PrettyDialog prettyDialog = new PrettyDialog(Activity_ScanQrCode.this);
                                    prettyDialog
                                            .setTitle("QR Code")
                                            .setMessage("QR Not Valid")
                                            .setIcon(R.drawable.cross)
                                            .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                                @Override
                                                public void onClick() {
                                                    prettyDialog.dismiss();
                                                    callIntent();
                                                }
                                            })
                                            .show();

                                    break;
                                case "106":
                                    Toast.makeText(Activity_ScanQrCode.this, msg1, Toast.LENGTH_SHORT).show();
                                    callIntent();
                                    break;
                                default:
                                    callIntent();
                                    Toast.makeText(Activity_ScanQrCode.this, msg1, Toast.LENGTH_SHORT).show();
                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            final PrettyDialog prettyDialog = new PrettyDialog(Activity_ScanQrCode.this);
                            prettyDialog.setCancelable(false);
                            prettyDialog
                                    .setTitle("Connection Error")
                                    .setMessage("Please try again")
                                    .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                                    .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            prettyDialog.dismiss();
                                            callIntent();
                                        }
                                    })
                                    .show();
                    }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_SPLIT_TOUCH);
                final PrettyDialog prettyDialog = new PrettyDialog(Activity_ScanQrCode.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Connection Error")
                        .setMessage("Please try again")
                        .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                                startActivity(new Intent(Activity_ScanQrCode.this,EmployeeActivity.class));
                                finish();
                            }
                        })
                        .show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if (Activity_ScanQrCode.this.day_Status.equals("day_in")) {
                    params.put("mode", "day_in");
                }
                if (Activity_ScanQrCode.this.day_Status.equals("day_out")) {
                    params.put("mode", "day_out");
                }

                params.put("employer_id", employerId);
                params.put("employee_id", employeeId);
                params.put("force", "0");
                params.put("qrstring", qrString);
               Log.d("qrscan param is", " " + params);

                return params;
            }

        };

        requestQueue.add(stringRequest);
    }

    //Attendance mark
    public void callEmployeeAttendance1(final String day_status) {

        progressDialog.show();

        if(day_status.equals("day_in"))
        {
            dayStatus="Day In";

        }
        else if(day_status.equals("day_out"))
        {
            dayStatus="Day Out";
        }
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());


        StringRequest stringRequest = new StringRequest(Request.Method.POST, EMPLOYEE_ATTENDANCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("attendance", " " + response);
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            statusCode = jsonObject.getString("error_code");
                            msg = jsonObject.getString("message");
                            if(statusCode.equals("101")) {
                                preferenceManager.setPreference(PreferenceManager.KEY_ATTENDANCE,setAttendance);
                                final PrettyDialog prettyDialog= new PrettyDialog(Activity_ScanQrCode.this);
                                prettyDialog
                                        .setTitle("Day Status")
                                        .setMessage(dayStatus + " Marked Successfully")
                                        .setIcon(R.drawable.right)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog.dismiss();
                                                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_STATUS,dayStatus.replace("Day",""));
                                                callIntent();
                                            }
                                        })
                                        .show();

                            }else if (statusCode.equals("104")) {
                                preferenceManager.setPreference(PreferenceManager.KEY_ATTENDANCE,setAttendance);
                                final PrettyDialog prettyDialog= new PrettyDialog(Activity_ScanQrCode.this);
                                prettyDialog
                                        .setTitle("Day Status")
                                        .setMessage(dayStatus + " Marked Successfully")
                                        .setIcon(R.drawable.right)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog.dismiss();
                                                preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_STATUS,dayStatus.replace("Day",""));
                                                callIntent();
                                            }
                                        })
                                        .show();

                            }else if (statusCode.equals("107")) {
                                final PrettyDialog prettyDialog= new PrettyDialog(Activity_ScanQrCode.this);

                                prettyDialog
                                        .setTitle("Day Status")
                                        .setMessage("Mark" + dayStatus +"First")
                                        .setIcon(R.drawable.cross)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog.dismiss();
                                                callIntent();
                                            }
                                        })
                                        .show();
                            }else if(statusCode.equals("106")) {
                                final PrettyDialog prettyDialog= new PrettyDialog(Activity_ScanQrCode.this);

                                prettyDialog
                                        .setTitle("Day Status")
                                        .setMessage(dayStatus + " Already Marked")
                                        .setIcon(R.drawable.cross)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog.dismiss();
                                                callIntent();
                                            }
                                        })
                                        .show();

                            } else  if(statusCode.equals("100")) {
                                callIntent();
                            }else if(statusCode.equals("110")) {
                                final PrettyDialog prettyDialog= new PrettyDialog(Activity_ScanQrCode.this);

                                prettyDialog
                                        .setTitle("Day Status")
                                        .setMessage(msg)
                                        .setIcon(R.drawable.cross)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog.dismiss();
                                                callIntent();
                                            }
                                        })
                                        .show();


                            }else{
                                final PrettyDialog prettyDialog= new PrettyDialog(Activity_ScanQrCode.this);
                                prettyDialog
                                        .setTitle("Day Status")
                                        .setMessage(msg)
                                        .setIcon(R.drawable.cross)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog.dismiss();
                                                callIntent();
                                            }
                                        })
                                        .show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            final PrettyDialog prettyDialog = new PrettyDialog(Activity_ScanQrCode.this);
                            prettyDialog.setCancelable(false);
                            prettyDialog
                                    .setTitle("Connection Error")
                                    .setMessage("Please try again")
                                    .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                                    .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            prettyDialog.dismiss();
                                            callIntent();
                                        }
                                    })
                                    .show();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        final PrettyDialog prettyDialog = new PrettyDialog(Activity_ScanQrCode.this);
                        prettyDialog.setCancelable(false);
                        prettyDialog
                                .setTitle("Connection Error")
                                .setMessage("Please try again")
                                .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                                .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                    @Override
                                    public void onClick() {
                                        prettyDialog.dismiss();
                                        callIntent();
                                    }
                                })
                                .show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (day_status.equals("day_in")) {
                    params.put("mode", "day_in");
                }
                if (day_status.equals("day_out")) {
                    params.put("mode", "day_out");
                }
                params.put("force", "0");
                if(call_from.equalsIgnoreCase("notification"))
                params.put("request_from", "notification");

                params.put("employer_id", employerId);

                params.put("employee_id", employeeId);
                params.put("operation_type", preferenceManager.getStringPreference(PreferenceManager.KEY_OPERATION_TYPE));

                if (preferenceManager.getStringPreference(PreferenceManager.KEY_OPERATION_TYPE).equalsIgnoreCase("office")) {
                    params.put("shift_no", "");
                }else {
                    params.put("shift_no", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_SHIFTS));
                }
                params.put("employee_lat_long","");

                params.put("type_of_attendance", employeeAttendance);

                params.put("wifi_name", "");

                Log.d("attendance", " " + params + EMPLOYEE_ATTENDANCE);
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }
    private void callIntent() {

        finish();
    }

    @Override
    protected void onResume() {
        mNoNet.RegisterNoNet();
        super.onResume();
        if (mCodeScanner != null) {
            mCodeScanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        mNoNet.unRegisterNoNet();
        if (mCodeScanner != null) {
            mCodeScanner.releaseResources();
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(Activity_ScanQrCode.this);
// Include dialog.xml file
        dialog.setContentView(R.layout.cancel_qr);
        dialog.show();

        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Activity_ScanQrCode.this,EmployeeActivity.class);
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
