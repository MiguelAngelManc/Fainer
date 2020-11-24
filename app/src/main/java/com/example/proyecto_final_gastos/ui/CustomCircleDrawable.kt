package com.example.proyecto_final_gastos.ui

import Categoria
import android.R.attr.factor
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.proyecto_final_gastos.R
import kotlin.math.roundToInt


class CustomCircleDrawable : Drawable {
    var coordenadas: RectF? = null
    var anguloBarrido: Float = 0.0F
    var anguloInicio: Float = 269.0F
    var grosorMetrica: Int = 0
    var grosorFondo: Int = 0
    var context: Context? = null
    var categoriasMes : List<Categoria>

    constructor(context: Context, categoriasMes: List<Categoria>) : super() {
        this.context = context
        this.categoriasMes = categoriasMes
        grosorMetrica = context.resources.getDimensionPixelSize(R.dimen.graphWidth)
        grosorFondo = context.resources.getDimensionPixelSize(R.dimen.graphBackground)
    }


    override fun draw(p0: Canvas) {
        val fondo: Paint = Paint()
        fondo.style = Paint.Style.STROKE
        fondo.strokeWidth = (this.grosorFondo).toFloat()
        fondo.isAntiAlias = true
        fondo.strokeCap = Paint.Cap.ROUND
        fondo.color = context?.resources?.getColor(R.color.negro) ?: R.color.negro
        val ancho: Float = (bounds.width() - 50).toFloat()
        val alto: Float = (bounds.width() - 80).toFloat()

        coordenadas = RectF(50.0F,50.0F,ancho,alto)

        //p0.drawArc(coordenadas!!, 0.0F, 360.0F, false, fondo)


        if(categoriasMes.size != 0){
            // Consigue el total primero
            var total : Float = 0f
            for(c in categoriasMes) {
                total+=c.total
            }
            for(c in categoriasMes){
                val degree : Float = ((c.total/total)*360)
                this.anguloBarrido = degree

                var seccion: Paint = Paint()
                seccion.style = Paint.Style.FILL
                seccion.strokeWidth = (this.grosorMetrica).toFloat()
                seccion.isAntiAlias = true
                seccion.strokeCap = Paint.Cap.SQUARE
                seccion.color = c.color
                p0.drawArc(coordenadas!!,this.anguloInicio,this.anguloBarrido,true,seccion)
                //p0.drawArc(coordenadas!!,this.anguloInicio,this.anguloBarrido,true,fondo)

                this.anguloInicio+=this.anguloBarrido
            }
        }

    }



    override fun setAlpha(p0: Int) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun setColorFilter(p0: ColorFilter?) {

    }


}