
package com.miscos.staffhandler.employee.Model;

import java.util.ArrayList;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DesignationResponse implements Parcelable
{

    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("list")
    @Expose
    private java.util.List<DList> list = new ArrayList<DList>();
    public final static Creator<DesignationResponse> CREATOR = new Creator<DesignationResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DesignationResponse createFromParcel(android.os.Parcel in) {
            return new DesignationResponse(in);
        }

        public DesignationResponse[] newArray(int size) {
            return (new DesignationResponse[size]);
        }

    }
    ;

    protected DesignationResponse(android.os.Parcel in) {
        this.errorCode = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.list, (DList.class.getClassLoader()));
    }

    public DesignationResponse() {
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

    public java.util.List<DList> getList() {
        return list;
    }

    public void setList(java.util.List<DList> list) {
        this.list = list;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(errorCode);
        dest.writeValue(message);
        dest.writeList(list);
    }

    public int describeContents() {
        return  0;
    }

}
