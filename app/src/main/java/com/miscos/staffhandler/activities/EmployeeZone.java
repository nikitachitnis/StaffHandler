package com.miscos.staffhandler.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.miscos.staffhandler.hrms_management.hrms.activity.ApplyLeaveActivity;
import com.miscos.staffhandler.hrms_management.hrms.activity.SalaryInformationActivity;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.ssa.inout.InOutActivity;

/*Developed under Miscos
 * Developed by Nikhil
 * 18-06-2020
 * */
public class EmployeeZone extends AppCompatActivity
{
    ImageView tvBack;
    CardView card_leave,card_salary_info,card_emp_inout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_zone);
        tvBack =  findViewById(R.id.imBack);
card_leave=findViewById(R.id.leave_management);
card_salary_info=findViewById(R.id.salaryDetails);
card_emp_inout=findViewById(R.id.emp_inout);
card_salary_info.setOnClickListener(new View.OnClickListener()
{
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(EmployeeZone.this, SalaryInformationActivity.class);
        startActivity(intent);
    }
});
        card_emp_inout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EmployeeZone.this, InOutActivity.class);
                startActivity(intent);
            }
        });
card_leave.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view)
    {
        Intent intent=new Intent(EmployeeZone.this, ApplyLeaveActivity.class);
        startActivity(intent);
    }
});
        //Back Button
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
       finish();
    }
}
