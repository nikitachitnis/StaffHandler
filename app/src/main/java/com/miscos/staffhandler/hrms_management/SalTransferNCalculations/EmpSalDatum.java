
package com.miscos.staffhandler.hrms_management.SalTransferNCalculations;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmpSalDatum implements Parcelable
{

    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("amount_list")
    @Expose
    private List<AmountList> amountList = null;
    public final static Creator<EmpSalDatum> CREATOR = new Creator<EmpSalDatum>() {


        @SuppressWarnings({
            "unchecked"
        })
        public EmpSalDatum createFromParcel(Parcel in) {
            return new EmpSalDatum(in);
        }

        public EmpSalDatum[] newArray(int size) {
            return (new EmpSalDatum[size]);
        }

    }
    ;

    protected EmpSalDatum(Parcel in) {
        this.errorCode = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.amountList, (AmountList.class.getClassLoader()));
    }

    public EmpSalDatum() {
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

    public List<AmountList> getAmountList() {
        return amountList;
    }

    public void setAmountList(List<AmountList> amountList) {
        this.amountList = amountList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(errorCode);
        dest.writeValue(message);
        dest.writeList(amountList);
    }

    public int describeContents() {
        return  0;
    }

}
