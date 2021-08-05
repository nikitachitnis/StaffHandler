package com.miscos.staffhandler.employer_java.remote.model.breaktime;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("office_data")
	private List<OfficeDataItem> officeData;

	@SerializedName("employee_data")
	private List<Object> employeeData;

	public List<OfficeDataItem> getOfficeData(){
		return officeData;
	}

	public List<Object> getEmployeeData(){
		return employeeData;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"office_data = '" + officeData + '\'' + 
			",employee_data = '" + employeeData + '\'' + 
			"}";
		}
}