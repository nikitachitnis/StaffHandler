package com.miscos.staffhandler.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.miscos.staffhandler.R;
/*Developed under Miscos
 * Developed by Nikhil
 * 05-06-2020
 * */
public class ContactUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(ContactUs.this, HelpandSupport.class));
        finish();
    }
}