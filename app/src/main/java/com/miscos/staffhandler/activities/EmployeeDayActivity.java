package com.miscos.staffhandler.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fivemin.chief.nonetworklibrary.networkBroadcast.NoNet;
import com.miscos.staffhandler.Adapters.DayActivityAdapter;
import com.miscos.staffhandler.MainActivity;
import com.miscos.staffhandler.Model.DayActivityModel;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.Urls.App_Urls;
import com.miscos.staffhandler.employee.helper.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

/*Developed under Miscos
 * Developed by Nikhil
 * 01-06-2020
 * */
public class EmployeeDayActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = EmployeeDayActivity.class.getCanonicalName();
    private RecyclerView rcDayActivity;
    private SwipeRefreshLayout pullToRefresh;
    private PreferenceManager preferenceManager;
    private List<DayActivityModel> dayActivityModels = new ArrayList<>();
    private DayActivityAdapter dayActivityAdapter;
    private TextView tvNoData;
    private String employer_id, selecteddate, reportFor, attendanceType;
    private RelativeLayout rlMain;
    private ProgressDialog progressDialog;
    // No Internet Dialog
    private NoNet mNoNet;
    private FragmentManager fm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_employee);
        fm = getSupportFragmentManager();
        mNoNet = new NoNet();
        mNoNet.initNoNet(this, fm);

        preferenceManager = new PreferenceManager(EmployeeDayActivity.this);

        settingToolBar();

        tvNoData = findViewById(R.id.tvNodata);
        ImageView tvBack = findViewById(R.id.imBack);
        rlMain = findViewById(R.id.rlMain);
        employer_id = preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID);
        Intent intent = getIntent();
        selecteddate = intent.getStringExtra("date");
        reportFor = intent.getStringExtra("report_for");
        attendanceType = intent.getStringExtra("attendance_type");
        rcDayActivity = findViewById(R.id.rcEmployeeDayList);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rcDayActivity.setLayoutManager(mLayoutManager);
        rcDayActivity.setItemAnimator(new DefaultItemAnimator());
        dayActivityAdapter = new DayActivityAdapter(this, dayActivityModels);
        pullToRefresh.setOnRefreshListener(this);
        EmployeeDayActivityList();
        pullToRefresh.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        pullToRefresh.post(() -> {
            pullToRefresh.setRefreshing(true);
            EmployeeDayActivityList();
        });
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        mNoNet.RegisterNoNet();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mNoNet.unRegisterNoNet();
        super.onPause();
    }

    @Override
    public void onRefresh() {
        EmployeeDayActivityList();
    }

    //Employee Day List
    public void  EmployeeDayActivityList()
    {
        pullToRefresh.setRefreshing(true);
        final StringRequest strReq = new StringRequest(Request.Method.POST, App_Urls.Employee_DAY_ACTIVITY, response -> {
            Log.e("day_activity_response", " " + response);
            try {
                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                int error_code = jsonObject.getInt("error_code");
                String msg = jsonObject.getString("message");

                if (error_code == 101)
                {
                    dayActivityModels.clear();
                    JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArrayData.length(); i++) {
                        JSONObject dayActivityList = jsonArrayData.getJSONObject(i);
                        DayActivityModel dayActivityModel = new DayActivityModel();
                        dayActivityModel.setS_no(dayActivityList.getInt("s_no"));
                        dayActivityModel.setEmployee_name(dayActivityList.getString("employee_name"));
                        dayActivityModel.setDate(dayActivityList.getString("date"));
                        dayActivityModel.setDay_in(dayActivityList.getString("day_in"));
                        dayActivityModel.setDay_out(dayActivityList.getString("day_out"));
                        dayActivityModel.setEmployee_id(dayActivityList.getString("employee_id"));
                        dayActivityModel.setAttendance_type(dayActivityList.getString("attendance_type"));
                        dayActivityModels.add(dayActivityModel);
                        //     Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    }
                    dayActivityAdapter = new DayActivityAdapter(this, dayActivityModels);
                    rcDayActivity.setAdapter(dayActivityAdapter);
                    dayActivityAdapter.notifyDataSetChanged();
                    if (dayActivityModels.size() == 0) {
                        tvNoData.setVisibility(View.VISIBLE);
                        rlMain.setVisibility(View.GONE);
                    } else {
                        rlMain.setVisibility(View.VISIBLE);
                        tvNoData.setVisibility(View.GONE);
                    }
                } else if (error_code == 104) {
                    //   Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    tvNoData.setVisibility(View.VISIBLE);
                    rlMain.setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                tvNoData.setVisibility(View.VISIBLE);
                Log.e("Check", "JSONEXCEPTION" + e);
                e.printStackTrace();
                final Dialog dialog1 = new Dialog(EmployeeDayActivity.this);
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
            pullToRefresh.setRefreshing(false);
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pullToRefresh.setRefreshing(false);
                final PrettyDialog prettyDialog = new PrettyDialog(EmployeeDayActivity.this);
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
                params.put("employer_id", employer_id);
                params.put("date", selecteddate);
                params.put("report_for", reportFor);
                params.put("attendance_type", attendanceType);
                Log.e("params is", " " + params);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(EmployeeDayActivity.this);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void settingToolBar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}
