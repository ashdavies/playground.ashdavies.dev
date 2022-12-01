public object Playground {

    const val compileSdk = 33
    const val jvmTarget = "11"
    const val minSdk = 21

    val freeCompilerArgs = listOf(
        "-opt-in=kotlin.RequiresOptIn",
        "-Xallow-result-return-type",
        "-Xcontext-receivers",
        "-Xmulti-platform",
    )
}
