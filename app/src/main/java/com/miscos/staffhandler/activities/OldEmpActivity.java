package com.miscos.staffhandler.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miscos.staffhandler.Adapters.OldEmployeeAdapter;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.Urls.App_Urls;
import com.miscos.staffhandler.databinding.ActivityOldEmpBinding;
import com.miscos.staffhandler.employee.Model.EmployeeList;
import com.miscos.staffhandler.employee.Model.OldEmpResponse;
import com.miscos.staffhandler.employee.helper.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class OldEmpActivity extends AppCompatActivity
{
PreferenceManager preferenceManager;
OldEmployeeAdapter oldEmployeeAdapter;
ArrayList<EmployeeList> employeeLists,filtertemp;
ActivityOldEmpBinding activityOldEmpBinding;
ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       activityOldEmpBinding= DataBindingUtil.setContentView(this,R.layout.activity_old_emp);
        preferenceManager=new PreferenceManager(this);
        employeeLists=new ArrayList<>();
        filtertemp=new ArrayList<>();
        oldEmployeeAdapter=new OldEmployeeAdapter(this,filtertemp);
        activityOldEmpBinding.recyclerViewOldemp.setAdapter(oldEmployeeAdapter);
        progressDialog = new ProgressDialog(this);
        progressDialog = ProgressDialog.show(this, null, null, true, false);
        progressDialog.setIndeterminate(true);
        progressDialog.setContentView(R.layout.loader);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        activityOldEmpBinding.searchEmpEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filterAdmin(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
setToolbarData();
        EmployeeActivity();
    }
    void filterAdmin(String text)
    {
        if(text.isEmpty())
        {
            filtertemp.addAll(employeeLists);
            oldEmployeeAdapter.notifyDataSetChanged();
            return;
        }
        filtertemp.clear();
        for (EmployeeList  d : employeeLists) {
            if (d.getName().toLowerCase().contains(text.toLowerCase()))
            {
                filtertemp.add(d);
            }
        }
        oldEmployeeAdapter.notifyDataSetChanged();
    }
    private void setToolbarData()
    {
        activityOldEmpBinding.toolbar.setTitle(R.string.old_emp);
        activityOldEmpBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        activityOldEmpBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void EmployeeActivity()
    {

        progressDialog.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.Employee_Management, response -> {
            Log.e("remove employee_data", " " + response);

            try {
                JSONArray jsonArray=new JSONArray(response);
                JSONObject jsonObject=jsonArray.getJSONObject(0);
                int error_code=jsonObject.getInt("error_code");
                String msg=jsonObject.getString("message");
                if (error_code == 101)
                {
                    progressDialog.dismiss();
                    Gson gson=new Gson();
                  List<OldEmpResponse>  oldEmpResponse= gson.fromJson(response,new TypeToken<List<OldEmpResponse>>(){}.getType());
                 if(oldEmpResponse.get(0).getEmployeeList().size()>0)
                 {
                     activityOldEmpBinding.txtnodata.setVisibility(View.GONE);
                     activityOldEmpBinding.recyclerViewOldemp.setVisibility(View.VISIBLE);
                     employeeLists.addAll(oldEmpResponse.get(0).getEmployeeList());
                     filtertemp.addAll(employeeLists);
                        oldEmployeeAdapter.notifyDataSetChanged();
                 }
                }
                else
                {
                    progressDialog.dismiss();
                    activityOldEmpBinding.recyclerViewOldemp.setVisibility(View.GONE);
                    activityOldEmpBinding.txtnodata.setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                progressDialog.dismiss();
                final Dialog dialog1 = new Dialog(this);
// Include dialog.xml file
                dialog1.setContentView(R.layout.dialog_exit);
                dialog1.show();

                TextView heading1 = dialog1.findViewById(R.id.tv_quit_learning);
                TextView description1 = dialog1.findViewById(R.id.tv_description);
                Button btnNo1 = (Button) dialog1.findViewById(R.id.btn_no);
                Button btnYes1 = (Button) dialog1.findViewById(R.id.btn_yes);
                heading1.setText("Error");
                description1.setText("An unfortunate error occurred, please try again.");
                btnNo1.setVisibility(View.GONE);
                btnYes1.setText("Ok");
                btnYes1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                final PrettyDialog prettyDialog = new PrettyDialog(OldEmpActivity.this);
                prettyDialog.setCancelable(false);
                prettyDialog
                        .setTitle("Connection Error")
                        .setMessage("Please try again")
                        .setIcon(R.drawable.sad, R.color.primaryTextColor, null)
                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                            }
                        })
                        .show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mode", "get_remove_employee_list");
                params.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
                Log.e("remove employee params", " " + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
