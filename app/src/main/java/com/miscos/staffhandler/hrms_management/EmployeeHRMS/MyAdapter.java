package com.miscos.staffhandler.hrms_management.EmployeeHRMS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.miscos.staffhandler.R;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter{

    Context context;
    ArrayList<SalaryStructItem> salaryStructItems = new ArrayList<>();

    public MyAdapter(Context context,ArrayList<SalaryStructItem> objects)
    {
        this.context=context;
        salaryStructItems = objects;
    }

    @Override
    public int getCount() {
        return salaryStructItems.size();
    }

    @Override
    public Object getItem(int i) {
        return salaryStructItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.add_salary_struc, null);
        TextView textView = (TextView) v.findViewById(R.id.txtname);
        EditText editText = (EditText) v.findViewById(R.id.txtamount);
       textView.setText(salaryStructItems.get(position).getName()+"(₹)");
       editText.setText(salaryStructItems.get(position).getAmount());
        return v;

    }

}