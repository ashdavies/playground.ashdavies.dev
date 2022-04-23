plugins {
    `multiplatform-library`
    `multiplatform-sql`
}

sqldelight {
    database("PlaygroundDatabase") {
        packageName = "io.ashdavies.playground"
        dialect = "sqlite:3.25"
    }
}
