
package com.miscos.staffhandler.hrms_management.hrms_model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PolicyResponse implements Parcelable
{

    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("config_data")
    @Expose
    private ConfigData configData;
    public final static Creator<PolicyResponse> CREATOR = new Creator<PolicyResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PolicyResponse createFromParcel(Parcel in) {
            return new PolicyResponse(in);
        }

        public PolicyResponse[] newArray(int size) {
            return (new PolicyResponse[size]);
        }

    }
    ;

    protected PolicyResponse(Parcel in) {
        this.errorCode = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.configData = ((ConfigData) in.readValue((ConfigData.class.getClassLoader())));
    }

    public PolicyResponse() {
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

    public ConfigData getConfigData() {
        return configData;
    }

    public void setConfigData(ConfigData configData) {
        this.configData = configData;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(errorCode);
        dest.writeValue(message);
        dest.writeValue(configData);
    }

    public int describeContents() {
        return  0;
    }

}
