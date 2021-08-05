package com.miscos.staffhandler.employer_java.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavArgument;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.ActivityMainEmployerBinding;
import com.miscos.staffhandler.employee.employeemodule.Activity_PinLogin;
import com.miscos.staffhandler.employee.employeemodule.EmployeeActivity;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.miscos.staffhandler.employer_java.remote.NetworkService;
import com.miscos.staffhandler.employer_java.remote.model.NormalResponse;
import com.miscos.staffhandler.employer_java.utils.SnackBarHelper;
import com.razorpay.PaymentResultListener;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector , PaymentResultListener {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private String type, employerId, password;
    private ActivityMainEmployerBinding binding;
    NavHostFragment navHostFragment;
    private ProgressDialog progressDialog;
    PreferenceManager preferenceManager;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("type", type);
        outState.putString("employerId", employerId);
        outState.putString("password", password);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_employer);
        preferenceManager=new PreferenceManager(this);
        if (savedInstanceState!=null) {
            type = savedInstanceState.getString("type");
            employerId = savedInstanceState.getString("employerId");
            password = savedInstanceState.getString("password");
        } else {
            type = getIntent().getStringExtra("type");
            employerId = getIntent().getStringExtra("employerId");
            password = getIntent().getStringExtra("password");
        }
        setProgressDialog();

        navHostFragment = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.source_container));
        NavInflater inflater = navHostFragment.getNavController().getNavInflater();

        NavGraph graph = inflater.inflate(R.navigation.main);

        switch (type) {
            case "form2":
                graph.setStartDestination(R.id.form2Fragment);
                NavArgument navArgument1 = new NavArgument.Builder().setDefaultValue(employerId).build();
                NavArgument navArgument2 = new NavArgument.Builder().setDefaultValue(password).build();
                graph.addArgument("employerId", navArgument1);
                graph.addArgument("password", navArgument2);
                break;
            case "form3":
                graph.setStartDestination(R.id.form3Fragment);
                NavArgument navArgument3 = new NavArgument.Builder().setDefaultValue(employerId).build();
                graph.addArgument("employerId", navArgument3);
                break;

            default:
                graph.setStartDestination(R.id.form1Fragment);
        }

        navHostFragment.getNavController().setGraph(graph);

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

    public void showProgressDialog(){
        if (progressDialog!=null && !progressDialog.isShowing())
            progressDialog.show();
    }

    public void dismissProgressDialog(){
        if (progressDialog!=null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        if (navHostFragment.getNavController().getCurrentDestination()==null || navHostFragment.getNavController().getCurrentDestination().getId() == R.id.holidayList) {
            super.onBackPressed();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit");
            builder.setMessage("Do You Want To Exit");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    MainActivity.super.onBackPressed();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }
    @Override
    public void onPaymentSuccess(String s)
    {
        // payment successfull pay_DGU19rDsInjcF
        Log.e("TAG", " payment successful " + s.toString());
        NetworkService.getInstance().api().form3PaymentAPI(
                "pay_for_adhar_verification", employerId, preferenceManager.getStringPreference(PreferenceManager.KEY_ADHAR_LICENSECOUNT), preferenceManager.getStringPreference(PreferenceManager.KEY_ADHAR_LICENSE_AMOUNT), s, s, "Success"
        ).enqueue(new Callback<NormalResponse>() {
            @Override
            public void onResponse(Call<NormalResponse> call, Response<NormalResponse> it) {
                //    ((MainActivity) getActivity()).dismissProgressDialog();
                if (it.body() != null)
                {
                    final PrettyDialog prettyDialog = new PrettyDialog(MainActivity.this);
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setTitle(s)
                            .setMessage("Your payment for Adhar verification license  is successful")
                            .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                            .addButton("Ok", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback()
                            {
                                @Override
                                public void onClick()
                                {
                                    prettyDialog.dismiss();
                                    preferenceManager.setPreference(PreferenceManager.KEY_ADHAR_LICENSE_AMOUNT,"0");
                                    preferenceManager.setPreference(PreferenceManager.KEY_ADHAR_LICENSECOUNT,"0");
                                    finish();
                                }
                            })
                            .show();


                } else
                    SnackBarHelper.snackBarMessage(MainActivity.this, getString(R.string.something_wrong));

            }

            @Override
            public void onFailure(Call<NormalResponse> call, Throwable t) {
                //       ((MainActivity) getActivity()).dismissProgressDialog();
                SnackBarHelper.snackBarMessage(MainActivity.this, getString(R.string.something_wrong));
            }
        });

    }

    @Override
    public void onPaymentError(int i, String s
    ) {
        try {
            final PrettyDialog prettyDialog = new PrettyDialog(MainActivity.this);
            prettyDialog.setCancelable(false);
            prettyDialog
                    .setTitle(s)
                    .setMessage("Payment Unsuccessful, Please try again")
                    .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                    .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback()
                    {
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
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
