package com.miscos.staffhandler.employer_java.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.databinding.FragmentForm2Binding;
import com.miscos.staffhandler.employee.employeemodule.Activity_PinLogin;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.miscos.staffhandler.employer_java.activity.MainActivity;
import com.miscos.staffhandler.employer_java.activity.MapActivity;
import com.miscos.staffhandler.employer_java.remote.NetworkService;
import com.miscos.staffhandler.employer_java.remote.model.NormalResponse;
import com.miscos.staffhandler.employer_java.remote.model.fetchdataform2.FetchResponseForm2;
import com.miscos.staffhandler.employer_java.utils.SnackBarHelper;



import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Form2Fragment extends Fragment {

    private FragmentForm2Binding binding;

    private Double lat;
    private Double lon;
    private String address;

    private String employerId = null;
    private String password = null;

    private String newQrType = "";
    private String qrType,old_attendance_type = "";
    private String contact = "";
    private boolean manualOverride;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_form2,
                container,
                false
        );
        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("employerId", employerId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && resultCode == Activity.RESULT_OK) {
            lat = data.getDoubleExtra("lat", 0.0);
            lon = data.getDoubleExtra("lon", 0.0);
            address = data.getStringExtra("address");
            binding.officeAddress.setText(address);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            employerId = savedInstanceState.getString("employerId");
            password = savedInstanceState.getString("password");
        } else {
            employerId = Form2FragmentArgs.fromBundle(getArguments()).getEmployerId();
            password = Form2FragmentArgs.fromBundle(getArguments()).getPassword();
        }

        Log.e("Form2Fragment", employerId + " " + password);
        if (employerId != null && password != null)
            fetchDataAPI();

        binding.qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.qrCode.isChecked())
                    showQrTypeDialog(qrType);
            }
        });

        binding.gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if (binding.gps.isChecked()&&old_attendance_type.equalsIgnoreCase("gps_with_wifi"))
                    showGPSWIFItoQR();
            }
        });

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.companyName.getText().toString().isEmpty())
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please enter Company Name"
                    );
                else if (binding.businessType.getSelectedItemPosition()==0)
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please select Business Area"
                    );
                else if (binding.name.getText().toString().isEmpty())
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please enter Name"
                    );
                else if (binding.mobile.getText().toString().isEmpty())
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please enter Mobile Number"
                    );
                else if (binding.mobile.getText().toString().length() < 10)
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Mobile Number should be atleast 10 digits."
                    );
                else if (binding.mail.getText().toString().isEmpty())
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please enter Email Address"
                    );
                else if (!Patterns.EMAIL_ADDRESS.matcher(binding.mail.getText().toString()).matches())
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please enter a valid Email Address"
                    );
                else if (binding.noEmployee.getText().toString().isEmpty())
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please enter No of Employees"
                    );
                else if (!binding.qrCode.isChecked() && !binding.gps.isChecked() && !binding.gpsWithWifi.isChecked())
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please enter Attendance Type"
                    );
                else if (binding.qrCode.isChecked() && newQrType.isEmpty()) {
                    showQrTypeDialog(qrType);
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Qr type cannot be empty"
                    );
                } else if (binding.gps.isChecked() && newQrType.isEmpty()) {
                    showQrTypeDialog(qrType);
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Qr type cannot be empty"
                    );
                } else if (binding.officeAddress.getText().toString().isEmpty())
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please enter office address"
                    );
                else if (!(lat != null && lon != null && lat != 0.0 && lon != 0.0))
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please select office location on map"
                    );
                else if (binding.distance.getText().toString().isEmpty())
                    SnackBarHelper.snackBarMessage(
                            requireActivity(),
                            "Please enter Geo Permitted Distance"
                    );
                else {

                    final PrettyDialog prettyDialog= new PrettyDialog(getActivity());
                    prettyDialog.setCancelable(false);
                    prettyDialog
                            .setMessage("All the changes made will be saved. Are you sure?")
                            .setIcon(R.drawable.info,R.color.white,null)
                            .addButton("Yes", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                    postFormAPI();

                                }
                            })
                            .addButton("No", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                    requireActivity().finish();
                                }
                            })
                            .show();


                }
            }
        });

        binding.setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnackBarHelper.snackBarMessage(requireActivity(), "Select Office Location on Map");
                Intent intent = new Intent(requireActivity(), MapActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                startActivityForResult(intent, 100);
            }
        });
    }

    private void showQrTypeDialog(String qrType) {
        manualOverride = false;
        Dialog dialog = new Dialog(requireActivity());
        dialog.setContentView(R.layout.layout_qr_type);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        TextView action = dialog.findViewById(R.id.action);
        RadioButton staticQr = dialog.findViewById(R.id.staticQr);
        RadioButton digital = dialog.findViewById(R.id.digitalQr);
        if (qrType.equalsIgnoreCase("")) {
            staticQr.setChecked(true);
            digital.setEnabled(true);
            action.setText("Generate QR Code");
        } else if (qrType.equalsIgnoreCase("static")) {
            staticQr.setChecked(true);
            action.setText("Generate QR Code");
        } else if (qrType.equalsIgnoreCase("digital")) {
            digital.setChecked(true);
            action.setText("Change Password");
        }
        staticQr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !manualOverride) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                    builder.setMessage("Are you sure you want to change type from digital to static?")
                            .setPositiveButton(
                                    "Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            action.setText("Generate QR Code");
                                        }
                                    }
                            )
                            .setNegativeButton(
                                    "No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            manualOverride = true;
                                            staticQr.setChecked(false);
                                            digital.setChecked(true);
                                            manualOverride = false;
                                        }
                                    }
                            );
                    builder.create().show();
                }
            }
        });
        digital.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !manualOverride) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                    builder.setMessage("Are you sure you want to change type from static to digital?")
                            .setPositiveButton(
                                    "Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            action.setText("Generate Username and Password");
                                        }
                                    }
                            )
                            .setNegativeButton(
                                    "No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            manualOverride = true;
                                            staticQr.setChecked(true);
                                            digital.setChecked(false);
                                            manualOverride = false;
                                        }
                                    }
                            );
                    builder.create().show();
                }
            }
        });
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (action.getText().toString().equalsIgnoreCase("Generate QR Code")) {
                    generateQrCodeApi("abc@123");
                } else if (action.getText().toString().equalsIgnoreCase("Generate Username and Password")) {
                    setQrConfigApi("generate", contact);
                } else if (action.getText().toString().equalsIgnoreCase("Change Password")) {
                    setQrConfigApi("change", contact);
                }
            }
        });
        dialog.show();
    }

    void setQrConfigApi(String mode, String contact) {
        ((MainActivity) getActivity()).showProgressDialog();
        NetworkService.getInstance().api().setQrConfig(mode,contact,employerId)
                .enqueue(new Callback<NormalResponse>() {
                    @Override
                    public void onResponse(Call<NormalResponse> call, Response<NormalResponse> it) {
                        ((MainActivity) getActivity()).dismissProgressDialog();
                        if (it.body()!=null) {
                            SnackBarHelper.snackBarMessage(requireActivity(), it.body().getMessage());
                            if (it.body().getStatus() == 101) {
                                newQrType = "digital";
                            }
                            Log.e("setQrConfigApi", it.body().getMessage());
                        } else
                            SnackBarHelper.snackBarMessage(requireActivity(), getString(R.string.something_wrong));

                    }
                    @Override
                    public void onFailure(Call<NormalResponse> call, Throwable t) {
                        ((MainActivity) getActivity()).dismissProgressDialog();
                        SnackBarHelper.snackBarMessage(requireActivity(), getString(R.string.something_wrong));
                    }
                });
    }

    void generateQrCodeApi(String qrCode) {
        ((MainActivity) getActivity()).showProgressDialog();
        NetworkService.getInstance().api().generateQrCode("qrcode","static",employerId,password,qrCode)
        .enqueue(new Callback<NormalResponse>() {
            @Override
            public void onResponse(Call<NormalResponse> call, Response<NormalResponse> it) {
                ((MainActivity) getActivity()).dismissProgressDialog();
                if (it.body()!=null) {
                    SnackBarHelper.snackBarMessage(requireActivity(), it.body().getMessage());
                    if (it.body().getStatus() == 101) {
                        newQrType = "static";
                        showGenerateQrDialog(it.body().getQrscan_img());
                    }
                } else
                    SnackBarHelper.snackBarMessage(requireActivity(), getString(R.string.something_wrong));

            }

            @Override
            public void onFailure(Call<NormalResponse> call, Throwable t) {
                Log.e("generateQrCodeApi", t.getMessage() );
                ((MainActivity) getActivity()).dismissProgressDialog();
                SnackBarHelper.snackBarMessage(requireActivity(), getString(R.string.something_wrong));
            }
        });
    }

    private void fetchDataAPI() {
        ((MainActivity) getActivity()).showProgressDialog();
        String currYear = new SimpleDateFormat("yyyy").format(new Date());
        NetworkService.getInstance().api().fetchDataForm2("fetch", employerId, currYear)
                .enqueue(new Callback<FetchResponseForm2>() {
                    @Override
                    public void onResponse(Call<FetchResponseForm2> call, Response<FetchResponseForm2> it) {
                        ((MainActivity) getActivity()).dismissProgressDialog();
                        if (it.body()!=null)
                        {
                            Log.e("fetchDataForm2", it.body().getMessage());
                            if (it.body().getEmployeeData().size() > 0) {
                               binding.setData(it.body().getEmployeeData().get(0));
                                qrType = it.body().getEmployeeData().get(0).getQrType();
                                old_attendance_type=it.body().getEmployeeData().get(0).getAttendanceType();
                                newQrType = it.body().getEmployeeData().get(0).getQrType();
                                contact = it.body().getEmployeeData().get(0).getOwnerContact();
                                String latLng[] = it.body().getEmployeeData().get(0).getOfficeGpsLocation().split(",");
                                if (latLng.length > 1) {
                                    lat = Double.parseDouble(latLng[0]);
                                    lon = Double.parseDouble(latLng[1]);
                                }
                                String[] businessTypes = getContext().getResources().getStringArray(R.array.business_type);
                                for (int i = 0; i < businessTypes.length; i++) {
                                    if (it.body().getEmployeeData().get(0).getBusinessType().equalsIgnoreCase(businessTypes[i])) {
                                        binding.businessType.setSelection(i);
                                        break;
                                    }
                                }
                            }
                        }
                        else
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

    private void postFormAPI() {
        ((MainActivity) getActivity()).showProgressDialog();
        String[] nameSplits = binding.name.getText().toString().split(" ");
        String attendance_type;
        if (binding.qrCode.isChecked())
            attendance_type = "qr";
        else if (binding.gpsWithWifi.isChecked())
            attendance_type = "gps_with_wifi";
        else attendance_type = "both";
        String latLng = lat + "," + lon;
        StringBuilder firstName = new StringBuilder();
        firstName.append(binding.nameTitle.getSelectedItem().toString()).append(" ");
        for (int i = 0; i < nameSplits.length-1; i++) {
            firstName.append(nameSplits[i]);
            if (i!=nameSplits.length-2)
                firstName.append(" ");
        }
        NetworkService.getInstance().api().postForm2(firstName.toString(),
                nameSplits[nameSplits.length - 1],
                "update",
                binding.mobile.getText().toString(),
                binding.businessType.getSelectedItem().toString(),
                binding.companyName.getText().toString(),
                binding.noEmployee.getText().toString(),
                attendance_type,
                binding.mail.getText().toString(),
                latLng,
                binding.officeAddress.getText().toString(),
                binding.distance.getText().toString(),
                employerId,
                newQrType).enqueue(new Callback<NormalResponse>() {
            @Override
            public void onResponse(Call<NormalResponse> call, Response<NormalResponse> it) {
                ((MainActivity) getActivity()).dismissProgressDialog();
                if (it.body()!=null) {
                    SnackBarHelper.snackBarMessage(requireActivity(), it.body().getMessage());
                    if (it.body().getStatus() == 101) {
                        requireActivity().finish();
                    }
                } else
                    SnackBarHelper.snackBarMessage(requireActivity(), getString(R.string.something_wrong));
            }

            @Override
            public void onFailure(Call<NormalResponse> call, Throwable t) {
                ((MainActivity) getActivity()).dismissProgressDialog();
                SnackBarHelper.snackBarMessage(requireActivity(), getString(R.string.something_wrong));
            }
        });
    }


    void showGPSWIFItoQR()
    {
        boolean manualOverride = false;
        Dialog dialog = new Dialog(requireActivity());
        dialog.setContentView(R.layout.gpswithwifi);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        TextView action = dialog.findViewById(R.id.btnAction);
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showQrTypeDialog(qrType);
            }
        });
        dialog.show();
    }


    void downloadFile(String uRl)
    {
        File direct = new File(requireActivity().getExternalFilesDir(null), "/Miscos");
        if (!direct.exists()) {
            direct.mkdirs();
        }
        DownloadManager mgr = (DownloadManager) requireActivity().getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(true).setTitle("Miscos")
                .setDescription("Downloading...")
                .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_PICTURES,
                        "/Miscos/qrCode.png"
                );
        mgr.enqueue(request);
        SnackBarHelper.snackBarMessage(requireActivity(), "QR code successfully saved in storage");

    }

    void showGenerateQrDialog(String qrCode)
    {
        Dialog dialog = new Dialog(requireActivity());
        dialog.setContentView(R.layout.layout_show_qr_dialog);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        ImageView image = dialog.findViewById(R.id.qrCode);
        TextView save = dialog.findViewById(R.id.save);
        TextView cancel = dialog.findViewById(R.id.cancel);
        Glide.with(requireActivity()).load(qrCode)
                .apply(new RequestOptions().fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(image);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(qrCode);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
