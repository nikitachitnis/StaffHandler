package com.miscos.staffhandler.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.ActivityReportsBinding;

import java.util.ArrayList;

public class ReportsActivity extends AppCompatActivity
{
    ArrayList<String> strings=new ArrayList<>();
    ArrayAdapter<String> stringArrayAdapter;


ActivityReportsBinding reportsBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        reportsBinding= DataBindingUtil.setContentView(this,R.layout.activity_reports);
        strings.add("List of employees");
        strings.add("Month wise Salary & Other deductions with additional payments with Tax deductions (Summary)");
        strings.add("Month wise Salary & Other deductions with additional payments with Tax deductions  (Summary)");
        strings.add("Employee Attendance  (Summary)");
        strings.add("Employee Attendance  (Summary)");
        strings.add("Employee Leaves Monitoring");
        strings.add("Monthly tax collections");
        strings.add("List of admin & roles");
        strings.add("Shift wise employee list");
        strings.add("Month wise employee details");
        strings.add("Old employee report");
stringArrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,strings);
reportsBinding.recyclerViewReports.setAdapter(stringArrayAdapter);

reportsBinding.recyclerViewReports.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        Intent intent=new Intent(ReportsActivity.this,ReportFragmentActivity.class);
        intent.putExtra("name",strings.get(i));
        startActivity(intent);

    }
});





    }
}
