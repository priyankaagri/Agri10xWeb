package com.mobile.agri10x.Model;

import java.io.Serializable;

public class KYC_data implements Serializable {
    private String Passport;
    private String VoterID;
    private String Aadhar;
    private String Other;
    private String Pancard;

    public String getPassport() {
        return Passport;
    }

    public void setPassport(String passport) {
        Passport = passport;
    }

    public String getVoterID() {
        return VoterID;
    }

    public void setVoterID(String voterID) {
        VoterID = voterID;
    }

    public String getAadhar() {
        return Aadhar;
    }

    public void setAadhar(String aadhar) {
        Aadhar = aadhar;
    }

    public String getOther() {
        return Other;
    }

    public void setOther(String other) {
        Other = other;
    }

    public String getPancard() {
        return Pancard;
    }

    public void setPancard(String pancard) {
        Pancard = pancard;
    }
}
