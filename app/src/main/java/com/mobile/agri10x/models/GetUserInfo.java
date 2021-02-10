package com.mobile.agri10x.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUserInfo {

        @SerializedName("Update")
        @Expose
        private Boolean update;

        public Boolean getUpdate() {
            return update;
        }

        public void setUpdate(Boolean update) {
            this.update = update;
        }
    }

