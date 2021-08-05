package com.miscos.staffhandler.shiftmanagement.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.miscos.staffhandler.shiftmanagement.Api;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonDataForEmployee;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonDataForSpecialTiming;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonResponse;
import com.miscos.staffhandler.shiftmanagement.models.SpecialTiming;
import com.fivemin.chief.nonetworklibrary.networkBroadcast.NoNet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/*Developed under Miscos
 * Developed by Nikhil
 * 15-09-2020
 * */
public class SpecialManagement extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog progressDialog;
    AutoCompleteTextView employeeName, employeeId;
    ArrayList<String> employeeNameList = new ArrayList<>();
    ArrayList<String> employeeIdList = new ArrayList<>();
    List<String> employeeIdList2 = new ArrayList<>();
    Button btnGo, btnSave;
    CardView specialTimingCardView,cardViewHolidayMarking,specialTimingCardview2,cardviewHolidayMarking2;
    Button specialText, holidayText,specialText2, holidayText2;
    TextView  textViewSun, textViewMon, textViewTue, textViewWed, textViewThu, textViewFri, textViewSat, startShift, stopShift, preStartShift, postStopShift, actionText, actiontextHoliday;
    Date startShiftDate, stopShiftDate, preStartShiftDate, postStopShiftDate,startShiftDate2, stopShiftDate2, preStartShiftDate2, postStopShiftDate2;
    int mHour, mMinute;
    TextView textViewSun2, textViewMon2, textViewTue2, textViewWed2, textViewThu2, textViewFri2, textViewSat2, startShift2, stopShift2, preStartShift2, postStopShift2, actionText2, actiontextHoliday2;
    PreferenceManager preferenceManager;
    SimpleDateFormat timeFormat, timeFormat1;
    GetJsonDataForEmployee getJsonDataForEmployee;
    GetJsonDataForSpecialTiming getJsonDataForSpecialTiming;
    SpecialTiming specialTiming;
    LayerDrawable shape, shape1, shape2;
    GradientDrawable gradientDrawable, gradientDrawable1, gradientDrawable2;
    String weekOf = "",weekoff2="",employer_id;
    List<String> weekOffs = new ArrayList<>();
    List<String> weekOffs2 = new ArrayList<>();
    boolean sun = false, mon = false, tue = false, wed = false, thu = false, fri = false, sat = false,sun2 = false, mon2 = false, tue2 = false, wed2 = false, thu2 = false, fri2 = false, sat2= false;
    ColorStateList oldColors;
    Handler handler;
    boolean specialTime = false, specialTime2 = false,markHoliday = false,markHoliday2=false;
    ImageView tvBack;
    TextView txtappliedFromdate,txtemployeeidhidden;
    // No Internet Dialog
    private NoNet mNoNet;
    private FragmentManager fm = null;
    RelativeLayout rl_timing,rl_holiday,rl_timing2,rl_holiday2;
    SpinnerDialog spinnerDialog1,spinnerDialog2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //  ((MainActivity) SpecialManagement.this).toolbar.setTitle("Admin Settings");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specialmanagement);

        fm = getSupportFragmentManager();
        mNoNet = new NoNet();
        mNoNet.initNoNet(this, fm);
        //Progress Bar
        progressDialog = new ProgressDialog(this);
        progressDialog = ProgressDialog.show(SpecialManagement.this, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.dismiss();

        preferenceManager =new PreferenceManager(SpecialManagement.this);
        employer_id = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID);
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat1 = new SimpleDateFormat("HH:mm");
        tvBack =  findViewById(R.id.imBack);
        handler = new Handler(Looper.getMainLooper());

        shape = (LayerDrawable) ContextCompat.getDrawable(SpecialManagement.this, R.drawable.border1);
        gradientDrawable = (GradientDrawable) shape.findDrawableByLayerId(R.id.shape);
        gradientDrawable.setColor(ContextCompat.getColor(SpecialManagement.this, R.color.red));

        shape2 = (LayerDrawable) ContextCompat.getDrawable(SpecialManagement.this, R.drawable.border1);
        gradientDrawable2 = (GradientDrawable) shape2.findDrawableByLayerId(R.id.shape);
        gradientDrawable2.setColor(ContextCompat.getColor(SpecialManagement.this, R.color.green_500));

        shape1 = (LayerDrawable) ContextCompat.getDrawable(SpecialManagement.this, R.drawable.border1);
        gradientDrawable1 = (GradientDrawable) shape1.findDrawableByLayerId(R.id.shape);
        gradientDrawable1.setColor(ContextCompat.getColor(SpecialManagement.this, R.color.transparent));

        employeeName = findViewById(R.id.employeeName);
        employeeId = findViewById(R.id.employeeId);
        btnSave = findViewById(R.id.btnSave);
        txtemployeeidhidden=findViewById(R.id.txtemployeehiddenid);
        textViewSun = findViewById(R.id.textViewSun);
        textViewMon = findViewById(R.id.textViewMon);
        textViewTue = findViewById(R.id.textViewTue);
        textViewWed = findViewById(R.id.textViewWed);
        textViewThu = findViewById(R.id.textViewThu);
        textViewFri = findViewById(R.id.textViewFri);
        textViewSat = findViewById(R.id.textViewSat);
        textViewSun2 = findViewById(R.id.textViewSun2);
        textViewMon2 = findViewById(R.id.textViewMon2);
        textViewTue2 = findViewById(R.id.textViewTue2);
        textViewWed2 = findViewById(R.id.textViewWed2);
        textViewThu2 = findViewById(R.id.textViewThu2);
        textViewFri2 = findViewById(R.id.textViewFri2);
        textViewSat2= findViewById(R.id.textViewSat2);
        rl_timing=findViewById(R.id.rl_timing);
        enableDisableView(rl_timing,false);
        rl_holiday=findViewById(R.id.rl_holiday);
        enableDisableView(rl_holiday,false);
        rl_timing2=findViewById(R.id.rl_timing2);
        enableDisableView(rl_timing2,false);
        rl_holiday2=findViewById(R.id.rl_holiday2);
        enableDisableView(rl_holiday2,false);
        specialText = findViewById(R.id.specialTimingText);
        holidayText = findViewById(R.id.holidayHeading);
        actionText = findViewById(R.id.actionText);

        specialText2 = findViewById(R.id.specialTimingText2);
        holidayText2 = findViewById(R.id.holidayHeading2);
        actionText2 = findViewById(R.id.actionText2);
        txtappliedFromdate=findViewById(R.id.applyFromDate);
        txtappliedFromdate.setEnabled(false);
        txtappliedFromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                long now = System.currentTimeMillis() - 1000;
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(SpecialManagement.this,
                        new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                            {
                                txtappliedFromdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, year, month, day);

                picker.setMessage("Choose Date of Application");
                picker.getDatePicker().setMinDate(now);
                picker.show();
            }
        });
        specialTimingCardView = findViewById(R.id.specialTimingCardView);
        specialTimingCardView.setEnabled(false);
        cardViewHolidayMarking = findViewById(R.id.card_view_holiday_marking);
        cardViewHolidayMarking.setEnabled(false);
        specialTimingCardview2 = findViewById(R.id.specialTimingCardView2);
        specialTimingCardview2.setEnabled(false);
        cardviewHolidayMarking2 = findViewById(R.id.card_view_holiday_marking2);
        cardviewHolidayMarking2.setEnabled(false);
        actiontextHoliday = findViewById(R.id.actionTextHoliday);
        actiontextHoliday2 = findViewById(R.id.actionTextHoliday2);
        btnGo = findViewById(R.id.btnGo);
        startShift = findViewById(R.id.startTime);
        stopShift = findViewById(R.id.stopTime);
        preStartShift = findViewById(R.id.preStartTime);
        postStopShift = findViewById(R.id.postStopTime);
        startShift2 = findViewById(R.id.startTime2);
        stopShift2 = findViewById(R.id.stopTime2);
        preStartShift2= findViewById(R.id.preStartTime2);
        postStopShift2 = findViewById(R.id.postStopTime2);
        oldColors = startShift.getTextColors();
      /*  ((MainActivity) SpecialManagement.this).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) SpecialManagement.this).getSupportActionBar().setDisplayShowHomeEnabled(true);*/
     /*   ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (SpecialManagement.this, android.R.layout.simple_list_item_1, employeeNameList);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
                (SpecialManagement.this, android.R.layout.simple_list_item_1, employeeIdList);
        employeeName.setAdapter(adapter);
        employeeName.setThreshold(0);
        employeeId.setAdapter(adapter1);
        employeeId.setThreshold(0);
        employeeName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = employeeNameList.indexOf(employeeName.getText().toString());
                employeeId.setText(employeeIdList.get(pos));
                txtemployeeidhidden.setText(employeeIdList2.get(pos));
            }
        });
        employeeId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos1 = employeeIdList.indexOf(employeeId.getText().toString());
                employeeName.setText(employeeNameList.get(pos1));
                txtemployeeidhidden.setText(employeeIdList2.get(pos1));
            }
        });*/
        spinnerDialog1=new SpinnerDialog(SpecialManagement.this,employeeNameList,"Select or Search Employee",R.style.DialogAnimations_SmileWindow,"close");// With 	Animation

        spinnerDialog1.setCancellable(true); // for cancellable
        spinnerDialog1.setShowKeyboard(false);// for open keyboard by default


        employeeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(employeeNameList.size()==0)
                {
                    Toast.makeText(SpecialManagement.this, "No employee found", Toast.LENGTH_SHORT).show();
                    return;
                }
                spinnerDialog1=new SpinnerDialog(SpecialManagement.this,employeeNameList,"Select or Search Employee",R.style.DialogAnimations_SmileWindow,"close");// With 	Animation

                spinnerDialog1.setCancellable(true); // for cancellable
                spinnerDialog1.setShowKeyboard(false);// for open keyboard by default
                spinnerDialog1.showSpinerDialog();
                spinnerDialog1.bindOnSpinerListener(new OnSpinerItemClick()
                {
                    @Override
                    public void onClick(String item, int position)
                    {
                        //Toast.makeText(UserRegistrationActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                        int pos = employeeNameList.indexOf(item);
                        if(pos!=-1)
                        {
                            employeeId.setText(employeeIdList.get(pos));
                            txtemployeeidhidden.setText(employeeIdList2.get(pos));
                            employeeName.setText(item);

                        }

                    }

                });
            }
        });


        spinnerDialog2=new SpinnerDialog(SpecialManagement.this,employeeIdList,"Select or Search Employee",R.style.DialogAnimations_SmileWindow,"close");// With 	Animation

        spinnerDialog2.setCancellable(true); // for cancellable
        spinnerDialog2.setShowKeyboard(false);// for open keyboard by default
        employeeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(employeeIdList.size()==0)
                {
                    Toast.makeText(SpecialManagement.this, "No employee found", Toast.LENGTH_SHORT).show();
                    return;
                }
                spinnerDialog2=new SpinnerDialog(SpecialManagement.this,employeeIdList,"Select or Search Employee",R.style.DialogAnimations_SmileWindow,"close");// With 	Animation

                spinnerDialog2.setCancellable(true); // for cancellable
                spinnerDialog2.setShowKeyboard(false);// for open keyboard by default
                spinnerDialog2.showSpinerDialog();
                spinnerDialog2.bindOnSpinerListener(new OnSpinerItemClick()
                {
                    @Override
                    public void onClick(String item, int position)
                    {
                        int pos1 = employeeIdList.indexOf(item);
                        if(pos1!=-1)
                        {
                            employeeName.setText(employeeNameList.get(pos1));
                            txtemployeeidhidden.setText(employeeIdList2.get(pos1));
                            employeeId.setText(item);
                        }

                    }

                });

            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                (Api.getClient()).getEmployeeList("fetch", employer_id, "0", "", "office").enqueue(new Callback<GetJsonDataForEmployee>() {
                    @Override
                    public void onResponse(Call<GetJsonDataForEmployee> call, Response<GetJsonDataForEmployee> response) {
                        try {
                            getJsonDataForEmployee = response.body();
                            if (getJsonDataForEmployee.getErrorCode() == 101)
                            {
                                for (int i = 0; i < getJsonDataForEmployee.getEmployeeData().size(); i++) {
                                    employeeNameList.add(getJsonDataForEmployee.getEmployeeData().get(i).getName());
                                    employeeIdList2.add(getJsonDataForEmployee.getEmployeeData().get(i).getEmployeeId());
                                    employeeIdList.add(getJsonDataForEmployee.getEmployeeData().get(i).getEmployeeNo());
                                }
                               progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            final Dialog dialog1 = new Dialog(SpecialManagement.this);
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
                    public void onFailure(Call<GetJsonDataForEmployee> call, Throwable t) {

                    }
                });

            }
        }, 1000);
        btnGo.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        startShift.setOnClickListener(this);
        stopShift.setOnClickListener(this);
        preStartShift.setOnClickListener(this);
        textViewFri.setOnClickListener(this);
        textViewMon.setOnClickListener(this);
        textViewSat.setOnClickListener(this);
        textViewSun.setOnClickListener(this);
        textViewThu.setOnClickListener(this);
        textViewTue.setOnClickListener(this);
        textViewWed.setOnClickListener(this);
        postStopShift.setOnClickListener(this);
        startShift2.setOnClickListener(this);
        stopShift2.setOnClickListener(this);
        preStartShift2.setOnClickListener(this);
        textViewFri2.setOnClickListener(this);
        textViewMon2.setOnClickListener(this);
        textViewSat2.setOnClickListener(this);
        textViewSun2.setOnClickListener(this);
        textViewThu2.setOnClickListener(this);
        textViewTue2.setOnClickListener(this);
        textViewWed2.setOnClickListener(this);
        postStopShift2.setOnClickListener(this);
        specialText.setOnClickListener(this);
        holidayText.setOnClickListener(this);
        actionText.setOnClickListener(this);
        actiontextHoliday.setOnClickListener(this);
        specialText2.setOnClickListener(this);
        holidayText2.setOnClickListener(this);
        //Back Button
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(SpecialManagement.this,EmployerZone.class));
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
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.actionText:
                if (specialTime)
                {
                    if (actionText.getText().toString().matches("Remove")) {
                        final Dialog dialog = new Dialog(SpecialManagement.this);
// Include dialog.xml file
                        dialog.setContentView(R.layout.dialog_exit);
                        dialog.show();

                        TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                        TextView description = dialog.findViewById(R.id.tv_description);
                        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                        heading.setText("Remove");
                        description.setText("Are You Sure ?");
                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                final Dialog dialog = new Dialog(SpecialManagement.this);
// Include dialog.xml file
                                dialog.setContentView(R.layout.dialog_exit);
                                dialog.show();
                                TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                TextView description = dialog.findViewById(R.id.tv_description);
                                Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                heading.setText("Confirm");
                                description.setText("Do you want to assign this employee as a full time employee ?");
                                btnNo.setText("Remove");
                                btnYes.setText("Assign");
                                btnYes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        progressDialog.show();
                                        (Api.getClient()).removeSpecialTimingData("remove", employer_id, txtemployeeidhidden.getText().toString(), "full_time").enqueue(new Callback<GetJsonResponse>() {
                                            @Override
                                            public void onResponse(Call<GetJsonResponse> call, Response<GetJsonResponse> response) {
                                                try {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(SpecialManagement.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    progressDialog.dismiss();
                                                    final Dialog dialog1 = new Dialog(SpecialManagement.this);
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

// if decline button is clicked, close the custom dialog
                                btnNo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Close dialog
                                        dialog.dismiss();
                                        progressDialog.show();
                                        (Api.getClient()).removeSpecialTimingData("remove", employer_id, txtemployeeidhidden.getText().toString(), "remove").enqueue(new Callback<GetJsonResponse>() {
                                            @Override
                                            public void onResponse(Call<GetJsonResponse> call, Response<GetJsonResponse> response) {
                                                try {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(SpecialManagement.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    progressDialog.dismiss();
                                                    final Dialog dialog1 = new Dialog(SpecialManagement.this);
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

                    } else {
                        startShift.setText("00:00");
                        stopShift.setText("00:00");
                        preStartShift.setText("00:00");
                        postStopShift.setText("00:00");
                    }
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Special Timing to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.actionTextHoliday:
                sun = false;
                mon = false;
                tue = false;
                wed = false;
                thu = false;
                fri = false;
                ;
                sat = false;
                textViewSun.setBackground(shape1);
                textViewMon.setBackground(shape1);
                textViewTue.setBackground(shape1);
                textViewWed.setBackground(shape1);
                textViewThu.setBackground(shape1);
                textViewFri.setBackground(shape1);
                textViewSat.setBackground(shape1);
                weekOffs = new ArrayList<>();
                weekOf = "";
                break;
            case R.id.specialTimingText:
                if (specialTime) {
                    specialTime = false;
                    specialText.setBackgroundColor((getResources().getColor(R.color.blue_600)));
                } else {
                    specialTime = true;
                    markHoliday = false;
                    specialText.setBackgroundColor((getResources().getColor(R.color.green_500)));
                    holidayText.setBackgroundColor((getResources().getColor(R.color.blue_600)));
                }
                break;
            case R.id.holidayHeading:
                if (markHoliday)
                {
                    markHoliday = false;
                    holidayText.setBackgroundColor((getResources().getColor(R.color.blue_600)));
                } else {
                    markHoliday = true;
                    specialTime = false;
                    holidayText.setBackgroundColor((getResources().getColor(R.color.green_500)));
                    specialText.setBackgroundColor((getResources().getColor(R.color.blue_600)));
                }
                break;
            case R.id.actionText2:
                if (specialTime2)
                {
                    if (actionText2.getText().toString().matches("Remove"))
                    {
                        final Dialog dialog = new Dialog(SpecialManagement.this);
// Include dialog.xml file
                        dialog.setContentView(R.layout.dialog_exit);
                        dialog.show();

                        TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                        TextView description = dialog.findViewById(R.id.tv_description);
                        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                        heading.setText("Remove");
                        description.setText("Are You Sure ?");
                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                final Dialog dialog = new Dialog(SpecialManagement.this);
// Include dialog.xml file
                                dialog.setContentView(R.layout.dialog_exit);
                                dialog.show();
                                TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                                TextView description = dialog.findViewById(R.id.tv_description);
                                Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                                Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                                heading.setText("Confirm");
                                description.setText("Do you want to assign this employee as a full time employee ?");
                                btnNo.setText("Remove");
                                btnYes.setText("Assign");
                                btnYes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        progressDialog.show();
                                        (Api.getClient()).removeSpecialTimingData("remove", employer_id, txtemployeeidhidden.getText().toString(), "full_time").enqueue(new Callback<GetJsonResponse>() {
                                            @Override
                                            public void onResponse(Call<GetJsonResponse> call, Response<GetJsonResponse> response) {
                                                try {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(SpecialManagement.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    progressDialog.dismiss();
                                                    final Dialog dialog1 = new Dialog(SpecialManagement.this);
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

// if decline button is clicked, close the custom dialog
                                btnNo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Close dialog
                                        dialog.dismiss();
                                        progressDialog.show();
                                        (Api.getClient()).removeSpecialTimingData("remove", employer_id, txtemployeeidhidden.getText().toString(), "remove").enqueue(new Callback<GetJsonResponse>() {
                                            @Override
                                            public void onResponse(Call<GetJsonResponse> call, Response<GetJsonResponse> response) {
                                                try {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(SpecialManagement.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    progressDialog.dismiss();
                                                    final Dialog dialog1 = new Dialog(SpecialManagement.this);
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

                    } else {
                        startShift2.setText("00:00");
                        stopShift2.setText("00:00");
                        preStartShift2.setText("00:00");
                        postStopShift2.setText("00:00");
                    }
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Special Timing 2 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.actionTextHoliday2:
                sun2 = false;
                mon2 = false;
                tue2 = false;
                wed2 = false;
                thu2 = false;
                fri2 = false;
                ;
                sat2 = false;
                textViewSun2.setBackground(shape1);
                textViewMon2.setBackground(shape1);
                textViewTue2.setBackground(shape1);
                textViewWed2.setBackground(shape1);
                textViewThu2.setBackground(shape1);
                textViewFri2.setBackground(shape1);
                textViewSat2.setBackground(shape1);
                weekOffs2 = new ArrayList<>();
                weekoff2 = "";
                break;
            case R.id.specialTimingText2:
                if (specialTime2)
                {
                    specialTime2 = false;
                    specialText2.setBackgroundColor((getResources().getColor(R.color.blue_600)));
                } else {
                    specialTime2 = true;
                    markHoliday2 = false;
                    specialText2.setBackgroundColor((getResources().getColor(R.color.green_500)));
                    holidayText2.setBackgroundColor((getResources().getColor(R.color.blue_600)));
                }
                break;

            case R.id.holidayHeading2:
                if (markHoliday2)
                {
                    markHoliday2= false;
                    holidayText2.setBackgroundColor((getResources().getColor(R.color.blue_600)));
                } else {
                    markHoliday2 = true;
                    specialTime2 = false;
                    holidayText2.setBackgroundColor((getResources().getColor(R.color.green_500)));
                    specialText2.setBackgroundColor((getResources().getColor(R.color.blue_600)));
                }
                break;
            case R.id.btnGo:
                if (employeeId.getText().toString().matches(""))
                {
                    Toast.makeText(SpecialManagement.this, "Select Employee", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!employeeIdList.contains(employeeId.getText().toString())||!employeeNameList.contains(employeeName.getText().toString()))
                {
                    Toast.makeText(SpecialManagement.this, "Invalid Employee", Toast.LENGTH_SHORT).show();
                    return;
                }

                else
                    {
                        enableDisableView(rl_timing,true);
                        enableDisableView(rl_holiday,true);
                        enableDisableView(rl_timing2,true);
                        enableDisableView(rl_holiday2,true);
                    progressDialog.show();
                    (Api.getClient()).getSpecialTimingData("fetch", employer_id, txtemployeeidhidden.getText().toString().trim()).enqueue(new Callback<GetJsonDataForSpecialTiming>()
                    {
                        @Override
                        public void onResponse(Call<GetJsonDataForSpecialTiming> call, Response<GetJsonDataForSpecialTiming> response)
                        {
                            try
                            {
                                getJsonDataForSpecialTiming = response.body();
                                if (getJsonDataForSpecialTiming.getErrorCode() == 101)
                                {
                                    progressDialog.dismiss();
                                    specialTiming = getJsonDataForSpecialTiming.getShiftData();
                                    specialTimingCardView.setVisibility(View.VISIBLE);
                                    specialTimingCardView.setEnabled(true);
                                    txtappliedFromdate.setEnabled(true);
                                    cardViewHolidayMarking.setVisibility(View.VISIBLE);
                                    cardViewHolidayMarking.setEnabled(true);
                                    specialTimingCardview2.setVisibility(View.VISIBLE);
                                    specialTimingCardview2.setEnabled(true);
                                    cardviewHolidayMarking2.setVisibility(View.VISIBLE);
                                    cardviewHolidayMarking2.setEnabled(true);
                                    btnSave.setVisibility(View.VISIBLE);
                                    try {
                                        txtappliedFromdate.setText(getJsonDataForSpecialTiming.getShiftData().getApplicable_from());
                                        if(!specialTiming.getInTiming().isEmpty())
                                        startShiftDate = timeFormat.parse(specialTiming.getInTiming());
                                        if(!specialTiming.getOutTiming().isEmpty())
                                        stopShiftDate = timeFormat.parse(specialTiming.getOutTiming());
                                        if(!specialTiming.getPreStartTiming().isEmpty())
                                        preStartShiftDate = timeFormat.parse(specialTiming.getPreStartTiming());
                                        if(!specialTiming.getPostCloseTiming().isEmpty())
                                        postStopShiftDate = timeFormat.parse(specialTiming.getPostCloseTiming());
                                        if(!specialTiming.getSpecial_in_time_2().isEmpty())
                                        startShiftDate2 = timeFormat.parse(specialTiming.getSpecial_in_time_2());
                                        if(!specialTiming.getSpecial_out_time_2().isEmpty()) {
                                            stopShiftDate2 = timeFormat.parse(specialTiming.getSpecial_out_time_2());
                                            stopShift2.setText(timeFormat1.format(stopShiftDate2));
                                        }
                                        if(!specialTiming.getSpecial_pre_in_time_2().isEmpty()) {
                                            preStartShiftDate2 = timeFormat.parse(specialTiming.getSpecial_pre_in_time_2());
                                            preStartShift2.setText(timeFormat1.format(preStartShiftDate2));
                                        }
                                        if(!specialTiming.getSpecial_out_time_2().isEmpty()) {
                                            postStopShiftDate2 = timeFormat.parse(specialTiming.getSpecial_post_close_time_2());
                                            postStopShift2.setText(timeFormat1.format(postStopShiftDate2));
                                        }
                                        startShift.setText(timeFormat1.format(startShiftDate));
                                        stopShift.setText(timeFormat1.format(stopShiftDate));
                                        preStartShift.setText(timeFormat1.format(preStartShiftDate));
                                        postStopShift.setText(timeFormat1.format(postStopShiftDate));

                                        startShift2.setText(timeFormat1.format(startShiftDate2));



                                        if (!startShift.getText().toString().matches("00:00"))
                                        {
                                            actionText.setText("Remove");
                                        }
                                        if (specialTiming.getWeekOff().contains("monday"))
                                        {
                                            textViewMon.setBackground(shape);
                                            textViewMon.setTextColor(Color.BLACK);
                                            weekOffs.add("monday");
                                            mon = true;
                                        }
                                        if (specialTiming.getWeekOff().contains("tuesday"))
                                        {
                                            textViewTue.setBackground(shape);
                                            textViewTue.setTextColor(Color.BLACK);
                                            weekOffs.add("tuesday");
                                            tue = true;
                                        }
                                        if (specialTiming.getWeekOff().contains("wednesday"))
                                        {
                                            textViewWed.setBackground(shape);
                                            textViewWed.setTextColor(Color.BLACK);
                                            weekOffs.add("wednesday");
                                            wed = true;
                                        }
                                        if (specialTiming.getWeekOff().contains("thursday"))
                                        {
                                            textViewThu.setBackground(shape);
                                            textViewThu.setTextColor(Color.BLACK);
                                            weekOffs.add("thursday");
                                            thu = true;
                                        }
                                        if (specialTiming.getWeekOff().contains("friday"))
                                        {
                                            textViewFri.setBackground(shape);
                                            textViewFri.setTextColor(Color.BLACK);
                                            weekOffs.add("friday");
                                            fri = true;
                                        }
                                        if (specialTiming.getWeekOff().contains("saturday"))
                                        {
                                            textViewSat.setBackground(shape);
                                            textViewSat.setTextColor(Color.BLACK);
                                            weekOffs.add("saturday");
                                            sat = true;
                                        }
                                        if (specialTiming.getWeekOff().contains("sunday"))
                                        {
                                            textViewSun.setBackground(shape);
                                            textViewSun.setTextColor(Color.BLACK);
                                            weekOffs.add("sunday");
                                            sun = true;
                                        }
                                        //
                                        if (specialTiming.getSpecial_holiday_2().contains("monday"))
                                        {
                                            textViewMon2.setBackground(shape);
                                            textViewMon2.setTextColor(Color.BLACK);
                                            weekOffs2.add("monday");
                                            mon2 = true;
                                        }
                                        if (specialTiming.getSpecial_holiday_2().contains("tuesday"))
                                        {
                                            textViewTue2.setBackground(shape);
                                            textViewTue2.setTextColor(Color.BLACK);
                                            weekOffs2.add("tuesday");
                                            tue2 = true;
                                        }
                                        if (specialTiming.getSpecial_holiday_2().contains("wednesday"))
                                        {
                                            textViewWed2.setBackground(shape);
                                            textViewWed2.setTextColor(Color.BLACK);
                                            weekOffs2.add("wednesday");
                                            wed2 = true;
                                        }
                                        if (specialTiming.getSpecial_holiday_2().contains("thursday"))
                                        {
                                            textViewThu2.setBackground(shape);
                                            textViewThu2.setTextColor(Color.BLACK);
                                            weekOffs2.add("thursday");
                                            thu2 = true;
                                        }
                                        if (specialTiming.getSpecial_holiday_2().contains("friday"))
                                        {
                                            textViewFri2.setBackground(shape);
                                            textViewFri2.setTextColor(Color.BLACK);
                                            weekOffs2.add("friday");
                                            fri2 = true;
                                        }
                                        if (specialTiming.getSpecial_holiday_2().contains("saturday"))
                                        {
                                            textViewSat2.setBackground(shape);
                                            textViewSat2.setTextColor(Color.BLACK);
                                            weekOffs2.add("saturday");
                                            sat2 = true;
                                        }
                                        if (specialTiming.getSpecial_holiday_2().contains("sunday"))
                                        {
                                            textViewSun2.setBackground(shape);
                                            textViewSun2.setTextColor(Color.BLACK);
                                            weekOffs2.add("sunday");
                                            sun2 = true;
                                        }
                                        //
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else
                                    {
                                    startShiftDate = timeFormat.parse("00:00:00");
                                    stopShiftDate = timeFormat.parse("00:00:00");
                                    preStartShiftDate = timeFormat.parse("00:00:00");
                                    postStopShiftDate = timeFormat.parse("00:00:00");
                                        startShiftDate2 = timeFormat.parse("00:00:00");
                                        stopShiftDate2 = timeFormat.parse("00:00:00");
                                        preStartShiftDate2 = timeFormat.parse("00:00:00");
                                        postStopShiftDate2= timeFormat.parse("00:00:00");
                                    txtappliedFromdate.setText("");
                                    progressDialog.dismiss();
                                    specialTimingCardView.setVisibility(View.VISIBLE);
                                    specialTimingCardView.setEnabled(true);
                                    cardViewHolidayMarking.setVisibility(View.VISIBLE);
                                    cardViewHolidayMarking.setEnabled(true);
                                        specialTimingCardview2.setVisibility(View.VISIBLE);
                                        specialTimingCardview2.setEnabled(true);
                                        cardviewHolidayMarking2.setVisibility(View.VISIBLE);
                                        cardviewHolidayMarking2.setEnabled(true);
                                    btnSave.setVisibility(View.VISIBLE);
                                    reset();
                                    txtappliedFromdate.setEnabled(true);
                                    final PrettyDialog prettyDialog = new PrettyDialog(SpecialManagement.this);
                                    prettyDialog
                                            .setTitle("Special Management")
                                            .setMessage("No special management available for "+employeeName.getText().toString()+
                                                    ".From here you can add special timing & holiday marking for "+employeeName.getText().toString())
                                            .setIcon(R.drawable.ic_time)
                                            .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                                @Override
                                                public void onClick() {
                                                    prettyDialog.dismiss();
                                                }
                                            })
                                            .show();
                                }
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                final Dialog dialog1 = new Dialog(SpecialManagement.this);
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
                        public void onFailure(Call<GetJsonDataForSpecialTiming> call, Throwable t) {

                        }
                    });
                }
                break;
            case R.id.startTime:
                if (specialTime)
                {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(SpecialManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                                        if (preStartShift.getText().toString().matches("00:00")) {
                                            startShiftDate = date;
                                            startShift.setText(timeFormat1.format(date));
                                        } else {
                                            if (date.before(preStartShiftDate)) {
                                                Toast.makeText(SpecialManagement.this, "Shift Start cannot be before Shift Pre Start", Toast.LENGTH_SHORT).show();
                                            } else {
                                                startShiftDate = date;
                                                startShift.setText(timeFormat1.format(date));
                                                Log.e("Timing1",timeFormat1.format(date)+" "+timeFormat1.format(startShiftDate));
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Special Timing to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.stopTime:
                if (specialTime) {
                    final Calendar c1 = Calendar.getInstance();
                    mHour = c1.get(Calendar.HOUR_OF_DAY);
                    mMinute = c1.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog1 = new TimePickerDialog(SpecialManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                                        if (date.before(startShiftDate)) {
                                            Toast.makeText(SpecialManagement.this, "Stop Time cannot be before Start Time", Toast.LENGTH_SHORT).show();
                                        } else {
                                            stopShiftDate = date;
                                            stopShift.setText(timeFormat1.format(date));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog1.show();
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Special Timing to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.preStartTime:
                if (specialTime) {
                    final Calendar c2 = Calendar.getInstance();
                    mHour = c2.get(Calendar.HOUR_OF_DAY);
                    mMinute = c2.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog2 = new TimePickerDialog(SpecialManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {

                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);

                                        if (date.after(startShiftDate)) {
                                            Toast.makeText(SpecialManagement.this, "Pre Start Time cannot be after Start Time", Toast.LENGTH_SHORT).show();
                                        } else {
                                            preStartShiftDate = date;
                                            preStartShift.setText(timeFormat1.format(date));
                                            Log.e("Timing",timeFormat1.format(preStartShiftDate)+" "+timeFormat1.format(startShiftDate));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog2.show();
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Special Timing to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.postStopTime:
                if (specialTime) {
                    final Calendar c3 = Calendar.getInstance();
                    mHour = c3.get(Calendar.HOUR_OF_DAY);
                    mMinute = c3.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog3 = new TimePickerDialog(SpecialManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {

                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                                        if (date.before(stopShiftDate)) {
                                            Toast.makeText(SpecialManagement.this, "Stop Time cannot be before Start Time", Toast.LENGTH_SHORT).show();
                                        } else {
                                            postStopShiftDate = date;
                                            postStopShift.setText(timeFormat1.format(date));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog3.show();
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Special Timing to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.startTime2:
                if (specialTime2)
                {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(SpecialManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                                        if (preStartShift2.getText().toString().matches("00:00")) {
                                            startShiftDate2 = date;
                                            startShift2.setText(timeFormat1.format(date));
                                        } else {
                                            if (date.before(preStartShiftDate2)) {
                                                Toast.makeText(SpecialManagement.this, "Shift Start cannot be before Shift Pre Start", Toast.LENGTH_SHORT).show();
                                            } else {
                                                startShiftDate2 = date;
                                                startShift2.setText(timeFormat1.format(date));
                                                Log.e("Timing1",timeFormat1.format(date)+" "+timeFormat1.format(startShiftDate));
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Special Timing 2 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.stopTime2:
                if (specialTime2)
                {
                    final Calendar c1 = Calendar.getInstance();
                    mHour = c1.get(Calendar.HOUR_OF_DAY);
                    mMinute = c1.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog1 = new TimePickerDialog(SpecialManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                                        if (date.before(startShiftDate2)) {
                                            Toast.makeText(SpecialManagement.this, "Stop Time cannot be before Start Time", Toast.LENGTH_SHORT).show();
                                        } else {
                                            stopShiftDate2 = date;
                                            stopShift2.setText(timeFormat1.format(date));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog1.show();
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Special Timing 2 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.preStartTime2:
                if (specialTime2) {
                    final Calendar c2 = Calendar.getInstance();
                    mHour = c2.get(Calendar.HOUR_OF_DAY);
                    mMinute = c2.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog2 = new TimePickerDialog(SpecialManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {

                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);

                                        if (date.after(startShiftDate2)) {
                                            Toast.makeText(SpecialManagement.this, "Pre Start Time cannot be after Start Time", Toast.LENGTH_SHORT).show();
                                        } else {
                                            preStartShiftDate2 = date;
                                            preStartShift2.setText(timeFormat1.format(date));
                                            Log.e("Timing",timeFormat1.format(preStartShiftDate2)+" "+timeFormat1.format(startShiftDate));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog2.show();
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Special Timing 2 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.postStopTime2:
                if (specialTime2) {
                    final Calendar c3 = Calendar.getInstance();
                    mHour = c3.get(Calendar.HOUR_OF_DAY);
                    mMinute = c3.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog3 = new TimePickerDialog(SpecialManagement.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {

                                    try {
                                        Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                                        if (date.before(stopShiftDate2)) {
                                            Toast.makeText(SpecialManagement.this, "Stop Time cannot be before Start Time", Toast.LENGTH_SHORT).show();
                                        } else {
                                            postStopShiftDate2 = date;
                                            postStopShift2.setText(timeFormat1.format(date));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog3.show();
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Special Timing 2 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.textViewSun:
                if (markHoliday) {
                    if (sun) {
                        sun = false;
                        weekOffs.remove("Sunday");
                        textViewSun.setBackground(shape1);
                        textViewSun.setTextColor(oldColors);
                    } else {
                        weekOffs.add("Sunday");
                        sun = true;
                        textViewSun.setBackground(shape);
                        textViewSun.setTextColor(Color.BLACK);
                    }
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Holiday Management to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.textViewMon:
                if (markHoliday) {
                    if (mon) {
                        mon = false;
                        weekOffs.remove("Monday");
                        textViewMon.setBackground(shape1);
                        textViewMon.setTextColor(oldColors);
                    } else {
                        weekOffs.add("Monday");
                        mon = true;
                        textViewMon.setBackground(shape);
                        textViewMon.setTextColor(Color.BLACK);
                    }
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Holiday Management to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.textViewTue:
                if (markHoliday) {
                    if (tue) {
                        tue = false;
                        weekOffs.remove("Tuesday");
                        textViewTue.setBackground(shape1);
                        textViewTue.setTextColor(oldColors);
                    } else {
                        weekOffs.add("Tuesday");
                        tue = true;
                        textViewTue.setBackground(shape);
                        textViewTue.setTextColor(Color.BLACK);
                    }
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Holiday Management to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.textViewWed:
                if (markHoliday) {
                    if (wed) {
                        wed = false;
                        weekOffs.remove("Wednesday");
                        textViewWed.setBackground(shape1);
                        textViewWed.setTextColor(oldColors);
                    } else {
                        weekOffs.add("Wednesday");
                        wed = true;
                        textViewWed.setBackground(shape);
                        textViewWed.setTextColor(Color.BLACK);
                    }
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Holiday Management to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.textViewThu:
                if (markHoliday) {
                    if (thu) {
                        thu = false;
                        weekOffs.remove("Thursday");
                        textViewThu.setBackground(shape1);
                        textViewThu.setTextColor(oldColors);
                    } else {
                        weekOffs.add("Thursday");
                        thu = true;
                        textViewThu.setBackground(shape);
                        textViewThu.setTextColor(Color.BLACK);
                    }
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Holiday Management to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.textViewFri:
                if (markHoliday) {
                    if (fri) {
                        fri = false;
                        weekOffs.remove("Friday");
                        textViewFri.setBackground(shape1);
                        textViewFri.setTextColor(oldColors);
                    } else {
                        weekOffs.add("Friday");
                        fri = true;
                        textViewFri.setBackground(shape);
                        textViewFri.setTextColor(Color.BLACK);
                    }
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Holiday Management to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.textViewSat:
                if (markHoliday2) {
                    if (sat) {
                        sat = false;
                        weekOffs.remove("Saturday");
                        textViewSat.setBackground(shape1);
                        textViewSat.setTextColor(oldColors);
                    } else {
                        weekOffs.add("Saturday");
                        sat = true;
                        textViewSat.setBackground(shape);
                        textViewSat.setTextColor(Color.BLACK);
                    }
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Holiday Management to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;

                //
            case R.id.textViewSun2:
                if (markHoliday2) {
                    if (sun2) {
                        sun2 = false;
                        weekOffs2.remove("Sunday");
                        textViewSun2.setBackground(shape1);
                        textViewSun2.setTextColor(oldColors);
                    } else {
                        weekOffs2.add("Sunday");
                        sun2 = true;
                        textViewSun2.setBackground(shape);
                        textViewSun2.setTextColor(Color.BLACK);
                    }
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Holiday Management 2 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.textViewMon2:
                if (markHoliday2) {
                    if (mon2) {
                        mon2 = false;
                        weekOffs2.remove("Monday");
                        textViewMon2.setBackground(shape1);
                        textViewMon2.setTextColor(oldColors);
                    } else {
                        weekOffs2.add("Monday");
                        mon2 = true;
                        textViewMon2.setBackground(shape);
                        textViewMon2.setTextColor(Color.BLACK);
                    }
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Holiday Management 2 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.textViewTue2:
                if (markHoliday2) {
                    if (tue2) {
                        tue2 = false;
                        weekOffs2.remove("Tuesday");
                        textViewTue2.setBackground(shape1);
                        textViewTue2.setTextColor(oldColors);
                    } else {
                        weekOffs2.add("Tuesday");
                        tue2 = true;
                        textViewTue2.setBackground(shape);
                        textViewTue2.setTextColor(Color.BLACK);
                    }
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Holiday Management 2 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.textViewWed2:
                if (markHoliday2) {
                    if (wed2) {
                        wed2 = false;
                        weekOffs2.remove("Wednesday");
                        textViewWed2.setBackground(shape1);
                        textViewWed2.setTextColor(oldColors);
                    } else {
                        weekOffs2.add("Wednesday");
                        wed2 = true;
                        textViewWed2.setBackground(shape);
                        textViewWed2.setTextColor(Color.BLACK);
                    }
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Holiday Management 2 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.textViewThu2:
                if (markHoliday2)
                {
                    if (thu2) {
                        thu2 = false;
                        weekOffs2.remove("Thursday");
                        textViewThu2.setBackground(shape1);
                        textViewThu2.setTextColor(oldColors);
                    } else {
                        weekOffs2.add("Thursday");
                        thu2 = true;
                        textViewThu2.setBackground(shape);
                        textViewThu2.setTextColor(Color.BLACK);
                    }
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Holiday Management 2 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.textViewFri2:
                if (markHoliday2)
                {
                    if (fri2)
                    {
                        fri2 = false;
                        weekOffs2.remove("Friday");
                        textViewFri2.setBackground(shape1);
                        textViewFri2.setTextColor(oldColors);
                    } else
                        {
                        weekOffs2.add("Friday");
                        fri2 = true;
                        textViewFri2.setBackground(shape);
                        textViewFri2.setTextColor(Color.BLACK);
                    }
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Holiday Management 2 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.textViewSat2:
                if (markHoliday2) {
                    if (sat2) {
                        sat2 = false;
                        weekOffs2.remove("Saturday");
                        textViewSat2.setBackground(shape1);
                        textViewSat2.setTextColor(oldColors);
                    } else {
                        weekOffs2.add("Saturday");
                        sat2 = true;
                        textViewSat2.setBackground(shape);
                        textViewSat2.setTextColor(Color.BLACK);
                    }
                } else {
                    Toast.makeText(SpecialManagement.this, "Click on Holiday Management 2 to enable editing", Toast.LENGTH_SHORT).show();
                }
                break;
            //
            case R.id.btnSave:
                if (startShift.getText().toString().matches("00:00") || preStartShift.getText().toString().matches("00:00") || stopShift.getText().toString().matches("00:00")
                        || postStopShift.getText().toString().matches("00:00") || employeeId.getText().toString().matches(""))
                {
                    if (startShift.getText().toString().matches("00:00"))
                    {
                        Toast.makeText(SpecialManagement.this, "Add Start Shift", Toast.LENGTH_SHORT).show();
                    } else if (preStartShift.getText().toString().matches("00:00")) {
                        Toast.makeText(SpecialManagement.this, "Add Pre Start Shift", Toast.LENGTH_SHORT).show();
                    } else if (stopShift.getText().toString().matches("00:00")) {
                        Toast.makeText(SpecialManagement.this, "Add Stop Shift", Toast.LENGTH_SHORT).show();
                    } else if (postStopShift.getText().toString().matches("00:00")) {
                        Toast.makeText(SpecialManagement.this, "Add Post Stop Shift", Toast.LENGTH_SHORT).show();
                    }
                    if (employeeId.getText().toString().matches("")) {
                        Toast.makeText(SpecialManagement.this, "Select Employee", Toast.LENGTH_SHORT).show();
                    }
                } else
                    {
                    if (weekOffs.size() == 7&&weekOffs2.size()==7)
                    {
                        Toast.makeText(SpecialManagement.this, "Atleast One day should be working", Toast.LENGTH_SHORT).show();

                    }  else if(txtappliedFromdate.getText().toString().isEmpty())
                    {
                        Toast.makeText(SpecialManagement.this, "Select applied from date", Toast.LENGTH_SHORT).show();

                    }else
                        {
                        weekOf = "";
                        weekoff2="";
                        if (!weekOffs.isEmpty())
                        {
                            for (int i = 0; i < weekOffs.size(); i++) {
                                if (i == 0) {
                                    weekOf = weekOf + weekOffs.get(i);
                                } else {
                                    weekOf = weekOf + "," + weekOffs.get(i);
                                }
                            }
                        }
                        if (!weekOffs2.isEmpty())
                        {
                            for (int i = 0; i < weekOffs2.size(); i++) {
                                if (i == 0) {
                                    weekoff2 = weekoff2 + weekOffs2.get(i);
                                } else {
                                    weekoff2 = weekoff2 + "," + weekOffs2.get(i);
                                }
                            }
                        }
                        if(weekOf.equalsIgnoreCase(""))
                        {
                            final PrettyDialog prettyDialog = new PrettyDialog(SpecialManagement.this);
                            prettyDialog
                                    .setTitle("Select Holiday")
                                    .setMessage("Set holiday for employee")
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
                        final Dialog dialog = new Dialog(SpecialManagement.this);
// Include dialog.xml file
                        dialog.setContentView(R.layout.dialog_exit);
                        dialog.show();
                        TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                        TextView description = dialog.findViewById(R.id.tv_description);
                        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                        heading.setText("Save");
                        description.setText("Are You Sure ?");
                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                progressDialog.show();
                                (Api.getClient()).setSpecialTimingData("update", employer_id,
                                        txtemployeeidhidden.getText().toString(), startShift.getText().toString(),
                                        stopShift.getText().toString(), preStartShift.getText().toString(),
                                        postStopShift.getText().toString(), weekOf,
                                        txtappliedFromdate.getText().toString(),startShift2.getText().toString(),
                                        stopShift2.getText().toString(), preStartShift2.getText().toString(),
                                        postStopShift2.getText().toString(), weekoff2
                                        ).enqueue(new Callback<GetJsonResponse>() {
                                    @Override
                                    public void onResponse(Call<GetJsonResponse> call, Response<GetJsonResponse> response) {
                                        try {
                                            if (response.body().getErrorCode() == 101)
                                            {
                                                progressDialog.dismiss();
                                              //startActivity(new Intent(SpecialManagement.this,EmployerZone.class));
                                              finish();
                                                Toast.makeText(SpecialManagement.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            } else  if (response.body().getErrorCode() == 102)
                                            {
                                                progressDialog.dismiss();
                                                final PrettyDialog prettyDialog = new PrettyDialog(SpecialManagement.this);
                                                prettyDialog
                                                        .setTitle("Response - "+response.body().getErrorCode())
                                                        .setMessage(response.body().getMessage())
                                                        .setIcon(R.drawable.cross)
                                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                                            @Override
                                                            public void onClick() {
                                                                prettyDialog.dismiss();
                                                            }
                                                        })
                                                        .show();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            progressDialog.dismiss();
                                            final Dialog dialog1 = new Dialog(SpecialManagement.this);
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

// if decline button is clicked, close the custom dialog
                        btnNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Close dialog
                                dialog.dismiss();
                            }
                        });
                    }
                }
                break;
        }
    }
    //backpress
    protected void exitByBackKey() {

        final Dialog dialog = new Dialog(SpecialManagement.this);
// Include dialog.xml file
        dialog.setContentView(R.layout.dialog_exit);
        dialog.show();

        Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //Intent intent = new Intent(SpecialManagement.this, EmployerZone.class);
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

    void reset()
    {
       /* textViewSun.setText("");
        textViewMon.setText("");
        textViewTue.setText("");
        textViewWed.setText("");
        textViewThu.setText("");
        textViewFri.setText("");
        textViewSat.setText("");*/
        startShift.setText("00:00");
    stopShift.setText("00:00");
        preStartShift.setText("00:00");
        postStopShift.setText("00:00");
        sun = false;
        mon = false;
        tue = false;
        wed = false;
        thu = false;
        fri = false;
        ;
        sat = false;
        textViewSun.setBackground(shape1);
        textViewMon.setBackground(shape1);
        textViewTue.setBackground(shape1);
        textViewWed.setBackground(shape1);
        textViewThu.setBackground(shape1);
        textViewFri.setBackground(shape1);
        textViewSat.setBackground(shape1);
        weekOffs = new ArrayList<>();
        weekOf = "";

        startShift2.setText("00:00");
        stopShift2.setText("00:00");
        preStartShift2.setText("00:00");
        postStopShift2.setText("00:00");
        sun2 = false;
        mon2 = false;
        tue2 = false;
        wed2 = false;
        thu2 = false;
        fri2 = false;
        ;
        sat2 = false;
        textViewSun2.setBackground(shape1);
        textViewMon2.setBackground(shape1);
        textViewTue2.setBackground(shape1);
        textViewWed2.setBackground(shape1);
        textViewThu2.setBackground(shape1);
        textViewFri2.setBackground(shape1);
        textViewSat2.setBackground(shape1);
        weekOffs2= new ArrayList<>();
        weekoff2= "";
    }

    private void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);

        if ( view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;

            for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }
    @Override
    public void onBackPressed() {
        exitByBackKey();
    }
}