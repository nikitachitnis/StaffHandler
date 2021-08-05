package com.miscos.staffhandler.hrms_management.hrms.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SalaryAndLeaveData {

    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("report_list")
    @Expose
    private ReportList reportList;

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

    public ReportList getReportList() {
        return reportList;
    }

    public void setReportList(ReportList reportList) {
        this.reportList = reportList;
    }

    public static class ReportList {

        @SerializedName("salary_detail")
        @Expose
        private List<SalaryDetail> salaryDetail = null;
        @SerializedName("leave_detail")
        @Expose
        private ArrayList<LeaveDetail> leaveDetail = null;
        @SerializedName("total_amount")
        @Expose
        private Integer totalAmount;
        @SerializedName("due_amount")
        @Expose
        private Integer dueAmount;

        public List<SalaryDetail> getSalaryDetail() {
            return salaryDetail;
        }

        public void setSalaryDetail(List<SalaryDetail> salaryDetail) {
            this.salaryDetail = salaryDetail;
        }

        public ArrayList<LeaveDetail> getLeaveDetail() {
            return leaveDetail;
        }

        public void setLeaveDetail(ArrayList<LeaveDetail> leaveDetail) {
            this.leaveDetail = leaveDetail;
        }

        public Integer getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(Integer totalAmount) {
            this.totalAmount = totalAmount;
        }

        public Integer getDueAmount() {
            return dueAmount;
        }

        public void setDueAmount(Integer dueAmount) {
            this.dueAmount = dueAmount;
        }

    }


    public static class LeaveDetail {

        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("type")
        @Expose
        private String type;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
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

    public static class SalaryDetail {

        @SerializedName("employer_id")
        @Expose
        private String employerId;
        @SerializedName("employee_id")
        @Expose
        private String employeeId;
        @SerializedName("employee_name")
        @Expose
        private String employeeName;
        @SerializedName("actual_salary")
        @Expose
        private Integer actualSalary;
        @SerializedName("net_salary")
        @Expose
        private Integer netSalary;
        @SerializedName("total_alraedy_paid_amount")
        @Expose
        private String totalAlraedyPaidAmount;
        @SerializedName("remaining_salary")
        @Expose
        private Integer remainingSalary;
        @SerializedName("leave_deduction")
        @Expose
        private Integer leaveDeduction;
        @SerializedName("taxs")
        @Expose
        private Integer taxs;
        @SerializedName("other_deduction_amt")
        @Expose
        private String otherDeductionAmt;
        @SerializedName("is_settalment")
        @Expose
        private String isSettalment;
        @SerializedName("total")
        @Expose
        private Integer total;
        @SerializedName("additional_payment")
        @Expose
        private Integer additional_payment;

        public Integer getAdditional_payment() {
            return additional_payment;
        }

        public void setAdditional_payment(Integer additional_payment) {
            this.additional_payment = additional_payment;
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

        public String getEmployeeName() {
            return employeeName;
        }

        public void setEmployeeName(String employeeName) {
            this.employeeName = employeeName;
        }

        public Integer getActualSalary() {
            return actualSalary;
        }

        public void setActualSalary(Integer actualSalary) {
            this.actualSalary = actualSalary;
        }

        public Integer getNetSalary() {
            return netSalary;
        }

        public void setNetSalary(Integer netSalary) {
            this.netSalary = netSalary;
        }

        public String getTotalAlraedyPaidAmount() {
            return totalAlraedyPaidAmount;
        }

        public void setTotalAlraedyPaidAmount(String totalAlraedyPaidAmount) {
            this.totalAlraedyPaidAmount = totalAlraedyPaidAmount;
        }

        public Integer getRemainingSalary() {
            return remainingSalary;
        }

        public void setRemainingSalary(Integer remainingSalary) {
            this.remainingSalary = remainingSalary;
        }

        public Integer getLeaveDeduction() {
            return leaveDeduction;
        }

        public void setLeaveDeduction(Integer leaveDeduction) {
            this.leaveDeduction = leaveDeduction;
        }

        public Integer getTaxs() {
            return taxs;
        }

        public void setTaxs(Integer taxs) {
            this.taxs = taxs;
        }

        public String getOtherDeductionAmt() {
            return otherDeductionAmt;
        }

        public void setOtherDeductionAmt(String otherDeductionAmt) {
            this.otherDeductionAmt = otherDeductionAmt;
        }

        public String getIsSettalment() {
            return isSettalment;
        }

        public void setIsSettalment(String isSettalment) {
            this.isSettalment = isSettalment;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

    }
}
