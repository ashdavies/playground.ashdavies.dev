import kotlin.reflect.KProperty

plugins {
    kotlin("jvm")
    `kotlin-dsl`

    id("maven-publish")
    id("version-catalog")
}

catalog {
    versionCatalog {
        androidx {
            artifact("activity-ktx", "1.4.0-beta01")
            artifact("annotation", "1.3.0-beta01")

            group("compose", "1.1.0-alpha05") {
                artifact("foundation")
                artifact("material")
                artifact("runtime")

                group("ui") {
                    //artifact()
                    artifact("test")
                    artifact("tooling")
                }
            }

            artifact("core-ktx", "1.7.0-beta02")
            artifact("datastore-core", "1.0.0")
            artifact("fragment-ktx", "1.4.0-alpha10")

            group("lifecycle", "2.4.0-rc01") {
                artifact("common-java8")
                artifact("livedata-ktx")
                artifact("viewmodel-ktx")
            }

            group("navigation", "2.4.0-alpha10") {
                artifact("compose")
                artifact("runtime-ktx")
                artifact("ui-ktx")
            }

            artifact("paging-runtime", "3.1.0-alpha04")
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["versionCatalog"])
        }
    }
}
