package com.example.practicadibujotablero.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.os.Handler
import com.example.practicadibujotablero.ui.model.Tablero

class TableroView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var modelo: Tablero = Tablero()
    private val pincel = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    private var direccionActual: String = "arriba"

    private val handler = Handler()

    private val updateRunnable = object : Runnable {
        override fun run() {
            moverDireccionSerpiente(direccionActual)
            invalidate()
            handler.postDelayed(this, 300)
        }
    }

    init {
        handler.post(updateRunnable)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val tablero = modelo
        val anchoDisponible = width
        val altoDisponible = height

        val tamañoCelda = minOf(
            anchoDisponible / tablero.columnas,
            altoDisponible / tablero.filas
        )

        for (i in 0 until tablero.filas) {
            for (j in 0 until tablero.columnas) {
                val color =
                    when (tablero.matriz[i][j]) {
                        1 -> Color.BLACK  // Cuerpo de la serpiente
                        2 -> Color.RED    // Comida de la serpiente
                        3 -> Color.GREEN  // Cabeza de la serpiente
                        else -> Color.WHITE // Fondo vacío
                    }
                pincel.color = color
                canvas.drawRect(
                    (j * tamañoCelda).toFloat(),
                    (i * tamañoCelda).toFloat(),
                    ((j + 1) * tamañoCelda).toFloat(),
                    ((i + 1) * tamañoCelda).toFloat(),
                    pincel
                )
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                val y = event.y

                if (y < height * 0.25) {
                    direccionActual = "arriba"
                } else if (y > height * 0.75) {
                    direccionActual = "abajo"
                } else if (x < width * 0.5) {
                    direccionActual = "izquierda"
                } else if (x > width * 0.5) {
                    direccionActual = "derecha"
                }
                moverDireccionSerpiente(direccionActual)
            }
        }

        return true
    }

    private fun moverDireccionSerpiente(direccion: String) {
        modelo.moverSerpiente(direccion, context)
        invalidate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler.removeCallbacks(updateRunnable)
    }

}