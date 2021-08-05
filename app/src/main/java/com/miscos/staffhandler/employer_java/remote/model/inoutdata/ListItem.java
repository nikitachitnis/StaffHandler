package com.miscos.staffhandler.employer_java.remote.model.inoutdata;

import com.google.gson.annotations.SerializedName;

public class ListItem{

	@SerializedName("reason")
	private String reason;

	@SerializedName("IN")
	private String iN;

	@SerializedName("OUT")
	private String oUT;

	@SerializedName("date")
	private String date;

	public String getReason(){
		return reason;
	}

	public String getIN(){
		return iN;
	}

	public String getOUT(){
		return oUT;
	}

	public String getDate(){
		return date;
	}

	@Override
 	public String toString(){
		return 
			"ListItem{" + 
			"reason = '" + reason + '\'' + 
			",iN = '" + iN + '\'' + 
			",oUT = '" + oUT + '\'' + 
			",date = '" + date + '\'' + 
			"}";
		}
}