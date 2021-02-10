package com.mobile.agri10x.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetProfile {
    @SerializedName("address")
    @Expose
    private GetAddress address;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("bankName")
    @Expose
    private String bankName;
    @SerializedName("ifsc")
    @Expose
    private String ifsc;
    @SerializedName("accountNo")
    @Expose
    private String accountNo;
    @SerializedName("accounttype")
    @Expose
    private String accounttype;
    @SerializedName("bankcity")
    @Expose
    private String bankcity;
    @SerializedName("bankcountry")
    @Expose
    private String bankcountry;
    @SerializedName("bankswiftcode")
    @Expose
    private String bankswiftcode;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("SSOcscid")
    @Expose
    private String sSOcscid;
    @SerializedName("AddedBy")
    @Expose
    private String addedBy;
    @SerializedName("Active")
    @Expose
    private Boolean active;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Firstname")
    @Expose
    private String firstname;
    @SerializedName("Lastname")
    @Expose
    private String lastname;
    @SerializedName("Telephone")
    @Expose
    private String telephone;
    @SerializedName("DateCreated")
    @Expose
    private String dateCreated;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("imgUrl")
    @Expose
    private String imgUrl;

    public GetAddress getAddress() {
        return address;
    }

    public void setAddress(GetAddress address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype;
    }

    public String getBankcity() {
        return bankcity;
    }

    public void setBankcity(String bankcity) {
        this.bankcity = bankcity;
    }

    public String getBankcountry() {
        return bankcountry;
    }

    public void setBankcountry(String bankcountry) {
        this.bankcountry = bankcountry;
    }

    public String getBankswiftcode() {
        return bankswiftcode;
    }

    public void setBankswiftcode(String bankswiftcode) {
        this.bankswiftcode = bankswiftcode;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSSOcscid() {
        return sSOcscid;
    }

    public void setSSOcscid(String sSOcscid) {
        this.sSOcscid = sSOcscid;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


}
