package com.mobile.agri10x.Model;

public class AddStockData {
    String Ent;
    String Comm;
    String Quan;
    String Peri;
    String Cold;
    String file;
    String unit;
    String Dura;
    String Userid;
    String Variety;
    String Commid;

    public String getUserid() {
        return Userid;
    }

    public String getCommid() {
        return Commid;
    }

    public void setCommid(String commid) {
        Commid = commid;
    }

    public String getVariety() {
        return Variety;
    }

    public void setVariety(String variety) {
        Variety = variety;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getPeri() {
        return Peri;
    }

    public String getCold() {
        return Cold;
    }

    public String getDura() {
        return Dura;
    }

    public void setDura(String dura) {
        Dura = dura;
    }

    public String getEnt() {
        return Ent;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setEnt(String ent) {
        Ent = ent;
    }

    public String getComm() {
        return Comm;
    }

    public void setComm(String comm) {
        Comm = comm;
    }

    public String getQuan() {
        return Quan;
    }

    public void setQuan(String quan) {
        Quan = quan;
    }

    public void setPeri(String peri) {
        Peri = peri;
    }

    public void setCold(String cold) {
        Cold = cold;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
