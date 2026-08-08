pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

rootProject.name = "melonDS-android"

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":rcheevos-api")
