package com.miscos.staffhandler.hrms_management.hrms.model;

public class LeaveRequestData {
    String fromDate;
    String toDate;
    String status;
    String type;

    public LeaveRequestData(String fromDate, String toDate, String status, String type) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.status = status;
        this.type = type;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
