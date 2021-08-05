package com.miscos.staffhandler.shiftmanagement.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetJsonDataForSpecialTiming {
    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("shift_data")
    @Expose
    private SpecialTiming shiftData;

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

    public SpecialTiming getShiftData() {
        return shiftData;
    }

    public void setShiftData(SpecialTiming shiftData) {
        this.shiftData = shiftData;
    }
}
