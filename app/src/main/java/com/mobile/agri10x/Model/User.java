package com.mobile.agri10x.Model;

import android.text.TextUtils;
import com.google.gson.Gson;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class User implements Serializable {
    private String Telephone;//Telephone
    private String Username; //Username
    private String Email;
    private String Firstname;//Firstname
    private String Lastname;// Lastname
    private String imgUrl;// imgUrl
    private String role; //role
    private String  kyc;
    private Address_data address;//address
    private String country;         //country
    private String city;            //city
    private String _id;             //_id
    private String DateCreated;     //DateCreated


    public User(){
        this.Telephone = null;
        this.role= null;
        this.Username = null;
        this.Email = null;
        this.Firstname = null;
        this.imgUrl = null;
        this.kyc = null;
        this.address = null;
        this._id = null;
    }

    public User(String Telephone, String username, String email, String Firstname, String Lastname, String imgUrl, String role, String kyc, Address_data address,String city,String country,String _id,String DateCreated)
    {
        this.Telephone = Telephone;
        this.role= role;
        this.Username = username;
        this.Email = email;
        this.Firstname = Firstname;
        this.imgUrl = imgUrl;
        this.Lastname = Lastname;
        this.kyc = kyc;
        this.address = address;
        this.city = city;
        this.country = country;
        this._id = _id;
        this.DateCreated = DateCreated;
    }

    public Address_data getAddress() {
        return address;
    }

    public void setAddress(Address_data address) {
        this.address = address;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }
    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        this.Telephone = telephone;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        this.Firstname = firstname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getKyc() {
        return kyc;
    }

    public void setKyc(String kyc) {
        this.kyc = kyc;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public static User  extractFeatureFromJson(String earthquakeJSON) {
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }
        User u = new Gson().fromJson(earthquakeJSON,User.class);
        String kyc=u.getRole();
        if(kyc.charAt(0)=='P'){
                kyc = "Pending";
        }
        else{
            kyc="Verified";
        }
        u.setKyc(kyc);
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat= new SimpleDateFormat("dd/MM/yyyy");
        try {
            if(u.getDateCreated()!=null)
                u.setDateCreated(outputFormat.format(inputFormat.parse(u.getDateCreated())));
        } catch (ParseException e){}
        return u;
    }

}
