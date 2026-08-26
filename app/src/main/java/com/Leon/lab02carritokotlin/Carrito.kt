package com.Leon.lab02carritokotlin

import android.annotation.SuppressLint
import kotlin.math.round

data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)

fun calcularSubtotal(productos: List<Producto>): Double {
    var subtotal = 0.0
    for (p in productos) {
        subtotal += p.precio * p.cantidad
    }
    return subtotal
}

fun calcularIGV(subtotal: Double): Double {
    var igv = subtotal * 0.18
    return igv
}

fun calcularTotal(subtotal: Double, igv: Double): Double {
    var total = subtotal + igv
    return total
}

fun mostrarDetalle(productos: List<Producto>) {
    println("--------- DETALLE DEL CARRITO ---------")
    var i = 1
    for (p in productos) {
        val importe = p.precio * p.cantidad
        println(String.format("%d. %-20s x%d S/ %8.2f",
            i, p.nombre, p.cantidad, importe))
        i++
    }
    println("---------------------------------------")
}


fun main() {
    println("=========================================")
    println(" CARRITO DE COMPRAS - TIENDA TECSUP ")
    println("=========================================")

    val nombreCliente = "Daniella Leon" // String (inferido)
    val carrito = mutableListOf<Producto>() // lista vacía de producto

    println("Cliente: $nombreCliente")
    println()

    carrito.add(Producto("Laptop HP", 2500.0, 1))
    carrito.add(Producto("Mouse Logitech", 45.5, 2))
    carrito.add(Producto("Teclado Razer", 80.0, 1))
    carrito.add(Producto("Monitor LG", 150.0, 1))
    for (producto in carrito) {
        println("Producto agregado: ${producto.nombre}")
    }

    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)
    println()
    mostrarDetalle(carrito)
    println("Cantidad de productos: ${carrito.size}")
    println(String.format("%-20s S/ %8.2f", "Subtotal:", subtotal))
    println(String.format("%-20s S/ %8.2f", "IGV (18%):", igv))
    println(String.format("%-20s S/ %8.2f", "Total:", total))
    println("---------------------------------------")
}