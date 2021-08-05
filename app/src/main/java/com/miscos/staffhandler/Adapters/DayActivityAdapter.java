package com.miscos.staffhandler.Adapters;

import android.app.TimePickerDialog;
import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miscos.staffhandler.hrms_management.hrms.utility.Helper;
import com.miscos.staffhandler.Model.DayActivityModel;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.Urls.App_Urls;
import com.miscos.staffhandler.employee.helper.PreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

/*Developed under Miscos
 * Developed by Nikhil
 * 01-06-2020
 * */
public class DayActivityAdapter extends RecyclerView.Adapter<DayActivityAdapter.DayActivityHolder> {

    private List<DayActivityModel> dayActivityModelArrayList = new ArrayList<>();
    private  Context context;
    PreferenceManager preferenceManager;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    public DayActivityAdapter(Context context, List<DayActivityModel> dayActivityModels) {
        this.context = context;
        this.dayActivityModelArrayList = dayActivityModels;
        preferenceManager=new PreferenceManager(context);
    }

    @NonNull
    @Override
    public DayActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.day_activity, parent, false);

        return new DayActivityHolder(view);
    }

    @Override
    public void onBindViewHolder(final DayActivityHolder holder, int position)
    {
        DayActivityModel activityModel = dayActivityModelArrayList.get(position);
        holder.dayIn.setText(parseDateToddMMyyyy(activityModel.getDay_in()));
        holder.dayOut.setText(parseDateToddMMyyyy(activityModel.getDay_out()));
        if(!activityModel.getDay_in().equalsIgnoreCase("")&&(isToday(activityModel.getDate())||isYesterday(activityModel.getDate())))
            holder.imginedit.setVisibility(View.VISIBLE);
        else
            holder.imginedit.setVisibility(View.INVISIBLE);

        if(!activityModel.getDay_out().equalsIgnoreCase("")&&(isToday(activityModel.getDate())||isYesterday(activityModel.getDate())))
            holder.imgoutedit.setVisibility(View.VISIBLE);
        else
            holder.imgoutedit.setVisibility(View.INVISIBLE);

        holder.imginedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        final PrettyDialog dialog=    new PrettyDialog(context);
                        dialog
                                .setTitle("Confirm")
                                .setMessage("Are you sure to update?")
                                .setIcon(R.drawable.info,R.color.primaryTextColor,null)
                                .addButton("Yes", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                    @Override
                                    public void onClick()
                                    {
                                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        calendar.set(Calendar.MINUTE, minute);
                                        holder.dayIn.setText(simpleDateFormat.format(calendar.getTime()));
                                        DayAttendance("day_in",activityModel.getAttendance_type(),simpleDateFormat.format(calendar.getTime()),activityModel.getEmployee_id(),position,activityModel.getDate());
                                        dialog.dismiss();
                                    }
                                })
                                .addButton("No", R.color.whiteTextColor, R.color.red, new PrettyDialogCallback() {
                                    @Override
                                    public void onClick() {
                                        dialog.dismiss();
                                    }
                                })

                                .show();
                        dialog.setCancelable(false);



                    }
                };
                Date date = null;
                try {
                    date = simpleDateFormat.parse(holder.dayIn.getText().toString());
                    calendar.setTime(date);
                    new TimePickerDialog(
                            context,
                            timeSetListener,
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                    ).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
holder.imgoutedit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view)
    {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {

                final PrettyDialog dialog=    new PrettyDialog(context);
                dialog
                        .setTitle("Confirm")
                        .setMessage("Are you sure to update?")
                        .setIcon(R.drawable.info,R.color.primaryTextColor,null)
                        .addButton("Yes", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                            @Override
                            public void onClick()
                            {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                holder.dayOut.setText(simpleDateFormat.format(calendar.getTime()));
                                DayAttendance("day_out",activityModel.getAttendance_type(),simpleDateFormat.format(calendar.getTime()),activityModel.getEmployee_id(),position,activityModel.getDate());
                                dialog.dismiss();
                            }
                        })
                        .addButton("No", R.color.whiteTextColor, R.color.red, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                dialog.dismiss();
                            }
                        })

                        .show();
                dialog.setCancelable(false);

            }
        };
            Date date = null;
            try {
                date = simpleDateFormat.parse(holder.dayOut.getText().toString());
                calendar.setTime(date);
                new TimePickerDialog(
                        context,
                        timeSetListener,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                ).show();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    });
        holder.tvName.setText(activityModel.getEmployee_name());

    }

    public int getItemCount() {
        return dayActivityModelArrayList.size();
    }

    public static class DayActivityHolder extends RecyclerView.ViewHolder{
        View itemView;
        TextView dayIn,dayOut,tvName;
        ImageView imginedit,imgoutedit;

        public DayActivityHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            dayIn = itemView.findViewById(R.id.tvDayIn);
            dayOut = itemView.findViewById(R.id.tvDayout);
            tvName = itemView.findViewById(R.id.tvName);
            imginedit=itemView.findViewById(R.id.imgDayInedit);
            imgoutedit=itemView.findViewById(R.id.imgDayoutedit);
        }
    }
    public String parseDateToddMMyyyy(String time)
    {
        if(time.equalsIgnoreCase(""))
            return "";
        String inputPattern = "hh:mm:ss";
        String outputPattern = "hh:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    private void DayAttendance(final String status,final String attendance_type,String time,String emp_id,int position,String date)
    {
        Helper.showProgress(context);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, App_Urls.EMPLOYEE_ATTENDANCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Helper.hideProgress();
                        String statusCode = null,msg = null;
                        Log.d("day_activity response", " " + response);
                        //Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            statusCode = jsonObject.getString("error_code");
                           msg = jsonObject.getString("message");

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                        switch (statusCode) {
                            case "101":
                                final PrettyDialog prettyDialog= new PrettyDialog(context);
                                prettyDialog.setCancelable(false);
                                prettyDialog
                                        .setTitle("Day Status")
                                        .setMessage(msg)
                                        .setIcon(R.drawable.check,R.color.primaryTextColor,null)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {

                                                prettyDialog.dismiss();
                                            }
                                        })
                                        .show();
                                break;
                            case "100":
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                break;
                            case "102":
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                break;
                            case "103":
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                break;
                            case "104":
                                final PrettyDialog prettyDialog2= new PrettyDialog(context);
                                prettyDialog2.setCancelable(false);
                                prettyDialog2
                                        .setTitle("Day Status")
                                        .setMessage(msg)
                                        .setIcon(R.drawable.check,R.color.white,null)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog2.dismiss();
                                            }
                                        })
                                        .show();
                                break;
                            case "105":
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                break;
                            case "106":
                                final PrettyDialog prettyDialog1= new PrettyDialog(context);
                                prettyDialog1.setCancelable(false);
                                prettyDialog1
                                        .setTitle("Day Status")
                                        .setMessage(msg)
                                        .setIcon(R.drawable.cross,R.color.white,null)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog1.dismiss();
                                            }
                                        })
                                        .show();
                                break;
                            case "107":
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                break;
                            case "110":
                                final PrettyDialog prettyDialog3= new PrettyDialog(context);
                                prettyDialog3.setCancelable(false);
                                prettyDialog3
                                        .setTitle("Day Status")
                                        .setMessage(msg)
                                        .setIcon(R.drawable.cross,R.color.white,null)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog3.dismiss();
                                            }
                                        })
                                        .show();
                                break;
                            default:
                                final PrettyDialog prettyDialog4= new PrettyDialog(context);
                                prettyDialog4.setCancelable(false);
                                prettyDialog4
                                        .setTitle("Day Status")
                                        .setMessage(msg)
                                        .setIcon(R.drawable.cross,R.color.white,null)
                                        .addButton("Okay", R.color.whiteTextColor, R.color.primaryTextColor, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog4.dismiss();
                                            }
                                        })
                                        .show();
                                break;
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       Helper.hideProgress();
                        Toast.makeText(context, R.string.connection_error, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // it will check for day status for day in or day out accordingly mode parameter will be passed
                if (status.equals("day_in")) {
                    params.put("mode_type", "day_in");
                }
                if (status.equals("day_out")) {
                    params.put("mode_type", "day_out");
                }
                params.put("mode", "edit_manually_attendance");
                params.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
                params.put("employee_id", emp_id);
                params.put("force", "1");
                params.put("time", time);
                params.put("date", date);
                params.put("operation_type", preferenceManager.getStringPreference(PreferenceManager.KEY_OPERATION_TYPE));
                params.put("type_of_attendance", attendance_type);
                params.put("employee_lat_long", "");

                params.put("wifi_name", "");

                if (preferenceManager.getStringPreference(PreferenceManager.KEY_OPERATION_TYPE).equalsIgnoreCase("office")) {
                    params.put("shift_no", "");

                }else {
                    params.put("shift_no", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_SHIFTS));
                }
                Log.e("dayInOut params is", " " + params);
                return params;
            }

        };

        requestQueue.add(stringRequest);
    }
    public  boolean isYesterday(String  date)
    {
        Date d= null;
        try {
            d = simpleDateFormat1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return DateUtils.isToday(d.getTime() + DateUtils.DAY_IN_MILLIS);
    }
    public  boolean isToday(String date)
    {
        Date d= null;
        try {
            d = simpleDateFormat1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return DateUtils.isToday(d.getTime());
    }
}
