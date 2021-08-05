package com.miscos.staffhandler.employer_java.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.ActivityHolidayMangementBinding;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.miscos.staffhandler.employer_java.adapter.HolidayAdapter;
import com.miscos.staffhandler.employer_java.remote.NetworkService;
import com.miscos.staffhandler.employer_java.remote.model.fetchdataform2.FetchResponseForm2;
import com.miscos.staffhandler.employer_java.remote.model.fetchdataform2.HolidayData;
import com.miscos.staffhandler.employer_java.utils.SnackBarHelper;


import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class HolidayManagement extends AppCompatActivity implements HolidayAdapter.CallBack {

    ActivityHolidayMangementBinding binding;
    List<HolidayData> holidayDataList;
    HolidayAdapter mAdapter;
    String empId;
    PreferenceManager preferenceManager;
    private ProgressDialog progressDialog;
    int currYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_holiday_mangement);
        preferenceManager=new PreferenceManager(this);
        if (savedInstanceState == null) {
            empId = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID);
            currYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        } else {
            empId = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID);
            currYear = savedInstanceState.getInt("currYear");
        }
        setProgressDialog();
        binding.recyclerView.setAdapter(mAdapter = new HolidayAdapter(this));
        binding.txtAddHoliday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(HolidayManagement.this, AddHolidayActivity.class)
                                .putExtra("id", empId)
                                .putExtra("year", currYear)
                        , 7);
            }
        });
        binding.txtPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currYear--;
                fetchApiCall();
            }
        });
        binding.txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currYear++;
                fetchApiCall();
            }
        });
        binding.lyrBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        fetchApiCall();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("empId", empId);
        outState.putSerializable("currYear", currYear);
    }

    @Override
    public void onItemEditClick(HolidayData item) {
        startActivityForResult(new Intent(this, AddHolidayActivity.class)
                        .putExtra("data", item)
                        .putExtra("id", empId)
                        .putExtra("year", currYear)
                , 7);
    }

    private void fetchApiCall()
    {
        showProgressDialog();
        NetworkService.getInstance().api().fetchDataForm2("fetch", empId, currYear + "")
                .enqueue(new Callback<FetchResponseForm2>() {
                    @Override
                    public void onResponse(Call<FetchResponseForm2> call, Response<FetchResponseForm2> it) {
                        dismissProgressDialog();
                        if (it.body() != null)
                        {
                            holidayDataList = it.body().getHolidayData();

                            if (currYear >= Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()))) {
                                binding.txtAddHoliday.setVisibility(View.VISIBLE);
                                if (holidayDataList != null)
                                {
                                    Date now = new Date();
                                    for (int i = 0; i < holidayDataList.size(); i++) {
                                        try {
                                            if (new SimpleDateFormat("yyyy-MM-dd").parse(holidayDataList.get(i).getFromDate()).before(now))
                                                holidayDataList.get(i).setEditable(false);
                                            else
                                                holidayDataList.get(i).setEditable(true);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } else {
                                binding.txtAddHoliday.setVisibility(View.GONE);
                                if (holidayDataList != null)
                                    for (int i = 0; i < holidayDataList.size(); i++) {
                                        holidayDataList.get(i).setEditable(false);
                                    }
                            }
                            binding.txtNext.setText("Next Holidays " + (currYear + 1));
                            binding.txtPrevious.setText("Previous Holidays " + (currYear - 1));
                            binding.title.setText("Configured Holidays for Year " + currYear);
                            Log.e("fetchDataForm2", it.body().getMessage());
                            mAdapter.submitList(holidayDataList);
                            if(holidayDataList.size()==0)
                            {
                                binding.txtnodatadfound.setVisibility(View.VISIBLE);
                                binding.recyclerView.setVisibility(View.GONE);

                            }
                            else
                            {
                                binding.txtnodatadfound.setVisibility(View.GONE);
                                binding.recyclerView.setVisibility(View.VISIBLE);
                            }
                        } else
                        {
                            SnackBarHelper.snackBarMessage(HolidayManagement.this, getString(R.string.something_wrong));
                            binding.txtnodatadfound.setVisibility(View.VISIBLE);
                            binding.recyclerView.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFailure(Call<FetchResponseForm2> call, Throwable t) {
                        Log.e("fetchDataForm2", t.getMessage());
                        dismissProgressDialog();
                        SnackBarHelper.snackBarMessage(HolidayManagement.this, getString(R.string.something_wrong));
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK)
        {
            fetchApiCall();
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
}