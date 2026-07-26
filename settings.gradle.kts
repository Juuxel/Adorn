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

plugins {
    // Set up a Loom version across the whole mod.
    // This also applies Loom's repositories to every Gradle project in the build.
    // Must match the version in build-logic/build.gradle.kts!
    id("dev.architectury.loom") version "1.17.+"
}

rootProject.name = "adorn"

// Set up the data generator and other Gradle plugins as an included build (see https://docs.gradle.org/current/userguide/composite_builds.html).
// This allows them to be accessed in the plugins block. The plugins are read from the included build automatically
// if they're requested, so we don't need to do a dependency substitution here.
includeBuild("build-logic")
includeBuild("datagen")

include(
    "common",
    "fabric",
    "forge",
)

// Set up custom repositories.
dependencyResolutionManagement {
    repositories {
        // For Architectury and REI.
        maven {
            name = "Architectury"
            url = uri("https://maven.architectury.dev")
        }

        // TerraformersMC maven for Mod Menu and EMI.
        maven {
            name = "TerraformersMC"
            url = uri("https://maven.terraformersmc.com/releases")

            content {
                includeGroup("com.terraformersmc")
                includeGroup("dev.emi")
            }
        }

        // For JEI.
        maven {
            name = "Modrinth"
            url = uri("https://api.modrinth.com/maven")

            content {
                includeGroup("maven.modrinth")
            }
        }

        // Set up NeoForge's Maven repository.
        maven("https://maven.neoforged.net/releases/")

        // Jitpack for Virtuoel's mods (Towelette compat).
        maven {
            name = "Jitpack"
            url = uri("https://jitpack.io")

            // Since Jitpack is a very slow repository,
            // it's best to filter it to only include the artifacts we want.
            content {
                includeGroup("com.github.Virtuoel")
            }
        }

        // For Kelp.
        exclusiveContent {
            forRepository {
                ivy {
                    url = uri("https://github.com/Juuxel/Kelp/releases/download/")
                    patternLayout {
                        artifact("[revision]/[artifact]-[revision].[ext]")
                    }
                    metadataSources {
                        artifact()
                    }
                }
            }

            filter {
                includeModule("io.github.juuxel", "kelp")
            }
        }
    }

    // To prevent any annoying issues, disable adding new repositories in projects altogether.
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
}
