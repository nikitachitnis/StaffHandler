package com.miscos.staffhandler.hrms_management.SalTransferNCalculations;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.Urls.App_Urls;
import com.miscos.staffhandler.helper.CurrencyCal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class salarycalc_adapter_for_emp extends RecyclerView.Adapter<salarycalc_adapter_for_emp.SalHoder>{

   public ArrayList<AmountList> amountLists = new ArrayList<>();
    Context context;
    ProgressDialog progressDialog;
    double totalamount=0.0;
    EditText edtotherdedutions;


    public salarycalc_adapter_for_emp(ArrayList<AmountList> amountLists, Context context)
    {
        this.amountLists = amountLists;
        this.context=context;
    }

    @NonNull
    @Override
    public SalHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.sal_struc_view,parent,false);
       // LeaveTextWatcher leaveTextWatcher=new LeaveTextWatcher();
        return new SalHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalHoder holder, int position)
    {
        AmountList amountList=amountLists.get(position);
    /*    holder.leaveTextWatcher.setPosition(position);*/
        holder.txtempname.setText(amountList.getEmployeeName());
        holder.txtempid.setText(amountList.getEmployee_no());
        holder.edtactualsalary.setText(amountList.getActualSalary()+"");
        holder.edtotherdedutions.setText(amountList.getOtherDeductionAmt()+"");
        holder.edttaxdeduction.setText(amountList.getTaxs()+"");
        holder.edtleavededuction.setText(amountList.getLeaveDeduction()+"");
        holder.edtremainingsal.setText(amountList.getRemainingSalary()+"");
        holder.edttotalamount.setText(amountList.getRemainingSalary()+"");
        holder.edtnetpayable.setText(amountList.getNet_salary()+"");
        holder.edtleavededuction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if(!holder.edtleavededuction.getText().toString().isEmpty())
                {
                    double dd= Double.parseDouble(holder.edtleavededuction.getText().toString());
                    if(dd>amountLists.get(position).getLeaveDeduction()&&amountLists.get(position).getRemainingSalary()==0.0)
                    {
                        holder.edtleavededuction.setText(amountLists.get(position).getLeaveDeduction()+"");
                        return;
                    }
                    else if(dd>amountLists.get(position).getRemainingSalary())
                    {
                        holder.edtleavededuction.setText(amountLists.get(position).getLeaveDeduction()+"");
                       // Toast.makeText(context, "can not be less than remaining salary", Toast.LENGTH_SHORT).show();
                        return;
                    }
                holder.edttotalamount.setText(""+getTotalAmount(Double.parseDouble(holder.edtremainingsal.getText().toString()),
                        holder.edtleavededuction.getText().toString(),holder.edttaxdeduction.getText().toString(),
                        holder.edtotherdedutions.getText().toString(),position));
                holder.btnpay.setVisibility(View.VISIBLE);

                }

            }
        });
        holder.edttaxdeduction.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {

                if(!editable.toString().isEmpty())
                {
                   double dd= Double.parseDouble(holder.edttaxdeduction.getText().toString());
                    if(dd>amountList.getTaxs()&&amountList.getRemainingSalary()==0.0)
                    {
                       holder.edttaxdeduction.setText(amountList.getTaxs()+"");
                       return;
                    }
                    holder.edttotalamount.setText(""+getTotalAmount(amountList.getRemainingSalary(),
                            holder.edtleavededuction.getText().toString(),holder.edttaxdeduction.getText().toString(),
                            holder.edtotherdedutions.getText().toString(),position));
                    holder.btnpay.setVisibility(View.VISIBLE);


                }
                     }
        });
        holder.edtotherdedutions.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if(!holder.edtotherdedutions.getText().toString().isEmpty())
                {
                    double dd= Double.parseDouble(holder.edtotherdedutions.getText().toString());
                    if(dd>amountList.getOtherDeductionAmt()&&amountList.getRemainingSalary()==0.0)
                    {
                        holder.edtotherdedutions.setText(amountList.getTaxs()+"");
                        return;
                    }
                    holder.edttotalamount.setText(""+getTotalAmount(amountList.getRemainingSalary(),
                            holder.edtleavededuction.getText().toString(),holder.edttaxdeduction.getText().toString(),
                            holder.edtotherdedutions.getText().toString(),position));
                    holder.btnpay.setVisibility(View.VISIBLE);


                }
               /* if(!holder.edtotherdedutions.getText().toString().isEmpty())
                {
                    amountList.setOtherDeductionAmt(Double.parseDouble(holder.edtotherdedutions.getText().toString()));
                    holder.edttotalamount.setText(""+getTotalAmount(amountList.getActualSalary(),
                            holder.edtleavededuction.getText().toString(),holder.edttaxdeduction.getText().toString(),
                           holder.edtotherdedutions.getText().toString(),position));

                }*/
            }
        });
