package com.miscos.staffhandler.shiftmanagement.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetJsonDataForWifi {
    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("wifi_names")
    @Expose
    private String wifiNames;
    @SerializedName("gps_with_wifi")
    @Expose
    private String gpsWithWifi;
    @SerializedName("alert_on_holiday")
    @Expose
    private String alertOnHoliday;
    @SerializedName("absent_leave_deduction")
    @Expose
    private String absent_leave_deduction;
    @SerializedName("enable_salary_management")
    @Expose
    private String enable_salary_management;
    @SerializedName("enable_leave_management")
    @Expose
    private String enable_leave_management;
    @SerializedName("enable_show_salary_detail")
    @Expose
    private String enable_show_salary_detail;

    public String getEnable_salary_management()
    {
        return enable_salary_management;
    }

    public void setEnable_salary_management(String enable_salary_management)
    {
        this.enable_salary_management = enable_salary_management;
    }

    public String getEnable_leave_management()
    {
        return enable_leave_management;
    }

    public void setEnable_leave_management(String enable_leave_management) {
        this.enable_leave_management = enable_leave_management;
    }

    public String getEnable_show_salary_detail() {
        return enable_show_salary_detail;
    }

    public void setEnable_show_salary_detail(String enable_show_salary_detail) {
        this.enable_show_salary_detail = enable_show_salary_detail;
    }

    public String getAbsent_leave_deduction() {
        return absent_leave_deduction;
    }

    public void setAbsent_leave_deduction(String absent_leave_deduction) {
        this.absent_leave_deduction = absent_leave_deduction;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWifiNames() {
        return wifiNames;
    }

    public void setWifiNames(String wifiNames) {
        this.wifiNames = wifiNames;
    }

    public String getGpsWithWifi() {
        return gpsWithWifi;
    }

    public void setGpsWithWifi(String gpsWithWifi) {
        this.gpsWithWifi = gpsWithWifi;
    }

    public String getAlertOnHoliday() {
        return alertOnHoliday;
    }

    public void setAlertOnHoliday(String alertOnHoliday) {
        this.alertOnHoliday = alertOnHoliday;
    }
}

