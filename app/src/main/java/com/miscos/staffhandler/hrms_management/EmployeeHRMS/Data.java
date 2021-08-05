
package com.miscos.staffhandler.hrms_management.EmployeeHRMS;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable
{

    @SerializedName("salary_struct")
    @Expose
    private String salaryStruct;
    @SerializedName("leave_struct")
    @Expose
    private List<LeaveStruct> leaveStruct = null;
    @SerializedName("setting_deduction")
    @Expose
    private SettingDeduction settingDeduction;
    public final static Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    }
    ;

    protected Data(Parcel in) {
        this.salaryStruct = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.leaveStruct, (LeaveStruct.class.getClassLoader()));
        this.settingDeduction = ((SettingDeduction) in.readValue((SettingDeduction.class.getClassLoader())));
    }

    public Data() {
    }

    public String getSalaryStruct() {
        return salaryStruct;
    }

    public void setSalaryStruct(String salaryStruct) {
        this.salaryStruct = salaryStruct;
    }

    public List<LeaveStruct> getLeaveStruct() {
        return leaveStruct;
    }

    public void setLeaveStruct(List<LeaveStruct> leaveStruct) {
        this.leaveStruct = leaveStruct;
    }

    public SettingDeduction getSettingDeduction() {
        return settingDeduction;
    }

    public void setSettingDeduction(SettingDeduction settingDeduction) {
        this.settingDeduction = settingDeduction;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(salaryStruct);
        dest.writeList(leaveStruct);
        dest.writeValue(settingDeduction);
    }

    public int describeContents() {
        return  0;
    }

}
