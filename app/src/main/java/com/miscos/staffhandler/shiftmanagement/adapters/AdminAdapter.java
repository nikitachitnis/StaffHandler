package com.miscos.staffhandler.shiftmanagement.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.shiftmanagement.fragments.AdminSettings;
import com.miscos.staffhandler.shiftmanagement.models.Admin;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.MyViewHolder1> {

    Context context;
    List<Admin> employeeArray;
    OnButtonClick onButtonClick;
    int pos = -1;
    ProgressDialog progressBar;

    public void ClickListener(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    public AdminAdapter(Context context, List<Admin> projectsArray, ProgressDialog progressBar) {
        this.employeeArray = projectsArray;
        this.context = context;
        this.progressBar=progressBar;
    }

    @NonNull
    @Override
    public AdminAdapter.MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee_recycler_item, parent, false);

        return new AdminAdapter.MyViewHolder1(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdminAdapter.MyViewHolder1 holder, final int i) {

        holder.textView.setText("Name: " + employeeArray.get(i).getName() + "\nEmployee No: " + employeeArray.get(i).getEmployeeNo());
        AdminSettings.tvNoData.setVisibility(View.GONE);
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return employeeArray.size();
    }


    public class MyViewHolder1 extends RecyclerView.ViewHolder {
        TextView textView;
        //ImageView remove;

        public MyViewHolder1(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview);
            // remove = itemView.findViewById(R.id.remove);
        }

        void bind() {

            if (pos == -1) {
                textView.setBackgroundColor(context.getResources().getColor(R.color.white));
            } else {
                if (pos == getAdapterPosition()) {
                    textView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    itemView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                } else {
                    textView.setBackgroundColor(context.getResources().getColor(R.color.white));
                    itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
                }
            }

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Adapter Position", getAdapterPosition() + "");
                    textView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    itemView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
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

    public void updateList(List<Admin> list){
        employeeArray = list;
        notifyDataSetChanged();
    }

}
