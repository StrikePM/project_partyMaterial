package com.komputer.kit.partymaterialsforusers;

public class ClassKategori {

    private String idkelompok, kelompok;

    public ClassKategori(String idkelompok, String kelompok) {
        this.idkelompok = idkelompok;
        this.kelompok = kelompok;
    }

    public String getIdkelompok() {
        return idkelompok;
    }

    public void setIdkelompok(String idkelompok) {
        this.idkelompok = idkelompok;
    }

    public String getKelompok() {
        return kelompok;
    }

    public void setKelompok(String kelompok) {
        this.kelompok = kelompok;
    }
}
