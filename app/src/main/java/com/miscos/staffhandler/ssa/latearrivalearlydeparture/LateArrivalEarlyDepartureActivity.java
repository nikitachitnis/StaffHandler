package com.miscos.staffhandler.ssa.latearrivalearlydeparture;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.ActivityLateArrivalEarlyDepartureBinding;
import com.miscos.staffhandler.employer_java.remote.NetworkService;
import com.miscos.staffhandler.employer_java.remote.model.NormalResponse;
import com.miscos.staffhandler.employer_java.utils.SnackBarHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LateArrivalEarlyDepartureActivity extends AppCompatActivity {

    ActivityLateArrivalEarlyDepartureBinding binding;
    String employerId;
    static ProgressDialog progressDialog;

    public static void showProgress(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog = ProgressDialog.show(context, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();
    }

    public static void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_late_arrival_early_departure);
        employerId = "Shin3";
        setData();
        binding.txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid())
                submitAPI();
            }
        });
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(binding.edtMaxDailyDelay.getText().toString())){
            SnackBarHelper.snackBarMessage(
                    LateArrivalEarlyDepartureActivity.this,
                    "Please Enter Max Daily Permitted Delay"
            );
            return false;
        }
        else if (TextUtils.isEmpty(binding.edtPenaltyRs.getText().toString())) {
            SnackBarHelper.snackBarMessage(
                    LateArrivalEarlyDepartureActivity.this,
                    "Please Enter Late Arrival Penalty Rs"
            );
            return false;
        }
        else if (TextUtils.isEmpty(binding.edtPermittedDepMin.getText().toString())) {
            SnackBarHelper.snackBarMessage(
                    LateArrivalEarlyDepartureActivity.this,
                    "Please Enter Early Departure Min"
            );
            return false;
        }
        else if (TextUtils.isEmpty(binding.edtEveryEarlyDpRs.getText().toString())) {
            SnackBarHelper.snackBarMessage(
                    LateArrivalEarlyDepartureActivity.this,
                    "Please Enter Penalty For Early Departure"
            );
            return false;
        }
        else
            return true;
    }

    private void setData() {
        List<String> tempData = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            tempData.add(i + "");
        }

        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(
                this, R.layout.layout_spinner, tempData);

        spAdapter.setDropDownViewResource(R.layout.layout_spinner);

        binding.spEarlyHour.setAdapter(spAdapter);
        binding.spEarlyMin.setAdapter(spAdapter);
        binding.spEveryEarlyDp.setAdapter(spAdapter);
        binding.spHour.setAdapter(spAdapter);
        binding.spMaxEarlyDpMonth.setAdapter(spAdapter);
        binding.spMin.setAdapter(spAdapter);
        binding.spPenaltyEvery.setAdapter(spAdapter);
        binding.spMaxMonthlyDelay.setAdapter(spAdapter);
    }

    private void submitAPI() {

        showProgress(this);
        String autoReflectSalary,permittedDepartureBefore;

        if (binding.rdGroupSalary.getCheckedRadioButtonId() == R.id.rd_sal_yes)
            autoReflectSalary = "Y";
        else
            autoReflectSalary = "N";

        if (binding.rdGroupDpBefore.getCheckedRadioButtonId() == R.id.rd_dp_yes)
            permittedDepartureBefore = "Y";
        else
            permittedDepartureBefore = "N";

        NetworkService.getInstance().api().saveLateArrivalEarlyDeparture("add",
                binding.edtMaxDailyDelay.getText().toString(),
                binding.spMaxMonthlyDelay.getSelectedItem().toString(),
                binding.spPenaltyEvery.getSelectedItem().toString(),
                binding.edtPenaltyRs.getText().toString(),
                binding.spHour.getSelectedItem().toString(),
                binding.edtPermittedDepMin.getText().toString(),
                binding.spMaxEarlyDpMonth.getSelectedItem().toString(),
                binding.spEveryEarlyDp.getSelectedItem().toString(),
                binding.edtEveryEarlyDpRs.getText().toString(),
                binding.spEarlyMin.getSelectedItem().toString(),
                "Y", binding.spNotification.getSelectedItem().toString(),
                autoReflectSalary, employerId).enqueue(new Callback<List<NormalResponse>>() {
            @Override
            public void onResponse(Call<List<NormalResponse>> call, Response<List<NormalResponse>> response) {
                hideProgress();
                if (response.body() != null && response.body().size() > 0) {
                    SnackBarHelper.snackBarMessage(LateArrivalEarlyDepartureActivity.this, response.body().get(0).getMessage());
                }
                }

            @Override
            public void onFailure(Call<List<NormalResponse>> call, Throwable t) {
                hideProgress();
                SnackBarHelper.snackBarMessage(LateArrivalEarlyDepartureActivity.this, t.getMessage());
            }
        });
    }
}