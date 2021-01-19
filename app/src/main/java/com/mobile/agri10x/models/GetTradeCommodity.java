package com.mobile.agri10x.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class
GetTradeCommodity {


    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("Commodity")
    @Expose
    private String Commodity;



    public GetTradeCommodity(String id, String Commodity) {
        this.id = id;
        this.Commodity = Commodity;

    }
    public String getId() {
        return id;
    }

    public String getCommodity() {
        return Commodity;
    }


}
