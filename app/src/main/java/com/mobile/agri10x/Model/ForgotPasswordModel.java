package com.mobile.agri10x.Model;

import java.io.Serializable;

public class ForgotPasswordModel implements Serializable {
    String telephone,email,password;

    public ForgotPasswordModel(String email, String telephone, String password) {
        this.telephone = telephone;
        this.email = email;
        this.password = password;
    }
}
