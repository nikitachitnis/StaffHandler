package com.miscos.staffhandler.shiftmanagement.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Roster {


    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("employee_data")
    @Expose
    private String employeeData;
    @SerializedName("new_arrange_list")
    @Expose
    private NewArrangeList newArrangeList;
    public final static Parcelable.Creator<Roster> CREATOR = new Parcelable.Creator<Roster>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Roster createFromParcel(Parcel in) {
            return new Roster(in);
        }

        public Roster[] newArray(int size) {
            return (new Roster[size]);
        }

    }
            ;

    protected Roster(Parcel in) {
        this.errorCode = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeData = ((String) in.readValue((String.class.getClassLoader())));
        this.newArrangeList = ((NewArrangeList) in.readValue((NewArrangeList.class.getClassLoader())));
    }

    public Roster() {
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

    public String getEmployeeData() {
        return employeeData;
    }

    public void setEmployeeData(String employeeData) {
        this.employeeData = employeeData;
    }

    public NewArrangeList getNewArrangeList() {
        return newArrangeList;
    }

    public void setNewArrangeList(NewArrangeList newArrangeList) {
        this.newArrangeList = newArrangeList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(errorCode);
        dest.writeValue(message);
        dest.writeValue(employeeData);
        dest.writeValue(newArrangeList);
    }

    public int describeContents() {
        return  0;
    }
}