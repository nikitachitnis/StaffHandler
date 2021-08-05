
package com.miscos.staffhandler.hrms_management.hrms.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveCount implements Parcelable
{

    @SerializedName("month_name")
    @Expose
    private String monthName;
    @SerializedName("approved_cnt")
    @Expose
    private Integer approvedCnt;
    @SerializedName("rejected_cnt")
    @Expose
    private Integer rejectedCnt;
    @SerializedName("pending_cnt")
    @Expose
    private Integer pendingCnt;
    public final static Creator<LeaveCount> CREATOR = new Creator<LeaveCount>() {


        @SuppressWarnings({
            "unchecked"
        })
        public LeaveCount createFromParcel(Parcel in) {
            return new LeaveCount(in);
        }

        public LeaveCount[] newArray(int size) {
            return (new LeaveCount[size]);
        }

    }
    ;

    protected LeaveCount(Parcel in) {
        this.monthName = ((String) in.readValue((String.class.getClassLoader())));
        this.approvedCnt = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.rejectedCnt = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.pendingCnt = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public LeaveCount() {
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public Integer getApprovedCnt() {
        return approvedCnt;
    }

    public void setApprovedCnt(Integer approvedCnt) {
        this.approvedCnt = approvedCnt;
    }

    public Integer getRejectedCnt() {
        return rejectedCnt;
    }

    public void setRejectedCnt(Integer rejectedCnt) {
        this.rejectedCnt = rejectedCnt;
    }

    public Integer getPendingCnt() {
        return pendingCnt;
    }

    public void setPendingCnt(Integer pendingCnt) {
        this.pendingCnt = pendingCnt;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(monthName);
        dest.writeValue(approvedCnt);
        dest.writeValue(rejectedCnt);
        dest.writeValue(pendingCnt);
    }

    public int describeContents() {
        return  0;
    }

}
