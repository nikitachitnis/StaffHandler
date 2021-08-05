
package com.miscos.staffhandler.employee.Model;

import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Department implements Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("employer_id")
    @Expose
    private String employerId;
    @SerializedName("department_id")
    @Expose
    private String departmentId;
    @SerializedName("designation_id")
    @Expose
    private String designationId;
    @SerializedName("department_name")
    @Expose
    private String departmentName;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("department_status")
    @Expose
    private String departmentStatus;
    @SerializedName("old_designation_id")
    @Expose
    private String oldDesignationId;
    @SerializedName("department_deactivated_on")
    @Expose
    private String departmentDeactivatedOn;
    @SerializedName("designation_created_on")
    @Expose
    private String designationCreatedOn;
    public final static Creator<Department> CREATOR = new Creator<Department>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Department createFromParcel(android.os.Parcel in) {
            return new Department(in);
        }

        public Department[] newArray(int size) {
            return (new Department[size]);
        }

    }
    ;

    protected Department(android.os.Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.employerId = ((String) in.readValue((String.class.getClassLoader())));
        this.departmentId = ((String) in.readValue((String.class.getClassLoader())));
        this.designationId = ((String) in.readValue((String.class.getClassLoader())));
        this.departmentName = ((String) in.readValue((String.class.getClassLoader())));
        this.createdOn = ((String) in.readValue((String.class.getClassLoader())));
        this.departmentStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.oldDesignationId = ((String) in.readValue((String.class.getClassLoader())));
        this.departmentDeactivatedOn = ((String) in.readValue((String.class.getClassLoader())));
        this.designationCreatedOn = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Department() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDesignationId() {
        return designationId;
    }

    public void setDesignationId(String designationId) {
        this.designationId = designationId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getDepartmentStatus() {
        return departmentStatus;
    }

    public void setDepartmentStatus(String departmentStatus) {
        this.departmentStatus = departmentStatus;
    }

    public String getOldDesignationId() {
        return oldDesignationId;
    }

    public void setOldDesignationId(String oldDesignationId) {
        this.oldDesignationId = oldDesignationId;
    }

    public String getDepartmentDeactivatedOn() {
        return departmentDeactivatedOn;
    }

    public void setDepartmentDeactivatedOn(String departmentDeactivatedOn) {
        this.departmentDeactivatedOn = departmentDeactivatedOn;
    }

    public String getDesignationCreatedOn() {
        return designationCreatedOn;
    }

    public void setDesignationCreatedOn(String designationCreatedOn) {
        this.designationCreatedOn = designationCreatedOn;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(employerId);
        dest.writeValue(departmentId);
        dest.writeValue(designationId);
        dest.writeValue(departmentName);
        dest.writeValue(createdOn);
        dest.writeValue(departmentStatus);
        dest.writeValue(oldDesignationId);
        dest.writeValue(departmentDeactivatedOn);
        dest.writeValue(designationCreatedOn);
    }

    public int describeContents() {
        return  0;
    }

}
