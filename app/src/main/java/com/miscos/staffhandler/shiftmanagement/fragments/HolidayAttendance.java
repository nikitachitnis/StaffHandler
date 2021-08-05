package com.miscos.staffhandler.shiftmanagement.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.miscos.staffhandler.shiftmanagement.Api;
import com.miscos.staffhandler.shiftmanagement.adapters.EmployeeAdapter2;
import com.miscos.staffhandler.shiftmanagement.models.Employee;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonDataForShift;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonDataForWeekOffEmployees;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonDataForWorkingOnHoliday;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonResponse;
import com.miscos.staffhandler.shiftmanagement.models.HolidayList;
import com.miscos.staffhandler.shiftmanagement.models.Shift;
import com.fivemin.chief.nonetworklibrary.networkBroadcast.NoNet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*Developed under Miscos
 * Developed by Nikhil
 * 15-09-2020
 * */
public class HolidayAttendance extends AppCompatActivity implements View.OnClickListener {


    ImageView add, remove;
    Spinner selectdate;
    SimpleDateFormat simpleDateFormat;
    Calendar calendar;
    int mYear, mMonth, mDay;
    Handler handler;
    TextView employeeTypeTitle;
    GetJsonDataForShift getJsonDataForShift;
    Shift shift;
    PreferenceManager preferenceManager;
    Spinner shiftSpinner;
    List<String> noOfShifts = new ArrayList<>();
    List<String> holidayLists = new ArrayList<>();
    List<String> holidayDatesList = new ArrayList<>();
    Button btnGo, btnPermit;
    GetJsonDataForWorkingOnHoliday getJsonDataForWorkingOnHoliday;
    RecyclerView selectedEmployees, notSelectedEmployees;
    EmployeeAdapter2 selectedEmployeeAdapter, nonSelectedEmployeeAdapter;
    CardView selectedEmployeeCardView, nonSelectedEmployeeCardView;
    int selectedPos = -1, nonSelectedPos = -1;
    List<Employee> selected = new ArrayList<>(), nonSelected = new ArrayList<>();
    String selectedEmployeesList = "";
    LinearLayout selectEmployeeButtonLay;
    Date currentDate, holidayDate;
    String date, date1, date2;
    HolidayList holidayList;
    CheckBox weekOffEmployee;
    RadioGroup operationType;
    RadioButton officeStaff, shiftEmployee, both;
    GetJsonDataForWeekOffEmployees getJsonDataForWeekOffEmployees;
    LinearLayout selectShiftLay;
    String opType = "O",employeeType="office",employer_id,weekOff="N";
    int shiftNo=0;
    RadioButton rdbtnemp,rdbtncompny;
    private ProgressDialog progressDialog;
    ImageView tvBack;
    String attendace_type="c";
    // No Internet Dialog
    private NoNet mNoNet;
    private FragmentManager fm = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //  ((MainActivity) getActivity()).toolbar.setTitle("Admin Settings");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holidayattendance);
        fm = getSupportFragmentManager();
        mNoNet = new NoNet();
        mNoNet.initNoNet(this, fm);
        preferenceManager =new PreferenceManager(HolidayAttendance.this);
        employer_id = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID);
        /*((MainActivity) getActivity()).toolbar.setTitle("Holiday Attendance");
        progressBar = ((MainActivity) getActivity()).progressBar;*/
        //Progress Bar
        progressDialog = new ProgressDialog(this);
        progressDialog = ProgressDialog.show(HolidayAttendance.this, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.dismiss();

        tvBack =  findViewById(R.id.imBack);
        handler = new Handler(Looper.getMainLooper());
        nonSelectedEmployeeCardView = findViewById(R.id.employeeCardView);
        selectShiftLay = findViewById(R.id.selectShiftLay);
        employeeTypeTitle =findViewById(R.id.employee_type_title);
        selectedEmployeeCardView =findViewById(R.id.selectedEmployeeCardView);
        selectEmployeeButtonLay = findViewById(R.id.selectEmployeeButtonLay);
        weekOffEmployee =findViewById(R.id.selectWeekOffEmployees);
        operationType = findViewById(R.id.operationType);
        officeStaff = findViewById(R.id.officeRadio);
        shiftEmployee =findViewById(R.id.shiftRadio);
        both = findViewById(R.id.bothRadio);
        selectEmployeeButtonLay.setVisibility(View.GONE);
        add = findViewById(R.id.add);
        remove =findViewById(R.id.remove);
        selectdate = findViewById(R.id.holidayDateSpinner);
        btnGo = findViewById(R.id.btnGo);
        btnPermit = findViewById(R.id.btnPermit);
        btnPermit.setVisibility(View.GONE);
        selectedEmployees = findViewById(R.id.selctedEmployeeRecyclerView);
        notSelectedEmployees = findViewById(R.id.employeeRecyclerView);
        selectedEmployees.setLayoutManager(new LinearLayoutManager(this));
        notSelectedEmployees.setLayoutManager(new LinearLayoutManager(this));
        selectedEmployees.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        notSelectedEmployees.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        shiftSpinner = findViewById(R.id.shiftSpinner);
        rdbtncompny=findViewById(R.id.rdbtncompny);
        rdbtncompny.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    attendace_type="c";
            }
        });
        rdbtncompny.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    attendace_type="e";
            }
        });
        rdbtnemp=findViewById(R.id.rdbtnemployee);
        add.setRotation(180);
        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = simpleDateFormat.format(calendar.getTime());
        try {
            currentDate = simpleDateFormat.parse(date);
            date1 = simpleDateFormat.format(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        operationType.check(R.id.officeRadio);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                progressDialog.show();
                (Api.getClient()).getShiftData("show", employer_id).enqueue(new Callback<GetJsonDataForShift>() {
                    @Override
                    public void onResponse(Call<GetJsonDataForShift> call, Response<GetJsonDataForShift> response) {
                        try {
                            getJsonDataForShift = response.body();

                            if (getJsonDataForShift.getErrorCode() == 101)
                            {       progressDialog.dismiss();
                                employeeTypeTitle.setVisibility(View.VISIBLE);
                                operationType.setVisibility(View.VISIBLE);
                                shift = getJsonDataForShift.getShiftData();
                                    int no = Integer.parseInt(shift.getShiftCount());
                                    if (no==0){
                                        shiftSpinner.setVisibility(View.GONE);
                                        employeeTypeTitle.setVisibility(View.GONE);
                                        operationType.setVisibility(View.GONE);
                                        progressDialog.dismiss();
                                    }else {
                                        ArrayList<String> shiftNames=new ArrayList<>();
                                        for (int i = 1; i <= no; i++)
                                        {
                                            noOfShifts.add(String.valueOf(i));
                                            switch (i)
                                            {
                                                case 1:
                                                    shiftNames.add(shift.getShift1name());
                                                    break;
                                                case 2:
                                                    shiftNames.add(shift.getShift2name());
                                                    break;
                                                case 3:
                                                    shiftNames.add(shift.getShift3name());
                                                    break;
                                            }


                                        }
                                        ArrayAdapter<String> shiftAdapter = new ArrayAdapter<String>(HolidayAttendance.this, R.layout.spinner_item1, R.id.spinnerText1, shiftNames);
                                        shiftSpinner.setAdapter(shiftAdapter);
                                    }
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            getHolidayList();
                                        }
                                    }, 1000);

                            } else {
                                progressDialog.dismiss();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                            final Dialog dialog1 = new Dialog(HolidayAttendance.this);
// Include dialog.xml file
                            dialog1.setContentView(R.layout.dialog_exit);
                            dialog1.show();

                            TextView heading1 = dialog1.findViewById(R.id.tv_quit_learning);
                            TextView description1 = dialog1.findViewById(R.id.tv_description);
                            Button btnNo1 = (Button) dialog1.findViewById(R.id.btn_no);
                            Button btnYes1 = (Button) dialog1.findViewById(R.id.btn_yes);
                            heading1.setText("Error");
                            description1.setText("An unfortunate error occured, please try again.");
                            btnNo1.setVisibility(View.GONE);
                            btnYes1.setText("Ok");
                            btnYes1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog1.dismiss();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<GetJsonDataForShift> call, Throwable t) {
                        progressDialog.dismiss();
                    }
                });

            }
        }, 1000);

        btnGo.setOnClickListener(this);
        remove.setOnClickListener(this);
        add.setOnClickListener(this);
        btnPermit.setOnClickListener(this);
        selectdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                {
                    try {
                        holidayDate = simpleDateFormat.parse(holidayDatesList.get(position - 1));
                        date2 = simpleDateFormat.format(holidayDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        operationType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.officeRadio:
                        selectShiftLay.setVisibility(View.GONE);
                        opType = "O";
                        employeeType="office";
                        weekOff="N";
                        getHolidayList();
                        selectEmployeeButtonLay.setVisibility(View.GONE);
                        nonSelectedEmployeeCardView.setVisibility(View.GONE);
                        btnPermit.setVisibility(View.GONE);
                        selectedEmployeeCardView.setVisibility(View.GONE);
                        break;
                    case R.id.shiftRadio:
                        selectShiftLay.setVisibility(View.VISIBLE);
                        opType = "S";
                        employeeType="shift";
                        getHolidayList();
                        selectEmployeeButtonLay.setVisibility(View.GONE);
                        btnPermit.setVisibility(View.GONE);
                        nonSelectedEmployeeCardView.setVisibility(View.GONE);
                        selectedEmployeeCardView.setVisibility(View.GONE);
                        break;
                    case R.id.bothRadio:
                        selectShiftLay.setVisibility(View.VISIBLE);
                        opType = "B";
                        employeeType="both";
                        getHolidayList();
                        selectEmployeeButtonLay.setVisibility(View.GONE);
                        btnPermit.setVisibility(View.GONE);
                        nonSelectedEmployeeCardView.setVisibility(View.GONE);
                        selectedEmployeeCardView.setVisibility(View.GONE);
                        break;
                }
            }
        });

        //Back Button
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(HolidayAttendance.this,EmployerZone.class));
                finish();
            }
        });

        weekOffEmployee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    weekOff="Y";
                }
                else {
                    weekOff="N";
                }
            }
        });
    }


    @Override
    protected void onResume()
    {
        mNoNet.RegisterNoNet();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mNoNet.unRegisterNoNet();
        super.onPause();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGo:
                if (selectdate.getSelectedItemPosition() == 0) {
                    Toast.makeText(this, "Select Holiday Date First", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    try {
                        selected=new ArrayList<Employee>();
                        nonSelected=new ArrayList<Employee>();
                        if (opType.matches("O"))
                        {
                            shiftNo=0;
                        }
                        else {
                            shiftNo=shiftSpinner.getSelectedItemPosition()+1;
                        }
                        (Api.getClient()).getEmployeeListForHoilday("fetch", employer_id,String.valueOf(shiftNo), date2,employeeType,weekOff).enqueue(new Callback<GetJsonDataForWorkingOnHoliday>() {
                            @Override
                            public void onResponse(Call<GetJsonDataForWorkingOnHoliday> call, Response<GetJsonDataForWorkingOnHoliday> response) {
                                try {

                                    getJsonDataForWorkingOnHoliday = response.body();
                                    if (getJsonDataForWorkingOnHoliday.getErrorCode() == 101)
                                    {
                                        progressDialog.dismiss();
                                        if(getJsonDataForWorkingOnHoliday.getGrantAccessData().isEmpty()&&getJsonDataForWorkingOnHoliday.getNotGrantedAccessData().isEmpty())
                                        {
                                            nonSelectedEmployeeCardView.setVisibility(View.GONE);
                                            selectEmployeeButtonLay.setVisibility(View.GONE);
                                            btnPermit.setVisibility(View.GONE);
                                            selectedEmployeeCardView.setVisibility(View.GONE);
                                            final PrettyDialog prettyDialog = new PrettyDialog(HolidayAttendance.this);
                                            prettyDialog
                                                    .setTitle("Holiday Attendance")
                                                    .setMessage("No data found")
                                                    .setIcon(R.drawable.cross)
                                                    .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                                        @Override
                                                        public void onClick() {
                                                            prettyDialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                            return;
                                        }

                                        selectedEmployeeCardView.setVisibility(View.VISIBLE);
                                        nonSelectedEmployeeCardView.setVisibility(View.VISIBLE);
                                        selectEmployeeButtonLay.setVisibility(View.VISIBLE);
                                        btnPermit.setVisibility(View.VISIBLE);
                                        if (!getJsonDataForWorkingOnHoliday.getGrantAccessData().isEmpty())
                                        {

                                            selected = getJsonDataForWorkingOnHoliday.getGrantAccessData();
                                            selectedEmployeeAdapter = new EmployeeAdapter2(HolidayAttendance.this, selected, HolidayAttendance.this);
                                            selectedEmployeeAdapter.ClickListener(new EmployeeAdapter2.OnButtonClick() {
                                                @Override
                                                public void addButtonClick(int pos) {
                                                    selectedPos = pos;
                                                    Log.e("Selected Pos", String.valueOf(selectedPos));
                                                }
                                            });
                                            selectedEmployees.setAdapter(selectedEmployeeAdapter);
                                        }
                                        else
                                        { selected.clear();
                                            selectedEmployeeAdapter = new EmployeeAdapter2(HolidayAttendance.this, selected, HolidayAttendance.this);
                                            selectedEmployeeAdapter.ClickListener(new EmployeeAdapter2.OnButtonClick() {
                                                @Override
                                                public void addButtonClick(int pos) {
                                                    selectedPos = pos;
                                                    Log.e("Selected Pos", String.valueOf(selectedPos));
                                                }
                                            });
                                            selectedEmployees.setAdapter(selectedEmployeeAdapter);
                                        }
                                        if (!getJsonDataForWorkingOnHoliday.getNotGrantedAccessData().isEmpty())
                                        {
                                            nonSelected=getJsonDataForWorkingOnHoliday.getNotGrantedAccessData();

                                            nonSelectedEmployeeAdapter = new EmployeeAdapter2(HolidayAttendance.this, nonSelected, HolidayAttendance.this);
                                            nonSelectedEmployeeAdapter.ClickListener(new EmployeeAdapter2.OnButtonClick() {
                                                @Override
                                                public void addButtonClick(int pos) {
                                                    nonSelectedPos = pos;
                                                    Log.e("Non Selected Pos", String.valueOf(nonSelectedPos));
                                                }
                                            });
                                            notSelectedEmployees.setAdapter(nonSelectedEmployeeAdapter);
                                        }else
                                            { nonSelected.clear();
                                                nonSelectedEmployeeAdapter = new EmployeeAdapter2(HolidayAttendance.this, nonSelected, HolidayAttendance.this);
                                                nonSelectedEmployeeAdapter.ClickListener(new EmployeeAdapter2.OnButtonClick() {
                                                    @Override
                                                    public void addButtonClick(int pos) {
                                                        nonSelectedPos = pos;
                                                        Log.e("Non Selected Pos", String.valueOf(nonSelectedPos));
                                                    }
                                                });
                                                notSelectedEmployees.setAdapter(nonSelectedEmployeeAdapter);
                                            }
                                            progressDialog.dismiss();


                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(HolidayAttendance.this, getJsonDataForWorkingOnHoliday.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                    final Dialog dialog1 = new Dialog(HolidayAttendance.this);
                                    // Include dialog.xml file
                                    dialog1.setContentView(R.layout.dialog_exit);
                                    dialog1.show();
                                    TextView heading1 = dialog1.findViewById(R.id.tv_quit_learning);
                                    TextView description1 = dialog1.findViewById(R.id.tv_description);
                                    Button btnNo1 = (Button) dialog1.findViewById(R.id.btn_no);
                                    Button btnYes1 = (Button) dialog1.findViewById(R.id.btn_yes);
                                    heading1.setText("Error");
                                    description1.setText("An unfortunate error occured, please try again.");
                                    btnNo1.setVisibility(View.GONE);
                                    btnYes1.setText("Ok");
                                    btnYes1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog1.dismiss();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Call<GetJsonDataForWorkingOnHoliday> call, Throwable t) {
                                progressDialog.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btnPermit:

             permit();


                break;
            case R.id.add:
                if (nonSelected.isEmpty() || nonSelectedPos == -1)
                {
                    Toast.makeText(HolidayAttendance.this, "No Employee Selected", Toast.LENGTH_SHORT).show();
                } else {

                    final PrettyDialog dialog2=    new PrettyDialog(HolidayAttendance.this);
                    dialog2.setTitle("Holiday attendance")
                            .setMessage("Please select from below")
                            .setIcon(R.drawable.info,R.color.primaryTextColor,null)
                            .addButton("Inviting by company for office work", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                @Override
                                public void onClick()
                                {

                                    nonSelected.get(nonSelectedPos).setInvite_type("c");
                                    addTopermit();
                                    dialog2.dismiss();

                                }
                            })
                            .addButton("Inviting due to incomplete work(under performer)", R.color.whiteTextColor, R.color.red, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    nonSelected.get(nonSelectedPos).setInvite_type("e");
                                    addTopermit();
                                    dialog2.dismiss();



                                }
                            })
                            .show();


                }
                break;
            case R.id.remove:
                if (selected.isEmpty() || selectedPos == -1) {
                    Toast.makeText(this, "No Employee Selected", Toast.LENGTH_SHORT).show();
                } else {
                    nonSelectedPos=-1;
                    nonSelected.add(selected.get(selectedPos));
                    selected.remove(selectedPos);
                    selectedPos=-1;
                    if (selected.isEmpty()) {
                        selectedEmployeeCardView.setVisibility(View.GONE);
                    } else {
                        selectedEmployeeCardView.setVisibility(View.VISIBLE);
                    }
                    if (nonSelected.isEmpty()) {
                        nonSelectedEmployeeCardView.setVisibility(View.GONE);
                    } else {
                        nonSelectedEmployeeCardView.setVisibility(View.VISIBLE);
                    }
                    selectedEmployeeAdapter = new EmployeeAdapter2(HolidayAttendance.this, selected, HolidayAttendance.this);
                    selectedEmployeeAdapter.ClickListener(new EmployeeAdapter2.OnButtonClick() {
                        @Override
                        public void addButtonClick(int pos) {
                            selectedPos = pos;
                        }
                    });
                    selectedEmployees.setAdapter(selectedEmployeeAdapter);
                    nonSelectedEmployeeAdapter = new EmployeeAdapter2(HolidayAttendance.this, nonSelected, HolidayAttendance.this);
                    nonSelectedEmployeeAdapter.ClickListener(new EmployeeAdapter2.OnButtonClick() {
                        @Override
                        public void addButtonClick(int pos) {
                            nonSelectedPos = pos;
                        }
                    });
                    notSelectedEmployees.setAdapter(nonSelectedEmployeeAdapter);
                }
                break;
        }
    }
    void addTopermit()
    {
        selectedPos=-1;
        selected.add(nonSelected.get(nonSelectedPos));
        nonSelected.remove(nonSelectedPos);
        nonSelectedPos=-1;
        if (selected.isEmpty()) {
            selectedEmployeeCardView.setVisibility(View.GONE);
        } else {
            selectedEmployeeCardView.setVisibility(View.VISIBLE);
        }
        if (nonSelected.isEmpty()) {
            nonSelectedEmployeeCardView.setVisibility(View.GONE);
        } else {
            nonSelectedEmployeeCardView.setVisibility(View.VISIBLE);
        }
        selectedEmployeeAdapter = new EmployeeAdapter2(HolidayAttendance.this, selected, HolidayAttendance.this);
        selectedEmployeeAdapter.ClickListener(new EmployeeAdapter2.OnButtonClick() {
            @Override
            public void addButtonClick(int pos) {
                selectedPos = pos;
            }
        });
        selectedEmployees.setAdapter(selectedEmployeeAdapter);
        nonSelectedEmployeeAdapter = new EmployeeAdapter2(HolidayAttendance.this, nonSelected, HolidayAttendance.this);
        nonSelectedEmployeeAdapter.ClickListener(new EmployeeAdapter2.OnButtonClick() {
            @Override
            public void addButtonClick(int pos) {
                nonSelectedPos = pos;
            }
        });
        notSelectedEmployees.setAdapter(nonSelectedEmployeeAdapter);
    }
    void permit()
    {
        final Dialog dialog = new Dialog(HolidayAttendance.this);
// Include dialog.xml file
        dialog.setContentView(R.layout.dialog_exit);
        dialog.show();

        TextView heading = dialog.findViewById(R.id.tv_quit_learning);
        TextView description = dialog.findViewById(R.id.tv_description);
        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
        heading.setText("Permit");
        description.setText("Are You Sure ?");
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (!selected.isEmpty()) {
                    for (int i = 0; i < selected.size(); i++) {
                        if (i == 0) {
                            selectedEmployeesList = selected.get(i).getEmployeeId()+"@"+selected.get(i).getInvite_type();
                        } else {
                            selectedEmployeesList = selectedEmployeesList + "," + selected.get(i).getEmployeeId()+"@"+selected.get(i).getInvite_type();;
                        }
                    }
                }
                (Api.getClient()).setEmployeeListForHoilday("insert", employer_id, selectedEmployeesList, String.valueOf(shiftNo), date2).enqueue(new Callback<GetJsonResponse>() {
                    @Override
                    public void onResponse(Call<GetJsonResponse> call, Response<GetJsonResponse> response) {
                        try {
                            if (response.body().getErrorCode() == 101) {
                                //startActivity(new Intent(HolidayAttendance.this,EmployerZone.class));
                                finish();
                                Toast.makeText(HolidayAttendance.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(HolidayAttendance.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                            final Dialog dialog1 = new Dialog(HolidayAttendance.this);
// Include dialog.xml file
                            dialog1.setContentView(R.layout.dialog_exit);
                            dialog1.show();

                            TextView heading1 = dialog1.findViewById(R.id.tv_quit_learning);
                            TextView description1 = dialog1.findViewById(R.id.tv_description);
                            Button btnNo1 = (Button) dialog1.findViewById(R.id.btn_no);
                            Button btnYes1 = (Button) dialog1.findViewById(R.id.btn_yes);
                            heading1.setText("Error");
                            description1.setText("An unfortunate error occured, please try again.");
                            btnNo1.setVisibility(View.GONE);
                            btnYes1.setText("Ok");
                            btnYes1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog1.dismiss();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<GetJsonResponse> call, Throwable t) {
                        progressDialog.dismiss();
                    }
                });
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    void getHolidayList()
    {
        progressDialog.show();
        holidayLists=new ArrayList<String>();
        holidayDatesList=new ArrayList<String>();
        (Api.getClient()).getHolidayList("holiday_list", employer_id, date1, opType).enqueue(new Callback<HolidayList>() {
            @Override
            public void onResponse(Call<HolidayList> call, Response<HolidayList> response) {
                try {
                    holidayList = response.body();
                    if (holidayList.getErrorCode() == 101)
                    {
                        progressDialog.dismiss();
                        holidayLists = holidayList.getHolidayList();
                        for (int i = 0; i < holidayLists.size(); i++) {
                            String[] sep = holidayLists.get(i).split(":");
                            holidayDatesList.add(sep[1]);
                        }
                        holidayLists.add(0, "Select Holiday");
                        ArrayAdapter<String> holidayAdapter = new ArrayAdapter<String>(HolidayAttendance.this, R.layout.spinner_item1, R.id.spinnerText1, holidayLists);
                        selectdate.setAdapter(holidayAdapter);

                    } else {
                        progressDialog.dismiss();
                        holidayLists.add(0, "Select Holiday");
                        ArrayAdapter<String> holidayAdapter = new ArrayAdapter<String>(HolidayAttendance.this, R.layout.spinner_item1, R.id.spinnerText1, holidayLists);
                        selectdate.setAdapter(holidayAdapter);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                    final Dialog dialog1 = new Dialog(HolidayAttendance.this);
// Include dialog.xml file
                    dialog1.setContentView(R.layout.dialog_exit);
                    dialog1.show();

                    TextView heading1 = dialog1.findViewById(R.id.tv_quit_learning);
                    TextView description1 = dialog1.findViewById(R.id.tv_description);
                    Button btnNo1 = (Button) dialog1.findViewById(R.id.btn_no);
                    Button btnYes1 = (Button) dialog1.findViewById(R.id.btn_yes);
                    heading1.setText("Error");
                    description1.setText("An unfortunate error occured, please try again.");
                    btnNo1.setVisibility(View.GONE);
                    btnYes1.setText("Ok");
                    btnYes1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<HolidayList> call, Throwable t) {
       progressDialog.dismiss();
            }
        });

    }
    //backpress
    protected void exitByBackKey()
    {

        final Dialog dialog = new Dialog(HolidayAttendance.this);
// Include dialog.xml file
        dialog.setContentView(R.layout.dialog_exit);
        dialog.show();

        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
               /* Intent intent = new Intent(HolidayAttendance.this, EmployerZone.class);
                startActivity(intent);*/
                finish();
            }
        });
// if decline button is clicked, close the custom dialog
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });

    }
    @Override
    public void onBackPressed() {
        exitByBackKey();
    }
}