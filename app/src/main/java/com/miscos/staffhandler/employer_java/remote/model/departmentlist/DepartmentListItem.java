package com.miscos.staffhandler.employer_java.remote.model.departmentlist;

import com.google.gson.annotations.SerializedName;

public class DepartmentListItem{

	@SerializedName("department_status")
	private String departmentStatus;

	@SerializedName("designation_created_on")
	private String designationCreatedOn;

	@SerializedName("department_id")
	private String departmentId;

	@SerializedName("created_on")
	private String createdOn;

	@SerializedName("department_deactivated_on")
	private String departmentDeactivatedOn;

	@SerializedName("designation_id")
	private String designationId;

	@SerializedName("department_name")
	private String departmentName;

	@SerializedName("id")
	private String id;

	@SerializedName("old_designation_id")
	private String oldDesignationId;

	@SerializedName("employer_id")
	private String employerId;

	public String getDepartmentStatus(){
		return departmentStatus;
	}

	public String getDesignationCreatedOn(){
		return designationCreatedOn;
	}

	public String getDepartmentId(){
		return departmentId;
	}

	public String getCreatedOn(){
		return createdOn;
	}

	public String getDepartmentDeactivatedOn(){
		return departmentDeactivatedOn;
	}

	public String getDesignationId(){
		return designationId;
	}

	public String getDepartmentName(){
		return departmentName;
	}

	public String getId(){
		return id;
	}

	public String getOldDesignationId(){
		return oldDesignationId;
	}

	public String getEmployerId(){
		return employerId;
	}

	@Override
 	public String toString(){
		return 
			"DepartmentListItem{" + 
			"department_status = '" + departmentStatus + '\'' + 
			",designation_created_on = '" + designationCreatedOn + '\'' + 
			",department_id = '" + departmentId + '\'' + 
			",created_on = '" + createdOn + '\'' + 
			",department_deactivated_on = '" + departmentDeactivatedOn + '\'' + 
			",designation_id = '" + designationId + '\'' + 
			",department_name = '" + departmentName + '\'' + 
			",id = '" + id + '\'' + 
			",old_designation_id = '" + oldDesignationId + '\'' + 
			",employer_id = '" + employerId + '\'' + 
			"}";
		}
}