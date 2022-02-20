package io.ashdavies.notion

public expect object Browser {
    public fun launch(uriString: String): Boolean
}
