package com.miscos.staffhandler.hrms_management.hrms.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OtherDeductionResponse {

    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("other_deduction_detail")
    @Expose
    private OtherDeductionDetail otherDeductionDetail;

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

    public OtherDeductionDetail getOtherDeductionDetail() {
        return otherDeductionDetail;
    }

    public void setOtherDeductionDetail(OtherDeductionDetail otherDeductionDetail) {
        this.otherDeductionDetail = otherDeductionDetail;
    }


    public class OtherDeductionDetail {

        @SerializedName("data")
        @Expose
        private ArrayList<DeductionData> data = null;
        @SerializedName("total")
        @Expose
        private Integer total;

        public ArrayList<DeductionData> getData() {
            return data;
        }

        public void setData(ArrayList<DeductionData> data) {
            this.data = data;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

    }

    public class DeductionData {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("amount")
        @Expose
        private String amount;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

    }
}
