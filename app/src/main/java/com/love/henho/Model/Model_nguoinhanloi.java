package com.love.henho.Model;

public class Model_nguoinhanloi {
    String id;
    String loinhan;

    public Model_nguoinhanloi() {
    }

    public Model_nguoinhanloi(String id, String loinhan) {
        this.id = id;
        this.loinhan = loinhan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoinhan() {
        return loinhan;
    }

    public void setLoinhan(String loinhan) {
        this.loinhan = loinhan;
    }
}
