package com.Leon.lab02CarritoPoo

// =========================================================
// CLASE ABSTRACTA BASE (antes era data class Producto)
// =========================================================
abstract class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
) {
    // Cada subclase define su propio calculo de importe (POLIMORFISMO)
    abstract fun calcularImporte(): Double

    // Cada subclase indica su propio tipo, usado en el detalle
    abstract fun getTipo(): String
}

// =========================================================
// SUBCLASE 1: ProductoFisico
// Comportamiento distinto: suma un costo de envio segun peso
// =========================================================
class ProductoFisico(
    nombre: String,
    precio: Double,
    cantidad: Int,
    val pesoKg: Double
) : Producto(nombre, precio, cantidad) {

    companion object {
        const val COSTO_ENVIO_POR_KG = 5.0
    }

    override fun calcularImporte(): Double {
        val importeBase = precio * cantidad
        val costoEnvio = pesoKg * cantidad * COSTO_ENVIO_POR_KG
        return importeBase + costoEnvio
    }

    override fun getTipo(): String = "Fisico"
}

// =========================================================
// SUBCLASE 2: ProductoDigital
// Comportamiento distinto: aplica descuento por licencia digital
// y no genera costo de envio
// =========================================================
class ProductoDigital(
    nombre: String,
    precio: Double,
    cantidad: Int
) : Producto(nombre, precio, cantidad) {

    companion object {
        const val DESCUENTO_LICENCIA = 0.03 // 3%
    }

    override fun calcularImporte(): Double {
        val importeBase = precio * cantidad
        return importeBase - (importeBase * DESCUENTO_LICENCIA)
    }

    override fun getTipo(): String = "Digital"
}

// =========================================================
// CLASE Carrito: encapsula la lista de productos y toda la
// logica comercial que antes estaba en funciones sueltas
// =========================================================
class Carrito(private val cliente: String) {

    private val productos = mutableListOf<Producto>()

    fun agregarProducto(producto: Producto) {
        productos.add(producto)
        println("Producto agregado: ${producto.nombre}")
    }

    fun cantidadProductos(): Int = productos.size

    // Se conserva la regla: subtotal = suma de importes de cada producto
    // (el importe ahora depende del comportamiento polimorfico de cada tipo)
    fun calcularSubtotal(): Double = productos.sumOf { it.calcularImporte() }

    // Se conserva la regla: IGV = 18% del subtotal
    fun calcularIGV(subtotal: Double): Double = subtotal * 0.18

    // Se conserva la regla: total = subtotal + IGV
    fun calcularTotal(subtotal: Double, igv: Double): Double = subtotal + igv

    // Se conserva la regla de descuentos por tramos (5% y 10%)
    fun calcularDescuento(total: Double): Double = when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }

    private fun obtenerTextoDescuento(total: Double): String = when {
        total > 5000 -> "Descuento (10%):"
        total > 3000 -> "Descuento (5%):"
        else -> "Descuento (0%):"
    }

    // Se conserva la busqueda del producto mas caro con maxByOrNull
    fun productoMasCaro(): Producto? = productos.maxByOrNull { it.precio }

    fun mostrarDetalle() {
        println("--------- DETALLE DEL CARRITO ---------")
        productos.forEachIndexed { index, p ->
            println(
                String.format(
                    "%d. %-20s x%d S/ %8.2f [%s]",
                    index + 1, p.nombre, p.cantidad, p.calcularImporte(), p.getTipo()
                )
            )
        }
        println("---------------------------------------")
    }

    fun mostrarResumen() {
        val subtotal = calcularSubtotal()
        val igv = calcularIGV(subtotal)
        val total = calcularTotal(subtotal, igv)
        val descuento = calcularDescuento(total)
        val totalConDescuento = total - descuento
        val textoDescuento = obtenerTextoDescuento(total)

        mostrarDetalle()
        println("Cantidad de productos: ${cantidadProductos()}")
        println(String.format("%-20s S/ %8.2f", "Subtotal:", subtotal))
        println(String.format("%-20s S/ %8.2f", "IGV (18%):", igv))
        println(String.format("%-20s S/ %8.2f", "Total:", total))
        println("---------------------------------------")

        productoMasCaro()?.let {
            println(
                "Producto mas caro: ${it.nombre} " +
                        String.format("(S/ %.2f)", it.precio)
            )
        }

        println(String.format("%-23s S/ %8.2f", textoDescuento, descuento))
        println(String.format("%-20s S/ %8.2f", "TOTAL CON DESCUENTO:", totalConDescuento))
        println("---------------------------------------")
    }
}

// =========================================================
// FUNCION MAIN
// =========================================================
fun main() {
    println("=========================================")
    println(" CARRITO DE COMPRAS - TIENDA TECSUP ")
    println("=========================================")

    val carrito = Carrito("Daniella Leon")
    println("Cliente: Daniella Leon")
    println()

    // Productos fisicos (heredan costo de envio segun peso)
    carrito.agregarProducto(ProductoFisico("Laptop HP", 2500.0, 1, 2.5))
    carrito.agregarProducto(ProductoFisico("Mouse Logitech", 45.5, 2, 0.2))
    carrito.agregarProducto(ProductoFisico("Teclado Razer", 80.0, 1, 0.8))
    carrito.agregarProducto(ProductoFisico("Monitor LG", 150.0, 1, 4.0))

    // Producto digital (aplica descuento de licencia, sin envio)
    carrito.agregarProducto(ProductoDigital("Licencia Antivirus", 120.0, 1))

    println()
    carrito.mostrarResumen()
}