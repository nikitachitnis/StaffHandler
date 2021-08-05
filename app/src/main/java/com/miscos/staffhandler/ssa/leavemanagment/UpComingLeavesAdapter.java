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


public class UpComingLeavesAdapter extends DataBoundListAdapter<String, RowLeaveManagementBinding> {

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


    public UpComingLeavesAdapter() {
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
        if (position==0){
            binding.txtReason.setText("Home Visit");
            binding.txtFrom.setText("16-07-2021");
            binding.txtTo.setText("18-07-2021");
            binding.txtAction.setVisibility(View.GONE);
            binding.txtCancel.setVisibility(View.VISIBLE);
        }

    }

}
