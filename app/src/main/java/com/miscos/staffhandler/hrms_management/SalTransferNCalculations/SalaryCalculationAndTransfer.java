package com.miscos.staffhandler.hrms_management.SalTransferNCalculations;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miscos.staffhandler.Adapters.MultiAdapter;
import com.miscos.staffhandler.LocalDatabase.SqLiteHelper;
import com.miscos.staffhandler.Model.Employee;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.Urls.App_Urls;
import com.miscos.staffhandler.employee.helper.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;

public class SalaryCalculationAndTransfer extends AppCompatActivity {



    private Spinner spFlatType;
    private SqLiteHelper db;

    String emplyoeeID="";
    ProgressDialog progressDialog;
    Button btnGetSelected,btnOkay,btnCancel;
    PreferenceManager preferenceManager;
    private ArrayList<String> employeeNameList;
    private ArrayList<String> selected_employeeIDList;
    private ArrayList<String> selected_employeeNameList;
    private ArrayList<String> employeeIDList;
    private RecyclerView recyclerView;
    private ArrayList<Employee> employees = new ArrayList<>();
    private MultiAdapter adapter;
    TextView noData,txttotalAmount,txtdueamount,txtheading,employeeBtn;
    LinearLayout allAmount,lv_total;
    RelativeLayout empDetails;
    RecyclerView recyclerView_emp;
    private ArrayList<Employee> filteredEmployees = new ArrayList<>();
    ArrayList<AmountList> allAmountLists,selectedAmountlist;
    salarycalc_adapter_for_emp salarycalc_adapter_for_emp;
    CheckBox chkAll,chkwithtax;
 public String applicable_date="",selectedmonthyear="",mode="";
 ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_salary_calculation_and_transfer);
    //    View view =  inflater.inflate(R.layout.fragment_salary_calculation_and_transfer, container, false);
        preferenceManager = new PreferenceManager(this);
        //Progress Bar
        progressDialog = new ProgressDialog(SalaryCalculationAndTransfer.this);
        progressDialog = ProgressDialog.show(SalaryCalculationAndTransfer.this, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.dismiss();
        chkAll=findViewById(R.id.checkAllEmployee);
        chkwithtax=findViewById(R.id.chkwithtax);
        imgBack=findViewById(R.id.imBack);
        imgBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        chkwithtax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b)
                {
                    Double integer=0.0,remaining_amount=0.0;
                    for(AmountList amountList:selectedAmountlist)
                    {
                        integer=integer+amountList.getActualSalary();

                        remaining_amount=remaining_amount+amountList.getRemainingSalary();
                        remaining_amount=remaining_amount-amountList.getTaxs();
                    }
                    txttotalAmount.setText("Total Amount ₹\n"+Math.round(integer));
                    txtdueamount.setText("Due Amount ₹\n"+Math.round(remaining_amount));
                }
                else
                {
                    Double integer=0.0,remaining_amount=0.0;
                    for(AmountList amountList:selectedAmountlist)
                    {
                        integer=integer+amountList.getActualSalary();
                        remaining_amount=remaining_amount+amountList.getRemainingSalary();

                    }
                    txttotalAmount.setText("Total Amount ₹\n"+Math.round(integer));
                    txtdueamount.setText("Due Amount ₹\n"+Math.round(remaining_amount));
                }
            }
        });
        chkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if(b&!applicable_date.equalsIgnoreCase(""))
                {
                    mode="multiple_fetch";
                    getsalarydata(applicable_date,"multiple_fetch","");
                }
            }
        });
        lv_total=findViewById(R.id.linearLayout3);
        lv_total.setVisibility(GONE);
        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -6);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
       // endDate.add(Calendar.MONTH, 1);

