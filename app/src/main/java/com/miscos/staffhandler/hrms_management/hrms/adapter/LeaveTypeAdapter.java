package com.miscos.staffhandler.hrms_management.hrms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.miscos.staffhandler.R;

import java.util.ArrayList;

public class LeaveTypeAdapter extends ArrayAdapter<String> {

    public LeaveTypeAdapter(Context context,
                            ArrayList<String> algorithmList)
    {
        super(context, 0, algorithmList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_leave_type, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.tvProjectName);
        String currentItem = getItem(position);
        if (currentItem != null) {
            textViewName.setText(currentItem);
        }
        return convertView;
    }
}