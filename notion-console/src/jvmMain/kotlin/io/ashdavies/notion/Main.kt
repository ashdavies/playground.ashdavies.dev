package io.ashdavies.notion

import com.jakewharton.mosaic.runMosaic

public suspend fun main(args: Array<String>) {
    runMosaic { NotionShell(args) }
}
