package com.miscos.staffhandler.ssa.inout;

import android.app.DatePickerDialog;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.common.api.GoogleApiClient;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.ActivityInOutBinding;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.miscos.staffhandler.employer_java.remote.NetworkService;
import com.miscos.staffhandler.employer_java.remote.model.NormalResponse;
import com.miscos.staffhandler.employer_java.remote.model.breaktime.BreakTimeResponse;
import com.miscos.staffhandler.employer_java.remote.model.inoutdata.InOutResponse;
import com.miscos.staffhandler.employer_java.remote.model.inoutdata.oldinoutdata.OldInOutResponse;
import com.miscos.staffhandler.employer_java.utils.SnackBarHelper;
import com.miscos.staffhandler.ssa.dialogs.DialogAddBreaks;
import com.miscos.staffhandler.ssa.dialogs.DialogCallBack;
import com.miscos.staffhandler.ssa.dialogs.DialogReason;
import com.miscos.staffhandler.ssa.inout.location.EasyLocationProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InOutActivity extends AppCompatActivity {

    ActivityInOutBinding binding;
    PreferenceManager preferenceManager;
    String currentDate;
    InOutAdapter inOutAdapter;
    OldRecordsAdapter oldRecordsAdapter;
    EasyLocationProvider easyLocationProvider;
    double currLat, currLng, officeLat, officeLng;
    String lineNo="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_in_out);
        preferenceManager = new PreferenceManager(this);
        setCurrentData();
        setAdapter();
        setClickListeners();
        setLocationProvider();
        getInOutData();
        getBreakTime("Office");
    }

    private void getInOutData() {
        NetworkService.getInstance().api().getInOutData("fetch_today_in_out_data",
                preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID),
                preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEEID)).enqueue(new Callback<List<InOutResponse>>() {
            @Override
            public void onResponse(Call<List<InOutResponse>> call, Response<List<InOutResponse>> response) {
                if (response.body() != null && response.body().size() > 0) {
                    if (response.body().get(0).getData() != null) {
                        inOutAdapter.submitList(response.body().get(0).getData().getList());
                        lineNo = response.body().get(0).getData().getLineNo();
                        preferenceManager.setPreference(PreferenceManager.KEY_IS_OFFICE_OUT,response.body().get(0).getData().getStatus().equals("OUT"));
                        setCurrentData();
                    } else {
                        SnackBarHelper.snackBarMessage(InOutActivity.this, "No Data Found");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<InOutResponse>> call, Throwable t) {

            }
        });
    }

    private void getOldInOutData(String from, String to) {
        NetworkService.getInstance().api().getOldInOutData("get_old_record",
                preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID),
                preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEEID), from, to).enqueue(new Callback<List<OldInOutResponse>>() {
            @Override
            public void onResponse(Call<List<OldInOutResponse>> call, Response<List<OldInOutResponse>> response) {
                if (response.body() != null && response.body().size() > 0) {
                    if (response.body().get(0).getData() != null)
                        oldRecordsAdapter.submitList(response.body().get(0).getData());
                    else
                        SnackBarHelper.snackBarMessage(InOutActivity.this, "No Data Found");
                }
            }

            @Override
            public void onFailure(Call<List<OldInOutResponse>> call, Throwable t) {

            }
        });
    }

    private void setLocationProvider() {
        easyLocationProvider = new EasyLocationProvider.Builder(this)
                .setInterval(5000)
                .setFastestInterval(2000)
                //.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setListener(new EasyLocationProvider.EasyLocationCallback() {
                    @Override
                    public void onGoogleAPIClient(GoogleApiClient googleApiClient, String message) {
                        Log.e("EasyLocationProvider", "onGoogleAPIClient: " + message);
                    }

                    @Override
                    public void onLocationUpdated(double latitude, double longitude) {
                        currLat = latitude;
                        currLng = longitude;
                        Log.e("EasyLocationProvider", "onLocationUpdated:: " + "Latitude: " + latitude + " Longitude: " + longitude);
                    }

                    @Override
                    public void onLocationUpdateRemoved() {
                        Log.e("EasyLocationProvider", "onLocationUpdateRemoved");
                    }
                }).build();

        getLifecycle().addObserver(easyLocationProvider);
    }


    private boolean isLatLngWithInOffice() {
        float[] results = new float[1];
        Location.distanceBetween(officeLat, officeLng, currLat, currLng, results);
        float distanceInMeters = results[0];
        return distanceInMeters < 10;
    }

    @Override
    protected void onDestroy() {
        easyLocationProvider.removeUpdates();
        getLifecycle().removeObserver(easyLocationProvider);
        super.onDestroy();
    }

    private void setClickListeners() {
        binding.btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferenceManager.getBooleanPreference(PreferenceManager.KEY_IS_OFFICE_OUT)) {
                    callAPISubmit("IN", "");
                } else {
                    new DialogReason(InOutActivity.this, new DialogCallBack() {
                        @Override
                        public void onClickOk(String value) {
                            callAPISubmit("OUT", value);
                        }

                        @Override
                        public void onClickCancel() {

                        }
                    }).show();
                }
            }
        });
        binding.txtAddShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogAddBreaks(InOutActivity.this, new DialogCallBack() {
                    @Override
                    public void onClickOk(String value) {

                    }

                    @Override
                    public void onClickCancel() {

                    }
                }, true, 3).show();
            }
        });
        binding.edtFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(InOutActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String fromDate;
                                if (monthOfYear < 9)
                                    fromDate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
                                else
                                    fromDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                binding.edtFrom.setText(fromDate);
                                binding.edtTo.setText("");

                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

        binding.edtTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(InOutActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                if (!TextUtils.isEmpty(binding.edtFrom.getText().toString())) {
                                    try {
                                        if (new SimpleDateFormat("dd-MM-yyyy").parse(binding.edtFrom.getText().toString()).before(new SimpleDateFormat("dd-MM-yyyy").parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)) ||
                                                new SimpleDateFormat("dd-MM-yyyy").parse(binding.edtFrom.getText().toString()).equals(new SimpleDateFormat("dd-MM-yyyy").parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year))) {
                                            String toDate;
                                            if (monthOfYear < 9)
                                                toDate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
                                            else
                                                toDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                            binding.edtTo.setText(toDate);
                                            getOldInOutData(binding.edtFrom.getText().toString(), binding.edtTo.getText().toString());
                                        } else
                                            SnackBarHelper.snackBarMessage(InOutActivity.this, "To date must be greater than or equals to from date");
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else
                                    SnackBarHelper.snackBarMessage(InOutActivity.this, "Please Select from date");
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
    }

    private void callAPISubmit(String status, String reason) {
        NetworkService.getInstance()
                .api()
                .addInOutData("save_today_in_out_data", "Shin3", "Shin3",lineNo,
                        new SimpleDateFormat("yyyy-MM-dd").format(new Date()), status, reason)
                .enqueue(new Callback<List<NormalResponse>>() {
                    @Override
                    public void onResponse(Call<List<NormalResponse>> call, Response<List<NormalResponse>> response) {
                        if (response.body() != null && response.body().size() > 0) {
                            if (response.body().get(0).getError_code() == 104) {
                               getInOutData();
                            } else
                                SnackBarHelper.snackBarMessage(InOutActivity.this, response.body().get(0).getMessage());

                        }
                    }

                    @Override
                    public void onFailure(Call<List<NormalResponse>> call, Throwable t) {

                    }
                });
    }

    private void getBreakTime(String op) {
        NetworkService.getInstance()
                .api()
                .getBreakTimingData("get_break_timing_data", preferenceManager.getStringPreference(preferenceManager.KEY_EMPLOYERID), preferenceManager.getStringPreference(preferenceManager.KEY_EMPLOYEEID),op)
                .enqueue(new Callback<List<BreakTimeResponse>>() {
                    @Override
                    public void onResponse(Call<List<BreakTimeResponse>> call, Response<List<BreakTimeResponse>> response) {
                        if (response.body() != null && response.body().size() > 0) {
                            if (response.body().get(0).getErrorCode() == 104) {
                               if (!preferenceManager.getBooleanPreference(PreferenceManager.KEY_IS_OFFICE_OUT)) {
                                   for (int i = 0; i < response.body().get(0).getData().getOfficeData().size() ; i++) {
                                       String[] from = response.body().get(0).getData().getOfficeData().get(i).getFromTime().split(":");
                                       String[] to = response.body().get(0).getData().getOfficeData().get(i).getToTime().split(":");
                                       if (from.length<2|| to.length<2)
                                           break;
                                       Calendar fromCal = Calendar.getInstance();
                                       fromCal.setTimeInMillis(System.currentTimeMillis());
                                       fromCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(from[0]));
                                       fromCal.set(Calendar.MINUTE, Integer.parseInt(from[1]));
                                       fromCal.set(Calendar.SECOND, 0);


                                       Calendar toCal = Calendar.getInstance();
                                       toCal.setTimeInMillis(System.currentTimeMillis());
                                       toCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(to[0]));
                                       toCal.set(Calendar.MINUTE, Integer.parseInt(to[1]));
                                       toCal.set(Calendar.SECOND, 0);

                                       Calendar currCal = Calendar.getInstance();

                                       if ((currCal.after(from)||currCal.equals(fromCal)) && currCal.before(toCal)) {
                                           binding.btnAction.setText("Break Time");
                                       }
                                   }

                               }
                            } else
                                SnackBarHelper.snackBarMessage(InOutActivity.this, response.body().get(0).getMessage());

                        }
                    }

                    @Override
                    public void onFailure(Call<List<BreakTimeResponse>> call, Throwable t) {

                    }
                });
    }

    private void setAdapter() {
        binding.recyclerViewInOut.setAdapter(inOutAdapter = new InOutAdapter());
        binding.recyclerViewOldRecords.setAdapter(oldRecordsAdapter = new OldRecordsAdapter());
    }

    private void setCurrentData() {
        if (preferenceManager.getBooleanPreference(PreferenceManager.KEY_IS_OFFICE_OUT)) {
            binding.txtCurrentStatus.setText("Out Of Office");
            binding.txtCurrentStatus.setTextColor(getResources().getColor(R.color.red));
            binding.btnAction.setText("Back to The Office");

        } else {
            binding.txtCurrentStatus.setText("In Office");
            binding.txtCurrentStatus.setTextColor(getResources().getColor(R.color.green));
            binding.btnAction.setText("Going Out From The Office");
        }

        binding.txtDate.setText(currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

    }


}