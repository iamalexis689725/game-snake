package com.example.practicadibujotablero.ui.model
import android.content.Context
import androidx.appcompat.app.AlertDialog
import kotlin.random.Random

class Tablero {
    var filas: Int = 20
    var columnas: Int = 10
    var matriz: Array<IntArray> = Array(filas) { IntArray(columnas) }
    var comida: Pair<Int, Int> = Pair(-1, -1)

    private var posicionSerpiente: MutableList<Pair<Int, Int>> = mutableListOf()
    private var ultimaDireccion: String = ""

    init {
        inicializarJuego()
        colocarComida()
    }

    private fun inicializarJuego() {

        matriz = Array(filas) { IntArray(columnas) }
        posicionSerpiente.clear()

        val inicioX = filas / 2
        val inicioY = columnas / 2
        posicionSerpiente.add(Pair(inicioX, inicioY)) // Cabeza
        posicionSerpiente.add(Pair(inicioX, inicioY + 1)) // Cuerpo

        matriz[inicioX][inicioY] = 3 // Cabeza de la serpiente
        matriz[inicioX][inicioY + 1] = 1 // Mi primer cuadro del cuerpo de la serpiente
    }

    private fun colocarComida() {
        do {
            comida = Pair(Random.nextInt(filas), Random.nextInt(columnas))
        } while (matriz[comida.first][comida.second] != 0)

        matriz[comida.first][comida.second] = 2
    }

    fun moverSerpiente(direccion: String, context: Context) {
        if ((direccion == "izquierda" && ultimaDireccion == "derecha") ||
            (direccion == "derecha" && ultimaDireccion == "izquierda") ||
            (direccion == "arriba" && ultimaDireccion == "abajo") ||
            (direccion == "abajo" && ultimaDireccion == "arriba")) {
            return
        }

        ultimaDireccion = direccion

        val cabeza = posicionSerpiente[0]
        var nuevaCabeza: Pair<Int, Int> = cabeza

        // Este es mi switch para cambiar la posicion de la cabeza de la serpiente segun la direccion
        when (direccion) {
            "arriba" -> nuevaCabeza = Pair(cabeza.first - 1, cabeza.second)
            "abajo" -> nuevaCabeza = Pair(cabeza.first + 1, cabeza.second)
            "izquierda" -> nuevaCabeza = Pair(cabeza.first, cabeza.second - 1)
            "derecha" -> nuevaCabeza = Pair(cabeza.first, cabeza.second + 1)
        }

        // Compruebo si la cabeza está fuera de los límites para traspasar
        if (nuevaCabeza.first < 0) {
            nuevaCabeza = Pair(filas - 1, nuevaCabeza.second)
        } else if (nuevaCabeza.first >= filas) {
            nuevaCabeza = Pair(0, nuevaCabeza.second)
        }

        if (nuevaCabeza.second < 0) {
            nuevaCabeza = Pair(nuevaCabeza.first, columnas - 1)
        } else if (nuevaCabeza.second >= columnas) {
            nuevaCabeza = Pair(nuevaCabeza.first, 0)
        }

        // Este es para saber si choco con su cuerpo
        if (posicionSerpiente.contains(nuevaCabeza)) {
            // mostrarDialogoPerdida(context)
            return
        }

        // Este es mi if para saber si la serpiente comio
        if (nuevaCabeza == comida) {
            matriz[nuevaCabeza.first][nuevaCabeza.second] = 3
            posicionSerpiente.add(0, nuevaCabeza)
            colocarComida()
        } else {
            matriz[cabeza.first][cabeza.second] = 1
            posicionSerpiente.add(0, nuevaCabeza)
            matriz[nuevaCabeza.first][nuevaCabeza.second] = 3
            matriz[posicionSerpiente.last().first][posicionSerpiente.last().second] = 0
            posicionSerpiente.removeAt(posicionSerpiente.size - 1)
        }
    }

    /*fun mostrarDialogoPerdida(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("¡Perdiste!")
        builder.setMessage("¿Quieres volver a jugar?")
        builder.setPositiveButton("Sí") { dialog, which ->
            reiniciarJuego()
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }*/


    // fun reiniciarJuego() {
    //     inicializarJuego()
    //     colocarComida()
    // }
}
