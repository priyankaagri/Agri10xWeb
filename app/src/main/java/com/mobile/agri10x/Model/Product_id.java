package com.mobile.agri10x.Model;

import org.json.JSONObject;

import java.io.Serializable;

public class Product_id implements Serializable {
    private String Status;//Status
    private String Commodity;//Commodity
    private int SellinPrice;//SellingPrice
    private String Quality;//Quality
    private int SellOrderQuantity;//SellOrderQuantity
    private int Unit;//Unit
    private String ProductImage;//ProductImage


    public Product_id(String status, String commodity,String Quality, int sellinPrice, int sellOrderQuantity, int unit,String productImage) {
        this.Status = status;
        this.Commodity = commodity;
        this.SellinPrice = sellinPrice;
        this.SellOrderQuantity = sellOrderQuantity;
        this.Unit = unit;
        this.Quality = Quality;
        this.ProductImage = productImage;
    }


    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getQuality() {
        return Quality;
    }

    public void setQuality(String quality) {
        Quality = quality;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCommodity() {
        return Commodity;
    }

    public void setCommodity(String commodity) {
        Commodity = commodity;
    }

    public int getSellinPrice() {
        return SellinPrice;
    }

    public void setSellinPrice(int sellinPrice) {
        SellinPrice = sellinPrice;
    }

    public int getSellOrderQuantity() {
        return SellOrderQuantity;
    }

    public void setSellOrderQuantity(int sellOrderQuantity) {
        SellOrderQuantity = sellOrderQuantity;
    }

    public int getUnit() {
        return Unit;
    }

    public void setUnit(int unit) {
        Unit = unit;
    }

    static Product_id extractJSON(JSONObject obj){
        try {
            String Status = obj.getString("Status");
            String Commodity = obj.getString("Commodity");
            String Quality = obj.getString("Quality");
            int SellingPrice = obj.getInt("SellingPrice");
            int SellOrderQuantity = obj.getInt("SellOrderQuantity");//SellOrderQuantity
            int Unit =obj.getInt("Unit");

            String imageURL = null;
            try {
                imageURL = obj.getString("ProductImage");
            }catch(Exception e){

            }
            return new Product_id(Status,Commodity,Quality,SellingPrice,SellOrderQuantity,Unit,imageURL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}


//[
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