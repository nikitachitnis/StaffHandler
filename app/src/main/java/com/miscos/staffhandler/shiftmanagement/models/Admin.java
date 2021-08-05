package com.miscos.staffhandler.shiftmanagement.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Admin {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("employee_id")
    @Expose
    private String employeeId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("operation_type")
    @Expose
    private String operationType;
    @SerializedName("employee_no")
    @Expose
    private String employeeNo;

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}
