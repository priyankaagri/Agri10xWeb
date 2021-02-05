package com.mobile.agri10x.Model;

import java.io.Serializable;

public class SignUpUser implements Serializable {
    String pwd;
    String email;
    String fn;
    String ln;
    String img;

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public void setLn(String ln) {
        this.ln = ln;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    String role;
    String tp;
    String wpwd;

    public SignUpUser(  String role,String tp) {//String email, String firstname, String lastname, String img,

//        this.email = email;
//        this.fn = firstname;
//        this.ln = lastname;
//        this.img = img;
        this.role = role;
        this.tp = tp;

    }
}