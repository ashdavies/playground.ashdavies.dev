package io.ashdavies.notion

fun interface Printer {
    fun print(block: Printer.() -> String)
}

fun Printer() = object : Printer {
    override fun print(block: Printer.() -> String) = print(block())
}

operator fun Printer.invoke(block: Printer.() -> Unit) {
    block()
}

fun Printer.println(block: Printer.() -> String) {
    print { "${block()}\n" }
}

fun Printer.print(colour: Colour, block: Printer.() -> String) {
    print { colour.ansi + block() + Colour.Reset.ansi }
}

fun Printer.print(style: Style, block: Printer.() -> String) {
    print { style.ansi + block() + Style.Reset.ansi }
}

fun Printer.green(block: Printer.() -> String) {
    return print(Colour.Green, block)
}

fun Printer.bold(block: Printer.() -> String) {
    return print(Style.Bold, block)
}

enum class Colour(val ansi: String) {
    Green("\\u001B[32m"),
    Reset("\\u001B[0m");
}

enum class Style(val ansi: String) {
    Bold("\u001B[1m"),
    Reset("\u001B[0m");
}
