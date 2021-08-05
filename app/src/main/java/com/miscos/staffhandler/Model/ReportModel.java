package com.miscos.staffhandler.Model;

import java.io.Serializable;

public class ReportModel implements Serializable {
    int s_no,duty_hour,total_working_days,day_in_count,day_out_count,total_working_hours;
    String employee_name,date,day_in,day_out;

    public int getS_no() {
        return s_no;
    }

    public void setS_no(int s_no) {
        this.s_no = s_no;
    }

    public int getDuty_hour() {
        return duty_hour;
    }

    public void setDuty_hour(int duty_hour) {
        this.duty_hour = duty_hour;
    }

    public int getTotal_working_days() {
        return total_working_days;
    }

    public void setTotal_working_days(int total_working_days) {
        this.total_working_days = total_working_days;
    }

    public int getDay_in_count() {
        return day_in_count;
    }

    public void setDay_in_count(int day_in_count) {
        this.day_in_count = day_in_count;
    }

    public int getDay_out_count() {
        return day_out_count;
    }

    public void setDay_out_count(int day_out_count) {
        this.day_out_count = day_out_count;
    }

    public int getTotal_working_hours() {
        return total_working_hours;
    }

    public void setTotal_working_hours(int total_working_hours) {
        this.total_working_hours = total_working_hours;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay_in() {
        return day_in;
    }

    public void setDay_in(String day_in) {
        this.day_in = day_in;
    }

    public String getDay_out() {
        return day_out;
    }

    public void setDay_out(String day_out) {
        this.day_out = day_out;
    }
}
