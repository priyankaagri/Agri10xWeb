package com.mobile.agri10x.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SetUserO {

        @SerializedName("address")
        @Expose
        private SetAddressO address;
        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("fn")
        @Expose
        private String fn;
        @SerializedName("ln")
        @Expose
        private String ln;

        public SetAddressO getAddress() {
            return address;
        }

        public void setAddress(SetAddressO address) {
            this.address = address;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFn() {
            return fn;
        }

        public void setFn(String fn) {
            this.fn = fn;
        }

        public String getLn() {
            return ln;
        }

        public void setLn(String ln) {
            this.ln = ln;
        }

}
