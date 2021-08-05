package com.miscos.staffhandler.employer_java.remote.model.inoutdata;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("line_no")
	private String lineNo;

	@SerializedName("list")
	private List<ListItem> list;

	@SerializedName("status")
	private String status;

	public String getLineNo(){
		return lineNo;
	}

	public List<ListItem> getList(){
		return list;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"line_no = '" + lineNo + '\'' + 
			",list = '" + list + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}