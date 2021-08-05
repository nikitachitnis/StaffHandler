package com.miscos.staffhandler.hrms_management.hrms.model;

public class LeaveInfoData {
    String leaveName;
    String leaveCount;
    String leaveRemainCount;

    public LeaveInfoData(String leaveName, String leaveCount, String leaveRemainCount) {
        this.leaveName = leaveName;
        this.leaveCount = leaveCount;
        this.leaveRemainCount = leaveRemainCount;
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

    public String getLeaveRemainCount() {
        return leaveRemainCount;
    }

    public void setLeaveRemainCount(String leaveRemainCount) {
        this.leaveRemainCount = leaveRemainCount;
    }
}
