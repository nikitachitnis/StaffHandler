package com.miscos.staffhandler;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.fivemin.chief.nonetworklibrary.networkBroadcast.NoNet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.miscos.staffhandler.employee.helper.PreferenceManager;

import com.miscos.staffhandler.fragments.EmployeeManagementFragment;
import com.miscos.staffhandler.fragments.EmployeeReportFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

/*Developed under Miscos
 * Developed by Nikhil
 * 15-07-2020
 * */
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    String reports;
    PreferenceManager preferenceManager;
    private ActionBar toolbar;
    private FrameLayout frameLayout;
    private NoNet mNoNet;
    private FragmentManager fm = null;
    private Object Activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();
        mNoNet = new NoNet();

        preferenceManager = new PreferenceManager(this);
        reports = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_REPORT);
        mNoNet.initNoNet(this, fm);
        frameLayout = findViewById(R.id.frame_container);
     /*   //loading the default fragment
        loadFragment(new EmployeeManagementFragment());
        BottomNavigationView navView = findViewById(R.id.bottomNavigation);

        navView.setOnNavigationItemSelectedListener(this);*/
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_management:
                fragment = new EmployeeManagementFragment();
                break;

           /* case R.id.navigation_reports:
                if (preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_REPORT).equalsIgnoreCase("Y")) {
                    fragment = new EmployeeReportFragment();
                } else {
                    allow_access();
                }
                break;*/
        }
        return loadFragment(fragment);
    }
    @Override
    protected void onResume() {
        mNoNet.RegisterNoNet();
        super.onResume();
    }

    private void allow_access() {
        final PrettyDialog prettyDialog = new PrettyDialog(MainActivity.this);
        prettyDialog.setCancelable(false);
        prettyDialog
                .setTitle("Access Denied")
                .setMessage("You are not allow to access this.")
                .setIcon(R.drawable.cross, R.color.white, null)
                .addButton("Close", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        prettyDialog.dismiss();
                    }
                })
                .show();

    }

    @Override
    protected void onPause() {
        mNoNet.unRegisterNoNet();
        super.onPause();
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
