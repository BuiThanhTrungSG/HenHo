package com.henho.banmuonhenho.Model;

public class Model_hengap_danhsach {

    String id;
    String hengi;

    public Model_hengap_danhsach() {
    }

    public Model_hengap_danhsach(String id, String hengi) {
        this.id = id;
        this.hengi = hengi;
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
}
