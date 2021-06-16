import ProjectDependencies.Google
import ProjectDependencies.JetBrains

plugins {
    `kotlin-jvm`
    serialization
    apollo
}

dependencies {
    implementation(project(":shared"))

    implementation(ProjectDependencies.ApolloGraphQl.apolloRuntime)
    implementation(ProjectDependencies.Dropbox.store4)
    implementation(Google.CloudFunctions.functionsFrameworkApi)
    implementation(Google.Firebase.firebaseAdmin)
    implementation(JetBrains.KotlinX.kotlinxCoroutinesCore)
    implementation(JetBrains.KotlinX.kotlinxDatetime)
    implementation(JetBrains.KotlinX.kotlinxSerializationCore)
    implementation(JetBrains.KotlinX.kotlinxSerializationJson)

    testImplementation(kotlin("test"))
}