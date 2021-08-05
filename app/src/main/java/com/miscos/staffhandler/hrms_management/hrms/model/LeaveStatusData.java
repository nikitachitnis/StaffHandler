package com.miscos.staffhandler.hrms_management.hrms.model;

public class LeaveStatusData {
    String leaveName;
    String leaveCount;
    String leaveType;

    public LeaveStatusData(String leaveName, String leaveCount, String leaveType) {
        this.leaveName = leaveName;
        this.leaveCount = leaveCount;
        this.leaveType = leaveType;
    }

    public String getLeaveName() {
        return leaveName;
    }

    public void setLeaveName(String leaveName) {
        this.leaveName = leaveName;
    }

    public String getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(String leaveCount) {
        this.leaveCount = leaveCount;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }
}
