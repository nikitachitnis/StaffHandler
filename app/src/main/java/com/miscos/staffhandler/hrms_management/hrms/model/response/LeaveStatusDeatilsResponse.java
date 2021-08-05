package com.miscos.staffhandler.hrms_management.hrms.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveStatusDeatilsResponse {
    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("leave_deatils")
    @Expose
    private String leaveDeatils;
    @SerializedName("deducted_amount")
    @Expose
    private Float deductedAmount;

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

    public String getLeaveDeatils() {
        return leaveDeatils;
    }

    public void setLeaveDeatils(String leaveDeatils) {
        this.leaveDeatils = leaveDeatils;
    }

    public Float getDeductedAmount() {
        return deductedAmount;
    }

    public void setDeductedAmount(Float deductedAmount) {
        this.deductedAmount = deductedAmount;
    }
}
