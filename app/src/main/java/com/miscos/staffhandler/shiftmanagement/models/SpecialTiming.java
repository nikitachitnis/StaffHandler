package com.miscos.staffhandler.shiftmanagement.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpecialTiming {

    @SerializedName("in_timing")
    @Expose
    private String inTiming;
    @SerializedName("out_timing")
    @Expose
    private String outTiming;
    @SerializedName("pre_start_timing")
    @Expose
    private String preStartTiming;
    @SerializedName("post_close_timing")
    @Expose
    private String postCloseTiming;
    @SerializedName("week_off")
    @Expose
    private String weekOff;
    @SerializedName("special_duty_applicable_from_date")
    @Expose
    private String applicable_from;


    @SerializedName("special_holiday_2")
    @Expose
    private String special_holiday_2;
    @SerializedName("special_in_time_2")
    @Expose
    private String special_in_time_2;
    @SerializedName("special_out_time_2")
    @Expose
    private String special_out_time_2;
    @SerializedName("special_pre_in_time_2")
    @Expose
    private String special_pre_in_time_2;
    @SerializedName("special_post_close_time_2")
    @Expose
    private String special_post_close_time_2;

    public String getSpecial_holiday_2() {
        return special_holiday_2;
    }

    public void setSpecial_holiday_2(String special_holiday_2) {
        this.special_holiday_2 = special_holiday_2;
    }

    public String getSpecial_in_time_2() {
        return special_in_time_2;
    }

    public void setSpecial_in_time_2(String special_in_time_2) {
        this.special_in_time_2 = special_in_time_2;
    }

    public String getSpecial_out_time_2() {
        return special_out_time_2;
    }

    public void setSpecial_out_time_2(String special_out_time_2) {
        this.special_out_time_2 = special_out_time_2;
    }

    public String getSpecial_pre_in_time_2() {
        return special_pre_in_time_2;
    }

    public void setSpecial_pre_in_time_2(String special_pre_in_time_2) {
        this.special_pre_in_time_2 = special_pre_in_time_2;
    }

    public String getSpecial_post_close_time_2() {
        return special_post_close_time_2;
    }

    public void setSpecial_post_close_time_2(String special_post_close_time_2) {
        this.special_post_close_time_2 = special_post_close_time_2;
    }

    public String getInTiming() {
        return inTiming;
    }

    public void setInTiming(String inTiming) {
        this.inTiming = inTiming;
    }

    public String getOutTiming() {
        return outTiming;
    }

    public void setOutTiming(String outTiming) {
        this.outTiming = outTiming;
    }

    public String getPreStartTiming() {
        return preStartTiming;
    }

    public void setPreStartTiming(String preStartTiming) {
        this.preStartTiming = preStartTiming;
    }

    public String getPostCloseTiming() {
        return postCloseTiming;
    }

    public void setPostCloseTiming(String postCloseTiming) {
        this.postCloseTiming = postCloseTiming;
    }

    public String getWeekOff() {
        return weekOff;
    }

    public void setWeekOff(String weekOff) {
        this.weekOff = weekOff;
    }

    public String getApplicable_from() {
        return applicable_from;
    }

    public void setApplicable_from(String applicable_from) {
        this.applicable_from = applicable_from;
    }
}