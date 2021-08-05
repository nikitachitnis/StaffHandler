package com.miscos.staffhandler.ssa.dialogs;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.databinding.DataBindingUtil;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.DialogAddShiftBinding;
import com.miscos.staffhandler.employer_java.remote.NetworkService;
import com.miscos.staffhandler.employer_java.remote.model.NormalResponse;
import com.miscos.staffhandler.employer_java.utils.SnackBarHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogAddBreaks extends Dialog {

    public Activity mActivity;
    private DialogCallBack callBack;
    private final boolean isShift;
    private final int shiftCount;
    private DialogAddShiftBinding binding;
    private List<View> shiftViewList;


    public DialogAddBreaks(Activity mActivity, DialogCallBack callBack, boolean isShift, int shiftCount) {
        super(mActivity);
        this.mActivity = mActivity;
        this.callBack = callBack;
        this.isShift = isShift;
        this.shiftCount = shiftCount;
        this.shiftViewList = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        binding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.dialog_add_shift, null, false);
        setContentView(binding.getRoot());
        binding.txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiCallForAddShift();
            }
        });
        binding.txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onClickCancel();
                dismiss();
            }
        });

        binding.edtFrom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(mActivity, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        binding.edtFrom.setText(selectedHour + ":" + selectedMinute + ":00");
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        binding.edtTo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(mActivity, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hour, min;
                        if (selectedHour < 10)
                            hour = "0" + selectedHour;
                        else
                            hour = "" + selectedHour;

                        if (selectedMinute < 10)
                            min = "0" + selectedMinute;
                        else
                            min = "" + selectedMinute;

                        binding.edtTo.setText(hour + ":" + min + ":00");
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        if (isShift) {
            setShiftData();
        } else {
            binding.lyrShift.setVisibility(View.GONE);
            binding.lyrOffice.setVisibility(View.VISIBLE);
        }
    }

    private void setShiftData() {
        LayoutInflater mInflater = LayoutInflater.from(mActivity);
        for (int i = 0; i < shiftCount; i++) {
            View shiftView = mInflater.inflate(R.layout.layout_shift, null, false);
            shiftView.setTag(i + 1);
            EditText edtFrom = (EditText) shiftView.findViewById(R.id.edt_from);
            EditText edtTo = (EditText) shiftView.findViewById(R.id.edt_to);
            edtFrom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);

                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(mActivity, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            String hour, min;
                            if (selectedHour < 10)
                                hour = "0" + selectedHour;
                            else
                                hour = "" + selectedHour;

                            if (selectedMinute < 10)
                                min = "0" + selectedMinute;
                            else
                                min = "" + selectedMinute;

                            edtFrom.setText(hour + ":" + min + ":00");
                        }
                    }, hour, minute, true);
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();

                }
            });

            edtTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);

                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(mActivity, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            String hour, min;
                            if (selectedHour < 10)
                                hour = "0" + selectedHour;
                            else
                                hour = "" + selectedHour;

                            if (selectedMinute < 10)
                                min = "0" + selectedMinute;
                            else
                                min = "" + selectedMinute;

                            edtTo.setText(hour + ":" + min + ":00");
                        }
                    }, hour, minute, true);
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();

                }
            });
            shiftViewList.add(shiftView);
            binding.lyrShift.addView(shiftView);
        }
        binding.lyrShift.setVisibility(View.VISIBLE);
        binding.lyrOffice.setVisibility(View.GONE);

    }

    void ApiCallForAddShift() {

        String time = "";
        String operationType;

        if (isShift) {
            operationType = "shift";
            for (int i = 0; i < shiftViewList.size(); i++) {
                View shiftView = shiftViewList.get(i);
                EditText edtFrom = (EditText) shiftView.findViewById(R.id.edt_from);
                EditText edtTo = (EditText) shiftView.findViewById(R.id.edt_to);
                if (!TextUtils.isEmpty(edtFrom.getText().toString()) &&
                        !TextUtils.isEmpty(edtTo.getText().toString())) {
                    if (i > 0)
                        time += ",";
                    time += edtFrom.getText().toString() + "@" +
                            edtTo.getText().toString();
                } else {
                    SnackBarHelper.snackBarMessage(mActivity, "Required Field can't be empty");
                    return;
                }
            }
        } else {
            operationType = "office";
            if (!TextUtils.isEmpty(binding.edtFrom.getText().toString()) &&
                    !TextUtils.isEmpty(binding.edtTo.getText().toString())) {
                time = binding.edtFrom.getText().toString() + "@" +
                        binding.edtTo.getText().toString();
            } else {
                SnackBarHelper.snackBarMessage(mActivity, "Required Field can't be empty");
                return;
            }
        }

        NetworkService.getInstance().api().saveBreakTime("save_break_timing_data",
                "Shin3", "Shin3", operationType, time)
                .enqueue(new Callback<List<NormalResponse>>() {
                    @Override
                    public void onResponse(Call<List<NormalResponse>> call, Response<List<NormalResponse>> response) {
                        if (response.body() != null && response.body().size() > 0) {
                            SnackBarHelper.snackBarMessage(mActivity, response.body().get(0).getMessage());
                            if (response.body().get(0).getError_code() == 104) {
                                if (isShift) {
                                    for (int i = 0; i < shiftViewList.size(); i++) {
                                        View shiftView = shiftViewList.get(i);
                                        EditText edtTo = (EditText) shiftView.findViewById(R.id.edt_to);
                                        setNotificationAlarmForInterval(edtTo.getText().toString(),i);
                                    }
                                } else
                                    setNotificationAlarmForInterval(binding.edtTo.getText().toString(),786);
                                dismiss();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<NormalResponse>> call, Throwable t) {

                    }
                });

    }


    private void setNotificationAlarmForInterval(String time,int id) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        /*Date d = null;
        try {
            d = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (d == null)
            return;


        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(d.getTime() - 600000);

        Calendar currCal = Calendar.getInstance();

        currCal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
        currCal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
        currCal.set(Calendar.SECOND, cal.get(Calendar.SECOND));

        // if time is after or equal curr time then schedule for next day
        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > currCal.get(Calendar.HOUR_OF_DAY) ||
                (currCal.get(Calendar.HOUR_OF_DAY) == Calendar.getInstance().get(Calendar.HOUR_OF_DAY) &&
                        (currCal.get(Calendar.MINUTE) <= Calendar.getInstance().get(Calendar.MINUTE)))) {
            currCal.add(Calendar.DAY_OF_YEAR, 1); // add, not set!
        }*/
        String[] times = time.split(":");

        if (time.length()>=2) {

            Calendar currCal = Calendar.getInstance();
            currCal.setTimeInMillis(System.currentTimeMillis());
            currCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
            currCal.set(Calendar.MINUTE, Integer.parseInt(times[1]));
            currCal.set(Calendar.SECOND, 0);

            //alarm before 10 min
            currCal.add(Calendar.MINUTE, -10);

            if(Calendar.getInstance().after(currCal)){
                // Move to tomorrow
                currCal.add(Calendar.DATE, 1);
            }

            Intent myIntent = new Intent(mActivity, NotifyService.class);
            AlarmManager alarmManager = (AlarmManager) mActivity.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mActivity, id, myIntent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, currCal.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
        }

    }


}