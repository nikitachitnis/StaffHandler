package com.miscos.staffhandler.employer_java.remote.model.fetchdataform2;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class FetchResponseForm2{

	@SerializedName("holiday_data")
	private List<HolidayData> holidayData;

	@SerializedName("employee_data")
	private List<EmployeeDataItem> employeeData;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public List<HolidayData> getHolidayData(){
		return holidayData;
	}

	public List<EmployeeDataItem> getEmployeeData(){
		return employeeData;
	}

	public String getMessage(){
		return message;
	}

	public int getStatus(){
		return status;
	}
}