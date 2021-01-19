package com.mobile.agri10x.Model.UnverifiedUserModels;

public class Information {

    private String Feature;
    private String text;

    public Information(String feature, String text) {
        Feature = feature;
        this.text = text;
    }

    public String getFeature() {
        return Feature;
    }

    public void setFeature(String feature) {
        Feature = feature;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
