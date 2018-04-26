package com.arifinfrds.codechallangeutsarifin.model;

/**
 * Created by arifinfrds on 4/25/18.
 */

public class DosenMataKuliah {

    private String idBaris;
    private String idDosen;
    private String idMataKuliah;

    public DosenMataKuliah() {

    }

    public DosenMataKuliah(String idBaris, String idDosen, String idMataKuliah) {
        this.idBaris = idBaris;
        this.idDosen = idDosen;
        this.idMataKuliah = idMataKuliah;
    }

    public String getIdBaris() {
        return idBaris;
    }

    public void setIdBaris(String idBaris) {
        this.idBaris = idBaris;
    }

    public String getIdDosen() {
        return idDosen;
    }

    public void setIdDosen(String idDosen) {
        this.idDosen = idDosen;
    }

    public String getIdMataKuliah() {
        return idMataKuliah;
    }

    public void setIdMataKuliah(String idMataKuliah) {
        this.idMataKuliah = idMataKuliah;
    }
}
