package com.mobile.agri10x.Model;

import java.io.Serializable;

public class Track implements Serializable {

    private final String vDate;
    private final String vLat;
    private final String vLong;
    private final String vPhone;

    public Track(String currTime, String lat, String longi, String phone) {
        this.vDate = currTime;
        this.vLat = lat;
        this.vLong = longi;
        this.vPhone = phone;
    }
}
