package com.love.henho.Model;

public class Model_phongchat {
    String ten;
    String dangcap;
    String noidung;
    String linkanhdaidien;
    String id;

    public Model_phongchat() {
    }

    public Model_phongchat(String ten, String dangcap, String noidung, String linkanhdaidien, String id) {
        this.ten = ten;
        this.dangcap = dangcap;
        this.noidung = noidung;
        this.linkanhdaidien = linkanhdaidien;
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getDangcap() {
        return dangcap;
    }

    public void setDangcap(String dangcap) {
        this.dangcap = dangcap;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getLinkanhdaidien() {
        return linkanhdaidien;
    }

    public void setLinkanhdaidien(String linkanhdaidien) {
        this.linkanhdaidien = linkanhdaidien;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