HorizontalCalendar  horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate,endDate)
                .datesNumberOnScreen(4)
                .mode(HorizontalCalendar.Mode.MONTHS)
                .configure()
                .formatMiddleText("MMM")
                .formatBottomText("yyyy")
                .showTopText(false)
                .showBottomText(true)
                .textColor(Color.BLACK, Color.BLUE)
                .end().defaultSelectedDate(endDate).build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {

            @Override
            public void onDateSelected(Calendar date, int position)
            {

            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                //do something

                applicable_date="01-"+(date.get(Calendar.MONTH)+1)+"-"+date.get(Calendar.YEAR);
                selectedmonthyear=getMonthForInt(date.get(Calendar.MONTH))+" "+date.get(Calendar.YEAR);

                if(emplyoeeID.isEmpty()&&!chkAll.isChecked())
                {
                    final PrettyDialog prettyDialog = new PrettyDialog(SalaryCalculationAndTransfer.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Error")
                            .setMessage("Select Employee")
                            .setIcon(R.drawable.info, R.color.primaryTextColor, null)
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
                else if(chkAll.isChecked())
                {
                    txtheading.setText("Showing Data for "+selectedmonthyear);
                    mode="multiple_fetch";
                    getsalarydata(applicable_date,"multiple_fetch","");
                }
                else
                { txtheading.setText("Showing Data for "+selectedmonthyear);
                mode="single_fetch";
                    getsalarydata(applicable_date,"single_fetch",emplyoeeID);

                }

                return true;
            }
        });
        db =  new SqLiteHelper(SalaryCalculationAndTransfer.this);

        empDetails = findViewById(R.id.rlDetails);
        allAmount = findViewById(R.id.linearLayout3);
        txttotalAmount=findViewById(R.id.txttotalAmount);
        txtdueamount=findViewById(R.id.txtdueamount);
        employeeBtn =findViewById(R.id.spinnerEmployee);
        recyclerView_emp=findViewById(R.id.recyclerview_emp);
        selectedAmountlist=new ArrayList<>();
        allAmountLists=new ArrayList<>();
        salarycalc_adapter_for_emp=new salarycalc_adapter_for_emp(selectedAmountlist,this);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView_emp.setLayoutManager(layoutManager);
        recyclerView_emp.setAdapter(salarycalc_adapter_for_emp);
        recyclerView_emp.setItemViewCacheSize(selectedAmountlist.size());
        txtheading=findViewById(R.id.txtheading);
        txtheading.setText(selectedmonthyear);
        employeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiSelectData();
            }
        });
    }
    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }
    //setting spinner
    private  void multiSelectData()
    {
        View itemLayoutView = LayoutInflater.from(SalaryCalculationAndTransfer.this).inflate(
                R.layout.multiselection_activity, null);
        new BottomSheetDialog(this);
        BottomSheetDialog dialog;
        dialog = new BottomSheetDialog(this,R.style.TransparentDialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(itemLayoutView);
        dialog.setCancelable(true);
        BottomSheetDialog finalDialog = dialog;
        btnGetSelected = dialog.findViewById(R.id.btnGetSelected);
        btnOkay = dialog.findViewById(R.id.btnSelectedokay);
        btnCancel = dialog.findViewById(R.id.btnCancel);
        noData = dialog.findViewById(R.id.tvNodata);
        recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        employees.clear();
        adapter = new MultiAdapter(this, employees);
        recyclerView.setAdapter(adapter);
        employeeNameList = new ArrayList<>();
        employeeIDList = new ArrayList<>();
        Cursor cursor = db.getEmployeelist();
        if (cursor != null) {
            Log.e("count", cursor.getCount() + "");
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast())
                {
                    employeeNameList.add(cursor.getString(cursor.getColumnIndex(SqLiteHelper.COLUMN_EMPLOYEE_NAME)));
                    employeeIDList.add(cursor.getString(cursor.getColumnIndex(SqLiteHelper.COLUMN_EMPLOYEE_ID)));
                    cursor.moveToNext();
                }
            }
        }
      adapter.setEmployees(employees);

        createList();
        for (int i=0;i<employees.size();i++)
        {
            employees.get(i).setChecked(true);
        }
        btnGetSelected.setVisibility(GONE);
        if(employees.size()==0)
        {
            noData.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
        }else
            {
            noData.setVisibility(GONE);
            btnCancel.setVisibility(GONE);
        }
        EditText edtsearchview=dialog.findViewById(R.id.search_editText);
        edtsearchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                filterList(edtsearchview.getText().toString());

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDialog.dismiss();
            }
        });
        btnGetSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                selectAll();
            }
        });
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (adapter.getSelected().size() > 0)
                { String stringBuilder="";
                    String stringBuilderName="";
                    for (int i = 0; i < adapter.getSelected().size(); i++) {
                        if (i==0) {
                            stringBuilder = adapter.getSelected().get(i).getId();
                            stringBuilderName = adapter.getSelected().get(i).getName();
                        }else{
                            stringBuilder = stringBuilder+","+ adapter.getSelected().get(i).getId();
                            stringBuilderName = stringBuilderName+","+ adapter.getSelected().get(i).getName();
                        }
                    }

                    employeeBtn.setText(stringBuilderName);
                    emplyoeeID = stringBuilder;
                    //Toast.makeText(SalaryCalculationAndTransfer.this, emplyoeeID, Toast.LENGTH_SHORT).show();
                    finalDialog.dismiss();
                  if(!applicable_date.equalsIgnoreCase(""))
                  {
                      mode="single_fetch";
                      getsalarydata(applicable_date,"single_fetch",emplyoeeID);
                  }


                } else {
                    Toast.makeText(SalaryCalculationAndTransfer.this, "Select Employee", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
    public void filterList(String s)
    {
        filteredEmployees.clear();
        if(s.equalsIgnoreCase(""))
        {
            filteredEmployees.addAll(employees);
            adapter.setEmployees(filteredEmployees);
            return;
        }

        for (int i = 0; i < employees.size(); i++)
        {
            if(employees.get(i).getName().toLowerCase().contains(s.toLowerCase()))
                filteredEmployees.add(employees.get(i));
        }
        adapter.setEmployees(filteredEmployees);
    }
    private void createList()
    {
        employees = new ArrayList<>();
        for (int i = 0; i < employeeNameList.size(); i++)
        {
            Employee employee = new Employee();
            employee.setName(employeeNameList.get(i));
            employee.setId(employeeIDList.get(i));
            employee.setChecked(false);
            employees.add(employee);
        }
        adapter.setEmployees(employees);
    }


    private void selectAll()
    {
        for (int i=0;i<employees.size();i++)
        {
            employees.get(i).setChecked(false);
        }
        adapter = new MultiAdapter(this, employees);
        recyclerView.setAdapter(adapter);
       // getsalarydata(applicable_date,"multiple_fetch","");
    }

    public void  getsalarydata(final String applicabledate,String mode,String employee_id)
    {
        progressDialog.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.EMPLOYEE_HRMS_SALARY_TRANSFER, response -> {
            progressDialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(response);
                Log.e("hrms_config_response", " " + response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                int error_code = jsonObject.getInt("error_code");
                String msg = jsonObject.getString("message");

                if (error_code == 104)
                {
                    allAmount.setVisibility(View.VISIBLE);
                    Gson gson=new Gson();
                    selectedAmountlist.clear();
                    salarycalc_adapter_for_emp.notifyDataSetChanged();
                    List<EmpSalDatum> empSalDatum=gson.fromJson(response,new TypeToken<List<EmpSalDatum>>(){}.getType());
                    if(empSalDatum.get(0).getAmountList().size()>0)
                    { lv_total.setVisibility(View.VISIBLE);
                        for(AmountList amountList:empSalDatum.get(0).getAmountList())
                        {
                            if(!amountList.getEmployeeId().equals(amountList.getEmployerId()))
                            selectedAmountlist.add(amountList);
                        }

                        salarycalc_adapter_for_emp.notifyDataSetChanged();
                    }
                    Double integer=0.0,remaining_amount=0.0;
                    for(AmountList amountList:selectedAmountlist)
                    {
                        integer=integer+amountList.getActualSalary();

                        remaining_amount=remaining_amount+amountList.getRemainingSalary();

                    }

                    txttotalAmount.setText("Total Amount ₹\n"+Math.round(integer));
                    txtdueamount.setText("Due Amount ₹\n"+Math.round(remaining_amount));

                         } else
                    {
                    selectedAmountlist.clear();
                    salarycalc_adapter_for_emp.notifyDataSetChanged();
                    lv_total.setVisibility(GONE);
                    final PrettyDialog prettyDialog = new PrettyDialog(SalaryCalculationAndTransfer.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle("Employee Salary")
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
                final Dialog dialog1 = new Dialog(SalaryCalculationAndTransfer.this);
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
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                final PrettyDialog prettyDialog = new PrettyDialog(SalaryCalculationAndTransfer.this);
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
                params.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
                params.put("employee_id", employee_id);
               /* params.put("employer_id","Shin3");
                params.put("employee_id", "Shin3");*/
                params.put("month_year", applicabledate);
                Log.e("hrms_config_params", "" + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(SalaryCalculationAndTransfer.this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(SalaryCalculationAndTransfer.this, EmployerZone.class));
        finish();
    }

}