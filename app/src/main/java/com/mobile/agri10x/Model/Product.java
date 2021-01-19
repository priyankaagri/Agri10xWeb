package com.mobile.agri10x.Model;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Product {

    private Product_id _id;//_id
    private int BidCount;//BidCount
    private int MaxBid;//MaxBid
    private int MinBid;//MinBid
    private int AvgBid;//AvgBid


    public Product_id get_id() {
        return _id;
    }

    public void set_id(Product_id _id) {
        this._id = _id;
    }

    public int getBidCount() {
        return BidCount;
    }

    public void setBidCount(int bidCount) {
        BidCount = bidCount;
    }

    public int getMaxBid() {
        return MaxBid;
    }

    public void setMaxBid(int maxBid) {
        MaxBid = maxBid;
    }

    public int getMinBid() {
        return MinBid;
    }

    public void setMinBid(int minBid) {
        MinBid = minBid;
    }

    public int getAvgBid() {
        return AvgBid;
    }

    public void setAvgBid(int avgBid) {
        AvgBid = avgBid;
    }

    public Product(Product_id _id, int bidCount, int maxBid, int minBid, int avgBid) {
        this._id = _id;
        this.BidCount = bidCount;
        this.MaxBid = maxBid;
        this.MinBid = minBid;
        this.AvgBid = avgBid;
    }

    public static Product[] extractFeatureFromJson(String JSON) {
        Product[] p;
        if (TextUtils.isEmpty(JSON)) {
            return null;
        }
        try {
            JSONArray array = new JSONArray(JSON);
            p = new Product[array.length()];
            for (int i = 0; i < array.length(); i++) {
                JSONObject properties = array.getJSONObject(i);
                p[i]=new Gson().fromJson(properties.toString(),Product.class);
            }
            return p;
        } catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }
// [
//      {
//      "_id":
//              {"Status":"Accepted",
//               "Commodity":"RAISINS",
//               "SellingPrice":1750,
//               "SellOrderQuantity":1000,
//              "Quality":"GRADE AAA",
//               "Unit":10},
//      "BidCount":1,
//      "MaxBid":1777,
//      "MinBid":1777,
//      "AvgBid":1777
//      }
// ]


}
