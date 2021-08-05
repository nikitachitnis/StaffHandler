package com.miscos.staffhandler.ssa.policynruledocumentation;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.RowPolicyFilesBinding;
import com.miscos.staffhandler.employer_java.adapter.DataBoundListAdapter;


public class PolicyFilesAdapter extends DataBoundListAdapter<String, RowPolicyFilesBinding> {

    static DiffUtil.ItemCallback<String> diffCallback = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.toString().equals(newItem.toString());
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return false;
        }
    };


    public PolicyFilesAdapter() {
        super(diffCallback);
    }

    @Override
    protected RowPolicyFilesBinding createBinding(ViewGroup parent) {
        RowPolicyFilesBinding binding = DataBindingUtil
                .inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.row_policy_files,
                        parent,
                        false);
        return binding;
    }

    @Override
    protected void bind(RowPolicyFilesBinding binding, String item, int position) {
     binding.txtFile.setText(item);
    }

}
