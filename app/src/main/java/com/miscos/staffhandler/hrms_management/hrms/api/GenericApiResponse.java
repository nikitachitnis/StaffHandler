package com.miscos.staffhandler.hrms_management.hrms.api;


public class GenericApiResponse<T>  {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}