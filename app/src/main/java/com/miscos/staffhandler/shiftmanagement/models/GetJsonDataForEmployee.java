package com.miscos.staffhandler.shiftmanagement.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetJsonDataForEmployee {
    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("employee_data")
    @Expose
    private List<Employee> employeeData;

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

    public List<Employee> getEmployeeData() {
        return employeeData;
    }

    public void setEmployeeData(List<Employee> employeeData) {
        this.employeeData = employeeData;
    }
}
