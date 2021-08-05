
package com.miscos.staffhandler.shiftmanagement.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewArrangeList implements Parcelable
{

    @SerializedName("applied_date")
    @Expose
    private String appliedDate;
    @SerializedName("shift_no")
    @Expose
    private String shiftNo;
    @SerializedName("list")
    @Expose
    private String list;
    public final static Creator<NewArrangeList> CREATOR = new Creator<NewArrangeList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public NewArrangeList createFromParcel(Parcel in) {
            return new NewArrangeList(in);
        }

        public NewArrangeList[] newArray(int size) {
            return (new NewArrangeList[size]);
        }

    }
    ;

    protected NewArrangeList(Parcel in) {
        this.appliedDate = ((String) in.readValue((String.class.getClassLoader())));
        this.shiftNo = ((String) in.readValue((String.class.getClassLoader())));
        this.list = ((String) in.readValue((String.class.getClassLoader())));
    }

    public NewArrangeList() {
    }

    public String getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(String appliedDate) {
        this.appliedDate = appliedDate;
    }

    public String getShiftNo() {
        return shiftNo;
    }

    public void setShiftNo(String shiftNo) {
        this.shiftNo = shiftNo;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(appliedDate);
        dest.writeValue(shiftNo);
        dest.writeValue(list);
    }

    public int describeContents() {
        return  0;
    }

}
