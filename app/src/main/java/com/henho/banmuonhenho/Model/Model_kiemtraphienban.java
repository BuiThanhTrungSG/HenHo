package com.henho.banmuonhenho.Model;

public class Model_kiemtraphienban {
    private Integer phienbanchan;
    private Integer phienbankhuyenkhich;

    public Model_kiemtraphienban() {
    }

    public Model_kiemtraphienban(Integer phienbanchan, Integer phienbankhuyenkhich) {
        this.phienbanchan = phienbanchan;
        this.phienbankhuyenkhich = phienbankhuyenkhich;
    }

    public Integer getPhienbanchan() {
        return phienbanchan;
    }

    public void setPhienbanchan(Integer phienbanchan) {
        this.phienbanchan = phienbanchan;
    }

    public Integer getPhienbankhuyenkhich() {
        return phienbankhuyenkhich;
    }

    public void setPhienbankhuyenkhich(Integer phienbankhuyenkhich) {
        this.phienbankhuyenkhich = phienbankhuyenkhich;
    }
}


