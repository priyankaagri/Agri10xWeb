package com.mobile.agri10x.Model;

public class Interested {

    private String StockReqRef;
    private String UserRef;
    private String role;
    private boolean MyInterest;
    private boolean interested;
    private final String Userid;


    public boolean isMyInterest() {
        return MyInterest;
    }

    public void setMyInterest(boolean myInterest) {
        MyInterest = myInterest;
    }


    public Interested(String StockReqRef, String userRef, String role, boolean interested,String userid) {
        this.StockReqRef = StockReqRef;
        UserRef = userRef;
        this.role = role;
        this.interested = interested;
        this.Userid = userid;
    }

    public String getStockReqRef() {
        return StockReqRef;
    }

    public void setStockReqRef(String stockReqRef) {
        this.StockReqRef = stockReqRef;
    }

    public String getUserRef() {
        return UserRef;
    }

    public void setUserRef(String userRef) {
        UserRef = userRef;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isInterested() {
        return interested;
    }

    public void setInterested(boolean interested) {
        this.interested = interested;
    }
}
