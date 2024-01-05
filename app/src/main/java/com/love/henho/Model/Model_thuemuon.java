package com.love.henho.Model;

public class Model_thuemuon {
    String id;
    String canthue;
    String de;
    String tai;
    String gia;
    String yeucau;
    String ngay;

    public Model_thuemuon() {
    }

    public Model_thuemuon(String id, String canthue, String de, String tai, String gia, String yeucau, String ngay) {
        this.id = id;
        this.canthue = canthue;
        this.de = de;
        this.tai = tai;
        this.gia = gia;
        this.yeucau = yeucau;
        this.ngay = ngay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCanthue() {
        return canthue;
    }

    public void setCanthue(String canthue) {
        this.canthue = canthue;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getTai() {
        return tai;
    }

    public void setTai(String tai) {
        this.tai = tai;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getYeucau() {
        return yeucau;
    }

    public void setYeucau(String yeucau) {
        this.yeucau = yeucau;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }
}
