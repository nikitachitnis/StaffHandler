package com.miscos.staffhandler.employer_java.remote.model.designationlist;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DesignationResponse{

	@SerializedName("error_code")
	private int errorCode;

	@SerializedName("message")
	private String message;

	@SerializedName("list")
	private List<ListItem> list;

	public int getErrorCode(){
		return errorCode;
	}

	public String getMessage(){
		return message;
	}

	public List<ListItem> getList(){
		return list;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setList(List<ListItem> list) {
		this.list = list;
	}

	@Override
 	public String toString(){
		return 
			"DesignationResponse{" + 
			"error_code = '" + errorCode + '\'' + 
			",message = '" + message + '\'' + 
			",list = '" + list + '\'' + 
			"}";
		}
}