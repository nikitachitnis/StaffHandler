package com.miscos.staffhandler.hrms_management.hrms.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.miscos.staffhandler.hrms_management.hrms.model.response.EmployeeLIst;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.RowEmpBinding;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.DataViewHolder> {

    private Context context;
    private ArrayList<EmployeeLIst.EmployeeData> employeeDataArrayList;
    private EmployeeListener listener;

    public EmployeeAdapter(Context context, ArrayList<EmployeeLIst.EmployeeData> employeeDataArrayList, EmployeeListener listener) {
        this.employeeDataArrayList = employeeDataArrayList;
        this.context = context;
        this.listener=listener;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowEmpBinding binding;
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_emp, parent, false);
        return new DataViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        EmployeeLIst.EmployeeData data = employeeDataArrayList.get(position);
        holder.mBinding.tvEmployeeName.setText(data.getName() + ",Employee No - " + data.getEmployee_no());

        holder.mBinding.clMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.selectEmployee(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeDataArrayList != null ? employeeDataArrayList.size() : 0;
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        private RowEmpBinding mBinding;

        public DataViewHolder(RowEmpBinding mBinding) {
            super(mBinding.clMain);
            this.mBinding = mBinding;
        }
    }


    public interface EmployeeListener{
        void selectEmployee(EmployeeLIst.EmployeeData data);
    }

    public void updateList(ArrayList<EmployeeLIst.EmployeeData> list) {
        employeeDataArrayList = list;
        notifyDataSetChanged();
    }
}