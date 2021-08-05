package com.miscos.staffhandler.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miscos.staffhandler.hrms_management.EmployeeHRMS.EmployeeInformationHRMS;
import com.miscos.staffhandler.hrms_management.hrms.activity.EmployeeFeedbackActivity;
import com.miscos.staffhandler.hrms_management.hrms.utility.Helper;
import com.miscos.staffhandler.activities.UpdateEmployee;
import com.miscos.staffhandler.MainActivity;
import com.miscos.staffhandler.Model.EmployeeListModel;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.Urls.App_Urls;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

/*Developed under Miscos
 * Developed by Nikhil
 * 01-06-2020
 * */

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeHolder> {

    private List<EmployeeListModel> employeeListModels = new ArrayList<>();
    private Context context;
    private ProgressDialog progressDialog;
    private  String employeeID,employerID,name,firstname,lastname,opertaion_type,mobile,age,address,gender,operation_type,emergency_type,shiftAssignList,employee_pic;
    PreferenceManager preferenceManager;

    public EmployeeAdapter(Context mContext, List<EmployeeListModel> employeeListModels)
    {
        this.context = mContext;
        preferenceManager = new PreferenceManager(context);
        this.employeeListModels = employeeListModels;
        employerID = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID);
    }
    @NonNull
    @Override
    public EmployeeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.employee_data, parent, false);
        return new EmployeeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final EmployeeHolder holder, int position)
    {


        /*opertaion_type =  employeeListModels.get(position).getOperation_type();
        preferenceManager.setPreference(KEY_OPERATION_TYPE1,opertaion_type);*/


        EmployeeListModel employeeListModel = employeeListModels.get(position);
        final String contactno="tel:"+employeeListModel.getMobile_no();
        if(employeeListModel.getIsVerified().equalsIgnoreCase("Y"))
        {
            holder.txtaadharverified.setTextColor(context.getResources().getColor(R.color.green_600));
            holder.txtaadharverified.setText("Adhar Verified");
        }
        else
        {
            holder.txtaadharverified.setTextColor(context.getResources().getColor(R.color.red));
            holder.txtaadharverified.setText("Adhar not Verified");
        }

        holder.tvName.setText(employeeListModel.getEmployee_name());
        holder.mobile.setText(employeeListModel.getMobile_no());
        holder.txtid.setText(employeeListModel.getEmployee_no());
        holder.txtdepartment.setText(employeeListModel.getDepartment_name());
        holder.txtdesignation.setText(employeeListModel.getDesignation_name());
        if (!employeeListModels.get(position).getEmployee_pic().isEmpty())
        {
            Picasso.get()
                    .load(employeeListModel.getEmployee_pic())
                    .placeholder(R.drawable.loading_error)
                    .error(R.drawable.imgnot)
                    .into(holder.employeeImage);
        } else
            {
            holder.employeeImage.setImageResource(R.drawable.imgnot);
        }

        holder.mobile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final PrettyDialog dialog=    new PrettyDialog(context);
                dialog
                        .setTitle("Call")
                        .setMessage("Do you want to make a Call?")
                        .setIcon(R.drawable.phone,R.color.primaryTextColor,null)
                        .addButton("Call", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse(contactno));
                                context.startActivity(intent);
                                dialog.dismiss();
                            }
                        })
                        .addButton("No", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                @Override
                public void onClick() {
                    dialog.dismiss();
                }
            })
                        .show();

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                employeeID =  employeeListModels.get(position).getEmployee_id();
                name =  employeeListModels.get(position).getEmployee_name();
                String names = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                String[] splited = names.split("\\s+");
               firstname = (splited[0]);
                if (splited.length > 1) {
                    lastname = (splited[1]);
                }
                mobile =  employeeListModels.get(position).getMobile_no();
                address =  employeeListModels.get(position).getAddress();
                age =  employeeListModels.get(position).getAge();
                gender =  employeeListModels.get(position).getGender();
                emergency_type =  employeeListModels.get(position).getPermitted_for_emergency();
                operation_type =  employeeListModels.get(position).getOperation_type();
                shiftAssignList =  employeeListModels.get(position).getShift_assign_list();
                employee_pic =  employeeListModels.get(position).getEmployee_pic();


                showBottomSheetDialog(employeeListModels.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return employeeListModels.size();
    }
    public static class EmployeeHolder extends RecyclerView.ViewHolder{
        View itemView;
        CircleImageView employeeImage;
        TextView mobile,tvName,txtid,txtaadharverified,txtdesignation,txtdepartment;

        public EmployeeHolder(View itemView)
        {
            super(itemView);
            this.itemView = itemView;
            mobile = itemView.findViewById(R.id.tvMobile);
            tvName = itemView.findViewById(R.id.tvName);
            txtid=itemView.findViewById(R.id.txtid);
            employeeImage = itemView.findViewById(R.id.empPic);
            txtaadharverified=itemView.findViewById(R.id.txtAdharVerified);
            txtdepartment=itemView.findViewById(R.id.txtDepartment);
            txtdesignation=itemView.findViewById(R.id.txtdesignation);
        }
    }

    private void showBottomSheetDialog(EmployeeListModel employeeListModel) {
        View itemLayoutView = LayoutInflater.from(context).inflate(
                R.layout.add_emlpoyee_view, null);
        final Button btnCancel = itemLayoutView.findViewById(R.id.btnCancel);
        final TextView Remove = itemLayoutView.findViewById(R.id.tvRemove);
        final TextView Update = itemLayoutView.findViewById(R.id.tvEdit);
        final TextView empInfo = itemLayoutView.findViewById(R.id.tvEmployeeInfo);
        final TextView SendCredentials = itemLayoutView.findViewById(R.id.tvSendCredentials);
        new BottomSheetDialog(context);
        BottomSheetDialog dialog;
        dialog = new BottomSheetDialog(context,R.style.TransparentDialog);
        dialog.setTitle("Action");
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(itemLayoutView);
        dialog.setCancelable(true);
        BottomSheetDialog finalDialog = dialog;
        Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDialog.dismiss();
                final PrettyDialog prettyDialog= new PrettyDialog(context);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Remove")
                        .setMessage("Are you sure to remove this employee as you will not be able to undo this action")
                        .setIcon(R.drawable.cross,R.color.white,null)
                        .addButton("Yes", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick()
                            {
                                RemoveEmployee(employeeListModel);
                                prettyDialog.dismiss();
                            }
                        })
                        .addButton("No", R.color.whiteTextColor, R.color.red_600, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                            }
                        })
                        .show();

            }
        });

        SendCredentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDialog.dismiss();
                final PrettyDialog prettyDialog= new PrettyDialog(context);
                prettyDialog.setCancelable(true);
                prettyDialog
                        .setTitle("Send Credentials")
                        .setMessage("Login Details will be sent to the registered mobile number of the Employee. Please Check for Updated details before sending.")
                        .setIcon(R.drawable.updatenumber,R.color.white,null)
                        .addButton("Update Number", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                Intent mIntent = new Intent(context, UpdateEmployee.class);
                                mIntent.putExtra("employee_id", employeeID);
                                mIntent.putExtra("name", firstname);
                                mIntent.putExtra("lastname", lastname);
                                mIntent.putExtra("mobile", mobile);
                                mIntent.putExtra("address", address);
                                mIntent.putExtra("age", age);
                                mIntent.putExtra("gender",gender);
                                mIntent.putExtra("emergency_type",emergency_type);
                                mIntent.putExtra("operation_type",operation_type);
                                mIntent.putExtra("assign_list",shiftAssignList);
                                mIntent.putExtra("employee_pic",employee_pic);
                                mIntent.putExtra("employee_data",employeeListModel);
                                mIntent.putExtra("send_credential","send_credentials");
                                context.startActivity(mIntent);
                                ((Activity)context).finish();
                            }
                        })
                        .addButton("Send Credentials", R.color.whiteTextColor, R.color.red_600, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                sendCredentials();
                                prettyDialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDialog.dismiss();
                Intent mIntent = new Intent(context, UpdateEmployee.class);
                mIntent.putExtra("employee_id", employeeID);
                mIntent.putExtra("name", firstname);
                mIntent.putExtra("lastname", lastname);
                mIntent.putExtra("mobile", mobile);
                mIntent.putExtra("address", address);
                mIntent.putExtra("age", age);
                mIntent.putExtra("gender",gender);
                mIntent.putExtra("emergency_type",emergency_type);
                mIntent.putExtra("operation_type",operation_type);
                mIntent.putExtra("assign_list",shiftAssignList);
                mIntent.putExtra("employee_pic",employee_pic);
                mIntent.putExtra("employee_data",employeeListModel);
                mIntent.putExtra("send_credential","edit");
                context.startActivity(mIntent);
                ((Activity)context).finish();
            }
        });
        empInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDialog.dismiss();
                Intent mIntent = new Intent(context, EmployeeInformationHRMS.class);
                mIntent.putExtra("employee_id", employeeID);
                mIntent.putExtra("employee_name",firstname+" "+lastname);
                mIntent.putExtra("call_from","viewEmp");
                context.startActivity(mIntent);
                ((Activity)context).finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDialog.dismiss();
            }
        });
        dialog.show();
    }
    private void RemoveEmployee(EmployeeListModel employeeListModel)
    {Helper.showProgress(context);
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.Employee_Management, response -> {

            Log.e("remove employee", " " + response);
Helper.hideProgress();
            try {

                JSONArray jsonArray=new JSONArray(response);
                JSONObject jsonObject=jsonArray.getJSONObject(0);
                int error_code=jsonObject.getInt("error_code");
                String msg=jsonObject.getString("message");
                if (error_code == 101) {
                    remove_Dialog(employeeListModel);
                }else if (error_code == 103) {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }else if (error_code == 102) {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mode", "remove");
                params.put("employer_id", employerID);
                params.put("employee_id", employeeID);
                params.put("shift_assign_list", shiftAssignList);
                Log.e("remove params is", " " + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void sendCredentials()
    {
        Helper.showProgress(context);
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.SEND_CREDENTIALS, response -> {
            Log.e("send credentials", " " + response);

            try {
                JSONArray jsonArray=new JSONArray(response);
                JSONObject jsonObject=jsonArray.getJSONObject(0);
                int error_code=jsonObject.getInt("error_code");
                String msg=jsonObject.getString("message");
                Helper.hideProgress();
                if (error_code == 101)
                {
                    send_dialog();
                }else if (error_code == 103) {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }else if (error_code == 102) {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Helper.hideProgress();
                Toast.makeText(context, R.string.connection_error, Toast.LENGTH_LONG).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("employer_id", employerID);
                params.put("employee_id", employeeID);
                params.put("mode", "all");
                Log.e("send params is", " " + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
    private void remove_Dialog(EmployeeListModel employeeListModel)
    {
        employeeListModels.remove(employeeListModel);
        notifyDataSetChanged();
        if(preferenceManager.getStringPreference(PreferenceManager.KEY_ENABLE_EMPLOYEE_FEEDBACK).equalsIgnoreCase("y"))
        {
            final PrettyDialog prettyDialog= new PrettyDialog(context);
            prettyDialog.setCancelable(false);
            prettyDialog
                    .setMessage("Would you like to add feedback for removed employee?")
                    .setIcon(R.drawable.success,R.color.primaryTextColor,null)
                    .addButton("Yes", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                        @Override
                        public void onClick()
                        {
                            if(preferenceManager.getStringPreference(PreferenceManager.KEY_ENABLE_EMPLOYEE_FEEDBACK).equalsIgnoreCase("y"))
                            {
                                Intent intent=new Intent(context, EmployeeFeedbackActivity.class);
                                intent.putExtra("emp_id",employeeListModel.getEmployee_id());
                                intent.putExtra("emp_name",employeeListModel.getEmployee_name());
                                intent.putExtra("emp_doj",employeeListModel.getDate_of_joining_date());
                                context.startActivity(intent);
                                ((Activity)context).finish();
                            }


                            prettyDialog.dismiss();
                        }
                    })
                    .addButton("No", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                        @Override
                        public void onClick()
                        {

                            prettyDialog.dismiss();
                        }
                    })
                    .show();

        }
        else
        {
            final PrettyDialog prettyDialog= new PrettyDialog(context);
            prettyDialog.setCancelable(false);
            prettyDialog
                    .setTitle("Remove")
                    .setMessage("Employee Removed Successfully")
                    .setIcon(R.drawable.success,R.color.primaryTextColor,null)
                    .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                        @Override
                        public void onClick()
                        {

                            prettyDialog.dismiss();
                        }
                    })
                    .show();
        }



    }
    private void send_dialog()
    {
        final PrettyDialog prettyDialog= new PrettyDialog(context);
        prettyDialog.setCancelable(false);
        prettyDialog
                .setTitle("Success")
                .setMessage("Credentials has been sent to employee")
                .setIcon(R.drawable.success,R.color.primaryTextColor,null)
                .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        context.startActivity(new Intent(context, MainActivity.class));
                        ((Activity)context).finish();
                        prettyDialog.dismiss();
                    }
                })
                .show();
    }
}
