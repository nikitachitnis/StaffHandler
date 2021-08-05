package com.miscos.staffhandler.shiftmanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.miscos.staffhandler.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AuthorityAdapter extends RecyclerView.Adapter<AuthorityAdapter.MyViewHolder1> {

    Context context;
    List<String> employeeArray;
    public List<Boolean> authStatus;
    public String manual_attendance;

    public AuthorityAdapter(Context context, List<String> employeeArray, List<Boolean> authStatus) {
        this.authStatus = authStatus;
        this.employeeArray = employeeArray;
        this.context = context;

    }

    public AuthorityAdapter(Context context, List<String> employeeArray) {
        this.employeeArray = employeeArray;
        this.context = context;

    }

    @NonNull
    @Override
    public AuthorityAdapter.MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.authority_recycler_item, parent, false);

        return new AuthorityAdapter.MyViewHolder1(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AuthorityAdapter.MyViewHolder1 holder, final int i) {
        holder.textView.setText(employeeArray.get(i));
        holder.rdmanualAttendance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                switch (radioGroup.getCheckedRadioButtonId())
                {
                    case R.id.rdbtnAllshifts:
                        manual_attendance="all";
                        break;
                    case R.id.rdbtnshiftwise:
                        manual_attendance="shift";
                        break;
                }

            }
        });
        if (authStatus!=null)
        {
            if (authStatus.get(i))
            {
                holder.yes.setChecked(true);
                if(employeeArray.get(i).equalsIgnoreCase("Employee Manual attendance"))
                {
                    holder.lv_manualattendance.setVisibility(View.VISIBLE);
                }
                else
                    holder.lv_manualattendance.setVisibility(View.GONE);
            } else {
                holder.no.setChecked(true);
            }

        }

        holder.yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    holder.no.setChecked(false);
                    authStatus.set(i,true);
                    if(employeeArray.get(i).equalsIgnoreCase("Employee Manual attendance"))
                    {
                        holder.lv_manualattendance.setVisibility(View.VISIBLE);
                    }
                    else
                        holder.lv_manualattendance.setVisibility(View.GONE);
                }
            }
        });
        holder.no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.yes.setChecked(false);
                    authStatus.set(i,false);
                    if(employeeArray.get(i).equalsIgnoreCase("Employee Manual attendance"))
                    {
                        holder.lv_manualattendance.setVisibility(View.GONE);
                    }

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return employeeArray.size();
    }


    public class MyViewHolder1 extends RecyclerView.ViewHolder {
        TextView textView;
        RadioButton yes, no;
        RadioGroup rdmanualAttendance;
        LinearLayout lv_manualattendance;

        public MyViewHolder1(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.authorityText);
            yes = itemView.findViewById(R.id.yesRadioButton);
            no = itemView.findViewById(R.id.noRadioButton);
            lv_manualattendance=itemView.findViewById(R.id.lv_manualAttendance);
            rdmanualAttendance=itemView.findViewById(R.id.rdgroupManualattendance);

        }

    }

}
