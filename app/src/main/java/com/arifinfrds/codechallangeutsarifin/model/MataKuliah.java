package com.arifinfrds.codechallangeutsarifin.model;

import java.util.ArrayList;

/**
 * Created by arifinfrds on 4/25/18.
 */

public class MataKuliah {

    private String id;
    private String nama;

    public MataKuliah() {

    }

    public MataKuliah(String id, String nama) {
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

    public static ArrayList<MataKuliah> getMataKuliahList() {
        ArrayList<MataKuliah> list = new ArrayList<>();
        list.add(new MataKuliah("1", "MK 1"));
        list.add(new MataKuliah("2", "MK 2"));
        return list;
    }
}
