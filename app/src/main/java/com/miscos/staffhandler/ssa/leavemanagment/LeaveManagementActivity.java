package com.miscos.staffhandler.ssa.leavemanagment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.ActivityLeaveManagementBinding;

import java.util.ArrayList;
import java.util.List;

public class LeaveManagementActivity extends AppCompatActivity {

    ActivityLeaveManagementBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this,R.layout.activity_leave_management);
        setData();
    }

    private void setData() {
        UpComingLeavesAdapter leavesAdapter = new UpComingLeavesAdapter();
        PastLeaveAdapter pastLeaveAdapter = new PastLeaveAdapter();
        binding.recyclerLeaveUpcoming.setAdapter(leavesAdapter);
        binding.recyclerLeaveLast6Months.setAdapter(pastLeaveAdapter);
        List<String> s = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            s.add(i+"");
        }
        List<String> s1 = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            s1.add(i+"");
        }
        leavesAdapter.submitList(s1);
        pastLeaveAdapter.submitList(s);
    }
}