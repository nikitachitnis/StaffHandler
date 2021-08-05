package com.miscos.staffhandler.hrms_management.hrms.api;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by Freeware Sys on 28-05-2016.
 */
public class ApiParent {

    private int MY_SOCKET_TIMEOUT_MS = 60000;

    protected void setRequestTimeOut(StringRequest myRequest){
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    protected void setRequestLongTimeOut(StringRequest myRequest){
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
