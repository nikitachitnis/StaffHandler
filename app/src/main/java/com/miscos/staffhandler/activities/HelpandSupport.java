package com.miscos.staffhandler.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.miscos.staffhandler.R;
/*Developed under Miscos
 * Developed by Nikhil
 * 05-06-2020
 * */
public class HelpandSupport extends AppCompatActivity {

    Toolbar toolbar;
    LinearLayout lv1,lv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpand_support);
        toolbar = findViewById(R.id.toolbar);
        lv1 = findViewById(R.id.lvCon1);
        lv2 = findViewById(R.id.lvCon2);
        lv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpandSupport.this,ContactUs.class));
                finish();
            }
        });
        lv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpandSupport.this,UserManual.class));
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(HelpandSupport.this, EmployerZone.class));
        finish();
    }
}