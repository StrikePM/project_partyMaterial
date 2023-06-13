package com.komputer.kit.partymaterialsforusers;

public class ClassTransGetSet {

    private String idBarang, idOrderDetail, idOrder, jmlh, hrg;

    public ClassTransGetSet(String idBarang, String idOrderDetail, String idOrder, String jmlh, String hrg){

        this.idBarang = idBarang;
        this.idOrderDetail = idOrderDetail;
        this.idOrder = idOrder;
        this.jmlh = jmlh;
        this.hrg = hrg;
    }

    public String getIdBarang() {
        return idBarang;
    }

    public void setIdBarang(String idBarang) {
        this.idBarang = idBarang;
    }

    public String getIdOrderDetail() {
        return idOrderDetail;
    }

    public void setIdOrderDetail(String idOrderDetail) {
        this.idOrderDetail = idOrderDetail;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getJmlh() {
        return jmlh;
    }

    public void setJmlh(String jmlh) {
        this.jmlh = jmlh;
    }

    public String getHrg() {
        return hrg;
    }

    public void setHrg(String hrg) {
        this.hrg = hrg;
    }
}
