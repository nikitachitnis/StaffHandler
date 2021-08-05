package com.miscos.staffhandler.employer_java.remote.model.breaktime;

import com.google.gson.annotations.SerializedName;

public class OfficeDataItem{

	@SerializedName("break_duration_in_min")
	private String breakDurationInMin;

	@SerializedName("to_time")
	private String toTime;

	@SerializedName("from_time")
	private String fromTime;

	public String getBreakDurationInMin(){
		return breakDurationInMin;
	}

	public String getToTime(){
		return toTime;
	}

	public String getFromTime(){
		return fromTime;
	}

	@Override
 	public String toString(){
		return 
			"OfficeDataItem{" + 
			"break_duration_in_min = '" + breakDurationInMin + '\'' + 
			",to_time = '" + toTime + '\'' + 
			",from_time = '" + fromTime + '\'' + 
			"}";
		}
}