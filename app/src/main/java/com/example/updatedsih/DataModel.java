package com.example.updatedsih;

public class DataModel {
    private String Assets;
    private double Capital_invested;
    private String District;
    private String HolderUserid;
    private String Scheme;
    private String State;
    private String Village;
    private String No_of_assets;
    private double Longitude;
    private double Latitude;

    public DataModel() {
        // empty constuctor for firestore
    }
    public DataModel(String scheme, String state, String district, String village, String assets, String holderUserid, double capital_invested, String no_of_assets, double longitude, double latitude) {
        Scheme = scheme;
        State = state;
        District = district;
        Village = village;
        Assets = assets;
        HolderUserid = holderUserid;
        Capital_invested = capital_invested;
        No_of_assets = no_of_assets;
        Longitude = longitude;
        Latitude = latitude;
    }

    public String getScheme() {
        return Scheme;
    }

    public void setScheme(String scheme) {
        Scheme = scheme;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getVillage() {
        return Village;
    }

    public void setVillage(String village) {
        Village = village;
    }

    public String getAssets() {
        return Assets;
    }

    public void setAssets(String assets) {
        Assets = assets;
    }

    public String getHolderUserid() {
        return HolderUserid;
    }

    public void setHolderUserid(String holderUserid) {
        HolderUserid = holderUserid;
    }

    public double getCapital_invested() {
        return Capital_invested;
    }

    public void setCapital_invested(double capital_invested) {
        Capital_invested = capital_invested;
    }

    public String getNo_of_assets() {
        return No_of_assets;
    }

    public void setNo_of_assets(String no_of_assets) {
        No_of_assets = no_of_assets;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }
}
