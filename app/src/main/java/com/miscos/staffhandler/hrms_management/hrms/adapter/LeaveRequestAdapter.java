package com.miscos.staffhandler.hrms_management.hrms.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.miscos.staffhandler.hrms_management.hrms.model.response.FetchApplyLeaveData;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.RowLeaveRequestBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LeaveRequestAdapter extends RecyclerView.Adapter<LeaveRequestAdapter.DataViewHolder> {

    private Context context;
    private ArrayList<FetchApplyLeaveData.LeaveApplyDatum> leaveRequestData;

    public LeaveRequestAdapter(Context context, ArrayList<FetchApplyLeaveData.LeaveApplyDatum> leaveRequestData) {
        this.leaveRequestData = leaveRequestData;
        this.context = context;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowLeaveRequestBinding binding;
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_leave_request, parent, false);
        return new DataViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        FetchApplyLeaveData.LeaveApplyDatum data = leaveRequestData.get(position);
        holder.mBinding.setLeaveData(data);
        holder.mBinding.tvType.setText(data.getType()+" Day");
        holder.mBinding.tvFromDate.setText(parseDateToddMMyyyy(data.getLeaveFromDate()));
        holder.mBinding.tvToDate.setText(parseDateToddMMyyyy(data.getLeaveToDate()));
        if (data.getLeaveStatus().equalsIgnoreCase("Rejected")){
            holder.mBinding.tvStatus.setTextColor(context.getResources().getColor(R.color.red_500));
        }
        if (data.getLeaveStatus().equalsIgnoreCase("Approved")){
            holder.mBinding.tvStatus.setTextColor(context.getResources().getColor(R.color.green));
        }
        if (data.getLeaveStatus().equalsIgnoreCase("Pending")){
            holder.mBinding.tvStatus.setTextColor(context.getResources().getColor(R.color.blue_500));
        }
    }
    public String parseDateToddMMyyyy(String time)
    {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    @Override
    public int getItemCount() {
        return leaveRequestData != null ? leaveRequestData.size() : 0;
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        private RowLeaveRequestBinding mBinding;

        public DataViewHolder(RowLeaveRequestBinding mBinding) {
            super(mBinding.clMain);
            this.mBinding = mBinding;
        }
    }

}