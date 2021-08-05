package com.miscos.staffhandler.hrms_management.hrms_model;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miscos.staffhandler.hrms_management.PolicyAndConfiguration;
import com.miscos.staffhandler.R;

import java.util.ArrayList;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class Salary_structure_adapter extends RecyclerView.Adapter<Salary_structure_adapter.Leaveholder>{

   public ArrayList<salary_structure> salary_structures = new ArrayList<>();
    int pos = 10;
    Context context;
    String call_from;

    public Salary_structure_adapter(ArrayList<salary_structure> salary_structures, Context context,String call_from) {
        this.salary_structures = salary_structures;
        this.context=context;
        this.call_from=call_from;
    }

    @NonNull
    @Override
    public Leaveholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_layout_sal_structure,parent,false);

        return new Leaveholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Leaveholder holder, int position)
    {


if(call_from.equalsIgnoreCase("info"))
{
    holder.txtamount.setText("₹"+salary_structures.get(position).getAmount());
    holder.txtamount.setVisibility(View.VISIBLE);
    holder.imgremove.setVisibility(View.GONE);
    holder.txtsalname.setText(salary_structures.get(position).getStruct_name());

}

else
{
    holder.txtamount.setVisibility(View.GONE);
    holder.imgremove.setVisibility(View.VISIBLE);
    if(salary_structures.get(position).getStatus().equalsIgnoreCase("transit"))
    {
        holder.txtsalname.setText(salary_structures.get(position).getStruct_name()+"- T");
    }
    else
        holder.txtsalname.setText(salary_structures.get(position).getStruct_name());
}
holder.imgremove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if(salary_structures.get(position).getStatus().equalsIgnoreCase("transit"))
                {
                    //show edit dialog
                    final PrettyDialog dialog=    new PrettyDialog(context);
                    dialog
                            .setTitle("Choose action")
                            .setMessage("What you want to do with this salary structure ?")
                            .setIcon(R.drawable.ic_logo,R.color.primaryTextColor,null)
                            .addButton("A.Deactivate for all employees", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick()
                                {
                                    if(context instanceof PolicyAndConfiguration)
                                    {
                                        int poss=((PolicyAndConfiguration) context).salary_structures.indexOf(salary_structures.get(position));
                                        Log.d("position",poss+"");
                                        ((PolicyAndConfiguration) context).salary_structures.get(poss).setStatus("deactive");
                                        ((PolicyAndConfiguration) context).getsalStruct();
                                    }



                                    Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            })
                            .addButton("B.Activate again", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    dialog.dismiss();
                                    if(context instanceof PolicyAndConfiguration)
                                    {
                                        int poss=((PolicyAndConfiguration) context).salary_structures.indexOf(salary_structures.get(position));
                                        Log.d("position",poss+"");
                                        ((PolicyAndConfiguration) context).salary_structures.get(poss).setStatus("active");
                                        ((PolicyAndConfiguration) context).getsalStruct();
                                        Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })

                            .addButton("Cancel", R.color.whiteTextColor, R.color.red, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                else
                    {
                    //show edit dialog
                    final PrettyDialog dialog=    new PrettyDialog(context);
                    dialog
                            .setTitle("Deactivate")
                            .setMessage("Please choose,What you want to do with this salary structure ?")
                            .setIcon(R.drawable.info,R.color.primaryTextColor,null)
                            .addButton("A.Deactivate for all employees", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick()
                                {
                                    if(context instanceof PolicyAndConfiguration)
                                    {
                                        int poss=((PolicyAndConfiguration) context).salary_structures.indexOf(salary_structures.get(position));
                                        Log.d("position",poss+"");
                                        ((PolicyAndConfiguration) context).salary_structures.get(poss).setStatus("deactive");
                                        ((PolicyAndConfiguration) context).getsalStruct();
                                    }



                                    Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            })
                            .addButton("B.Deactivate but continue for old employees with already configured amount", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    dialog.dismiss();
                                    if(context instanceof PolicyAndConfiguration)
                                    {
                                        int poss=((PolicyAndConfiguration) context).salary_structures.indexOf(salary_structures.get(position));
                                        Log.d("position",poss+"");
                                        ((PolicyAndConfiguration) context).salary_structures.get(poss).setStatus("transit");
                                        ((PolicyAndConfiguration) context).getsalStruct();
                                        Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })

                            .addButton("Cancel", R.color.whiteTextColor, R.color.red, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }

            }
        });

        holder.imgedit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                //show edit dialog
                showDialogAddSalaryStruct(position);
            }
        });
    }
    public void showDialogAddSalaryStruct(int index)
    {
       Dialog dialog = new Dialog(context);
// Include dialog.xml file
        dialog.setContentView(R.layout.add_salary_struc);
        dialog.show();
        //dialog.setCancelable(false);
        Button buttonSubmit = (Button) dialog.findViewById(R.id.btnaddsalaryStruct);
        Button btncancel = (Button) dialog.findViewById(R.id.btncancel);
        EditText edtamount=dialog.findViewById(R.id.edtamount);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        buttonSubmit.setText("Update");
        EditText edtsal = (EditText) dialog.findViewById(R.id.etTitle);
        edtsal.setText(salary_structures.get(index).getStruct_name());
        if(call_from.equalsIgnoreCase("info"))
        {

            edtamount.setVisibility(View.VISIBLE);
            edtsal.setText(salary_structures.get(index).getStruct_name());
            edtsal.setEnabled(false);
            edtamount.setText(salary_structures.get(index).getAmount());
        }

        buttonSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(edtsal.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(context, "Please add title", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    if(call_from.equalsIgnoreCase("add"))
                    {
                        if(context instanceof PolicyAndConfiguration)
                        {
                            int poss=((PolicyAndConfiguration) context).salary_structures.indexOf(salary_structures.get(index));
                            Log.d("position",poss+"");
                            ((PolicyAndConfiguration) context).salary_structures.get(poss).setStatus("active");
                            ((PolicyAndConfiguration) context).salary_structures.get(poss).setStruct_name(edtsal.getText().toString());
                            ((PolicyAndConfiguration) context).getsalStruct();
                        }
                    }
                    else
                    {
                        if(edtamount.getText().toString().isEmpty()||edtamount.getText().toString().equalsIgnoreCase("0"))
                        {
                            Toast.makeText(context, "Enter valid amount", Toast.LENGTH_SHORT).show();
                            return;
                        }
                       salary_structures.get(index).setAmount(edtamount.getText().toString());
                        notifyDataSetChanged();
                    }

                    Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show();

                }

                dialog.dismiss();
            }
        });
    }
    @Override
    public int getItemCount() {

        return salary_structures.size();
    }
    public class Leaveholder extends RecyclerView.ViewHolder
    {
        TextView txtsalname,txtamount;
        ImageView imgedit,imgremove;
        public Leaveholder(@NonNull View itemView)
        {
            super(itemView);
            txtsalname = (TextView)itemView.findViewById(R.id.txtsalname);
            txtamount=itemView.findViewById(R.id.txtamount);

            imgedit=itemView.findViewById(R.id.imgedit);
            imgremove=itemView.findViewById(R.id.imgremove);
        }
    }

}
