package com.miscos.staffhandler.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.employee.helper.PreferenceManager;

/*Developed under Miscos
 * Developed by Nikhil
 * 05-06-2020
 * */
public class UserManual extends AppCompatActivity {
    PreferenceManager preferenceManager;
    CardView card1,card2,card3,card4,card5,card6,card7,card8,card9,card10,card11,card12,card13,card14,card15,card16,card17,card18,card19,card20;
    String df;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manual);
        preferenceManager = new PreferenceManager(this);

        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);
        card5 = findViewById(R.id.card5);
        card6 = findViewById(R.id.card6);
        card7 = findViewById(R.id.card7);
        card8 = findViewById(R.id.card8);
        card9 = findViewById(R.id.card9);
        card10 = findViewById(R.id.card10);
        card11 = findViewById(R.id.card11);
        card12 = findViewById(R.id.card12);
        card13 = findViewById(R.id.card13);
        card14 = findViewById(R.id.card14);
        card15 = findViewById(R.id.card15);
        card16 = findViewById(R.id.card16);
        card17 = findViewById(R.id.card17);
        card18 = findViewById(R.id.card18);
        card19 = findViewById(R.id.card19);
        card20 = findViewById(R.id.card20);

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://staffhandler.com/production/mini_staff_handler/help/android/1.pdf"));
                startActivity(viewIntent);
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://staffhandler.com/production/mini_staff_handler/help/android/2.pdf"));
                startActivity(viewIntent);
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://staffhandler.com/production/mini_staff_handler/help/android/3.pdf"));
                startActivity(viewIntent);
            }
        });

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://staffhandler.com/production/mini_staff_handler/help/android/4.pdf"));
                startActivity(viewIntent);
            }
        });

        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://staffhandler.com/production/mini_staff_handler/help/android/5.pdf"));
                startActivity(viewIntent);
            }
        });

        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://staffhandler.com/production/mini_staff_handler/help/android/6.pdf"));
                startActivity(viewIntent);
            }
        });

        card7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://staffhandler.com/production/mini_staff_handler/help/android/7.pdf"));
                startActivity(viewIntent);
            }
        });

        card8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://staffhandler.com/production/mini_staff_handler/help/android/8.pdf"));
                startActivity(viewIntent);
            }
        });

        card9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://staffhandler.com/production/mini_staff_handler/help/android/9.pdf"));
                startActivity(viewIntent);
            }
        });

        card10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://staffhandler.com/production/mini_staff_handler/help/android/10.pdf"));
                startActivity(viewIntent);
            }
        });

        card11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://staffhandler.com/production/mini_staff_handler/help/android/11.pdf"));
                startActivity(viewIntent);
            }
        });

        card12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://staffhandler.com/production/mini_staff_handler/help/android/12.pdf"));
                startActivity(viewIntent);
            }
        });

        card13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://staffhandler.com/production/mini_staff_handler/help/android/13.pdf"));
                startActivity(viewIntent);
            }
        });
        card14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://staffhandler.com/production/mini_staff_handler/help/android/14.pdf"));
                startActivity(viewIntent);
            }
        });
        card15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://staffhandler.com/production/mini_staff_handler/help/android/15.pdf"));
                startActivity(viewIntent);
            }
        });
        card16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://staffhandler.com/production/mini_staff_handler/help/android/16.pdf"));
                startActivity(viewIntent);
            }
        });
        card17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://staffhandler.com/production/mini_staff_handler/help/android/17.pdf"));
                startActivity(viewIntent);
            }
        });
        card18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://staffhandler.com/production/mini_staff_handler/help/android/18.pdf"));
                startActivity(viewIntent);
            }
        });
        card19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://staffhandler.com/production/mini_staff_handler/help/android/19.pdf"));
                startActivity(viewIntent);
            }
        });
        card20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://staffhandler.com/production/mini_staff_handler/help/android/20.pdf"));
                startActivity(viewIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UserManual.this, HelpandSupport.class));
        finish();
    }
}