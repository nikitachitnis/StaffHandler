package com.miscos.staffhandler.hrms_management.EmployeeHRMS;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miscos.staffhandler.R;

import java.util.ArrayList;

public class Leave_structure_adapter_for_emp extends RecyclerView.Adapter<Leave_structure_adapter_for_emp.Leaveholder>{

   public ArrayList<LeaveStruct> leaveStructs = new ArrayList<>();
    Context context;
    String call_from;

    String[] Leaves = new String[]{
            "Full Day",
            "Half Day"};
    public Leave_structure_adapter_for_emp(ArrayList<LeaveStruct> leaveStructs, Context context,String call_from)
    {
        this.leaveStructs = leaveStructs;
        this.context=context;
        this.call_from=call_from;
    }

    @NonNull
    @Override
    public Leaveholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_layout_leave_structure,parent,false);

        return new Leaveholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Leaveholder holder, int position)
    {
        LeaveStruct leaveStruct = leaveStructs.get(position);
        if(leaveStruct.getLeaveType().equalsIgnoreCase("monthly"))
        {
            if(call_from.equalsIgnoreCase("addEmp"))
            {
                holder.txttotalleaves.setText(leaveStruct.getEmployerAssignNoOfLeaves());

            }

            else
                holder.txttotalleaves.setText(leaveStruct.getEmployeeHavingNoOfLeaves());
        }
        else
        {
            if(call_from.equalsIgnoreCase("addEmp"))
                holder.txttotalleaves.setText("0");
            else
                holder.txttotalleaves.setText(leaveStruct.getEmployeeHavingNoOfLeaves());
        }
        holder.txtleavetype.setText(leaveStruct.getLeaveType());
        holder.txtleavename.setText(leaveStruct.getLeaveName());


      /*  holder.imgremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //show edit dialog
                final PrettyDialog dialog=    new PrettyDialog(context);
                dialog
                        .setTitle("Remove")
                        .setMessage("Do you want to remove this?")
                        .setIcon(R.drawable.info,R.color.primaryTextColor,null)
                        .addButton("Yes", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick()
                            {
                                leaveStructs.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .addButton("No", R.color.whiteTextColor, R.color.red, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });*/

        holder.imgedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show edit dialog
                showDialogAddLeaveStruct(position);
            }
        });
    }
    public void showDialogAddLeaveStruct(int position)
    {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.addleave);
        EditText edtleavename=dialog.findViewById(R.id.edtleavename);
        edtleavename.setEnabled(false);
        Spinner spinleaves=dialog.findViewById(R.id.spinNoOfleaves);
        RadioGroup rdgroup1,rdgroup2;

        RadioButton rdbtnyes,rdbtnno,rdbtnmonthly,rdbtnas;
        rdbtnyes=dialog.findViewById(R.id.rdbtnYes);
        rdbtnno=dialog.findViewById(R.id.rdbtnno);
        Spinner spinday=dialog.findViewById(R.id.spindays);
        rdbtnmonthly=dialog.findViewById(R.id.rdbtnmonthly);
        rdbtnas=dialog.findViewById(R.id.rdbtnasrequired);
       TextView textView3=dialog.findViewById(R.id.textView3);
        textView3.setVisibility(View.GONE);
       TextView txttype=dialog.findViewById(R.id.txttype);
        txttype.setVisibility(View.GONE);
        ArrayList<String> strings=new ArrayList<>();
        for(int i=00;i<=Integer.parseInt(leaveStructs.get(position).getEmployerAssignNoOfLeaves());i++)
        {
            strings.add(i+"");
        }
        ArrayAdapter<String> spinnerArrayAdapter3= new ArrayAdapter<String>(
                context,R.layout.layout_spinner,strings);

        spinnerArrayAdapter3.setDropDownViewResource(R.layout.layout_spinner);
        spinleaves.setAdapter(spinnerArrayAdapter3);
        spinleaves.setSelection(strings.indexOf(leaveStructs.get(position).getEmployeeHavingNoOfLeaves()));
        rdgroup1=dialog.findViewById(R.id.rdgroupCF);
        rdgroup2=dialog.findViewById(R.id.rdgroupleaveType);
        rdgroup1.setVisibility(View.GONE);
        rdgroup2.setVisibility(View.GONE);
        LinearLayout lv_resetdate=dialog.findViewById(R.id.lv_resetdate);
        lv_resetdate.setVisibility(View.GONE);
        edtleavename.setText(leaveStructs.get(position).getLeaveName());
        if(leaveStructs.get(position).getCarryForwardable().equalsIgnoreCase("F"))
        {
            rdbtnyes.setChecked(true);
        }
        else
            rdbtnno.setChecked(true);

        if(leaveStructs.get(position).getLeaveType().equalsIgnoreCase("Monthly"))
        {
            rdbtnmonthly.setChecked(true);
        }
        else
            rdbtnas.setChecked(true);
        ArrayAdapter<String> spinadapter= new ArrayAdapter<String>(
                context,R.layout.layout_spinner,Leaves);



        spinadapter.setDropDownViewResource(R.layout.layout_spinner);
        spinday.setAdapter(spinadapter);
        spinday.setEnabled(false);
if(leaveStructs.get(position).getType().equalsIgnoreCase("1"))
    spinday.setSelection(0);
else
    spinday.setSelection(1);
        Button btnsubmit,btncancel;
        btnsubmit=dialog.findViewById(R.id.btnadd);
        btnsubmit.setText("Save");
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                leaveStructs.get(position).setEmployeeHavingNoOfLeaves(spinleaves.getSelectedItem().toString());
                notifyDataSetChanged();
                Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        btncancel=dialog.findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });



        dialog.show();





    }
    @Override
    public int getItemCount() {

        return leaveStructs.size();
    }
    public class Leaveholder extends RecyclerView.ViewHolder
    {
        TextView txtleavename,txttotalleaves,txtleavetype,textView3,txttype;
        ImageView imgCF,imgedit,imgremove;
        public Leaveholder(@NonNull View itemView)
        {
            super(itemView);
            txtleavename = (TextView)itemView.findViewById(R.id.txtleave_name);
            txtleavetype = (TextView)itemView.findViewById(R.id.txtleavetype);
            txtleavetype.setVisibility(View.VISIBLE);
            txttotalleaves = (TextView)itemView.findViewById(R.id.spTotalLeave);
            imgCF=itemView.findViewById(R.id.imgcarryFrwd);
            imgCF.setVisibility(View.GONE);
            imgedit=itemView.findViewById(R.id.imgedit);
            imgremove=itemView.findViewById(R.id.imgremove);
            imgremove.setVisibility(View.GONE);

        }
    }

}
