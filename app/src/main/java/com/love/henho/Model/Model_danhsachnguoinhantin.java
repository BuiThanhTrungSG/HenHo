package com.love.henho.Model;

public class Model_danhsachnguoinhantin {
    String idnguoichat;
    String thoidiem;
    Boolean trangthai;

    public Model_danhsachnguoinhantin() {
    }

    public Model_danhsachnguoinhantin(String idnguoichat, String thoidiem, Boolean trangthai) {
        this.idnguoichat = idnguoichat;
        this.thoidiem = thoidiem;
        this.trangthai = trangthai;
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
}
