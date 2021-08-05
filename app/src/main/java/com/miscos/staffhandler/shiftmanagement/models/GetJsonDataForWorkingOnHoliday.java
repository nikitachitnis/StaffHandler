package com.miscos.staffhandler.shiftmanagement.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetJsonDataForWorkingOnHoliday {
    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("grant_access_data")
    @Expose
    private List<Employee> grantAccessData;
    @SerializedName("not_granted_access_data")
    @Expose
    private List<Employee> notGrantedAccessData;

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

    public List<Employee> getGrantAccessData() {
        return grantAccessData;
    }

    public void setGrantAccessData(List<Employee> grantAccessData) {
        this.grantAccessData = grantAccessData;
    }

    public List<Employee> getNotGrantedAccessData() {
        return notGrantedAccessData;
    }

    public void setNotGrantedAccessData(List<Employee> notGrantedAccessData) {
        this.notGrantedAccessData = notGrantedAccessData;
    }
}
