package com.miscos.staffhandler.employer_java.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;


import com.miscos.staffhandler.BR;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.LayoutWeekoffItemBinding;
import com.miscos.staffhandler.databinding.RowHolidayBinding;
import com.miscos.staffhandler.employer_java.remote.model.fetchdataform2.HolidayData;


import java.util.ArrayList;

import java.util.ArrayList;

public class HolidayAdapter extends DataBoundListAdapter<HolidayData, RowHolidayBinding> {

    CallBack callBack;
    static DiffUtil.ItemCallback<HolidayData> diffCallback = new DiffUtil.ItemCallback<HolidayData>() {
        @Override
        public boolean areItemsTheSame(@NonNull HolidayData oldItem, @NonNull HolidayData newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull HolidayData oldItem, @NonNull HolidayData newItem) {
            return true;

        }
    };


    public HolidayAdapter(CallBack callBack) {
        super(diffCallback);
        this.callBack = callBack;
    }

    @Override
    protected RowHolidayBinding createBinding(ViewGroup parent) {
        RowHolidayBinding binding = DataBindingUtil
                .inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.row_holiday,
                        parent,
                        false);
        return binding;
    }

    @Override
    protected void bind(RowHolidayBinding binding, HolidayData item, int position) {
        binding.setHolidayData(item);
        if (item.isEditable()) {
            binding.imgEdit.setVisibility(View.VISIBLE);
        } else
        {
            binding.imgEdit.setVisibility(View.GONE);
        }
        binding.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onItemEditClick(item);
            }
        });
    }

    public interface CallBack{
        void onItemEditClick(HolidayData item);
    }

}
