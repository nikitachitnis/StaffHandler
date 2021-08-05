package com.miscos.staffhandler.employer_java.remote.model.departmentlist;

import java.io.Serializable;
import java.util.List;

public class DepartmentListResponse implements Serializable {

    List<DepartmentResponse> departmentResponseList;

    public List<DepartmentResponse> getDepartmentResponseList() {
        return departmentResponseList;
    }
}
