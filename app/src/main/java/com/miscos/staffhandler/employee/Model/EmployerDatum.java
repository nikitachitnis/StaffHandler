package com.miscos.staffhandler.employee.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployerDatum
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("employer_id")
    @Expose
    private String employerId;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("office_gps_location")
    @Expose
    private String officeGpsLocation;
    @SerializedName("office_gps_plotting")
    @Expose
    private String officeGpsPlotting;
    @SerializedName("gps_method")
    @Expose
    private String gpsMethod;
    @SerializedName("business_type")
    @Expose
    private String businessType;
    @SerializedName("owner_name")
    @Expose
    private String ownerName;
    @SerializedName("owner_contact")
    @Expose
    private String ownerContact;
    @SerializedName("no_of_emp")
    @Expose
    private String noOfEmp;
    @SerializedName("office_type")
    @Expose
    private String officeType;
    @SerializedName("office_picture")
    @Expose
    private String officePicture;
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
    @SerializedName("owner_email_id")
    @Expose
    private String ownerEmailId;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("free_period")
    @Expose
    private String freePeriod;
    @SerializedName("office_address")
    @Expose
    private String officeAddress;
    @SerializedName("owner_age")
    @Expose
    private String ownerAge;
    @SerializedName("owner_gender")
    @Expose
    private String ownerGender;
    @SerializedName("geo_permitted_distance")
    @Expose
    private String geoPermittedDistance;
    @SerializedName("attendance_type")
    @Expose
    private String attendanceType;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("modified_on")
    @Expose
    private String modifiedOn;
    @SerializedName("week_off")
    @Expose
    private String weekOff;
    @SerializedName("qr_type")
    @Expose
    private String qrType;
    @SerializedName("current_qr")
    @Expose
    private String currentQr;
    @SerializedName("previous_qr")
    @Expose
    private String previousQr;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("device_password")
    @Expose
    private String devicePassword;
    @SerializedName("scheme_id")
    @Expose
    private String schemeId;
    @SerializedName("scheme_type")
    @Expose
    private String schemeType;
    @SerializedName("scheme_expire_on")
    @Expose
    private String schemeExpireOn;
    @SerializedName("optional_address")
    @Expose
    private String optionalAddress;
    @SerializedName("shift_count")
    @Expose
    private String shiftCount;
    @SerializedName("sec_1_in_timing")
    @Expose
    private String sec1InTiming;
    @SerializedName("sec_1_out_timing")
    @Expose
    private String sec1OutTiming;
    @SerializedName("sec_2_in_timing")
    @Expose
    private String sec2InTiming;
    @SerializedName("sec_2_out_timing")
    @Expose
    private String sec2OutTiming;
    @SerializedName("sec_3_in_timing")
    @Expose
    private String sec3InTiming;
    @SerializedName("sec_3_out_timing")
    @Expose
    private String sec3OutTiming;
    @SerializedName("sec_1_pre_start_timing")
    @Expose
    private String sec1PreStartTiming;
    @SerializedName("sec_1_post_close_timing")
    @Expose
    private String sec1PostCloseTiming;
    @SerializedName("sec_2_pre_start_timing")
    @Expose
    private String sec2PreStartTiming;
    @SerializedName("sec_2_post_close_timing")
    @Expose
    private String sec2PostCloseTiming;
    @SerializedName("sec_3_pre_start_timing")
    @Expose
    private String sec3PreStartTiming;
    @SerializedName("sec_3_post_close_timing")
    @Expose
    private String sec3PostCloseTiming;
    @SerializedName("wifi_names")
    @Expose
    private String wifiNames;
    @SerializedName("additional_administrators")
    @Expose
    private String additionalAdministrators;
    @SerializedName("Where_did_the_registration_come_from")
    @Expose
    private String whereDidTheRegistrationComeFrom;
    @SerializedName("absent_leave_deduction")
    @Expose
    private String absentLeaveDeduction;
    @SerializedName("want_adhar_verification")
    @Expose
    private String wantAdharVerification;
    @SerializedName("adhar_verification_count_assign")
    @Expose
    private String adharVerificationCountAssign;
    @SerializedName("adhar_verification_count_used")
    @Expose
    private String adharVerificationCountUsed;
    @SerializedName("employee_number_type")
    @Expose
    private String employeeNumberType;
    @SerializedName("employee_number_format")
    @Expose
    private String employeeNumberFormat;
    @SerializedName("last_issued_no")
    @Expose
    private String lastIssuedNo;
    @SerializedName("enable_save_feedback_employee")
    @Expose
    private String enableSaveFeedbackEmployee;
    @SerializedName("enable_employee_activity_message")
    @Expose
    private String enableEmployeeActivityMessage;

    @SerializedName("adhar_verification_feedback")
    @Expose
    private String adharVerificationFeedback;
    @SerializedName("enable_salary_management")
    @Expose
    private String enable_salary_management;
    @SerializedName("enable_leave_management")
    @Expose
    private String enable_leave_management;
    @SerializedName("enable_show_salary_detail")
    @Expose
    private String enable_show_salary_detail;
    public final static Parcelable.Creator<EmployerDatum> CREATOR = new Parcelable.Creator<EmployerDatum>() {


        @SuppressWarnings({
                "unchecked"
        })
        public EmployerDatum createFromParcel(Parcel in) {
            return new EmployerDatum(in);
        }

        public EmployerDatum[] newArray(int size) {
            return (new EmployerDatum[size]);
        }

    }
            ;

    protected EmployerDatum(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.employerId = ((String) in.readValue((String.class.getClassLoader())));
        this.companyName = ((String) in.readValue((String.class.getClassLoader())));
        this.officeGpsLocation = ((String) in.readValue((String.class.getClassLoader())));
        this.officeGpsPlotting = ((String) in.readValue((String.class.getClassLoader())));
        this.gpsMethod = ((String) in.readValue((String.class.getClassLoader())));
        this.businessType = ((String) in.readValue((String.class.getClassLoader())));
        this.ownerName = ((String) in.readValue((String.class.getClassLoader())));
        this.ownerContact = ((String) in.readValue((String.class.getClassLoader())));
        this.noOfEmp = ((String) in.readValue((String.class.getClassLoader())));
        this.officeType = ((String) in.readValue((String.class.getClassLoader())));
        this.officePicture = ((String) in.readValue((String.class.getClassLoader())));
        this.inTiming = ((String) in.readValue((String.class.getClassLoader())));
        this.outTiming = ((String) in.readValue((String.class.getClassLoader())));
        this.preStartTiming = ((String) in.readValue((String.class.getClassLoader())));
        this.postCloseTiming = ((String) in.readValue((String.class.getClassLoader())));
        this.ownerEmailId = ((String) in.readValue((String.class.getClassLoader())));
        this.createdOn = ((String) in.readValue((String.class.getClassLoader())));
        this.freePeriod = ((String) in.readValue((String.class.getClassLoader())));
        this.officeAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.ownerAge = ((String) in.readValue((String.class.getClassLoader())));
        this.ownerGender = ((String) in.readValue((String.class.getClassLoader())));
        this.geoPermittedDistance = ((String) in.readValue((String.class.getClassLoader())));
        this.attendanceType = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.modifiedOn = ((String) in.readValue((String.class.getClassLoader())));
        this.weekOff = ((String) in.readValue((String.class.getClassLoader())));
        this.qrType = ((String) in.readValue((String.class.getClassLoader())));
        this.currentQr = ((String) in.readValue((String.class.getClassLoader())));
        this.previousQr = ((String) in.readValue((String.class.getClassLoader())));
        this.deviceId = ((String) in.readValue((String.class.getClassLoader())));
        this.devicePassword = ((String) in.readValue((String.class.getClassLoader())));
        this.schemeId = ((String) in.readValue((String.class.getClassLoader())));
        this.schemeType = ((String) in.readValue((String.class.getClassLoader())));
        this.schemeExpireOn = ((String) in.readValue((String.class.getClassLoader())));
        this.optionalAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.shiftCount = ((String) in.readValue((String.class.getClassLoader())));
        this.sec1InTiming = ((String) in.readValue((String.class.getClassLoader())));
        this.sec1OutTiming = ((String) in.readValue((String.class.getClassLoader())));
        this.sec2InTiming = ((String) in.readValue((String.class.getClassLoader())));
        this.sec2OutTiming = ((String) in.readValue((String.class.getClassLoader())));
        this.sec3InTiming = ((String) in.readValue((String.class.getClassLoader())));
        this.sec3OutTiming = ((String) in.readValue((String.class.getClassLoader())));
        this.sec1PreStartTiming = ((String) in.readValue((String.class.getClassLoader())));
        this.sec1PostCloseTiming = ((String) in.readValue((String.class.getClassLoader())));
        this.sec2PreStartTiming = ((String) in.readValue((String.class.getClassLoader())));
        this.sec2PostCloseTiming = ((String) in.readValue((String.class.getClassLoader())));
        this.sec3PreStartTiming = ((String) in.readValue((String.class.getClassLoader())));
        this.sec3PostCloseTiming = ((String) in.readValue((String.class.getClassLoader())));
        this.wifiNames = ((String) in.readValue((String.class.getClassLoader())));
        this.additionalAdministrators = ((String) in.readValue((String.class.getClassLoader())));
        this.whereDidTheRegistrationComeFrom = ((String) in.readValue((String.class.getClassLoader())));
        this.absentLeaveDeduction = ((String) in.readValue((String.class.getClassLoader())));
        this.wantAdharVerification = ((String) in.readValue((String.class.getClassLoader())));
        this.adharVerificationCountAssign = ((String) in.readValue((String.class.getClassLoader())));
        this.adharVerificationCountUsed = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeNumberType = ((String) in.readValue((String.class.getClassLoader())));
        this.employeeNumberFormat = ((String) in.readValue((String.class.getClassLoader())));
        this.lastIssuedNo = ((String) in.readValue((String.class.getClassLoader())));
        this.enableSaveFeedbackEmployee = ((String) in.readValue((String.class.getClassLoader())));
        this.enableEmployeeActivityMessage = ((String) in.readValue((String.class.getClassLoader())));
        this.adharVerificationFeedback = ((String) in.readValue((String.class.getClassLoader())));
        this.enable_salary_management=((String) in.readValue((String.class.getClassLoader())));
        this.enable_leave_management=((String) in.readValue((String.class.getClassLoader())));
        this.enable_show_salary_detail=((String) in.readValue((String.class.getClassLoader())));

    }

    public String getEnable_salary_management() {
        return enable_salary_management;
    }

    public void setEnable_salary_management(String enable_salary_management) {
        this.enable_salary_management = enable_salary_management;
    }

    public String getEnable_leave_management() {
        return enable_leave_management;
    }

    public void setEnable_leave_management(String enable_leave_management) {
        this.enable_leave_management = enable_leave_management;
    }

    public String getEnable_show_salary_detail() {
        return enable_show_salary_detail;
    }

    public void setEnable_show_salary_detail(String enable_show_salary_detail) {
        this.enable_show_salary_detail = enable_show_salary_detail;
    }

    public EmployerDatum() {
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOfficeGpsLocation() {
        return officeGpsLocation;
    }

    public void setOfficeGpsLocation(String officeGpsLocation) {
        this.officeGpsLocation = officeGpsLocation;
    }

    public String getOfficeGpsPlotting() {
        return officeGpsPlotting;
    }

    public void setOfficeGpsPlotting(String officeGpsPlotting) {
        this.officeGpsPlotting = officeGpsPlotting;
    }

    public String getGpsMethod() {
        return gpsMethod;
    }

    public void setGpsMethod(String gpsMethod) {
        this.gpsMethod = gpsMethod;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerContact() {
        return ownerContact;
    }

    public void setOwnerContact(String ownerContact) {
        this.ownerContact = ownerContact;
    }

    public String getNoOfEmp() {
        return noOfEmp;
    }

    public void setNoOfEmp(String noOfEmp) {
        this.noOfEmp = noOfEmp;
    }

    public String getOfficeType() {
        return officeType;
    }

    public void setOfficeType(String officeType) {
        this.officeType = officeType;
    }

    public String getOfficePicture() {
        return officePicture;
    }

    public void setOfficePicture(String officePicture) {
        this.officePicture = officePicture;
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

    public String getOwnerEmailId() {
        return ownerEmailId;
    }

    public void setOwnerEmailId(String ownerEmailId) {
        this.ownerEmailId = ownerEmailId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getFreePeriod() {
        return freePeriod;
    }

    public void setFreePeriod(String freePeriod) {
        this.freePeriod = freePeriod;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getOwnerAge() {
        return ownerAge;
    }

    public void setOwnerAge(String ownerAge) {
        this.ownerAge = ownerAge;
    }

    public String getOwnerGender() {
        return ownerGender;
    }

    public void setOwnerGender(String ownerGender) {
        this.ownerGender = ownerGender;
    }

    public String getGeoPermittedDistance() {
        return geoPermittedDistance;
    }

    public void setGeoPermittedDistance(String geoPermittedDistance) {
        this.geoPermittedDistance = geoPermittedDistance;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getWeekOff() {
        return weekOff;
    }

    public void setWeekOff(String weekOff) {
        this.weekOff = weekOff;
    }

    public String getQrType() {
        return qrType;
    }

    public void setQrType(String qrType) {
        this.qrType = qrType;
    }

    public String getCurrentQr() {
        return currentQr;
    }

    public void setCurrentQr(String currentQr) {
        this.currentQr = currentQr;
    }

    public String getPreviousQr() {
        return previousQr;
    }

    public void setPreviousQr(String previousQr) {
        this.previousQr = previousQr;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDevicePassword() {
        return devicePassword;
    }

    public void setDevicePassword(String devicePassword) {
        this.devicePassword = devicePassword;
    }

    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }

    public String getSchemeType() {
        return schemeType;
    }

    public void setSchemeType(String schemeType) {
        this.schemeType = schemeType;
    }

    public String getSchemeExpireOn() {
        return schemeExpireOn;
    }

    public void setSchemeExpireOn(String schemeExpireOn) {
        this.schemeExpireOn = schemeExpireOn;
    }

    public String getOptionalAddress() {
        return optionalAddress;
    }

    public void setOptionalAddress(String optionalAddress) {
        this.optionalAddress = optionalAddress;
    }

    public String getShiftCount() {
        return shiftCount;
    }

    public void setShiftCount(String shiftCount) {
        this.shiftCount = shiftCount;
    }

    public String getSec1InTiming() {
        return sec1InTiming;
    }

    public void setSec1InTiming(String sec1InTiming) {
        this.sec1InTiming = sec1InTiming;
    }

    public String getSec1OutTiming() {
        return sec1OutTiming;
    }

    public void setSec1OutTiming(String sec1OutTiming) {
        this.sec1OutTiming = sec1OutTiming;
    }

    public String getSec2InTiming() {
        return sec2InTiming;
    }

    public void setSec2InTiming(String sec2InTiming) {
        this.sec2InTiming = sec2InTiming;
    }

    public String getSec2OutTiming() {
        return sec2OutTiming;
    }

    public void setSec2OutTiming(String sec2OutTiming) {
        this.sec2OutTiming = sec2OutTiming;
    }

    public String getSec3InTiming() {
        return sec3InTiming;
    }

    public void setSec3InTiming(String sec3InTiming) {
        this.sec3InTiming = sec3InTiming;
    }

    public String getSec3OutTiming() {
        return sec3OutTiming;
    }

    public void setSec3OutTiming(String sec3OutTiming) {
        this.sec3OutTiming = sec3OutTiming;
    }

    public String getSec1PreStartTiming() {
        return sec1PreStartTiming;
    }

    public void setSec1PreStartTiming(String sec1PreStartTiming) {
        this.sec1PreStartTiming = sec1PreStartTiming;
    }

    public String getSec1PostCloseTiming() {
        return sec1PostCloseTiming;
    }

    public void setSec1PostCloseTiming(String sec1PostCloseTiming) {
        this.sec1PostCloseTiming = sec1PostCloseTiming;
    }

    public String getSec2PreStartTiming() {
        return sec2PreStartTiming;
    }

    public void setSec2PreStartTiming(String sec2PreStartTiming) {
        this.sec2PreStartTiming = sec2PreStartTiming;
    }

    public String getSec2PostCloseTiming() {
        return sec2PostCloseTiming;
    }

    public void setSec2PostCloseTiming(String sec2PostCloseTiming) {
        this.sec2PostCloseTiming = sec2PostCloseTiming;
    }

    public String getSec3PreStartTiming() {
        return sec3PreStartTiming;
    }

    public void setSec3PreStartTiming(String sec3PreStartTiming) {
        this.sec3PreStartTiming = sec3PreStartTiming;
    }

    public String getSec3PostCloseTiming() {
        return sec3PostCloseTiming;
    }

    public void setSec3PostCloseTiming(String sec3PostCloseTiming) {
        this.sec3PostCloseTiming = sec3PostCloseTiming;
    }

    public String getWifiNames() {
        return wifiNames;
    }

    public void setWifiNames(String wifiNames) {
        this.wifiNames = wifiNames;
    }

    public String getAdditionalAdministrators() {
        return additionalAdministrators;
    }

    public void setAdditionalAdministrators(String additionalAdministrators) {
        this.additionalAdministrators = additionalAdministrators;
    }

    public String getWhereDidTheRegistrationComeFrom() {
        return whereDidTheRegistrationComeFrom;
    }

    public void setWhereDidTheRegistrationComeFrom(String whereDidTheRegistrationComeFrom) {
        this.whereDidTheRegistrationComeFrom = whereDidTheRegistrationComeFrom;
    }

    public String getAbsentLeaveDeduction() {
        return absentLeaveDeduction;
    }

    public void setAbsentLeaveDeduction(String absentLeaveDeduction) {
        this.absentLeaveDeduction = absentLeaveDeduction;
    }

    public String getWantAdharVerification() {
        return wantAdharVerification;
    }

    public void setWantAdharVerification(String wantAdharVerification) {
        this.wantAdharVerification = wantAdharVerification;
    }

    public String getAdharVerificationCountAssign() {
        return adharVerificationCountAssign;
    }

    public void setAdharVerificationCountAssign(String adharVerificationCountAssign) {
        this.adharVerificationCountAssign = adharVerificationCountAssign;
    }

    public String getAdharVerificationCountUsed() {
        return adharVerificationCountUsed;
    }

    public void setAdharVerificationCountUsed(String adharVerificationCountUsed) {
        this.adharVerificationCountUsed = adharVerificationCountUsed;
    }

    public String getEmployeeNumberType() {
        return employeeNumberType;
    }

    public void setEmployeeNumberType(String employeeNumberType) {
        this.employeeNumberType = employeeNumberType;
    }

    public String getEmployeeNumberFormat() {
        return employeeNumberFormat;
    }

    public void setEmployeeNumberFormat(String employeeNumberFormat) {
        this.employeeNumberFormat = employeeNumberFormat;
    }

    public String getLastIssuedNo() {
        return lastIssuedNo;
    }

    public void setLastIssuedNo(String lastIssuedNo) {
        this.lastIssuedNo = lastIssuedNo;
    }

    public String getEnableSaveFeedbackEmployee() {
        return enableSaveFeedbackEmployee;
    }

    public void setEnableSaveFeedbackEmployee(String enableSaveFeedbackEmployee) {
        this.enableSaveFeedbackEmployee = enableSaveFeedbackEmployee;
    }

    public String getEnableEmployeeActivityMessage() {
        return enableEmployeeActivityMessage;
    }

    public void setEnableEmployeeActivityMessage(String enableEmployeeActivityMessage) {
        this.enableEmployeeActivityMessage = enableEmployeeActivityMessage;
    }



    public String getAdharVerificationFeedback() {
        return adharVerificationFeedback;
    }

    public void setAdharVerificationFeedback(String adharVerificationFeedback) {
        this.adharVerificationFeedback = adharVerificationFeedback;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(employerId);
        dest.writeValue(companyName);
        dest.writeValue(officeGpsLocation);
        dest.writeValue(officeGpsPlotting);
        dest.writeValue(gpsMethod);
        dest.writeValue(businessType);
        dest.writeValue(ownerName);
        dest.writeValue(ownerContact);
        dest.writeValue(noOfEmp);
        dest.writeValue(officeType);
        dest.writeValue(officePicture);
        dest.writeValue(inTiming);
        dest.writeValue(outTiming);
        dest.writeValue(preStartTiming);
        dest.writeValue(postCloseTiming);
        dest.writeValue(ownerEmailId);
        dest.writeValue(createdOn);
        dest.writeValue(freePeriod);
        dest.writeValue(officeAddress);
        dest.writeValue(ownerAge);
        dest.writeValue(ownerGender);
        dest.writeValue(geoPermittedDistance);
        dest.writeValue(attendanceType);
        dest.writeValue(status);
        dest.writeValue(modifiedOn);
        dest.writeValue(weekOff);
        dest.writeValue(qrType);
        dest.writeValue(currentQr);
        dest.writeValue(previousQr);
        dest.writeValue(deviceId);
        dest.writeValue(devicePassword);
        dest.writeValue(schemeId);
        dest.writeValue(schemeType);
        dest.writeValue(schemeExpireOn);
        dest.writeValue(optionalAddress);
        dest.writeValue(shiftCount);
        dest.writeValue(sec1InTiming);
        dest.writeValue(sec1OutTiming);
        dest.writeValue(sec2InTiming);
        dest.writeValue(sec2OutTiming);
        dest.writeValue(sec3InTiming);
        dest.writeValue(sec3OutTiming);
        dest.writeValue(sec1PreStartTiming);
        dest.writeValue(sec1PostCloseTiming);
        dest.writeValue(sec2PreStartTiming);
        dest.writeValue(sec2PostCloseTiming);
        dest.writeValue(sec3PreStartTiming);
        dest.writeValue(sec3PostCloseTiming);
        dest.writeValue(wifiNames);
        dest.writeValue(additionalAdministrators);
        dest.writeValue(whereDidTheRegistrationComeFrom);
        dest.writeValue(absentLeaveDeduction);
        dest.writeValue(wantAdharVerification);
        dest.writeValue(adharVerificationCountAssign);
        dest.writeValue(adharVerificationCountUsed);
        dest.writeValue(employeeNumberType);
        dest.writeValue(employeeNumberFormat);
        dest.writeValue(lastIssuedNo);
        dest.writeValue(enableSaveFeedbackEmployee);
        dest.writeValue(enableEmployeeActivityMessage);
        dest.writeValue(adharVerificationFeedback);
        dest.writeValue(enable_salary_management);
        dest.writeValue(enable_leave_management);
        dest.writeValue(enable_show_salary_detail);
    }

    public int describeContents() {
        return  0;
    }



}
