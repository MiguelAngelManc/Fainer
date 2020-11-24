package com.example.proyecto_final_gastos.ui.registrogasto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.proyecto_final_gastos.R

class RegistroGastoFragment : Fragment() {

    private lateinit var slideshowViewModel: RegistroGastoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProviders.of(this).get(RegistroGastoViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_registrogasto, container, false)

        activity?.title = "REGISTRO DE GASTO"


        return root
    }
}
