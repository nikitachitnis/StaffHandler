package com.miscos.staffhandler.employee.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Employee {

    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("employer_data")
    @Expose
    private List<EmployerDatum> employerData = null;
    @SerializedName("employee_data")
    @Expose
    private EmployeeData employeeData;
    @SerializedName("office_status")
    @Expose
    private String officeStatus;
    @SerializedName("employee_status")
    @Expose
    private String employeeStatus;
    @SerializedName("manual")
    @Expose
    private List<Manual> manualList = null;
    @SerializedName("shift_no")
    @Expose
    private String shiftNo;
    @SerializedName("hrms_congifured")
    @Expose
    private String hrms_congifured;

    public String getHrms_congifured() {
        return hrms_congifured;
    }

    public void setHrms_congifured(String hrms_congifured) {
        this.hrms_congifured = hrms_congifured;
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

    public List<EmployerDatum> getEmployerData() {
        return employerData;
    }

    public void setEmployerData(List<EmployerDatum> employerData) {
        this.employerData = employerData;
    }

    public EmployeeData getEmployeeData() {
        return employeeData;
    }

    public void setEmployeeData(EmployeeData employeeData) {
        this.employeeData = employeeData;
    }

    public String getOfficeStatus() {
        return officeStatus;
    }

    public void setOfficeStatus(String officeStatus) {
        this.officeStatus = officeStatus;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }
    public List<Manual> getManualList() {
        return manualList;
    }
    public void setManualList(List<Manual> manualList) {
        this.manualList = manualList;
    }
    public String getShiftNo() {
        return shiftNo;
    }

    public void setShiftNo(String shiftNo) {
        this.shiftNo = shiftNo;
    }
}