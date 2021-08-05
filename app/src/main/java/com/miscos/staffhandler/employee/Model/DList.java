
package com.miscos.staffhandler.employee.Model;

import java.util.ArrayList;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DList implements Parcelable
{

    @SerializedName("employer_id")
    @Expose
    private String employerId;
    @SerializedName("dept_id")
    @Expose
    private String deptId;
    @SerializedName("dept_name")
    @Expose
    private String deptName;
    @SerializedName("dept_created_on")
    @Expose
    private String deptCreatedOn;
    @SerializedName("designation_arr")
    @Expose
    private java.util.List<DesignationArr> designationArr = new ArrayList<DesignationArr>();
    public final static Creator<DList> CREATOR = new Creator<DList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DList createFromParcel(android.os.Parcel in) {
            return new DList(in);
        }

        public DList[] newArray(int size) {
            return (new DList[size]);
        }

    }
    ;

    protected DList(android.os.Parcel in) {
        this.employerId = ((String) in.readValue((String.class.getClassLoader())));
        this.deptId = ((String) in.readValue((String.class.getClassLoader())));
        this.deptName = ((String) in.readValue((String.class.getClassLoader())));
        this.deptCreatedOn = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.designationArr, (DesignationArr.class.getClassLoader()));
    }

    public DList() {
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptCreatedOn() {
        return deptCreatedOn;
    }

    public void setDeptCreatedOn(String deptCreatedOn) {
        this.deptCreatedOn = deptCreatedOn;
    }

    public java.util.List<DesignationArr> getDesignationArr() {
        return designationArr;
    }

    public void setDesignationArr(java.util.List<DesignationArr> designationArr) {
        this.designationArr = designationArr;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(employerId);
        dest.writeValue(deptId);
        dest.writeValue(deptName);
        dest.writeValue(deptCreatedOn);
        dest.writeList(designationArr);
    }

    public int describeContents() {
        return  0;
    }

}
