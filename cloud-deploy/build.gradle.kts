repositories {
    gradlePluginPortal()
}

plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins.register("deploy-function") {
        implementationClass = "DeployFunctionPlugin"
        id = name
    }
}
