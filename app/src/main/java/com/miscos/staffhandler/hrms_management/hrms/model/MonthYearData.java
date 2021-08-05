package com.miscos.staffhandler.hrms_management.hrms.model;

public class MonthYearData {
    String monthName;
    String year;
    boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public MonthYearData(String monthName, String year, boolean isSelect) {
        this.monthName = monthName;
        this.year = year;
        this.isSelect = isSelect;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
