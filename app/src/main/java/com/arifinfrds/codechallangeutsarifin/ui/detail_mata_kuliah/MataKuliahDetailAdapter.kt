package com.arifinfrds.codechallangeutsarifin.ui.detail_mata_kuliah

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.arifinfrds.codechallangeutsarifin.R
import com.arifinfrds.codechallangeutsarifin.model.Dosen

/**
 * Created by arifinfrds on 4/25/18.
 */
class MataKuliahDetailAdapter(

        val items: ArrayList<Dosen>

) : RecyclerView.Adapter<MataKuliahDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MataKuliahDetailAdapter.ViewHolder?, position: Int) {
        var item = items[position]
        holder?.titleTextView?.text = item.nama
    }

    override fun getItemCount(): Int {
        return items.count()
    }


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        // MARK: - Public Properties
        val titleTextView: TextView
        val container_list_item: FrameLayout


        // MARK: - Initialization
        init {
            titleTextView = itemView?.findViewById(R.id.titleTextView) as TextView
            container_list_item = itemView?.findViewById(R.id.container_list_item) as FrameLayout
        }
    }
}
