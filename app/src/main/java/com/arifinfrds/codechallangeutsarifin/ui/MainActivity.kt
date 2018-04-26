package com.arifinfrds.codechallangeutsarifin.ui

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.arifinfrds.codechallangeutsarifin.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import android.support.v4.widget.DrawerLayout
import android.view.View
import com.arifinfrds.codechallangeutsarifin.model.MataKuliah
import com.arifinfrds.codechallangeutsarifin.ui.nav_dosen.DosenFragment
import com.arifinfrds.codechallangeutsarifin.ui.nav_mata_kuliah.MataKuliahFragment


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        displayView(R.id.nav_dosen)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                navigateToTambahDataActivity()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToTambahDataActivity() {
        val intent = Intent(this, TambahDataActivity::class.java)
        startActivity(intent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.nav_dosen -> {
                displayView(id)
            }
            R.id.nav_mata_kuliah -> {
                displayView(id)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun displayView(viewId: Int) {
        var fragment: Fragment? = null
        var title = getString(R.string.app_name)

        when (viewId) {
            R.id.nav_dosen -> {
                title = "Dosen"
                fragment = DosenFragment()
            }
            R.id.nav_mata_kuliah -> {
                title = "Mata Kuliah"
                fragment = MataKuliahFragment()
            }
        }

        if (fragment != null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.content_main, fragment)
            ft.commit()
        }

        // set the toolbar title
        if (supportActionBar != null) {
            supportActionBar!!.setTitle(title)
        }

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
    }
}
