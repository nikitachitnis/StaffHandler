package com.miscos.staffhandler.LocalDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.miscos.staffhandler.Model.EmployeeListModel;

public class SqLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "staff_handler.db";
    public static final String TABLE_EMPLOYEE_DETAILS = "tbl_employee_details";

    //Table Employee
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMPLOYEE_ID = "employee_id";
    public static final String COLUMN_EMPLOYER_ID = "employer_id";
    public static final String COLUMN_EMPLOYEE_NAME = "employee_name";
    public static final String COLUMN_PASSWORD = "employee_password";
    public static final String COLUMN_LOGIN_PIN = "login_pin";
    public static final String COLUMN_MOBILE_NUMBER = "employee_mobile";
    public static final String COLUMN_EMPLOYEE_DESIGNATION = "employee_designation";
    public static final String COLUMN_EMPLOYEE_ADDRESS  = "employee_address";
    public static final String COLUMN_EMPLOYEE_EMAIL_ID = "employee_email_id";
    public static final String COLUMN_EMPLOYEE_IMAGE = "employee_image";
    public static final String COLUMN_EMPLOYEE_CREATED_ON = "employee_created_on";
    public static final String COLUMN_EMPLOYEE_AGE = "employee_age";
    public static final String COLUMN_EMPLOYEE_GENDER = "employee_gender";
    public static final String COLUMN_EMPLOYEE_STATUS = "employee_status";
    public static final String COLUMN_EMPLOYEE_DEVICE_ID = "device_id";
    public static final String COLUMN_EMPLOYEE_LOGIN_STATUS = "login_status";
    public static final String COLUMN_EMPLOYEE_DEVICE_OS = "device_os";
    public static final String COLUMN_EMPLOYEE_DEVICE_VERSION = "device_version";
    public static final String COLUMN_EMPLOYEE_NOTIFICATION_STATUS = "notification_status";
    public static final String COLUMN_EMPLOYEE_WEEK_OFF = "week_off";
    public static final String COLUMN_EMPLOYEE_NEW_ARRANGEMENT = "new_arrangement_list";
    public static final String COLUMN_EMPLOYEE_IN_TIMING= "in_timing";
    public static final String COLUMN_EMPLOYEE_OUT_TIMING = "out_timing";
    public static final String COLUMN_EMPLOYEE_PRE_START_TIMING = "pre_start_timing";
    public static final String COLUMN_EMPLOYEE_POST_CLOSE_TIMING = "post_close_timing";
    public static final String COLUMN_EMPLOYEE_SPECIAL_DUTY = "special_duty";
    public static final String COLUMN_EMPLOYEE_TYPE = "type";
    public static final String COLUMN_EMPLOYEE_REGISTER_NEW_EMP = "register_new_emp";
    public static final String COLUMN_EMPLOYEE_EMPLOYEE_REPORT = "employee_report";
    public static final String COLUMN_EMPLOYEE_POLICY_CONFIGURATION = "policy_configuration";
    public static final String COLUMN_EMPLOYEE_NEW_SHIFT_MANAGEMENT = "shift_management";
    public static final String COLUMN_EMPLOYEE_SPECIAL_MANAGEMENT = "special_management";
    public static final String COLUMN_EMPLOYEE_ROSTER_MANAGEMENT = "roster_management";
    public static final String COLUMN_EMPLOYEE_HOLIDAY_ATTENDANCE = "holiday_attendance";
    public static final String COLUMN_EMPLOYEE_HOLIDAY_MANAGEMENT = "holiday_management";
    public static final String COLUMN_EMPLOYEE_OTHER_SETTINGS = "other_settings";
    public static final String COLUMN_EMPLOYEE_VIEW_EMR_LOGIN_LOGOUT = "view_emergency_login_logout";
    public static final String COLUMN_EMPLOYEE_PERMITTED_FOR_EMERGENCY = "permitted_for_emergency";
    public static final String COLUMN_EMPLOYEE_EMPLOYEE_MANUAL_ATTENDANCE = "employee_manual_attendance";
    public static final String COLUMN_EMPLOYEE_OPERATION_TYPE = "operation_type";
    public static final String COLUMN_EMPLOYEE_ALERT_ON_HOLIDAY = "alert_on_holiday";
    public static final String COLUMN_EMPLOYEE_SHIFT_ASSIGN_LIST = "shift_assign_list";
    public static final String COLUMN_EMPLOYEE_DATE_OF_JOINING = "date_of_joining";

    public static final String CREATE_TABLE_EMPLOYEE = "CREATE TABLE " + TABLE_EMPLOYEE_DETAILS + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_ID + " TEXT," +
            COLUMN_EMPLOYEE_ID + " TEXT," +
            COLUMN_EMPLOYER_ID + " TEXT," +
            COLUMN_PASSWORD + " TEXT," +
            COLUMN_LOGIN_PIN + " TEXT," +
            COLUMN_MOBILE_NUMBER + " TEXT," +
            COLUMN_EMPLOYEE_NAME + " TEXT," +
            COLUMN_EMPLOYEE_DESIGNATION + " TEXT," +
            COLUMN_EMPLOYEE_ADDRESS + " INTEGER," +
            COLUMN_EMPLOYEE_EMAIL_ID + " TEXT," +
            COLUMN_EMPLOYEE_IMAGE + " TEXT," +
            COLUMN_EMPLOYEE_CREATED_ON + " TEXT," +
            COLUMN_EMPLOYEE_AGE + " TEXT," +
            COLUMN_EMPLOYEE_GENDER + " TEXT," +
            COLUMN_EMPLOYEE_STATUS + " TEXT," +
            COLUMN_EMPLOYEE_DEVICE_ID + " TEXT," +
            COLUMN_EMPLOYEE_LOGIN_STATUS + " TEXT," +
            COLUMN_EMPLOYEE_DEVICE_OS + " TEXT," +
            COLUMN_EMPLOYEE_DEVICE_VERSION + " TEXT," +
            COLUMN_EMPLOYEE_NOTIFICATION_STATUS + " TEXT," +
            COLUMN_EMPLOYEE_WEEK_OFF + " TEXT," +
            COLUMN_EMPLOYEE_NEW_ARRANGEMENT + " TEXT," +
            COLUMN_EMPLOYEE_IN_TIMING + " TEXT," +
            COLUMN_EMPLOYEE_OUT_TIMING + " TEXT," +
            COLUMN_EMPLOYEE_PRE_START_TIMING + " TEXT," +
            COLUMN_EMPLOYEE_POST_CLOSE_TIMING + " TEXT," +
            COLUMN_EMPLOYEE_SPECIAL_DUTY + " TEXT," +
            COLUMN_EMPLOYEE_TYPE + " TEXT," +
            COLUMN_EMPLOYEE_REGISTER_NEW_EMP + " TEXT," +
            COLUMN_EMPLOYEE_EMPLOYEE_REPORT + " TEXT," +
            COLUMN_EMPLOYEE_POLICY_CONFIGURATION + " TEXT," +
            COLUMN_EMPLOYEE_NEW_SHIFT_MANAGEMENT + " TEXT," +
            COLUMN_EMPLOYEE_SPECIAL_MANAGEMENT + " TEXT," +
            COLUMN_EMPLOYEE_ROSTER_MANAGEMENT + " TEXT," +
            COLUMN_EMPLOYEE_HOLIDAY_ATTENDANCE + " TEXT," +
            COLUMN_EMPLOYEE_HOLIDAY_MANAGEMENT + " TEXT," +
            COLUMN_EMPLOYEE_OTHER_SETTINGS + " TEXT," +
            COLUMN_EMPLOYEE_VIEW_EMR_LOGIN_LOGOUT + " TEXT," +
            COLUMN_EMPLOYEE_PERMITTED_FOR_EMERGENCY + " TEXT," +
            COLUMN_EMPLOYEE_EMPLOYEE_MANUAL_ATTENDANCE + " TEXT," +
            COLUMN_EMPLOYEE_OPERATION_TYPE + " TEXT," +
            COLUMN_EMPLOYEE_ALERT_ON_HOLIDAY + " TEXT," +
            COLUMN_EMPLOYEE_SHIFT_ASSIGN_LIST + " TEXT,"+
            COLUMN_EMPLOYEE_DATE_OF_JOINING +  " TEXT);";




    private static final int DATABASE_VERSION = 3;
    private SQLiteDatabase database;

    public SqLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_EMPLOYEE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tbl_employee_details");
        onCreate(db);
    }

    public boolean addEmployeeDetails(EmployeeListModel employeeListModel) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, employeeListModel.getEm_id());
        contentValues.put(COLUMN_EMPLOYEE_ID, employeeListModel.getEmployee_id());
        contentValues.put(COLUMN_EMPLOYER_ID, employeeListModel.getEmployer_id());
        contentValues.put(COLUMN_PASSWORD, employeeListModel.getPassword());
        contentValues.put(COLUMN_LOGIN_PIN, employeeListModel.getLogin_pin());
        contentValues.put(COLUMN_MOBILE_NUMBER, employeeListModel.getMobile_no());
        contentValues.put(COLUMN_EMPLOYEE_NAME, employeeListModel.getEmployee_name());
        contentValues.put(COLUMN_EMPLOYEE_DESIGNATION, employeeListModel.getDesignation());
        contentValues.put(COLUMN_EMPLOYEE_ADDRESS, employeeListModel.getAddress());
        contentValues.put(COLUMN_EMPLOYEE_EMAIL_ID, employeeListModel.getEmail_id());
        contentValues.put(COLUMN_EMPLOYEE_IMAGE, employeeListModel.getEmployee_pic());
        contentValues.put(COLUMN_EMPLOYEE_CREATED_ON, employeeListModel.getCreated_on());
        contentValues.put(COLUMN_EMPLOYEE_AGE, employeeListModel.getAge());
        contentValues.put(COLUMN_EMPLOYEE_GENDER, employeeListModel.getGender());
        contentValues.put(COLUMN_EMPLOYEE_STATUS, employeeListModel.getStatus());
        contentValues.put(COLUMN_EMPLOYEE_DEVICE_ID, employeeListModel.getDevice_id());
        contentValues.put(COLUMN_EMPLOYEE_LOGIN_STATUS, employeeListModel.getLogin_status());
        contentValues.put(COLUMN_EMPLOYEE_DEVICE_OS, employeeListModel.getDevice_os());
        contentValues.put(COLUMN_EMPLOYEE_DEVICE_VERSION, employeeListModel.getDevice_version());
        contentValues.put(COLUMN_EMPLOYEE_NOTIFICATION_STATUS, employeeListModel.getNotification_status());
        contentValues.put(COLUMN_EMPLOYEE_WEEK_OFF, employeeListModel.getWeek_off());
        contentValues.put(COLUMN_EMPLOYEE_NEW_ARRANGEMENT, employeeListModel.getRegister_new_emp());
        contentValues.put(COLUMN_EMPLOYEE_IN_TIMING, employeeListModel.getIn_timing());
        contentValues.put(COLUMN_EMPLOYEE_OUT_TIMING, employeeListModel.getOut_timing());
        contentValues.put(COLUMN_EMPLOYEE_PRE_START_TIMING, employeeListModel.getPre_start_timing());
        contentValues.put(COLUMN_EMPLOYEE_POST_CLOSE_TIMING, employeeListModel.getPost_close_timing());
        contentValues.put(COLUMN_EMPLOYEE_SPECIAL_DUTY, employeeListModel.getSpecial_duty());
        contentValues.put(COLUMN_EMPLOYEE_TYPE, employeeListModel.getEmployeeType());
        contentValues.put(COLUMN_EMPLOYEE_REGISTER_NEW_EMP, employeeListModel.getRegister_new_emp());
        contentValues.put(COLUMN_EMPLOYEE_EMPLOYEE_REPORT, employeeListModel.getEmployee_report());
        contentValues.put(COLUMN_EMPLOYEE_POLICY_CONFIGURATION, employeeListModel.getPolicy_configuration());
        contentValues.put(COLUMN_EMPLOYEE_NEW_SHIFT_MANAGEMENT, employeeListModel.getShift_management());
        contentValues.put(COLUMN_EMPLOYEE_SPECIAL_MANAGEMENT, employeeListModel.getSpecial_management());
        contentValues.put(COLUMN_EMPLOYEE_ROSTER_MANAGEMENT, employeeListModel.getRoster_management());
        contentValues.put(COLUMN_EMPLOYEE_HOLIDAY_ATTENDANCE, employeeListModel.getHoliday_attendance());
        contentValues.put(COLUMN_EMPLOYEE_HOLIDAY_MANAGEMENT, employeeListModel.getHoliday_management());
        contentValues.put(COLUMN_EMPLOYEE_OTHER_SETTINGS, employeeListModel.getOther_settings());
        contentValues.put(COLUMN_EMPLOYEE_VIEW_EMR_LOGIN_LOGOUT, employeeListModel.getView_emergency_login_logout());
        contentValues.put(COLUMN_EMPLOYEE_PERMITTED_FOR_EMERGENCY, employeeListModel.getPermitted_for_emergency());
        contentValues.put(COLUMN_EMPLOYEE_EMPLOYEE_MANUAL_ATTENDANCE, employeeListModel.getEmployee_manual_attendance());
        contentValues.put(COLUMN_EMPLOYEE_OPERATION_TYPE, employeeListModel.getOperation_type());
        contentValues.put(COLUMN_EMPLOYEE_ALERT_ON_HOLIDAY, employeeListModel.getAlert_on_holiday());
        contentValues.put(COLUMN_EMPLOYEE_SHIFT_ASSIGN_LIST, employeeListModel.getShift_assign_list());
        contentValues.put(COLUMN_EMPLOYEE_DATE_OF_JOINING, employeeListModel.getDate_of_joining_date());

        long row = database.insert(TABLE_EMPLOYEE_DETAILS, null, contentValues);
        return row > 0;
    }

    public Cursor getEmployeelist() {
        String select_query = "select " + COLUMN_EMPLOYEE_ID + " , " + COLUMN_EMPLOYEE_NAME + " , " + COLUMN_EMPLOYEE_DATE_OF_JOINING +" from " + TABLE_EMPLOYEE_DETAILS;
        Log.d("sql_query", select_query);
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(select_query, null);
        return cursor;
    }

    public Cursor getSinlgeEntry(String password)
    {
        String label = "";
        String select_query = "select " + COLUMN_EMPLOYEE_ID + " , " + COLUMN_EMPLOYEE_NAME + " from " + TABLE_EMPLOYEE_DETAILS + " WHERE " + COLUMN_EMPLOYEE_OPERATION_TYPE + " ='" + password + "'";
        Log.d("sql_query", select_query);
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(select_query, null);
        return cursor;
    }

    public void deleteEmployee() {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_EMPLOYEE_DETAILS, null, null);
        database.close();
    }

}
