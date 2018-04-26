package com.arifinfrds.codechallangeutsarifin.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.arifinfrds.codechallangeutsarifin.R
import com.arifinfrds.codechallangeutsarifin.extension.toast
import com.arifinfrds.codechallangeutsarifin.model.Dosen
import com.arifinfrds.codechallangeutsarifin.util.Constant
import com.arifinfrds.codechallangeutsarifin.util.Constant.CHILD_DOSEN
import com.arifinfrds.codechallangeutsarifin.util.Constant.TAG_EDIT_DOSEN
import com.google.firebase.database.*

import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.content_edit.*

class EditActivity : AppCompatActivity() {

    private var database: FirebaseDatabase? = null
    private var databaseRef: DatabaseReference? = null

    private var extras: Bundle? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setSupportActionBar(toolbar)

        setupFirebase()

        extras = intent.extras
        val idDosen = extras?.getString(Constant.KEY_ID_DOSEN)

        fetchDosen(idDosen!!)

        saveButton.setOnClickListener {
            updateDosen(idDosen!!)
        }
    }

    private fun fetchDosen(idDosen: String) {
        databaseRef?.child(CHILD_DOSEN)?.child(idDosen)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                toast("Error: ${p0?.message}")
            }

            override fun onDataChange(p0: DataSnapshot?) {
                val dosen = p0?.getValue(Dosen::class.java)
                Log.d(TAG_EDIT_DOSEN, dosen?.nama)
                updateUI(dosen!!)
            }
        })
    }

    private fun updateUI(dosen: Dosen) {
        namaDosenEditText.setText(dosen.nama)
    }

    private fun updateDosen(idDosen: String) {
        val nama = namaDosenEditText.text.toString()

        val dosen = Dosen(idDosen, nama)

        databaseRef?.child(CHILD_DOSEN)?.child(idDosen)?.setValue(dosen)
                ?.addOnSuccessListener {
                    toast("Sukses update dosen")
                    finish()
                }
                ?.addOnFailureListener {
                    toast("Gagal update dosen")
                }
    }


    private fun setupFirebase() {
        database = FirebaseDatabase.getInstance()
        databaseRef = database?.reference
    }

}
