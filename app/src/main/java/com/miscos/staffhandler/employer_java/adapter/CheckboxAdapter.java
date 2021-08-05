package com.miscos.staffhandler.employer_java.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.LayoutWeekoffItemBinding;

import java.util.ArrayList;

public class CheckboxAdapter extends DataBoundListAdapter<String, LayoutWeekoffItemBinding>{


   public ArrayList<String> selectedValue = new ArrayList<>();
    static DiffUtil.ItemCallback<String> diffCallback = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }
    };
    private CallBack callBack;

    public CheckboxAdapter(CallBack callBack) {
        super(diffCallback);
        this.callBack = callBack;
    }

    @Override
    protected LayoutWeekoffItemBinding createBinding(ViewGroup parent) {
        LayoutWeekoffItemBinding binding = DataBindingUtil
                .inflate(
                LayoutInflater.from(parent.getContext()),
        R.layout.layout_weekoff_item,
                parent,
                false);
        return binding;
    }

    @Override
    protected void bind(LayoutWeekoffItemBinding binding, String item, int position) {
        binding.setItem(item.substring(0,3));
        binding.setSelectedItems(selectedValue);

        if(selectedValue.contains(item.toLowerCase()))
        {
            binding.chkweekoff.setChecked(true);
        }
        else
            binding.chkweekoff.setChecked(false);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onItemClick(item,position);
            }
        });
    }


    public interface CallBack{
        void onItemClick(String item,int pos);
    }


}
