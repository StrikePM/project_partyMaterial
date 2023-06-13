package com.komputer.kit.partymaterialsforusers;

public class ClassBarang {

    private String idjasa, idkelompok, barang, harga, kelompok;

    public ClassBarang(String idjasa, String idkelompok, String barang, String harga, String kelompok) {
        this.idjasa = idjasa;
        this.idkelompok = idkelompok;
        this.barang = barang;
        this.harga = harga;
        this.kelompok = kelompok;
    }

    public String getIdjasa() {
        return idjasa;
    }

    public void setIdjasa(String idjasa) {
        this.idjasa = idjasa;
    }

    public String getIdkelompok() {
        return idkelompok;
    }

    public void setIdkelompok(String idkelompok) {
        this.idkelompok = idkelompok;
    }

    public String getBarang() {
        return barang;
    }

    public void setBarang(String barang) {
        this.barang = barang;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getKelompok() {
        return kelompok;
    }

    public void setKelompok(String kelompok) {
        this.kelompok = kelompok;
    }
}
