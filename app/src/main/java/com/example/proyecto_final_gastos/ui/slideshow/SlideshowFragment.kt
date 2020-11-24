package com.example.proyecto_final_gastos.ui.slideshow

import Categoria
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.proyecto_final_gastos.R
import com.example.proyecto_final_gastos.ui.CustomCircleDrawable
import com.example.proyecto_final_gastos.ui.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_slideshow.view.*
import kotlinx.android.synthetic.main.grid_categoria.view.*
import java.io.BufferedReader
import kotlin.math.roundToInt


class SlideshowFragment : Fragment() {

    private lateinit var homeViewModel : HomeViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = activity?.run {
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)

        homeViewModel.nombre.observe(viewLifecycleOwner, Observer {
            println("toy observandote")
            var nombre = homeViewModel.nombre.value
            if(nombre!=null)
                root.txtNombreGastos.text = "Hola $nombre"
        })

        auth = FirebaseAuth.getInstance()

        var dinero = homeViewModel.getDinero()!!

        activity?.title = "GASTO MENSUAL"

        // Recupera gastos
        var categorias = leerInterno(root,"Mayo 2020")

        if(!categorias.isEmpty()){
            // Convierte
            var nuevaCategorias = convertirLista(categorias)
            // Hace la grafica
            val fondo = CustomCircleDrawable(root.context,nuevaCategorias)
            root.graphCategorias.background = fondo
            // Hace las legendas
            root.gridCategorias.adapter = CategoriaAdapter(root.context,nuevaCategorias)
            // Calcula total
            var total = 0f
            for(c in nuevaCategorias)
                total+=c.total
            // Indica ahorros
            var finalahorro = homeViewModel.getDinero()!! - total
            root.txtAhorrosMes.text = "Tus ahorros del mes: $" + finalahorro
        }else{
            root.textView5.text = "No tienes gastos registrados este mes."
        }



        return root
    }

    fun leerInterno(root: View, mes: String?): ArrayList<Categoria>{
        var categorias = ArrayList<Categoria>()

        if(mes==null)
            return  categorias

        val nombre = auth.currentUser!!.email+"$mes.txt"
        var cuerpo : String

        try {
            val ofi = root.context.openFileInput(nombre)

            cuerpo = ofi.bufferedReader().use(BufferedReader::readText)




            // Divide el texto leido por barras para saber donde empiezan y terminan las categorias
            val divido = cuerpo.split("|")



            // Itera
            // Pares = nombres ; Inpares = gastos
            var titulo : Boolean = true
            var nombre : String = ""
            for(valor : String in divido){
                // Variables de la categoria
                var gastosFloat = ArrayList<Float>()


                if(titulo){
                    println("Nombre: $valor")
                    nombre = valor
                    titulo = false
                }else{
                    println("Gastos: $valor")
                    // Divide el texto por tildes
                    var gastos = valor.split("~")

                    // Itera sobre texto
                    for(gasto : String in gastos){
                        try{
                            gastosFloat.add(gasto.toFloat())
                        }catch (e: java.lang.NumberFormatException){
                            println("$gasto no es un float")
                        }
                    }

                    // Crea la categoria
                    var categoria : Categoria = Categoria(nombre,gastosFloat,0f,0)
                    // Agrega a la lista
                    categorias.add(categoria)

                    titulo = true
                }
            }

        }catch(e: Exception){
            Toast.makeText(root.context,"No tienes guardado nada de este mes", Toast.LENGTH_SHORT).show()
            println(e.message)
        }

        return categorias
    }

    class CategoriaAdapter: BaseAdapter {
        var categoriass : List<Categoria>
        var context: Context? = null
        var inflater: LayoutInflater? = null

        constructor(context: Context, categoriass: List<Categoria>) {
            this.context = context
            this.categoriass = categoriass
            inflater =
                this.context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var categoria = categoriass[p0]
            var vista = inflater!!.inflate(R.layout.grid_categoria, null)
            vista.setLayoutParams(AbsListView.LayoutParams(context!!.resources.getDimensionPixelSize(R.dimen.categoriaWidth), context!!.resources.getDimensionPixelSize(R.dimen.categoriaHeight)))
            vista.nombreCategoria.text = categoria.nombre
            var fondo = vista.circuloCategoria.background
            if (fondo is GradientDrawable) { // cast to 'ShapeDrawable'
                fondo.setColor(categoria.color)
            }
            return vista;
        }

        override fun getItem(p0: Int): Any {
            return categoriass[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return categoriass.size
        }
    }

    private fun convertirLista(cats : ArrayList<Categoria>): List<Categoria>{
        // Color inicial
        var colorInicial = context?.resources?.getColor(R.color.colorPrimary) ?: R.color.colorPrimary
        var colorIterativo = colorInicial


        for(c in cats) {
            var totalMes : Float = 0f
            for(s in c.semanas)
                totalMes+=s
            c.total = totalMes
        }

        // Ordena de mayor a menor
        var nueva = cats.sortedByDescending { it.total }

        // Cambia colores
        for(c in nueva){
            c.color = colorIterativo
            colorIterativo = siguienteColor(colorIterativo)
        }

        return nueva
    }

    private fun siguienteColor(colorIterativo: Int): Int {
        val a = Color.alpha(colorIterativo)
        val r = (Color.red(colorIterativo) * 1.1).roundToInt()
        val g = (Color.green(colorIterativo) * 1.1).roundToInt()
        val b = (Color.blue(colorIterativo) * 1.1).roundToInt()
        return Color.argb(
            a,
            r.coerceAtMost(255),
            g.coerceAtMost(255),
            b.coerceAtMost(255)
        )
    }

}
