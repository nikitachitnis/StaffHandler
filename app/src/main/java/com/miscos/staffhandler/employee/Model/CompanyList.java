
package com.miscos.staffhandler.employee.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyList implements Parcelable
{

    @SerializedName("employer_id")
    @Expose
    private String employerId;
    @SerializedName("employee_id")
    @Expose
    private String employeeId;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("login_pin")
    @Expose
    private String loginPin;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("office_address")
    @Expose
    private String officeAddress;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    public final static Creator<CompanyList> CREATOR = new Creator<CompanyList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public CompanyList createFromParcel(Parcel in) {
            return new CompanyList(in);
        }

        public CompanyList[] newArray(int size) {
            return (new CompanyList[size]);
        }

    }
    ;

    protected CompanyList(Parcel in) {
        this.employerId = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeId = ((String) in.readValue((String.class.getClassLoader())));
        this.password = ((String) in.readValue((String.class.getClassLoader())));
        this.mobileNo = ((String) in.readValue((String.class.getClassLoader())));
        this.loginPin = ((String) in.readValue((String.class.getClassLoader())));
        this.companyName = ((String) in.readValue((String.class.getClassLoader())));
        this.officeAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.deviceId = ((String) in.readValue((String.class.getClassLoader())));
    }

    public CompanyList() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getLoginPin() {
        return loginPin;
    }

    public void setLoginPin(String loginPin) {
        this.loginPin = loginPin;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(employerId);
        dest.writeValue(employeeId);
        dest.writeValue(password);
        dest.writeValue(mobileNo);
        dest.writeValue(loginPin);
        dest.writeValue(companyName);
        dest.writeValue(officeAddress);
        dest.writeValue(deviceId);
    }

    public int describeContents() {
        return  0;
    }

}
