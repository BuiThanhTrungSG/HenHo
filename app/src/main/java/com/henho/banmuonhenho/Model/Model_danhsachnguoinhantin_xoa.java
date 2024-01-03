package com.henho.banmuonhenho.Model;

public class Model_danhsachnguoinhantin_xoa {
    String idnguoichat;
    String thoidiem;
    Boolean trangthai;
    int trangthaixoa;

    public Model_danhsachnguoinhantin_xoa() {
    }

    public Model_danhsachnguoinhantin_xoa(String idnguoichat, String thoidiem, Boolean trangthai, int trangthaixoa) {
        this.idnguoichat = idnguoichat;
        this.thoidiem = thoidiem;
        this.trangthai = trangthai;
        this.trangthaixoa = trangthaixoa;
    }

    public String getIdnguoichat() {
        return idnguoichat;
    }

    public void setIdnguoichat(String idnguoichat) {
        this.idnguoichat = idnguoichat;
    }

    public String getThoidiem() {
        return thoidiem;
    }

    public void setThoidiem(String thoidiem) {
        this.thoidiem = thoidiem;
    }

    public Boolean getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(Boolean trangthai) {
        this.trangthai = trangthai;
    }

    public int getTrangthaixoa() {
        return trangthaixoa;
    }

    public void setTrangthaixoa(int trangthaixoa) {
        this.trangthaixoa = trangthaixoa;
    }
}
