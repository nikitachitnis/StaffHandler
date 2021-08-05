package com.miscos.staffhandler.employer_java.remote.model.fetchdataform2;

import com.google.gson.annotations.SerializedName;

public class EmployeeDataItem{

	@SerializedName("office_picture")
	private String officePicture;

	@SerializedName("qr_type")
	private String qrType;

	@SerializedName("sec_3_pre_start_timing")
	private String sec3PreStartTiming;

	@SerializedName("office_type")
	private String officeType;

	@SerializedName("sec_3_in_timing")
	private String sec3InTiming;

	@SerializedName("sec_1_post_close_timing")
	private String sec1PostCloseTiming;

	@SerializedName("absent_leave_deduction")
	private String absentLeaveDeduction;

	@SerializedName("post_close_timing")
	private String postCloseTiming;

	@SerializedName("sec_1_out_timing")
	private String sec1OutTiming;

	@SerializedName("free_period")
	private String freePeriod;

	@SerializedName("in_timing")
	private String inTiming;

	@SerializedName("Where_did_the_registration_come_from")
	private String whereDidTheRegistrationComeFrom;

	@SerializedName("scheme_type")
	private String schemeType;

	@SerializedName("enable_save_feedback_employee")
	private String enableSaveFeedbackEmployee;

	@SerializedName("sec_2_post_close_timing")
	private String sec2PostCloseTiming;

	@SerializedName("scheme_id")
	private String schemeId;

	@SerializedName("id")
	private String id;

	@SerializedName("last_issued_no")
	private String lastIssuedNo;

	@SerializedName("employer_id")
	private String employerId;

	@SerializedName("adhar_verification_count_used")
	private String adharVerificationCountUsed="0";

	@SerializedName("owner_name")
	private String ownerName;

	@SerializedName("pre_start_timing")
	private String preStartTiming;

	@SerializedName("device_id")
	private String deviceId;

	@SerializedName("sec_2_out_timing")
	private String sec2OutTiming;

	@SerializedName("sec_1_pre_start_timing")
	private String sec1PreStartTiming;

	@SerializedName("sec_2_in_timing")
	private String sec2InTiming;

	@SerializedName("wifi_names")
	private String wifiNames;

	@SerializedName("adhar_verification_feedback")
	private String adharVerificationFeedback;

	@SerializedName("no_of_emp")
	private String noOfEmp;

	@SerializedName("current_qr")
	private String currentQr;

	@SerializedName("employee_number_type")
	private String employeeNumberType="";

	@SerializedName("owner_email_id")
	private String ownerEmailId;

	@SerializedName("created_on")
	private String createdOn;

	@SerializedName("device_password")
	private String devicePassword;

	@SerializedName("optional_address")
	private String optionalAddress;

	@SerializedName("company_name")
	private String companyName;

	@SerializedName("sec_3_post_close_timing")
	private String sec3PostCloseTiming;

	@SerializedName("enable_salary_leave_management")
	private String enableSalaryLeaveManagement;

	@SerializedName("status")
	private String status;

	@SerializedName("want_adhar_verification")
	private String wantAdharVerification="";

	@SerializedName("employee_number_format")
	private String employeeNumberFormat;

	@SerializedName("sec_1_in_timing")
	private String sec1InTiming;

	@SerializedName("gps_method")
	private String gpsMethod;

	@SerializedName("adhar_verification_count_assign")
	private String adharVerificationCountAssign="0";

	@SerializedName("week_off")
	private String weekOff;

	@SerializedName("scheme_expire_on")
	private String schemeExpireOn;

	@SerializedName("additional_administrators")
	private String additionalAdministrators;

	@SerializedName("business_type")
	private String businessType;

	@SerializedName("sec_3_out_timing")
	private String sec3OutTiming;

	@SerializedName("out_timing")
	private String outTiming;

	@SerializedName("attendance_type")
	private String attendanceType;

	@SerializedName("modified_on")
	private String modifiedOn;

	@SerializedName("shift_count")
	private String shiftCount;

	@SerializedName("owner_age")
	private String ownerAge;

	@SerializedName("previous_qr")
	private String previousQr;

	@SerializedName("office_gps_plotting")
	private String officeGpsPlotting;

	@SerializedName("owner_gender")
	private String ownerGender;

	@SerializedName("amount_per_api_verification")
	private int amountPerApiVerification;

	@SerializedName("office_address")
	private String officeAddress;

	@SerializedName("enable_employee_activity_message")
	private String enableEmployeeActivityMessage;

	@SerializedName("sec_2_pre_start_timing")
	private String sec2PreStartTiming;

	@SerializedName("owner_contact")
	private String ownerContact;

	@SerializedName("geo_permitted_distance")
	private String geoPermittedDistance;

	@SerializedName("office_gps_location")
	private String officeGpsLocation;


	public int getSelectedTitlePos(){
		if (ownerName!=null){
			if (ownerName.startsWith("Mrs")){
				return 2;
			} else if (ownerName.startsWith("Mr")){
				return 0;
			} else if (ownerName.startsWith("Ms")){
				return 1;
			} else return 0;
		} else return 0;
	}

	public String getOfficePicture(){
		return officePicture;
	}

	public String getQrType(){
		return qrType;
	}

	public String getSec3PreStartTiming(){
		return sec3PreStartTiming;
	}

