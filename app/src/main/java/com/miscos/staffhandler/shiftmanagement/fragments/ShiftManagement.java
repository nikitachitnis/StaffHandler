package com.miscos.staffhandler.shiftmanagement.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.activities.EmployerZone;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.miscos.staffhandler.shiftmanagement.Api;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonDataForEmployee;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonDataForShift;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonResponse;
import com.miscos.staffhandler.shiftmanagement.models.Shift;
import com.fivemin.chief.nonetworklibrary.networkBroadcast.NoNet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/*Developed under Miscos
 * Developed by Nikhil
 * 15-09-2020
 * */
public class ShiftManagement extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    TextView startTimeShift1, stopTimeShift1, preStartTimeShift1, postStopTimeShift1, startTimeShift2, stopTimeShift2, preStartTimeShift2, postStopTimeShift2, startTimeShift3, stopTimeShift3, preStartTimeShift3, postStopTimeShift3;
    Date startTimeShift1Date, stopTimeShift1Date, preStartTimeShift1Date, postStopTimeShift1Date, startTimeShift2Date,
            stopTimeShift2Date, preStartTimeShift2Date, postStopTimeShift2Date, startTimeShift3Date, stopTimeShift3Date,
            preStartTimeShift3Date, postStopTimeShift3Date,breakstarttime1Date,breakstoptimeDate,breakstarttime2Date,breakstoptime2Date,breakstarttime3Date,breakstoptime3Date;
    TextView action1, action2, action3,txtnoshifts;
    Button shift1Text, shift2Text, shift3Text;
    GetJsonDataForShift getJsonDataForShift;
    Shift shift;
