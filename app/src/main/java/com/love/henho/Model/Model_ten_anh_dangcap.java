package com.love.henho.Model;

public class Model_ten_anh_dangcap {

    String Ten;
    String Anhdaidien;
    String Dangcap;

    public Model_ten_anh_dangcap() {
    }

    public Model_ten_anh_dangcap(String ten, String anhdaidien, String dangcap) {
        Ten = ten;
        Anhdaidien = anhdaidien;
        Dangcap = dangcap;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public String getAnhdaidien() {
        return Anhdaidien;
    }

    public void setAnhdaidien(String anhdaidien) {
        Anhdaidien = anhdaidien;
    }

    public String getDangcap() {
        return Dangcap;
    }

    public void setDangcap(String dangcap) {
        Dangcap = dangcap;
    }
}
