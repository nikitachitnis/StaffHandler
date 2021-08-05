package com.miscos.staffhandler.hrms_management.hrms.model;

public class DayInfoData {
    String dayName;
    String dayCount;

    public DayInfoData(String dayName, String dayCount) {
        this.dayName = dayName;
        this.dayCount = dayCount;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getDayCount() {
        return dayCount;
    }

    public void setDayCount(String dayCount) {
        this.dayCount = dayCount;
    }
}
