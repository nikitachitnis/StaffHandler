package com.miscos.staffhandler.employer_java.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class DataBoundViewHolder<V extends ViewDataBinding> extends RecyclerView.ViewHolder {

    V binding;

    public DataBoundViewHolder(V binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
