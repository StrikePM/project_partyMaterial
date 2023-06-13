package com.komputer.kit.partymaterialsforusers;

public class ClassPengembalian {

    private String faktur, tanggal, pelanggan, nophone, barang, jumlah;

    public ClassPengembalian(String faktur, String tanggal, String pelanggan, String notlp, String barang, String jumlah){
        this.faktur = faktur;
        this.tanggal = tanggal;
        this.pelanggan = pelanggan;
        this.nophone = notlp;
        this.barang = barang;
        this.jumlah = jumlah;
    }

    public String getFaktur() {
        return faktur;
    }

    public void setFaktur(String faktur) {
        this.faktur = faktur;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(String pelanggan) {
        this.pelanggan = pelanggan;
    }

    public String getNophone() {
        return nophone;
    }

    public void setNophone(String nophone) {
        this.nophone = nophone;
    }

    public String getBarang() {
        return barang;
    }

    public void setBarang(String barang) {
        this.barang = barang;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }
}
