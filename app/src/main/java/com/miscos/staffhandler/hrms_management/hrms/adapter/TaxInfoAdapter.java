package com.miscos.staffhandler.hrms_management.hrms.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.miscos.staffhandler.hrms_management.hrms.model.TaxInfoData;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.RowTaxInfoBinding;

import java.util.ArrayList;

public class TaxInfoAdapter extends RecyclerView.Adapter<TaxInfoAdapter.DataViewHolder> {

    private Context context;
    private ArrayList<TaxInfoData> taxInfoData;

    public TaxInfoAdapter(Context context, ArrayList<TaxInfoData> taxInfoData) {
        this.taxInfoData = taxInfoData;
        this.context = context;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowTaxInfoBinding binding;
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_tax_info, parent, false);
        return new DataViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        TaxInfoData data = taxInfoData.get(position);
        holder.mBinding.setTaxData(data);
    }

    @Override
    public int getItemCount() {
        return taxInfoData != null ? taxInfoData.size() : 0;
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        private RowTaxInfoBinding mBinding;

        public DataViewHolder(RowTaxInfoBinding mBinding) {
            super(mBinding.clMain);
            this.mBinding = mBinding;
        }
    }

}