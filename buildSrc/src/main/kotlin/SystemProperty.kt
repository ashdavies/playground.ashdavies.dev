import java.util.Locale
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty

typealias SystemDelegateProvider = PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, String>>

fun <T> T.SystemProperty(block: T.(type: String, name: String, value: String) -> Unit) = SystemProperty { name, value ->
    block("String", name, "\"$value\"")
}

fun <T> T.SystemProperty(block: T.(name: String, value: String) -> Unit) = SystemDelegateProvider { _, property ->
    val name = CaseFormat.LowerCamel(property.name).toUpperSnake()
    val value = System.getenv(name)
    block(name, value)

    ReadOnlyProperty<Any?, String> { _, _ -> value }
}

internal class CaseFormat private constructor(private val words: List<String>) {

    fun toUpperSnake(): String {
        return words.joinToString(separator = "_") { it.toUpperCase(Locale.getDefault()) }
    }

    companion object {
        fun LowerCamel(value: String) = CaseFormat(value.split(Regex("(?<!^)(?=[A-Z])")))
    }
}
