import org.gradle.internal.impldep.com.google.common.base.CaseFormat
import kotlin.properties.ReadOnlyProperty

fun <T, V> T.CaseProperty(block: T.(String) -> V) = ReadOnlyProperty<T, V> { thisRef, property ->
    thisRef.block(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, property.name))
}
