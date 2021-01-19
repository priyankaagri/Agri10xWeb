package com.mobile.agri10x.Model;

import java.io.Serializable;

public class LogOut implements Serializable {
    String deviceID;

    public LogOut(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
}
