package com.arifinfrds.codechallangeutsarifin.model;

import java.util.ArrayList;

/**
 * Created by arifinfrds on 4/25/18.
 */

public class Dosen {

    private String id;
    private String nama;

    public Dosen() {

    }

    public Dosen(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public static ArrayList<Dosen> getDosenList() {
        ArrayList<Dosen> list = new ArrayList<>();
        list.add(new Dosen("1", "Dosen 1"));
        list.add(new Dosen("2", "Dosen 2"));
        return list;
    }
}
