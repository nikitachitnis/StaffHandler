package com.miscos.staffhandler.employer_java.remote.model;

import com.google.gson.annotations.SerializedName;

public class NormalResponse {

    @SerializedName("message")
    String message = "";

    @SerializedName("qrscan_img")
    String qrscan_img= "";

    @SerializedName("employer_id")
    String employer_id = "";

    @SerializedName("status")
    int status;

    @SerializedName("error_code")
    int error_code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getQrscan_img() {
        return qrscan_img;
    }

    public void setQrscan_img(String qrscan_img) {
        this.qrscan_img = qrscan_img;
    }

    public String getEmployer_id() {
        return employer_id;
    }

    public void setEmployer_id(String employer_id) {
        this.employer_id = employer_id;
    }

    public int getStatus() {
        return status;
    }

    public int getError_code() {
        return error_code;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