Spinner spinduration1,spinduration2,spinduration3;
String duration1="",duration2="",duration3="";
    TextView breakstarttime1,breakstoptime,breakstarttime2,breakstoptime2,breakstarttime3,breakstoptime3;
    SimpleDateFormat timeFormat, timeFormat1;
    int mHour, mMinute, affectedEmployees = 0;
    boolean shift1 = false, shift2 = false, shift3 = false;
    List<String> noOfShifts = new ArrayList<>(Arrays.asList("0", "1", "2", "3"));
    List<String> breakDuration= new ArrayList<>(Arrays.asList("Select","10 mins", "15 mins", "20 mins", "30 mins","60 mins","90 mins"));
    ArrayAdapter<String> adapter1,adapter2,adapter3;
    List<String> noOfShifts1 = new ArrayList<>();
    Spinner shiftSpinner;
    Button submit;
    PreferenceManager preferenceManager;
    CardView shift1CardView, shift2CardView, shift3CardView;
    LayerDrawable shape, shape1;
    GradientDrawable gradientDrawable, gradientDrawable1;
    Handler handler;
    ArrayAdapter<String> shiftAdapter, shiftAdapter1;
    private ProgressDialog progressDialog;
    String employer_id;
    Toolbar toolbar;
    ImageView tvBack;
    EditText edtshiftname1,edtshiftname2,edtshiftname3;
    // No Internet Dialog
    private NoNet mNoNet;
    private FragmentManager fm = null;
    String send_notification="N",send_voice_alarm="N";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //  ((MainActivity) getActivity()).toolbar.setTitle("Admin Settings");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shiftmanagement);

        fm = getSupportFragmentManager();
        mNoNet = new NoNet();
        mNoNet.initNoNet(this, fm);

        //Progress Bar
        progressDialog = new ProgressDialog(this);
        progressDialog = ProgressDialog.show(this, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.dismiss();

        handler = new Handler(Looper.getMainLooper());
        //((MainActivity) getActivity()).toolbar.setTitle("Shift Management");
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat1 = new SimpleDateFormat("HH:mm");
        //progressBar = ((MainActivity) getActivity()).progressBar;
        preferenceManager =new PreferenceManager(ShiftManagement.this);
        employer_id = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID);
        tvBack =  findViewById(R.id.imBack);
        shift1Text = findViewById(R.id.shiftNo1);
        shift2Text = findViewById(R.id.shiftNo2);
        shift3Text = findViewById(R.id.shiftNo3);
        shiftSpinner = findViewById(R.id.shiftNo);
        txtnoshifts=findViewById(R.id.txtnoshifts);
        edtshiftname1=findViewById(R.id.edtshift1name);
        edtshiftname2=findViewById(R.id.edtshift1name2);
        edtshiftname3=findViewById(R.id.edtshift1name3);
        shift1CardView = findViewById(R.id.card_view_shift1);
        shift2CardView = findViewById(R.id.card_view_shift2);
        shift3CardView = findViewById(R.id.card_view_shift3);
        startTimeShift1 = findViewById(R.id.startTimeShift1);
        stopTimeShift1 = findViewById(R.id.stopTimeShift1);
        preStartTimeShift1 = findViewById(R.id.preStartTimeShift1);
        postStopTimeShift1 = findViewById(R.id.postStopTimeShift1);
        startTimeShift2 = findViewById(R.id.startTimeShift2);
        stopTimeShift2 = findViewById(R.id.stopTimeShift2);
        preStartTimeShift2 = findViewById(R.id.preStartTimeShift2);
        postStopTimeShift2 = findViewById(R.id.postStopTimeShift2);
        startTimeShift3 = findViewById(R.id.startTimeShift3);
        stopTimeShift3 = findViewById(R.id.stopTimeShift3);
        preStartTimeShift3 = findViewById(R.id.preStartTimeShift3);
        postStopTimeShift3 = findViewById(R.id.postStopTimeShift3);
        action1 = findViewById(R.id.actionText1);
        action2 = findViewById(R.id.actionText2);
        action3 = findViewById(R.id.actionText3);
        submit = findViewById(R.id.btnSave);
        breakstarttime1=findViewById(R.id.break1strattime);
        breakstoptime=findViewById(R.id.break1stoptime);
        breakstarttime2=findViewById(R.id.break2strattime);
        breakstoptime2=findViewById(R.id.break2stoptime);
        breakstarttime3=findViewById(R.id.break3strattime);
        breakstoptime3=findViewById(R.id.break3stoptime);
        spinduration1=findViewById(R.id.breaktimeduration1);
        spinduration2=findViewById(R.id.breaktimeduration2);
        spinduration3=findViewById(R.id.breaktimeduration3);
        adapter1=new ArrayAdapter<>(this,R.layout.spinner_item_duration,R.id.spinnerText,breakDuration);
        spinduration1.setAdapter(adapter1);
        spinduration2.setAdapter(adapter1);
        spinduration3.setAdapter(adapter1);
        spinduration1.setSelection(0);
        spinduration2.setSelection(0);
        spinduration3.setSelection(0);
        spinduration1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(i==0)
                    return;
                if(diffTime(breakstarttime1Date,breakstoptimeDate,Integer.parseInt(breakDuration.get(i).split(" ")[0])))
                duration1=breakDuration.get(i);
                else
                {
                    spinduration1.setSelection(0);
                    duration1="";
                    Toast.makeText(ShiftManagement.this, "Choose valid duration", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
        spinduration2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

                if(i==0)
                    return;
                if(diffTime(breakstarttime2Date,breakstoptime2Date,Integer.parseInt(breakDuration.get(i).split(" ")[0])))
                    duration2=breakDuration.get(i);
                else
                {
                    spinduration2.setSelection(0);
                    duration2="";
                    Toast.makeText(ShiftManagement.this, "Choose valid duration", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
        spinduration3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {


                if(i==0)
                    return;
                if(diffTime(breakstarttime3Date,breakstoptime3Date,Integer.parseInt(breakDuration.get(i).split(" ")[0])))
                    duration3=breakDuration.get(i);
                else
                {
                    spinduration3.setSelection(0);
                    duration3="";
                    Toast.makeText(ShiftManagement.this, "Choose valid duration", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });


        shape = (LayerDrawable) ContextCompat.getDrawable(ShiftManagement.this, R.drawable.border1);
        gradientDrawable = (GradientDrawable) shape.findDrawableByLayerId(R.id.shape);
        gradientDrawable.setColor(ContextCompat.getColor(ShiftManagement.this, R.color.green_500));

        shape1 = (LayerDrawable) ContextCompat.getDrawable(ShiftManagement.this, R.drawable.border1);
        gradientDrawable1 = (GradientDrawable) shape1.findDrawableByLayerId(R.id.shape);
        gradientDrawable1.setColor(ContextCompat.getColor(ShiftManagement.this, R.color.transparent));

        shiftAdapter = new ArrayAdapter<String>(ShiftManagement.this, R.layout.spinner_items, R.id.spinnerText, noOfShifts);

        shiftSpinner.setAdapter(shiftAdapter);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getShiftData();
            }
        }, 1000);

        startTimeShift1.setOnClickListener(this);
        startTimeShift2.setOnClickListener(this);
        startTimeShift3.setOnClickListener(this);
        stopTimeShift1.setOnClickListener(this);
        preStartTimeShift1.setOnClickListener(this);
        postStopTimeShift1.setOnClickListener(this);
        stopTimeShift2.setOnClickListener(this);
        preStartTimeShift2.setOnClickListener(this);
        postStopTimeShift2.setOnClickListener(this);
        stopTimeShift3.setOnClickListener(this);
        preStartTimeShift3.setOnClickListener(this);
        postStopTimeShift3.setOnClickListener(this);
        shift1Text.setOnClickListener(this);
        shift2Text.setOnClickListener(this);
        shift3Text.setOnClickListener(this);
        action1.setOnClickListener(this);
        action2.setOnClickListener(this);
        action3.setOnClickListener(this);
        submit.setOnClickListener(this);
        breakstarttime1.setOnClickListener(this);
        breakstoptime.setOnClickListener(this);
        breakstarttime2.setOnClickListener(this);
        breakstoptime2.setOnClickListener(this);
        breakstarttime3.setOnClickListener(this);
        breakstoptime3.setOnClickListener(this);
        shiftSpinner.setOnItemSelectedListener(this);
        //Back Button
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        mNoNet.RegisterNoNet();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mNoNet.unRegisterNoNet();
        super.onPause();
    }
    public void getShiftData()
    {
        progressDialog.show();
        (Api.getClient()).getShiftData("show", employer_id).enqueue(new Callback<GetJsonDataForShift>() {
            @Override
            public void onResponse(Call<GetJsonDataForShift> call, Response<GetJsonDataForShift> response) {
                try {
                    getJsonDataForShift = response.body();
                    if (getJsonDataForShift.getErrorCode() == 101)
                    {
                        progressDialog.dismiss();
                        shift = getJsonDataForShift.getShiftData();
                        try {
                            for (int i = 1; i <= Integer.parseInt(shift.getShiftCount()); i++) {
                                noOfShifts1.add("Shift no. " + i);
                            }
                            //shiftNo.setText(shift.getShiftCount());
                            shiftSpinner.setSelection(Integer.parseInt(shift.getShiftCount()));
                            if(Integer.parseInt(shift.getShiftCount())==0)
                                txtnoshifts.setVisibility(View.VISIBLE);
                            else
                                txtnoshifts.setVisibility(View.GONE);


                            edtshiftname1.setText(shift.getShift1name());
                            edtshiftname2.setText(shift.getShift2name());
                            edtshiftname3.setText(shift.getShift3name());
                            startTimeShift1Date = timeFormat.parse(shift.getSec1InTiming());

                            stopTimeShift1Date = timeFormat.parse(shift.getSec1OutTiming());
                            preStartTimeShift1Date = timeFormat.parse(shift.getSec1PreStartTiming());
                            postStopTimeShift1Date = timeFormat.parse(shift.getSec1PostCloseTiming());

                            startTimeShift2Date = timeFormat.parse(shift.getSec2InTiming());
                            stopTimeShift2Date = timeFormat.parse(shift.getSec2OutTiming());
                            preStartTimeShift2Date = timeFormat.parse(shift.getSec2PreStartTiming());
                            postStopTimeShift2Date = timeFormat.parse(shift.getSec2PostCloseTiming());

                            startTimeShift3Date = timeFormat.parse(shift.getSec3InTiming());
                            stopTimeShift3Date = timeFormat.parse(shift.getSec3OutTiming());
                            preStartTimeShift3Date = timeFormat.parse(shift.getSec3PreStartTiming());
                            postStopTimeShift3Date = timeFormat.parse(shift.getSec3PostCloseTiming());

                            startTimeShift1.setText(timeFormat1.format(startTimeShift1Date));
                            stopTimeShift1.setText(timeFormat1.format(stopTimeShift1Date));
                            preStartTimeShift1.setText(timeFormat1.format(preStartTimeShift1Date));
                            postStopTimeShift1.setText(timeFormat1.format(postStopTimeShift1Date));

                            startTimeShift2.setText(timeFormat1.format(startTimeShift2Date));
                            stopTimeShift2.setText(timeFormat1.format(stopTimeShift2Date));
                            preStartTimeShift2.setText(timeFormat1.format(preStartTimeShift2Date));
                            postStopTimeShift2.setText(timeFormat1.format(postStopTimeShift2Date));

                            startTimeShift3.setText(timeFormat1.format(startTimeShift3Date));
                            stopTimeShift3.setText(timeFormat1.format(stopTimeShift3Date));
                            preStartTimeShift3.setText(timeFormat1.format(preStartTimeShift3Date));
                            postStopTimeShift3.setText(timeFormat1.format(postStopTimeShift3Date));

                            breakstarttime1Date = timeFormat.parse(shift.getShift_1_break_from_time());

                            breakstarttime1Date = timeFormat.parse(shift.getShift_1_break_from_time());
                            breakstarttime2Date = timeFormat.parse(shift.getShift_2_break_from_time());
                            breakstoptimeDate = timeFormat.parse(shift.getShift_1_break_to_time());
                            breakstoptime2Date = timeFormat.parse(shift.getShift_2_break_to_time());
                            breakstoptime3Date = timeFormat.parse(shift.getShift_3_break_to_time());

                            breakstarttime1.setText(timeFormat1.format(breakstarttime1Date));
                            breakstarttime2.setText(timeFormat1.format(breakstarttime2Date));
                            breakstarttime3.setText(timeFormat1.format(breakstarttime3Date));
                            breakstoptime.setText(timeFormat1.format(breakstoptimeDate));
                            breakstoptime2.setText(timeFormat1.format(breakstoptime2Date));
                            breakstoptime3.setText(timeFormat1.format(breakstoptime3Date));

                            if (!startTimeShift1.getText().toString().matches("00:00") && startTimeShift2.getText().toString().matches("00:00")) {
                                action1.setVisibility(View.VISIBLE);
                                action1.setText("Remove");
                            } else {
                                action1.setVisibility(View.GONE);
                            }

                            if (!startTimeShift2.getText().toString().matches("00:00")) {
                                action2.setText("Remove");
                            }
                            if (!startTimeShift3.getText().toString().matches("00:00")) {
                                action3.setText("Remove");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetJsonDataForShift> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.shiftNo1:
                if (shift1) {
                    shift1Text.setBackgroundColor(getResources().getColor(R.color.blue_600));
                    edtshiftname1.setEnabled(false);
                    shift1 = false;
                } else {
                    shift1Text.setBackgroundColor((getResources().getColor(R.color.green_500)));
                    edtshiftname1.setEnabled(true);
                    shift2Text.setBackgroundColor((getResources().getColor(R.color.blue_600)));
                    shift3Text.setBackgroundColor((getResources().getColor(R.color.blue_600)));
                    shift1 = true;
                    shift2 = false;
                    shift3 = false;
                }
                break;
            case R.id.shiftNo2:
                if (shift2) {
                    shift2Text.setBackgroundColor((getResources().getColor(R.color.blue_600)));
                    edtshiftname2.setEnabled(false);
                    shift2 = false;
                } else {
                    shift1Text.setBackgroundColor((getResources().getColor(R.color.blue_600)));
                    shift2Text.setBackgroundColor((getResources().getColor(R.color.green_500)));
                    edtshiftname2.setEnabled(true);
                    shift3Text.setBackgroundColor((getResources().getColor(R.color.blue_600)));
                    shift2 = true;
                    shift1 = false;
                    shift3 = false;
                }
                break;
            case R.id.shiftNo3:
                if (shift3) {
                    shift3Text.setBackgroundColor((getResources().getColor(R.color.blue_600)));
                    edtshiftname3.setEnabled(false);
                    shift3 = false;
                } else {
                    shift1Text.setBackgroundColor((getResources().getColor(R.color.blue_600)));
                    shift2Text.setBackgroundColor((getResources().getColor(R.color.blue_600)));
                    shift3Text.setBackgroundColor((getResources().getColor(R.color.green_500)));
                    shift3 = true;
                    edtshiftname3.setEnabled(true);
                    shift2 = false;
                    shift1 = false;
                }
                break;
            case R.id.actionText1:
                if (shift1)
                {
                    final Dialog dialog1 = new Dialog(ShiftManagement.this);
// Include dialog.xml file
                    dialog1.setContentView(R.layout.dialog_exit);
                    dialog1.show();

                    TextView heading1 = dialog1.findViewById(R.id.tv_quit_learning);
                    TextView description1 = dialog1.findViewById(R.id.tv_description);
                    Button btnNo1 = (Button) dialog1.findViewById(R.id.btn_no);
                    Button btnYes1 = (Button) dialog1.findViewById(R.id.btn_yes);
                    heading1.setText("Remove");
                    description1.setText("This action will completely remove the roster system and all shift staffs will be converted to office staff do you want to continue ?");
                    btnYes1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                           progressDialog.show();
                            (Api.getClient()).removeShift("remove", employer_id, "1", "").enqueue(new Callback<GetJsonResponse>() {
                                @Override
                                public void onResponse(Call<GetJsonResponse> call, Response<GetJsonResponse> response) {
                                    try {
                                        if (response.body().getErrorCode() == 101) {
                                            progressDialog.dismiss();
                                            Toast.makeText(ShiftManagement.this, "Removed Successfully", Toast.LENGTH_SHORT).show();
                                            preferenceManager.setPreference(PreferenceManager.KEY_SHIFT_COUNT,"0");
                                            finish();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(ShiftManagement.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        progressDialog.dismiss();
                                        final Dialog dialog1 = new Dialog(ShiftManagement.this);
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

                                }
                            });
                        }
                    });
                    btnNo1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                        }
                    });
                }
                else {
                    Toast.makeText(ShiftManagement.this, "Click on Shift-1 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.actionText2:
                if (shift2) {
                    if (action2.getText().toString().matches("Remove"))
                    {
                        progressDialog.show();
                        (Api.getClient()).getEmployeeList("fetch", employer_id, "2", "employee", "shift").enqueue(new Callback<GetJsonDataForEmployee>() {
                            @Override
                            public void onResponse(Call<GetJsonDataForEmployee> call, Response<GetJsonDataForEmployee> response) {
                                try {
                                    if (response.body().getErrorCode() == 101) {
                                        progressDialog.dismiss();
                                        affectedEmployees = response.body().getEmployeeData().size();
                                    } else {
                                        affectedEmployees = 0;
                                        progressDialog.dismiss();
                                    }
                                    final Dialog dialog = new Dialog(ShiftManagement.this);
// Include dialog.xml file
                                    dialog.setContentView(R.layout.dialog_text_spinner);
                                    dialog.show();

                                    TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                    TextView description = dialog.findViewById(R.id.tv_description);
                                    final Spinner selectShift = dialog.findViewById(R.id.shiftNoSpinner);
                                    Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                    Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                    btnYes.setText("Submit");
                                    btnNo.setText("Cancel");
                                    heading.setText("Select Shift");
                                    if (affectedEmployees == 0) {
                                        description.setText("Please Select Shift for the Employees. No Employee will be Affected by this change.");
                                    } else {
                                        if (affectedEmployees == 1) {
                                            description.setText("Please Select Shift for the Employees. Total " + affectedEmployees + " Employee will be Affected by this change.");
                                        } else {
                                            description.setText("Please Select Shift for the Employees. Total " + affectedEmployees + " Employees will be Affected by this change.");
                                        }
                                    }
                                    noOfShifts1.remove("Shift no. 2");
                                    preferenceManager.setPreference(PreferenceManager.KEY_SHIFT_COUNT,"1");
                                    shiftAdapter1 = new ArrayAdapter<String>(ShiftManagement.this, R.layout.spinner_item1, R.id.spinnerText1, noOfShifts1);
                                    selectShift.setAdapter(shiftAdapter1);
                                    btnYes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            progressDialog.show();
                                            (Api.getClient()).removeShift("remove", employer_id, shiftSpinner.getSelectedItem().toString(), String.valueOf(selectShift.getSelectedItemPosition() + 1)).enqueue(new Callback<GetJsonResponse>() {
                                                @Override
                                                public void onResponse(Call<GetJsonResponse> call, Response<GetJsonResponse> response) {
                                                    if (response.body().getErrorCode() == 101) {
                                                        shift2 = false;
                                                        shift2Text.setBackground(shape1);
                                                        Toast.makeText(ShiftManagement.this, "Removed Successfuly", Toast.LENGTH_SHORT).show();
                                                        action2.setText("Clear");
                                                        getShiftData();
                                                    } else {
                                                        Toast.makeText(ShiftManagement.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<GetJsonResponse> call, Throwable t) {

                                                }
                                            });
                                        }
                                    });

// if decline button is clicked, close the custom dialog
                                    btnNo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<GetJsonDataForEmployee> call, Throwable t) {

                            }
                        });

                    } else {
                        startTimeShift2.setText("00:00");
                        stopTimeShift2.setText("00:00");
                        preStartTimeShift2.setText("00:00");
                        postStopTimeShift2.setText("00:00");
                    }
                } else {
                    Toast.makeText(ShiftManagement.this, "Click on Shift-2 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.actionText3:
                if (shift3) {

                    if (action3.getText().toString().matches("Remove")) {
                        progressDialog.show();
                        (Api.getClient()).getEmployeeList("fetch", employer_id, "3", "employee", "shift").enqueue(new Callback<GetJsonDataForEmployee>() {
                            @Override
                            public void onResponse(Call<GetJsonDataForEmployee> call, Response<GetJsonDataForEmployee> response) {
                                try {
                                    if (response.body().getErrorCode() == 101) {
                                        progressDialog.dismiss();
                                        affectedEmployees = response.body().getEmployeeData().size();
                                    } else {
                                        affectedEmployees = 0;
                                        progressDialog.dismiss();
                                    }
                                    final Dialog dialog = new Dialog(ShiftManagement.this);
// Include dialog.xml file
                                    dialog.setContentView(R.layout.dialog_text_spinner);
                                    dialog.show();

                                    TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                    TextView description = dialog.findViewById(R.id.tv_description);
                                    final Spinner selectShift = dialog.findViewById(R.id.shiftNoSpinner);
                                    Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                    Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                    btnYes.setText("Submit");
                                    btnNo.setText("Cancel");
                                    heading.setText("Select Shift");
                                    if (affectedEmployees == 0) {
                                        description.setText("Please Select Shift for the Employees. No Employee will be Affected by this change.");
                                    } else {
                                        if (affectedEmployees == 1) {
                                            description.setText("Please Select Shift for the Employees. Total " + affectedEmployees + " Employee will be Affected by this change.");
                                        } else {
                                            description.setText("Please Select Shift for the Employees. Total " + affectedEmployees + " Employees will be Affected by this change.");
                                        }
                                    }
                                    noOfShifts1.remove("Shift no. 3");
                                    preferenceManager.setPreference(PreferenceManager.KEY_SHIFT_COUNT,"2");
                                    shiftAdapter1 = new ArrayAdapter<String>(ShiftManagement.this, R.layout.spinner_item1, R.id.spinnerText, noOfShifts1);
                                    selectShift.setAdapter(shiftAdapter1);
                                    btnYes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            progressDialog.show();
                                            (Api.getClient()).removeShift("remove", employer_id, shiftSpinner.getSelectedItem().toString(), String.valueOf(selectShift.getSelectedItemPosition() + 1)).enqueue(new Callback<GetJsonResponse>() {
                                                @Override
                                                public void onResponse(Call<GetJsonResponse> call, Response<GetJsonResponse> response) {
                                                    if (response.body().getErrorCode() == 101) {
                                                        shift3 = false;
                                                        shift3Text.setBackground(shape1);
                                                        action3.setText("Clear");
                                                        Toast.makeText(ShiftManagement.this, "Removed Successfuly", Toast.LENGTH_SHORT).show();
                                                        getShiftData();
                                                    } else {
                                                        Toast.makeText(ShiftManagement.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<GetJsonResponse> call, Throwable t) {

                                                }
                                            });
                                        }
                                    });

// if decline button is clicked, close the custom dialog
                                    btnNo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<GetJsonDataForEmployee> call, Throwable t) {

                            }
                        });

                    } else {
                        startTimeShift3.setText("00:00");
                        stopTimeShift3.setText("00:00");
                        preStartTimeShift3.setText("00:00");
                        postStopTimeShift3.setText("00:00");
                    }
                } else {
                    Toast.makeText(ShiftManagement.this, "Click on Shift-3 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.startTimeShift1:
                if (shift1) {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ShiftManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                                        if (preStartTimeShift1.getText().toString().matches("00:00")) {
                                            startTimeShift1Date = date;
                                            startTimeShift1.setText(timeFormat1.format(date));
                                        } else {
                                            if (date.before(preStartTimeShift1Date)) {
                                                final Dialog dialog = new Dialog(ShiftManagement.this);
// Include dialog.xml file
                                                dialog.setContentView(R.layout.dialog_exit);
                                                dialog.show();

                                                TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                                TextView description = dialog.findViewById(R.id.tv_description);
                                                Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                                Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                                heading.setText("Error");
                                                description.setText("Shift Start cannot be before Shift Pre Start");
                                                btnNo.setVisibility(View.GONE);
                                                btnYes.setText("Ok");
                                                btnYes.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                            } else {
                                                startTimeShift1Date = date;
                                                startTimeShift1.setText(timeFormat1.format(date));
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                } else {
                    Toast.makeText(ShiftManagement.this, "Click on Shift-1 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.startTimeShift2:
                if (shift2) {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ShiftManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);

                                        if (date.before(stopTimeShift1Date)) {
                                            final Dialog dialog = new Dialog(ShiftManagement.this);
// Include dialog.xml file
                                            dialog.setContentView(R.layout.dialog_exit);
                                            dialog.show();

                                            TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                            TextView description = dialog.findViewById(R.id.tv_description);
                                            Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                            Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                            heading.setText("Error");
                                            description.setText("Shift 2 cannot be before Shift 1");
                                            btnNo.setVisibility(View.GONE);
                                            btnYes.setText("Ok");
                                            btnYes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                        } else {
                                            if (date.before(preStartTimeShift2Date)) {
                                                final Dialog dialog = new Dialog(ShiftManagement.this);
// Include dialog.xml file
                                                dialog.setContentView(R.layout.dialog_exit);
                                                dialog.show();

                                                TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                                TextView description = dialog.findViewById(R.id.tv_description);
                                                Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                                Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                                heading.setText("Error");
                                                description.setText("Start Time cannot be before Pre Start Time");
                                                btnNo.setVisibility(View.GONE);
                                                btnYes.setText("Ok");
                                                btnYes.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                            } else {
                                                startTimeShift2Date = date;
                                                startTimeShift2.setText(timeFormat1.format(date));
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                } else {
                    Toast.makeText(ShiftManagement.this, "Click on Shift-2 to enable editing", Toast.LENGTH_SHORT).show();
                }
            case R.id.startTimeShift3:
                if (shift3) {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ShiftManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                                        if (date.before(stopTimeShift2Date) || date.equals(startTimeShift1Date)) {
                                            final Dialog dialog = new Dialog(ShiftManagement.this);
// Include dialog.xml file
                                            dialog.setContentView(R.layout.dialog_exit);
                                            dialog.show();

                                            TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                            TextView description = dialog.findViewById(R.id.tv_description);
                                            Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                            Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                            heading.setText("Error");
                                            description.setText("Shift 3 cannot start be before Shift 2 or after shift 1");
                                            btnNo.setVisibility(View.GONE);
                                            btnYes.setText("Ok");
                                            btnYes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                        } else {
                                            startTimeShift3Date = date;
                                            startTimeShift3.setText(timeFormat1.format(date));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                } else {
                    Toast.makeText(ShiftManagement.this, "Click on Shift-3 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.stopTimeShift1:
                if (shift1) {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ShiftManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                                        Log.e("Time", timeFormat1.format(startTimeShift1Date));
                                        if (date.before(startTimeShift1Date))
                                        {
                                            final Dialog dialog = new Dialog(ShiftManagement.this);
// Include dialog.xml file
                                            dialog.setContentView(R.layout.dialog_exit);
                                            dialog.show();

                                            TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                            TextView description = dialog.findViewById(R.id.tv_description);
                                            Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                            Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                            heading.setText("Error");
                                            description.setText("Shift Close cannot be before Shift Start");
                                            btnNo.setVisibility(View.GONE);
                                            btnYes.setText("Ok");
                                            btnYes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                        } else {
                                            stopTimeShift1Date = date;
                                            stopTimeShift1.setText(timeFormat1.format(date));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                } else {
                    Toast.makeText(this, "Click on Shift-1 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.preStartTimeShift1:
                if (shift1) {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ShiftManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {

                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                                        Log.e("StartTime", timeFormat1.format(startTimeShift1Date));
                                        if (startTimeShift1.getText().toString().matches("00:00")) {
                                            preStartTimeShift1Date = date;
                                            preStartTimeShift1.setText(timeFormat1.format(date));
                                        } else {
                                            if (date.after(startTimeShift1Date)) {
                                                final Dialog dialog = new Dialog(ShiftManagement.this);
// Include dialog.xml file
                                                dialog.setContentView(R.layout.dialog_exit);
                                                dialog.show();

                                                TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                                TextView description = dialog.findViewById(R.id.tv_description);
                                                Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                                Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                                heading.setText("Error");
                                                description.setText("Pre Start Time cannot be after Start Time");
                                                btnNo.setVisibility(View.GONE);
                                                btnYes.setText("Ok");
                                                btnYes.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                            } else {
                                                preStartTimeShift1Date = date;
                                                preStartTimeShift1.setText(timeFormat1.format(date));
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                } else {
                    Toast.makeText(this, "Click on Shift-1 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.postStopTimeShift1:
                if (shift1) {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ShiftManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {


                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                                        if (date.before(stopTimeShift1Date)) {
                                            final Dialog dialog = new Dialog(ShiftManagement.this);
// Include dialog.xml file
                                            dialog.setContentView(R.layout.dialog_exit);
                                            dialog.show();

                                            TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                            TextView description = dialog.findViewById(R.id.tv_description);
                                            Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                            Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                            heading.setText("Error");
                                            description.setText("Post Stop Time cannot be before Stop Time");
                                            btnNo.setVisibility(View.GONE);
                                            btnYes.setText("Ok");
                                            btnYes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                        } else {
                                            postStopTimeShift1Date = date;
                                            postStopTimeShift1.setText(timeFormat1.format(date));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                } else {
                    Toast.makeText(this, "Click on Shift-1 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.stopTimeShift2:
                if (shift2) {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ShiftManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                                        if (date.before(stopTimeShift1Date) && date.after(startTimeShift1Date) || date.before(stopTimeShift2Date)) {
                                            final Dialog dialog = new Dialog(ShiftManagement.this);
// Include dialog.xml file
                                            dialog.setContentView(R.layout.dialog_exit);
                                            dialog.show();

                                            TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                            TextView description = dialog.findViewById(R.id.tv_description);
                                            Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                            Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                            heading.setText("Error");
                                            description.setText("Stop Time cannot be before Start Time");
                                            btnNo.setVisibility(View.GONE);
                                            btnYes.setText("Ok");
                                            btnYes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                        } else {
                                            stopTimeShift2Date = date;
                                            stopTimeShift2.setText(timeFormat1.format(date));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                } else {
                    Toast.makeText(this, "Click on Shift-2 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.preStartTimeShift2:
                if (shift2) {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ShiftManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {

                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                                        if (startTimeShift2.getText().toString().matches("00:00")) {
                                            preStartTimeShift2Date = date;
                                            preStartTimeShift2.setText(timeFormat1.format(date));
                                        } else {
                                            if (date.after(startTimeShift2Date)) {
                                                final Dialog dialog = new Dialog(ShiftManagement.this);
// Include dialog.xml file
                                                dialog.setContentView(R.layout.dialog_exit);
                                                dialog.show();

                                                TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                                TextView description = dialog.findViewById(R.id.tv_description);
                                                Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                                Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                                heading.setText("Error");
                                                description.setText("Pre Start Time cannot be before Start Time");
                                                btnNo.setVisibility(View.GONE);
                                                btnYes.setText("Ok");
                                                btnYes.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                            } else {
                                                preStartTimeShift2Date = date;
                                                preStartTimeShift2.setText(timeFormat1.format(date));
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                } else {
                    Toast.makeText(this, "Click on Shift-2 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.postStopTimeShift2:
                if (shift2) {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ShiftManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                                        if (date.before(stopTimeShift2Date)) {
                                            final Dialog dialog = new Dialog(ShiftManagement.this);
// Include dialog.xml file
                                            dialog.setContentView(R.layout.dialog_exit);
                                            dialog.show();

                                            TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                            TextView description = dialog.findViewById(R.id.tv_description);
                                            Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                            Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                            heading.setText("Error");
                                            description.setText("Post Stop Time cannot be before Stop Time");
                                            btnNo.setVisibility(View.GONE);
                                            btnYes.setText("Ok");
                                            btnYes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                        } else {
                                            postStopTimeShift2Date = date;
                                            postStopTimeShift2.setText(timeFormat1.format(date));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                } else {
                    Toast.makeText(this, "Click on Shift-2 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.stopTimeShift3:
                if (shift3) {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ShiftManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {

                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                                        if (date.before(startTimeShift3Date) )/*|| stopTimeShift3Date.after(startTimeShift1Date))*/
                                            {
                                            final Dialog dialog = new Dialog(ShiftManagement.this);
// Include dialog.xml file
                                            dialog.setContentView(R.layout.dialog_exit);
                                            dialog.show();

                                            TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                            TextView description = dialog.findViewById(R.id.tv_description);
                                            Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                            Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                            heading.setText("Error");
                                            description.setText("Shift 3 Stop time cannot be before Start Time");
                                            btnNo.setVisibility(View.GONE);
                                            btnYes.setText("Ok");
                                            btnYes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                        } else {
                                            stopTimeShift3Date = date;
                                            stopTimeShift3.setText(timeFormat1.format(date));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                } else {
                    Toast.makeText(this, "Click on Shift-3 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.preStartTimeShift3:
                if (shift3)
                {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ShiftManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {


                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                                        if (startTimeShift3.getText().toString().matches("00:00")) {
                                            preStartTimeShift3Date = date;
                                            preStartTimeShift3.setText(timeFormat1.format(date));
                                        } else {
                                            if (date.after(startTimeShift3Date) )/*|| date.equals(startTimeShift1Date))*/ {
                                                final Dialog dialog = new Dialog(ShiftManagement.this);
// Include dialog.xml file
                                                dialog.setContentView(R.layout.dialog_exit);
                                                dialog.show();

                                                TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                                TextView description = dialog.findViewById(R.id.tv_description);
                                                Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                                Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                                heading.setText("Error");
                                                description.setText("Pre Start Time cannot be after Start Time");
                                                btnNo.setVisibility(View.GONE);
                                                btnYes.setText("Ok");
                                                btnYes.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                            } else {
                                                preStartTimeShift3Date = date;
                                                preStartTimeShift3.setText(timeFormat1.format(date));
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                } else {
                    Toast.makeText(this, "Click on Shift-3 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.postStopTimeShift3:
                if (shift3)
                {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ShiftManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {

                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                                        if (date.before(stopTimeShift3Date)) {
                                            final Dialog dialog = new Dialog(ShiftManagement.this);
// Include dialog.xml file
                                            dialog.setContentView(R.layout.dialog_exit);
                                            dialog.show();

                                            TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                            TextView description = dialog.findViewById(R.id.tv_description);
                                            Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                            Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                            heading.setText("Error");
                                            description.setText("Post Stop Time cannot be before Stop Time");
                                            btnNo.setVisibility(View.GONE);
                                            btnYes.setText("Ok");
                                            btnYes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                        } else {
                                            postStopTimeShift3Date = date;
                                            postStopTimeShift3.setText(timeFormat1.format(date));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                } else {
                    Toast.makeText(this, "Click on Shift-3 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.break1strattime:
                if (shift1)
                {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ShiftManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {

                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);

                                        if (date.before(startTimeShift1Date))
                                        {
                                            final Dialog dialog = new Dialog(ShiftManagement.this);
                                                // Include dialog.xml file
                                            dialog.setContentView(R.layout.dialog_exit);
                                            dialog.show();

                                            TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                            TextView description = dialog.findViewById(R.id.tv_description);
                                            Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                            Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                            heading.setText("Error");
                                            description.setText("Break Time should match with shift time");
                                            btnNo.setVisibility(View.GONE);
                                            btnYes.setText("Ok");
                                            btnYes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                        } else {
                                            breakstarttime1Date = date;
                                            breakstarttime1.setText(timeFormat1.format(date));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                } else {
                    Toast.makeText(this, "Click on Shift-1 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.break2strattime:
                if (shift2)
                {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ShiftManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {

                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                                        if (date.before(startTimeShift2Date))
                                        {
                                            final Dialog dialog = new Dialog(ShiftManagement.this);
                                            // Include dialog.xml file
                                            dialog.setContentView(R.layout.dialog_exit);
                                            dialog.show();

                                            TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                            TextView description = dialog.findViewById(R.id.tv_description);
                                            Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                            Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                            heading.setText("Error");
                                            description.setText("Break Time should match with shift time");
                                            btnNo.setVisibility(View.GONE);
                                            btnYes.setText("Ok");
                                            btnYes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                        } else {
                                            breakstarttime2Date = date;
                                            breakstarttime2.setText(timeFormat1.format(date));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                } else {
                    Toast.makeText(this, "Click on Shift-2 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.break3strattime:
                if (shift3)
                {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ShiftManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {

                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                                        if (date.before(startTimeShift3Date))
                                        {
                                            final Dialog dialog = new Dialog(ShiftManagement.this);
                                            // Include dialog.xml file
                                            dialog.setContentView(R.layout.dialog_exit);
                                            dialog.show();

                                            TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                            TextView description = dialog.findViewById(R.id.tv_description);
                                            Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                            Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                            heading.setText("Error");
                                            description.setText("Break Time should match with shift time");
                                            btnNo.setVisibility(View.GONE);
                                            btnYes.setText("Ok");
                                            btnYes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                        } else {
                                            breakstarttime3Date = date;
                                            breakstarttime3.setText(timeFormat1.format(date));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                } else {
                    Toast.makeText(this, "Click on Shift-3 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.break1stoptime:
                if (shift1)
                {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ShiftManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {

                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                                        if (date.after(stopTimeShift1Date))
                                        {
                                            final Dialog dialog = new Dialog(ShiftManagement.this);
                                            // Include dialog.xml file
                                            dialog.setContentView(R.layout.dialog_exit);
                                            dialog.show();

                                            TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                            TextView description = dialog.findViewById(R.id.tv_description);
                                            Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                            Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                            heading.setText("Error");
                                            description.setText("Break Time should match with shift time");
                                            btnNo.setVisibility(View.GONE);
                                            btnYes.setText("Ok");
                                            btnYes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                        } else if(date.before(breakstarttime1Date))
                                        {
                                            final Dialog dialog = new Dialog(ShiftManagement.this);
                                            // Include dialog.xml file
                                            dialog.setContentView(R.layout.dialog_exit);
                                            dialog.show();

                                            TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                            TextView description = dialog.findViewById(R.id.tv_description);
                                            Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                            Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                            heading.setText("Error");
                                            description.setText("Break stop time should not before start time");
                                            btnNo.setVisibility(View.GONE);
                                            btnYes.setText("Ok");
                                            btnYes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                        }
                                        else {
                                            breakstoptimeDate = date;
                                            breakstoptime.setText(timeFormat1.format(date));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                } else {
                    Toast.makeText(this, "Click on Shift-1 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.break2stoptime:
                if (shift2)
                {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ShiftManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {

                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                                        if (date.after(stopTimeShift2Date))
                                        {
                                            final Dialog dialog = new Dialog(ShiftManagement.this);
                                            // Include dialog.xml file
                                            dialog.setContentView(R.layout.dialog_exit);
                                            dialog.show();

                                            TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                            TextView description = dialog.findViewById(R.id.tv_description);
                                            Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                            Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                            heading.setText("Error");
                                            description.setText("Break Time should match with shift time");
                                            btnNo.setVisibility(View.GONE);
                                            btnYes.setText("Ok");
                                            btnYes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                        }
                                        else if(date.before(breakstarttime2Date))
                                        {
                                            final Dialog dialog = new Dialog(ShiftManagement.this);
                                            // Include dialog.xml file
                                            dialog.setContentView(R.layout.dialog_exit);
                                            dialog.show();

                                            TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                            TextView description = dialog.findViewById(R.id.tv_description);
                                            Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                            Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                            heading.setText("Error");
                                            description.setText("Break stop time should not before start time");
                                            btnNo.setVisibility(View.GONE);
                                            btnYes.setText("Ok");
                                            btnYes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                        }else {
                                            breakstoptime2Date = date;
                                            breakstoptime2.setText(timeFormat1.format(date));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                } else {
                    Toast.makeText(this, "Click on Shift-2 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.break3stoptime:
                if (shift2)
                {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ShiftManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {

                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                                        if (date.after(stopTimeShift3Date))
                                        {
                                            final Dialog dialog = new Dialog(ShiftManagement.this);
                                            // Include dialog.xml file
                                            dialog.setContentView(R.layout.dialog_exit);
                                            dialog.show();

                                            TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                            TextView description = dialog.findViewById(R.id.tv_description);
                                            Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                            Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                            heading.setText("Error");
                                            description.setText("Break Time should match with shift time");
                                            btnNo.setVisibility(View.GONE);
                                            btnYes.setText("Ok");
                                            btnYes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                        } else if(date.before(breakstarttime3Date))
                                        {
                                            final Dialog dialog = new Dialog(ShiftManagement.this);
                                            // Include dialog.xml file
                                            dialog.setContentView(R.layout.dialog_exit);
                                            dialog.show();

                                            TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                            TextView description = dialog.findViewById(R.id.tv_description);
                                            Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                            Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                            heading.setText("Error");
                                            description.setText("Break stop time should not before start time");
                                            btnNo.setVisibility(View.GONE);
                                            btnYes.setText("Ok");
                                            btnYes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                        }else {
                                            breakstoptime3Date = date;
                                            breakstoptime3.setText(timeFormat1.format(date));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                } else {
                    Toast.makeText(this, "Click on Shift-3 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnSave:
                final Dialog dialog = new Dialog(ShiftManagement.this);
// Include dialog.xml file
                dialog.setContentView(R.layout.dialog_exit);
                dialog.show();

                TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                TextView description = dialog.findViewById(R.id.tv_description);
                Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                heading.setText("Save");
                description.setText("Are you sure you want to update the shift details?");
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Log.e("SELCTED SHIFT", shiftSpinner.getSelectedItemPosition() + "");
                        if (shiftSpinner.getSelectedItemPosition() == 0)
                        {
                            if(edtshiftname1.getText().toString().isEmpty())
                            {
                                edtshiftname1.setError("can not be empty");

                            }
                            Toast.makeText(ShiftManagement.this, "Select No of Shifts", Toast.LENGTH_SHORT).show();
                        }
                        else if (shiftSpinner.getSelectedItemPosition() == 1)
                        {
                               if (startTimeShift1.getText().toString().matches("00:00")) {
                                    startTimeShift1.setError("Cannot be Empty");
                                }
                               else if (stopTimeShift1.getText().toString().matches("00:00")) {
                                    stopTimeShift1.setError("Cannot be Empty");
                                }
                               else if (preStartTimeShift1.getText().toString().matches("00:00")) {
                                    preStartTimeShift1.setError("Cannot be Empty");
                                }
                               else if (postStopTimeShift1.getText().toString().matches("00:00")) {
                                    postStopTimeShift1.setError("Cannot be Empty");
                                }
                               else if (breakstarttime1.getText().toString().matches("00:00")) {
                                    breakstarttime1.setError("Cannot be Empty");
                                }
                               else if (breakstoptime.getText().toString().matches("00:00")) {
                                    breakstoptime.setError("Cannot be Empty");
                                }

                            else   if (duration1.isEmpty()) {
                                    Toast.makeText(ShiftManagement.this, "Select break duration", Toast.LENGTH_SHORT).show();
                                }
                             else {
                               progressDialog.show();
                                setShift(1);
                            }
                        } else if (shiftSpinner.getSelectedItemPosition() == 2)
                        {
                            if(edtshiftname2.getText().toString().isEmpty())
                            {
                                edtshiftname2.setError("can not be empty");

                            }
                               if (startTimeShift1.getText().toString().matches("00:00"))
                                {
                                    startTimeShift1.setError("Cannot be Empty");
                                }
                               else if (stopTimeShift1.getText().toString().matches("00:00")) {
                                    stopTimeShift1.setError("Cannot be Empty");
                                }
                               else if (preStartTimeShift1.getText().toString().matches("00:00")) {
                                    preStartTimeShift1.setError("Cannot be Empty");
                                }
                               else if (postStopTimeShift1.getText().toString().matches("00:00")) {
                                    postStopTimeShift1.setError("Cannot be Empty");
                                }
                                else if (breakstarttime1.getText().toString().matches("00:00")) {
                                    breakstarttime1.setError("Cannot be Empty");
                                }
                                else if (breakstoptime.getText().toString().matches("00:00")) {
                                    breakstoptime.setError("Cannot be Empty");
                                }


                              else  if (startTimeShift2.getText().toString().matches("00:00")) {
                                    startTimeShift2.setError("Cannot be Empty");
                                }
                                else if (stopTimeShift2.getText().toString().matches("00:00")) {
                                    stopTimeShift2.setError("Cannot be Empty");
                                }
                               else if (preStartTimeShift2.getText().toString().matches("00:00")) {
                                    preStartTimeShift2.setError("Cannot be Empty");
                                }
                                else if (postStopTimeShift2.getText().toString().matches("00:00")) {
                                    postStopTimeShift2.setError("Cannot be Empty");
                                }
                                else if (breakstarttime2.getText().toString().matches("00:00")) {
                                    breakstarttime2.setError("Cannot be Empty");
                                }
                                else if (breakstoptime2.getText().toString().matches("00:00")) {
                                    breakstoptime2.setError("Cannot be Empty");
                                }

                              else  if (duration2.isEmpty()||duration1.isEmpty())
                              {
                                    Toast.makeText(ShiftManagement.this, "Select break duration", Toast.LENGTH_SHORT).show();
                                }
                            else {
                                progressDialog.dismiss();
                                setShift(2);
                            }
                        } else if (shiftSpinner.getSelectedItemPosition() == 3)
                        {
                            if(edtshiftname3.getText().toString().isEmpty())
                            {
                                edtshiftname3.setError("can not be empty");

                            }

                                if (startTimeShift1.getText().toString().matches("00:00")) {
                                    startTimeShift1.setError("Cannot be Empty");
                                }
                                else if (stopTimeShift1.getText().toString().matches("00:00")) {
                                    stopTimeShift1.setError("Cannot be Empty");
                                }
                                else if (preStartTimeShift1.getText().toString().matches("00:00")) {
                                    preStartTimeShift1.setError("Cannot be Empty");
                                }
                                else if (postStopTimeShift1.getText().toString().matches("00:00")) {
                                    postStopTimeShift1.setError("Cannot be Empty");
                                }
                               else if (startTimeShift2.getText().toString().matches("00:00")) {
                                    startTimeShift2.setError("Cannot be Empty");
                                }
                               else if (stopTimeShift2.getText().toString().matches("00:00")) {
                                    stopTimeShift2.setError("Cannot be Empty");
                                }
                                else if (preStartTimeShift2.getText().toString().matches("00:00")) {
                                    preStartTimeShift2.setError("Cannot be Empty");
                                }
                               else if (postStopTimeShift2.getText().toString().matches("00:00")) {
                                    postStopTimeShift2.setError("Cannot be Empty");
                                }
                               else if (startTimeShift3.getText().toString().matches("00:00")) {
                                    startTimeShift3.setError("Cannot be Empty");
                                }
                               else if (stopTimeShift3.getText().toString().matches("00:00")) {
                                    stopTimeShift3.setError("Cannot be Empty");
                                }
                               else if (preStartTimeShift3.getText().toString().matches("00:00")) {
                                    preStartTimeShift3.setError("Cannot be Empty");
                                }
                               else if (postStopTimeShift3.getText().toString().matches("00:00")) {
                                    postStopTimeShift3.setError("Cannot be Empty");
                                }
                               else if (breakstarttime1.getText().toString().matches("00:00")) {
                                    breakstarttime1.setError("Cannot be Empty");
                                }
                               else if (breakstoptime.getText().toString().matches("00:00")) {
                                    breakstoptime.setError("Cannot be Empty");
                                }

                               else if (duration1.isEmpty()||duration1.isEmpty()||duration3.isEmpty()) {
                                    Toast.makeText(ShiftManagement.this, "Select break duration", Toast.LENGTH_SHORT).show();
                                }
                               else if (breakstarttime2.getText().toString().matches("00:00")) {
                                    breakstarttime2.setError("Cannot be Empty");
                                }
                               else if (breakstoptime2.getText().toString().matches("00:00")) {
                                    breakstoptime2.setError("Cannot be Empty");
                                }
                               else if (breakstarttime3.getText().toString().matches("00:00")) {
                                    breakstarttime3.setError("Cannot be Empty");
                                }
                               else if (breakstoptime3.getText().toString().matches("00:00")) {
                                    breakstoptime3.setError("Cannot be Empty");
                                }
                               else {
                                    progressDialog.show();
                                    setShift(3);
                                }

                        }
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

                break;
        }
    }

    public void setShift(int count)
    {
        (Api.getClient()).setShiftData("update", employer_id, startTimeShift1.getText().toString(),
                stopTimeShift1.getText().toString(), preStartTimeShift1.getText().toString(), postStopTimeShift1.getText().toString(),
                startTimeShift2.getText().toString(), stopTimeShift2.getText().toString(), preStartTimeShift2.getText().toString(),
                postStopTimeShift2.getText().toString(), startTimeShift3.getText().toString(), stopTimeShift3.getText().toString(),
                preStartTimeShift3.getText().toString(), postStopTimeShift3.getText().toString(),
                shiftSpinner.getSelectedItem().toString(),edtshiftname1.getText().toString(),
                edtshiftname2.getText().toString(),
                edtshiftname3.getText().toString(),breakstarttime1.getText().toString(),breakstoptime.getText().toString(),breakstarttime2.getText().toString(),
                breakstoptime2.getText().toString(),breakstarttime3.getText().toString(),
                breakstoptime3.getText().toString(),duration1,
                duration2,duration3,send_notification,send_voice_alarm
        ).enqueue(new Callback<GetJsonResponse>() {
            @Override
            public void onResponse(Call<GetJsonResponse> call, Response<GetJsonResponse> response) {
                try
                {
                    if (response.body().getErrorCode() == 101)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(ShiftManagement.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        preferenceManager.setPreference(PreferenceManager.KEY_SHIFT_COUNT,count+"");
                     //   startActivity(new Intent(ShiftManagement.this,EmployerZone.class));
                        finish();
                    } else
                        {
                        progressDialog.dismiss();
                        Toast.makeText(ShiftManagement.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetJsonResponse> call, Throwable t) {

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        if (position == 1)
        {
            txtnoshifts.setVisibility(View.GONE);
            shift1CardView.setVisibility(View.VISIBLE);
            shift3CardView.setVisibility(View.GONE);
            shift2CardView.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);
        }
        if (position == 2)
        {
            txtnoshifts.setVisibility(View.GONE);
            shift1CardView.setVisibility(View.VISIBLE);
            shift2CardView.setVisibility(View.VISIBLE);
            shift3CardView.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);
        }
        if (position == 3) {
            txtnoshifts.setVisibility(View.GONE);
            shift1CardView.setVisibility(View.VISIBLE);
            shift3CardView.setVisibility(View.VISIBLE);
            shift2CardView.setVisibility(View.VISIBLE);
            submit.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    protected void exitByBackKey() {

        final Dialog dialog = new Dialog(ShiftManagement.this);
// Include dialog.xml file
        dialog.setContentView(R.layout.dialog_exit);
        dialog.show();

        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //Intent intent = new Intent(ShiftManagement.this, EmployerZone.class);
                //startActivity(intent);
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

    public boolean diffTime(Date date1,Date date2,int duration) {
        long min = 0;
        long difference ;
        try {

            difference = (date2.getTime() - date1.getTime()) / 1000;
            long hours = difference % (24 * 3600) / 3600; // Calculating Hours
            long minute = difference % 3600 / 60; // Calculating minutes if there is any minutes difference
            min = minute + (hours * 60); // This will be our final minutes. Multiplying by 60 as 1 hour contains 60 mins
            if(duration>min)
                return false;
            else
                return true;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }
}
