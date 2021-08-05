
package com.miscos.staffhandler.employee.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeList implements Parcelable
{

    @SerializedName("employer_id")
    @Expose
    private String employerId;
    @SerializedName("employee_id")
    @Expose
    private String employeeId;
    @SerializedName("employee_no")
    @Expose
    private String employeeNo;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile_no")
    @Expose
    private String mobile_no;
    @SerializedName("date_of_joining")
    @Expose
    private String dateOfJoining;
    @SerializedName("date_of_leaving")
    @Expose
    private String dateOfLeaving;
    public final static Creator<EmployeeList> CREATOR = new Creator<EmployeeList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public EmployeeList createFromParcel(Parcel in) {
            return new EmployeeList(in);
        }

        public EmployeeList[] newArray(int size) {
            return (new EmployeeList[size]);
        }

    }
    ;

    protected EmployeeList(Parcel in) {
        this.employerId = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeId = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.dateOfJoining = ((String) in.readValue((String.class.getClassLoader())));
        this.dateOfLeaving = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeNo = ((String) in.readValue((String.class.getClassLoader())));
    }

    public EmployeeList() {
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getDateOfLeaving() {
        return dateOfLeaving;
    }

    public void setDateOfLeaving(String dateOfLeaving) {
        this.dateOfLeaving = dateOfLeaving;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(employerId);
        dest.writeValue(employeeId);
        dest.writeValue(name);
        dest.writeValue(dateOfJoining);
        dest.writeValue(dateOfLeaving);
        dest.writeValue(employeeNo);
    }

    public int describeContents() {
        return  0;
    }

}
