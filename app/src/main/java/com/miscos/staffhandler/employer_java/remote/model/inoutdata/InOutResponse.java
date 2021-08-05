package com.miscos.staffhandler.employer_java.remote.model.inoutdata;

import com.google.gson.annotations.SerializedName;

public class InOutResponse{

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
			"InOutResponse{" + 
			"data = '" + data + '\'' + 
			",error_code = '" + errorCode + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}