package com.miscos.staffhandler.employer_java.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.miscos.staffhandler.hrms_management.hrms.utility.Helper;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.ActivityAddHolidayBinding;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.miscos.staffhandler.employer_java.customview.MultiSpinner;
import com.miscos.staffhandler.employer_java.remote.NetworkService;
import com.miscos.staffhandler.employer_java.remote.model.GetJsonResponse;
import com.miscos.staffhandler.employer_java.remote.model.NormalResponse;
import com.miscos.staffhandler.employer_java.remote.model.fetchdataform2.HolidayData;
import com.miscos.staffhandler.employer_java.utils.SnackBarHelper;


import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddHolidayActivity extends AppCompatActivity {



    ActivityAddHolidayBinding binding;
    String fromDate = "", toDate = "";
    String empId;
    private File selectedFile;
    String imgPath;
    String sdf = "dd-MM-yyyy";
    boolean[] selectedArray;
    String[] holidaysType;
    String[] shiftData = new String[]{"B", "O", "S1", "S2", "S3"};
    boolean isEdit;
    HolidayData data;
    int currYear;

    PreferenceManager preferenceManager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedArray = new boolean[shiftData.length];
        Arrays.fill(selectedArray, false);

        if (savedInstanceState == null) {
            empId = getIntent().getStringExtra("id");
            currYear = getIntent().getIntExtra("year", 0);
        } else {
            empId = savedInstanceState.getString("id", "");
            currYear = savedInstanceState.getInt("year", 0);
            data = (HolidayData) savedInstanceState.getParcelable("data");
            if (data != null)
            {
                isEdit = true;
                try {
                    String from = new SimpleDateFormat(sdf).format(new SimpleDateFormat("dd-MMM-yyyy").parse(data.getFromDate() + "-" + currYear));
                    String to = new SimpleDateFormat(sdf).format(new SimpleDateFormat("dd-MMM-yyyy").parse(data.getToDate() + "-" + currYear));
                    binding.txtStartDate.setText(from);
                    binding.txtEndDate.setText(to);
                    fromDate = from;
                    toDate = to;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                binding.edtHolidayName.setText(data.getHolidayName());
                int i = 0;
                for (i = 0; i < holidaysType.length; i++) {
                    if (holidaysType[i].equalsIgnoreCase(data.getHolidayType()))
                        break;
                }
                if (i == holidaysType.length)
                    i = 0;
                selectedArray = new boolean[shiftData.length];
                if(data.getAppliedOn().contains("&"))
                {
                    String[] shiftArray = data.getAppliedOn().split("&");
                    for (int j = 0; j < shiftData.length; j++)
                    {
                        for (String s : shiftArray)
                        {
                            if (shiftData[j].equalsIgnoreCase(s)) {
                                selectedArray[j] = true;
                                break;
                            }
                        }
                    }
                }
                else if(!data.getAppliedOn().isEmpty())
                {
                    for (int j = 0; j < shiftData.length; j++)
                    {
                            if (shiftData[j].equalsIgnoreCase(data.getAppliedOn()))
                            {
                                selectedArray[j] = true;
                                break;
                            }
                    }
                }


                binding.spHolidayType.setSelection(i);
                binding.edtHolidayMessage.setText(data.getMessageForEmployees());
                Glide.with(this)
                        .load(data.getHolidayImage()).apply(new RequestOptions().fitCenter())
                        .placeholder(R.drawable.default_place_holder)
                        .into(binding.imgHoliday);
            }
        }
        preferenceManager = new PreferenceManager(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_holiday);
        binding.title.setText("Configuring Holidays for year " + currYear);
        holidaysType = getResources().getStringArray(R.array.holiday_types);
        if (getIntent() != null) {
            data = (HolidayData) getIntent().getParcelableExtra("data");
            if (data != null) {
                isEdit = true;
                try {
                    String from = new SimpleDateFormat(sdf).format(new SimpleDateFormat("dd-MMM-yyyy").parse(data.getFromDate() + "-" + currYear));
                    String to = new SimpleDateFormat(sdf).format(new SimpleDateFormat("dd-MMM-yyyy").parse(data.getToDate() + "-" + currYear));
                    binding.txtStartDate.setText(from);
                    binding.txtEndDate.setText(to);
                    fromDate = from;
                    toDate = to;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                binding.edtHolidayName.setText(data.getHolidayName());
                int i = 0;
                for (i = 0; i < holidaysType.length; i++) {
                    if (holidaysType[i].equalsIgnoreCase(data.getHolidayType()))
                        break;
                }
                if (i == holidaysType.length)
                    i = 0;
                binding.spHolidayType.setSelection(i);
                selectedArray = new boolean[shiftData.length];
                String[] shiftArray = data.getAppliedOn().split("&");
                for (int j = 0; j < shiftData.length; j++) {
                    for (String s : shiftArray) {
                        if (shiftData[j].equalsIgnoreCase(s)) {
                            selectedArray[j] = true;
                            break;
                        }
                    }
                }
                binding.edtHolidayMessage.setText(data.getMessageForEmployees());
                Glide.with(this)
                        .load(data.getHolidayImage()).apply(new RequestOptions().fitCenter())
                        .placeholder(R.drawable.default_place_holder)
                        .into(binding.imgHoliday);
            }
        }
        setProgressDialog();
        if(isEdit)
        {
            binding.txtremove.setVisibility(View.VISIBLE);
            binding.txtheading.setText("Edit Holiday");
        }
        else
        {
            binding.txtremove.setVisibility(View.GONE);
            binding.txtheading.setText("Add Holiday");

        }

        binding.txtremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrettyDialog prettyDialog = new PrettyDialog(AddHolidayActivity.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle(AddHolidayActivity.this.getString(R.string.app_name))
                        .setMessage("Are you sure to remove this holiday?")
                        .setIcon(R.drawable.ic_done, R.color.color_white, null)
                        .addButton("Yes", R.color.color_white, R.color.blue, new PrettyDialogCallback() {
                            @Override
                            public void onClick()
                            {

                                prettyDialog.dismiss();
                                //remove
                                remove_holiday_api(data.getHolidayId(),data.getFromDate());
                            }
                        })
                        .addButton("No", R.color.color_white, R.color.blue, new PrettyDialogCallback() {
                            @Override
                            public void onClick()
                            {
                                prettyDialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        binding.imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(AddHolidayActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    submitHolidayAPI();
                }
            }
        });

        binding.txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddHolidayActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                try {
                                    if (TextUtils.isEmpty(toDate) || new SimpleDateFormat("dd-MM-yyyy").parse(toDate).after(new SimpleDateFormat("dd-MM-yyyy").parse( dayOfMonth + "-" + (monthOfYear + 1) + "-" + year))||new SimpleDateFormat("dd-MM-yyyy").parse(toDate).equals(new SimpleDateFormat("dd-MM-yyyy").parse( dayOfMonth + "-" + (monthOfYear + 1) + "-" + year))) {
                                        if (monthOfYear<9)
                                            fromDate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
                                        else
                                            fromDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                        binding.txtStartDate.setText(fromDate);
                                        binding.txtEndDate.setText(fromDate);
                                    } else SnackBarHelper.snackBarMessage(AddHolidayActivity.this,"From date must be before to date");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, mYear, mMonth, mDay);
                try {
                    if (currYear == Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date())))
                        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                    else
                        datePickerDialog.getDatePicker().setMinDate(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-" + currYear).getTime());
                    datePickerDialog.getDatePicker().setMaxDate(new SimpleDateFormat("dd-MM-yyyy").parse("31-12-" + currYear).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datePickerDialog.show();
            }
        });
        binding.txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddHolidayActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                try {
                                    if (TextUtils.isEmpty(fromDate) || new SimpleDateFormat("dd-MM-yyyy").parse(fromDate).before(new SimpleDateFormat("dd-MM-yyyy").parse( dayOfMonth + "-" + (monthOfYear + 1) + "-" + year))||new SimpleDateFormat("dd-MM-yyyy").parse(fromDate).equals(new SimpleDateFormat("dd-MM-yyyy").parse( dayOfMonth + "-" + (monthOfYear + 1) + "-" + year))) {
                                        if (monthOfYear<9)
                                            toDate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
                                        else
                                            toDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                        binding.txtEndDate.setText(toDate);
                                    } else SnackBarHelper.snackBarMessage(AddHolidayActivity.this,"To date must be after from date");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, mYear, mMonth, mDay);
                try {
                    if (currYear == Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date())))
                        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                    else
                        datePickerDialog.getDatePicker().setMinDate(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-" + currYear).getTime());
                    datePickerDialog.getDatePicker().setMaxDate(new SimpleDateFormat("dd-MM-yyyy").parse("31-12-" + currYear).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datePickerDialog.show();
            }
        });

        binding.lyrBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        int shiftcount=Integer.parseInt(preferenceManager.getStringPreference(PreferenceManager.KEY_SHIFT_COUNT));
        List<Integer> disabled = new ArrayList<>();
        for(int i=(shiftcount+2);i<=4;i++)
        {
            disabled.add(i);
        }

        binding.spAppliedOn.setItems(Arrays.asList(getResources().getStringArray(R.array.holiday_applied_on)), selectedArray, disabled, "Please select shift", new MultiSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                selectedArray = selected;
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    private void submitHolidayAPI() {
        RequestBody requestFile;
        MultipartBody.Part imageBody;
        StringBuilder appliedOn = new StringBuilder();
        String prefix = "";
        for (int i = 0; i < selectedArray.length; i++) {
            if (selectedArray[i]) {
                appliedOn.append(prefix);
                prefix = "&";
                appliedOn.append(shiftData[i]);
            }
        }
        if (appliedOn.length() > 1)
            appliedOn.substring(0, appliedOn.length() - 1);

        if (TextUtils.isEmpty(appliedOn.toString())) {
            SnackBarHelper.snackBarMessage(this, "Please select a shift");
            return;
        }


        if (selectedFile != null) {
            requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), selectedFile);
            imageBody =
                    MultipartBody.Part.createFormData("holiday_image", selectedFile.getName(), requestFile);
        } else {
            requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), "");
            imageBody =
                    MultipartBody.Part.createFormData("holiday_image", "", requestFile);
        }
        RequestBody mode =
                RequestBody.create(MediaType.parse("multipart/form-data"), "add_holiday");
        RequestBody name =
                RequestBody.create(MediaType.parse("multipart/form-data"), binding.edtHolidayName.getText().toString());
        RequestBody from =
                RequestBody.create(MediaType.parse("multipart/form-data"), fromDate);
        RequestBody to =
                RequestBody.create(MediaType.parse("multipart/form-data"), binding.txtEndDate.getText().toString());
        RequestBody id =
                RequestBody.create(MediaType.parse("multipart/form-data"), empId);
        RequestBody holiday_for =
                RequestBody.create(MediaType.parse("multipart/form-data"), appliedOn.toString());
        RequestBody type =
                RequestBody.create(MediaType.parse("multipart/form-data"), binding.spHolidayType.getSelectedItem().toString());
        RequestBody msg =
                RequestBody.create(MediaType.parse("multipart/form-data"), binding.edtHolidayMessage.getText().toString());
        showProgressDialog();
        if (isEdit)
        {
            Call<NormalResponse> api;
            RequestBody holidayId = RequestBody.create(MediaType.parse("multipart/form-data"), data.getHolidayId());
            mode = RequestBody.create(MediaType.parse("multipart/form-data"), "edit_holiday");

            if (selectedFile == null)
                api = NetworkService.getInstance().api().editHolidayWithoutImage(mode, name, from, to, id, holiday_for, type, holidayId, msg);
            else
                api = NetworkService.getInstance().api().editHoliday(mode, name, from, to, id, holiday_for, type, holidayId, msg, imageBody);

            api.enqueue(new Callback<NormalResponse>() {
                @Override
                public void onResponse(Call<NormalResponse> call, Response<NormalResponse> response) {
                    dismissProgressDialog();
                    if (response.body() != null) {
                        SnackBarHelper.snackBarMessage(AddHolidayActivity.this, response.body().getMessage());
                        if (response.body().getStatus() == 101) {
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    } else
                        SnackBarHelper.snackBarMessage(AddHolidayActivity.this, getString(R.string.something_wrong));
                }

                @Override
                public void onFailure(Call<NormalResponse> call, Throwable t) {
                    dismissProgressDialog();
                    SnackBarHelper.snackBarMessage(AddHolidayActivity.this, t.getMessage() + " ");
                }
            });
        } else {
            NetworkService.getInstance().api().addHoliday(mode, name, from, to, id, holiday_for, type, msg, imageBody).enqueue(new Callback<NormalResponse>() {
                @Override
                public void onResponse(Call<NormalResponse> call, Response<NormalResponse> response) {
                    dismissProgressDialog();
                    if (response.body() != null) {
                        SnackBarHelper.snackBarMessage(AddHolidayActivity.this, response.body().getMessage());
                        if (response.body().getStatus() == 101) {
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    } else
                        SnackBarHelper.snackBarMessage(AddHolidayActivity.this, getString(R.string.something_wrong));
                }

                @Override
                public void onFailure(Call<NormalResponse> call, Throwable t) {
                    dismissProgressDialog();
                    SnackBarHelper.snackBarMessage(AddHolidayActivity.this, t.getMessage() + " ");
                }
            });
        }
    }

    private boolean isValid()
    {
        if (TextUtils.isEmpty(binding.edtHolidayName.getText().toString())) {
            SnackBarHelper.snackBarMessage(this, "Please enter holiday name");
            return false;
        } else if (TextUtils.isEmpty(fromDate)) {
            SnackBarHelper.snackBarMessage(this, "Please select holiday date");
            return false;
        } else return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("id", empId);
        outState.putParcelable("data", data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            if (data != null) {
                selectedFile = ImagePicker.Companion.getFile(data);
                String filePath = ImagePicker.Companion.getFilePath(data);
                Log.e("onActivityResult", selectedFile.getAbsolutePath());
                imgPath = filePath;
                Glide.with(AddHolidayActivity.this)
                        .load(filePath).apply(new RequestOptions().fitCenter())
                        .placeholder(R.drawable.default_place_holder)
                        .into(binding.imgHoliday);
            }
        }
    }

    private void setProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog = ProgressDialog.show(this, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.dismiss();
    }

    public void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(isEdit)
        getMenuInflater().inflate(R.menu.remove_holiday,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId()==R.id.action_remove)
        {

        }
        return true;

    }
    private void remove_holiday_api(String holiday_id,String from_date)
    {
        Helper.showProgress(this);
        NetworkService.getInstance().api().remove_holiday("remove_holiday", empId, holiday_id,from_date)
                .enqueue(new Callback<GetJsonResponse>()
                {
                    @Override
                    public void onResponse(Call<GetJsonResponse> call, retrofit2.Response<GetJsonResponse> it)
                    {
                    Helper.hideProgress();
                        if (it.body() != null)
                        {
                            if(it.body().getErrorCode().equalsIgnoreCase("101"))
                            {
                                Toast.makeText(AddHolidayActivity.this, "Removed Successfully!", Toast.LENGTH_SHORT).show();
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            }

                        } else
                        {
                            SnackBarHelper.snackBarMessage(AddHolidayActivity.this,"Failed to get reponse from server");

                        }

                    }

                    @Override
                    public void onFailure(Call<GetJsonResponse> call, Throwable t)
                    {
                        Log.e("fetchDataForm2", t.getMessage());
                        Helper.hideProgress();
                        SnackBarHelper.snackBarMessage(AddHolidayActivity.this, getString(R.string.something_wrong));
                    }
                });
    }
}