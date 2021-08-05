package com.miscos.staffhandler.hrms_management.hrms.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.miscos.staffhandler.hrms_management.hrms.model.response.OtherDeductionResponse;
import com.miscos.staffhandler.hrms_management.hrms.utility.AppConstant;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.RowOtherDeductionDataBinding;

import java.util.ArrayList;

public class OtherDeductionAdapter extends RecyclerView.Adapter<OtherDeductionAdapter.DataViewHolder> {

    private Context context;
    private ArrayList<OtherDeductionResponse.DeductionData> deductionData;

    public OtherDeductionAdapter(Context context, ArrayList<OtherDeductionResponse.DeductionData> deductionData) {
        this.deductionData = deductionData;
        this.context = context;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowOtherDeductionDataBinding binding;
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_other_deduction_data, parent, false);
        return new DataViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        OtherDeductionResponse.DeductionData data = deductionData.get(position);
        holder.mBinding.setDeductionsData(data);
        holder.mBinding.tvOtherDeductionAmount.setText(AppConstant.RUPEE_SIGN+data.getAmount());
    }

    @Override
    public int getItemCount() {
        return deductionData != null ? deductionData.size() : 0;
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        private RowOtherDeductionDataBinding mBinding;

        public DataViewHolder(RowOtherDeductionDataBinding mBinding) {
            super(mBinding.clMain);
            this.mBinding = mBinding;
        }
    }

}