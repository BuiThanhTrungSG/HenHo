package com.henho.banmuonhenho.Model;

public class Model_thanhvienmoidangnhap {
    String ID;
    String Ten;
    Integer Namsinh;
    String Gioitinh;
    String Gioithieubanthan;
    String Mucdichthamgia;
    String Nghenghiep;
    Integer Ngaydangxuat;
    String Anhdaidien;
    String Dangcap;

    public Model_thanhvienmoidangnhap() {
    }

    public Model_thanhvienmoidangnhap(String ID, String ten, Integer namsinh, String gioitinh, String gioithieubanthan, String mucdichthamgia, String nghenghiep, Integer ngaydangxuat, String anhdaidien, String dangcap) {
        this.ID = ID;
        Ten = ten;
        Namsinh = namsinh;
        Gioitinh = gioitinh;
        Gioithieubanthan = gioithieubanthan;
        Mucdichthamgia = mucdichthamgia;
        Nghenghiep = nghenghiep;
        Ngaydangxuat = ngaydangxuat;
        Anhdaidien = anhdaidien;
        Dangcap = dangcap;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public Integer getNamsinh() {
        return Namsinh;
    }

    public void setNamsinh(Integer namsinh) {
        Namsinh = namsinh;
    }

    public String getGioitinh() {
        return Gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        Gioitinh = gioitinh;
    }

    public String getGioithieubanthan() {
        return Gioithieubanthan;
    }

    public void setGioithieubanthan(String gioithieubanthan) {
        Gioithieubanthan = gioithieubanthan;
    }

    public String getMucdichthamgia() {
        return Mucdichthamgia;
    }

    public void setMucdichthamgia(String mucdichthamgia) {
        Mucdichthamgia = mucdichthamgia;
    }

    public String getNghenghiep() {
        return Nghenghiep;
    }

    public void setNghenghiep(String nghenghiep) {
        Nghenghiep = nghenghiep;
    }

    public Integer getNgaydangxuat() {
        return Ngaydangxuat;
    }

    public void setNgaydangxuat(Integer ngaydangxuat) {
        Ngaydangxuat = ngaydangxuat;
    }

    public String getAnhdaidien() {
        return Anhdaidien;
    }

    public void setAnhdaidien(String anhdaidien) {
        Anhdaidien = anhdaidien;
    }

    public String getDangcap() {
        return Dangcap;
    }

    public void setDangcap(String dangcap) {
        Dangcap = dangcap;
    }
}
