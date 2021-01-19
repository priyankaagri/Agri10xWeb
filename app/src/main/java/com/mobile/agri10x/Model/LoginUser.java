package com.mobile.agri10x.Model;

import java.io.Serializable;

public class LoginUser implements Serializable {
    String user;
    String pwd;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
