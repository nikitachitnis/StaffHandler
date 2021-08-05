
package com.miscos.staffhandler.employee.Model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MutiOrganizationResponse implements Parcelable
{

    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("company_list")
    @Expose
    private List<CompanyList> companyList = null;
    public final static Creator<MutiOrganizationResponse> CREATOR = new Creator<MutiOrganizationResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public MutiOrganizationResponse createFromParcel(Parcel in) {
            return new MutiOrganizationResponse(in);
        }

        public MutiOrganizationResponse[] newArray(int size) {
            return (new MutiOrganizationResponse[size]);
        }

    }
    ;

    protected MutiOrganizationResponse(Parcel in) {
        this.errorCode = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.companyList, (CompanyList.class.getClassLoader()));
    }

    public MutiOrganizationResponse() {
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

    public List<CompanyList> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<CompanyList> companyList) {
        this.companyList = companyList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(errorCode);
        dest.writeValue(message);
        dest.writeList(companyList);
    }

    public int describeContents() {
        return  0;
    }

}
