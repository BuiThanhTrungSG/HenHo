package com.henho.banmuonhenho.Model;

public class Model_timquanhday {
    String id;
    String khoangcach;

    public Model_timquanhday() {
    }

    public Model_timquanhday(String id, String khoangcach) {
        this.id = id;
        this.khoangcach = khoangcach;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKhoangcach() {
        return khoangcach;
    }

    public void setKhoangcach(String khoangcach) {
        this.khoangcach = khoangcach;
    }
}