	public String getOfficeType(){
		return officeType;
	}

	public String getSec3InTiming(){
		return sec3InTiming;
	}

	public String getSec1PostCloseTiming(){
		return sec1PostCloseTiming;
	}

	public String getAbsentLeaveDeduction(){
		return absentLeaveDeduction;
	}

	public String getPostCloseTiming(){
		return postCloseTiming;
	}

	public String getSec1OutTiming(){
		return sec1OutTiming;
	}

	public String getFreePeriod(){
		return freePeriod;
	}

	public String getInTiming(){
		return inTiming;
	}

	public String getWhereDidTheRegistrationComeFrom(){
		return whereDidTheRegistrationComeFrom;
	}

	public String getSchemeType(){
		return schemeType;
	}

	public String getEnableSaveFeedbackEmployee(){
		return enableSaveFeedbackEmployee;
	}

	public String getSec2PostCloseTiming(){
		return sec2PostCloseTiming;
	}

	public String getSchemeId(){
		return schemeId;
	}

	public String getId(){
		return id;
	}

	public String getLastIssuedNo(){
		return lastIssuedNo;
	}

	public String getEmployerId(){
		return employerId;
	}

	public String getAdharVerificationCountUsed(){
		return adharVerificationCountUsed;
	}

	public String getAvailableCount()
	{
		if (adharVerificationCountUsed!=null && adharVerificationCountAssign!=null){
			return String.valueOf(Integer.parseInt(adharVerificationCountAssign)-Integer.parseInt(adharVerificationCountUsed));
		}else return "0";
	}

	public String getOwnerName(){
		String[] name = ownerName.split(" ");
		if (name.length>1)
		{
			if (name[0].equals("Mr")||name[0].equals("Ms")||name[0].equals("Mrs")){
				StringBuilder fullName = new StringBuilder();
				for (int i = 1; i < name.length; i++) {
					fullName.append(name[i]);
					if (i!=name.length-1)
						fullName.append(" ");
				}
				return fullName.toString();
			}
		}
		return ownerName;
	}

	public String getPerVerificationText(){
		return "Rs."+amountPerApiVerification+" /- per verifications";
	}

	public String getPreStartTiming(){
		return preStartTiming;
	}

	public String getDeviceId(){
		return deviceId;
	}

	public String getSec2OutTiming(){
		return sec2OutTiming;
	}

	public String getSec1PreStartTiming(){
		return sec1PreStartTiming;
	}

	public String getSec2InTiming(){
		return sec2InTiming;
	}

	public String getWifiNames(){
		return wifiNames;
	}

	public String getAdharVerificationFeedback(){
		return adharVerificationFeedback;
	}

	public String getNoOfEmp(){
		return noOfEmp;
	}

	public String getCurrentQr(){
		return currentQr;
	}

	public String getEmployeeNumberType(){
		return employeeNumberType;
	}

	public String getOwnerEmailId(){
		return ownerEmailId;
	}

	public String getCreatedOn(){
		return createdOn;
	}

	public String getDevicePassword(){
		return devicePassword;
	}

	public String getOptionalAddress(){
		return optionalAddress;
	}

	public String getCompanyName(){
		return companyName;
	}

	public String getSec3PostCloseTiming(){
		return sec3PostCloseTiming;
	}

	public String getEnableSalaryLeaveManagement(){
		return enableSalaryLeaveManagement;
	}

	public String getStatus(){
		return status;
	}

	public String getWantAdharVerification(){
		return wantAdharVerification;
	}

	public String getEmployeeNumberFormat(){
		return employeeNumberFormat;
	}

	public String getSec1InTiming(){
		return sec1InTiming;
	}

	public String getGpsMethod(){
		return gpsMethod;
	}

	public String getAdharVerificationCountAssign(){
		if (adharVerificationCountAssign==null)
			return "0";
		else
			return adharVerificationCountAssign;
	}

	public String getWeekOff(){
		return weekOff;
	}

	public String getSchemeExpireOn(){
		return schemeExpireOn;
	}

	public String getAdditionalAdministrators(){
		return additionalAdministrators;
	}

	public String getBusinessType(){
		return businessType;
	}

	public String getSec3OutTiming(){
		return sec3OutTiming;
	}

	public String getOutTiming(){
		return outTiming;
	}

	public String getAttendanceType(){
		return attendanceType;
	}

	public String getModifiedOn(){
		return modifiedOn;
	}

	public String getShiftCount(){
		return shiftCount;
	}

	public String getOwnerAge(){
		return ownerAge;
	}

	public String getPreviousQr(){
		return previousQr;
	}

	public String getOfficeGpsPlotting(){
		return officeGpsPlotting;
	}

	public String getOwnerGender(){
		return ownerGender;
	}

	public int getAmountPerApiVerification(){
		return amountPerApiVerification;
	}

	public String getOfficeAddress(){
		return officeAddress;
	}

	public String getEnableEmployeeActivityMessage(){
		return enableEmployeeActivityMessage;
	}

	public String getSec2PreStartTiming(){
		return sec2PreStartTiming;
	}

	public String getOwnerContact(){
		return ownerContact;
	}

	public String getGeoPermittedDistance(){
		return geoPermittedDistance;
	}

	public String getOfficeGpsLocation(){
		return officeGpsLocation;
	}
}