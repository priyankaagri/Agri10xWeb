package com.mobile.agri10x.Model;

public class Stock_data {
    String Stock_name ;
    String Stock_image;
    int Base_price;
    int Bid_count;

    public int getBase_price() {
        return Base_price;
    }

    public int  getBid_count() {
        return Bid_count;
    }

    public void setBid_count(int bid_count) {
        Bid_count = bid_count;
    }

    public void setBase_price(int base_price) {
        Base_price = base_price;
    }

    public String getStock_name() {
        return Stock_name;
    }

    public void setStock_name(String stock_name) {
        Stock_name = stock_name;
    }

    public String getStock_image() {
        return Stock_image;
    }

    public void setStock_image(String stock_image) {
        Stock_image = stock_image;
    }
}
