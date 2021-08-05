package com.miscos.staffhandler.hrms_management.hrms.api;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.google.gson.Gson;
import com.miscos.staffhandler.hrms_management.hrms.utility.Helper;
import com.miscos.staffhandler.hrms_management.hrms.utility.InternetReachability;

import java.util.Map;

/**
 * Created by gunjesh.kumar on 11-05-2018.
 */

public class ApiCallListener extends ApiParent {
    private Context activity;
    private Gson gson = new Gson();

    public ApiCallListener(Activity activity) {
        this.activity = activity;
    }

    public ApiCallListener(Context activity, boolean showProgressDialog) {
        this.activity = activity;
    }

    public interface ApiCallInterface {
        void onSuccess(String response, String apiName);

        void onFail(String errortype, String apiName);
//        public void onDataNotFound();
    }

    public void executeApiCall(final Map<String, String> parameters, final String apiUrl,
                               final String apiName, final ApiCallInterface apiCallInterface) {
        if (InternetReachability.hasConnection(activity)) {

            System.out.println("parameters : " + parameters.toString() + " apiUrl : " + apiUrl + " ApiName : " + apiName);

            RequestQueue queue = Volley.newRequestQueue(activity);
            StringRequest postRequest = new StringRequest(Request.Method.POST, apiUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response", response);

                            if (response != null) {

                                try {
                                    System.out.println(apiName + " API Response : " + response);
                                    apiCallInterface.onSuccess(response, apiName);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    apiCallInterface.onFail("responseError", apiName);
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("ERROR", "error => " + error.toString());

                            if (error instanceof NetworkError) {
                                Helper.showMessage(activity, "No Internet connection");
                            }
                            apiCallInterface.onFail("VolleyError", apiName);

                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return parameters;
                }
            };

            setRequestTimeOut(postRequest);
            postRequest.setShouldCache(false);
            queue.add(postRequest);
        } else {
            Helper.showMessage(activity, "No Internet connection");
        }

    }


    public interface GenericJSONApiCallInterface<T> {
        void onApiSuccess(GenericApiResponse<T> response, String apiName);

        void onFail(String errorMessage, String apiName);
    }


}
