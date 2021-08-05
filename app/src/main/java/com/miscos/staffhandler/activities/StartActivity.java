package com.miscos.staffhandler.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fivemin.chief.nonetworklibrary.networkBroadcast.NoNet;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.employee.employeemodule.RegisterActivity;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.miscos.staffhandler.employer_java.activity.MainActivity;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

/*Developed under Miscos
 * Developed by Nikhil
 * 04-06-2020
 * */

public class StartActivity extends AppCompatActivity {
    TextView tvEmployee, tvEmployer;

    String user_name;
    String user_pass;
    String set_pin;
    PreferenceManager preferenceManager;
    PackageInfo pinfo;
    int versionNumber;
    TextView tvVersion;
    String versionName, latestVersion;
    // No Internet Dialog
    private NoNet mNoNet;
    private FragmentManager fm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        fm = getSupportFragmentManager();
        mNoNet = new NoNet();
        mNoNet.initNoNet(this, fm);
        tvEmployee = findViewById(R.id.tvEmployee);
        tvEmployer = findViewById(R.id.tvEmployer);
        tvVersion = findViewById(R.id.tvVersion);
        preferenceManager = new PreferenceManager(StartActivity.this);

        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        versionNumber = pinfo.versionCode;
        versionName = pinfo.versionName;
        tvVersion.setText("Version " + versionName);
        Log.d("TAG", "onCreate: " + versionName);


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
                    Toast.makeText(StartActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT)
                            .show();
                }


            };

            //App Permission
            TedPermission.with(this)
                    .setPermissionListener(permissionlistener)
                    .setRationaleTitle(R.string.rationale_title)
                    .setRationaleMessage(R.string.rationale_message)
                    .setDeniedTitle("Permission denied")
                    .setDeniedMessage(
                            "If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setGotoSettingButtonText("Ok")
                    .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                    .check();

        }
        user_name = preferenceManager.getStringPreference(PreferenceManager.KEY_USERNAME);
        user_pass = preferenceManager.getStringPreference(PreferenceManager.KEY_PASSWORD);
        set_pin = preferenceManager.getStringPreference(PreferenceManager.KEY_SET_UP_PIN);


        tvEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(StartActivity.this, RegisterActivity.class));
//                finish();
                Intent intent1 = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        tvEmployer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartActivity.this, MainActivity.class);
                i.putExtra("type", "form1");
                startActivity(i);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
