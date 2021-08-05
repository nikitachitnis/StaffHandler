package com.miscos.staffhandler.employer_java.remote.model.inoutdata.oldinoutdata;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OldInOutResponse{

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("error_code")
	private int errorCode;

	@SerializedName("message")
	private String message;

	public List<DataItem> getData(){
		return data;
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
			"OldInOutResponse{" + 
			"data = '" + data + '\'' + 
			",error_code = '" + errorCode + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}