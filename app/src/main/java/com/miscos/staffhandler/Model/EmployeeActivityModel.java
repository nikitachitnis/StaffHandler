
package com.miscos.staffhandler.Model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeActivityModel implements Parcelable
{

    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<EmployeeData> data = null;
    public final static Creator<EmployeeActivityModel> CREATOR = new Creator<EmployeeActivityModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public EmployeeActivityModel createFromParcel(Parcel in) {
            return new EmployeeActivityModel(in);
        }

        public EmployeeActivityModel[] newArray(int size) {
            return (new EmployeeActivityModel[size]);
        }

    }
    ;

    protected EmployeeActivityModel(Parcel in) {
        this.errorCode = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.data, (com.miscos.staffhandler.Model.EmployeeData.class.getClassLoader()));
    }

    public EmployeeActivityModel() {
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

    public List<EmployeeData> getData() {
        return data;
    }

    public void setData(List<EmployeeData> data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(errorCode);
        dest.writeValue(message);
        dest.writeList(data);
    }

    public int describeContents() {
        return  0;
    }

}
