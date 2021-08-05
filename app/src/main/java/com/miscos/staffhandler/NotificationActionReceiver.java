package com.miscos.staffhandler;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miscos.staffhandler.Urls.App_Urls;
import com.miscos.staffhandler.activities.ManualAttendanceActivity;
import com.miscos.staffhandler.employee.employeemodule.Activity_ScanQrCode;
import com.miscos.staffhandler.employee.employeemodule.EmployeeActivity;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.miscos.staffhandler.employee.network.LocationTrack;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class NotificationActionReceiver extends BroadcastReceiver
{
    String status,type_of_attendance,attendance_type,post_close_time="";
    PreferenceManager preferenceManager;
    double dist;
    double compareValue;
    double lat;
    double longi;
    private ProgressDialog progressDialog;
    String addWifiNames,wifinames,latitude, longitude;
    private WifiManager wifiManager;
    private final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
    LocationManager lm;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    String notification_for="";
    double office_latitude, office_lognitude;
    List<String> deviceList = new ArrayList<>();
    List<ScanResult> wifiList = new ArrayList<>();
    final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String time = dateFormat.format(new Date());
        preferenceManager=new PreferenceManager(context);
        try {
            JSONObject jsonObject=new JSONObject(intent.getStringExtra("data"));
            String employee_id=jsonObject.getString("employee_id");
            preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEEID,employee_id);
            preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYERID,jsonObject.getString("employer_id"));
            preferenceManager.setPreference(PreferenceManager.KEY_ATTENDANCETYPE,jsonObject.getString("attendance_type"));
            attendance_type=jsonObject.getString("attendance_type");
            preferenceManager.setPreference(PreferenceManager.KEY_OPERATION_TYPE,jsonObject.getString("operation_type"));
            latitude = preferenceManager.getStringPreference(PreferenceManager.KEY_LATITUDE);
            longitude = preferenceManager.getStringPreference(PreferenceManager.KEY_LONGITUDE);
            wifinames=jsonObject.getString("wifi_names");
            post_close_time=jsonObject.getString("post_close_time");
            compareValue=Double.parseDouble(jsonObject.getString("geo_permitted_distance"));
            status = "day_out";
            notification_for=jsonObject.getString("notification_to");
            type_of_attendance = jsonObject.getString("type_of_attendance");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        longi = LocationTrack.getLongitude();
        lat = LocationTrack.getLatitude();
       String latLang = lat+","+longi;

        // Toast.makeText(this, latLang, Toast.LENGTH_SHORT).show();
        preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_LATITUDE_LONGITUDE, latLang);
        if(!notification_for.equalsIgnoreCase("employee"))
        {
            Intent intent1 = new Intent(context, ManualAttendanceActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
            return;
        }



        office_latitude = Double.parseDouble(latitude);
        office_lognitude = Double.parseDouble(longitude);
        dist=showDistance();

        if(dist>0&&dist >= compareValue&&!type_of_attendance.equalsIgnoreCase("emergency"))
        {
            errorDialog(context,"outside");
            return;
        }
        if(diffTime()>30)
        {
            errorDialog(context,"inside");

            return;
        }
        if(attendance_type.equalsIgnoreCase("gps_with_wifi"))
        {
            // turnOnWifi(context);
            getWifi(context);
        }
        if (attendance_type.equalsIgnoreCase("qr"))
            {
                Intent intent1 = new Intent(context, Activity_ScanQrCode.class);
                intent1.putExtra("Day", status);
                intent1.putExtra("attendance_type", type_of_attendance);
                intent1.putExtra(Activity_ScanQrCode.CALL_FROM,"notification");
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);

            } else if (attendance_type.equals("both")||attendance_type.equals("gps"))
            {
                if (dist>0&&dist <= compareValue &&preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("geo_permitted_distance")||type_of_attendance.equalsIgnoreCase("emergency")){
                    callSuccessdialog(context);
                }else if (preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("gps_plotting")){
                    checkGPS(context);
                }else {
                    errorDialog(context,"ouside");
                }
            }else if(attendance_type.equals("gps_with_wifi"))
            {
                if (preferenceManager.getStringPreference(PreferenceManager.KEY_WIFI_NAMES).equals(""))
                {
                    noWifiAdded(context);
                }else
                    {
                    if (dist>0&&dist <= compareValue && preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("geo_permitted_distance")||type_of_attendance.equalsIgnoreCase("emergency")) {
                        callSuccessdialogWifi(context);
                    }else if(preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equalsIgnoreCase("gps_plotting")) {
                        checkGPS(context);
                    }else {
                        errorDialog(context,"outside");
                    }
                }
            }else {
                errorDialog(context,"outside");
            }

        Toast.makeText(context, "Marked day out", Toast.LENGTH_SHORT).show();
    }
    private void callSuccessdialog(Context context)
    {
        Intent intent = new Intent(context, Activity_ScanQrCode.class);
        intent.putExtra("Day", status);
        intent.putExtra(Activity_ScanQrCode.CALL_FROM,"notification");
        intent.putExtra("attendance_type", type_of_attendance);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);



    }
    private void callSuccessdialogWifi(Context context)
    {
        placeAttendance(status,type_of_attendance,context);
    

    }
    public void turnOnWifi(Context context){

        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "location turned off", Toast.LENGTH_SHORT).show();
              /*  ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_ACCESS_COARSE_LOCATION);*/
            }
        }
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(context, "Turning WiFi ON...", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }else{
            Toast.makeText(context, "Turn On Wifi First", Toast.LENGTH_SHORT).show();
        }
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
          context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        context.registerReceiver(receiverWifi, intentFilter);
    }
    private void getWifi(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "location turned off", Toast.LENGTH_SHORT).show();
              /*  ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_ACCESS_COARSE_LOCATION);*/
            } else {
                wifiManager.startScan();
            }
        } else
            {
            Toast.makeText(context, "scanning", Toast.LENGTH_SHORT).show();
            wifiManager.startScan();

        }
    }


    BroadcastReceiver receiverWifi = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
                wifiList = wifiManager.getScanResults();
                deviceList = new ArrayList<>();
                for (ScanResult scanResult : wifiList)
                {
                    deviceList.add(scanResult.SSID);
                    addWifiNames = "";
                    for (int i = 0; i < deviceList.size(); i++) {
                        if (addWifiNames.matches("")) {
                            addWifiNames = deviceList.get(i);
                        } else {
                            addWifiNames = addWifiNames + "," + deviceList.get(i);
                        }
                    }
                    //Toast.makeText(context, addWifiNames, Toast.LENGTH_SHORT).show();
                }

            }
        }
    };
    private void errorDialog(Context context,String loc_status)
    {
        notificationEmployer(context,loc_status,post_close_time);
        String channelId = context.getString(R.string.default_notification_channel_id);
        String channelName =context.getString(R.string.fcm_message);
        String msg="";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if(loc_status.equalsIgnoreCase("outside"))
       msg="You are out of office premises.Your Distance from Office is " + dist+" Meters,However this request is sent to admin";
        else
            msg="You can not mark Duty out as office timing is over.However this request is sent to admin";


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Duty out request sent")
                        .setContentText(msg)
                        .setStyle(new NotificationCompat.BigTextStyle())
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri);


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());




    }
    private void noWifiAdded(Context context)
    {
        Toast.makeText(context, "Please add wifi names using Other Settings", Toast.LENGTH_SHORT).show();
        /*final PrettyDialog prettyDialog= new PrettyDialog(context);
        prettyDialog.setCancelable(false);
        prettyDialog
                .setTitle("Wifi")
                .setMessage("Please add Wifi from Wifi management")
                .setIcon(R.drawable.cross,R.color.white,null)
                .addButton("Close", R.color.whiteTextColor, R.color.blue_600, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        prettyDialog.dismiss();
                    }
                })
                .show();*/

    }
    private void checkGPS(Context context)
    {
        //progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, App_Urls.CHECK_GPS,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        //progressDialog.dismiss();
                        Log.d("check_gps", " "+response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String statusCode = jsonObject.getString("error_code");
                            String msg = jsonObject.getString("message");

                            if (statusCode.equals("102"))
                            {
                                if (attendance_type.equals("gps_with_wifi")) {
                                    placeAttendance(status,type_of_attendance,context);

                                }else{
                                    Intent intent = new Intent(context, Activity_ScanQrCode.class);
                                    intent.putExtra("Day", status);
                                    intent.putExtra(Activity_ScanQrCode.CALL_FROM,"notification");
                                    intent.putExtra("attendance_type", type_of_attendance);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                               
                                }

                             
                            } else{
                                Toast.makeText(context, "You are out of office premises", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error while getting response from server", Toast.LENGTH_SHORT).show();
                        }
                        //progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.dismiss();
                        Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                // it will check for day stauts for day in or day out accordingly mode parameter will be passed
                params.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
                params.put("employee_lat_long", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_LATITUDE_LONGITUDE));
                Log.e("check_gps", " " + params);
                return params;
            }

        };

        requestQueue.add(stringRequest);
    }
    private void placeAttendance(final String status,String typeAttendance,Context context)
    {
        //progressDialog.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.EMPLOYEE_ATTENDANCE, response -> {
            //progressDialog.dismiss();
            Log.d("attendance_mark", " "+response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                int error_code = jsonObject.getInt("error_code");
                String msg = jsonObject.getString("message");
                if(error_code ==101) {
                    preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_STATUS, "in");
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                } else if (error_code ==106) {

                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                } else if (error_code == 104) {
                    preferenceManager.setPreference(PreferenceManager.KEY_EMPLOYEE_STATUS, "Out");
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                }if (error_code == 102)
                {
                    Toast.makeText(context, "You are not connect to the office wifi", Toast.LENGTH_SHORT).show();

                }else if (error_code ==107)
                {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e)
            {
                e.printStackTrace();

                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Toast.makeText(context, "Error while parsing response", Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                // it will check for day stauts for day in or day out accordingly mode parameter will be passed
                if (status.equals("day_in")) {
                    params.put("mode", "day_in");
                }
                if (status.equals("day_out")) {
                    params.put("mode", "day_out");
                }
                params.put("force", "0");
                params.put("request_from", "notification");
                params.put("employer_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));

                params.put("employee_id", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEEID));
                params.put("operation_type", preferenceManager.getStringPreference(PreferenceManager.KEY_OPERATION_TYPE));

                if (preferenceManager.getStringPreference(PreferenceManager.KEY_OPERATION_TYPE).equalsIgnoreCase("office")) {
                    params.put("shift_no", "");

                }else {
                    params.put("shift_no", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_SHIFTS));
                }

                if (attendance_type.equals("gps_with_wifi")) {
                    if (preferenceManager.getStringPreference(PreferenceManager.KEY_GPS_METHOD).equals("geo_permitted_distance")) {
                        params.put("employee_lat_long","");
                    }else{
                        params.put("employee_lat_long", preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEE_LATITUDE_LONGITUDE));
                    }
                }else{
                    params.put("employee_lat_long","");
                }
                params.put("type_of_attendance", typeAttendance);

                if (attendance_type.equals("gps_with_wifi")) {
                    params.put("wifi_name", wifinames);
                }else{
                    params.put("wifi_name", "");
                }
                Log.e("attendance params is", " " + params);
                return params;
            }

        };

        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
    private void notificationEmployer(Context context,String emp_location,String post_close_time)
    {
        //progressDialog.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                App_Urls.NOTIFICATION_EMPLOYER, response -> {
            //progressDialog.dismiss();
            Log.d("attendance_mark", " "+response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                int error_code = jsonObject.getInt("error_code");
                String msg = jsonObject.getString("message");
                if(error_code ==101)
                {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                } else
                {

                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e)
            {
                e.printStackTrace();

                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Toast.makeText(context, "Error while parsing response", Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mode","send_notification_to_employer");
                params.put("employer_id",preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID));
                params.put("employee_id",preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYEEID));
                params.put("employee_location",emp_location);
                params.put("type_of_attendance",preferenceManager.getStringPreference(PreferenceManager.KEY_MANUAL_ATTENDANCE_TYPE));
                params.put("operation_type",preferenceManager.getStringPreference(PreferenceManager.KEY_OPERATION_TYPE));
                params.put("post_close_time",post_close_time);
                Log.d("notification",params.toString());
                return params;
            }

        };

        strReq.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
    private double showDistance()
    {

        Location locationA = new Location("Location A");

        locationA.setLatitude(office_latitude);

        locationA.setLongitude(office_lognitude);

        if(lat==0.0||longi==0.0)
        {
            dist=-1;
            return dist;
        }
        Location locationB = new Location("Location B");

        locationB.setLatitude(lat);

        locationB.setLongitude(longi);
        double distance=locationA.distanceTo(locationB);
        Log.d("distance",distance+"");

        return distance ;

    }
    public long diffTime()
    {
        long min = 0;
        long difference ;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss"); // for 12-hour system, hh should be used instead of HH
            // There is no minute different between the two, only 8 hours difference. We are not considering Date, So minute will always remain 0

            Date date2 = simpleDateFormat.parse(post_close_time);
            Date now=new Date();

            difference = (now.getTime() - date2.getTime()) / 1000;
            long hours = difference % (24 * 3600) / 3600; // Calculating Hours
            long minute = difference % 3600 / 60; // Calculating minutes if there is any minutes difference
            min = minute + (hours * 60); // This will be our final minutes. Multiplying by 60 as 1 hour contains 60 mins
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return min;
    }

}
