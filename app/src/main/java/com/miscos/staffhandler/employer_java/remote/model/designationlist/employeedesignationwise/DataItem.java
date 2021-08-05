package com.miscos.staffhandler.employer_java.remote.model.designationlist.employeedesignationwise;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataItem{

	@SerializedName("having_smart_phone_also_on_app")
	private String havingSmartPhoneAlsoOnApp;

	@SerializedName("designation_name")
	private String designationName;

	@SerializedName("total_no_of_employee")
	private String totalNoOfEmployee;

	@SerializedName("department_id")
	private String departmentId;

	@SerializedName("designation_id")
	private String designationId;

	@SerializedName("not_having_smartphone")
	private String notHavingSmartphone;

	@SerializedName("employee_data")
	private List<EmployeeData> employeeData;

	@SerializedName("having_smart_phone_but_not_on_app")
	private String havingSmartPhoneButNotOnApp;

	public String getHavingSmartPhoneAlsoOnApp(){
		return havingSmartPhoneAlsoOnApp;
	}

	public String getDesignationName(){
		return designationName;
	}

	public String getTotalNoOfEmployee(){
		return totalNoOfEmployee;
	}

	public String getDepartmentId(){
		return departmentId;
	}

	public String getDesignationId(){
		return designationId;
	}

	public String getNotHavingSmartphone(){
		return notHavingSmartphone;
	}

	public List<EmployeeData> getEmployeeData(){
		return employeeData;
	}

	public String getHavingSmartPhoneButNotOnApp(){
		return havingSmartPhoneButNotOnApp;
	}

	public String getActionText(){
		if (employeeData!=null && employeeData.size()>0){
			return "List";
		} else return "S";
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"having_smart_phone_also_on_app = '" + havingSmartPhoneAlsoOnApp + '\'' + 
			",designation_name = '" + designationName + '\'' + 
			",total_no_of_employee = '" + totalNoOfEmployee + '\'' + 
			",department_id = '" + departmentId + '\'' + 
			",designation_id = '" + designationId + '\'' + 
			",not_having_smartphone = '" + notHavingSmartphone + '\'' + 
			",employee_data = '" + employeeData + '\'' + 
			",having_smart_phone_but_not_on_app = '" + havingSmartPhoneButNotOnApp + '\'' + 
			"}";
		}
}