package com.miscos.staffhandler.employee.helper;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PreferenceManager
{
    public static final String LOGIN="prefs";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_DEVICE_TOKEN= "device_token";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ATTENDANCETYPE = "attendanceType";
    public static final String KEY_PERMITTEDDISTANCE = "permitted_distance";
    public static final String KEY_OFFICE_GPS_PLOTTING = "gps_plotting";
    public static final String KEY_GPS_METHOD = "gps_method";
    public static final String KEY_EMPLOYERID = "employerid";
    public static final String KEY_EMPLOYER_NAME = "employer_name";
    public static final String KEY_EMPLOYEEID = "employeeId";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_EMPLOYEE_LATITUDE_LONGITUDE = "employee_longitude_latitude";
    public static final String KEY_CURRENT_QR = "current_qr";
    public static final String KEY_OFFICE_STATUS = "office_status";
    public static final String KEY_EMPLOYEE_STATUS = "employee_status";
    public static final String KEY_SET_UP_PIN = "setuppin";
    public static final String KEY_DATA_SPINNER = "spinner_data";
    public static final String KEY_ATTENDANCE = "attendance";
    public static final String KEY_EMPLOYEE_COUNT = "emp_count";
    public static final String KEY_DATE_COUNT = "date_count";
    public static final String KEY_ORDER_ID = "order_id";
    public static final String KEY_PAYMENT_ID = "payment_id";
    public static final String KEY_PAYABLE_AMT = "payable_amt";
    public static final String KEY_MOBILE_NUMBER = "mobile_no";
    public static final String KEY_GETA = "pdfA";
    public static final String KEY_GETB = "pdfB";
    public static final String KEY_GETC = "pdfC";
    public static final String KEY_GETD = "pdfD";
    public static final String KEY_GETE = "pdfE";
    public static final String KEY_GETF = "pdfF";
    public static final String KEY_GETG = "pdfG";
    public static final String KEY_GETH = "pdfH";
    public static final String KEY_GETI = "pdfI";
    public static final String KEY_GETJ = "pdfJ";
    public static final String KEY_GETK = "pdfK";
    public static final String KEY_GETM = "pdfM";
    public static final String KEY_GETL = "pdfL";
    public static final String KEY_WEEK_OFF = "week_off";
    public static final String KEY_NEW_ARRANGEMENT_LIST = "new_arrangement_list";
    public static final String KEY_IN_TIMING = "in_timing";
    public static final String KEY_EMPLOYEE_IN_TIMING = "emp_in_timing";
    public static final String KEY_OUT_TIMING = "out_timing";
    public static final String KEY_EMPLOYEE_OUT_TIMING = "emp_out_timing";
    public static final String KEY_PRE_START = "pre_start_timing";
    public static final String KEY_EMPLOYEE_PRE_START = "emp_pre_start_timing";
    public static final String KEY_POST_CLOSE = "post_close_timing";
    public static final String KEY_EMPLOYEE_POST_CLOSE = "emp_post_close_timing";
    public static final String KEY_SPECIAL_DUTY = "special_duty";
    public static final String KEY_SPECIAL_DUTY_APPLICABLE_DATE = "special_duty_applicable_from";
    public static final String KEY_EMPLOYEE_TYPE = "type";
    public static final String KEY_REGISTER_NEW_EMPLOYEE = "register_new_emp";
    public static final String KEY_EMPLOYEE_REPORT = "employee_report";
    public static final String KEY_POLICY_CONFIGURATION = "policy_configuration";
    public static final String KEY_SHIFT_MANAGEMENT = "shift_management";
    public static final String KEY_SPECIAL_MANAGEMENT = "special_management";
    public static final String KEY_ROSTER_MANAGEMENT = "roster_management";
    public static final String KEY_HOLIDAY_ATTENDANCE = "holiday_attendance";
    public static final String KEY_HOLIDAY_MANAGEMENT = "holiday_management";
    public static final String KEY_OTHER_SETTINGS = "other_settings";
    public static final String KEY_EMER_LOGIN_LOGOUT = "view_emergency_login_logout";
    public static final String KEY_PERMITTED_FOR_EMER = "permitted_for_emergency";
    public static final String KEY_OPERATION_TYPE = "operation_type";
    public static final String KEY_OPERATION_TYPE1 = "operation_type1";
    public static final String KEY_ALERT_ON_HOLIDAY = "alert_on_holiday";
    public static final String KEY_WIFI_NAMES = "wifi_names";
    public static final String KEY_WIFI_MANAGEMENT = "wifi_management";
    public static final String KEY_EMPLOYEE_SHIFTS = "shift_no";

    public static final String KEY_LEAVE_MANAGEMENT = "leave_management";
    public static final String KEY_SALARY_MANAGEMENT = "salary_management";
    public static final String KEY_OLD_EMP_DATA = "old_emp_data";

    public static final String KEY_AADHAR_VERIFY_POLICY = "is_aadhar_verify";
    public static final String KEY_EMPLOYEE_NO_TYPE = "employee_no_type";
    public static final String KEY_ENABLE_EMPLOYEE_FEEDBACK= "enable_emp_feedback";
    public static final String KEY_SHIFT_COUNT= "shift_count";
    public static final String KEY_ADHAAR_ENABLED = "is_aadhar_enabled";
    public static final String KEY_MANUAL_ATTENDANCE = "manual_attendance";//y/n
    public static final String KEY_MANUAL_ATTENDANCE_TYPE="manual_attendance_type";
    public static final String KEY_HRMS_CONFIGURATION="hrms_configuration";
    public static final String KEY_SALARY_MANAGEMENT_SETTING="salary_management_setting";
    public static final String KEY_SALARY_REPORT_SETTING="salary_report_setting";
    public static final String KEY_LEAVE_MANAGEMENT_SETTING="leave_management_setting";
    public static final String KEY_SUPERVISOR_NAME="supervisor_name";
    public static final String KEY_ADHAR_LICENSECOUNT="licensecount";
    public static final String KEY_ADHAR_LICENSE_AMOUNT="amount";
    public static final String KEY_IS_OFFICE_OUT="is_office_out";
    Context mContext;
    SharedPreferences mSharedPreferences;

    public PreferenceManager(Context mContext)
    {
        this.mContext = mContext;
        getSharedPreferences();
    }

    public static SharedPreferences getDefaultSharedPreferences(Context context) {

        return null;
    }


    public  SharedPreferences getSharedPreferences()
    {
        mSharedPreferences=mContext.getSharedPreferences(LOGIN, MODE_PRIVATE);
        return mSharedPreferences;
    }


    SharedPreferences.Editor getEditor()
    {
        return  mSharedPreferences.edit();
    }

    public void setPreference(String key,String value)
    {
        SharedPreferences.Editor editor=getEditor();
        editor.putString(key,value);
        editor.apply();

    }

    public void setPreference(String key,boolean value)
    {
        SharedPreferences.Editor editor=getEditor();
        editor.putBoolean(key,value);
        editor.apply();

    }

    public void setPreference(String key,long value)
    {
        SharedPreferences.Editor editor=getEditor();
        editor.putLong(key,value);
        editor.apply();

    }
    public void setPreference(String key,int value)
    {
        SharedPreferences.Editor editor=getEditor();
        editor.putInt(key,value);
        editor.apply();

    }
    public void setIntPreference(String key,int value)
    {
        SharedPreferences.Editor editor=getEditor();
        editor.putInt(key,value);
        editor.apply();

    }

    public String getStringPreference(String key)
    {
        if(mSharedPreferences.contains(key))
        {
            return  mSharedPreferences.getString(key,"");
        }
        return "";
    }
    public boolean getBooleanPreference(String key)
    {
        if(mSharedPreferences.contains(key))
        {
            return  mSharedPreferences.getBoolean(key,false);
        }
        return false;
    }

}

