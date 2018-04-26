package com.arifinfrds.codechallangeutsarifin.ui

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.arifinfrds.codechallangeutsarifin.R
import com.arifinfrds.codechallangeutsarifin.extension.toast
import com.arifinfrds.codechallangeutsarifin.model.Dosen
import com.arifinfrds.codechallangeutsarifin.model.DosenMataKuliah
import com.arifinfrds.codechallangeutsarifin.model.MataKuliah
import com.arifinfrds.codechallangeutsarifin.util.Constant.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import kotlinx.android.synthetic.main.activity_tambah_data.*
import kotlinx.android.synthetic.main.content_tambah_data.*

class TambahDataActivity : AppCompatActivity() {

    private var database: FirebaseDatabase? = null
    private var databaseRef: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_data)
        setSupportActionBar(toolbar)

        setupFirebase()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Tambah", Snackbar.LENGTH_LONG)
                    .setAction("Tambah", null).show()

            insertData();
        }
    }

    private fun setupFirebase() {
        database = FirebaseDatabase.getInstance()
        databaseRef = database?.reference
    }


    // MARK: - Insert
    private fun insertData() {
        // insert Dosen
        val idDosen = databaseRef?.push()?.key
        insertDosen(idDosen!!)

        // insert MK
        val idMataKuliah1 = databaseRef?.push()?.key
        val idMataKuliah2 = databaseRef?.push()?.key
        insertMK(idMataKuliah1!!, idMataKuliah2!!)

        // inser tabel penghubung
        insertTabelPenghubung(idDosen, idMataKuliah1, idMataKuliah2)
    }

    private fun insertDosen(id: String) {
        val nama = namaDosenEditText.text.toString()

        val dosen = Dosen(id, nama)

        databaseRef?.child(CHILD_DOSEN)?.child(id)?.setValue(dosen)
                ?.addOnSuccessListener {
                    toast("sukses input dosen")
                }?.addOnFailureListener {
                    toast("Gagal input dosen")
                }
    }

    private fun insertMK(idMataKuliah1: String, idMataKuliah2: String) {
        val mataKuliah1 = MataKuliah(idMataKuliah1, mataKuliah1EditText.text.toString())
        val mataKuliah2 = MataKuliah(idMataKuliah2, mataKuliah2EditText.text.toString())

        databaseRef?.child(CHILD_MATA_KULIAH)?.child(idMataKuliah1)?.setValue(mataKuliah1)
                ?.addOnSuccessListener {
                    toast("Sukses input Mata Kuliah 1")
                }?.addOnFailureListener {
                    toast("Gagal input Mata Kuliah 1")
                }

        databaseRef?.child(CHILD_MATA_KULIAH)?.child(idMataKuliah2)?.setValue(mataKuliah2)
                ?.addOnSuccessListener {
                    toast("Sukses input Mata Kuliah 2")
                }?.addOnFailureListener {
                    toast("Gagal input Mata Kuliah 2")
                }
    }

    private fun insertTabelPenghubung(idDosen: String, idMataKuliah1: String, idMataKuliah2: String) {
        var idBaris = databaseRef?.push()?.key
        var dosenMataKuliah = DosenMataKuliah(idBaris, idDosen, idMataKuliah1)

        databaseRef?.child(CHILD_DOSEN_MK)?.child(idBaris)?.setValue(dosenMataKuliah)
                ?.addOnSuccessListener {
                    toast("Sukses input Mata Kuliah 1 ke tabel penghubung")
                }?.addOnFailureListener {
                    toast("Gagal input Mata Kuliah 1 ke tabel penghubung")
                }

        idBaris = databaseRef?.push()?.key
        dosenMataKuliah = DosenMataKuliah(idBaris, idDosen, idMataKuliah2)

        databaseRef?.child(CHILD_DOSEN_MK)?.child(idBaris)?.setValue(dosenMataKuliah)
                ?.addOnSuccessListener {
                    toast("Sukses input Mata Kuliah 2 ke tabel penghubung")
                    finish()
                }?.addOnFailureListener {
                    toast("Gagal input Mata Kuliah 2 ke tabel penghubung")
                }
    }


}
