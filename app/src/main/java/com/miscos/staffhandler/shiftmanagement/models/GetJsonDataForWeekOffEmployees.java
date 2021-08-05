package com.miscos.staffhandler.shiftmanagement.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetJsonDataForWeekOffEmployees {
    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("week_off_employee_list")
    @Expose
    private List<Employee> WeekOffEmployeeList;

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

    public List<Employee> getWeekOffEmployeeList() {
        return WeekOffEmployeeList;
    }

    public void setWeekOffEmployeeList(List<Employee> weekOffEmployeeList) {
        WeekOffEmployeeList = weekOffEmployeeList;
    }
}
