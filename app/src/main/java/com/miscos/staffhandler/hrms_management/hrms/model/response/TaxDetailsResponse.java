package com.miscos.staffhandler.hrms_management.hrms.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaxDetailsResponse {

    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("tax_detail_list")
    @Expose
    private TaxDetailList taxDetailList;

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

    public TaxDetailList getTaxDetailList() {
        return taxDetailList;
    }

    public void setTaxDetailList(TaxDetailList taxDetailList) {
        this.taxDetailList = taxDetailList;
    }

    public static class TaxDetailList {

        @SerializedName("Provident_Found")
        @Expose
        private String providentFound;
        @SerializedName("Professional_Tax")
        @Expose
        private String professionalTax;
        @SerializedName("ESIC")
        @Expose
        private String eSIC;
        @SerializedName("GST")
        @Expose
        private String gST;
        @SerializedName("TDS")
        @Expose
        private String tDS;

        public String getProvidentFound() {
            return providentFound;
        }

        public void setProvidentFound(String providentFound) {
            this.providentFound = providentFound;
        }

        public String getProfessionalTax() {
            return professionalTax;
        }

        public void setProfessionalTax(String professionalTax) {
            this.professionalTax = professionalTax;
        }

        public String getESIC() {
            return eSIC;
        }

        public void setESIC(String eSIC) {
            this.eSIC = eSIC;
        }

        public String getGST() {
            return gST;
        }

        public void setGST(String gST) {
            this.gST = gST;
        }

        public String getTDS() {
            return tDS;
        }

        public void setTDS(String tDS) {
            this.tDS = tDS;
        }
    }
}
