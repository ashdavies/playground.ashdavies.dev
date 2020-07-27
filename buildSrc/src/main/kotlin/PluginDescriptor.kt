interface PluginDescriptor {
    val module: String
    val version: String
}

@Suppress("FunctionName")
fun PluginDescriptor(
    module: String,
    version: String
): PluginDescriptor = PluginDescriptorImpl(
    module = module,
    version = version
)

private class PluginDescriptorImpl(
    override val module: String,
    override val version: String
) : StringFacade("$module:$version"), PluginDescriptor

private abstract class StringFacade(
    private val value: String
): CharSequence by value {
    override fun toString(): String = value
}
