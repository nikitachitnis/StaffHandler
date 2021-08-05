package com.miscos.staffhandler.ssa.shiftwiseattendance;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.ActivityShiftWiseAttendBinding;

import java.util.ArrayList;
import java.util.List;

public class ShiftWiseAttendActivity extends AppCompatActivity {

    ActivityShiftWiseAttendBinding binding;
    AttendanceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shift_wise_attend);
        binding.recyclerView.setAdapter(adapter = new AttendanceAdapter());
        List<String> strings= new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            strings.add(i+"");
        }
        adapter.submitList(strings);
    }
}