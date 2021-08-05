
package com.miscos.staffhandler.hrms_management.EmployeeHRMS;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TDS implements Parcelable
{

    @SerializedName("employer_assign")
    @Expose
    private String employerAssign;
    @SerializedName("employee_having")
    @Expose
    private String employeeHaving;
    public final static Creator<TDS> CREATOR = new Creator<TDS>() {


        @SuppressWarnings({
            "unchecked"
        })
        public TDS createFromParcel(Parcel in) {
            return new TDS(in);
        }

        public TDS[] newArray(int size) {
            return (new TDS[size]);
        }

    }
    ;

    protected TDS(Parcel in) {
        this.employerAssign = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeHaving = ((String) in.readValue((String.class.getClassLoader())));
    }

    public TDS() {
    }

    public String getEmployerAssign() {
        return employerAssign;
    }

    public void setEmployerAssign(String employerAssign) {
        this.employerAssign = employerAssign;
    }

    public String getEmployeeHaving() {
        return employeeHaving;
    }

    public void setEmployeeHaving(String employeeHaving) {
        this.employeeHaving = employeeHaving;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(employerAssign);
        dest.writeValue(employeeHaving);
    }

    public int describeContents() {
        return  0;
    }

}
