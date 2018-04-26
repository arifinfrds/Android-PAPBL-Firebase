package com.arifinfrds.codechallangeutsarifin.ui.detail_mata_kuliah

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import com.arifinfrds.codechallangeutsarifin.R
import com.arifinfrds.codechallangeutsarifin.extension.toast
import com.arifinfrds.codechallangeutsarifin.model.Dosen
import com.arifinfrds.codechallangeutsarifin.model.DosenMataKuliah
import com.arifinfrds.codechallangeutsarifin.ui.detail_dosen.DosenDetailActivity
import com.arifinfrds.codechallangeutsarifin.util.Constant
import com.arifinfrds.codechallangeutsarifin.util.Constant.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_mata_kuliah_detail.*
import kotlinx.android.synthetic.main.content_mata_kuliah_detail.*

class MataKuliahDetailActivity : AppCompatActivity() {

    private var database: FirebaseDatabase? = null
    private var databaseRef: DatabaseReference? = null

    private var dosenList: ArrayList<Dosen>? = null
    private var dosenMataKuliahList: ArrayList<DosenMataKuliah>? = null

    private var adapter: MataKuliahDetailAdapter? = null

    private var extras: Bundle? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mata_kuliah_detail)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        dosenList = java.util.ArrayList<Dosen>()
        dosenMataKuliahList = java.util.ArrayList<DosenMataKuliah>()
        setupFirebase()

        extras = intent.extras
        val idMataKuliah = extras?.getString(Constant.KEY_ID_MATA_KULIAH)
        val namaMataKuliah = extras?.getString(Constant.KEY_NAMA_MATA_KULIAH)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        actionBar?.title = namaMataKuliah

        fetchDosen(idMataKuliah!!)
    }

    private var mIdDosen: String? = null

    private fun fetchDosen(idMataKuliah: String) {
        databaseRef?.child(CHILD_DOSEN_MK)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                toast("Error: ${p0?.message}")
            }

            override fun onDataChange(p0: DataSnapshot?) {

                if (!dosenMataKuliahList!!.isEmpty()) {
                    dosenMataKuliahList?.clear()
                }
                for (productDataSnapshot in p0!!.children) {
                    val dosenMataKuliah = productDataSnapshot.getValue(DosenMataKuliah::class.java)
                    dosenMataKuliahList?.add(dosenMataKuliah!!)
                }

                // filter
                dosenMataKuliahList?.forEach { item ->

                    if (item.idMataKuliah.equals(idMataKuliah)) {
                        mIdDosen = item.idDosen
                    }
                }
                // fetch Dosen Pengajar
                fetchDosenPengajar(mIdDosen!!)
            }
        })
    }

    private fun fetchDosenPengajar(idDosen: String) {
        databaseRef?.child(CHILD_DOSEN)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                toast("Error: ${p0?.message}")
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (!dosenList!!.isEmpty()) {
                    dosenList?.clear()
                }
                for (productDataSnapshot in p0!!.children) {
                    val dosen = productDataSnapshot.getValue(Dosen::class.java)
                    if (dosen?.id.equals(idDosen)) {
                        dosenList?.add(dosen!!)
                    }
                }
                setupRecyclerView()
            }
        })
    }


    private fun setupFirebase() {
        database = FirebaseDatabase.getInstance()
        databaseRef = database?.reference
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager?

        adapter = MataKuliahDetailAdapter(dosenList!!)
        recyclerView.adapter = adapter
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
