
package com.miscos.staffhandler.employee.Model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OldEmpResponse implements Parcelable
{

    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("employee_list")
    @Expose
    private List<EmployeeList> employeeList = null;
    public final static Creator<OldEmpResponse> CREATOR = new Creator<OldEmpResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public OldEmpResponse createFromParcel(Parcel in) {
            return new OldEmpResponse(in);
        }

        public OldEmpResponse[] newArray(int size) {
            return (new OldEmpResponse[size]);
        }

    }
    ;

    protected OldEmpResponse(Parcel in) {
        this.errorCode = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.employeeList, (EmployeeList.class.getClassLoader()));
    }

    public OldEmpResponse() {
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

    public List<EmployeeList> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<EmployeeList> employeeList) {
        this.employeeList = employeeList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(errorCode);
        dest.writeValue(message);
        dest.writeList(employeeList);
    }

    public int describeContents() {
        return  0;
    }

}
