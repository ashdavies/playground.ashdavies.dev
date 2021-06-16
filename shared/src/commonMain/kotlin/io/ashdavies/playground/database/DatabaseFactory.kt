package io.ashdavies.playground.database

class DatabaseFactory(private val driverFactory: DriverFactory) {

    fun create() = PlaygroundDatabase(
        driver = driverFactory.create(),
    )
}
