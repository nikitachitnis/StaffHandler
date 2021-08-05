package com.miscos.staffhandler.employer_java.remote.model.designationlist.employeedesignationwise;

import com.google.gson.annotations.SerializedName;

public class EmployeeData {

    @SerializedName("employee_id")
    private String employeeId;

    @SerializedName("name")
    private String name;

    @SerializedName("status")
    private String status;

    public String getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        if (status!=null)
            return status.replace("_"," ");
        else return "";
    }

    @Override
    public String toString() {
        return "EmployeeData{" +
                "employeeId='" + employeeId + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
