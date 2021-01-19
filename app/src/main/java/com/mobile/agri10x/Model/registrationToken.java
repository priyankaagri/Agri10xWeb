package com.mobile.agri10x.Model;

public class registrationToken {
    private String Userid;
    private String role;
    private String deviceID;
    private String registrationToken;

    public registrationToken(String userid, String role, String deviceID, String registrationToken) {
        this.Userid = userid;
        this.role = role;
        this.deviceID = deviceID;
        this.registrationToken = registrationToken;
    }

    public String getUsername() {
        return Userid;
    }

    public void setUsername(String userid) {
        this.Userid = userid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }
}
