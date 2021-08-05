package com.miscos.staffhandler.hrms_management.hrms_model;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.miscos.staffhandler.hrms_management.PolicyAndConfiguration;
import com.miscos.staffhandler.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class Leave_structure_adapter extends RecyclerView.Adapter<Leave_structure_adapter.Leaveholder>{

   public ArrayList<LeaveStruct> leaveStructs = new ArrayList<>();
    int pos = 10;
    Context context;
    String carryForwadable="N",leavetype="monthly";
    String[] Leave1 = new String[]{
            "00",
            "01",
            "02",
            "03",
            "04",
            "05",
            "06",
            "07",
            "08",
            "09"
    };
    String[] Leaves = new String[]{
            "Full Day",
            "Half Day"};
    public Leave_structure_adapter(ArrayList<LeaveStruct> leaveStructs, Context context) {
        this.leaveStructs = leaveStructs;
        this.context=context;
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
        holder.txttotalleaves.setText(leaveStruct.getNoOfLeaves());
        holder.txtleavetype.setText(leaveStruct.getLeaveType());
        holder.txtleavename.setText(leaveStruct.getLeaveName());
        if(leaveStruct.getCarryForwardable().equalsIgnoreCase("F"))
        {
            holder.imgCF.setVisibility(View.VISIBLE);
        }
        else
            holder.imgCF.setVisibility(View.INVISIBLE);

        holder.imgremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!leaveStruct.getCarryForwardable().equalsIgnoreCase("F"))
                {
                    //show edit dialog
                    final PrettyDialog dialog=    new PrettyDialog(context);
                    dialog
                            .setTitle("Deactivate")
                            .setMessage("Are you sure to deacivate this leave structure as it will remove all previously given leaves to all employees?")
                            .setIcon(R.drawable.info,R.color.primaryTextColor,null)
                            .addButton("Yes", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick()
                                {
                                    long now = System.currentTimeMillis() - 1000;
                                    final Calendar cldr = Calendar.getInstance();
                                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                                    int month = cldr.get(Calendar.MONTH);
                                    int year = cldr.get(Calendar.YEAR);
                                    // date picker dialog
                                    DatePickerDialog picker = new DatePickerDialog(context,
                                            new DatePickerDialog.OnDateSetListener()
                                            {
                                                @Override
                                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                                                { int poss=((PolicyAndConfiguration) context).leaveStructs.indexOf(leaveStructs.get(position));
                                                    Log.d("position",poss+"");
                                                    ((PolicyAndConfiguration) context).leaveStructs.get(poss).setLeaveStatus("deactive%"+dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                                    ((PolicyAndConfiguration) context).getLeaveStructlist();
                                                    Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show();

                                                }
                                            }, year, month, day);
                            picker.getDatePicker().setMinDate(now);
                                    picker.setMessage("Choose Date of Deactivation");
                                    picker.show();
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
                    dialog.setCancelable(false);
                }
                else
                {
                    //show edit dialog
                    final PrettyDialog dialog=    new PrettyDialog(context);

                    dialog.setTitle("Deactivate")
                            .setMessage("Please choose,What you want to do with this leave structure ?")
                            .setIcon(R.drawable.info,R.color.primaryTextColor,null);
                           dialog.addButton("A.Deactivate for all employees", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick()
                                {
                                    long now = System.currentTimeMillis() - 1000;
                                    final Calendar cldr = Calendar.getInstance();
                                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                                    int month = cldr.get(Calendar.MONTH);
                                    int year = cldr.get(Calendar.YEAR);
                                    // date picker dialog
                                    DatePickerDialog picker = new DatePickerDialog(context,
                                            new DatePickerDialog.OnDateSetListener()
                                            {
                                                @Override
                                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                                                { int poss=((PolicyAndConfiguration) context).leaveStructs.indexOf(leaveStructs.get(position));
                                                    Log.d("position",poss+"");
                                                    ((PolicyAndConfiguration) context).leaveStructs.get(poss).setLeaveStatus("deactive%"+dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                                    ((PolicyAndConfiguration) context).getLeaveStructlist();
                                                    Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();

                                                }
                                            }, year, month, day);

                                    picker.setMessage("Choose Date of Deactivation");
                                    picker.getDatePicker().setMinDate(now);
                                    picker.show();

                                }
                            });
                            dialog.addButton("B.Deactivate & Encashment of remaining leaves", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    long now = System.currentTimeMillis() - 1000;
                                    final Calendar calendar = Calendar.getInstance();
                                    int year = calendar.get(Calendar.YEAR);
                                    int month = calendar.get(Calendar.MONTH);
                                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                                    calendar.set(Calendar.DAY_OF_MONTH, day);
                                    calendar.set(Calendar.DATE, +1);
                                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT,
                                            new DatePickerDialog.OnDateSetListener() {
                                                @Override
                                                public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                                    int poss=((PolicyAndConfiguration) context).leaveStructs.indexOf(leaveStructs.get(position));
                                                    Log.d("position",poss+"");
                                                    ((PolicyAndConfiguration) context).leaveStructs.get(poss).setLeaveStatus("encashment%01-"+month+"-"+year);
                                                    ((PolicyAndConfiguration) context).getLeaveStructlist();
                                                    Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                }
                                            }, year, month, day);
                                    ((ViewGroup) datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
                                    datePickerDialog.getDatePicker().setMinDate(now);
                                    datePickerDialog.setMessage("Choose Month of Encashment");
                                    datePickerDialog.show();



                                }
                            });
                            if(leaveStructs.size()>1)
                            {

                                dialog.addButton("C.Deactivate & merge existing leaves with other leave", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                    @Override
                                    public void onClick() {


                                        final PrettyDialog prettyDialog = new PrettyDialog(context);
                                        prettyDialog.setCancelable(false);
                                        prettyDialog
                                                .setTitle("Deactivation")
                                                .setMessage("Please choose,After deactivation all remaining "+"'"+leaveStructs.get(position).getLeaveName()+"'"+" leaves will be merge with ?")
                                                .setIcon(R.drawable.info, R.color.white, null);
                                               for(LeaveStruct leaveStruct1:leaveStructs)
                                               {
                                                   if(leaveStructs.get(position).getLeaveName().equals(leaveStruct1.getLeaveName()))
                                                       continue;
                                                   prettyDialog.addButton(leaveStruct1.getLeaveName(), R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                                       @Override
                                                       public void onClick()
                                                       {
                                                           int poss=((PolicyAndConfiguration) context).leaveStructs.indexOf(leaveStructs.get(position));
                                                           Log.d("position",poss+"");
                                                           ((PolicyAndConfiguration) context).leaveStructs.get(poss).setLeaveStatus("merge%"+leaveStruct1.getLeaveName());
                                                           ((PolicyAndConfiguration) context).getLeaveStructlist();
                                                           Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show();
                                                           prettyDialog.dismiss();
                                                           dialog.dismiss();
                                                       }
                                                   });
                                               }
                                        prettyDialog.addButton("Cancel", R.color.whiteTextColor, R.color.red, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick()
                                            {
                                              prettyDialog.dismiss();

                                            }
                                        });
                                                prettyDialog.show();

                                    }
                                });
                            }


                                        dialog.addButton("Cancel", R.color.whiteTextColor, R.color.red, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        dialog.dismiss();
                    }
                });
                            dialog.setCancelable(false);
                           dialog.show();

                }

            }
        });

        holder.imgedit.setOnClickListener(new View.OnClickListener()
        {
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
        Spinner spinleaves=dialog.findViewById(R.id.spinNoOfleaves);
        RadioGroup rdgroup1,rdgroup2;
        RadioButton rdbtnyes,rdbtnno,rdbtnmonthly,rdbtnas;
        rdbtnyes=dialog.findViewById(R.id.rdbtnYes);
        rdbtnno=dialog.findViewById(R.id.rdbtnno);
        Spinner spinday=dialog.findViewById(R.id.spindays);
        rdbtnmonthly=dialog.findViewById(R.id.rdbtnmonthly);
        rdbtnas=dialog.findViewById(R.id.rdbtnasrequired);
        ArrayList<String> strings=new ArrayList<>();
        strings.addAll(Arrays.asList(Leave1));
        for(int i=10;i<=90;i+=10)
        {
            strings.add(i+"");
        }
        ArrayAdapter<String> spinnerArrayAdapter3= new ArrayAdapter<String>(
                context,R.layout.layout_spinner,strings);

        spinnerArrayAdapter3.setDropDownViewResource(R.layout.layout_spinner);
        spinleaves.setAdapter(spinnerArrayAdapter3);
        spinleaves.setSelection(strings.indexOf(leaveStructs.get(position).getNoOfLeaves()));
        rdgroup1=dialog.findViewById(R.id.rdgroupCF);
        rdgroup2=dialog.findViewById(R.id.rdgroupleaveType);
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
        EditText editText=dialog.findViewById(R.id.edtresetdate);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                            {
                                editText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        spinadapter.setDropDownViewResource(R.layout.layout_spinner);
        spinday.setAdapter(spinadapter);
if(leaveStructs.get(position).getType().equalsIgnoreCase("1"))
    spinday.setSelection(0);
else
    spinday.setSelection(1);
        Button btnsubmit,btncancel;
        btnsubmit=dialog.findViewById(R.id.btnadd);
        btnsubmit.setText("Update");
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(edtleavename.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(context, "Leave name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                int poss=((PolicyAndConfiguration) context).leaveStructs.indexOf(leaveStructs.get(position));
                Log.d("position",poss+"");

                ((PolicyAndConfiguration) context).leaveStructs.get(poss).setLeaveName(edtleavename.getText().toString());
                ((PolicyAndConfiguration) context).leaveStructs.get(poss).setCarryForwardable(carryForwadable);
                ((PolicyAndConfiguration) context).leaveStructs.get(poss).setLeaveType(leavetype);
                ((PolicyAndConfiguration) context).leaveStructs.get(poss).setNoOfLeaves(spinleaves.getSelectedItem().toString());
                ((PolicyAndConfiguration) context).leaveStructs.get(poss).setType("1");
                ((PolicyAndConfiguration) context).getLeaveStructlist();
                Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show();
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
        rdgroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                switch (radioGroup.getCheckedRadioButtonId())
                {
                    case R.id.rdbtnYes:
                        carryForwadable="F";
                        break;
                    case R.id.rdbtnno:
                        carryForwadable="N";
                        break;
                }
            }
        });
        rdgroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                switch (radioGroup.getCheckedRadioButtonId())
                {
                    case R.id.rdbtnmonthly:
                        leavetype="Monthly";
                        break;
                    case R.id.rdbtnasrequired:
                        leavetype="As-required";
                        break;
                }
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
        TextView txtleavename,txttotalleaves,txtleavetype;
        ImageView imgCF,imgedit,imgremove;
        public Leaveholder(@NonNull View itemView)
        {
            super(itemView);
            txtleavename = (TextView)itemView.findViewById(R.id.txtleave_name);
            txtleavetype = (TextView)itemView.findViewById(R.id.txtleavetype);
            txttotalleaves = (TextView)itemView.findViewById(R.id.spTotalLeave);
            imgCF=itemView.findViewById(R.id.imgcarryFrwd);
            imgedit=itemView.findViewById(R.id.imgedit);
            imgremove=itemView.findViewById(R.id.imgremove);
        }
    }

}
