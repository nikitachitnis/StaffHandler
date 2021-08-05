package com.miscos.staffhandler.shiftmanagement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.miscos.staffhandler.hrms_management.hrms.utility.AppConstant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/*Developed under Miscos
 * Developed by Karan
 * 15-09-2020
 * */
public class Api {
    private static Retrofit retrofit = null;
    public static Post getClient() {

        if (retrofit==null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstant.BASE_URL)
                    //.addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(getHttpClient())
                    .build();
        }
        //Creating object for our interface
        Post api = retrofit.create(Post.class);
        return api; // return the Post object
    }
    
    public static OkHttpClient getHttpClient()
    {
        HttpLoggingInterceptor.Level logLevel = HttpLoggingInterceptor.Level.BODY;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(logLevel);

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(1000, TimeUnit.SECONDS)
                .build();

        return okHttpClient;
    }

}
