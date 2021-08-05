
package com.miscos.staffhandler.hrms_management.SalTransferNCalculations;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AmountList implements Parcelable
{

    @SerializedName("employer_id")
    @Expose
    private String employerId;
    @SerializedName("employee_id")
    @Expose
    private String employeeId;

    @SerializedName("employee_no")
    @Expose
    private String employee_no;
    @SerializedName("employee_name")
    @Expose
    private String employeeName;
    @SerializedName("actual_salary")
    @Expose
    private Double actualSalary;
    @SerializedName("remaining_salary")
    @Expose
    private Double remainingSalary;
    @SerializedName("leave_deduction")
    @Expose
    private Double leaveDeduction;
    @SerializedName("taxs")
    @Expose
    private Double taxs;
    @SerializedName("other_deduction_amt")
    @Expose
    private Double otherDeductionAmt;
    @SerializedName("total")
    @Expose
    private Double total;
    @SerializedName("is_settalment")
    @Expose
    private String  settlement;
    @SerializedName("net_salary")
    @Expose
    private Double  net_salary;

    private String  tax_details="";

    public String getEmployee_no() {
        return employee_no;
    }

    public void setEmployee_no(String employee_no) {
        this.employee_no = employee_no;
    }

    public String getTax_details() {
        return tax_details;
    }

    public void setTax_details(String tax_details) {
        this.tax_details = tax_details;
    }

    public Double getNet_salary() {
        return net_salary;
    }

    public void setNet_salary(Double net_salary) {
        this.net_salary = net_salary;
    }

    public String getSettlement() {
        return settlement;
    }

    public void setSettlement(String settlement) {
        this.settlement = settlement;
    }

    public final static Creator<AmountList> CREATOR = new Creator<AmountList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AmountList createFromParcel(Parcel in) {
            return new AmountList(in);
        }

        public AmountList[] newArray(int size) {
            return (new AmountList[size]);
        }

    }
    ;

    protected AmountList(Parcel in) {
        this.employerId = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeId = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeName = ((String) in.readValue((String.class.getClassLoader())));
        this.actualSalary = ((Double) in.readValue((Double.class.getClassLoader())));
        this.remainingSalary = ((Double) in.readValue((Double.class.getClassLoader())));
        this.leaveDeduction = ((Double) in.readValue((Double.class.getClassLoader())));
        this.taxs = ((Double) in.readValue((Double.class.getClassLoader())));
        this.otherDeductionAmt = ((Double) in.readValue((Double.class.getClassLoader())));
        this.total = ((Double) in.readValue((Double.class.getClassLoader())));
    }

    public AmountList() {
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

    public Double getActualSalary() {
        return actualSalary;
    }

    public void setActualSalary(Double actualSalary) {
        this.actualSalary = actualSalary;
    }

    public Double getRemainingSalary() {
        return remainingSalary;
    }

    public void setRemainingSalary(Double remainingSalary) {
        this.remainingSalary = remainingSalary;
    }

    public Double getLeaveDeduction() {
        return leaveDeduction;
    }

    public void setLeaveDeduction(Double leaveDeduction) {
        this.leaveDeduction = leaveDeduction;
    }

    public Double getTaxs() {
        return taxs;
    }

    public void setTaxs(Double taxs) {
        this.taxs = taxs;
    }

    public Double getOtherDeductionAmt() {
        return otherDeductionAmt;
    }

    public void setOtherDeductionAmt(Double otherDeductionAmt) {
        this.otherDeductionAmt = otherDeductionAmt;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(employerId);
        dest.writeValue(employeeId);
        dest.writeValue(employeeName);
        dest.writeValue(actualSalary);
        dest.writeValue(remainingSalary);
        dest.writeValue(leaveDeduction);
        dest.writeValue(taxs);
        dest.writeValue(otherDeductionAmt);
        dest.writeValue(total);
    }

    public int describeContents() {
        return  0;
    }

}
