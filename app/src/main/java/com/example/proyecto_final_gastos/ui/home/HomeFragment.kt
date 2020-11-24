package com.example.proyecto_final_gastos.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.proyecto_final_gastos.R
import com.example.proyecto_final_gastos.ui.gallery.GalleryFragment
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    var nombre : String? = null
    var dinero : Float? = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = activity?.run {
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        nombre = arguments?.getString("nombre")
        dinero = arguments?.getFloat("dinero")
        println(nombre)
        if(nombre!=null) {
            homeViewModel.setNombre(nombre)
        }
        if(dinero!=null){
            homeViewModel.setDinero(dinero)
        }



        activity?.title = "BIENVENIDO"

        root.btnPrimerRegistro.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.nav_host_fragment,GalleryFragment()).commit()
        }

        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        println("hola")

    }

    companion object {

        @JvmStatic
        fun newInstance(nombre: String, dinero: Float) = HomeFragment().apply {
            arguments = Bundle().apply {
                putString("nombre", nombre)
                putFloat("dinero",dinero)
            }
        }
    }
}
