package com.henho.banmuonhenho.Model;

public class Model_timquanhday_upload {

    String id;
    double latitude;
    double longitude;
    String ngay;

    public Model_timquanhday_upload() {
    }

    public Model_timquanhday_upload(String id, double latitude, double longitude, String ngay) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ngay = ngay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }
}
