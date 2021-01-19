package com.mobile.agri10x.Model;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class StockList {

    private int quantity;//quantity
    private int unit;//unit
    private int Blockedquantity;//Blockedquantity
    private String commodityname;//commodityname
    private boolean Perishable;//Perishable
    private boolean NeedColdStorage;//NeedColdStorage
    private String ProductImage;//ProductImage
    private String EntryTime;//EntryTime
    private String quality;//quality

    public StockList(int quantity, int unit, int blockedquantity, String commodityname, boolean perishable, boolean needColdStorage, String productImage, String entryTime, String quality) {
        this.quantity = quantity;
        this.unit = unit;
        Blockedquantity = blockedquantity;
        this.commodityname = commodityname;
        Perishable = perishable;
        NeedColdStorage = needColdStorage;
        ProductImage = productImage;
        EntryTime = entryTime;
        this.quality = quality;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public int getBlockedquantity() {
        return Blockedquantity;
    }

    public void setBlockedquantity(int blockedquantity) {
        Blockedquantity = blockedquantity;
    }

    public String getCommodityname() {
        return commodityname;
    }

    public void setCommodityname(String commodityname) {
        this.commodityname = commodityname;
    }

    public boolean isPerishable() {
        return Perishable;
    }

    public void setPerishable(boolean perishable) {
        Perishable = perishable;
    }

    public boolean isNeedColdStorage() {
        return NeedColdStorage;
    }

    public void setNeedColdStorage(boolean needColdStorage) {
        NeedColdStorage = needColdStorage;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getEntryTime() {
        return EntryTime;
    }

    public void setEntryTime(String entryTime) {
        EntryTime = entryTime;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public static StockList[] extractFromJson(String str) {
        StockList[] p = new StockList[0];
        try {
            JSONArray jsonArray = new JSONArray(str);
            int n = jsonArray.length();
            p  = new StockList[n];
            for (int i = 0; i < n; i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                p[i] = new Gson().fromJson(obj.toString(),StockList.class);
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                SimpleDateFormat outputFormat= new SimpleDateFormat("dd/MM/yyyy");
                if(p[i].getEntryTime()!=null){
                    p[i].setEntryTime( outputFormat.format(inputFormat.parse(p[i].getEntryTime())));
                }
            }
        } catch (Exception e) {

        }
        return p;
    }
}
//[
// {"_id":"5d0784bb420d3319c01ec20c","index":0,"quantity":10,"unit":10,"Perishable":false,
// "NeedColdStorage":false,
// "quality":"","Blockedquantity":0,"fee":100,"feePaid":0,"CertURL":"","
// ProductImage":"/images/Products/ALMOND_1560773819686.png","owner":"5cc6b016c39cac174c60e4ab",
// "CurrentOwner":"0x95F166131F296DC202DBCFBBE3ABDE8F94412095","commodityname":"ALMOND",
// "EntryTime":"2019-06-17T12:16:59.686Z","__v":0}