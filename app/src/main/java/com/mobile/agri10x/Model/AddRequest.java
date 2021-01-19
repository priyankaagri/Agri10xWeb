package com.mobile.agri10x.Model;

import java.io.Serializable;

public class AddRequest implements Serializable {

    String commodityname;
    String quantity;
    String unit;
    String UserRef;
    String validFrom;
    String validTill;
    String quality;
    String role;
    String Userid;
    String Organic;
    String ResidueFree;
    String User_type;
    String variety;

    public String getQuantity() {
        return quantity;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setQuality(String quality){
        this.quality = quality;
    }

    public AddRequest(String commodityname, String quantity, String unit, String UserRef, String validFrom, String validTill,String quality,String role,String userid,String organic,String residuefree,String user_type,String variety) {
        this.commodityname = commodityname;
        this.quantity = quantity;
        this.unit = unit;
        this.UserRef = UserRef;
        this.validFrom = validFrom;
        this.quality  = quality;
        this.role = role;
        this.validTill = validTill;
        this.Userid = userid;
        this.Organic = organic;
        this.ResidueFree = residuefree;
        this.User_type = user_type;
        this.variety = variety;
    }

    public String getUserRef() {
        return UserRef;
    }

    public void setUserRef(String userRef) {
        this.UserRef = userRef;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTill() {
        return validTill;
    }

    public void setValidTill(String validTill) {
        this.validTill = validTill;
    }

    public void setCommodityname(String commodityname) {
        this.commodityname = commodityname;
    }


    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}
