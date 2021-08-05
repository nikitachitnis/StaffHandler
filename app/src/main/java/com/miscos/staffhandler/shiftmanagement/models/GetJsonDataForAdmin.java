package com.miscos.staffhandler.shiftmanagement.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetJsonDataForAdmin {

    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("admin_emp")
    @Expose
    private List<Admin> admins;
    @SerializedName("non_admin_emp")
    @Expose
    private List<Employee> nonAdmins;

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

    public List<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Admin> admins) {
        this.admins = admins;
    }

    public List<Employee> getNonAdmins() {
        return nonAdmins;
    }

    public void setNonAdmins(List<Employee> nonAdmins) {
        this.nonAdmins = nonAdmins;
    }
}