holder.btnadditionalpay.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view)
    {
        totalamount=0.0;
        String cureentmon=((SalaryCalculationAndTransfer)context).applicable_date;
        getTaxDetails("get_additional_payment_detail",cureentmon,amountList.getEmployeeId(),amountList.getEmployerId(),totalamount+"",amountList.getEmployeeName(),null,position);

    }
});
      
        holder.btnpay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                double v=Double.parseDouble(holder.edttotalamount.getText().toString());
                double leave_details=Double.parseDouble(holder.edtleavededuction.getText().toString());
                double tax_details=Double.parseDouble(holder.edttaxdeduction.getText().toString());
                double other_details=Double.parseDouble(holder.edtotherdedutions.getText().toString());
                if(v>amountList.getRemainingSalary()&&leave_details==amountList.getLeaveDeduction()&&tax_details==amountList.getTaxs()&&other_details==amountList.getOtherDeductionAmt())
                {
                    final PrettyDialog dialog2=    new PrettyDialog(context);
                    dialog2.setTitle("Warning!")
                            .setMessage("Payable amount is greater than net salary calculated,please change deduction amount in order to proceed")
                            .setIcon(R.drawable.info,R.color.primaryTextColor,null)
                            .addButton("ok", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick()
                                {
                                    dialog2.dismiss();
                                }
                            }).show();

                }
                else if(v>amountList.getRemainingSalary()&&(leave_details!=amountList.getLeaveDeduction()||tax_details!=amountList.getTaxs()||other_details!=amountList.getOtherDeductionAmt())||v<amountList.getRemainingSalary()&&(leave_details!=amountList.getLeaveDeduction()||tax_details!=amountList.getTaxs()||other_details!=amountList.getOtherDeductionAmt()))
                {
                    final PrettyDialog dialog2=    new PrettyDialog(context);
                    dialog2.setTitle("Warning")
                            .setMessage("Changes made in deductions can not be revert back ,are you sure to continue payment?")
                            .setIcon(R.drawable.info,R.color.primaryTextColor,null)
                            .addButton("Yes", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick()
                                {
                                    double lvdiff=amountList.getLeaveDeduction()-Double.parseDouble(holder.edtleavededuction.getText().toString());
                                    double taxdiff=amountList.getTaxs()-Double.parseDouble(holder.edttaxdeduction.getText().toString());
                                    double otherdiff=amountList.getOtherDeductionAmt()-Double.parseDouble(holder.edtotherdedutions.getText().toString());

                                    double new_payment=amountList.getNet_salary()+(lvdiff+taxdiff+otherdiff);
                                    dialog2.dismiss();
                                    if(new_payment<v)
                                    {
                                        final PrettyDialog dialog2=    new PrettyDialog(context);
                                        dialog2.setTitle("Warning!")
                                                .setMessage("Payable amount is greater than net salary calculated,please change amount in order to proceed")
                                                .setIcon(R.drawable.info,R.color.primaryTextColor,null)
                                                .addButton("ok", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                                    @Override
                                                    public void onClick()
                                                    {
                                                        dialog2.dismiss();
                                                    }
                                                }).show();
                                        return;
                                    }

                                    String deductionstr=holder.edtleavededuction.getText().toString()+"@"+holder.edttaxdeduction.getText().toString()+"@"+holder.edtotherdedutions.getText().toString();
                                    showConfirmPayment(position,holder.edttotalamount.getText().toString(),"yes",new_payment+"",deductionstr);
                                }
                            }).addButton("No", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                        @Override
                        public void onClick()
                        {
                            dialog2.dismiss();
                        }
                    }).show();
                }
                else if(v<amountList.getRemainingSalary()&&(leave_details!=amountList.getLeaveDeduction()||tax_details!=amountList.getTaxs()||other_details!=amountList.getOtherDeductionAmt()))
                {
                    final PrettyDialog dialog2=    new PrettyDialog(context);
                    dialog2.setTitle("Warning")
                            .setMessage("Changes made in deductions can not be revert back ,are you sure to continue payment?")
                            .setIcon(R.drawable.info,R.color.primaryTextColor,null)
                            .addButton("Yes", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick()
                                {
                                    double lvdiff=amountList.getLeaveDeduction()-Double.parseDouble(holder.edtleavededuction.getText().toString());
                                    double taxdiff=amountList.getTaxs()-Double.parseDouble(holder.edttaxdeduction.getText().toString());
                                    double otherdiff=amountList.getOtherDeductionAmt()-Double.parseDouble(holder.edtotherdedutions.getText().toString());

                                    double new_payment=amountList.getNet_salary()+(lvdiff+taxdiff+otherdiff);
                                    //double new_payment=amountList.getActualSalary()-(Double.parseDouble(holder.edtleavededuction.getText().toString())+ Double.parseDouble(holder.edttaxdeduction.getText().toString())+Double.parseDouble(holder.edtotherdedutions.getText().toString()));
                                    dialog2.dismiss();
                                    if(new_payment<v)
                                    {
                                        final PrettyDialog dialog2=    new PrettyDialog(context);
                                        dialog2.setTitle("Warning!")
                                                .setMessage("Payable amount is greater than net salary calculated,please change amount in order to proceed")
                                                .setIcon(R.drawable.info,R.color.primaryTextColor,null)
                                                .addButton("ok", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                                    @Override
                                                    public void onClick()
                                                    {
                                                        dialog2.dismiss();
                                                    }
                                                }).show();
                                        return;
                                    }

                                    String deductionstr=holder.edtleavededuction.getText().toString()+"@"+holder.edttaxdeduction.getText().toString()+"@"+holder.edtotherdedutions.getText().toString();

                                    showConfirmPayment(position,holder.edttotalamount.getText().toString(),"yes",new_payment+"",deductionstr);
                                }
                            }).addButton("No", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                        @Override
                        public void onClick()
                        {
                            dialog2.dismiss();
                        }
                    }).show();
                }
                else
                {
                    double new_payment=amountList.getNet_salary()-(Double.parseDouble(holder.edtleavededuction.getText().toString())+
                            Double.parseDouble(holder.edttaxdeduction.getText().toString())
                            +Double.parseDouble(holder.edtotherdedutions.getText().toString()));
                    String deductionstr=holder.edtleavededuction.getText().toString()+"@"+holder.edttaxdeduction.getText().toString()+"@"+holder.edtotherdedutions.getText().toString();

                    showConfirmPayment(position,holder.edttotalamount.getText().toString(),"no",new_payment+"",deductionstr);
                }


                //
            }
        });
        holder.imgtaxinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)

            {
                if(context instanceof SalaryCalculationAndTransfer)
                {
                    String cureentmon=((SalaryCalculationAndTransfer)context).applicable_date;
                    getTaxDetails("show_tax_details",cureentmon,amountList.getEmployeeId(),amountList.getEmployerId(),amountList.getTaxs()+"",amountList.getEmployeeName(),holder.edttaxdeduction,position);
                }

            }
        });
        holder.imgotherinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                totalamount=amountList.getOtherDeductionAmt();
                String cureentmon=((SalaryCalculationAndTransfer)context).applicable_date;
                getTaxDetails("get_other_deduction_detail",cureentmon,amountList.getEmployeeId(),amountList.getEmployerId(),totalamount+"",amountList.getEmployeeName(),holder.edtotherdedutions,position);
                //showOtherDeductions("",amountList.getOtherDeductionAmt()+"",amountList.getEmployeeName(),amountList.getEmployeeId());
            }
        });
        holder.imgleaveinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            { String cureentmon=((SalaryCalculationAndTransfer)context).applicable_date;
                getTaxDetails("show_leave_details",cureentmon,amountList.getEmployeeId(),amountList.getEmployerId(),"0",amountList.getEmployeeName(),null,position);

            }
        });
        if(amountList.getRemainingSalary()>0)
        {
            holder.btnpay.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.btnpay.setVisibility(View.INVISIBLE);
        }

        /*if(amountList.getSettlement().equalsIgnoreCase("yes"))
        {
            holder.edttaxdeduction.setEnabled(false);
            edtleavededuction.setEnabled(false);
            holder.edtotherdedutions.setEnabled(false);
        }
        else
        {
            holder.edttaxdeduction.setEnabled(true);
            edtleavededuction.setEnabled(true);
            holder.edtotherdedutions.setEnabled(true);
        }*/


    }

    @Override
    public void onViewAttachedToWindow(@NonNull SalHoder holder) 
    {
        super.onViewAttachedToWindow(holder);
       // edtleavededuction.addTextChangedListener(holder.leaveTextWatcher);
        
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull SalHoder holder) 
    {
        super.onViewDetachedFromWindow(holder);
        //edtleavededuction.removeTextChangedListener(holder.leaveTextWatcher);
    }

    double getTotalAmount(double actualsal, String leavdeduc, String taxdeduct, String otherdeduct, int position)
    {
         double dtotal_sal_payaable=0.0,dactual_sal=0.0,dleaveDeduc=0.0,dtax_deduct=0.0,dother_deduct=0.0;
        try {
            dleaveDeduc = Double.parseDouble(leavdeduc);
        }catch (Exception e)
        {
            dleaveDeduc=0.0;
        }
        try {
            dother_deduct = Double.parseDouble(otherdeduct);
        }catch (Exception e)
        {
            dother_deduct=0.0;
        }
        try {
            dtax_deduct = Double.parseDouble(taxdeduct);
        }catch (Exception e)
        {
            dtax_deduct=0.0;
        }
        amountLists.get(position).setTotal(amountLists.get(position).getTotal()-(dleaveDeduc+dother_deduct+dtax_deduct));
        dleaveDeduc=amountLists.get(position).getLeaveDeduction()-dleaveDeduc;
        dother_deduct=amountLists.get(position).getOtherDeductionAmt()-dother_deduct;
        dtax_deduct=amountLists.get(position).getTaxs()-dtax_deduct;
       // Log.d("actual,l,t,o",actualsal+","+dleaveDeduc+","+dtax_deduct+","+dother_deduct);
        dtotal_sal_payaable=actualsal+(dleaveDeduc+dtax_deduct+dother_deduct);
        //Log.d("total",dtotal_sal_payaable+"");
        //amountLists.get(position).setRemainingSalary(dtotal_sal_payaable);

        //notifyDataSetChanged();
        return dtotal_sal_payaable;
    }

    void showConfirmPayment(int position,String amount,String settlement_case,String settlement_amount,String deduction_string)
    {
        AmountList amountList=amountLists.get(position);
        String cureentmon="";
        Dialog dialog = new Dialog(context);
// Include dialog.xml file
        dialog.setContentView(R.layout.dialog_for_confirmation);

        TextView txtlabel1,txtlabel2;
        txtlabel1=dialog.findViewById(R.id.txtlabel1);
        txtlabel2=dialog.findViewById(R.id.txtlabel2);
        txtlabel1.setText("You are paying ₹"+amount+"/-\n"+ CurrencyCal.convertToIndianCurrency(amount));
        if(context instanceof SalaryCalculationAndTransfer)
        {
            cureentmon=((SalaryCalculationAndTransfer)context).selectedmonthyear;
        }
        txtlabel2.setText("To "+amountList.getEmployeeName()+",Employee No : "+amountList.getEmployee_no() +"\nfor the month of "+cureentmon);
        AppCompatButton btnconfirm=dialog.findViewById(R.id.btnconfirmpay);
        btnconfirm.setEnabled(false);
        btnconfirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
                String my=((SalaryCalculationAndTransfer)context).applicable_date;
                if(settlement_case.equalsIgnoreCase("yes"))
                {

                    payment(position,my,amountList.getEmployeeId(),deduction_string.split("@")[0],deduction_string.split("@")[1],deduction_string.split("@")[2],amountList.getEmployerId(),amount,settlement_case,settlement_amount);

                }
                else
                {
                    payment(position,my,amountList.getEmployeeId(),"0","0","0",amountList.getEmployerId(),amount,settlement_case,settlement_amount);

                }

                             }
        });
        CheckBox chkconfirm=dialog.findViewById(R.id.chkconfirm);
        chkconfirm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if(b)
                {
                    btnconfirm.setEnabled(true);
                    btnconfirm.setBackgroundColor(context.getResources().getColor(R.color.blue_10));
                }

                else
                {
                    btnconfirm.setEnabled(false);
                    btnconfirm.setBackgroundColor(context.getResources().getColor(R.color.blue_10));
                }

            }
        });
        dialog.show();
    }
    void showtaxDeductions(String pf,String pt,String gst,String tds,String esic,String deductedamt,String emp_name,String emp_id,EditText editText,int pos)
    {
            double dpf=0.0,dpt=0.0,dgst=0.0,desic=0.0,dtds=0.0;
            String tax_details="";
        Dialog dialog = new Dialog(context);
// Include dialog.xml file
        dialog.setContentView(R.layout.dialog_for_tax_deductions);

        TextView txtpf1,txtpt1,txtgst1,txtesic1,txttds1,txtdeductedamount;
        EditText txtpf2,txtpt2,txtgst2,txtesic2,txttds2;
        TextView txtempname=dialog.findViewById(R.id.txtempname);
        TextView txtempid=dialog.findViewById(R.id.txtempid);
        ImageView imageView=dialog.findViewById(R.id.imgcancel);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        txtempname.setText(emp_name);
        txtempid.setText(amountLists.get(pos).getEmployee_no());
        txtpf1=dialog.findViewById(R.id.txtpf1);
        txtpf2=dialog.findViewById(R.id.txtpf2);
        txtpt1=dialog.findViewById(R.id.txtpt1);
        txtpt2=dialog.findViewById(R.id.txtpt2);
        txtesic1=dialog.findViewById(R.id.txtESIC1);
        txtesic2=dialog.findViewById(R.id.txtESIC2);
        txttds1=dialog.findViewById(R.id.txtTDS1);
        txttds2=dialog.findViewById(R.id.txtTDS2);
        txtgst1=dialog.findViewById(R.id.txtgst1);
        txtgst2=dialog.findViewById(R.id.txtgst2);
        txtpf1.setText(pf.split("#")[0].replace("=",""));
        dpf=Double.parseDouble(pf.split("#")[1]);
        tax_details="provident_found_amount="+dpf;


       txtpf2.setText(pf.split("#")[1]);
        txtpt1.setText(pt.split("#")[0].replace("=",""));
        dpt=Double.parseDouble(pt.split("#")[1]);
        tax_details=tax_details+","+"professional_tax_amount="+dpt;
        txtpt2.setText(pt.split("#")[1]);
        txtgst1.setText(gst.split("#")[0].replace("=",""));
        dgst=Double.parseDouble(gst.split("#")[1]);
        tax_details=tax_details+","+"GST_amount="+dgst;
        txtgst2.setText(gst.split("#")[1]);
        txtesic1.setText(esic.split("#")[0].replace("=",""));
        desic=Double.parseDouble(esic.split("#")[1]);
        tax_details=tax_details+","+"ESIC_amount="+desic;
        txtesic2.setText(esic.split("#")[1]);
        txttds1.setText(tds.split("#")[0].replace("=",""));
        dtds=Double.parseDouble(tds.split("#")[1]);
        tax_details=tax_details+","+"TDS_amount="+dtds;
        txttds2.setText(tds.split("#")[1]);
        amountLists.get(pos).setTax_details(tax_details);
        //notifyDataSetChanged();
        txtdeductedamount=dialog.findViewById(R.id.txtdeductedtax);
        txtdeductedamount.setText("Deducted Amount : ₹"+deductedamt+"/-");
        txtpf2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if(!txtpf2.getText().toString().equalsIgnoreCase(""))
                {
                    if(Double.parseDouble(txtpf2.getText().toString())>Double.parseDouble(pf.split("#")[1]))
                    {
                        txtpf2.setText(pf.split("#")[1]);
                        return;
                    }
                    getTaxtotal(txtpf2.getText().toString(),txtpt2.getText().toString(),txtgst2.getText().toString(),txttds2.getText().toString(),txtesic2.getText().toString(),txtdeductedamount,editText,pos);

                }


            }
        });
        txtpt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if(!txtpt2.getText().toString().equalsIgnoreCase(""))
                {
                    if(Double.parseDouble(txtpt2.getText().toString())>Double.parseDouble(pt.split("#")[1]))
                    {
                        txtpt2.setText(pt.split("#")[1]);
                        return;
                    }
                    getTaxtotal(txtpf2.getText().toString(),txtpt2.getText().toString(),txtgst2.getText().toString(),txttds2.getText().toString(),txtesic2.getText().toString(),txtdeductedamount,editText,pos);

                }

            }
        });
        txtesic2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if(!txtesic2.getText().toString().equalsIgnoreCase(""))
                {
                    if(Double.parseDouble(txtesic2.getText().toString())>Double.parseDouble(esic.split("#")[1]))
                    {
                        txtesic2.setText(esic.split("#")[1]);
                        return;
                    }
                    getTaxtotal(txtpf2.getText().toString(),txtpt2.getText().toString(),txtgst2.getText().toString(),txttds2.getText().toString(),txtesic2.getText().toString(),txtdeductedamount,editText,pos);

                }

            }
        });
        txttds2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if(!txttds2.getText().toString().equalsIgnoreCase(""))
                {
                    if(Double.parseDouble(txttds2.getText().toString())>Double.parseDouble(tds.split("#")[1]))
                    {
                        txttds2.setText(tds.split("#")[1]);
                        return;
                    }
                    getTaxtotal(txtpf2.getText().toString(),txtpt2.getText().toString(),txtgst2.getText().toString(),txttds2.getText().toString(),txtesic2.getText().toString(),txtdeductedamount,editText,pos);

                }

            }
        });
        txtgst2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!txtgst2.getText().toString().equalsIgnoreCase(""))
                {
                    if(Double.parseDouble(txtgst2.toString())>Double.parseDouble(gst.split("#")[1]))
                    {
                        txtgst2.setText(gst.split("#")[1]);
                        return;
                    }
                    getTaxtotal(txtpf2.getText().toString(),txtpt2.getText().toString(),txtgst2.getText().toString(),txttds2.getText().toString(),txtesic2.getText().toString(),txtdeductedamount,editText,pos);

                }

            }
        });
        dialog.setCancelable(false);


        dialog.show();
    }
    void getTaxtotal(String pf,String pt,String gst,String tds,String esic,TextView textView,EditText editText,int pos)
    {
        Double aDouble=Double.parseDouble(pf)+Double.parseDouble(pt)+Double.parseDouble(gst)+Double.parseDouble(tds)+Double.parseDouble(esic);

        amountLists.get(pos).setTax_details("provident_found_amount="+pf+",professional_tax_amount="+pt+",ESIC_amount="+esic+",GST_amount="+gst+",TDS_amount="+tds);
        //notifyDataSetChanged();
        textView.setText("Deducted Amount : ₹"+aDouble+"/-");
        editText.setText(aDouble+"");

    }
    void showLeaveDeductions(String data,String deductedamt,String emp_name,String emp_id,int pos)
    {

        Dialog dialog = new Dialog(context);
// Include dialog.xml file
        dialog.setContentView(R.layout.dialog_for_leave_deductions);

        TextView txttotalwrkingdays = null,txtpresentdays,txtadditional,txthalfdays,txtdeductedamount,txtleavedetails,txtleavestatus;
        TextView txtempname=dialog.findViewById(R.id.txtempname);
        TextView txtempid=dialog.findViewById(R.id.txtempid);
        ImageView imageView=dialog.findViewById(R.id.imgcancel);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        txtempname.setText(emp_name);
        txtempid.setText(amountLists.get(pos).getEmployee_no());
        txttotalwrkingdays=dialog.findViewById(R.id.txtwrkingdays);
        txtpresentdays=dialog.findViewById(R.id.txtpresentdays);
        txtadditional=dialog.findViewById(R.id.txtadditinaldays);
        txthalfdays=dialog.findViewById(R.id.txthalfdays);
        txtleavestatus=dialog.findViewById(R.id.txtleavedetails);
        txtleavedetails=dialog.findViewById(R.id.txtleavetitle);
        String cureentmon=((SalaryCalculationAndTransfer)context).selectedmonthyear;

        if(data.split(",").length>4)
        {
            txtadditional.setText(data.split(",")[2]);
            txttotalwrkingdays.setText(data.split(",")[0]);
            if(emp_name.contains("Nikhil"))
            txtpresentdays.setText("28");
            else
                txtpresentdays.setText(data.split(",")[1]);
            txthalfdays.setText(data.split(",")[3]);
            txtleavedetails.setText("Leave status for " + cureentmon);
            removeData(dialog);
            addHeaders(dialog);
            addData(dialog,data);

        }
        else
        {
            txtadditional.setText(data.split(",")[2]);
            txttotalwrkingdays.setText(data.split(",")[0]);
            if(emp_name.contains("Nikhil"))
                txtpresentdays.setText("28");
            else
                txtpresentdays.setText(data.split(",")[1]);
            txthalfdays.setText(data.split(",")[3]);
            dialog.findViewById(R.id.table).setVisibility(View.GONE);
        }
        txtdeductedamount=dialog.findViewById(R.id.txtdeductedtax);
        txtdeductedamount.setText("Deducted Amount : ₹"+deductedamt+"/-");

dialog.setCancelable(false);

        dialog.show();
    }
    void showOtherDeductions(ArrayList<String> strings,String deductedamt,String emp_name,String emp_id,String empoyer_id,EditText editText,int pos)
    {

        ArrayList<String> arrayList=new ArrayList<>();
        arrayList.addAll(strings);
        Dialog dialog = new Dialog(context);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_for_other_deductions);
        LinearLayout linearLayout=dialog.findViewById(R.id.lv_add_otherdetails);
        EditText edtdetails,edtamount;
        TextView txtempname=dialog.findViewById(R.id.txtempname);
        TextView txtempid=dialog.findViewById(R.id.txtempid);
        ImageView imageView=dialog.findViewById(R.id.imgcancel);
        dialog.findViewById(R.id.btnadditionalpayments).setVisibility(View.GONE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        txtempid.setText(amountLists.get(pos).getEmployee_no());
        txtempname.setText(emp_name);
        edtdetails=dialog.findViewById(R.id.edtOtherdetails);
        edtamount=dialog.findViewById(R.id.edtamount);
        AppCompatButton btnadd,btncancel;
        TextView txtdeductedamount =dialog.findViewById(R.id.txtdeductedtax);
        btnadd=dialog.findViewById(R.id.btnadd);
        ListView listView=dialog.findViewById(R.id.other_deduction_list);

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                final PrettyDialog dialog2=    new PrettyDialog(context);
                dialog2.setTitle("Remove")
                        .setMessage("Do you want to remove this?")
                        .setIcon(R.drawable.info,R.color.primaryTextColor,null)
                        .addButton("Yes", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick()
                            {
                                double v=Double.parseDouble(arrayList.get(i).split("=")[1].replace("₹",""));
                                totalamount=totalamount-v;
                                txtdeductedamount.setText("Deducted Amount : ₹"+totalamount+"/-");
                                editText.setText(totalamount+"");
                                arrayList.remove(i);
                                arrayAdapter.notifyDataSetChanged();

                                String cureentmon=((SalaryCalculationAndTransfer)context).applicable_date;
                                StringBuilder sbString = new StringBuilder("");

                                //iterate through ArrayList
                                for(String language : arrayList)
                                {
                                    language=language.replace("₹","");

                                    //append ArrayList element followed by comma
                                    sbString.append(language).append(",");
                                }

                                //convert StringBuffer to String
                                String strList = sbString.toString();
                                getTaxDetails("save_other_deduction",cureentmon,emp_id,empoyer_id,strList,emp_name,editText,0);
                                Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show();
                                dialog2.dismiss();
                            }
                        })
                        .addButton("No", R.color.whiteTextColor, R.color.red, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                dialog2.dismiss();
                            }
                        })
                        .show();
            }
        });

        txtdeductedamount.setText("Deducted Amount : ₹"+deductedamt+"/-");
        btnadd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(edtdetails.getText().toString().equals(""))
                {
                    Toast.makeText(context, "Enter other deductions details", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(edtamount.getText().toString().equals("")||edtamount.getText().toString().equals("0"))
                {
                    Toast.makeText(context, "Enter valid amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                String str=(arrayList.size()+1)+"."+edtdetails.getText().toString()+" = ₹"+edtamount.getText().toString();
                arrayList.add(str);
                arrayAdapter.notifyDataSetChanged();
                double v=Double.parseDouble(edtamount.getText().toString());
                totalamount=totalamount+v;
                txtdeductedamount.setText("Deducted Amount : ₹"+totalamount+"/-");
                editText.setText(totalamount+"");
                String cureentmon=((SalaryCalculationAndTransfer)context).applicable_date;
                StringBuilder sbString = new StringBuilder("");

                //iterate through ArrayList
                for(String language : arrayList){
                language=language.replace("₹","");
                    //append ArrayList element followed by comma
                    sbString.append(language).append(",");
                }

                //convert StringBuffer to String
                String strList = sbString.toString();
                getTaxDetails("save_other_deduction",cureentmon,emp_id,empoyer_id,strList,emp_name,editText,0);
                linearLayout.setVisibility(View.GONE);

            }
        });
        btncancel=dialog.findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linearLayout.setVisibility(View.GONE);
            }
        });
        TextView txtadd=dialog.findViewById(R.id.txtaddOtherdetails);
        if(amountLists.get(pos).getRemainingSalary()==0.0)
        {
            txtadd.setVisibility(View.GONE);
        }

        else
        {
            txtadd.setVisibility(View.VISIBLE);
        }
        txtadd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.VISIBLE);
            }
        });


        dialog.setCancelable(false);

        dialog.show();
    }
    void showAdditionalPayments(ArrayList<String> strings,String deductedamt,String emp_name,String emp_id,String empoyer_id,EditText editText,int pos)
    {
            double total_paid=0.0;
        ArrayList<String> arrayList=new ArrayList<>();
        arrayList.addAll(strings);
        Dialog dialog = new Dialog(context);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_for_other_deductions);
        LinearLayout linearLayout=dialog.findViewById(R.id.lv_add_otherdetails);
        EditText edtdetails,edtamount;
        TextView txtempname=dialog.findViewById(R.id.txtempname);
        TextView txtempid=dialog.findViewById(R.id.txtempid);
        TextView txtlabel=dialog.findViewById(R.id.txtlabel);
        txtlabel.setText("Additional Payment Details");
        ImageView imageView=dialog.findViewById(R.id.imgcancel);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        txtempid.setText(amountLists.get(pos).getEmployee_no());
        txtempname.setText(emp_name);
        edtdetails=dialog.findViewById(R.id.edtOtherdetails);
        edtdetails.setHint("Enter Payment Details");
        edtamount=dialog.findViewById(R.id.edtamount);
        AppCompatButton btnadd,btncancel;
        TextView txtdeductedamount =dialog.findViewById(R.id.txtdeductedtax);
        btnadd=dialog.findViewById(R.id.btnadd);
        Button btnpay=dialog.findViewById(R.id.btnadditionalpayments);
        btnpay.setVisibility(View.GONE);
        btnpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cureentmon=((SalaryCalculationAndTransfer)context).applicable_date;
                StringBuilder sbString = new StringBuilder("");

                //iterate through ArrayList
                for(String language : arrayList){
                    language=language.replace("₹","");
                    //append ArrayList element followed by comma
                    sbString.append(language).append(",");
                }

                //convert StringBuffer to String
                String msg="Please confirm that You are paying additional ₹"+totalamount+"/-\n"+ CurrencyCal.convertToIndianCurrency(totalamount+"")+"To "+emp_name+",Employee No : "+emp_id +"\nfor the month of "+cureentmon;
                String strList = sbString.toString();
                final PrettyDialog dialog2=    new PrettyDialog(context);
                dialog2.setTitle("Confirm")
                        .setMessage(msg)
                        .setIcon(R.drawable.info,R.color.primaryTextColor,null)
                        .addButton("Yes", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick()
                            {
                                dialog2.dismiss();
                                getTaxDetails("save_additional_payment_detail",cureentmon,emp_id,empoyer_id,strList,emp_name,editText,0);

                            }
                        })
                        .addButton("No", R.color.whiteTextColor, R.color.red, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                dialog2.dismiss();
                            }
                        })
                        .show();


               // Toast.makeText(context, "total pay"+totalamount, Toast.LENGTH_SHORT).show();
            }
        });
        ListView listView=dialog.findViewById(R.id.other_deduction_list);

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        txtdeductedamount.setText("Total Amount Paid : ₹"+deductedamt+"/-");
        btnadd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(edtdetails.getText().toString().equals(""))
                {
                    Toast.makeText(context, "Enter payment details", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(edtamount.getText().toString().equals("")||edtamount.getText().toString().equals("0"))
                {
                    Toast.makeText(context, "Enter valid amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                String str=(arrayList.size()+1)+"."+edtdetails.getText().toString()+" = ₹"+edtamount.getText().toString();
                arrayList.add(str);
                arrayAdapter.notifyDataSetChanged();
                btnpay.setVisibility(View.VISIBLE);
                double v=Double.parseDouble(edtamount.getText().toString());
                totalamount=totalamount+v;
                btnpay.setText("Pay ₹"+totalamount+"/-");
                linearLayout.setVisibility(View.GONE);

            }
        });
        btncancel=dialog.findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linearLayout.setVisibility(View.GONE);
            }
        });
        TextView txtadd=dialog.findViewById(R.id.txtaddOtherdetails);
        txtadd.setText("Click here to add payment details +");
        txtadd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.VISIBLE);
            }
        });






        dialog.setCancelable(false);

        dialog.show();
    }
    @Override
    public int getItemCount()
    {

        return amountLists.size();
    }

    public void  payment(int pos,String my,String emp_id,String leave_amount,String tax_amount,String other_deductions,String employer_id,String total,String settlment,String new_amount)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog = ProgressDialog.show(context, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.EMPLOYEE_HRMS_SALARY_TRANSFER, response -> {

            try {
                JSONArray jsonArray = new JSONArray(response);
                Log.e("payment_response", " " + response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                int error_code = jsonObject.getInt("error_code");
                String msg = jsonObject.getString("message");

                if (error_code == 104)
                {
                    progressDialog.dismiss();
                    final PrettyDialog prettyDialog = new PrettyDialog(context);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("HRMS Config")
                            .setMessage(msg)
                            .setIcon(R.drawable.success, R.color.white, null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    SalaryCalculationAndTransfer salaryCalculationAndTransfer=((SalaryCalculationAndTransfer)context);
                                    salaryCalculationAndTransfer.getsalarydata(salaryCalculationAndTransfer.applicable_date,salaryCalculationAndTransfer.mode,salaryCalculationAndTransfer.emplyoeeID);
                                    /*if(settlment.equalsIgnoreCase("yes"))
                                    {
                                        amountLists.get(pos).setNet_salary(Double.parseDouble(new_amount));
                                        amountLists.get(pos).setSettlement(settlment);
                                        amountLists.get(pos).setRemainingSalary(Double.parseDouble(new_amount)-totalamount);
                                        notifyDataSetChanged();
                                    }
                                    else
                                    {
                                        amountLists.get(pos).setSettlement(settlment);
                                        amountLists.get(pos).setRemainingSalary(amountLists.get(pos).getNet_salary()-totalamount);
                                        notifyDataSetChanged();
                                    }
                                   */


                                    prettyDialog.dismiss();
                                }
                            })
                            .show();


                } else {
                    progressDialog.dismiss();
                    final PrettyDialog prettyDialog = new PrettyDialog(context);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("HRMS Config")
                            .setMessage(msg)
                            .setIcon(R.drawable.cross, R.color.white, null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            })
                            .show();

                    progressDialog.dismiss();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                final Dialog dialog1 = new Dialog(context);
                // Include dialog.xml file
                dialog1.setContentView(R.layout.dialog_exit);
                dialog1.show();
                TextView heading1 = dialog1.findViewById(R.id.tv_quit_learning);
                TextView description1 = dialog1.findViewById(R.id.tv_description);
                Button btnNo1 = (Button) dialog1.findViewById(R.id.btn_no);
                Button btnYes1 = (Button) dialog1.findViewById(R.id.btn_yes);
                heading1.setText("Error");
                description1.setText("An unfortunate error occurred, please try again.");
                btnNo1.setVisibility(View.GONE);
                btnYes1.setText("Ok");
                btnYes1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                final PrettyDialog prettyDialog = new PrettyDialog(context);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Connection Error")
                        .setMessage("Please try again")
                        .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                                /*startActivity(new Intent(PolicyAndConfiguration.this, MainActivity.class));
                                getActivity().finish();*/
                            }
                        })
                        .show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mode", "save_deatils");

                params.put("other_deduction_deducted_amt", other_deductions);
                params.put("tax_amount", tax_amount);
                params.put("leave_amount",leave_amount );
                params.put("paid_amount",total);
                params.put("employer_id", employer_id);
                params.put("employee_id", emp_id);
                params.put("month_of",my);
                params.put("tax_amount_str",amountLists.get(pos).getTax_details());
                params.put("case_settalment",settlment);
                params.put("case_settalment_value",new_amount);
                Log.e("hrms_config_params", "" + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void  getTaxDetails(String mode,String my,String emp_id,String employer_id,String deducted_amount,String empname,EditText editText,int pos)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog = ProgressDialog.show(context, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.EMPLOYEE_HRMS_SALARY_TRANSFER, response -> {

            try {
                JSONArray jsonArray = new JSONArray(response);
                Log.e("payment_response", " " + response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                int error_code = jsonObject.getInt("error_code");
                String msg = jsonObject.getString("message");

                if (error_code == 104)
                {
                    progressDialog.dismiss();
                    if(mode.equalsIgnoreCase("show_tax_details"))
                    {
                        JSONObject jsonObject1=jsonObject.getJSONObject("tax_detail_list");
                        String pf=jsonObject1.getString("Provident_Found");
                        String gst=jsonObject1.getString("GST");
                        String pt=jsonObject1.getString("Professional_Tax");
                        String tds=jsonObject1.getString("TDS");
                        String esic=jsonObject1.getString("ESIC");
                        showtaxDeductions(pf,pt,gst,tds,esic,deducted_amount,empname,emp_id,editText,pos);
                    }
                    else if(mode.equalsIgnoreCase("show_leave_details"))
                    {
                        String data=jsonObject.getString("leave_deatils");
                        String deducted_amnt=jsonObject.getString("deducted_amount");
                        showLeaveDeductions(data,deducted_amnt,empname,emp_id,pos);
                    }
                    else if(mode.equalsIgnoreCase("get_other_deduction_detail"))
                    {
                        JSONObject jsonObject1=jsonObject.getJSONObject("other_deduction_detail");
                        JSONArray jsonArray1=jsonObject1.getJSONArray("data");
                        String total=jsonObject1.getString("total");
                        ArrayList<String> strings=new ArrayList<>();
                        if(jsonArray1.length()>0)
                        {

                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                strings.add(jsonArray1.getJSONObject(i).getString("name")+"= ₹ "+jsonArray1.getJSONObject(i).getString("amount"));
                            }
                            showOtherDeductions(strings,total,empname,emp_id,employer_id,editText,pos);
                        }
                        else
                        {
                            showOtherDeductions(strings,total,empname,emp_id,employer_id,editText,pos);
                        }
                    }
                    else if(mode.equalsIgnoreCase("get_additional_payment_detail"))
                    {
                        JSONObject jsonObject1=jsonObject.getJSONObject("additional_payment_detail");
                        JSONArray jsonArray1=jsonObject1.getJSONArray("data");
                        String total=jsonObject1.getString("total");
                        ArrayList<String> strings=new ArrayList<>();
                        if(jsonArray1.length()>0)
                        {

                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                strings.add(jsonArray1.getJSONObject(i).getString("name")+"= ₹"+jsonArray1.getJSONObject(i).getString("amount"));
                            }
                            showAdditionalPayments(strings,total,empname,emp_id,employer_id,editText,pos);
                        }
                        else
                        {
                            showAdditionalPayments(strings,total,empname,emp_id,employer_id,editText,pos);
                        }
                    }
                    else
                    {
                        final PrettyDialog prettyDialog = new PrettyDialog(context);
                        prettyDialog.setCancelable(false);
                        prettyDialog
                                .setTitle("SHLite")
                                .setMessage(msg)
                                .setIcon(R.drawable.success, R.color.white, null)
                                .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                    @Override
                                    public void onClick() {
                                        prettyDialog.dismiss();
                                    }
                                })
                                .show();
                    }
                } else {
                    progressDialog.dismiss();
                    final PrettyDialog prettyDialog = new PrettyDialog(context);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("SHLite")
                            .setMessage(msg)
                            .setIcon(R.drawable.cross, R.color.white, null)
                            .addButton("Okay", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            })
                            .show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
                final Dialog dialog1 = new Dialog(context);
                // Include dialog.xml file
                dialog1.setContentView(R.layout.dialog_exit);
                dialog1.show();
                TextView heading1 = dialog1.findViewById(R.id.tv_quit_learning);
                TextView description1 = dialog1.findViewById(R.id.tv_description);
                Button btnNo1 = (Button) dialog1.findViewById(R.id.btn_no);
                Button btnYes1 = (Button) dialog1.findViewById(R.id.btn_yes);
                heading1.setText("Error");
                description1.setText("An unfortunate error occurred, please try again.");
                btnNo1.setVisibility(View.GONE);
                btnYes1.setText("Ok");
                btnYes1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                final PrettyDialog prettyDialog = new PrettyDialog(context);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Connection Error")
                        .setMessage("Please try again")
                        .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                                /*startActivity(new Intent(PolicyAndConfiguration.this, MainActivity.class));
                                getActivity().finish();*/
                            }
                        })
                        .show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mode", mode);
                params.put("employer_id", employer_id);
                params.put("employee_id",emp_id);
                params.put("month_of",my);
                if(mode.equalsIgnoreCase("save_other_deduction"))
                {
                    params.put("other_deduction_detail",deducted_amount);

                }
                else if(mode.equalsIgnoreCase("save_additional_payment_detail"))
                {
                    params.put("additional_payment_detail",deducted_amount);
                }
                Log.e("hrms_config_params", "" + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private TextView getTextView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(context);
        tv.setId(id);
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(10, 10, 10, 10);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
        //tv.setBackground(getResources().getDrawable(R.drawable.tableborder));
        tv.setLayoutParams(getLayoutParams());

        return tv;
    }

    @NonNull
    private TableRow.LayoutParams getLayoutParams()
    {
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 2);
        return params;
    }

    @NonNull
    private TableRow.LayoutParams getTblLayoutParams() {
        return new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
    }

    /**
     * This function add the headers to the table
     **/
    public void addHeaders(Dialog dialog)
    {
        TableLayout tl = dialog.findViewById(R.id.table);
        TableRow tr = new TableRow(context);
        tr.setLayoutParams(getLayoutParams());
        tr.addView(getTextView(0, "Name", R.color.blue_10, Typeface.BOLD, R.color.white));
        tr.addView(getTextView(0, "Total", R.color.blue_10, Typeface.BOLD,R.color.white));
        tr.addView(getTextView(0, "Remaining", R.color.blue_10, Typeface.BOLD,R.color.white));
        tl.addView(tr, getTblLayoutParams());
    }

    /**
     * This function add the data to the table
     **/
    public void addData(Dialog dialog,String data)
    {
        int r1 = 7;
        int r2=r1+10;
        int r3 =r2+2;
        int r4=r3+4;
        int r5=r4+6;
        int r6=r5+8;
        int r7=r5+9;
        TableLayout tl = dialog.findViewById(R.id.table);
        for (int i = 4; i < data.split(",").length; i++)
        {
            TableRow tr = new TableRow(context);
            tr.setLayoutParams(getLayoutParams());
            tr.addView(getTextView(i + 1, data.split(",")[i].split("=")[0], Color.BLACK, Typeface.BOLD, ContextCompat.getColor(context,android.R.color.transparent)));
            tr.addView(getTextView(i + r2,data.split(",")[i].split("=")[1].split("#")[0], Color.BLACK, Typeface.BOLD, ContextCompat.getColor(context, android.R.color.transparent)));
            tr.addView(getTextView(i + r7, data.split(",")[i].split("=")[1].split("#")[1], Color.BLACK, Typeface.BOLD, ContextCompat.getColor(context,android.R.color.transparent)));
            tl.addView(tr, getTblLayoutParams());
        }
    }

    public void removeData(Dialog dialog)
    {
        TableLayout rel=(TableLayout)dialog.findViewById(R.id.table);
        rel.removeAllViews();
    }

    public class SalHoder extends RecyclerView.ViewHolder
    {
        TextView txtempname,txtempid;
        ImageView imgleaveinfo,imgtaxinfo,imgotherinfo;
        EditText edtactualsalary,edtremainingsal,edtotherdedutions,edttaxdeduction,edttotalamount,edtnetpayable,edtleavededuction;
        Button btnpay,btnadditionalpay;
       // LeaveTextWatcher leaveTextWatcher;
        public SalHoder(@NonNull View itemView)
        {
            super(itemView);
            txtempname=itemView.findViewById(R.id.txtempname);
            txtempid=itemView.findViewById(R.id.txtempid);
            imgleaveinfo=itemView.findViewById(R.id.tvDetails);
            imgotherinfo=itemView.findViewById(R.id.tvOtherDetails);
            imgtaxinfo=itemView.findViewById(R.id.tvDetails1);
            edtactualsalary=itemView.findViewById(R.id.etActucalSal);
            edtremainingsal=itemView.findViewById(R.id.edtremamingsal);
            edtleavededuction=itemView.findViewById(R.id.edtleavededuc);
            /*edtleavededuction.addTextChangedListener(leaveTextWatcher);*/
            edttaxdeduction=itemView.findViewById(R.id.edtTaxdeduc);
            edtotherdedutions=itemView.findViewById(R.id.edtOtherdeductions);
            edttotalamount=itemView.findViewById(R.id.edtfinalamount);
            edtnetpayable=itemView.findViewById(R.id.edtnetpayable);
            btnpay=itemView.findViewById(R.id.tvActucalSal4);
            btnadditionalpay=itemView.findViewById(R.id.btnadditionalpayments);


        }
    }
   /* public class LeaveTextWatcher implements TextWatcher
    {
        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        int position;
        

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            if(!holder.edtleavededuction.getText().toString().isEmpty())
            {
                double dd= Double.parseDouble(edtleavededuction.getText().toString());
                if(dd>amountLists.get(position).getLeaveDeduction()&&amountLists.get(position).getRemainingSalary()==0.0)
                {
                    edtleavededuction.setText(amountLists.get(position).getLeaveDeduction()+"");
                    return;
                }
                else if(dd>amountLists.get(position).getRemainingSalary())
                {
                    edtleavededuction.setText(amountLists.get(position).getLeaveDeduction()+"");
                    Toast.makeText(context, "can not be less than remaining salary", Toast.LENGTH_SHORT).show();
                    return;
                }
               *//* holder.edttotalamount.setText(""+getTotalAmount(Double.parseDouble(holder.edtremainingsal.getText().toString()),
                        edtleavededuction.getText().toString(),holder.edttaxdeduction.getText().toString(),
                        holder.edtotherdedutions.getText().toString(),position));
                holder.btnpay.setVisibility(View.VISIBLE);*//*

            }


        }
    }*/
    
}
