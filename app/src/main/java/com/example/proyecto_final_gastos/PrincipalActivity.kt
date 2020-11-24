package com.example.proyecto_final_gastos

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import com.example.proyecto_final_gastos.ui.detallesmes.DetallesMesFragment
import com.example.proyecto_final_gastos.ui.gallery.GalleryFragment
import com.example.proyecto_final_gastos.ui.home.HomeFragment
import com.example.proyecto_final_gastos.ui.registrogasto.RegistroGastoFragment
import com.example.proyecto_final_gastos.ui.slideshow.SlideshowFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.fragment_detalle_mes.*
import java.io.BufferedReader

class PrincipalActivity : AppCompatActivity(){

    private lateinit var auth: FirebaseAuth
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)




        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        auth = FirebaseAuth.getInstance()

        navView.setNavigationItemSelectedListener {
            var selectedFragment: Fragment = HomeFragment()
            when (it.itemId) {
                R.id.nav_home -> {
                    selectedFragment = HomeFragment()
                }
                R.id.nav_registro_gasto -> {
                    selectedFragment = RegistroGastoFragment()
                }
                R.id.nav_slideshow -> {
                    selectedFragment = SlideshowFragment()
                }
                R.id.nav_gallery -> {
                    selectedFragment = GalleryFragment()
                }
                R.id.nav_enero -> {
                    selectedFragment = DetallesMesFragment.newInstance("Enero 2020")
                }
                R.id.nav_febrero -> {
                    selectedFragment = DetallesMesFragment.newInstance("Febrero 2020")
                }
                R.id.nav_marzo -> {
                    selectedFragment = DetallesMesFragment.newInstance("Marzo 2020")
                }
                R.id.nav_abril -> {
                    selectedFragment = DetallesMesFragment.newInstance("Abril 2020")
                }
                R.id.nav_mayo -> {
                    selectedFragment = DetallesMesFragment.newInstance("Mayo 2020")
                }

            }
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, selectedFragment)
            transaction.commit()

            drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

        var nombre = leerInternoNombre()
        var dinero = leerInternoDinero()

        val firstHome = HomeFragment.newInstance(nombre,dinero)

        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment,firstHome).commit()


    }

    fun leerInternoNombre(): String{
        var cuerpo = ""
        try {
            var archivo = auth.currentUser!!.email!!

            val ofi = openFileInput("$archivo.txt")

            cuerpo = ofi.bufferedReader().use(BufferedReader::readText)

        }catch(e: Exception){
            Toast.makeText(this,"Eror al recuperar nombre de usuario", Toast.LENGTH_SHORT).show()
            println(e.message)
        }

        return cuerpo
    }

    fun leerInternoDinero(): Float{
        var cuerpo = 0f
        try {
            var archivo = auth.currentUser!!.email!!

            val ofi = openFileInput("${archivo}dinero.txt")

            cuerpo = ofi.bufferedReader().use(BufferedReader::readText).toFloat()


        }catch(e: Exception){
            println(e.message)
        }

        return cuerpo
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        return when(item.itemId){
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            auth.signOut()
            super.onBackPressed()
        }
    }








//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.principal, menu)
//        return true
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
