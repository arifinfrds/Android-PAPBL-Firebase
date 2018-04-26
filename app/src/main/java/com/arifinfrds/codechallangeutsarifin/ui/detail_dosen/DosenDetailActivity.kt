package com.arifinfrds.codechallangeutsarifin.ui.detail_dosen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import com.arifinfrds.codechallangeutsarifin.R
import com.arifinfrds.codechallangeutsarifin.extension.toast
import com.arifinfrds.codechallangeutsarifin.model.DosenMataKuliah
import com.arifinfrds.codechallangeutsarifin.model.MataKuliah
import com.arifinfrds.codechallangeutsarifin.util.Constant.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_dosen_detail.*
import kotlinx.android.synthetic.main.fragment_dosen.*

class DosenDetailActivity : AppCompatActivity() {

    private var items: ArrayList<DosenMataKuliah>? = null
    private var extras: Bundle? = null

    private var database: FirebaseDatabase? = null
    private var databaseRef: DatabaseReference? = null

    private var adapter: DosenDetailAdapter? = null

    private var mataKuliahList: ArrayList<MataKuliah>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dosen_detail)
        setSupportActionBar(toolbar)

        items = ArrayList<DosenMataKuliah>()
        mataKuliahList = ArrayList<MataKuliah>()
        setupFirebase()

        extras = intent.extras
        val idDosen = extras?.getString(KEY_ID_DOSEN)
        val namaDosen = extras?.getString(KEY_NAMA_DOSEN)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        actionBar?.title = namaDosen

        fetchMataKuliahList(idDosen!!)
    }

    private fun setupFirebase() {
        database = FirebaseDatabase.getInstance()
        databaseRef = database?.reference
    }


    private fun fetchMataKuliahList(idDosen: String) {
        databaseRef?.child(CHILD_DOSEN_MK)?.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError?) {
                toast("Error: ${p0!!.message}")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                if (!items!!.isEmpty()) {
                    items?.clear()
                }
                for (childDataSnapshot in dataSnapshot!!.children) {
                    val dosenMataKuliah = childDataSnapshot.getValue(DosenMataKuliah::class.java)

                    if (dosenMataKuliah?.idDosen.equals(idDosen)) {
                        items?.add(dosenMataKuliah!!)
                    }
                    Log.d(TAG_DOSEN_DETAIL, dosenMataKuliah?.idMataKuliah)
                }

                items?.forEach { item ->
                    fetchMataKuliah(item.idMataKuliah)
                }

            }
        })
    }

    private fun fetchMataKuliah(id: String) {
        databaseRef?.child(CHILD_MATA_KULIAH)?.child(id)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                toast("Error: ${p0!!.message}")
            }

            override fun onDataChange(p0: DataSnapshot?) {
                val mataKuliah = p0?.getValue(MataKuliah::class.java)
                Log.d(TAG_DOSEN_DETAIL, mataKuliah?.nama)
                mataKuliahList?.add(mataKuliah!!)
                setupRecyclerView()
            }
        })
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager?

        adapter = DosenDetailAdapter(mataKuliahList!!)
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
