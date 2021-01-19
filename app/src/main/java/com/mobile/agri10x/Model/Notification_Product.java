package com.mobile.agri10x.Model;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Notification_Product implements Serializable {

    private final String UserRef; //UserRef
    private final String role;   //role
    private final String commodityname; //commodityname
    private final int quantity; //quantity
    private final int unit;    //unit
    private String _id; //_id
    private boolean interested = false;
    private final String quality;  //quality
    private String ValidFrom;  //ValidFrom
    private String ValidTill;  //ValidTill


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }



    public void setInterested(boolean interested) {
        this.interested = interested;
    }


    /*
        {
        "_id" : ObjectId("5d1e452da68e3e18542b9b5e"),
        "role" : "Farmer",
        "quantity" : 20,
        "unit" : 5,
        "interested" : true,
        "quality" : "req.quality",
        "UserRef" : ObjectId("5d1e452da68e3e18542b9b5d"),
        "commodityname" : "req.body.commodityname",
        "ValidFrom" : ISODate("2019-07-04T18:27:57.287Z"),
        "ValidTill" : ISODate("2019-07-04T18:27:57.287Z"),
        "__v" : 0
    }
    */

    public Notification_Product(String UserRef, String role, String commodityname, int quantity, int unit,  String quality, String validFrom, String validTill,String _id) {
        this.UserRef = UserRef;
        this.role = role;
        this.commodityname = commodityname;
        this.quantity = quantity;
        this.unit = unit;
        this.quality = quality;
        this.ValidFrom = validFrom;
        this.ValidTill = validTill;
        this._id = _id;
    }

    public String getUserRef() {
        return UserRef;
    }

    public String getRole() {
        return role;
    }

    public String getCommodityname() {
        return commodityname;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getUnit() {
        return unit;
    }

    public boolean isInterested() {
        return interested;
    }

    public String getQuality() {
        return quality;
    }

    public String getValidFrom() {
        return ValidFrom;
    }

    public void setValidFrom(String str){
        this.ValidFrom= str;
    }

    public String getValidTill() {
        return ValidTill;
    }

    public void setValidTill(String str){
        this.ValidTill= str;
    }

    public static Notification_Product[] extractFromJson(String str) {
        Notification_Product[] p = new Notification_Product[0];
        try {
            JSONArray jsonArray = new JSONArray(str);
            int n = jsonArray.length();
            p  = new Notification_Product[n];
            for (int i = 0; i < n; i++) {
                p[i] = new Gson().fromJson(jsonArray.get(i).toString(),Notification_Product.class);
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                SimpleDateFormat outputFormat= new SimpleDateFormat("dd/MM/yyyy");
                try{
                    if(p[i].getValidFrom()!=null)
                    p[i].setValidFrom(outputFormat.format(inputFormat.parse(p[i].getValidFrom())));
                    if(p[i].getValidTill()!=null)
                    p[i].setValidTill(outputFormat.format(inputFormat.parse(p[i].getValidTill())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
        }
        return p;
    }



}
