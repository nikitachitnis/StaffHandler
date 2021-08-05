package com.miscos.staffhandler.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.miscos.staffhandler.R;

public class ReportFragmentActivity extends AppCompatActivity
{
    TextView txttitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_fragment);
      /*  txttitle=findViewById(R.id.txttitle);
        if(getIntent()!=null)
        {
            txttitle.setText(getIntent().getStringExtra("name"));
        }*/
    }
}
