package com.henho.banmuonhenho.Model;

public class Model_hengap {
    String id;
    String hengi;
    String mucdich;
    String sotien;
    String nguoitratien;
    String diadiem;
    String ngaymoi;

    public Model_hengap() {
    }

    public Model_hengap(String id, String hengi, String mucdich, String sotien, String nguoitratien, String diadiem, String ngaymoi) {
        this.id = id;
        this.hengi = hengi;
        this.mucdich = mucdich;
        this.sotien = sotien;
        this.nguoitratien = nguoitratien;
        this.diadiem = diadiem;
        this.ngaymoi = ngaymoi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHengi() {
        return hengi;
    }

    public void setHengi(String hengi) {
        this.hengi = hengi;
    }

    public String getMucdich() {
        return mucdich;
    }

    public void setMucdich(String mucdich) {
        this.mucdich = mucdich;
    }

    public String getSotien() {
        return sotien;
    }

    public void setSotien(String sotien) {
        this.sotien = sotien;
    }

    public String getNguoitratien() {
        return nguoitratien;
    }

    public void setNguoitratien(String nguoitratien) {
        this.nguoitratien = nguoitratien;
    }

    public String getDiadiem() {
        return diadiem;
    }

    public void setDiadiem(String diadiem) {
        this.diadiem = diadiem;
    }

    public String getNgaymoi() {
        return ngaymoi;
    }

    public void setNgaymoi(String ngaymoi) {
        this.ngaymoi = ngaymoi;
    }
}
