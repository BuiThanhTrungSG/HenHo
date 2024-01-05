package com.love.henho.Model;

public class Model_anh_gioitinh {
    String Anhdaidien;
    String Gioitinh;

    public Model_anh_gioitinh() {
    }

    public Model_anh_gioitinh(String anhdaidien, String gioitinh) {
        Anhdaidien = anhdaidien;
        Gioitinh = gioitinh;
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
