package com.miscos.staffhandler.employer_java.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.FragmentForm1Binding;
import com.miscos.staffhandler.employer_java.activity.MainActivity;
import com.miscos.staffhandler.employer_java.activity.MapActivity;
import com.miscos.staffhandler.employer_java.remote.NetworkService;
import com.miscos.staffhandler.employer_java.remote.model.NormalResponse;
import com.miscos.staffhandler.employer_java.utils.SnackBarHelper;



import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Form1Fragment extends Fragment {

    FragmentForm1Binding binding;
    double lat, lng;
    String address;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_form1,
                container,
                false
        );

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                requireActivity(),
                R.layout.layout_spinner_item,
                getResources().getStringArray(R.array.business_type));

        binding.businessType.setAdapter(adapter);

        binding.setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnackBarHelper.snackBarMessage(requireActivity(), "Select Office Location on Map");
                Intent intent = new Intent(requireActivity(), MapActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        binding.tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://staffhandler.com/production/mini_staff_handler/admin_panel/tnc.html";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.name.getText().toString().isEmpty()) {
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please enter first name"
                    );
                } else if (binding.surName.getText().toString().isEmpty()) {
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please enter last name"
                    );
                } else if (binding.mobile.getText().toString().isEmpty()) {
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please enter mobile number"
                    );
                } else if (binding.mobile.getText().toString().length() < 10) {
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please enter a valid mobile number");
                } else if (binding.mail.getText().toString().isEmpty()) {
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please enter email address"
                    );
                } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.mail.getText().toString()).matches()) {
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please enter a valid email address");
                } else if (binding.businessType.getSelectedItem().toString().equalsIgnoreCase("Business Type")) {
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please select business type");
                } else if (binding.companyName.getText().toString().isEmpty()) {
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please enter company name"
                    );
                } else if (binding.male.isChecked() && binding.female.isChecked() && binding.other.isChecked()) {
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please Select Gender");
                } else if (binding.officeAddress.getText().toString().equalsIgnoreCase("Office Address")) {
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please enter office address"
                    );
                } else if (!(lat != 0.0 && lng != 0.0)) {
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please select office location on map"
                    );
                } else if (!binding.cbTerms.isChecked()) {
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please agree with terms and conditions"
                    );
                } else {
                    callAPI();
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            lat = data.getDoubleExtra("lat", 0.0);
            lng = data.getDoubleExtra("lon", 0.0);
            address = data.getStringExtra("address");
            binding.officeAddress.setText(address);
        }
    }

    private void callAPI() {
        String gender = "";
        if (binding.male.isChecked())
            gender = "male";
        else if (binding.female.isChecked())
            gender = "female";
        else
            gender = "other";

        String location = lat + "," + lng;
        ((MainActivity) getActivity()).showProgressDialog();
        NetworkService.getInstance().api().postForm1(binding.spinnerTitle.getSelectedItem().toString()+
                        " "+binding.name.getText().toString(),
                binding.surName.getText().toString(),
                "insert",
                binding.mobile.getText().toString(),
                binding.businessType.getSelectedItem().toString(),
                binding.companyName.getText().toString(),
                binding.age.getText().toString(),
                gender,
                binding.mail.getText().toString(),
                location,
                binding.officeAddress.getText().toString(),
                binding.officeAddressLand.getText().toString())
                .enqueue(new Callback<NormalResponse>() {
                    @Override
                    public void onResponse(Call<NormalResponse> call, Response<NormalResponse> it) {
                        ((MainActivity) getActivity()).dismissProgressDialog();
                        if (it.body()!=null)
                        {
                           // SnackBarHelper.snackBarMessage(requireActivity(), it.body().getMessage());
                            if (it.body().getStatus() == 101)
                            {
                                Dialog dialog = new Dialog(requireActivity());
                                dialog.setContentView(R.layout.layout_success_dialog);
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
                                        requireActivity().finish();
                                    }
                                });
                                dialog.show();
                            }
                            else
                            {
                                SnackBarHelper.snackBarMessage(requireActivity(),it.body().getMessage()+"");
                            }
                        } else SnackBarHelper.snackBarMessage(requireActivity(), getString(R.string.something_wrong));
                    }

                    @Override
                    public void onFailure(Call<NormalResponse> call, Throwable t) {
                        SnackBarHelper.snackBarMessage(requireActivity(), getString(R.string.something_wrong));
                        ((MainActivity) getActivity()).dismissProgressDialog();
                    }
                });
    }
}
