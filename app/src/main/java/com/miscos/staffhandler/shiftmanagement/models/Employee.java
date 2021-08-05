package com.miscos.staffhandler.shiftmanagement.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Employee {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("employee_id")
    @Expose
    private String employeeId;
    @SerializedName("week_off")
    @Expose
    private String weekOff;
    @SerializedName("employee_no")
    @Expose
    private String employeeNo="";

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }
    private String invite_type;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getWeekOff() {
        return weekOff;
    }

    public String getInvite_type() {
        return invite_type;
    }

    public void setInvite_type(String invite_type) {
        this.invite_type = invite_type;
    }

    public void setWeekOff(String weekOff) {
        this.weekOff = weekOff;
    }
}
