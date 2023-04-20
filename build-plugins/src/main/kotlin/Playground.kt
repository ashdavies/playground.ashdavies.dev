public object Playground {

    const val compileSdk = 33
    const val jvmTarget = 17
    const val minSdk = 24

    val freeCompilerArgs = listOf(
        "-opt-in=kotlin.RequiresOptIn",
        "-Xallow-result-return-type",
        "-Xexplicit-api=warning",
        "-Xcontext-receivers",
        "-Xmulti-platform",
    )
}
