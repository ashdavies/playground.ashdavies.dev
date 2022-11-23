val artifactType: Attribute<String> = Attribute.of("artifactType", String::class.java)

configurations.all {
    if (isCanBeResolved && !isCanBeConsumed && isJvm()) {
        attributes.attribute(artifactType, "jar")
    }
}

dependencies {
    attributesSchema {
        val matchingStrategy = getMatchingStrategy(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE)
        matchingStrategy.compatibilityRules.add(AarCompatibility::class.java)
    }

    registerTransform(UnpackAar::class.java) {
        from.attribute(artifactType, "aar")
        to.attribute(artifactType, "jar")
    }
}

fun Configuration.isJvm(): Boolean {
    return name.contains("jvm", ignoreCase = true)
}
