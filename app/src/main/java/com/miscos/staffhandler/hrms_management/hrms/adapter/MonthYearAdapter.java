package com.miscos.staffhandler.hrms_management.hrms.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.miscos.staffhandler.hrms_management.hrms.model.MonthYearData;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.RowMonthYearBinding;

import java.util.ArrayList;

public class MonthYearAdapter extends RecyclerView.Adapter<MonthYearAdapter.DataViewHolder> {

    private Context context;
    private ArrayList<MonthYearData> monthYearData;
    private SetOnMonthListener listener;
    private int selectedItem = 0;
    private boolean isNotSelect = true;


    public MonthYearAdapter(Context context, ArrayList<MonthYearData> monthYearData, SetOnMonthListener listener) {
        this.monthYearData = monthYearData;
        this.context = context;
        this.listener=listener;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowMonthYearBinding binding;
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_month_year, parent, false);
        return new DataViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        MonthYearData data = monthYearData.get(position);
        holder.mBinding.setMonthData(data);

        if (isNotSelect) {
            if (data.isSelect()) {
                holder.mBinding.clMain.setBackground(context.getResources().getDrawable(R.drawable.blue_rounded_rectangle));
                selectedItem = position;
                listener.selectMonth(data.getMonthName(),data.getYear(),position);
            } else {
                holder.mBinding.clMain.setBackground(context.getResources().getDrawable(R.drawable.gray_rounded_rectangle));
            }
        } else {
            if (selectedItem == position) {
                holder.mBinding.clMain.setBackground(context.getResources().getDrawable(R.drawable.blue_rounded_rectangle));
            } else {
                holder.mBinding.clMain.setBackground(context.getResources().getDrawable(R.drawable.gray_rounded_rectangle));
            }
        }

        holder.mBinding.clMain.setOnClickListener(view -> {
            isNotSelect = false;
            int previousItem = selectedItem;
            listener.selectMonth(data.getMonthName(),data.getYear(),position);
            selectedItem = position;
            notifyItemChanged(previousItem);
            notifyItemChanged(position);
        });

    }

    @Override
    public int getItemCount() {
        return monthYearData != null ? monthYearData.size() : 0;
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        private RowMonthYearBinding mBinding;
        public DataViewHolder(RowMonthYearBinding mBinding) {
            super(mBinding.clMain);
            this.mBinding = mBinding;
        }
    }

    public interface SetOnMonthListener{
        void selectMonth(String month, String year, int position);
    }

}