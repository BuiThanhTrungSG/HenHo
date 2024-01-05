package com.love.henho.Model;

public class Model_nguoithich {
    String id_nguoigui;
    String loicauhon;
    Integer sovangsinhle;
    Integer ngaygui;

    public Model_nguoithich() {
    }

    public Model_nguoithich(String id_nguoigui, String loicauhon, Integer sovangsinhle, Integer ngaygui) {
        this.id_nguoigui = id_nguoigui;
        this.loicauhon = loicauhon;
        this.sovangsinhle = sovangsinhle;
        this.ngaygui = ngaygui;
    }

    public String getId_nguoigui() {
        return id_nguoigui;
    }

    public void setId_nguoigui(String id_nguoigui) {
        this.id_nguoigui = id_nguoigui;
    }

    public String getLoicauhon() {
        return loicauhon;
    }

    public void setLoicauhon(String loicauhon) {
        this.loicauhon = loicauhon;
    }

    public Integer getSovangsinhle() {
        return sovangsinhle;
    }

    public void setSovangsinhle(Integer sovangsinhle) {
        this.sovangsinhle = sovangsinhle;
    }

    public Integer getNgaygui() {
        return ngaygui;
    }

    public void setNgaygui(Integer ngaygui) {
        this.ngaygui = ngaygui;
    }
}
