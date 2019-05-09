import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.30"
    idea
    id("fabric-loom") version "0.2.1-SNAPSHOT"
}

base {
    archivesBaseName = "Adorn"
}

version = "0.2.0-pre.1+1.14"

allprojects {
    apply(plugin = "java")

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    repositories {
        mavenCentral()
        mavenLocal()

        // For cotton, polyester and json-factory
        maven(url = "http://server.bbkr.space:8081/artifactory/libs-release")
        maven(url = "http://server.bbkr.space:8081/artifactory/libs-snapshot")

        // For towelette
        maven(url = "https://minecraft.curseforge.com/api/maven") {
            name = "CurseForge"
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks.getByName<ProcessResources>("processResources") {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(
                mutableMapOf(
                    "version" to project.version
                )
            )
        }
    }
}

minecraft {
}

fun DependencyHandler.modCompileAndInclude(str: String) {
    modCompile(str)
    include(str)
}

dependencies {
    minecraft("com.mojang:minecraft:1.14.1 Pre-Release 1")
    mappings("net.fabricmc:yarn:1.14.1 Pre-Release 1+build.1")

    // Fabric
    modCompile("net.fabricmc:fabric-loader:0.4.6+build.141")
    modCompile("net.fabricmc:fabric:0.2.7+build.127")
    modCompile("net.fabricmc:fabric-language-kotlin:1.3.30-SNAPSHOT")
    compileOnly("net.fabricmc:fabric-language-kotlin:1.3.30-SNAPSHOT")

    // Other mods
    modCompileAndInclude("io.github.juuxel:polyester:0.3.0-menu.5+1.14.1-pre.1-SNAPSHOT")
    modCompileAndInclude("towelette:Towelette:1.5.2")
    modCompileAndInclude("io.github.cottonmc:cotton:0.6.4+1.14-SNAPSHOT")
}
