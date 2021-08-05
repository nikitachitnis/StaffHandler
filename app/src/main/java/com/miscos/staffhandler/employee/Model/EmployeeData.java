package com.miscos.staffhandler.employee.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeData implements Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("employer_id")
    @Expose
    private String employerId;
    @SerializedName("employee_id")
    @Expose
    private String employeeId;
    @SerializedName("secondary_employer_id")
    @Expose
    private String secondaryEmployerId;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("login_pin")
    @Expose
    private String loginPin;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("employee_pic")
    @Expose
    private String employeePic;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("login_status")
    @Expose
    private String loginStatus;
    @SerializedName("device_os")
    @Expose
    private String deviceOs;
    @SerializedName("device_version")
    @Expose
    private String deviceVersion;
    @SerializedName("notification_status")
    @Expose
    private String notificationStatus;
    @SerializedName("week_off")
    @Expose
    private String weekOff;
    @SerializedName("new_arrangement_list")
    @Expose
    private String newArrangementList;
    @SerializedName("in_timing")
    @Expose
    private String inTiming;
    @SerializedName("out_timing")
    @Expose
    private String outTiming;
    @SerializedName("pre_start_timing")
    @Expose
    private String preStartTiming;
    @SerializedName("post_close_timing")
    @Expose
    private String postCloseTiming;
    @SerializedName("special_duty")
    @Expose
    private String specialDuty;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("register_new_emp")
    @Expose
    private String registerNewEmp;
    @SerializedName("employee_report")
    @Expose
    private String employeeReport;
    @SerializedName("policy_configuration")
    @Expose
    private String policyConfiguration;
    @SerializedName("shift_management")
    @Expose
    private String shiftManagement;
    @SerializedName("special_management")
    @Expose
    private String specialManagement;
    @SerializedName("roster_management")
    @Expose
    private String rosterManagement;
    @SerializedName("holiday_attendance")
    @Expose
    private String holidayAttendance;
    @SerializedName("holiday_management")
    @Expose
    private String holidayManagement;
    @SerializedName("other_settings")
    @Expose
    private String otherSettings;
    @SerializedName("view_emergency_login_logout")
    @Expose
    private String viewEmergencyLoginLogout;
    @SerializedName("permitted_for_emergency")
    @Expose
    private String permittedForEmergency;
    @SerializedName("employee_manual_attendance")
    @Expose
    private String employeeManualAttendance;
    @SerializedName("employee_manual_attendance_type")
    @Expose
    private String employeeManualAttendanceType;
    @SerializedName("wifi_management")
    @Expose
    private String wifiManagement;
    @SerializedName("salary_payment")
    @Expose
    private String salaryPayment;
    @SerializedName("leave_management")
    @Expose
    private String leaveManagement;
    @SerializedName("view_old_employee_data")
    @Expose
    private String viewOldEmployeeData;
    @SerializedName("operation_type")
    @Expose
    private String operationType;
    @SerializedName("alert_on_holiday")
    @Expose
    private String alertOnHoliday;
    @SerializedName("token_id")
    @Expose
    private String tokenId;
    @SerializedName("1_sal_data")
    @Expose
    private String _1SalData;
    @SerializedName("2_sal_data")
    @Expose
    private String _2SalData;
    @SerializedName("3_sal_data")
    @Expose
    private String _3SalData;
    @SerializedName("4_sal_data")
    @Expose
    private String _4SalData;
    @SerializedName("5_sal_data")
    @Expose
    private String _5SalData;
    @SerializedName("6_sal_data")
    @Expose
    private String _6SalData;
    @SerializedName("7_sal_data")
    @Expose
    private String _7SalData;
    @SerializedName("8_sal_data")
    @Expose
    private String _8SalData;
    @SerializedName("9_sal_data")
    @Expose
    private String _9SalData;
    @SerializedName("10_sal_data")
    @Expose
    private String _10SalData;
    @SerializedName("1_leave_data")
    @Expose
    private String _1LeaveData;
    @SerializedName("2_leave_data")
    @Expose
    private String _2LeaveData;
    @SerializedName("3_leave_data")
    @Expose
    private String _3LeaveData;
    @SerializedName("4_leave_data")
    @Expose
    private String _4LeaveData;
    @SerializedName("5_leave_data")
    @Expose
    private String _5LeaveData;
    @SerializedName("6_leave_data")
    @Expose
    private String _6LeaveData;
    @SerializedName("7_leave_data")
    @Expose
    private String _7LeaveData;
    @SerializedName("8_leave_data")
    @Expose
    private String _8LeaveData;
    @SerializedName("9_leave_data")
    @Expose
    private String _9LeaveData;
    @SerializedName("10_leave_data")
    @Expose
    private String _10LeaveData;
    @SerializedName("provident_found")
    @Expose
    private String providentFound;
    @SerializedName("professional_tax")
    @Expose
    private String professionalTax;
    @SerializedName("ESIC")
    @Expose
    private String eSIC;
    @SerializedName("GST")
    @Expose
    private String gST;
    @SerializedName("TDS")
    @Expose
    private String tDS;
    @SerializedName("1_leave_status")
    @Expose
    private String _1LeaveStatus;
    @SerializedName("2_leave_status")
    @Expose
    private String _2LeaveStatus;
    @SerializedName("3_leave_status")
    @Expose
    private String _3LeaveStatus;
    @SerializedName("4_leave_status")
    @Expose
    private String _4LeaveStatus;
    @SerializedName("5_leave_status")
    @Expose
    private String _5LeaveStatus;
    @SerializedName("6_leave_status")
    @Expose
    private String _6LeaveStatus;
    @SerializedName("7_leave_status")
    @Expose
    private String _7LeaveStatus;
    @SerializedName("8_leave_status")
    @Expose
    private String _8LeaveStatus;
    @SerializedName("9_leave_status")
    @Expose
    private String _9LeaveStatus;
    @SerializedName("10_leave_status")
    @Expose
    private String _10LeaveStatus;
    @SerializedName("applicable_from_date")
    @Expose
    private String applicableFromDate;
    @SerializedName("old_mobile_no_list")
    @Expose
    private String oldMobileNoList;
    @SerializedName("special_duty_applicable_from_date")
    @Expose
    private String special_duty_applicable_from_date;
    @SerializedName("shift_incharge_name")
    @Expose
    private String shift_incharge_name;
    public final static Parcelable.Creator<EmployeeData> CREATOR = new Creator<EmployeeData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public EmployeeData createFromParcel(Parcel in) {
            return new EmployeeData(in);
        }

        public EmployeeData[] newArray(int size) {
            return (new EmployeeData[size]);
        }

    }
            ;

    protected EmployeeData(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.employerId = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeId = ((String) in.readValue((String.class.getClassLoader())));
        this.secondaryEmployerId = ((String) in.readValue((String.class.getClassLoader())));
        this.password = ((String) in.readValue((String.class.getClassLoader())));
        this.loginPin = ((String) in.readValue((String.class.getClassLoader())));
        this.mobileNo = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.designation = ((String) in.readValue((String.class.getClassLoader())));
        this.address = ((String) in.readValue((String.class.getClassLoader())));
        this.emailId = ((String) in.readValue((String.class.getClassLoader())));
        this.employeePic = ((String) in.readValue((String.class.getClassLoader())));
        this.createdOn = ((String) in.readValue((String.class.getClassLoader())));
        this.age = ((String) in.readValue((String.class.getClassLoader())));
        this.gender = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.deviceId = ((String) in.readValue((String.class.getClassLoader())));
        this.loginStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.deviceOs = ((String) in.readValue((String.class.getClassLoader())));
        this.deviceVersion = ((String) in.readValue((String.class.getClassLoader())));
        this.notificationStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.weekOff = ((String) in.readValue((String.class.getClassLoader())));
        this.newArrangementList = ((String) in.readValue((String.class.getClassLoader())));
        this.inTiming = ((String) in.readValue((String.class.getClassLoader())));
        this.outTiming = ((String) in.readValue((String.class.getClassLoader())));
        this.preStartTiming = ((String) in.readValue((String.class.getClassLoader())));
        this.postCloseTiming = ((String) in.readValue((String.class.getClassLoader())));
        this.specialDuty = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.registerNewEmp = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeReport = ((String) in.readValue((String.class.getClassLoader())));
        this.policyConfiguration = ((String) in.readValue((String.class.getClassLoader())));
        this.shiftManagement = ((String) in.readValue((String.class.getClassLoader())));
        this.specialManagement = ((String) in.readValue((String.class.getClassLoader())));
        this.rosterManagement = ((String) in.readValue((String.class.getClassLoader())));
        this.holidayAttendance = ((String) in.readValue((String.class.getClassLoader())));
        this.holidayManagement = ((String) in.readValue((String.class.getClassLoader())));
        this.otherSettings = ((String) in.readValue((String.class.getClassLoader())));
        this.viewEmergencyLoginLogout = ((String) in.readValue((String.class.getClassLoader())));
        this.permittedForEmergency = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeManualAttendance = ((String) in.readValue((String.class.getClassLoader())));
        this.wifiManagement = ((String) in.readValue((String.class.getClassLoader())));
        this.salaryPayment = ((String) in.readValue((String.class.getClassLoader())));
        this.leaveManagement = ((String) in.readValue((String.class.getClassLoader())));
        this.viewOldEmployeeData = ((String) in.readValue((String.class.getClassLoader())));
        this.operationType = ((String) in.readValue((String.class.getClassLoader())));
        this.alertOnHoliday = ((String) in.readValue((String.class.getClassLoader())));
        this.tokenId = ((String) in.readValue((String.class.getClassLoader())));
        this._1SalData = ((String) in.readValue((String.class.getClassLoader())));
        this._2SalData = ((String) in.readValue((String.class.getClassLoader())));
        this._3SalData = ((String) in.readValue((String.class.getClassLoader())));
        this._4SalData = ((String) in.readValue((String.class.getClassLoader())));
        this._5SalData = ((String) in.readValue((String.class.getClassLoader())));
        this._6SalData = ((String) in.readValue((String.class.getClassLoader())));
        this._7SalData = ((String) in.readValue((String.class.getClassLoader())));
        this._8SalData = ((String) in.readValue((String.class.getClassLoader())));
        this._9SalData = ((String) in.readValue((String.class.getClassLoader())));
        this._10SalData = ((String) in.readValue((String.class.getClassLoader())));
        this._1LeaveData = ((String) in.readValue((String.class.getClassLoader())));
        this._2LeaveData = ((String) in.readValue((String.class.getClassLoader())));
        this._3LeaveData = ((String) in.readValue((String.class.getClassLoader())));
        this._4LeaveData = ((String) in.readValue((String.class.getClassLoader())));
        this._5LeaveData = ((String) in.readValue((String.class.getClassLoader())));
        this._6LeaveData = ((String) in.readValue((String.class.getClassLoader())));
        this._7LeaveData = ((String) in.readValue((String.class.getClassLoader())));
        this._8LeaveData = ((String) in.readValue((String.class.getClassLoader())));
        this._9LeaveData = ((String) in.readValue((String.class.getClassLoader())));
        this._10LeaveData = ((String) in.readValue((String.class.getClassLoader())));
        this.providentFound = ((String) in.readValue((String.class.getClassLoader())));
        this.professionalTax = ((String) in.readValue((String.class.getClassLoader())));
        this.eSIC = ((String) in.readValue((String.class.getClassLoader())));
        this.gST = ((String) in.readValue((String.class.getClassLoader())));
        this.tDS = ((String) in.readValue((String.class.getClassLoader())));
        this._1LeaveStatus = ((String) in.readValue((String.class.getClassLoader())));
        this._2LeaveStatus = ((String) in.readValue((String.class.getClassLoader())));
        this._3LeaveStatus = ((String) in.readValue((String.class.getClassLoader())));
        this._4LeaveStatus = ((String) in.readValue((String.class.getClassLoader())));
        this._5LeaveStatus = ((String) in.readValue((String.class.getClassLoader())));
        this._6LeaveStatus = ((String) in.readValue((String.class.getClassLoader())));
        this._7LeaveStatus = ((String) in.readValue((String.class.getClassLoader())));
        this._8LeaveStatus = ((String) in.readValue((String.class.getClassLoader())));
        this._9LeaveStatus = ((String) in.readValue((String.class.getClassLoader())));
        this._10LeaveStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.applicableFromDate = ((String) in.readValue((String.class.getClassLoader())));
        this.oldMobileNoList = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeManualAttendanceType = ((String) in.readValue((String.class.getClassLoader())));
        this.special_duty_applicable_from_date = ((String) in.readValue((String.class.getClassLoader())));
        this.shift_incharge_name = ((String) in.readValue((String.class.getClassLoader())));
    }

    public String getEmployeeManualAttendanceType() {
        return employeeManualAttendanceType;
    }

    public void setEmployeeManualAttendanceType(String employeeManualAttendanceType) {
        this.employeeManualAttendanceType = employeeManualAttendanceType;
    }

    public EmployeeData() {
    }

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

    public String getSecondaryEmployerId() {
        return secondaryEmployerId;
    }

    public void setSecondaryEmployerId(String secondaryEmployerId) {
        this.secondaryEmployerId = secondaryEmployerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginPin() {
        return loginPin;
    }

    public void setLoginPin(String loginPin) {
        this.loginPin = loginPin;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getEmployeePic() {
        return employeePic;
    }

    public void setEmployeePic(String employeePic) {
        this.employeePic = employeePic;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getDeviceOs() {
        return deviceOs;
    }

    public void setDeviceOs(String deviceOs) {
        this.deviceOs = deviceOs;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public String getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(String notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public String getWeekOff() {
        return weekOff;
    }

    public void setWeekOff(String weekOff) {
        this.weekOff = weekOff;
    }

    public String getNewArrangementList() {
        return newArrangementList;
    }

    public void setNewArrangementList(String newArrangementList) {
        this.newArrangementList = newArrangementList;
    }

    public String getInTiming() {
        return inTiming;
    }

    public void setInTiming(String inTiming) {
        this.inTiming = inTiming;
    }

    public String getOutTiming() {
        return outTiming;
    }

    public void setOutTiming(String outTiming) {
        this.outTiming = outTiming;
    }

    public String getPreStartTiming() {
        return preStartTiming;
    }

    public void setPreStartTiming(String preStartTiming) {
        this.preStartTiming = preStartTiming;
    }

    public String getPostCloseTiming() {
        return postCloseTiming;
    }

    public void setPostCloseTiming(String postCloseTiming) {
        this.postCloseTiming = postCloseTiming;
    }

    public String getSpecialDuty() {
        return specialDuty;
    }

    public void setSpecialDuty(String specialDuty) {
        this.specialDuty = specialDuty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegisterNewEmp() {
        return registerNewEmp;
    }

    public void setRegisterNewEmp(String registerNewEmp) {
        this.registerNewEmp = registerNewEmp;
    }

    public String getEmployeeReport() {
        return employeeReport;
    }

    public void setEmployeeReport(String employeeReport) {
        this.employeeReport = employeeReport;
    }

    public String getPolicyConfiguration() {
        return policyConfiguration;
    }

    public void setPolicyConfiguration(String policyConfiguration) {
        this.policyConfiguration = policyConfiguration;
    }

    public String getShiftManagement() {
        return shiftManagement;
    }

    public void setShiftManagement(String shiftManagement) {
        this.shiftManagement = shiftManagement;
    }

    public String getSpecialManagement() {
        return specialManagement;
    }

    public void setSpecialManagement(String specialManagement) {
        this.specialManagement = specialManagement;
    }

    public String getRosterManagement() {
        return rosterManagement;
    }

    public void setRosterManagement(String rosterManagement) {
        this.rosterManagement = rosterManagement;
    }

    public String getHolidayAttendance() {
        return holidayAttendance;
    }

    public void setHolidayAttendance(String holidayAttendance) {
        this.holidayAttendance = holidayAttendance;
    }

    public String getHolidayManagement() {
        return holidayManagement;
    }

    public void setHolidayManagement(String holidayManagement) {
        this.holidayManagement = holidayManagement;
    }

    public String getOtherSettings() {
        return otherSettings;
    }

    public void setOtherSettings(String otherSettings) {
        this.otherSettings = otherSettings;
    }

    public String getViewEmergencyLoginLogout() {
        return viewEmergencyLoginLogout;
    }

    public void setViewEmergencyLoginLogout(String viewEmergencyLoginLogout) {
        this.viewEmergencyLoginLogout = viewEmergencyLoginLogout;
    }

    public String getPermittedForEmergency() {
        return permittedForEmergency;
    }

    public void setPermittedForEmergency(String permittedForEmergency) {
        this.permittedForEmergency = permittedForEmergency;
    }

    public String getEmployeeManualAttendance() {
        return employeeManualAttendance;
    }

    public void setEmployeeManualAttendance(String employeeManualAttendance) {
        this.employeeManualAttendance = employeeManualAttendance;
    }

    public String getWifiManagement() {
        return wifiManagement;
    }

    public void setWifiManagement(String wifiManagement) {
        this.wifiManagement = wifiManagement;
    }

    public String getSalaryPayment() {
        return salaryPayment;
    }

    public void setSalaryPayment(String salaryPayment) {
        this.salaryPayment = salaryPayment;
    }

    public String getLeaveManagement() {
        return leaveManagement;
    }

    public void setLeaveManagement(String leaveManagement) {
        this.leaveManagement = leaveManagement;
    }

    public String getViewOldEmployeeData() {
        return viewOldEmployeeData;
    }

    public void setViewOldEmployeeData(String viewOldEmployeeData) {
        this.viewOldEmployeeData = viewOldEmployeeData;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getAlertOnHoliday() {
        return alertOnHoliday;
    }

    public void setAlertOnHoliday(String alertOnHoliday) {
        this.alertOnHoliday = alertOnHoliday;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String get1SalData() {
        return _1SalData;
    }

    public void set1SalData(String _1SalData) {
        this._1SalData = _1SalData;
    }

    public String get2SalData() {
        return _2SalData;
    }

    public void set2SalData(String _2SalData) {
        this._2SalData = _2SalData;
    }

    public String get3SalData() {
        return _3SalData;
    }

    public void set3SalData(String _3SalData) {
        this._3SalData = _3SalData;
    }

    public String get4SalData() {
        return _4SalData;
    }

    public void set4SalData(String _4SalData) {
        this._4SalData = _4SalData;
    }

    public String get5SalData() {
        return _5SalData;
    }

    public void set5SalData(String _5SalData) {
        this._5SalData = _5SalData;
    }

    public String get6SalData() {
        return _6SalData;
    }

    public void set6SalData(String _6SalData) {
        this._6SalData = _6SalData;
    }

    public String get7SalData() {
        return _7SalData;
    }

    public void set7SalData(String _7SalData) {
        this._7SalData = _7SalData;
    }

    public String get8SalData() {
        return _8SalData;
    }

    public void set8SalData(String _8SalData) {
        this._8SalData = _8SalData;
    }

    public String get9SalData() {
        return _9SalData;
    }

    public void set9SalData(String _9SalData) {
        this._9SalData = _9SalData;
    }

    public String get10SalData() {
        return _10SalData;
    }

    public void set10SalData(String _10SalData) {
        this._10SalData = _10SalData;
    }

    public String get1LeaveData() {
        return _1LeaveData;
    }

    public void set1LeaveData(String _1LeaveData) {
        this._1LeaveData = _1LeaveData;
    }

    public String get2LeaveData() {
        return _2LeaveData;
    }

    public void set2LeaveData(String _2LeaveData) {
        this._2LeaveData = _2LeaveData;
    }

    public String get3LeaveData() {
        return _3LeaveData;
    }

    public void set3LeaveData(String _3LeaveData) {
        this._3LeaveData = _3LeaveData;
    }

    public String get4LeaveData() {
        return _4LeaveData;
    }

    public void set4LeaveData(String _4LeaveData) {
        this._4LeaveData = _4LeaveData;
    }

    public String get5LeaveData() {
        return _5LeaveData;
    }

    public void set5LeaveData(String _5LeaveData) {
        this._5LeaveData = _5LeaveData;
    }

    public String get6LeaveData() {
        return _6LeaveData;
    }

    public void set6LeaveData(String _6LeaveData) {
        this._6LeaveData = _6LeaveData;
    }

    public String get7LeaveData() {
        return _7LeaveData;
    }

    public void set7LeaveData(String _7LeaveData) {
        this._7LeaveData = _7LeaveData;
    }

    public String get8LeaveData() {
        return _8LeaveData;
    }

    public void set8LeaveData(String _8LeaveData) {
        this._8LeaveData = _8LeaveData;
    }

    public String get9LeaveData() {
        return _9LeaveData;
    }

    public void set9LeaveData(String _9LeaveData) {
        this._9LeaveData = _9LeaveData;
    }

    public String get10LeaveData() {
        return _10LeaveData;
    }

    public void set10LeaveData(String _10LeaveData) {
        this._10LeaveData = _10LeaveData;
    }

    public String getProvidentFound() {
        return providentFound;
    }

    public void setProvidentFound(String providentFound) {
        this.providentFound = providentFound;
    }

    public String getProfessionalTax() {
        return professionalTax;
    }

    public void setProfessionalTax(String professionalTax) {
        this.professionalTax = professionalTax;
    }

    public String getESIC() {
        return eSIC;
    }

    public void setESIC(String eSIC) {
        this.eSIC = eSIC;
    }

    public String getGST() {
        return gST;
    }

    public void setGST(String gST) {
        this.gST = gST;
    }

    public String getTDS() {
        return tDS;
    }

    public void setTDS(String tDS) {
        this.tDS = tDS;
    }

    public String get1LeaveStatus() {
        return _1LeaveStatus;
    }

    public void set1LeaveStatus(String _1LeaveStatus) {
        this._1LeaveStatus = _1LeaveStatus;
    }

    public String get2LeaveStatus() {
        return _2LeaveStatus;
    }

    public void set2LeaveStatus(String _2LeaveStatus) {
        this._2LeaveStatus = _2LeaveStatus;
    }

    public String get3LeaveStatus() {
        return _3LeaveStatus;
    }

    public void set3LeaveStatus(String _3LeaveStatus) {
        this._3LeaveStatus = _3LeaveStatus;
    }

    public String get4LeaveStatus() {
        return _4LeaveStatus;
    }

    public void set4LeaveStatus(String _4LeaveStatus) {
        this._4LeaveStatus = _4LeaveStatus;
    }

    public String get5LeaveStatus() {
        return _5LeaveStatus;
    }

    public void set5LeaveStatus(String _5LeaveStatus) {
        this._5LeaveStatus = _5LeaveStatus;
    }

    public String get6LeaveStatus() {
        return _6LeaveStatus;
    }

    public void set6LeaveStatus(String _6LeaveStatus) {
        this._6LeaveStatus = _6LeaveStatus;
    }

    public String get7LeaveStatus() {
        return _7LeaveStatus;
    }

    public void set7LeaveStatus(String _7LeaveStatus) {
        this._7LeaveStatus = _7LeaveStatus;
    }

    public String get8LeaveStatus() {
        return _8LeaveStatus;
    }

    public void set8LeaveStatus(String _8LeaveStatus) {
        this._8LeaveStatus = _8LeaveStatus;
    }

    public String get9LeaveStatus() {
        return _9LeaveStatus;
    }

    public void set9LeaveStatus(String _9LeaveStatus) {
        this._9LeaveStatus = _9LeaveStatus;
    }

    public String get10LeaveStatus() {
        return _10LeaveStatus;
    }

    public void set10LeaveStatus(String _10LeaveStatus) {
        this._10LeaveStatus = _10LeaveStatus;
    }

    public String getApplicableFromDate() {
        return applicableFromDate;
    }

    public void setApplicableFromDate(String applicableFromDate) {
        this.applicableFromDate = applicableFromDate;
    }

    public String getSpecial_duty_applicable_from_date() {
        return special_duty_applicable_from_date;
    }

    public void setSpecial_duty_applicable_from_date(String special_duty_applicable_from_date) {
        this.special_duty_applicable_from_date = special_duty_applicable_from_date;
    }

    public String getOldMobileNoList() {
        return oldMobileNoList;
    }

    public void setOldMobileNoList(String oldMobileNoList) {
        this.oldMobileNoList = oldMobileNoList;
    }

    public String getShift_incharge_name() {
        return shift_incharge_name;
    }

    public void setShift_incharge_name(String shift_incharge_name) {
        this.shift_incharge_name = shift_incharge_name;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(employerId);
        dest.writeValue(employeeId);
        dest.writeValue(secondaryEmployerId);
        dest.writeValue(password);
        dest.writeValue(loginPin);
        dest.writeValue(mobileNo);
        dest.writeValue(name);
        dest.writeValue(designation);
        dest.writeValue(address);
        dest.writeValue(emailId);
        dest.writeValue(employeePic);
        dest.writeValue(createdOn);
        dest.writeValue(age);
        dest.writeValue(gender);
        dest.writeValue(status);
        dest.writeValue(deviceId);
        dest.writeValue(loginStatus);
        dest.writeValue(deviceOs);
        dest.writeValue(deviceVersion);
        dest.writeValue(notificationStatus);
        dest.writeValue(weekOff);
        dest.writeValue(newArrangementList);
        dest.writeValue(inTiming);
        dest.writeValue(outTiming);
        dest.writeValue(preStartTiming);
        dest.writeValue(postCloseTiming);
        dest.writeValue(specialDuty);
        dest.writeValue(type);
        dest.writeValue(registerNewEmp);
        dest.writeValue(employeeReport);
        dest.writeValue(policyConfiguration);
        dest.writeValue(shiftManagement);
        dest.writeValue(specialManagement);
        dest.writeValue(rosterManagement);
        dest.writeValue(holidayAttendance);
        dest.writeValue(holidayManagement);
        dest.writeValue(otherSettings);
        dest.writeValue(viewEmergencyLoginLogout);
        dest.writeValue(permittedForEmergency);
        dest.writeValue(employeeManualAttendance);
        dest.writeValue(wifiManagement);
        dest.writeValue(salaryPayment);
        dest.writeValue(leaveManagement);
        dest.writeValue(viewOldEmployeeData);
        dest.writeValue(operationType);
        dest.writeValue(alertOnHoliday);
        dest.writeValue(tokenId);
        dest.writeValue(_1SalData);
        dest.writeValue(_2SalData);
        dest.writeValue(_3SalData);
        dest.writeValue(_4SalData);
        dest.writeValue(_5SalData);
        dest.writeValue(_6SalData);
        dest.writeValue(_7SalData);
        dest.writeValue(_8SalData);
        dest.writeValue(_9SalData);
        dest.writeValue(_10SalData);
        dest.writeValue(_1LeaveData);
        dest.writeValue(_2LeaveData);
        dest.writeValue(_3LeaveData);
        dest.writeValue(_4LeaveData);
        dest.writeValue(_5LeaveData);
        dest.writeValue(_6LeaveData);
        dest.writeValue(_7LeaveData);
        dest.writeValue(_8LeaveData);
        dest.writeValue(_9LeaveData);
        dest.writeValue(_10LeaveData);
        dest.writeValue(providentFound);
        dest.writeValue(professionalTax);
        dest.writeValue(eSIC);
        dest.writeValue(gST);
        dest.writeValue(tDS);
        dest.writeValue(_1LeaveStatus);
        dest.writeValue(_2LeaveStatus);
        dest.writeValue(_3LeaveStatus);
        dest.writeValue(_4LeaveStatus);
        dest.writeValue(_5LeaveStatus);
        dest.writeValue(_6LeaveStatus);
        dest.writeValue(_7LeaveStatus);
        dest.writeValue(_8LeaveStatus);
        dest.writeValue(_9LeaveStatus);
        dest.writeValue(_10LeaveStatus);
        dest.writeValue(applicableFromDate);
        dest.writeValue(oldMobileNoList);
        dest.writeValue(employeeManualAttendanceType);
        dest.writeValue(special_duty_applicable_from_date);
        dest.writeValue(shift_incharge_name);
    }

    public int describeContents() {
        return  0;
    }

}
