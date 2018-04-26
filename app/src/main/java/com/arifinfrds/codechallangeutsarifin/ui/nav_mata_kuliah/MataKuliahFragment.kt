package com.arifinfrds.codechallangeutsarifin.ui.nav_mata_kuliah


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.arifinfrds.codechallangeutsarifin.R
import com.arifinfrds.codechallangeutsarifin.extension.toast
import com.arifinfrds.codechallangeutsarifin.model.Dosen
import com.arifinfrds.codechallangeutsarifin.model.DosenMataKuliah
import com.arifinfrds.codechallangeutsarifin.model.MataKuliah
import com.arifinfrds.codechallangeutsarifin.ui.detail_mata_kuliah.MataKuliahDetailActivity
import com.arifinfrds.codechallangeutsarifin.ui.nav_dosen.DosenAdapter
import com.arifinfrds.codechallangeutsarifin.util.Constant
import com.arifinfrds.codechallangeutsarifin.util.Constant.CHILD_DOSEN_MK
import com.arifinfrds.codechallangeutsarifin.util.Constant.CHILD_MATA_KULIAH
import com.arifinfrds.codechallangeutsarifin.util.SpacesItemDecoration
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_mata_kuliah.*


class MataKuliahFragment : Fragment(), MataKuliahListener {

    private var adapter: MataKuliahAdapter? = null
    private var items = MataKuliah.getMataKuliahList()

    private var database: FirebaseDatabase? = null
    private var databaseRef: DatabaseReference? = null

    private var dosenMataKuliahList: ArrayList<DosenMataKuliah>? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_mata_kuliah, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFirebase()
        setupSearchEditText()
        // fetchMataKuliah()
        dosenMataKuliahList = ArrayList<DosenMataKuliah>()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager?

        adapter = MataKuliahAdapter(items, this)
        recyclerView.adapter = adapter
    }

    private fun setupSearchEditText() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (p0.toString().count() == 0 || p0.toString().equals("")) {
                    fetchMataKuliah()
                    Log.d("TAG_SEARCH", "if")
                } else {
                    val text = p0.toString()
                    fetchMataKuliah(text)
                    Log.d("TAG_SEARCH", "else")
                }
            }
        })

        if (!searchEditText.isActivated || !searchEditText.isInEditMode) {
            fetchMataKuliah()
        }
    }

    private fun setupFirebase() {
        database = FirebaseDatabase.getInstance()
        databaseRef = database?.reference
    }

    // MARK: Read Dosen
    private fun fetchMataKuliah() {
        databaseRef?.child(Constant.CHILD_MATA_KULIAH)
                ?.orderByChild(Constant.CHILD_NAME)
                ?.addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (!items.isEmpty()) {
                            items.clear()
                        }
                        for (productDataSnapshot in dataSnapshot.children) {
                            val mataKuliah = productDataSnapshot.getValue(MataKuliah::class.java)
                            items.add(mataKuliah)
                        }
                        // updateUI
                        setupRecyclerView()
                        progressBar.setVisibility(View.GONE)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        context.toast("Error: ${databaseError.message}")
                        progressBar.setVisibility(View.GONE)
                    }
                })
    }

    // MARK: Read (Search)
    private fun fetchMataKuliah(nama: String) {
        databaseRef?.child(Constant.CHILD_MATA_KULIAH)?.orderByChild(Constant.CHILD_NAME)?.equalTo(nama)?.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError?) {
                context.toast("Error: ${p0?.message}")
                progressBar.setVisibility(View.GONE)
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (!items.isEmpty()) {
                    items.clear()
                }
                for (productDataSnapshot in p0!!.children) {
                    val mataKuliah = productDataSnapshot.getValue(MataKuliah::class.java)
                    items.add(mataKuliah)
                }
                // updateUI
                setupRecyclerView()
                progressBar.setVisibility(View.GONE)
            }
        })
    }

    override fun onItemClick(id: String, nama: String) {
        navigateToMataKuliahDetailActivity(id, nama)
    }

    override fun onItemLongClick(id: String, nama: String) {
        handleDelete(id, nama)
    }


    private fun navigateToMataKuliahDetailActivity(id: String, nama: String) {
        val intent = Intent(context, MataKuliahDetailActivity::class.java)
        intent.putExtra(Constant.KEY_ID_MATA_KULIAH, id)
        intent.putExtra(Constant.KEY_NAMA_MATA_KULIAH, nama)
        startActivity(intent)
    }

    private fun handleDelete(id: String, nama: String) {
        // show alert dialog
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle("Delete ${nama}")
        alertDialog.setMessage("Apakah Anda yakin ingin menghapus ${nama}?")

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { dialog, which ->
            dialog.dismiss()
            delete(id)
        }

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { dialog, which ->
            dialog.dismiss()
        }

        alertDialog.show()
    }

    // later
    private fun delete(id: String) {
        databaseRef?.child(CHILD_MATA_KULIAH)?.child(id)?.setValue(null)?.addOnSuccessListener {
            context.toast("Sukses Menghapus matakuliah")
            setupRecyclerView()
        }?.addOnFailureListener {
            context.toast("Gagal menghapus matakuliah")
        }

//        databaseRef?.child(CHILD_DOSEN_MK)?.addValueEventListener(object : ValueEventListener {
//
//            override fun onCancelled(p0: DatabaseError?) {
//                context.toast("Error: ${p0?.message}")
//            }
//
//            override fun onDataChange(p0: DataSnapshot?) {
//                for (productDataSnapshot in p0!!.children) {
//                    val dosenMataKuliah = productDataSnapshot.getValue(DosenMataKuliah::class.java)
//                    dosenMataKuliahList?.add(dosenMataKuliah!!)
//                }
//
//                var index = 0
//                dosenMataKuliahList?.forEach { item ->
//                    item.idMataKuliah
//                    index++
//                }
//            }
//        })
    }

}// Required empty public constructor

