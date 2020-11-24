package com.example.proyecto_final_gastos.ui.gallery

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.proyecto_final_gastos.R
import com.example.proyecto_final_gastos.ui.home.HomeViewModel
import com.example.proyecto_final_gastos.ui.registrogasto.RegistroGastoFragment
import com.example.proyecto_final_gastos.ui.slideshow.SlideshowFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.app_bar_principal.view.*
import kotlinx.android.synthetic.main.fragment_detalle_mes.view.*
import kotlinx.android.synthetic.main.fragment_gallery.view.*
import java.io.FileOutputStream

class GalleryFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = activity?.run {
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        auth = FirebaseAuth.getInstance()

        val root = inflater.inflate(R.layout.fragment_gallery, container, false)

        root.btnCuantoTraes.setOnClickListener {
            try {
                var dineronuevo =
                    root.edit_Cantidad_Registrar.text.toString().toFloat() + root.edit_Cuanto_Queda.text.toString().toFloat()
                homeViewModel.setDinero(dineronuevo)
                guardarInterno(root,dineronuevo)
                parentFragmentManager.beginTransaction().replace(
                    R.id.nav_host_fragment,
                    SlideshowFragment()
                ).commit()
            }catch (ex : Exception){
                Toast.makeText(root.context,"Error al guardar el ingreso. Favor de llenar los campos correctamente.",Toast.LENGTH_SHORT).show()
                println(ex.message)
            }
        }

        activity?.title = "REGISTRO DE INGRESO"

        root.edit_Cuanto_Queda.setText("${homeViewModel.getDinero()}")

        return root
    }

    fun guardarInterno(root: View?, dineronuevo:Float){
        var cuerpo = "$dineronuevo"

        val titulo = auth.currentUser!!.email+"dinero.txt"
        val fos : FileOutputStream = root!!.context.openFileOutput(titulo, Context.MODE_PRIVATE)
        fos.write(cuerpo.toByteArray())
        fos.close()

        Toast.makeText(root!!.context, "Se ha guardado tu dinero", Toast.LENGTH_SHORT).show()
    }

}
