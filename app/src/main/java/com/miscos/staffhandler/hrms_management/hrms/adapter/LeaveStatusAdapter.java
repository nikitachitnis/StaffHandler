package com.miscos.staffhandler.hrms_management.hrms.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.miscos.staffhandler.hrms_management.hrms.model.response.LeaveData;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.RowLeaveStatusBinding;

import java.util.ArrayList;

public class LeaveStatusAdapter extends RecyclerView.Adapter<LeaveStatusAdapter.DataViewHolder> {

    private Context context;
    private ArrayList<LeaveData> leaveStatusData;

    public LeaveStatusAdapter(Context context, ArrayList<LeaveData> leaveStatusData) {
        this.leaveStatusData = leaveStatusData;
        this.context = context;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowLeaveStatusBinding binding;
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_leave_status, parent, false);
        return new DataViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        LeaveData data = leaveStatusData.get(position);
        holder.mBinding.setLeaveData(data);
    }

    @Override
    public int getItemCount() {
        return leaveStatusData != null ? leaveStatusData.size() : 0;
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        private RowLeaveStatusBinding mBinding;

        public DataViewHolder(RowLeaveStatusBinding mBinding) {
            super(mBinding.clMain);
            this.mBinding = mBinding;
        }
    }

}