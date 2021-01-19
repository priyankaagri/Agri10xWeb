package com.mobile.agri10x.Model;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Raj on 06-Oct-18.
 */
public class UniqueCode {
    private String id;
    private String username;
    private String code;


    public UniqueCode(){
        this.id= null;
        this.username= null;
        this.code= null;
    }

    public UniqueCode(String id, String username, String code){
        this.id= id;
        this.username=username;
        this.code= code;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static UniqueCode extractFeatureFromJson(String earthquakeJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }
        try {
            JSONObject properties = new JSONObject(earthquakeJSON);

            // Extract out the title, number of people, and perceived strength values
            String username = properties.getString("username");
            String code = properties.getString("code");
            String id = properties.getString("id");

            return new UniqueCode(id,username,code);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
