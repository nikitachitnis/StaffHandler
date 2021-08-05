package com.miscos.staffhandler.employer_java.remote.model.fetchdataform2;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.annotations.SerializedName;
import com.miscos.staffhandler.R;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class HolidayData implements Parcelable {

    @SerializedName("message_for_employees")
    private String messageForEmployees;

    @SerializedName("holiday_id")
    private String holidayId;

    @SerializedName("from_date")
    private String fromDate;

    @SerializedName("to_date")
    private String toDate;

    @SerializedName("applied_on")
    private String appliedOn="";

    @SerializedName("holiday_image")
    private String holidayImage;

    @SerializedName("days")
    private String days;

    @SerializedName("id")
    private String id;

    @SerializedName("employer_id")
    private String employerId;

    @SerializedName("holiday_type")
    private String holidayType;

    @SerializedName("holiday_name")
    private String holidayName;

    private boolean isEditable=true;

    protected HolidayData(Parcel in) {
        messageForEmployees = in.readString();
        holidayId = in.readString();
        fromDate = in.readString();
        toDate = in.readString();
        appliedOn = in.readString();
        holidayImage = in.readString();
        days = in.readString();
        id = in.readString();
        employerId = in.readString();
        holidayType = in.readString();
        holidayName = in.readString();
        isEditable = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(messageForEmployees);
        dest.writeString(holidayId);
        dest.writeString(fromDate);
        dest.writeString(toDate);
        dest.writeString(appliedOn);
        dest.writeString(holidayImage);
        dest.writeString(days);
        dest.writeString(id);
        dest.writeString(employerId);
        dest.writeString(holidayType);
        dest.writeString(holidayName);
        dest.writeByte((byte) (isEditable ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HolidayData> CREATOR = new Creator<HolidayData>() {
        @Override
        public HolidayData createFromParcel(Parcel in) {
            return new HolidayData(in);
        }

        @Override
        public HolidayData[] newArray(int size) {
            return new HolidayData[size];
        }
    };

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public String getMessageForEmployees() {
        return messageForEmployees;
    }

    public String getHolidayId() {
        return holidayId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getDateString() {
        try {
            fromDate = new SimpleDateFormat("dd-MMM").format(new SimpleDateFormat("yyyy-MM-dd").parse(fromDate));
            toDate = new SimpleDateFormat("dd-MMM").format(new SimpleDateFormat("yyyy-MM-dd").parse(toDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (fromDate.equalsIgnoreCase(toDate)) {
            return fromDate;
        } else return fromDate + " - " + toDate;
    }

    public String getToDate() {
        return toDate;
    }

    public String getAppliedOn() {
        return appliedOn;
    }

    public String getShiftData()
    { if(appliedOn.contains("&"))
    {
        String[] data = appliedOn.split("&");
        StringBuilder shift = new StringBuilder();
        String prefix="";
        for (int i = 0; i < data.length; i++) {
            shift.append(prefix);
            prefix = ",";
            if (data[i].equalsIgnoreCase("B")) {
                shift.append(data[i]);
            }else if (data[i].equalsIgnoreCase("O")) {
                shift.append("Office Shift");
            }else if (data[i].equalsIgnoreCase("S1")) {
                shift.append("1st Shift");
            }else if (data[i].equalsIgnoreCase("S2")) {
                shift.append("2nd Shift");
            }else if (data[i].equalsIgnoreCase("S3")) {
                shift.append("3rd Shift");
            }
        }
        return  shift.toString();
    }
    else if(!appliedOn.isEmpty())
    {
        if (appliedOn.equalsIgnoreCase("B")) {
           return "ALL";
        }else if (appliedOn.equalsIgnoreCase("O")) {
            return  "Office Shift";
        }else if (appliedOn.equalsIgnoreCase("S1")) {
            return  "1st Shift";
        }else if (appliedOn.equalsIgnoreCase("S2")) {
            return  "2nd Shift";
        }else if (appliedOn.equalsIgnoreCase("S3")) {
            return  "3rd Shift";
        }
    }
        return "";


    }

    public String getHolidayImage() {
        return holidayImage;
    }

    public String getDays() {
        return days;
    }

    public String getId() {
        return id;
    }

    public String getEmployerId() {
        return employerId;
    }

    public String getHolidayType() {
        return holidayType;
    }

    public String getHolidayName() {
        return holidayName;
    }

  /*  @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HolidayData data = (HolidayData) o;
        return isEditable == data.isEditable &&
                Objects.equals(messageForEmployees, data.messageForEmployees) &&
                Objects.equals(holidayId, data.holidayId) &&
                Objects.equals(fromDate, data.fromDate) &&
                Objects.equals(toDate, data.toDate) &&
                Objects.equals(appliedOn, data.appliedOn) &&
                Objects.equals(holidayImage, data.holidayImage) &&
                Objects.equals(days, data.days) &&
                Objects.equals(id, data.id) &&
                Objects.equals(employerId, data.employerId) &&
                Objects.equals(holidayType, data.holidayType) &&
                Objects.equals(holidayName, data.holidayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageForEmployees, holidayId, fromDate, toDate, appliedOn, holidayImage, days, id, employerId, holidayType, holidayName, isEditable);
    }*/

    @BindingAdapter("holidayImage")
    public static void loadImage(ImageView view,String img) {
        Glide.with(view.getContext())
                .load(img).apply(new RequestOptions().fitCenter())
                .placeholder(R.drawable.imgnot)
                .into(view);
    }
}