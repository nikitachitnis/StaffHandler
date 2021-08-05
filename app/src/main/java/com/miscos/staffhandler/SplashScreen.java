package com.miscos.staffhandler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.miscos.staffhandler.activities.StartActivity;
import com.miscos.staffhandler.employee.employeemodule.Activity_PinLogin;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
/*Developed under Miscos
 * Developed by Karan
 * 05-06-2020
 * */
public class SplashScreen extends AppCompatActivity {
    Handler handler5;
    PreferenceManager preferenceManager;
    Intent intent;
    String user_name;
    String user_pass;
    String set_pin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        preferenceManager=new PreferenceManager(SplashScreen.this);
        user_name = preferenceManager.getStringPreference(PreferenceManager.KEY_USERNAME);
        user_pass = preferenceManager.getStringPreference(PreferenceManager.KEY_PASSWORD);
        set_pin  = preferenceManager.getStringPreference(PreferenceManager.KEY_SET_UP_PIN);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!user_name.equals("") && !user_pass.equals("") && !set_pin.equals(""))
                {
                    Intent intent = new Intent(SplashScreen.this, Activity_PinLogin.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(SplashScreen.this, StartActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }
}