pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        maven {
            name = "Arch"
            url = uri("https://maven.architectury.dev/")
        }
        maven {
            name = "Forge"
            url = uri("https://maven.minecraftforge.net/")
        }
        gradlePluginPortal()
    }
}

rootProject.name = "adorn"

// Set up the data generator and other Gradle plugins as an included build (see https://docs.gradle.org/current/userguide/composite_builds.html).
// This allows them to be accessed in the plugins block. The plugins are read from the included build automatically
// if they're requested, so we don't need to do a dependency substitution here.
includeBuild("build-logic")

include(
    "common",
    "common:lens",
    "fabric",
    "forge",
)
