package com.miscos.staffhandler.employer_java.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.miscos.staffhandler.R;

import com.miscos.staffhandler.databinding.FragmentForm3Binding;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.miscos.staffhandler.employer_java.activity.HolidayManagement;
import com.miscos.staffhandler.employer_java.activity.MainActivity;
import com.miscos.staffhandler.employer_java.adapter.CheckboxAdapter;
import com.miscos.staffhandler.employer_java.remote.NetworkService;
import com.miscos.staffhandler.employer_java.remote.model.NormalResponse;
import com.miscos.staffhandler.employer_java.remote.model.fetchdataform2.EmployeeDataItem;
import com.miscos.staffhandler.employer_java.remote.model.fetchdataform2.FetchResponseForm2;
import com.miscos.staffhandler.employer_java.remote.model.fetchdataform2.HolidayData;

import com.miscos.staffhandler.employer_java.utils.SnackBarHelper;
import com.miscos.staffhandler.shiftmanagement.fragments.ShiftManagement;
import com.miscos.staffhandler.ssa.latearrivalearlydeparture.LateArrivalEarlyDepartureActivity;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;


import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Form3Fragment extends Fragment implements RadioGroup.OnCheckedChangeListener, PaymentResultListener {
    private static final int PICK_IMAGE_REQUEST = 101;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
    // SimpleDateFormat simpleDateOutputFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);

    String filePath;
    String employerId;
    Date breakstarttime,breakstoptime;
FragmentForm3Binding binding;
    CheckboxAdapter checkboxAdapter;
    List<HolidayData> holidayData;
    private EmployeeDataItem empDataItem;
    private PreferenceManager preferenceManager;
    private String selectedLicenceCount;
    private String totalPayAmount;
    List<String> weekoffList,selected_weekofflist;
    private String feedback = "",aadhar_feedback="No",break_duration="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_form3, container, false);
        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("employerId", employerId);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            employerId = savedInstanceState.getString("employerId");
        else
            employerId = Form3FragmentArgs.fromBundle(getArguments()).getEmployerId();

        preferenceManager = new PreferenceManager(getContext());

       /* binding.cancel.setOnClickListener {
            requireActivity().onBackPressed()
        }*/
       selected_weekofflist=new ArrayList<>();
       weekoffList= Arrays.asList(
               "Monday",
               "Tuesday",
               "Wednesday",
               "Thursday",
               "Friday",
               "Saturday",
               "Sunday");
        List<String> breakDuration= new ArrayList<>(Arrays.asList("Select","10 mins", "15 mins", "20 mins", "30 mins","60 mins","90 mins"));
       ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getActivity(),R.layout.spinner_item1,R.id.spinnerText1,breakDuration);
        binding.spinDuration.setAdapter(adapter1);
        binding.spinDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(i==0)
                    return;
                if(breakstarttime==null||breakstoptime==null)
                {
                    Toast.makeText(getActivity(), "Break start or stop time should not be blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(diffTime(breakstarttime,breakstoptime,Integer.parseInt(breakDuration.get(i).split(" ")[0])))
                    break_duration=breakDuration.get(i);
                else
                {
                    binding.spinDuration.setSelection(0);
                    break_duration="";
                    Toast.makeText(getActivity(), "Choose valid duration", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
        checkboxAdapter = new CheckboxAdapter(new CheckboxAdapter.CallBack() {
            @Override
            public void onItemClick(String it,int pos)
            {
                if (checkboxAdapter.selectedValue.contains(it.toLowerCase()))
                {
                    selected_weekofflist.remove(weekoffList.get(pos).toLowerCase());
                    checkboxAdapter.selectedValue.remove(it.toLowerCase());

                } else {
                    //validation check for week off
//                if (adapter.selectedValue.size < 2)
                    selected_weekofflist.add(weekoffList.get(pos).toLowerCase());
                    checkboxAdapter.selectedValue.add(it.toLowerCase());
//                else
//                    adapter.selectedValue[0] = it.toLowerCase()
                }

                checkboxAdapter.notifyDataSetChanged();
            }
        });

        binding.weekOffRecyclerView.setAdapter(checkboxAdapter);
LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        binding.weekOffRecyclerView.setLayoutManager(layoutManager);
        checkboxAdapter.submitList(
                Arrays.asList(
                        "Monday",
                        "Tuesday",
                        "Wednesday",
                        "Thursday",
                        "Friday",
                        "Saturday",
                        "Sunday")
        );
        fetchApiCall();
        binding.manageHolidays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), LateArrivalEarlyDepartureActivity.class);
                startActivity(intent);
            }
        });

        binding.uploadLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(requireActivity());
                dialog.setContentView(R.layout.dailog_for_photo);
                dialog.getWindow().setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                MaterialButton ok = dialog.findViewById(R.id.okButton);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        binding.setBreakStartTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        try {
                            Date preStartDate = simpleDateFormat.parse(binding.setOpeningTime.getText().toString());
                            Date openingDate = calendar.getTime();
                            if (preStartDate.after(openingDate))
                            {
                                binding.setBreakStartTime.setText(simpleDateFormat.format(calendar.getTime()));
                                breakstarttime=openingDate;
                            } else
                                SnackBarHelper.snackBarMessage(getActivity(), "Break start time should be after opening time");
                        } catch (ParseException e) {
                            e.printStackTrace();
                            binding.setBreakStartTime.setText(simpleDateFormat.format(calendar.getTime()));
                        }
                    }
                };

                if (binding.setBreakStartTime.getText().toString().equals("SET TIME")) {
                    new TimePickerDialog(
                            getActivity(),
                            timeSetListener,
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                    ).show();
                } else {
                    try {
                        Date date = simpleDateFormat.parse(binding.setBreakStartTime.getText().toString());
                        breakstarttime=date;
                        calendar.setTime(date);
                        new TimePickerDialog(
                                getActivity(),
                                timeSetListener,
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                        ).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        binding.setBreakStopTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        try {
                            Date preStartDate = simpleDateFormat.parse(binding.setclosingTime.getText().toString());
                            Date openingDate = calendar.getTime();
                            if (openingDate.before(preStartDate)) {
                                binding.setBreakStopTime.setText(simpleDateFormat.format(calendar.getTime()));
                                breakstoptime=openingDate;
                            } else
                                SnackBarHelper.snackBarMessage(getActivity(), "Break stop time should be before closing time");
                        } catch (ParseException e) {
                            e.printStackTrace();
                            binding.setBreakStopTime.setText(simpleDateFormat.format(calendar.getTime()));
                        }
                    }
                };

                if (binding.setBreakStopTime.getText().toString().equals("SET TIME"))
                {
                    new TimePickerDialog(
                            getActivity(),
                            timeSetListener,
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                    ).show();
                } else {
                    try {
                        Date date = simpleDateFormat.parse(binding.setBreakStopTime.getText().toString());
                        calendar.setTime(date);
                        breakstoptime=date;
                        new TimePickerDialog(
                                getActivity(),
                                timeSetListener,
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                        ).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        binding.setOpeningTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        try {
                            Date preStartDate = simpleDateFormat.parse(binding.setPreStartTime.getText().toString());
                            Date openingDate = calendar.getTime();
                            if (preStartDate.before(openingDate)) {
                                binding.setOpeningTime.setText(simpleDateFormat.format(calendar.getTime()));
                            } else
                                SnackBarHelper.snackBarMessage(getActivity(), "Opening time must be after Pre start time ");
                        } catch (ParseException e) {
                            e.printStackTrace();
                            binding.setOpeningTime.setText(simpleDateFormat.format(calendar.getTime()));
                        }
                    }
                };

                if (binding.setOpeningTime.getText().toString() == "SET TIME") {
                    new TimePickerDialog(
                            getActivity(),
                            timeSetListener,
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                    ).show();
                } else {
                    try {
                        Date date = simpleDateFormat.parse(binding.setOpeningTime.getText().toString());
                        calendar.setTime(date);
                        new TimePickerDialog(
                                getActivity(),
                                timeSetListener,
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                        ).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        });



        binding.setclosingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        try {
                            Date postClosingDate = simpleDateFormat.parse(binding.setPostClosingTime.getText().toString());
                            Date closingDate = calendar.getTime();
                            if (postClosingDate.after(closingDate)) {
                                binding.setclosingTime.setText(simpleDateFormat.format(calendar.getTime()));
                            } else
                                SnackBarHelper.snackBarMessage(getActivity(), "Closing time must be before post closing time");
                        } catch (ParseException e) {
                            e.printStackTrace();
                            binding.setclosingTime.setText(simpleDateFormat.format(calendar.getTime()));
                        }
                    }
                };
                if (binding.setclosingTime.getText().toString() == "SET TIME") {
                    new TimePickerDialog(
                            getActivity(),
                            timeSetListener,
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                    ).show();
                } else {
                    try {
                        Date date = simpleDateFormat.parse(binding.setclosingTime.getText().toString());
                        calendar.setTime(date);
                        new TimePickerDialog(
                                getActivity(),
                                timeSetListener,
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                        ).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


        binding.setPreStartTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        try {
                            Date openingDate = simpleDateFormat.parse(binding.setOpeningTime.getText().toString());
                            Date preStartDate = calendar.getTime();
                            if (preStartDate.before(openingDate)) {
                                binding.setPreStartTime.setText(simpleDateFormat.format(calendar.getTime()));
                            } else
                                SnackBarHelper.snackBarMessage(getActivity(), "Pre start time must be before opening time");
                        } catch (ParseException e) {
                            e.printStackTrace();
                            binding.setPreStartTime.setText(simpleDateFormat.format(calendar.getTime()));
                        }

                    }
                };
                if (binding.setPreStartTime.getText().toString() == "SET TIME") {
                    new TimePickerDialog(
                            getActivity(),
                            timeSetListener,
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                    ).show();
                } else {
                    Date date = null;
                    try {
                        date = simpleDateFormat.parse(binding.setPreStartTime.getText().toString());
                        calendar.setTime(date);
                        new TimePickerDialog(
                                getActivity(),
                                timeSetListener,
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                        ).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        binding.setPostClosingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        try {
                            Date closingDate = simpleDateFormat.parse(binding.setclosingTime.getText().toString());
                            Date postClosingDate = calendar.getTime();
                            if (postClosingDate.after(closingDate)) {
                                binding.setPostClosingTime.setText(simpleDateFormat.format(calendar.getTime()));
                            } else
                                SnackBarHelper.snackBarMessage(getActivity(), "post closing time must be after closing time");
                        } catch (ParseException e) {
                            e.printStackTrace();
                            binding.setPostClosingTime.setText(simpleDateFormat.format(calendar.getTime()));
                        }
                    }
                };

                if (binding.setPostClosingTime.getText().toString() == "SET TIME") {
                    new TimePickerDialog(
                            getActivity(),
                            timeSetListener,
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                    ).show();
                } else {
                    Date date = null;
                    try {
                        date = simpleDateFormat.parse(binding.setPostClosingTime.getText().toString());
                        calendar.setTime(date);
                        new TimePickerDialog(
                                getActivity(),
                                timeSetListener,
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                        ).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  viewModel.fetchResponse.value?.data?.employeeData?.getOrNull(0)?.officePicture.isNullOrEmpty() && filePath.isNullOrEmpty() -> SnackBarHelper.snackBarMessage(
                    requireActivity(),
                    "Please upload company logo"
                )*/
                if (binding.setOpeningTime.getText().toString().equalsIgnoreCase("SET TIME")) {
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please set opening time"
                    );
                } else if (binding.setclosingTime.getText().toString().equalsIgnoreCase("SET TIME")) {
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please set closing time"
                    );
                } else if (binding.setPostClosingTime.getText().toString().equalsIgnoreCase("SET TIME")) {
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please set post closing time"
                    );
                } else if (binding.setPreStartTime.getText().toString().equalsIgnoreCase("SET TIME")) {
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please set pre start time"
                    );
                } else if (binding.rgAdharEnable.getCheckedRadioButtonId() == -1) {
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please select Enable the Aadhaar Card verifications "
                    );
                } else if (binding.rgFeedback.getCheckedRadioButtonId() == -1) {
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please select Enable the save the feedback "
                    );
                } else if (binding.rgEmpActivity.getCheckedRadioButtonId() == -1) {
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please select Enable Employee Activities"
                    );
                } else if (binding.rgEmpManually.getCheckedRadioButtonId() == -1) {
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please select Employee No Manually"
                    );
                } else {
                    try {
                        postForm3API();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        binding.rgAdharEnable.setOnCheckedChangeListener(this);
        binding.rgEmpManually.setOnCheckedChangeListener(this);

        binding.txtRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogS03();
            }
        });
    }

    private void postForm3API() throws ParseException {
        /*String pre_start_timing, post_close_timing,start,close;
        if (binding.setPreStartTime.getText().toString() != "SET TIME")
            pre_start_timing = binding.setPreStartTime.getText().toString();
        else
            pre_start_timing = "00:00:00";

        if (binding.setPostClosingTime.getText().toString() != "SET TIME")
            post_close_timing = binding.setPostClosingTime.getText().toString();
        else
            post_close_timing = "00:00:00";
        if (binding.setOpeningTime.getText().toString() != "SET TIME")
            start = binding.setOpeningTime.getText().toString();
        else
            start = "00:00:00";
        if (binding.setclosingTime.getText().toString() != "SET TIME")
            close = binding.setclosingTime.getText().toString();
        else
            close = "00:00:00";*/
        String aadhar, feedback, activity, noFormat;
        if (binding.rgAdharEnable.getCheckedRadioButtonId() == R.id.r_y) {
            aadhar = "Y";
        } else aadhar = "N";
        if (binding.rgFeedback.getCheckedRadioButtonId() == R.id.feedback_y) {
            feedback = "Y";
        } else feedback = "N";
        if (binding.rgEmpActivity.getCheckedRadioButtonId() == R.id.activity_y) {
            activity = "Y";
        } else activity = "N";
        if (binding.rgEmpManually.getCheckedRadioButtonId() == R.id.emp_no_manually_y) {
            noFormat = "Y";
        } else noFormat = "N";

        String adhar_flag="";
        if(preferenceManager.getStringPreference(PreferenceManager.KEY_ADHAAR_ENABLED).equalsIgnoreCase(""))
            adhar_flag="no";
        else if(preferenceManager.getStringPreference(PreferenceManager.KEY_ADHAAR_ENABLED).equalsIgnoreCase("yes"))
            adhar_flag="yes";
if(checkboxAdapter.selectedValue.size()==0)
{
    SnackBarHelper.snackBarMessage(requireActivity(), "Please add  atleast one weekoff");
    return;
}
        ((MainActivity) getActivity()).showProgressDialog();
        NetworkService.getInstance().api().postForm3(
                employerId,
                "update",
                TextUtils.join(",", checkboxAdapter.selectedValue),
                binding.setOpeningTime.getText().toString(),
                binding.setclosingTime.getText().toString(),
                binding.setPreStartTime.getText().toString(),
                binding.setPostClosingTime.getText().toString(),
                aadhar, feedback, activity, getEmpNoFormat(), noFormat, aadhar_feedback,adhar_flag,binding.setBreakStartTime.getText().toString(),binding.setBreakStopTime.getText().toString(),
                break_duration
        ).enqueue(new Callback<NormalResponse>() {
            @Override
            public void onResponse(Call<NormalResponse> call, Response<NormalResponse> it)
            {
                ((MainActivity) getActivity()).dismissProgressDialog();
                if (binding.rY.isChecked())
                    preferenceManager.setPreference(PreferenceManager.KEY_ADHAAR_ENABLED, "yes");
                if (it.body() != null) {
                    if(it.body().getStatus()==101)
                    {
                        preferenceManager.setPreference(PreferenceManager.KEY_ENABLE_EMPLOYEE_FEEDBACK,feedback);
                        preferenceManager.setPreference(PreferenceManager.KEY_AADHAR_VERIFY_POLICY,aadhar);
                        preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_NO_TYPE,noFormat);

                        PrettyDialog prettyDialog = new PrettyDialog(getActivity());
                        prettyDialog.setCancelable(false);
                        prettyDialog
                                .setTitle(getActivity().getString(R.string.app_name))
                                .setMessage("Policies updated successfully")
                                .setIcon(R.drawable.ic_done, R.color.color_white, null)
                                .addButton("Okay", R.color.color_white, R.color.blue, new PrettyDialogCallback() {
                                    @Override
                                    public void onClick() {
                                        prettyDialog.dismiss();
                                        getActivity().finish();
                                    }
                                })
                                .show();
                    }
                    else
                    {
                        PrettyDialog prettyDialog = new PrettyDialog(getActivity());
                        prettyDialog.setCancelable(false);
                        prettyDialog
                                .setTitle(getActivity().getString(R.string.app_name))
                                .setMessage(it.body().getMessage())
                                .setIcon(R.drawable.ic_done, R.color.color_white, null)
                                .addButton("Okay", R.color.color_white, R.color.blue, () -> prettyDialog.dismiss())
                                .show();
                    }



                } else
                    SnackBarHelper.snackBarMessage(requireActivity(), getString(R.string.something_wrong));

            }

            @Override
            public void onFailure(Call<NormalResponse> call, Throwable t) {
                ((MainActivity) getActivity()).dismissProgressDialog();
                SnackBarHelper.snackBarMessage(requireActivity(), t.getMessage());
            }
        });
    }

    public String getEmpNoFormat() {
        return binding.empNameChar.getText().toString() +"-"+ binding.spEmpMonth.getSelectedItem().toString() +"-"+ binding.empCompanyChar.getText().toString();
    }

    private void fetchApiCall()
    {
        ((MainActivity) getActivity()).showProgressDialog();
        String currYear = new SimpleDateFormat("yyyy").format(new Date());
        NetworkService.getInstance().api().fetchDataForm2("fetch", employerId, currYear)
                .enqueue(new Callback<FetchResponseForm2>() {
                    @Override
                    public void onResponse(Call<FetchResponseForm2> call, Response<FetchResponseForm2> it) {
                        ((MainActivity) getActivity()).dismissProgressDialog();
                        if (it.body() != null) {
                            binding.rgEmpManually.setOnCheckedChangeListener(null);
                            binding.rgAdharEnable.setOnCheckedChangeListener(null);
                            Log.e("fetchDataForm2", it.body().getMessage());

                            EmployeeDataItem dataItem = it.body().getEmployeeData().get(0);
                            empDataItem = it.body().getEmployeeData().get(0);
                            if (it.body().getHolidayData().size() > 0)
                                holidayData = it.body().getHolidayData();
                            binding.setData(dataItem);
                            if(empDataItem.getWantAdharVerification().equalsIgnoreCase("y"))
                            {
                                binding.lyrVerification.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                binding.lyrVerification.setVisibility(View.GONE);
                            }
                            if(empDataItem.getEmployeeNumberType().equalsIgnoreCase("y"))
                            {
                                binding.lyrAutoGeneratedWill.setVisibility(View.GONE);
                            }
                            else
                            {
                                binding.lyrAutoGeneratedWill.setVisibility(View.VISIBLE);
                            }
                            new Handler().postDelayed(new Runnable()
                            {
                                @Override
                                public void run() {
                                    binding.rgEmpManually.setOnCheckedChangeListener(Form3Fragment.this);
                                    binding.rgAdharEnable.setOnCheckedChangeListener(Form3Fragment.this);
                                }
                            }, 300);

                            if (!dataItem.getInTiming().equals("00:00:00")) {
                                   /* try {
                                       binding.setOpeningTime.setText(simpleDateFormat.format(simpleDateOutputFormat.parse(dataItem.getInTiming())));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }*/
                                binding.setOpeningTime.setText(dataItem.getInTiming());
                            }
                            if (!dataItem.getOutTiming().equals("00:00:00"))
                            {
                                binding.setclosingTime.setText(dataItem.getOutTiming());
                            }
                            if (!dataItem.getPreStartTiming().equals("00:00:00"))
                            {
                                binding.setPreStartTime.setText(dataItem.getPreStartTiming());
                            }
                            if (!dataItem.getPostCloseTiming().equals("00:00:00")) {
                                binding.setPostClosingTime.setText(dataItem.getPostCloseTiming());
                            }
                           /* if (!TextUtils.isEmpty(dataItem.getOfficePicture())) {
                                binding.uploadLogo.setVisibility(View.VISIBLE);
                                // binding.uploadedImage.visibility = VISIBLE
                            }*/
                       /* if (!it.officePicture.isNullOrEmpty()) {
                            binding.uploadLogo.visibility = VISIBLE
                           // binding.uploadedImage.visibility = VISIBLE
                            Glide.with(requireActivity()).load(it.officePicture)
                                .apply(
                                    RequestOptions().fitCenter()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                )
                                .into(binding.uploadedImage)
                        }*/
                            if (!TextUtils.isEmpty(dataItem.getWeekOff()))
                            {
                                String[] items = dataItem.getWeekOff().trim().split("[,]");
                                for (int i = 0; i < items.length; i++)
                                {
                                    items[i] = items[i].trim();
                                }
                                checkboxAdapter.selectedValue = new ArrayList<String>(Arrays.asList(items));
                                checkboxAdapter.notifyDataSetChanged();
                            }

                            if (!TextUtils.isEmpty(empDataItem.getEmployeeNumberType()) && empDataItem.getEmployeeNumberType().equalsIgnoreCase("Y")){
                            binding.lyrAutoGeneratedWill.setVisibility(View.GONE);
                            } else {
                                binding.lyrAutoGeneratedWill.setVisibility(View.VISIBLE);
                            }
                            setAutoGeneratedEmpNo();
                        } else
                            SnackBarHelper.snackBarMessage(requireActivity(), getString(R.string.something_wrong));
                    }

                    @Override
                    public void onFailure(Call<FetchResponseForm2> call, Throwable t) {
                        Log.e("fetchDataForm2", t.getMessage());
                        ((MainActivity) getActivity()).dismissProgressDialog();
                        SnackBarHelper.snackBarMessage(requireActivity(), getString(R.string.something_wrong));
                    }
                });
    }

    private void setAutoGeneratedEmpNo() {
        if (empDataItem != null)
        {
            if (!TextUtils.isEmpty(empDataItem.getEmployeeNumberFormat()) && empDataItem.getEmployeeNumberFormat().length() == 8) {
                String name = empDataItem.getEmployeeNumberFormat().substring(0, 3);
                String month = empDataItem.getEmployeeNumberFormat().substring(3, 5);
                String company = empDataItem.getEmployeeNumberFormat().substring(5, 8);
                binding.empNameChar.setText(name);
                binding.empCompanyChar.setText(company);
                if (month.equalsIgnoreCase("MM"))
                    binding.spEmpMonth.setSelection(0);
                else
                    binding.spEmpMonth.setSelection(1);
            } else {
                binding.empNameChar.setText(empDataItem.getCompanyName());
                binding.empCompanyChar.setText("000");
                binding.spEmpMonth.setSelection(0);
            }
        }
    }


    public void showDialogS01()
    {
        if (empDataItem != null) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_s01);
            TextView textMsg = (TextView) dialog.findViewById(R.id.txt_msg);
            TextView textAadhar = (TextView) dialog.findViewById(R.id.txt_aadhar_msg);
            TextView textOk = (TextView) dialog.findViewById(R.id.txt_ok);
            textMsg.setText("You are very lucky " + empDataItem.getOwnerName() + " Our Random Smart has free Gift for You");
            textAadhar.setText(empDataItem.getAmountPerApiVerification() + "\n" + "Aadhar\n" + "Verifications");
            textOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

    }

    public void showDialogS02()
    {
        if (empDataItem != null)
        {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_s02);
            EditText textMsg = (EditText) dialog.findViewById(R.id.edt_msg);
            TextView textOk = (TextView) dialog.findViewById(R.id.txt_ok);
            textOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    aadhar_feedback = textMsg.getText().toString();
                    if(aadhar_feedback.isEmpty())
                    {
                        textMsg.setError("Please tell us your feedback");
                        return;
                    }
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

    }

    public void showDialogS03()
    {
        if (empDataItem != null) {
            String[] l = getContext().getResources().getStringArray(R.array.verification_licence_count);
            selectedLicenceCount = l[0];
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_s03);
            Spinner licence = (Spinner) dialog.findViewById(R.id.spinner_licence);
            TextView textPay = (TextView) dialog.findViewById(R.id.txt_pay);
            TextView textOk = (TextView) dialog.findViewById(R.id.txt_ok);
            textPay.setText(Integer.parseInt(selectedLicenceCount) * empDataItem.getAmountPerApiVerification() + "/-");
            licence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedLicenceCount = l[position];
                    preferenceManager.setPreference(PreferenceManager.KEY_ADHAR_LICENSECOUNT,selectedLicenceCount);

                    textPay.setText(Integer.parseInt(selectedLicenceCount) * empDataItem.getAmountPerApiVerification() + "/-");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            textOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //       ((MainActivity) getActivity()).showProgressDialog();
                    startPayment(Integer.parseInt(selectedLicenceCount) * empDataItem.getAmountPerApiVerification() + "");
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

    }

    //Payment Mode
    public void startPayment(String orderamount) {
        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = getActivity();
        totalPayAmount = orderamount;
        preferenceManager.setPreference(PreferenceManager.KEY_ADHAR_LICENSE_AMOUNT,totalPayAmount);
        final Checkout co = new Checkout();
        co.setImage(R.drawable.splashlogo);
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Touch Tech solutions & services Pvt Ltd.");
            options.put("description", "Adhar Verification");
            //You can omit the image option to fetch the image from dashboard
            options.put("currency", "INR");
            String payment = orderamount;
            // amount is in paise so please multiple it by 100
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);
            co.open(activity, options);
        } catch (Exception e) {
            //  ((MainActivity) getActivity()).dismissProgressDialog();
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s)
    {
        // payment successfull pay_DGU19rDsInjcF
        Log.e("TAG", " payment successful " + s.toString());
        NetworkService.getInstance().api().form3PaymentAPI(
                "pay_for_adhar_verification", employerId, selectedLicenceCount, totalPayAmount, s, s, "Success"
        ).enqueue(new Callback<NormalResponse>() {
            @Override
            public void onResponse(Call<NormalResponse> call, Response<NormalResponse> it) {
                //    ((MainActivity) getActivity()).dismissProgressDialog();
                if (it.body() != null) {
                    SnackBarHelper.snackBarMessage(requireActivity(), it.body().getMessage());
                } else
                    SnackBarHelper.snackBarMessage(requireActivity(), getString(R.string.something_wrong));

            }

            @Override
            public void onFailure(Call<NormalResponse> call, Throwable t) {
                //       ((MainActivity) getActivity()).dismissProgressDialog();
                SnackBarHelper.snackBarMessage(requireActivity(), getString(R.string.something_wrong));
            }
        });

    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("TAG", "error code " + String.valueOf(i) + " -- Payment failed " + s.toString());
        ((MainActivity) getActivity()).dismissProgressDialog();
        try {
            final PrettyDialog prettyDialog = new PrettyDialog(getActivity());
            prettyDialog.setCancelable(false);
            prettyDialog
                    .setTitle(s)
                    .setMessage("Payment Unsuccessful, Please try again")
                    .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                    .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            prettyDialog.dismiss();
                        }
                    })
                    .show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId)
        {
            case R.id.r_y:
                binding.lyrVerification.setVisibility(View.VISIBLE);
                if (!preferenceManager.getStringPreference(PreferenceManager.KEY_ADHAAR_ENABLED).equalsIgnoreCase("yes"))
                    showDialogS01();

                break;
            case R.id.r_n:
                binding.lyrVerification.setVisibility(View.GONE);
                if(empDataItem==null)
                {
                    return;
                }
                if (empDataItem.getWantAdharVerification().equalsIgnoreCase("y"))
                    showDialogS02();

                break;
            case R.id.emp_no_manually_y:
                binding.lyrAutoGeneratedWill.setVisibility(View.GONE);
                if(empDataItem==null)
                {
                    return;
                }
                if(empDataItem.getEmployeeNumberType().equalsIgnoreCase("N"))
                {

                    final PrettyDialog prettyDialog = new PrettyDialog(getActivity());
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setMessage("Old employee number will not get affected by this.\nAre you sure?")
                            .setIcon(R.drawable.info, R.color.white, null)
                            .addButton("Yes", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {

                                    prettyDialog.dismiss();
                                }
                            })
                            .addButton("No", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                    binding.empNoManuallyN.setChecked(true);
                                }
                            })
                            .show();
                }

                break;
            case R.id.emp_no_manually_n:
                binding.lyrAutoGeneratedWill.setVisibility(View.VISIBLE);
                setAutoGeneratedEmpNo();
                if(empDataItem==null)
                {
                    return;
                }
                if(empDataItem.getEmployeeNumberType().equalsIgnoreCase("Y"))
                {

                    final PrettyDialog prettyDialog = new PrettyDialog(getActivity());
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setMessage("Old employee number will not get affected by this.\nAre you sure?")
                            .setIcon(R.drawable.info, R.color.white, null)
                            .addButton("Yes", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {

                                    prettyDialog.dismiss();
                                }
                            })
                            .addButton("No", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                    binding.empNoManuallyY.setChecked(true);
                                }
                            })
                            .show();


                }

                break;
        }
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
