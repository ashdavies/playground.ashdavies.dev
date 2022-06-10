plugins {
    `cloud-function`
}

dependencies {
    implementation(libs.auth.java.jwt)
    implementation(libs.bundles.ktor.client)
}

val deployAppCheckFunction by tasks.registering(DeployFunctionTask::class) {
    entryPoint = "io.ashdavies.check.AppCheckFunction"
    description = "Deploy app check function to Google Cloud"
    allowUnauthenticated = true
    function = "app-check"
}

// http://localhost:8080/?appId=1:279861227938:android:8b4e86b064ce7429c502fb&token=d193caab7c5e00a740557f8b23db4e163d0d997af12a85b1

val runAppCheckFunction by tasks.registering(RunFunctionTask::class) {
    println("NOTICE: Running AppCheck function with graphics environment ${System.getProperty("java.awt.graphicsenv")}")
    // properties += "java.awt.graphicsenv" to "io.ashdavies.playground.cloud.EmptyGraphicsEnvironment"
    target = "io.ashdavies.check.AppCheckFunction"
    description = "Run app check cloud function"
    setupClasspath()
}

fun RunFunctionTask.setupClasspath() = doFirst {
    sourceSets.main.configure { args("--classpath", files(configurations.runtimeClasspath, output).asPath) }
}
