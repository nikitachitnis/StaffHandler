
package com.miscos.staffhandler.hrms_management.EmployeeHRMS;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingDeduction implements Parcelable
{

    @SerializedName("provident_found")
    @Expose
    private ProvidentFound providentFound;
    @SerializedName("professional_tax")
    @Expose
    private ProfessionalTax professionalTax;
    @SerializedName("TDS")
    @Expose
    private TDS tDS;
    @SerializedName("GST")
    @Expose
    private GST gst;
    @SerializedName("ESIC")
    @Expose
    private ESIC esic;

    public ESIC getEsic() {
        return esic;
    }

    public void setEsic(ESIC esic) {
        this.esic = esic;
    }

    public GST getGst() {
        return gst;
    }

    public void setGst(GST gst) {
        this.gst = gst;
    }

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
        this.providentFound = ((ProvidentFound) in.readValue((ProvidentFound.class.getClassLoader())));
        this.professionalTax = ((ProfessionalTax) in.readValue((ProfessionalTax.class.getClassLoader())));
        this.tDS = ((TDS) in.readValue((TDS.class.getClassLoader())));
        this.gst = ((GST) in.readValue((GST.class.getClassLoader())));
        this.esic = ((ESIC) in.readValue((ESIC.class.getClassLoader())));
    }

    public SettingDeduction() {
    }

    public ProvidentFound getProvidentFound() {
        return providentFound;
    }

    public void setProvidentFound(ProvidentFound providentFound) {
        this.providentFound = providentFound;
    }

    public ProfessionalTax getProfessionalTax() {
        return professionalTax;
    }

    public void setProfessionalTax(ProfessionalTax professionalTax) {
        this.professionalTax = professionalTax;
    }

    public TDS getTDS() {
        return tDS;
    }

    public void setTDS(TDS tDS) {
        this.tDS = tDS;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(providentFound);
        dest.writeValue(professionalTax);
        dest.writeValue(tDS);
        dest.writeValue(gst);
        dest.writeValue(esic);
    }

    public int describeContents() {
        return  0;
    }

}
