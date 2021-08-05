package com.miscos.staffhandler.ssa.inout;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.RowOldRecordsInOutBinding;
import com.miscos.staffhandler.employer_java.adapter.DataBoundListAdapter;
import com.miscos.staffhandler.employer_java.remote.model.inoutdata.oldinoutdata.DataItem;


public class OldRecordsAdapter extends DataBoundListAdapter<DataItem, RowOldRecordsInOutBinding> {

    static DiffUtil.ItemCallback<DataItem> diffCallback = new DiffUtil.ItemCallback<DataItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull DataItem oldItem, @NonNull DataItem newItem) {
            return oldItem.toString().equals(newItem.toString());
        }

        @Override
        public boolean areContentsTheSame(@NonNull DataItem oldItem, @NonNull DataItem newItem) {
            return false;
        }
    };


    public OldRecordsAdapter() {
        super(diffCallback);
    }

    @Override
    protected RowOldRecordsInOutBinding createBinding(ViewGroup parent) {
        RowOldRecordsInOutBinding binding = DataBindingUtil
                .inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.row_old_records_in_out,
                        parent,
                        false);
        return binding;
    }

    @Override
    protected void bind(RowOldRecordsInOutBinding binding, DataItem item, int position) {
        binding.setOldItems(item);
    }

}
