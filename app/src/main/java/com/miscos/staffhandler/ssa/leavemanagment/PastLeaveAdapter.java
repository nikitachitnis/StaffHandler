package com.miscos.staffhandler.ssa.leavemanagment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.RowLeaveManagementBinding;
import com.miscos.staffhandler.employer_java.adapter.DataBoundListAdapter;


public class PastLeaveAdapter extends DataBoundListAdapter<String, RowLeaveManagementBinding> {

    static DiffUtil.ItemCallback<String> diffCallback = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.toString().equals(newItem.toString());
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return false;
        }
    };


    public PastLeaveAdapter() {
        super(diffCallback);
    }

    @Override
    protected RowLeaveManagementBinding createBinding(ViewGroup parent) {
        RowLeaveManagementBinding binding = DataBindingUtil
                .inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.row_leave_management,
                        parent,
                        false);
        return binding;
    }

    @Override
    protected void bind(RowLeaveManagementBinding binding, String item, int position) {
        if (position==1)
        {
            binding.txtReason.setText("Personal reason");
            binding.txtAction.setText("Approved");
            binding.txtAction.setTextColor(binding.txtAction.getContext().getResources().getColor(R.color.green));
        }
        else if(position==0)
        {
            binding.txtReason.setText("Sick leave");
            binding.txtAction.setText("Approved");
            binding.txtFrom.setText("12-05-2021");
            binding.txtTo.setText("12-05-2021");
            binding.txtAction.setTextColor(binding.txtAction.getContext().getResources().getColor(R.color.green));
        }
    }

}
