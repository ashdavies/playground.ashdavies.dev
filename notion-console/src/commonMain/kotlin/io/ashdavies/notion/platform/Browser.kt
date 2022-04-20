package io.ashdavies.notion.platform

@Deprecated("Browser is deprecated, use LocalUriHandler instead")
public expect object Browser {
    public fun launch(uriString: String): Boolean
}
