package com.miscos.staffhandler.hrms_management.hrms.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CurrentLeaveStatusData {

    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("leave_data")
    @Expose
    private ArrayList<LeaveData> leaveData = null;

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

    public ArrayList<LeaveData> getLeaveData() {
        return leaveData;
    }

    public void setLeaveData(ArrayList<LeaveData> leaveData) {
        this.leaveData = leaveData;
    }



}
