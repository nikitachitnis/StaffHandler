package com.miscos.staffhandler.employee.helper;

import android.app.Application;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SAI on 8/28/2019.
 */

public class App extends Application
{

    public static RequestQueue requestQueue ;

    @Override
    public void onCreate()
    {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(getApplicationContext());


    }
    public static boolean isvalidMobileNo(String mobileNo)
    {
        String pattern="^(?:(?:\\\\+|0{0,2})91(\\\\s*[\\\\-]\\\\s*)?|[0]?)?[789]\\\\d{9}$";
         return  mobileNo.matches(pattern);

    }

    public static boolean isValidName(String name)
    {
        String pattern="^([A-Za-z]+ )+[A-Za-z]+$|^[A-Za-z]+$ {2,35}";
        return name.matches(pattern);

    }
    public  static byte[] getFileDataFromString(String path)
    {
        File file = new File(path);
        byte fileContent[] = new byte[(int)file.length()];


        try {
            FileInputStream fin = new FileInputStream(file);
            fin.read(fileContent);
            Log.d("SUCCESS","READ");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }
}
