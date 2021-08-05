package com.miscos.staffhandler.employer_java.remote.model.designationlist;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListItem{

	@SerializedName("designation_arr")
	private List<DesignationArrItem> designationArr;

	@SerializedName("dept_name")
	private String deptName;

	@SerializedName("dept_created_on")
	private String deptCreatedOn;

	@SerializedName("dept_id")
	private String deptId;

	@SerializedName("employer_id")
	private String employerId;

	public List<DesignationArrItem> getDesignationArr(){
		return designationArr;
	}

	public String getDeptName(){
		return deptName;
	}

	public String getDeptCreatedOn(){
		return deptCreatedOn;
	}

	public String getDeptId(){
		return deptId;
	}

	public String getEmployerId(){
		return employerId;
	}

	public void setDesignationArr(List<DesignationArrItem> designationArr) {
		this.designationArr = designationArr;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public void setDeptCreatedOn(String deptCreatedOn) {
		this.deptCreatedOn = deptCreatedOn;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public void setEmployerId(String employerId) {
		this.employerId = employerId;
	}

	@Override
 	public String toString(){
		return deptName;
		}
}