package com.example.proyecto_final_gastos.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    val dinero: MutableLiveData<Float> = MutableLiveData()

    val nombre : MutableLiveData<String> = MutableLiveData<String>().apply {
        value = "textholder"
    }

    fun setNombre(value: String?){
        nombre.value = value
        println("cambio el nombre a "+value)
        println("el nombre actual es "+nombre.value)
    }

    fun setDinero(value: Float?){
        dinero.value = value
    }

    fun getDinero(): Float?{
        return dinero.value
    }






}