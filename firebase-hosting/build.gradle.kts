plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("firebaseHostingPlugin") {
            implementationClass = "FirebaseHostingPlugin"
            id = "dev.ashdavies.firebase.hosting"
        }
    }
}