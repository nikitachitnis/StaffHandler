package com.miscos.staffhandler.hrms_management.hrms_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class salary_structure implements Serializable {
    @SerializedName("name")
    @Expose
    public String struct_name;

    public String amount;
    @SerializedName("status")
    @Expose
    public String status;

    public String index;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getStruct_name() {
        return struct_name;
    }

    public void setStruct_name(String struct_name) {
        this.struct_name = struct_name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

