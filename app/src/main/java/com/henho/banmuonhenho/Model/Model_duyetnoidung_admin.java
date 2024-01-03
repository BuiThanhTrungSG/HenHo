package com.henho.banmuonhenho.Model;

public class Model_duyetnoidung_admin {
    String ten;
    Integer ngaysua;
    String noidung;
    String id;

    public Model_duyetnoidung_admin() {
    }

    public Model_duyetnoidung_admin(String ten, Integer ngaysua, String noidung, String id) {
        this.ten = ten;
        this.ngaysua = ngaysua;
        this.noidung = noidung;
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public Integer getNgaysua() {
        return ngaysua;
    }

    public void setNgaysua(Integer ngaysua) {
        this.ngaysua = ngaysua;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
