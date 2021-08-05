package com.miscos.staffhandler.hrms_management.hrms.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EmployeeLIst {
    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("employee_data")
    @Expose
    private ArrayList<EmployeeData> employeeData = null;
    @SerializedName("pending_leave_employee_list")
    @Expose
    private ArrayList<EmployeeData> pendingemployeeData = null;

    public ArrayList<EmployeeData> getPendingemployeeData() {
        return pendingemployeeData;
    }

    public void setPendingemployeeData(ArrayList<EmployeeData> pendingemployeeData) {
        this.pendingemployeeData = pendingemployeeData;
    }

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

    public ArrayList<EmployeeData> getEmployeeData() {
        return employeeData;
    }

    public void setEmployeeData(ArrayList<EmployeeData> employeeData) {
        this.employeeData = employeeData;
    }


    public static class EmployeeData {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("employee_id")
        @Expose
        private String employeeId;
        @SerializedName("designation")
        @Expose
        private String designation;
        @SerializedName("employee_no")
        @Expose
        private String employee_no;

        public String getEmployee_no() {
            return employee_no;
        }

        public void setEmployee_no(String employee_no) {
            this.employee_no = employee_no;
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

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public void setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
        }

    }
}
