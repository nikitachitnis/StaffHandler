package com.miscos.staffhandler.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.miscos.staffhandler.hrms_management.hrms.activity.OldEmployeeFeedbackActivity;
import com.miscos.staffhandler.hrms_management.hrms.model.response.EmployeeLIst;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.RowOldEmployeeDataBinding;
import com.miscos.staffhandler.employee.Model.EmployeeList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OldEmployeeAdapter extends RecyclerView.Adapter<OldEmployeeAdapter.DataViewHolder> {

    private Context context;
    private ArrayList<EmployeeList> employeeDataArrayList;
    private EmployeeListener listener;

    public OldEmployeeAdapter(Context context, ArrayList<EmployeeList> employeeDataArrayList)
    {
        this.employeeDataArrayList = employeeDataArrayList;
        this.context = context;
        this.listener=listener;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowOldEmployeeDataBinding binding;
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_old_employee_data, parent, false);
        return new DataViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position)
    {
        EmployeeList data = employeeDataArrayList.get(position);
        holder.mBinding.txtEmpName.setText("Name : "+data.getName() + " , Employee No : " + data.getEmployeeNo());
        holder.mBinding.tvdateofJoining.setText(parseDateToddMMyyyy(data.getDateOfJoining()));
        holder.mBinding.tvDateofLeaving.setText(parseDateToddMMyyyy(data.getDateOfLeaving()));
        holder.mBinding.txtMobileno.setText(data.getMobile_no());
        holder.mBinding.cloldEmp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(context, OldEmployeeFeedbackActivity.class);
                intent.putExtra("emp_id",data.getEmployeeId());
                intent.putExtra("emp_name",data.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeDataArrayList != null ? employeeDataArrayList.size() : 0;
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        private RowOldEmployeeDataBinding mBinding;

        public DataViewHolder(RowOldEmployeeDataBinding mBinding) {
            super(mBinding.cloldEmp);
            this.mBinding = mBinding;
        }
    }


    public interface EmployeeListener{
        void selectEmployee(EmployeeLIst.EmployeeData data);
    }

    public void updateList(ArrayList<EmployeeList> list) {
        employeeDataArrayList = list;
        notifyDataSetChanged();
    }
    public String parseDateToddMMyyyy(String time)
    {
        if(time.equalsIgnoreCase(""))
        {
            return "Not available";
        }
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