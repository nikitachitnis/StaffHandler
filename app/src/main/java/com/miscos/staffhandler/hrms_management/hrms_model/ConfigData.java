
package com.miscos.staffhandler.hrms_management.hrms_model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfigData implements Parcelable
{

    @SerializedName("salary_struct")
    @Expose
    private List<salary_structure> salaryStruct = null;
    @SerializedName("leave_struct")
    @Expose
    private List<LeaveStruct> leaveStruct = null;
    @SerializedName("setting_deduction")
    @Expose
    private SettingDeduction settingDeduction;
    public final static Parcelable.Creator<ConfigData> CREATOR = new Creator<ConfigData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ConfigData createFromParcel(Parcel in) {
            return new ConfigData(in);
        }

        public ConfigData[] newArray(int size) {
            return (new ConfigData[size]);
        }

    }
            ;

    protected ConfigData(Parcel in) {
        in.readList(this.salaryStruct, (salary_structure.class.getClassLoader()));
        in.readList(this.leaveStruct, (LeaveStruct.class.getClassLoader()));
        this.settingDeduction = ((SettingDeduction) in.readValue((SettingDeduction.class.getClassLoader())));
    }

    public ConfigData() {
    }

    public List<salary_structure> getSalaryStruct() {
        return salaryStruct;
    }

    public void setSalaryStruct(List<salary_structure> salaryStruct) {
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
        dest.writeList(salaryStruct);
        dest.writeList(leaveStruct);
        dest.writeValue(settingDeduction);
    }

    public int describeContents() {
        return  0;
    }

}