
package com.miscos.staffhandler.hrms_management.EmployeeHRMS;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfessionalTax implements Parcelable
{

    @SerializedName("employer_assign")
    @Expose
    private String employerAssign;
    @SerializedName("employee_having")
    @Expose
    private String employeeHaving;
    public final static Creator<ProfessionalTax> CREATOR = new Creator<ProfessionalTax>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ProfessionalTax createFromParcel(Parcel in) {
            return new ProfessionalTax(in);
        }

        public ProfessionalTax[] newArray(int size) {
            return (new ProfessionalTax[size]);
        }

    }
    ;

    protected ProfessionalTax(Parcel in) {
        this.employerAssign = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeHaving = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ProfessionalTax() {
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
