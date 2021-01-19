package com.mobile.agri10x.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTradeVariety {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("Variety")
    @Expose
    private String Variety;



    public GetTradeVariety(String id, String Variety) {
        this.id = id;
        this.Variety = Variety;

    }
    public String getId() {
        return id;
    }

    public String getVariety() {
        return Variety;
    }

}
