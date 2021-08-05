package com.miscos.staffhandler.hrms_management.hrms.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {
    public static final String mypreference = "mypref";

    private Context context;
    private SharedPreferences sharedPreferences;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
    }

    public String saveStringValue(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
        Log.e("save_value", value);
        return key;
    }


    public String getStringValue(String key)
    {
        Log.e("retrive_value",key);
        return sharedPreferences.getString(key, "");
    }

    public void saveBooleanValue(String key, boolean value) {  sharedPreferences.edit().putBoolean(key, value).apply();    }

    public boolean getBooleanValue(String key) {  return sharedPreferences.getBoolean(key, false);    }

    public void clearData(){
        sharedPreferences.edit().clear().apply();
    }

   /* public void saveUserData(LoginResponse responseModel){
        Gson gson = new Gson();
        saveStringValue(AppConstant.SM_USER_DATA,gson.toJson(responseModel));
    }

    public LoginResponse getUserData(){
        Gson gson = new Gson();
        LoginResponse responseModel=gson.fromJson(getStringValue(AppConstant.SM_USER_DATA),LoginResponse.class);
        return responseModel;
    }*/
}
