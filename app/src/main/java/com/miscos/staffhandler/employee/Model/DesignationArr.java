
package com.miscos.staffhandler.employee.Model;


import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DesignationArr implements Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    public final static Creator<DesignationArr> CREATOR = new Creator<DesignationArr>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DesignationArr createFromParcel(android.os.Parcel in) {
            return new DesignationArr(in);
        }

        public DesignationArr[] newArray(int size) {
            return (new DesignationArr[size]);
        }

    }
    ;

    protected DesignationArr(android.os.Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
    }

    public DesignationArr() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
    }

    public int describeContents() {
        return  0;
    }

}
