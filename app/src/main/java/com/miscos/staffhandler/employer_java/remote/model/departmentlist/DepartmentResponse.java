package com.miscos.staffhandler.employer_java.remote.model.departmentlist;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DepartmentResponse{

	@SerializedName("department_list")
	private List<DepartmentListItem> departmentList;

	@SerializedName("error_code")
	private int errorCode;

	@SerializedName("message")
	private String message;

	public List<DepartmentListItem> getDepartmentList(){
		return departmentList;
	}

	public int getErrorCode(){
		return errorCode;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"DepartmentResponse{" + 
			"department_list = '" + departmentList + '\'' + 
			",error_code = '" + errorCode + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}