package com.example.proyecto_final_gastos.ui.detallesmes

import Categoria
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.proyecto_final_gastos.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_detalle_mes.view.*
import kotlinx.android.synthetic.main.fragment_fragmento_expandir_categoria.view.*
import kotlinx.android.synthetic.main.fragment_fragmento_semana_detallada.view.*
import kotlinx.coroutines.withTimeout
import java.io.BufferedReader
import java.io.FileOutputStream


class DetallesMesFragment : Fragment() {

    private lateinit var slideshowViewModel: DetallesMesViewModel
    private lateinit var auth: FirebaseAuth

    private var categoriasMes = ArrayList<Categoria>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProviders.of(this).get(DetallesMesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_detalle_mes, container, false)

        var mes = arguments?.getString("mes")

        auth = FirebaseAuth.getInstance()

        println(mes)

        activity?.title = mes
        root.txtMes.text = mes

        categoriasMes = leerInterno(root,mes)



        if(categoriasMes.isEmpty())
            categoriasMes = obtenerFragmentos(mes)

        root.listaDetalleMes.adapter = CategoriaAdapter(this.requireContext(),categoriasMes)
        root.btnGuardarMes.setOnClickListener {
            guardarInterno(root, mes)
        }


        for(categoria : Categoria in categoriasMes){
            println("Nombre: ${categoria.nombre}")
            for(gasto : Float in categoria.semanas){
                println("Gasto: $gasto")
            }
        }

