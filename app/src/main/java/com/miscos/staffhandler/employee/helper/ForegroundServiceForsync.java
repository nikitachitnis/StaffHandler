package com.miscos.staffhandler.employee.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.miscos.staffhandler.LocalDatabase.SqLiteHelper;
import com.miscos.staffhandler.R;
import com.miscos.staffhandler.employee.employeemodule.EmployeeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


public class ForegroundServiceForsync extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static final String TAG = "Service";
    private final Handler handler = new Handler();
    boolean isRunning = false;
    List<String> name = new ArrayList<>();
    List<String> number = new ArrayList<>();
    List<String> groupId = new ArrayList<>();
    PreferenceManager preferenceManager;
    SqLiteHelper sQliteHelper;
    boolean isJsonCalling = false;
    int i = 0;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

           // synchData();
            recursiveHandler();


        }
    };

    void recursiveHandler() {

        handler.postDelayed(mRunnable, 10000);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service onCreate");
        isRunning = true;
        isJsonCalling = false;
        preferenceManager = new PreferenceManager(this);
        sQliteHelper = new SqLiteHelper(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service onStartCommand");

        //String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();

        Intent notificationIntent = new Intent(this, EmployeeActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("SHLite Application")
                .setContentText("Working on data Synchronization in background")
                .setSmallIcon(R.drawable.avatar)
                .build();

        startForeground(1, notification);
        recursiveHandler();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        if (handler != null)
            handler.removeCallbacks(mRunnable);
        Log.i(TAG, "Service onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }


    //Check Internet Connection
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            return false;
        }
        return true;
    }
}
