package com.mobile.agri10x.Model;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.SessionManagment.SessionManager;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Main {

    private static String indian_currency ="₹";
    private static String ip="https://data.agri10x.com/m";
    private static String oldUrl="https://data.agri10x.com/m";
    private static String negiUrl="https://data.agri10x.com/m";
    private static final String baseUrl="https://data.agri10x.com";


    private static final Set<String> errorSet = new HashSet<>();

    public static void addErrorReportRequest(final ErrorLog error, final Context context){
         String key  = error.getUniqueVal(error);
        if(!errorSet.contains(key)){
            errorSet.add(key);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Date date = new Date(System.currentTimeMillis());
                        error.setTime(Long.toString(date.getTime()));
                        Log.e("logError",new Gson().toJson(error));
                        POSTRequest.fetchUserData(Main.getNegiUrl()+"/logError",new Gson().toJson(error),context);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
    }

    public static String getNegiUrl() {
        return negiUrl;
    }

    public static void setNegiUrl(String negiUrl) {
        Main.negiUrl = negiUrl;
    }

    public static String getOldUrl() {
        return oldUrl;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static void setOldUrl(String oldUrl) {
        Main.oldUrl = oldUrl;
    }

    public static String getIndian_currency() {
        return indian_currency;
    }

    public static void setIndian_currency(String indian_currency) {
        Main.indian_currency = indian_currency;
    }

    public static String getIp(){
        boolean b = SessionManager.isLoggedIn();
        if(SessionManager.isLoggedIn())
            ip = SessionManager.getIP();
        return ip;
    }
    public static void setIp(String str){
        ip = str;
        if(SessionManager.isLoggedIn())
            SessionManager.setIP(ip);
    }

    //get the UUID
    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }
}