        return root
    }

    fun obtenerFragmentos(mes: String?) : ArrayList<Categoria>{
        var categorias = ArrayList<Categoria>()



        when(mes){
            "Enero 2020","Marzo 2020","Abril 2020","Mayo 2020"  ->{
                var gastos = ArrayList<Float>()
                gastos.add(150f)
                gastos.add(220f)
                gastos.add(300f)
                gastos.add(450f)
                gastos.add(50f)

                categorias.add(Categoria("Escuela",gastos,0f,0))
                categorias.add(Categoria("Comida",gastos,0f,0))
                categorias.add(Categoria("Transporte",gastos,0f,0))
                categorias.add(Categoria("Botanas",gastos,0f,0))
                categorias.add(Categoria("Diversion",gastos,0f,0))
                categorias.add(Categoria("Extra",gastos,0f,0))
            }
            "Febrero 2020" ->{
                var gastos = ArrayList<Float>()
                gastos.add(100f)
                gastos.add(200f)
                gastos.add(200f)
                gastos.add(600f)

                categorias.add(Categoria("Escuela",gastos,0f,0))
                categorias.add(Categoria("Comida",gastos,0f,0))
                categorias.add(Categoria("Transporte",gastos,0f,0))
                categorias.add(Categoria("Botanas",gastos,0f,0))
                categorias.add(Categoria("Diversion",gastos,0f,0))
                categorias.add(Categoria("Extra",gastos,0f,0))
                categorias.add(Categoria("San Valentin",gastos,0f,0))
            }
        }
        return categorias
    }

    fun guardarInterno(root: View, mes: String?){

        if(mes == null)
            return

        var cuerpo : String = ""

        // Recupera el adaptador para acceder a los datos de categoria
        var adaptarCategoria = root.listaDetalleMes.adapter

        // Itera por todas las categorias
        var i : Int = 0
        while(i < adaptarCategoria.count){
            // Consigue la categoria
            var categoria = adaptarCategoria.getItem(i) as Categoria

            // Itera sobre gastos
            // Utilizaremos una variable String para ir guardando los valores
            var gastoSemanal : String = ""
            var j : Int = 0
            while(j < categoria.semanas.size){
                // Consigue el gasto
                var gasto = categoria.semanas[j]

                // Empieza a escribir
                // Vamos a usar tildes para dividir los valores
                gastoSemanal = gastoSemanal.plus(gasto).plus("~")
                println("i:$i j:$j")
                // Aumenta el contador
                j++
            }

            // Quita el ultimo tilde
            gastoSemanal = gastoSemanal.substring(0,gastoSemanal.length-1)

            // Empieza a escribir
            // Vamos a usar barras para dividir entre nombre y gastos
            // Primero el nombre de la categoria...
            cuerpo = cuerpo.plus("|").plus(categoria.nombre).plus("|")

            // Ahora los valores de gastos obtenidos previamente
            cuerpo = cuerpo.plus(gastoSemanal)

            println(cuerpo)

            // Aumenta el contador
            i++
        }


        if(mes == "" || cuerpo == ""){
            Toast.makeText(root.context, "Error: campos vacios", Toast.LENGTH_SHORT).show()
            return
        }

        // Quita las barras del principio y fin

        cuerpo = cuerpo.substring(1)

        val titulo = auth.currentUser!!.email+"$mes.txt"
        val fos : FileOutputStream = root.context.openFileOutput(titulo, Context.MODE_PRIVATE)
        fos.write(cuerpo.toByteArray())
        fos.close()

        Toast.makeText(root.context, "Se han guardado tus gastos del mes", Toast.LENGTH_SHORT).show()
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
        var categoriass = ArrayList<Categoria>()
        var adapters = ArrayList<CostosAdapter>()
        var context: Context? = null
        var inflater : LayoutInflater? = null

        constructor(context: Context, categoriass : ArrayList<Categoria>){
            this.context = context
            this.categoriass = categoriass
            inflater = this.context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var categoria = categoriass[p0]
            var vista = inflater!!.inflate(R.layout.fragment_fragmento_expandir_categoria, null)


            var listView = vista.listaExpandida

            var newAdapter = CostosAdapter(context!!, categoria.semanas)
            vista.listaExpandida.adapter = newAdapter

            if(!adapters.contains(newAdapter))
                adapters.add(newAdapter)

            val params: ViewGroup.LayoutParams = listView.getLayoutParams()
            params.height = categoria.semanas.size * 115
            listView.setLayoutParams(params)
            listView.requestLayout()


            vista.txtNombreCategoriaAbierta.setText(categoria.nombre)
            vista.btnCambiarCategoria.setImageResource(R.drawable.ic_add_black_24dp)
            vista.btnCambiarCategoria.setOnClickListener {
                if(vista.listaExpandida.visibility==View.GONE){
                    vista.listaExpandida.visibility = View.VISIBLE
                    vista.btnCambiarCategoria.setImageResource(R.drawable.ic_remove_black_24dp)
                    vista.linealayoutcategoriaexpandida.setBackgroundResource(R.drawable.boton_08)
                    println("bisible")
                }else{
                    vista.listaExpandida.visibility = View.GONE
                    vista.btnCambiarCategoria.setImageResource(R.drawable.ic_add_black_24dp)
                    vista.linealayoutcategoriaexpandida.setBackgroundResource(R.drawable.boton_01)
                    println("soy un fantasma")
                }

            }

            vista.listaExpandida.requestLayout()

            return vista;
        }

        override fun getItem(p0: Int): Any {
            // Obtiene gastos actualizados
            var nuevosCosts = ArrayList<Float>()
            var i = 0
            while(i < adapters[p0].count){
                println("Categoria "+categoriass[p0].nombre+" costo $p0: "+adapters[p0].getItem(i) as Float)
                nuevosCosts.add(adapters[p0].getItem(i) as Float)
                i++
            }

            return categoriass[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return categoriass.size
        }

        class CostosAdapter: BaseAdapter {
            var gastos = ArrayList<Float>()
            var context: Context? = null
            var inflater : LayoutInflater? = null

            constructor(context: Context, gastos : ArrayList<Float>){
                this.context = context
                this.gastos = gastos
                inflater = this.context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            }

            override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
                var gasto = gastos[p0]
                var vista = inflater!!.inflate(R.layout.fragment_fragmento_semana_detallada, null)

                vista.txtSemanaExpandida.text = "Semana 0"+(p0+1)
                vista.editCantidadExpandida.setText(""+gasto)

                vista.editCantidadExpandida.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p1: Editable?) {
                        try{
                            gastos[p0] = p1.toString().toFloat()
                        }catch(e: NumberFormatException){
                            Toast.makeText(context,"Error al ingresar gasto, sera ignorado el cambio",Toast.LENGTH_SHORT).show()
                            println(e.message)
                        }
                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        // nada
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        // nada
                    }
                })

                return vista;
            }

            override fun getItem(p0: Int): Any {
                return gastos[p0]
            }

            override fun getItemId(p0: Int): Long {
                return p0.toLong()
            }

            override fun getCount(): Int {
                return gastos.size
            }
        }

    }

    companion object {

        @JvmStatic
        fun newInstance(mes: String) = DetallesMesFragment().apply {
            arguments = Bundle().apply {
                putString("mes", mes)
            }
        }
    }

}
