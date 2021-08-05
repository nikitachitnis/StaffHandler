package com.miscos.staffhandler.hrms_management.hrms.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.miscos.staffhandler.hrms_management.hrms.model.LeaveInfoData;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.RowLeaveInfoBinding;

import java.util.ArrayList;

public class LeaveInfoAdapter extends RecyclerView.Adapter<LeaveInfoAdapter.DataViewHolder> {

    private Context context;
    private ArrayList<LeaveInfoData> leaveInfoData;

    public LeaveInfoAdapter(Context context, ArrayList<LeaveInfoData> leaveInfoData) {
        this.leaveInfoData = leaveInfoData;
        this.context = context;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowLeaveInfoBinding binding;
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_leave_info, parent, false);
        return new DataViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        LeaveInfoData data = leaveInfoData.get(position);
        holder.mBinding.setLeaveInfo(data);
    }

    @Override
    public int getItemCount() {
        return leaveInfoData != null ? leaveInfoData.size() : 0;
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        private RowLeaveInfoBinding mBinding;

        public DataViewHolder(RowLeaveInfoBinding mBinding) {
            super(mBinding.clMain);
            this.mBinding = mBinding;
        }
    }

}