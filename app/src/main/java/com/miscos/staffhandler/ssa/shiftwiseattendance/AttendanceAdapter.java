package com.miscos.staffhandler.ssa.shiftwiseattendance;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.RowShiftwiseAttendanceBinding;
import com.miscos.staffhandler.employer_java.adapter.DataBoundListAdapter;


public class AttendanceAdapter extends DataBoundListAdapter<String, RowShiftwiseAttendanceBinding> {
    
    static DiffUtil.ItemCallback<String> diffCallback = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return true;

        }
    };


    public AttendanceAdapter() {
        super(diffCallback);
    }

    @Override
    protected RowShiftwiseAttendanceBinding createBinding(ViewGroup parent) {
        RowShiftwiseAttendanceBinding binding = DataBindingUtil
                .inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.row_shiftwise_attendance,
                        parent,
                        false);
        return binding;
    }

    @Override
    protected void bind(RowShiftwiseAttendanceBinding binding, String item, int position)
    {

        if(position==1)
        {
            binding.empName.setText("Karan Ahir");
            binding.empDeisignation.setText("Software Developer");
        }
        else if (position==2)

            {
                binding.empName.setText("Pradyuman Jha");
                binding.empDeisignation.setText("Software Developer"); }

    }

}
