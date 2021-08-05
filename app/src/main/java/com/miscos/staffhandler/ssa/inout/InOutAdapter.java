package com.miscos.staffhandler.ssa.inout;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.RowInOutBinding;
import com.miscos.staffhandler.employer_java.adapter.DataBoundListAdapter;
import com.miscos.staffhandler.employer_java.remote.model.inoutdata.ListItem;


public class InOutAdapter extends DataBoundListAdapter<ListItem, RowInOutBinding> {

    static DiffUtil.ItemCallback<ListItem> diffCallback = new DiffUtil.ItemCallback<ListItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull ListItem oldItem, @NonNull ListItem newItem) {
            return oldItem.toString().equals(newItem.toString());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ListItem oldItem, @NonNull ListItem newItem) {
            return false;
        }
    };


    public InOutAdapter() {
        super(diffCallback);
    }

    @Override
    protected RowInOutBinding createBinding(ViewGroup parent) {
        RowInOutBinding binding = DataBindingUtil
                .inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.row_in_out,
                        parent,
                        false);
        return binding;
    }

    @Override
    protected void bind(RowInOutBinding binding, ListItem item, int position) {
        binding.setInOutData(item);
    }

}
