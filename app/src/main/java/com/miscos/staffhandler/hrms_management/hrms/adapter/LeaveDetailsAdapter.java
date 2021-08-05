package com.miscos.staffhandler.hrms_management.hrms.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.miscos.staffhandler.hrms_management.hrms.model.response.SalaryAndLeaveData;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.RowLeaveDetailsBinding;

import java.util.ArrayList;

public class LeaveDetailsAdapter extends RecyclerView.Adapter<LeaveDetailsAdapter.DataViewHolder> {

    private Context context;
    private ArrayList<SalaryAndLeaveData.LeaveDetail> leaveDetailsData;

    public LeaveDetailsAdapter(Context context, ArrayList<SalaryAndLeaveData.LeaveDetail> leaveDetailsData) {
        this.leaveDetailsData = leaveDetailsData;
        this.context = context;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowLeaveDetailsBinding binding;
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_leave_details, parent, false);
        return new DataViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        SalaryAndLeaveData.LeaveDetail data = leaveDetailsData.get(position);
        holder.mBinding.setLeaveData(data);
    }

    @Override
    public int getItemCount() {
        return leaveDetailsData != null ? leaveDetailsData.size() : 0;
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        private RowLeaveDetailsBinding mBinding;

        public DataViewHolder(RowLeaveDetailsBinding mBinding) {
            super(mBinding.clMain);
            this.mBinding = mBinding;
        }
    }

}