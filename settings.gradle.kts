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
        maven {
            name = "Cotton"
            url = uri("https://server.bbkr.space/artifactory/libs-release/")
        }
        gradlePluginPortal()
    }
}

rootProject.name = "adorn"

// Set up the data generator as an included build (see https://docs.gradle.org/current/userguide/composite_builds.html).
// This allows it to be used as a Gradle plugin. The plugin is read from the included build automatically
// if it's requested, so we don't need to do a dependency substitution here.
includeBuild("datagen")

include(
    "common",
    "fabric",
    "forge",
)
