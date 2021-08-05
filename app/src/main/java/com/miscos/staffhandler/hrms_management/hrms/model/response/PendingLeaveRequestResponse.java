package com.miscos.staffhandler.hrms_management.hrms.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PendingLeaveRequestResponse {

    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("pending_leave_data")
    @Expose
    private ArrayList<PendingLeaveData> pendingLeaveData = null;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<PendingLeaveData> getPendingLeaveData() {
        return pendingLeaveData;
    }

    public void setPendingLeaveData(ArrayList<PendingLeaveData> pendingLeaveData) {
        this.pendingLeaveData = pendingLeaveData;
    }


    public static class PendingLeaveData {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("employer_id")
        @Expose
        private String employerId;
        @SerializedName("employee_id")
        @Expose
        private String employeeId;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("leave_from_date")
        @Expose
        private String leaveFromDate;
        @SerializedName("leave_to_date")
        @Expose
        private String leaveToDate;
        @SerializedName("reason_for_leave")
        @Expose
        private String reasonForLeave;
        @SerializedName("leave_description")
        @Expose
        private String leaveDescription;
        @SerializedName("leave_status")
        @Expose
        private String leaveStatus;
        @SerializedName("created_date")
        @Expose
        private String createdDate;
        @SerializedName("action_taken_by")
        @Expose
        private String actionTakenBy;
        @SerializedName("action_taken_date")
        @Expose
        private String actionTakenDate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmployerId() {
            return employerId;
        }

        public void setEmployerId(String employerId) {
            this.employerId = employerId;
        }

        public String getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLeaveFromDate() {
            return leaveFromDate;
        }

        public void setLeaveFromDate(String leaveFromDate) {
            this.leaveFromDate = leaveFromDate;
        }

        public String getLeaveToDate() {
            return leaveToDate;
        }

        public void setLeaveToDate(String leaveToDate) {
            this.leaveToDate = leaveToDate;
        }

        public String getReasonForLeave() {
            return reasonForLeave;
        }

        public void setReasonForLeave(String reasonForLeave) {
            this.reasonForLeave = reasonForLeave;
        }

        public String getLeaveDescription() {
            return leaveDescription;
        }

        public void setLeaveDescription(String leaveDescription) {
            this.leaveDescription = leaveDescription;
        }

        public String getLeaveStatus() {
            return leaveStatus;
        }

        public void setLeaveStatus(String leaveStatus) {
            this.leaveStatus = leaveStatus;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getActionTakenBy() {
            return actionTakenBy;
        }

        public void setActionTakenBy(String actionTakenBy) {
            this.actionTakenBy = actionTakenBy;
        }

        public String getActionTakenDate() {
            return actionTakenDate;
        }

        public void setActionTakenDate(String actionTakenDate) {
            this.actionTakenDate = actionTakenDate;
        }

    }
}
