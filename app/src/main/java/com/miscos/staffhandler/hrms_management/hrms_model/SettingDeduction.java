
package com.miscos.staffhandler.hrms_management.hrms_model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingDeduction implements Parcelable
{

    @SerializedName("provident_found")
    @Expose
    private String providentFound;
    @SerializedName("professional_tax")
    @Expose
    private String professionalTax;
    @SerializedName("ESIC")
    @Expose
    private String eSIC;
    @SerializedName("GST")
    @Expose
    private String gST;
    @SerializedName("TDS")
    @Expose
    private String tDS;
    public final static Creator<SettingDeduction> CREATOR = new Creator<SettingDeduction>() {


        @SuppressWarnings({
            "unchecked"
        })
        public SettingDeduction createFromParcel(Parcel in) {
            return new SettingDeduction(in);
        }

        public SettingDeduction[] newArray(int size) {
            return (new SettingDeduction[size]);
        }

    }
    ;

    protected SettingDeduction(Parcel in) {
        this.providentFound = ((String) in.readValue((String.class.getClassLoader())));
        this.professionalTax = ((String) in.readValue((String.class.getClassLoader())));
        this.eSIC = ((String) in.readValue((String.class.getClassLoader())));
        this.gST = ((String) in.readValue((String.class.getClassLoader())));
        this.tDS = ((String) in.readValue((String.class.getClassLoader())));
    }

    public SettingDeduction() {
    }

    public String getProvidentFound() {
        return providentFound;
    }

    public void setProvidentFound(String providentFound) {
        this.providentFound = providentFound;
    }

    public String getProfessionalTax() {
        return professionalTax;
    }

    public void setProfessionalTax(String professionalTax) {
        this.professionalTax = professionalTax;
    }

    public String getESIC() {
        return eSIC;
    }

    public void setESIC(String eSIC) {
        this.eSIC = eSIC;
    }

    public String getGST() {
        return gST;
    }

    public void setGST(String gST) {
        this.gST = gST;
    }

    public String getTDS() {
        return tDS;
    }

    public void setTDS(String tDS) {
        this.tDS = tDS;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(providentFound);
        dest.writeValue(professionalTax);
        dest.writeValue(eSIC);
        dest.writeValue(gST);
        dest.writeValue(tDS);
    }

    public int describeContents() {
        return  0;
    }

}
