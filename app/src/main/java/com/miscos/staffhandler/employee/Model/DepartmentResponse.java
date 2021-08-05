
package com.miscos.staffhandler.employee.Model;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DepartmentResponse implements Parcelable
{

    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("department_list")
    @Expose
    private List<Department> departmentList = new ArrayList<Department>();
    public final static Creator<DepartmentResponse> CREATOR = new Creator<DepartmentResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DepartmentResponse createFromParcel(android.os.Parcel in) {
            return new DepartmentResponse(in);
        }

        public DepartmentResponse[] newArray(int size) {
            return (new DepartmentResponse[size]);
        }

    }
    ;

    protected DepartmentResponse(android.os.Parcel in) {
        this.errorCode = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.departmentList, (Department.class.getClassLoader()));
    }

    public DepartmentResponse() {
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(errorCode);
        dest.writeValue(message);
        dest.writeList(departmentList);
    }

    public int describeContents() {
        return  0;
    }

}
