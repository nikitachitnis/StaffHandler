package com.miscos.staffhandler.shiftmanagement.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GetJsonDataForAuthority {
    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("authoriry_data")
    @Expose
    private AuthorityData authorityData;

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

    public AuthorityData getAuthorityData() {
        return authorityData;
    }

    public void setAuthorityData(AuthorityData authorityData) {
        this.authorityData = authorityData;
    }
}
