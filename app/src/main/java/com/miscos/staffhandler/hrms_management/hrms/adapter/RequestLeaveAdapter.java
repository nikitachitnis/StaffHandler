package com.miscos.staffhandler.hrms_management.hrms.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.miscos.staffhandler.hrms_management.hrms.model.response.PendingLeaveRequestResponse;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.RowRequestLeaveBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RequestLeaveAdapter extends RecyclerView.Adapter<RequestLeaveAdapter.DataViewHolder> {

    private Context context;
    private ArrayList<PendingLeaveRequestResponse.PendingLeaveData> leaveRequestDataArrayList;
    private SetSelectLeave setSelectLeave;

    public RequestLeaveAdapter(Context context, ArrayList<PendingLeaveRequestResponse.PendingLeaveData> leaveRequestDataArrayList, SetSelectLeave setSelectLeave) {
        this.leaveRequestDataArrayList = leaveRequestDataArrayList;
        this.context = context;
        this.setSelectLeave = setSelectLeave;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowRequestLeaveBinding binding;
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_request_leave, parent, false);
        return new DataViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        PendingLeaveRequestResponse.PendingLeaveData data = leaveRequestDataArrayList.get(position);
        String strLeave="From "+parseDateToddMMyyyy(data.getLeaveFromDate())+",To "+parseDateToddMMyyyy(data.getLeaveToDate())+" \nLeave Type :"+data.getType()+" Day ,"+data.getReasonForLeave();
        holder.mBinding.tvLeaveData.setText(strLeave);
        holder.mBinding.clMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectLeave.selectLeaveData(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return leaveRequestDataArrayList != null ? leaveRequestDataArrayList.size() : 0;
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        private RowRequestLeaveBinding mBinding;

        public DataViewHolder(RowRequestLeaveBinding mBinding) {
            super(mBinding.clMain);
            this.mBinding = mBinding;
        }
    }

    public interface SetSelectLeave {
        void selectLeaveData(PendingLeaveRequestResponse.PendingLeaveData leaveRequestData);
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
}