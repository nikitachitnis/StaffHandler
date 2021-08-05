package com.miscos.staffhandler.shiftmanagement.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.shiftmanagement.fragments.HolidayAttendance;
import com.miscos.staffhandler.shiftmanagement.models.Employee;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class EmployeeAdapter2 extends RecyclerView.Adapter<EmployeeAdapter2.MyViewHolder1> {

    Context context;
    List<Employee> employeeArray;
    OnButtonClick onButtonClick;
    int pos = -1;
    Activity fragment;

    public void ClickListener(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    public EmployeeAdapter2(Context context, List<Employee> projectsArray, Activity fragment) {
        this.fragment = fragment;
        this.employeeArray = projectsArray;
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee_recycler_item, parent, false);

        return new MyViewHolder1(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder1 holder, final int i) {

        holder.textView.setText("Name: " + employeeArray.get(i).getName() + "\nEmployee No : " + employeeArray.get(i).getEmployeeNo());
        if (fragment instanceof HolidayAttendance)
        {

            holder.imageView.setVisibility(View.VISIBLE);

            if (employeeArray.get(i).getWeekOff().matches("No"))
            {
                    holder.imageView.setImageResource(R.drawable.circle2);

            } else {
                holder.imageView.setImageResource(R.drawable.circle1);
            }
        }
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return employeeArray.size();
    }


    public class MyViewHolder1 extends RecyclerView.ViewHolder
    {
        TextView textView;
ImageView imageView;
        public MyViewHolder1(@NonNull View itemView)
        {
            super(itemView);
            textView = itemView.findViewById(R.id.textview);
            imageView=itemView.findViewById(R.id.imgweekoff);
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
                            imageView.setImageResource(R.drawable.circle2);
                        } else {
                            imageView.setImageResource(R.drawable.circle1);
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

    public void updateList(List<Employee> list){
        employeeArray = list;
        notifyDataSetChanged();
    }

}
