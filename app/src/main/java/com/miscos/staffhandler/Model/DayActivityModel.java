package com.miscos.staffhandler.Model;

import java.io.Serializable;

public class DayActivityModel implements Serializable {

    int s_no;
    String date,day_in,day_out,employee_name,attendance_type,employee_id;

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getAttendance_type() {
        return attendance_type;
    }

    public void setAttendance_type(String attendance_type) {
        this.attendance_type = attendance_type;
    }

    public int getS_no() {
        return s_no;
    }

    public void setS_no(int s_no) {
        this.s_no = s_no;
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

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }
}
