package com.henho.banmuonhenho.Model;

public class Model_timquanhday_thongtinnguoi {
    String Ten;
    String Anhdaidien;
    String Gioitinh;

    public Model_timquanhday_thongtinnguoi() {
    }

    public Model_timquanhday_thongtinnguoi(String ten, String anhdaidien, String gioitinh) {
        Ten = ten;
        Anhdaidien = anhdaidien;
        Gioitinh = gioitinh;
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

    public String getGioitinh() {
        return Gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        Gioitinh = gioitinh;
    }
}
