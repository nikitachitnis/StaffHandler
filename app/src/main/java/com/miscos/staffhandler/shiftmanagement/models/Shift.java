package com.miscos.staffhandler.shiftmanagement.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shift {

    @SerializedName("shift_count")
    @Expose
    private String shiftCount;
    @SerializedName("sec_1_in_timing")
    @Expose
    private String sec1InTiming;
    @SerializedName("sec_1_out_timing")
    @Expose
    private String sec1OutTiming;
    @SerializedName("sec_2_in_timing")
    @Expose
    private String sec2InTiming;
    @SerializedName("sec_2_out_timing")
    @Expose
    private String sec2OutTiming;
    @SerializedName("sec_3_in_timing")
    @Expose
    private String sec3InTiming;
    @SerializedName("sec_3_out_timing")
    @Expose
    private String sec3OutTiming;
    @SerializedName("sec_1_pre_start_timing")
    @Expose
    private String sec1PreStartTiming;
    @SerializedName("sec_1_post_close_timing")
    @Expose
    private String sec1PostCloseTiming;
    @SerializedName("sec_2_pre_start_timing")
    @Expose
    private String sec2PreStartTiming;
    @SerializedName("sec_2_post_close_timing")
    @Expose
    private String sec2PostCloseTiming;
    @SerializedName("sec_3_pre_start_timing")
    @Expose
    private String sec3PreStartTiming;
    @SerializedName("sec_3_post_close_timing")
    @Expose
    private String sec3PostCloseTiming;
    @SerializedName("shift_1_name")
    @Expose
    private String shift1name;
    @SerializedName("shift_2_name")
    @Expose
    private String shift2name;
    @SerializedName("shift_3_name")
    @Expose
    private String shift3name;
    @SerializedName("shift_2_break_duration_in_min")
    @Expose
    private String shift_2_break_duration_in_min;
    @SerializedName("shift_3_break_to_time")
    @Expose
    private String shift_3_break_to_time;
    @SerializedName("shift_3_break_from_time")
    @Expose
    private String shift_3_break_from_time;
    @SerializedName("shift_2_break_to_time")
    @Expose
    private String shift_2_break_to_time;
    @SerializedName("shift_2_break_from_time")
    @Expose
    private String shift_2_break_from_time;
    @SerializedName("shift_1_break_to_time")
    @Expose
    private String shift_1_break_to_time;
    @SerializedName("shift_1_break_from_time")
    @Expose
    private String shift_1_break_from_time;
    @SerializedName("generate_voice_alarm")
    @Expose
    private String generate_voice_alarm;
    @SerializedName("shift_send_notification_alert_for_break")
    @Expose
    private String shift_send_notification_alert_for_break;
    @SerializedName("shift_3_break_duration_in_min")
    @Expose
    private String shift_3_break_duration_in_min;
    @SerializedName("shift_1_break_duration_in_min")
    @Expose
    private String shift_1_break_duration_in_min;

    public String getShift_2_break_duration_in_min() {
        return shift_2_break_duration_in_min;
    }

    public void setShift_2_break_duration_in_min(String shift_2_break_duration_in_min) {
        this.shift_2_break_duration_in_min = shift_2_break_duration_in_min;
    }

    public String getShift_3_break_to_time() {
        return shift_3_break_to_time;
    }

    public void setShift_3_break_to_time(String shift_3_break_to_time) {
        this.shift_3_break_to_time = shift_3_break_to_time;
    }

    public String getShift_3_break_from_time() {
        return shift_3_break_from_time;
    }

    public void setShift_3_break_from_time(String shift_3_break_from_time) {
        this.shift_3_break_from_time = shift_3_break_from_time;
    }

    public String getShift_2_break_to_time() {
        return shift_2_break_to_time;
    }

    public void setShift_2_break_to_time(String shift_2_break_to_time) {
        this.shift_2_break_to_time = shift_2_break_to_time;
    }

    public String getShift_2_break_from_time() {
        return shift_2_break_from_time;
    }

    public void setShift_2_break_from_time(String shift_2_break_from_time) {
        this.shift_2_break_from_time = shift_2_break_from_time;
    }

    public String getShift_1_break_to_time() {
        return shift_1_break_to_time;
    }

    public void setShift_1_break_to_time(String shift_1_break_to_time) {
        this.shift_1_break_to_time = shift_1_break_to_time;
    }

    public String getShift_1_break_from_time() {
        return shift_1_break_from_time;
    }

    public void setShift_1_break_from_time(String shift_1_break_from_time) {
        this.shift_1_break_from_time = shift_1_break_from_time;
    }

    public String getGenerate_voice_alarm() {
        return generate_voice_alarm;
    }

    public void setGenerate_voice_alarm(String generate_voice_alarm) {
        this.generate_voice_alarm = generate_voice_alarm;
    }

    public String getShift_send_notification_alert_for_break() {
        return shift_send_notification_alert_for_break;
    }

    public void setShift_send_notification_alert_for_break(String shift_send_notification_alert_for_break) {
        this.shift_send_notification_alert_for_break = shift_send_notification_alert_for_break;
    }

    public String getShift_3_break_duration_in_min() {
        return shift_3_break_duration_in_min;
    }

    public void setShift_3_break_duration_in_min(String shift_3_break_duration_in_min) {
        this.shift_3_break_duration_in_min = shift_3_break_duration_in_min;
    }

    public String getShift_1_break_duration_in_min() {
        return shift_1_break_duration_in_min;
    }

    public void setShift_1_break_duration_in_min(String shift_1_break_duration_in_min) {
        this.shift_1_break_duration_in_min = shift_1_break_duration_in_min;
    }

    public String getShift1name() {
        return shift1name;
    }

    public void setShift1name(String shift1name) {
        this.shift1name = shift1name;
    }

    public String getShift2name() {
        return shift2name;
    }

    public void setShift2name(String shift2name) {
        this.shift2name = shift2name;
    }

    public String getShift3name() {
        return shift3name;
    }

    public void setShift3name(String shift3name) {
        this.shift3name = shift3name;
    }

    public String getShiftCount() {
        return shiftCount;
    }

    public void setShiftCount(String shiftCount) {
        this.shiftCount = shiftCount;
    }

    public String getSec1InTiming() {
        return sec1InTiming;
    }

    public void setSec1InTiming(String sec1InTiming) {
        this.sec1InTiming = sec1InTiming;
    }

    public String getSec1OutTiming() {
        return sec1OutTiming;
    }

    public void setSec1OutTiming(String sec1OutTiming) {
        this.sec1OutTiming = sec1OutTiming;
    }

    public String getSec2InTiming() {
        return sec2InTiming;
    }

    public void setSec2InTiming(String sec2InTiming) {
        this.sec2InTiming = sec2InTiming;
    }

    public String getSec2OutTiming() {
        return sec2OutTiming;
    }

    public void setSec2OutTiming(String sec2OutTiming) {
        this.sec2OutTiming = sec2OutTiming;
    }

    public String getSec3InTiming() {
        return sec3InTiming;
    }

    public void setSec3InTiming(String sec3InTiming) {
        this.sec3InTiming = sec3InTiming;
    }

    public String getSec3OutTiming() {
        return sec3OutTiming;
    }

    public void setSec3OutTiming(String sec3OutTiming) {
        this.sec3OutTiming = sec3OutTiming;
    }

    public String getSec1PreStartTiming() {
        return sec1PreStartTiming;
    }

    public void setSec1PreStartTiming(String sec1PreStartTiming) {
        this.sec1PreStartTiming = sec1PreStartTiming;
    }

    public String getSec1PostCloseTiming() {
        return sec1PostCloseTiming;
    }

    public void setSec1PostCloseTiming(String sec1PostCloseTiming) {
        this.sec1PostCloseTiming = sec1PostCloseTiming;
    }

    public String getSec2PreStartTiming() {
        return sec2PreStartTiming;
    }

    public void setSec2PreStartTiming(String sec2PreStartTiming) {
        this.sec2PreStartTiming = sec2PreStartTiming;
    }

    public String getSec2PostCloseTiming() {
        return sec2PostCloseTiming;
    }

    public void setSec2PostCloseTiming(String sec2PostCloseTiming) {
        this.sec2PostCloseTiming = sec2PostCloseTiming;
    }

    public String getSec3PreStartTiming() {
        return sec3PreStartTiming;
    }

    public void setSec3PreStartTiming(String sec3PreStartTiming) {
        this.sec3PreStartTiming = sec3PreStartTiming;
    }

    public String getSec3PostCloseTiming() {
        return sec3PostCloseTiming;
    }

    public void setSec3PostCloseTiming(String sec3PostCloseTiming) {
        this.sec3PostCloseTiming = sec3PostCloseTiming;
    }

}
