package com.miscos.staffhandler.empolyer.util.helperUtils;

import android.util.Log;

import com.google.gson.Gson;


public class Logger {

    private static final int LIMIT_LOG = 200;

    public static void e(String TAG, String message) {

        if (AppConfig.DEBUG_MODE) {
            try {
                String msg = "";

                int prevIndex = 0, lastIndex;
                for (int i = 0; i < message.length() / LIMIT_LOG + 1; i++) {

                    lastIndex = ((((i + 1) * LIMIT_LOG) > message.length()) ? message.length() : (i + 1) * LIMIT_LOG);
                    msg += message.substring(prevIndex, lastIndex) + "\n";
                    prevIndex = lastIndex;
                }

                Log.e(TAG, msg);
            }catch (Exception e){
            }
        }
    }

    public static void e(Thread current, String message) {

        if (AppConfig.DEBUG_MODE) {
            try {
                StackTraceElement[] stack = current.getStackTrace();
                StackTraceElement element = stack[3];
                if (!element.isNativeMethod()) {

                    String fileName = element.getFileName();
                    if (fileName.contains("."))
                        fileName = fileName.substring(0, fileName.indexOf("."));

                    int lineNumber = element.getLineNumber();
                    String methodName = element.getMethodName();

                    Logger.e(fileName + "(" + String.valueOf(lineNumber) + ")",
                            methodName + "()" + " : " + message);
                }
            }catch (Exception e){}
        }
    }

    public static void i(String TAG, String message) {

        if (AppConfig.DEBUG_MODE) {
            try {
                String msg = "";

                int prevIndex = 0, lastIndex;
                for (int i = 0; i < message.length() / LIMIT_LOG + 1; i++) {

                    lastIndex = ((((i + 1) * LIMIT_LOG) > message.length()) ? message.length() : (i + 1) * LIMIT_LOG);
                    msg += message.substring(prevIndex, lastIndex) + "\n";
                    prevIndex = lastIndex;
                }

                Log.i(TAG, msg);
            }catch (Exception e){

            }
        }
    }

    public static void i(Thread current, String message) {

        if (AppConfig.DEBUG_MODE) {
            try {
                StackTraceElement[] stack = current.getStackTrace();
                StackTraceElement element = stack[3];
                if (!element.isNativeMethod()) {

                    String fileName = element.getFileName();
                    if (fileName.contains("."))
                        fileName = fileName.substring(0, fileName.indexOf("."));

                    int lineNumber = element.getLineNumber();
                    String methodName = element.getMethodName();

                    Logger.i(fileName + "(" + String.valueOf(lineNumber) + ")",
                            methodName + "()" + " : " + message);
                }
            }catch (Exception e){}
        }
    }

    public static void d(String TAG, String message) {

        if (AppConfig.DEBUG_MODE)
            try {
                Log.d(TAG, message);
            }catch (Exception e){

            }
    }

    public static void d(Thread current, String message) {

        if (AppConfig.DEBUG_MODE) {
            try {
                StackTraceElement[] stack = current.getStackTrace();
                StackTraceElement element = stack[3];
                if (!element.isNativeMethod()) {

                    String fileName = element.getFileName();
                    if (fileName.contains("."))
                        fileName = fileName.substring(0, fileName.indexOf("."));

                    int lineNumber = element.getLineNumber();
                    String methodName = element.getMethodName();

                    Logger.d(fileName + "(" + String.valueOf(lineNumber) + ")",
                            methodName + "()" + " : " + message);
                }
            }catch (Exception e){}
        }
    }

    public static void o(Thread current, Object message) {

        if (AppConfig.DEBUG_MODE) {
            try {
                StackTraceElement[] stack = current.getStackTrace();
                StackTraceElement element = stack[3];
                if (!element.isNativeMethod()) {

                    String fileName = element.getFileName();
                    if (fileName.contains("."))
                        fileName = fileName.substring(0, fileName.indexOf("."));

                    int lineNumber = element.getLineNumber();
                    String methodName = element.getMethodName();

                    Logger.e(fileName + "(" + String.valueOf(lineNumber) + ")",
                            methodName + "()" + " : " + new Gson().toJson(message));
                }
            }catch (Exception e){

            }

        }
    }
}
