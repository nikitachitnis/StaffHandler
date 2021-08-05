package com.miscos.staffhandler.employer_java.remote;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.miscos.staffhandler.empolyer.util.helperUtils.AppConfig;
import com.miscos.staffhandler.empolyer.util.helperUtils.Logger;
import com.miscos.staffhandler.empolyer.util.helperUtils.TLSSocketFactory;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
    private static final String BASE_URL = AppConfig.BASE_URL;
    private Retrofit mRetrofit;

    private NetworkService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .writeTimeout(30, TimeUnit.SECONDS);
        try {
            TLSSocketFactory tlsSocketFactory = new TLSSocketFactory();
            if (tlsSocketFactory.getTrustManager() != null) {
                Logger.e(Thread.currentThread(),"setting ssl factory");
                builder.sslSocketFactory(tlsSocketFactory, tlsSocketFactory.getTrustManager());
            }
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        if (AppConfig.DEBUG_MODE) {
            builder.addNetworkInterceptor(new StethoInterceptor());
        }
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())

                .build();
    }

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public APIInterface api() {
        return mRetrofit.create(APIInterface.class);
    }
}
