package com.henho.banmuonhenho.Model;

public class Model_noidungtinnhan {
    String nguoigui;
    String noidung;
    String thoigiangui;

    public Model_noidungtinnhan() {
    }

    public Model_noidungtinnhan(String nguoigui, String noidung, String thoigiangui) {
        this.nguoigui = nguoigui;
        this.noidung = noidung;
        this.thoigiangui = thoigiangui;
    }

    public String getNguoigui() {
        return nguoigui;
    }

    public void setNguoigui(String nguoigui) {
        this.nguoigui = nguoigui;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getThoigiangui() {
        return thoigiangui;
    }

    public void setThoigiangui(String thoigiangui) {
        this.thoigiangui = thoigiangui;
    }
}

