package com.miscos.staffhandler.employer_java.remote.model.designationlist;

import com.google.gson.annotations.SerializedName;

public class DesignationArrItem{

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	public String getName(){
		return name;
	}

	public String getId(){
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
 	public String toString(){
		return name;
		}
}