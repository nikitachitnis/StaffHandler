package com.miscos.staffhandler.hrms_management.hrms.utility;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.widget.AppCompatTextView;

import com.miscos.staffhandler.hrms_management.hrms.model.MonthYearData;
import com.miscos.staffhandler.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class Helper {
    // Function to validate Aadhar number.
    public static boolean
    isValidAadharNumber(String str)
    {
        // Regex to check valid Aadhar number.
        String regex
                = "^[2-9]{1}[0-9]{3}\\s[0-9]{4}\\s[0-9]{4}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (str == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given string
        // and regular expression.
        Matcher m = p.matcher(str);

        // Return if the string
        // matched the ReGex
        return m.matches();
    }

    /*TODO Internet Check*/
    private static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //This for Wifi.
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean isCheckInternet(Context context) {
        if (isInternetAvailable(context)) {
            return true;
        } else {
            MessageUtility.showToast(context, "No Internet Connection ");
            return false;
        }
    }

    public static boolean hasInternet(Context context) {
        if (isInternetAvailable(context)) {
            return true;
        } else {
            //MessageUtility.showToast(context, "No Internet Connection ");
            return false;
        }
    }
    public static void openDatePickerDialog(Context context, AppCompatTextView textView) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(context,
                (view, year, monthOfYear, dayOfMonth) -> {
                    textView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                }, mYear, mMonth, mDay);

        dpd.getDatePicker().setMaxDate(new Date().getTime());
        dpd.show();
    }


    public static void openFromDatePickerDialog(Context context, AppCompatTextView textView, AppCompatTextView textView1) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(context,
                (view, year, monthOfYear, dayOfMonth) -> {
                    textView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    textView1.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                }, mYear, mMonth, mDay);

        dpd.getDatePicker().setMinDate(new Date().getTime());
        dpd.show();
    }


    public static void openDatePickerDialog(Context context, AppCompatTextView textView, String fromDate) {
        final Calendar c = Calendar.getInstance();
        Date date=null;
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        try {
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            date = format.parse(fromDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(context,
                (view, year, monthOfYear, dayOfMonth) -> {
                    textView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMinDate(date.getTime());
       // dpd.getDatePicker().setMaxDate(new Date().getTime());
        dpd.show();
    }

    public static void showMessage(Context context, String msg) {
        PrettyDialog prettyDialog = new PrettyDialog(context);
        prettyDialog.setCancelable(false);
        prettyDialog
                .setTitle(context.getString(R.string.app_name))
                .setMessage(msg)
                .setIcon(R.drawable.ic_close, R.color.color_white, null)
                .addButton("Okay", R.color.color_white, R.color.blue, () -> prettyDialog.dismiss())
                .show();

    }

    public static void showSucessMessage(Context context, String msg) {
        PrettyDialog prettyDialog = new PrettyDialog(context);
        prettyDialog.setCancelable(false);
        prettyDialog
                .setTitle(context.getString(R.string.app_name))
                .setMessage(msg)
                .setIcon(R.drawable.ic_done, R.color.color_white, null)
                .addButton("Okay", R.color.color_white, R.color.blue, () -> prettyDialog.dismiss())
                .show();

    }
    public static void showSucessMessageWithClose(Context context, String msg)
    {
        PrettyDialog prettyDialog = new PrettyDialog(context);
        prettyDialog.setCancelable(false);
        prettyDialog
                .setTitle(context.getString(R.string.app_name))
                .setMessage(msg)
                .setIcon(R.drawable.ic_done, R.color.color_white, null)
                .addButton("Okay", R.color.color_white, R.color.blue, new PrettyDialogCallback() {
                    @Override
                    public void onClick()
                    {

                    }
                })
                .show();

    }
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


    public static  int monthsBetweenDates(Date startDate, Date endDate){

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        int monthsBetween = 0;
        int dateDiff = end.get(Calendar.DAY_OF_MONTH)-start.get(Calendar.DAY_OF_MONTH);

        if(dateDiff<0) {
            int borrrow = end.getActualMaximum(Calendar.DAY_OF_MONTH);
            dateDiff = (end.get(Calendar.DAY_OF_MONTH)+borrrow)-start.get(Calendar.DAY_OF_MONTH);
            monthsBetween--;

            if(dateDiff>0) {
                monthsBetween++;
            }
        }
        else {
            monthsBetween++;
        }
        monthsBetween += end.get(Calendar.MONTH)-start.get(Calendar.MONTH);
        monthsBetween  += (end.get(Calendar.YEAR)-start.get(Calendar.YEAR))*12;
        return monthsBetween;
    }


    public static ArrayList<MonthYearData> getMonthList(){
        ArrayList<MonthYearData> monthYearDataArrayList=new ArrayList<>();
        String date1="01-Jan-1921";
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String date2 = df.format(c);


        Date dateOne = null;
        Date dateTwo = null;
        try {
            DateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            dateOne = format.parse(date1);
            dateTwo = format.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int totalMonth= Helper.monthsBetweenDates(dateOne,dateTwo);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM-yyyy");
        Calendar c1 = new GregorianCalendar();
        c1.setTime(new Date());
        System.out.println(sdf.format(c.getTime()));   // NOW
        String date = sdf.format(c.getTime());
        String[] part = date.split("-");
        MonthYearData monthYearData = new MonthYearData(part[0], part[1], true);
        monthYearDataArrayList.add(monthYearData);
        for (int i = 0; i < totalMonth; i++) {
            c1.add(Calendar.MONTH, -1);
            System.out.println(sdf.format(c1.getTime()));
            String datedata = sdf.format(c1.getTime());
            String[] part1 = datedata.split("-");
            MonthYearData monthYearData1 = new MonthYearData(part1[0], part1[1], false);
            monthYearDataArrayList.add(monthYearData1);
        }
        return monthYearDataArrayList;
    }

    public static String getMonthNumber(String monthName){
        Date date = null;
        try {
            date = new SimpleDateFormat("MMM").parse(monthName);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return String.valueOf(cal.get(Calendar.MONTH)+1);
    }
}
