
package com.miscos.staffhandler.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeData implements Parcelable
{

    @SerializedName("s_no")
    @Expose
    private Integer sNo;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("day_in")
    @Expose
    private String dayIn;
    @SerializedName("day_out")
    @Expose
    private String dayOut;
    @SerializedName("duty_hour")
    @Expose
    private Integer dutyHour;
    public final static Creator<EmployeeData> CREATOR = new Creator<EmployeeData>() {


        @SuppressWarnings({
            "unchecked"
        })
        public EmployeeData createFromParcel(Parcel in) {
            return new EmployeeData(in);
        }

        public EmployeeData[] newArray(int size) {
            return (new EmployeeData[size]);
        }

    }
    ;

    protected EmployeeData(Parcel in) {
        this.sNo = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.date = ((String) in.readValue((String.class.getClassLoader())));
        this.dayIn = ((String) in.readValue((String.class.getClassLoader())));
        this.dayOut = ((String) in.readValue((String.class.getClassLoader())));
        this.dutyHour = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public EmployeeData() {
    }

    public Integer getSNo() {
        return sNo;
    }

    public void setSNo(Integer sNo) {
        this.sNo = sNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDayIn() {
        return dayIn;
    }

    public void setDayIn(String dayIn) {
        this.dayIn = dayIn;
    }

    public String getDayOut() {
        return dayOut;
    }

    public void setDayOut(String dayOut) {
        this.dayOut = dayOut;
    }

    public Integer getDutyHour() {
        return dutyHour;
    }

    public void setDutyHour(Integer dutyHour) {
        this.dutyHour = dutyHour;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(sNo);
        dest.writeValue(date);
        dest.writeValue(dayIn);
        dest.writeValue(dayOut);
        dest.writeValue(dutyHour);
    }

    public int describeContents() {
        return  0;
    }

}
