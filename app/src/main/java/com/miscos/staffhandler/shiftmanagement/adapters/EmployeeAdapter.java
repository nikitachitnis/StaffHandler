package com.miscos.staffhandler.shiftmanagement.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.shiftmanagement.fragments.HolidayAttendance;
import com.miscos.staffhandler.shiftmanagement.models.Employee;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.MyViewHolder1> {

    Context context;
    public List<Employee> employeeArray;
    OnButtonClick onButtonClick;
    int pos = -1;
    Activity fragment;

    public void ClickListener(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    public EmployeeAdapter(Context context, List<Employee> projectsArray, Activity fragment)
    {
        this.fragment = fragment;
        this.employeeArray = projectsArray;
        this.context = context;

    }

    @NonNull
    @Override
    public EmployeeAdapter.MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee_recycler_item, parent, false);

        return new EmployeeAdapter.MyViewHolder1(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final EmployeeAdapter.MyViewHolder1 holder, final int i) {

        holder.textView.setText("Name: " + employeeArray.get(i).getName() + "\nEmployee No: " + employeeArray.get(i).getEmployeeNo());
        holder.textView.setBackgroundColor(employeeArray.get(i).isSelected() ? context.getResources().getColor(R.color.colorPrimary) : Color.WHITE);

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employeeArray.get(i).setSelected(!employeeArray.get(i).isSelected());
                if(employeeArray.get(i).isSelected())
                holder.textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick_point, 0);
                else
                    holder.textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                holder.textView.setBackgroundColor(employeeArray.get(i).isSelected() ? context.getResources().getColor(R.color.colorPrimary) : Color.WHITE);
            }
        });
        if (fragment instanceof HolidayAttendance)
        {
            for (int j = 0; j < employeeArray.size(); j++) {
                if (!employeeArray.get(j).isSelected()) {
                    Log.e("False", i + "");
                }
            }
            if (employeeArray.get(i).getWeekOff().matches("No")) {
                holder.textView.setTextColor(context.getResources().getColor(R.color.green_500));
            } else {
                holder.textView.setTextColor(context.getResources().getColor(R.color.deep_orange_500));
            }
        }
        // holder.bind();
    }

    @Override
    public int getItemCount() {
        return employeeArray.size();
    }


    public class MyViewHolder1 extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder1(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview);
        }

        void bind() {

            if (pos == -1) {
                textView.setBackgroundColor(context.getResources().getColor(R.color.white));
            } else {
                if (pos == getAdapterPosition()) {
                    textView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    textView.setTextColor(context.getResources().getColor(R.color.black));
                } else {
                    textView.setBackgroundColor(context.getResources().getColor(R.color.white));
                    if (fragment instanceof HolidayAttendance) {
                        if (employeeArray.get(pos).getWeekOff().matches("No")) {
                            textView.setTextColor(context.getResources().getColor(R.color.green_500));
                        } else {
                            textView.setTextColor(context.getResources().getColor(R.color.deep_orange_500));
                        }
                    }
                }
            }

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Adapter Position", getAdapterPosition() + "");
                    textView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    textView.setTextColor(context.getResources().getColor(R.color.black));
                    if (pos != getAdapterPosition()) {
                        notifyItemChanged(pos);
                        pos = getAdapterPosition();
                    }
                    onButtonClick.addButtonClick(pos);
                }
            });
        }

    }

    public interface OnButtonClick {
        void addButtonClick(int pos);
    }

    public void updateList(List<Employee> list) {
        employeeArray = list;
        notifyDataSetChanged();
    }

}

