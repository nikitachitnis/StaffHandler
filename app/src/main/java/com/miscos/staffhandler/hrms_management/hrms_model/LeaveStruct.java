
package com.miscos.staffhandler.hrms_management.hrms_model;

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
    @SerializedName("no_of_leaves")
    @Expose
    private String noOfLeaves;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("leave_type")
    @Expose
    private String leaveType;
    @SerializedName("status")
    @Expose
    private String leaveStatus;

    public String getReset_date() {
        return reset_date;
    }

    public void setReset_date(String reset_date) {
        this.reset_date = reset_date;
    }

    private String reset_date;

    public String getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(String leaveStatus) {
        this.leaveStatus = leaveStatus;
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

    protected LeaveStruct(Parcel in)
    {
        this.leaveName = ((String) in.readValue((String.class.getClassLoader())));
        this.carryForwardable = ((String) in.readValue((String.class.getClassLoader())));
        this.noOfLeaves = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.leaveType = ((String) in.readValue((Object.class.getClassLoader())));
        this.reset_date = ((String) in.readValue((Object.class.getClassLoader())));
        this.leaveStatus=((String) in.readValue((Object.class.getClassLoader())));
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

    public String getNoOfLeaves() {
        return noOfLeaves;
    }

    public void setNoOfLeaves(String noOfLeaves) {
        this.noOfLeaves = noOfLeaves;
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(leaveName);
        dest.writeValue(carryForwardable);
        dest.writeValue(noOfLeaves);
        dest.writeValue(type);
        dest.writeValue(leaveType);
        dest.writeValue(reset_date);
        dest.writeValue(leaveStatus);
    }

    public int describeContents() {
        return  0;
    }

}
