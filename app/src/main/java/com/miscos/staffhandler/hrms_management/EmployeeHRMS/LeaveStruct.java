
package com.miscos.staffhandler.hrms_management.EmployeeHRMS;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveStruct implements Parcelable
{

    @SerializedName("leave_name")
    @Expose
    private String leaveName;
    @SerializedName("carry_forwardable")
    @Expose
    private String carryForwardable;
    @SerializedName("employer_assign_no_of_leaves")
    @Expose
    private String employerAssignNoOfLeaves;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("leave_type")
    @Expose
    private String leaveType;
    @SerializedName("employee_having_no_of_leaves")
    @Expose
    private String employeeHavingNoOfLeaves;
    @SerializedName("index")
    @Expose
    private int index;

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public final static Creator<LeaveStruct> CREATOR = new Creator<LeaveStruct>() {


        @SuppressWarnings({
            "unchecked"
        })
        public LeaveStruct createFromParcel(Parcel in) {
            return new LeaveStruct(in);
        }

        public LeaveStruct[] newArray(int size) {
            return (new LeaveStruct[size]);
        }

    }
    ;

    protected LeaveStruct(Parcel in) {
        this.leaveName = ((String) in.readValue((String.class.getClassLoader())));
        this.carryForwardable = ((String) in.readValue((String.class.getClassLoader())));
        this.employerAssignNoOfLeaves = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.leaveType = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeHavingNoOfLeaves = ((String) in.readValue((String.class.getClassLoader())));
    }

    public LeaveStruct() {
    }

    public String getLeaveName() {
        return leaveName;
    }

    public void setLeaveName(String leaveName) {
        this.leaveName = leaveName;
    }

    public String getCarryForwardable() {
        return carryForwardable;
    }

    public void setCarryForwardable(String carryForwardable) {
        this.carryForwardable = carryForwardable;
    }

    public String getEmployerAssignNoOfLeaves() {
        return employerAssignNoOfLeaves;
    }

    public void setEmployerAssignNoOfLeaves(String employerAssignNoOfLeaves) {
        this.employerAssignNoOfLeaves = employerAssignNoOfLeaves;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getEmployeeHavingNoOfLeaves() {
        return employeeHavingNoOfLeaves;
    }

    public void setEmployeeHavingNoOfLeaves(String employeeHavingNoOfLeaves) {
        this.employeeHavingNoOfLeaves = employeeHavingNoOfLeaves;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(leaveName);
        dest.writeValue(carryForwardable);
        dest.writeValue(employerAssignNoOfLeaves);
        dest.writeValue(type);
        dest.writeValue(leaveType);
        dest.writeValue(employeeHavingNoOfLeaves);
    }

    public int describeContents() {
        return  0;
    }

}
