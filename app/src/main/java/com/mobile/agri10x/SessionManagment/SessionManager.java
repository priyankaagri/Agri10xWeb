package com.mobile.agri10x.SessionManagment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mobile.agri10x.LoginActivity;
import com.mobile.agri10x.Model.Address_data;
import com.mobile.agri10x.Model.KYC_data;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.SecurityData;
import com.mobile.agri10x.Model.User;


public class SessionManager {
    static SharedPreferences pref;

    static SharedPreferences.Editor editor;

    private static Context _context;


    static int  PRIVATE_MODE = 0;

    public static final String PREF_NAME = "LogedInUser";
    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_FIRSTNAME = "first_names";
    public static final String KEY_Email = "email";
    public static final String KEY_ImageURI = "imageUrl";
    public static final String KEY_LASTNAME = "last_name";
    public static final String KEY_TELEPHONE = "telephone";
    public static final String KEY_ROLE = "role";
    public static final String KEY_Username = "username";
    public static final String KEY_KYC = "KYC";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_IP = "ip";
    public static final String KEY_regToken_bool = "regToken_bool";
    public static final String KEY_Entity_id = "EntitiID";

    public static String getKEY_Entity_id() {
        return pref.getString(KEY_Entity_id,"");
    }

    public static boolean getKEY_regToken_bool() {
        String data =  pref.getString(KEY_regToken_bool,null);
        return data != null && data.equals("true");
    }

    public static void setKEY_regToken_bool(boolean flag) {
        if(flag){
            editor.putString(KEY_regToken_bool,"true") ;
            editor.commit();
        }
        else {
            editor.putString(KEY_regToken_bool,"false") ;
            editor.commit();
        }
    }

    public SessionManager(Context ApplicationContext){
        SessionManager._context = ApplicationContext;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static String getKYC_data(){
        String jsondata =  pref.getString(KEY_KYC,"");
         return jsondata;
    }

    public static void setKYC_data(KYC_data kyc_data){
        String json = new Gson().toJson(kyc_data);
        editor.putString(KEY_KYC,json) ;
        editor.commit();
    }

    public static Address_data getAddress_data(){
        String jsondata =  pref.getString(KEY_ADDRESS,null);
        return new Gson().fromJson(jsondata,Address_data.class);
    }

    public static void setAddress_data(Address_data address_data){
        String json = new Gson().toJson(address_data);
        editor.putString(KEY_ADDRESS,json) ;
        editor.commit();
    }

    public static  String getLastName() {
        return pref.getString(KEY_LASTNAME,"");
    }

    public static  void setLastName(String id) {
        editor.putString(KEY_LASTNAME,id) ;
        editor.commit();
    }

    public  static String getIP() {
        return pref.getString(KEY_IP,null);
    }

    public static  void setIP(String ip) {
        editor.putString(KEY_IP,ip) ;
        editor.commit();
    }


    public static  String getFirstName() {
        return pref.getString(KEY_FIRSTNAME,"");
    }

    public static  void setFirstName(String name) {
        editor.putString(KEY_FIRSTNAME,name) ;
        editor.commit();
    }

    public static  String getEmail() {
        return pref.getString(KEY_Email,"");
    }

    public static  void setEmail(String email) {
        editor.putString(KEY_Email,email) ;
        editor.commit();
    }

    public  static String getImageURI() {
        return pref.getString(KEY_ImageURI,null);

    }

    public static   void setImageURI(String ImageURI) {
        editor.putString(KEY_ImageURI,ImageURI) ;
        editor.commit();
    }

    public static  int getTelephone() {
        return pref.getInt(KEY_TELEPHONE,0);
    }

    public static  void setTelephone(int rno) {
        editor.putInt(KEY_TELEPHONE,rno) ;
        editor.commit();
    }

    public static String getUsername() {
        return pref.getString(KEY_Username,null);
    }

    public static  void setUsername(String username) {
        editor.putString(KEY_Username,username) ;
        editor.commit();
    }

    public static  String getRole() {
        return pref.getString(KEY_ROLE,"");
    }

    public static  void setRole(String details) {
        editor.putString(KEY_ROLE,details) ;
        editor.commit();
    }


    public static void createLoginSession(User u){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_FIRSTNAME, u.getFirstname());
        if(u.getEmail()!=null) {
            editor.putString(KEY_Email, u.getEmail());
        }
        else{
            editor.putString(KEY_Email,"xyz@gmail.com");
        }
        editor.putString(KEY_LASTNAME, u.getLastname());
        editor.putString(KEY_ROLE, u.getRole());
        editor.putString(KEY_Username, u.getUsername());
        editor.putString(KEY_TELEPHONE, u.getTelephone());
        editor.putString(KEY_ImageURI, u.getImgUrl());
        String json_address = new Gson().toJson(u.getAddress());
        editor.putString(KEY_ADDRESS,json_address);
        editor.putString(KEY_KYC,u.getKyc());
        editor.putString(KEY_IP, Main.getIp());
        editor.putString(KEY_regToken_bool,"false");
        editor.putString(KEY_Entity_id,u.get_id());
        editor.commit();
    }


    public static void logoutUser() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        SecurityData.setClosingBalance(null);
        SecurityData.setWithdthrawBalance(null);
        SecurityData.setUserImage(null);
        _context.startActivity(i);

    }


    public static  boolean isLoggedIn(){
        if(pref==null)
        {
            return false;
        }
        else return  pref.getBoolean(IS_LOGIN, false);
    }

}
