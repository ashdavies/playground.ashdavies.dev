import java.util.Locale
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty

private const val String = "String"

public typealias StringDelegateProvider = PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, String>>

public fun <T> T.SystemProperty(
    block: T.(type: String, name: String, value: String) -> Unit,
    value: (String) -> String = System::getenv,
) = PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, String>> { _, property ->
    val name = CaseFormat.LowerCamel(property.name).toUpperSnake()
    block(String, name, "\"${value(name)}\"")

    ReadOnlyProperty<Any?, String> { _, _ ->
        value(name)
    }
}

internal class CaseFormat private constructor(private val words: List<String>) {

    fun toUpperSnake(): String {
        return words.joinToString(separator = "_") { it.toUpperCase(Locale.getDefault()) }
    }

    companion object {
        fun LowerCamel(value: String) = CaseFormat(value.split(Regex("(?<!^)(?=[A-Z])")))
    }
}
