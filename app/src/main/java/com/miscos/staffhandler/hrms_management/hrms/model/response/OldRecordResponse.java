
package com.miscos.staffhandler.hrms_management.hrms.model.response;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OldRecordResponse implements Parcelable
{

    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("leave_count")
    @Expose
    private List<LeaveCount> leaveCount = null;
    public final static Creator<OldRecordResponse> CREATOR = new Creator<OldRecordResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public OldRecordResponse createFromParcel(Parcel in) {
            return new OldRecordResponse(in);
        }

        public OldRecordResponse[] newArray(int size) {
            return (new OldRecordResponse[size]);
        }

    }
    ;

    protected OldRecordResponse(Parcel in) {
        this.errorCode = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.leaveCount, (LeaveCount.class.getClassLoader()));
    }

    public OldRecordResponse() {
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

    public List<LeaveCount> getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(List<LeaveCount> leaveCount) {
        this.leaveCount = leaveCount;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(errorCode);
        dest.writeValue(message);
        dest.writeList(leaveCount);
    }

    public int describeContents() {
        return  0;
    }

}
