package com.miscos.staffhandler.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class EmployeeListModel implements Parcelable
{
    String em_id,employee_id,employer_id,password,login_pin,mobile_no,employee_name,designation,address,email_id,employee_pic,created_on
            ,age,gender,status,device_id,login_status,device_os,device_version,notification_status,week_off,new_arrangement_list,in_timing
            ,out_timing,pre_start_timing,post_close_timing,special_duty,employeeType,register_new_emp,employee_report,policy_configuration,shift_management,special_management
            ,roster_management,holiday_attendance,holiday_management,other_settings,view_emergency_login_logout,permitted_for_emergency,employee_manual_attendance,operation_type,alert_on_holiday,shift_assign_list;
String date_of_joining_date,employee_no,aadhar_no,isVerified="no",permanent_address="",employement_type="";
String date_of_birth="",panNo="",isHavingSmartPhone="no",contract_type="",contarct_name="";
String department_name,designation_name,department_id;


public EmployeeListModel()
{

}
    protected EmployeeListModel(Parcel in)
    {
        em_id = in.readString();
        employee_id = in.readString();
        employer_id = in.readString();
        password = in.readString();
        login_pin = in.readString();
        mobile_no = in.readString();
        employee_name = in.readString();
        designation = in.readString();
        address = in.readString();
        email_id = in.readString();
        employee_pic = in.readString();
        created_on = in.readString();
        age = in.readString();
        gender = in.readString();
        status = in.readString();
        device_id = in.readString();
        login_status = in.readString();
        device_os = in.readString();
        device_version = in.readString();
        notification_status = in.readString();
        week_off = in.readString();
        new_arrangement_list = in.readString();
        in_timing = in.readString();
        out_timing = in.readString();
        pre_start_timing = in.readString();
        post_close_timing = in.readString();
        special_duty = in.readString();
        employeeType = in.readString();
        register_new_emp = in.readString();
        employee_report = in.readString();
        policy_configuration = in.readString();
        shift_management = in.readString();
        special_management = in.readString();
        roster_management = in.readString();
        holiday_attendance = in.readString();
        holiday_management = in.readString();
        other_settings = in.readString();
        view_emergency_login_logout = in.readString();
        permitted_for_emergency = in.readString();
        employee_manual_attendance = in.readString();
        operation_type = in.readString();
        alert_on_holiday = in.readString();
        shift_assign_list = in.readString();
        date_of_joining_date = in.readString();
        employee_no = in.readString();
        aadhar_no = in.readString();
        isVerified = in.readString();
        permanent_address = in.readString();
        employement_type = in.readString();
        date_of_birth=in.readString();
        panNo = in.readString();
        isHavingSmartPhone=in.readString();
        contarct_name = in.readString();
        contract_type=in.readString();
        department_id=in.readString();
        department_name = in.readString();
        designation_name=in.readString();

    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getDesignation_name() {
        return designation_name;
    }

    public void setDesignation_name(String designation_name) {
        this.designation_name = designation_name;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getContract_type() {
        return contract_type;
    }

    public void setContract_type(String contract_type) {
        this.contract_type = contract_type;
    }

    public String getContarct_name() {
        return contarct_name;
    }

    public void setContarct_name(String contarct_name) {
        this.contarct_name = contarct_name;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getIsHavingSmartPhone() {
        return isHavingSmartPhone;
    }

    public void setIsHavingSmartPhone(String isHavingSmartPhone) {
        this.isHavingSmartPhone = isHavingSmartPhone;
    }

    public static final Creator<EmployeeListModel> CREATOR = new Creator<EmployeeListModel>() {
        @Override
        public EmployeeListModel createFromParcel(Parcel in) {
            return new EmployeeListModel(in);
        }

        @Override
        public EmployeeListModel[] newArray(int size) {
            return new EmployeeListModel[size];
        }
    };

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getAadhar_no()
    {
        return aadhar_no;
    }

    public void setAadhar_no(String aadhar_no)
    {
        this.aadhar_no = aadhar_no;
    }

    public String getIsVerified()
    {
        return isVerified;
    }

    public void setIsVerified(String isVerified)
    {
        this.isVerified = isVerified;
    }

    public String getPermanent_address()
    {
        return permanent_address;
    }

    public void setPermanent_address(String permanent_address) {
        this.permanent_address = permanent_address;
    }

    public String getEmployement_type() {
        return employement_type;
    }

    public void setEmployement_type(String employement_type) {
        this.employement_type = employement_type;
    }

    public String getEmployee_no() {
        return employee_no;
    }

    public void setEmployee_no(String employee_no) {
        this.employee_no = employee_no;
    }

    public String getDate_of_joining_date()
    {
        return date_of_joining_date;
    }

    public void setDate_of_joining_date(String date_of_joining_date) {
        this.date_of_joining_date = date_of_joining_date;
    }

    public String getEm_id() {
        return em_id;
    }

    public void setEm_id(String em_id) {
        this.em_id = em_id;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployer_id() {
        return employer_id;
    }

    public void setEmployer_id(String employer_id) {
        this.employer_id = employer_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin_pin() {
        return login_pin;
    }

    public void setLogin_pin(String login_pin) {
        this.login_pin = login_pin;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
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

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getEmployee_pic() {
        return employee_pic;
    }

    public void setEmployee_pic(String employee_pic) {
        this.employee_pic = employee_pic;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
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

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getLogin_status() {
        return login_status;
    }

    public void setLogin_status(String login_status) {
        this.login_status = login_status;
    }

    public String getDevice_os() {
        return device_os;
    }

    public void setDevice_os(String device_os) {
        this.device_os = device_os;
    }

    public String getDevice_version() {
        return device_version;
    }

    public void setDevice_version(String device_version) {
        this.device_version = device_version;
    }

    public String getNotification_status() {
        return notification_status;
    }

    public void setNotification_status(String notification_status) {
        this.notification_status = notification_status;
    }

    public String getWeek_off() {
        return week_off;
    }

    public void setWeek_off(String week_off) {
        this.week_off = week_off;
    }

    public String getNew_arrangement_list() {
        return new_arrangement_list;
    }

    public void setNew_arrangement_list(String new_arrangement_list) {
        this.new_arrangement_list = new_arrangement_list;
    }

    public String getIn_timing() {
        return in_timing;
    }

    public void setIn_timing(String in_timing) {
        this.in_timing = in_timing;
    }

    public String getOut_timing() {
        return out_timing;
    }

    public void setOut_timing(String out_timing) {
        this.out_timing = out_timing;
    }

    public String getPre_start_timing() {
        return pre_start_timing;
    }

    public void setPre_start_timing(String pre_start_timing) {
        this.pre_start_timing = pre_start_timing;
    }

    public String getPost_close_timing() {
        return post_close_timing;
    }

    public void setPost_close_timing(String post_close_timing) {
        this.post_close_timing = post_close_timing;
    }

    public String getSpecial_duty() {
        return special_duty;
    }

    public void setSpecial_duty(String special_duty) {
        this.special_duty = special_duty;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeetype) {
        this.employeeType = employeetype;
    }

    public String getRegister_new_emp() {
        return register_new_emp;
    }

    public void setRegister_new_emp(String register_new_emp) {
        this.register_new_emp = register_new_emp;
    }

    public String getEmployee_report() {
        return employee_report;
    }

    public void setEmployee_report(String employee_report) {
        this.employee_report = employee_report;
    }

    public String getPolicy_configuration() {
        return policy_configuration;
    }

    public void setPolicy_configuration(String policy_configuration) {
        this.policy_configuration = policy_configuration;
    }

    public String getShift_management() {
        return shift_management;
    }

    public void setShift_management(String shift_management) {
        this.shift_management = shift_management;
    }

    public String getSpecial_management() {
        return special_management;
    }

    public void setSpecial_management(String special_management) {
        this.special_management = special_management;
    }

    public String getRoster_management() {
        return roster_management;
    }

    public void setRoster_management(String roster_management) {
        this.roster_management = roster_management;
    }

    public String getHoliday_attendance() {
        return holiday_attendance;
    }

    public void setHoliday_attendance(String holiday_attendance) {
        this.holiday_attendance = holiday_attendance;
    }

    public String getHoliday_management() {
        return holiday_management;
    }

    public void setHoliday_management(String holiday_management) {
        this.holiday_management = holiday_management;
    }

    public String getOther_settings() {
        return other_settings;
    }

    public void setOther_settings(String other_settings) {
        this.other_settings = other_settings;
    }

    public String getView_emergency_login_logout() {
        return view_emergency_login_logout;
    }

    public void setView_emergency_login_logout(String view_emergency_login_logout) {
        this.view_emergency_login_logout = view_emergency_login_logout;
    }

    public String getPermitted_for_emergency() {
        return permitted_for_emergency;
    }

    public void setPermitted_for_emergency(String permitted_for_emergency) {
        this.permitted_for_emergency = permitted_for_emergency;
    }

    public String getEmployee_manual_attendance() {
        return employee_manual_attendance;
    }

    public void setEmployee_manual_attendance(String employee_manual_attendance) {
        this.employee_manual_attendance = employee_manual_attendance;
    }

    public String getOperation_type() {
        return operation_type;
    }

    public void setOperation_type(String operation_type) {
        this.operation_type = operation_type;
    }

    public String getAlert_on_holiday() {
        return alert_on_holiday;
    }

    public void setAlert_on_holiday(String alert_on_holiday) {
        this.alert_on_holiday = alert_on_holiday;
    }

    public String getShift_assign_list() {
        return shift_assign_list;
    }

    public void setShift_assign_list(String shift_assign_list) {
        this.shift_assign_list = shift_assign_list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(em_id);
        parcel.writeString(employee_id);
        parcel.writeString(employer_id);
        parcel.writeString(password);
        parcel.writeString(login_pin);
        parcel.writeString(mobile_no);
        parcel.writeString(employee_name);
        parcel.writeString(designation);
        parcel.writeString(address);
        parcel.writeString(email_id);
        parcel.writeString(employee_pic);
        parcel.writeString(created_on);
        parcel.writeString(age);
        parcel.writeString(gender);
        parcel.writeString(status);
        parcel.writeString(device_id);
        parcel.writeString(login_status);
        parcel.writeString(device_os);
        parcel.writeString(device_version);
        parcel.writeString(notification_status);
        parcel.writeString(week_off);
        parcel.writeString(new_arrangement_list);
        parcel.writeString(in_timing);
        parcel.writeString(out_timing);
        parcel.writeString(pre_start_timing);
        parcel.writeString(post_close_timing);
        parcel.writeString(special_duty);
        parcel.writeString(employeeType);
        parcel.writeString(register_new_emp);
        parcel.writeString(employee_report);
        parcel.writeString(policy_configuration);
        parcel.writeString(shift_management);
        parcel.writeString(special_management);
        parcel.writeString(roster_management);
        parcel.writeString(holiday_attendance);
        parcel.writeString(holiday_management);
        parcel.writeString(other_settings);
        parcel.writeString(view_emergency_login_logout);
        parcel.writeString(permitted_for_emergency);
        parcel.writeString(employee_manual_attendance);
        parcel.writeString(operation_type);
        parcel.writeString(alert_on_holiday);
        parcel.writeString(shift_assign_list);
        parcel.writeString(date_of_joining_date);
        parcel.writeString(employee_no);
        parcel.writeString(aadhar_no);
        parcel.writeString(isVerified);
        parcel.writeString(permanent_address);
        parcel.writeString(employement_type);
        parcel.writeString(date_of_birth);
        parcel.writeString(panNo);
        parcel.writeString(isHavingSmartPhone);
        parcel.writeString(contarct_name);
        parcel.writeString(contract_type);
        parcel.writeString(department_id);
        parcel.writeString(department_name);
        parcel.writeString(designation_name);

    }
}
