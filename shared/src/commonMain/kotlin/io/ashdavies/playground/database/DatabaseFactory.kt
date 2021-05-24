package io.ashdavies.playground.database

class DatabaseFactory(private val driverFactory: DriverFactory) {

    suspend fun create() = PlaygroundDatabase(
        driver = driverFactory.create(),
    )
}
