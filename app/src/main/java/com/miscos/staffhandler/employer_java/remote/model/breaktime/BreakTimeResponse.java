package com.miscos.staffhandler.employer_java.remote.model.breaktime;

import com.google.gson.annotations.SerializedName;

public class BreakTimeResponse{

	@SerializedName("data")
	private Data data;

	@SerializedName("error_code")
	private int errorCode;

	@SerializedName("message")
	private String message;

	public Data getData(){
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
			"BreakTimeResponse{" + 
			"data = '" + data + '\'' + 
			",error_code = '" + errorCode + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}