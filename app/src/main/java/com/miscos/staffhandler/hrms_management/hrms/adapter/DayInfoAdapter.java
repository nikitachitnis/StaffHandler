package com.miscos.staffhandler.hrms_management.hrms.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.miscos.staffhandler.hrms_management.hrms.model.DayInfoData;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.RowEmployeeDaysInfoBinding;

import java.util.ArrayList;

public class DayInfoAdapter extends RecyclerView.Adapter<DayInfoAdapter.DataViewHolder> {

    private Context context;
    private ArrayList<DayInfoData> dayInfoData;

    public DayInfoAdapter(Context context, ArrayList<DayInfoData> dayInfoData) {
        this.dayInfoData = dayInfoData;
        this.context = context;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowEmployeeDaysInfoBinding binding;
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_employee_days_info, parent, false);
        return new DataViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        DayInfoData data = dayInfoData.get(position);
        holder.mBinding.setDayInfo(data);
    }

    @Override
    public int getItemCount() {
        return dayInfoData != null ? dayInfoData.size() : 0;
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        private RowEmployeeDaysInfoBinding mBinding;

        public DataViewHolder(RowEmployeeDaysInfoBinding mBinding) {
            super(mBinding.clMain);
            this.mBinding = mBinding;
        }
    }

}