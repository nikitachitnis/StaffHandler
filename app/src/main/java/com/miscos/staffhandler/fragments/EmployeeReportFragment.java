package com.miscos.staffhandler.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.miscos.staffhandler.Adapters.MultiAdapter;
import com.miscos.staffhandler.LocalDatabase.SqLiteHelper;
import com.miscos.staffhandler.Model.Employee;
import com.miscos.staffhandler.Model.EmployeeListModel;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.activities.EmployeeReport;
import com.miscos.staffhandler.activities.EmployerZone;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.GONE;

public class EmployeeReportFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "employeeReports";
    boolean[] checkedItems1;
    private String[] items;
    private Toolbar toolbar;
    private PreferenceManager preferenceManager;
    private ImageView tvBack;
    private Spinner spinnerEmployee;
    private ImageView imgToDate, imgfromDate;
    private EditText txtToDate, txtFromDate, txtHeading, txtSearch;
    private String strFrom, strTo, emplyoeeID, employee_name, select_all, employer_id, strtext, employeeMode,reportFor;
    private String dateCheck;
    private Button btnGo, employeeBtn;
    private ArrayList<String> employeeNameList;
    private ArrayList<String> selected_employeeIDList;
    private ArrayList<String> selected_employeeNameList;
    private ArrayList<String> employeeIDList;
    private RecyclerView recyclerView;
    private ArrayList<Employee> employees = new ArrayList<>();
    private ArrayList<Employee> employeesID = new ArrayList<>();
    private ArrayList<EmployeeListModel> employeeListModels = new ArrayList<>();
    private MultiAdapter adapter;
    private Button btnGetSelected,btnOkay,btnCancel;
    private TextView noData;
    private SqLiteHelper db;
    private String dd = "00", mm = "00", yy = "0000";
    DatePickerDialog.OnDateSetListener myDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            int m = month + 1;
            dd = String.valueOf(dayOfMonth);
            mm = String.valueOf(m);
            yy = String.valueOf(year);
            onDateSetChange(view, year, month, dayOfMonth);
        }
    };
    private ProgressBar progressDialog;
    private RadioButton selectAll,chkShiftStaff,chkOfficeStaff;
    private ArrayList<String> buildingNameList;
    private ArrayList<String> buildingIDList;
    // No Internet Dialog
    //MultipleSelection spinner object
    private Spinner sp1;
    private Dialog dialog;
    public EmployeeReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employee_reports, container, false);

        db = new SqLiteHelper(getActivity());
        preferenceManager = new PreferenceManager(getActivity());
        employer_id = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID);
        toolbar = view.findViewById(R.id.toolbar);
        reportFor = "both";
        employeeBtn = view.findViewById(R.id.spinnerEmployee);
        sp1 = view.findViewById(R.id.spinnerEmployee1);
        selectAll = view.findViewById(R.id.checkAll);
        txtFromDate = view.findViewById(R.id.txtFromDate);
        txtToDate = view.findViewById(R.id.txtToDate);
        imgfromDate = view.findViewById(R.id.imgFromDate);
        imgToDate = view.findViewById(R.id.imgToDate);
        btnGo = view.findViewById(R.id.buttonGo);
        tvBack = view.findViewById(R.id.imBack);
        chkOfficeStaff=view.findViewById(R.id.chkOfficeStaff);
        chkShiftStaff=view.findViewById(R.id.chkShiftStaff);
        chkShiftStaff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    reportFor = "shift";
                    multiSelectData();
                }
            }
        });
        chkOfficeStaff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    reportFor = "office";
                    multiSelectData();
                }
            }
        });
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportFor = "both";
                multiSelectData();
            }
        });
        strtext = preferenceManager.getStringPreference(PreferenceManager.KEY_DATA_SPINNER);



        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        if (strtext.equals("nodata"))
        {
            sp1.setVisibility(View.VISIBLE);
            employeeBtn.setText(R.string.no_employee);
            selectAll.setEnabled(false);
            final List<String> flatType = new ArrayList<String>();
            flatType.add(0, "No Employees");
            ArrayAdapter<String> flatListAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item,
                    flatType) {
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);

                    TextView tv = (TextView) view;
                    if (position == 0)
                    {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            flatListAdapter.setDropDownViewResource(R.layout.spinner_items2);
            sp1.setAdapter(flatListAdapter);
            sp1.setSelection(0);
        } else if (strtext.equals("data"))
        {
            sp1.setVisibility(GONE);
            employeeBtn.setText(R.string.select_employee);
            selectAll.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked())
                    {
                        employeeBtn.setText(R.string.all_emp);
                        employeeMode = "select_all";
                        reportFor = "both";
                    } else
                        employeeBtn.setText(R.string.select_employee);

                }
            });

          //  employeeSpinner();
        }

        txtToDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                long now = System.currentTimeMillis() - 1000;
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.DATE, +1);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), myDateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 24));
                datePickerDialog.getDatePicker().setMaxDate(now);
                datePickerDialog.show();
                dateCheck = "To";
            }
        });
        txtFromDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                long now = System.currentTimeMillis() - 1000;
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.DATE, +1);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), myDateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 24));
                datePickerDialog.getDatePicker().setMaxDate(now);
                datePickerDialog.show();
                dateCheck = "From";
            }
        });
        imgfromDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                long now = System.currentTimeMillis() - 1000;
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.DATE, +1);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), myDateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 24));
                datePickerDialog.getDatePicker().setMaxDate(now);
                datePickerDialog.show();
                dateCheck = "From";
            }
        });
        imgToDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                long now = System.currentTimeMillis() - 1000;
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.DATE, +1);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), myDateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 24));
                datePickerDialog.getDatePicker().setMaxDate(now);
                datePickerDialog.show();
                dateCheck = "To";
            }
        });

        employeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multiSelectData();
            }
        });
        btnGo.setOnClickListener(this);

        // No Internet Dialog

        return view;
    }

    private void onDateSetChange(DatePicker datePicker, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTimeInMillis(0);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Log.d(" PICK TIME ", " " + cal.getTime());
            cal.set(year, month, day);
            cal.clear(Calendar.HOUR_OF_DAY);
            cal.clear(Calendar.AM_PM);
            cal.clear(Calendar.MINUTE);
            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.MILLISECOND);
            Log.e("Date", " 1");
            Date chosenDate = cal.getTime();
            Log.e("Date", " 2");
            String strDateFrom = df.format(chosenDate);
            Log.e("Date", " 3");
            if (dateCheck.equalsIgnoreCase("From")) {
                //txtFromDate.setText(strDateFrom);
                // txtFromDate.setText(dd+"-"+mm+"-"+yy);
                txtFromDate.setText(dd + "-" + mm + "-" + yy);
                strFrom = strDateFrom;
            } else if (dateCheck.equalsIgnoreCase("To")) {
                //txtToDate.setText(strDateFrom);
                // txtToDate.setText(dd+"-"+mm+"-"+yy);
                txtToDate.setText(dd + "-" + mm + "-" + yy);
                Log.d(TAG, "onDateSetChange:strTo " + txtToDate.getText().toString());
                strTo = strDateFrom;
            }
        } catch (Exception ex) {
            Log.e("Date picker ", " Exception is " + ex.toString());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    private void send_data(){
        if (strFrom != null && strTo != null && strFrom.length() > 3 && strTo.length() > 3) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
                Date toDate = sdf.parse(strTo);
                Date fromDate = sdf.parse(strFrom);
                if (toDate.after(fromDate) || toDate.equals(fromDate)) {
                    Intent i = new Intent(getActivity(), EmployeeReport.class);
                    i.putExtra("employee_id", emplyoeeID);
                    i.putExtra("from_date", strFrom);
                    i.putExtra("to_date", strTo);
                    i.putExtra("mode", employeeMode);
                    i.putExtra("report_for", reportFor);
                    startActivity(i);
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "Please select valid Date Format", Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                Log.e("Date Conversion", "  Exception" + ex.toString());
            }
        } else {
            Toast.makeText(getContext(), "Please select valid From and To Date", Toast.LENGTH_LONG).show();

        }
    }



    private  void multiSelectData(){
        View itemLayoutView = LayoutInflater.from(getContext()).inflate(
                R.layout.multiselection_activity, null);
        new BottomSheetDialog(getContext());
        BottomSheetDialog dialog;
        dialog = new BottomSheetDialog(getContext(),R.style.TransparentDialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(itemLayoutView);
        dialog.setCancelable(true);
        BottomSheetDialog finalDialog = dialog;
        btnGetSelected = dialog.findViewById(R.id.btnGetSelected);
        btnOkay = dialog.findViewById(R.id.btnSelectedokay);
        btnCancel = dialog.findViewById(R.id.btnCancel);
        noData = dialog.findViewById(R.id.tvNodata);
        recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        for (int i=0;i<employees.size();i++){
            employees.get(i).setChecked(false);
        }
        adapter = new MultiAdapter(getContext(), employees);
        recyclerView.setAdapter(adapter);
        employeeNameList = new ArrayList<>();
        employeeIDList = new ArrayList<>();
        if(reportFor.equalsIgnoreCase("both")){
            Cursor cursor = db.getEmployeelist();
            if (cursor != null) {
                Log.e("count", cursor.getCount() + "");
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        employeeNameList.add(cursor.getString(cursor.getColumnIndex(SqLiteHelper.COLUMN_EMPLOYEE_NAME)));
                        employeeIDList.add(cursor.getString(cursor.getColumnIndex(SqLiteHelper.COLUMN_EMPLOYEE_ID)));
                        cursor.moveToNext();
                    }
                }
            }
        }
        adapter = new MultiAdapter(getContext(), employees);
        recyclerView.setAdapter(adapter);
        db.getSinlgeEntry(reportFor);

        Cursor cursor = db.getSinlgeEntry(reportFor);
        if (cursor != null) {
            Log.e("count", cursor.getCount() + "");
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    employeeNameList.add(cursor.getString(cursor.getColumnIndex(SqLiteHelper.COLUMN_EMPLOYEE_NAME)));
                    employeeIDList.add(cursor.getString(cursor.getColumnIndex(SqLiteHelper.COLUMN_EMPLOYEE_ID)));
                    cursor.moveToNext();
                }
            }
        }
        createList();
        if(reportFor.equalsIgnoreCase("both"))
        {
            for (int i=0;i<employees.size();i++){
                employees.get(i).setChecked(true);
            }
            btnGetSelected.setVisibility(GONE);
        }
        if(employees.size()==0){
            noData.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
        }else {
            noData.setVisibility(GONE);
            btnCancel.setVisibility(GONE);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDialog.dismiss();
            }
        });
        btnGetSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAll();
            }
        });
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (adapter.getSelected().size() > 0) {
                    String stringBuilder="";
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
                    if (stringBuilder.contains(",")) {
                        employeeMode = "multiple";
                    }else{
                        employeeMode = "single";
                    }
                    employeeBtn.setText(stringBuilderName);
                    emplyoeeID = stringBuilder;
                    finalDialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Select Employee", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
    public void reportTypeDailog(){
        View itemLayoutView = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialogforreporttype, null);
        final Button btnCancel = itemLayoutView.findViewById(R.id.btnCancel);
        final TextView shiftStaff = itemLayoutView.findViewById(R.id.shiftStaffReport);
        final TextView officeStaff = itemLayoutView.findViewById(R.id.offStaffType);
        final TextView bothStaff = itemLayoutView.findViewById(R.id.bothStaffReport);

        new BottomSheetDialog(getContext());
        BottomSheetDialog dialog;
        dialog = new BottomSheetDialog(getActivity(),R.style.TransparentDialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(itemLayoutView);
        dialog.setCancelable(true);
        BottomSheetDialog finalDialog = dialog;

        shiftStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportFor = "shift";
                multiSelectData();
                finalDialog.dismiss();
            }
        });

        officeStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportFor = "office";
                multiSelectData();
                finalDialog.dismiss();
            }
        });
        bothStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportFor = "both";
                multiSelectData();
                finalDialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDialog.dismiss();
            }
        });

        dialog.show();
    }
    private void createList()
    {
        employees = new ArrayList<>();
        for (int i = 0; i < employeeNameList.size(); i++) {
            Employee employee = new Employee();
            employee.setName(employeeNameList.get(i));
            employee.setId(employeeIDList.get(i));
            employee.setChecked(false);
            employees.add(employee);
        }
        adapter.setEmployees(employees);
    }


    private void selectAll() {
        for (int i=0;i<employees.size();i++){
            employees.get(i).setChecked(true);
        }
        adapter = new MultiAdapter(getContext(), employees);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onClick(View v) {

        int id1 = v.getId();
        if (id1 == R.id.buttonGo) {

                if (employees.size() > 0) {
                    send_data();
                }else if(employeeBtn.getText().toString().contains("All Employees Are Selected")){
                    send_data();
                }else {
                    Toast.makeText(getContext(), "Select Employee", Toast.LENGTH_SHORT).show();
                }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        super.onActivityResult(requestCode, resultCode, data);

    }
    private void settingToolBar()
    {
        toolbar.setTitle("Employee Entries");
        toolbar.setLogo(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
}
