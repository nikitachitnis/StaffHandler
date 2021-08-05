package com.miscos.staffhandler.ssa.departdesignation;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.RowDepartEmployeeNameBinding;
import com.miscos.staffhandler.employer_java.adapter.DataBoundListAdapter;
import com.miscos.staffhandler.employer_java.remote.model.designationlist.employeedesignationwise.EmployeeData;


public class EmployeeDetailAdapter extends DataBoundListAdapter<EmployeeData,RowDepartEmployeeNameBinding > {

    static DiffUtil.ItemCallback<EmployeeData> diffCallback = new DiffUtil.ItemCallback<EmployeeData>() {
        @Override
        public boolean areItemsTheSame(@NonNull EmployeeData oldItem, @NonNull EmployeeData newItem) {
            return oldItem.getEmployeeId().equals(newItem.getEmployeeId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull EmployeeData oldItem, @NonNull EmployeeData newItem) {
            return oldItem.toString().equals(newItem.toString());
        }
    };

    DesignationCallBack callBack;


    public EmployeeDetailAdapter() {
        super(diffCallback);
    }

    @Override
    protected RowDepartEmployeeNameBinding createBinding(ViewGroup parent) {
        RowDepartEmployeeNameBinding binding = DataBindingUtil
                .inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.row_depart_employee_name,
                        parent,
                        false);
        return binding;
    }

    @Override
    protected void bind(RowDepartEmployeeNameBinding binding, EmployeeData item, int position) {
        binding.setEmployeeData(item);
    }

    public interface DesignationCallBack{
        void onListClick(int pos, EmployeeData item);
    }

}
