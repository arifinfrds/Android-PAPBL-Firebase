package com.arifinfrds.codechallangeutsarifin.ui.nav_dosen


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.arifinfrds.codechallangeutsarifin.R
import com.arifinfrds.codechallangeutsarifin.model.Dosen
import kotlinx.android.synthetic.main.fragment_dosen.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.arifinfrds.codechallangeutsarifin.extension.toast
import com.arifinfrds.codechallangeutsarifin.ui.EditActivity
import com.arifinfrds.codechallangeutsarifin.ui.detail_dosen.DosenDetailActivity
import com.arifinfrds.codechallangeutsarifin.util.Constant.*
import com.google.firebase.database.ValueEventListener


class DosenFragment : Fragment(), DosenListener {

    private var adapter: DosenAdapter? = null
    private var items = Dosen.getDosenList()

    private var database: FirebaseDatabase? = null
    private var databaseRef: DatabaseReference? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_dosen, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFirebase()

        fab.setOnClickListener {
            generateDosen();
        }

        setupSearchEditText()
    }

    private fun setupSearchEditText() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (p0.toString().count() == 0 || p0.toString().equals("")) {
                    fetchDosen()
                    Log.d("TAG_SEARCH", "if")
                } else {
                    val text = p0.toString()
                    fetchDosen(text)
                    Log.d("TAG_SEARCH", "else")
                }
            }
        })

        if (!searchEditText.isActivated || !searchEditText.isInEditMode) {
            fetchDosen()
        }
    }

    private fun setupFirebase() {
        database = FirebaseDatabase.getInstance()
        databaseRef = database?.reference
    }

    // MARK: - Insert
    private fun generateDosen() {
        val dosenRef = database?.getReference(CHILD_DOSEN)

        for (i in 1..2) {
            val id = dosenRef?.push()?.key

            val dosen = Dosen(id, "Dosen ${i}")
            dosenRef?.child(id)?.setValue(dosen)
        }
    }

    // MARK: Read Dosen
    private fun fetchDosen() {
        databaseRef?.child(CHILD_DOSEN)?.orderByChild(CHILD_NAME)?.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!items.isEmpty()) {
                    items.clear()
                }
                for (productDataSnapshot in dataSnapshot.children) {
                    val dosen = productDataSnapshot.getValue(Dosen::class.java)
                    items.add(dosen)
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
    private fun fetchDosen(nama: String) {
        databaseRef?.child(CHILD_DOSEN)?.orderByChild(CHILD_NAME)?.equalTo(nama)?.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError?) {
                context.toast("Error: ${p0?.message}")
                progressBar.setVisibility(View.GONE)
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (!items.isEmpty()) {
                    items.clear()
                }
                for (productDataSnapshot in p0!!.children) {
                    val dosen = productDataSnapshot.getValue(Dosen::class.java)
                    items.add(dosen)
                }
                // updateUI
                setupRecyclerView()
                progressBar.setVisibility(View.GONE)
            }
        })
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager?

        adapter = DosenAdapter(items, this)
        recyclerView.adapter = adapter
    }

    // MARK: - DosenListener
    override fun onItemClick(id: String, nama: String) {
        navigateToDosenDetailActivity(id, nama)
    }

    override fun onItemLongClick(id: String) {
        navigateToEditActivity(id)
    }

    private fun navigateToDosenDetailActivity(id: String, nama: String) {
        val intent = Intent(context, DosenDetailActivity::class.java)
        intent.putExtra(KEY_ID_DOSEN, id)
        intent.putExtra(KEY_NAMA_DOSEN, nama)
        startActivity(intent)
    }

    private fun navigateToEditActivity(id: String) {
        val intent = Intent(context, EditActivity::class.java)
        intent.putExtra(KEY_ID_DOSEN, id)
        startActivity(intent)
    }

}// Required empty public constructor
