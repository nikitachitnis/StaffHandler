package com.miscos.staffhandler.employer_java.remote.model.inoutdata.oldinoutdata;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("date")
	private String date;

	@SerializedName("reason")
	private String reason;

	@SerializedName("in_time")
	private String inTime;

	@SerializedName("out_time")
	private String outTime;

	public String getDate(){
		return date;
	}

	public String getReason(){
		return reason;
	}

	public String getInTime(){
		return inTime;
	}

	public String getOutTime() {
		return outTime;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"date = '" + date + '\'' + 
			",reason = '" + reason + '\'' + 
			",in_time = '" + inTime + '\'' + 
			"}";
		}
}