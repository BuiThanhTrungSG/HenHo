package com.henho.banmuonhenho.Model;

public class Model_duyetanh {
    String linkanhchoduyet;
    String idchuanh;

    public Model_duyetanh() {
    }

    public Model_duyetanh(String linkanhchoduyet, String idchuanh) {
        this.linkanhchoduyet = linkanhchoduyet;
        this.idchuanh = idchuanh;
    }

    public String getLinkanhchoduyet() {
        return linkanhchoduyet;
    }

    public void setLinkanhchoduyet(String linkanhchoduyet) {
        this.linkanhchoduyet = linkanhchoduyet;
    }

    public String getIdchuanh() {
        return idchuanh;
    }

    public void setIdchuanh(String idchuanh) {
        this.idchuanh = idchuanh;
    }
}

