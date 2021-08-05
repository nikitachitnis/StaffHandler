package com.miscos.staffhandler.hrms_management.hrms.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeFeedbackResponse {
    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("employee_data")
    @Expose
    private EmployeeFeedBackData employeeData;

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

    public EmployeeFeedBackData getEmployeeData() {
        return employeeData;
    }

    public void setEmployeeData(EmployeeFeedBackData employeeData) {
        this.employeeData = employeeData;
    }


    public static class EmployeeFeedBackData {

        @SerializedName("designation_at_the_time_of_joining")
        @Expose
        private String designationAtTheTimeOfJoining;
        @SerializedName("designation_at_the_time_of_leaving")
        @Expose
        private String designationAtTheTimeOfLeaving;
        @SerializedName("date_of_joining")
        @Expose
        private String dateOfJoining;
        @SerializedName("date_of_leaving")
        @Expose
        private String dateOfLeaving;
        @SerializedName("gross_salary")
        @Expose
        private String grossSalary;
        @SerializedName("supervisor_name_designation")
        @Expose
        private String supervisorNameDesignation;
        @SerializedName("job_knowledge")
        @Expose
        private String jobKnowledge;
        @SerializedName("work_quality")
        @Expose
        private String workQuality;
        @SerializedName("punctuality")
        @Expose
        private String punctuality;
        @SerializedName("initiative")
        @Expose
        private String initiative;
        @SerializedName("technical_skill")
        @Expose
        private String technicalSkill;
        @SerializedName("dependability")
        @Expose
        private String dependability;
        @SerializedName("overall_rating")
        @Expose
        private String overallRating;
        @SerializedName("reason_for_leaving")
        @Expose
        private String reasonForLeaving;
        @SerializedName("is_the_exit_formalities_completed")
        @Expose
        private String isTheExitFormalitiesCompleted;
        @SerializedName("additional_comments")
        @Expose
        private String additionalComments;

        public String getDesignationAtTheTimeOfJoining() {
            return designationAtTheTimeOfJoining;
        }

        public void setDesignationAtTheTimeOfJoining(String designationAtTheTimeOfJoining) {
            this.designationAtTheTimeOfJoining = designationAtTheTimeOfJoining;
        }

        public String getDesignationAtTheTimeOfLeaving() {
            return designationAtTheTimeOfLeaving;
        }

        public void setDesignationAtTheTimeOfLeaving(String designationAtTheTimeOfLeaving) {
            this.designationAtTheTimeOfLeaving = designationAtTheTimeOfLeaving;
        }

        public String getDateOfJoining() {
            return dateOfJoining;
        }

        public void setDateOfJoining(String dateOfJoining) {
            this.dateOfJoining = dateOfJoining;
        }

        public String getDateOfLeaving() {
            return dateOfLeaving;
        }

        public void setDateOfLeaving(String dateOfLeaving) {
            this.dateOfLeaving = dateOfLeaving;
        }

        public String getGrossSalary() {
            return grossSalary;
        }

        public void setGrossSalary(String grossSalary) {
            this.grossSalary = grossSalary;
        }

        public String getSupervisorNameDesignation() {
            return supervisorNameDesignation;
        }

        public void setSupervisorNameDesignation(String supervisorNameDesignation) {
            this.supervisorNameDesignation = supervisorNameDesignation;
        }

        public String getJobKnowledge() {
            return jobKnowledge;
        }

        public void setJobKnowledge(String jobKnowledge) {
            this.jobKnowledge = jobKnowledge;
        }

        public String getWorkQuality() {
            return workQuality;
        }

        public void setWorkQuality(String workQuality) {
            this.workQuality = workQuality;
        }

        public String getPunctuality() {
            return punctuality;
        }

        public void setPunctuality(String punctuality) {
            this.punctuality = punctuality;
        }

        public String getInitiative() {
            return initiative;
        }

        public void setInitiative(String initiative) {
            this.initiative = initiative;
        }

        public String getTechnicalSkill() {
            return technicalSkill;
        }

        public void setTechnicalSkill(String technicalSkill) {
            this.technicalSkill = technicalSkill;
        }

        public String getDependability() {
            return dependability;
        }

        public void setDependability(String dependability) {
            this.dependability = dependability;
        }

        public String getOverallRating() {
            return overallRating;
        }

        public void setOverallRating(String overallRating) {
            this.overallRating = overallRating;
        }

        public String getReasonForLeaving() {
            return reasonForLeaving;
        }

        public void setReasonForLeaving(String reasonForLeaving) {
            this.reasonForLeaving = reasonForLeaving;
        }

        public String getIsTheExitFormalitiesCompleted() {
            return isTheExitFormalitiesCompleted;
        }

        public void setIsTheExitFormalitiesCompleted(String isTheExitFormalitiesCompleted) {
            this.isTheExitFormalitiesCompleted = isTheExitFormalitiesCompleted;
        }

        public String getAdditionalComments() {
            return additionalComments;
        }

        public void setAdditionalComments(String additionalComments) {
            this.additionalComments = additionalComments;
        }

    }
}
