interface PluginDescriptor {
    val id: String
    val version: String
}

@Suppress("FunctionName")
fun PluginDescriptor(
    id: String,
    version: String
): PluginDescriptor = PluginDescriptorImpl(
    id = id,
    version = version
)

private class PluginDescriptorImpl(
    override val id: String,
    override val version: String
) : StringFacade("$id:$version"), PluginDescriptor

private abstract class StringFacade(
    private val value: String
): CharSequence by value {
    override fun toString(): String = value
}
