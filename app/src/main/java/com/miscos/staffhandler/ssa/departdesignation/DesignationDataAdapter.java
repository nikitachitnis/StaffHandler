package com.miscos.staffhandler.ssa.departdesignation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.RowDepartDesignationBinding;
import com.miscos.staffhandler.employer_java.adapter.DataBoundListAdapter;
import com.miscos.staffhandler.employer_java.remote.model.designationlist.employeedesignationwise.DataItem;


public class DesignationDataAdapter extends DataBoundListAdapter<DataItem, RowDepartDesignationBinding> {

    static DiffUtil.ItemCallback<DataItem> diffCallback = new DiffUtil.ItemCallback<DataItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull DataItem oldItem, @NonNull DataItem newItem) {
            return oldItem.getDesignationId().equals(newItem.getDesignationId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull DataItem oldItem, @NonNull DataItem newItem) {
            return false;
        }
    };

    DesignationCallBack callBack;


    public DesignationDataAdapter(DesignationCallBack callBack) {
        super(diffCallback);
        this.callBack = callBack;
    }

    @Override
    protected RowDepartDesignationBinding createBinding(ViewGroup parent) {
        RowDepartDesignationBinding binding = DataBindingUtil
                .inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.row_depart_designation,
                        parent,
                        false);
        return binding;
    }

    @Override
    protected void bind(RowDepartDesignationBinding binding, DataItem item, int position) {
        binding.setDataItem(item);
        binding.txtList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.txtList.getText().toString().equals("List")){
                    callBack.onListClick(position,item);
                }
            }
        });
    }

    public interface DesignationCallBack{
        void onListClick(int pos, DataItem item);
    }

}
