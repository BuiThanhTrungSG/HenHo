package com.henho.banmuonhenho.Model;

public class Model_thuemuon_danhsach {
    String id;
    String canthue;

    public Model_thuemuon_danhsach() {
    }

    public Model_thuemuon_danhsach(String id, String canthue) {
        this.id = id;
        this.canthue = canthue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCanthue() {
        return canthue;
    }

    public void setCanthue(String canthue) {
        this.canthue = canthue;
    }
}
