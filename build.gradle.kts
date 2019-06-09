import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.30"
    idea
    id("fabric-loom") version "0.2.4-SNAPSHOT"
}

base {
    archivesBaseName = "Adorn"
}

val minecraft: String by ext
val modVersion = ext["mod-version"] ?: error("Version was null")
val localBuild = ext["local-build"].toString().toBoolean()
version = "$modVersion+$minecraft" + if (localBuild) "-local" else ""

if (localBuild) {
    println("Note: local build mode enabled in gradle.properties; all dependencies might not work!")
}

allprojects {
    apply(plugin = "java")

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    repositories {
        mavenCentral()
        if (localBuild) {
            mavenLocal()
        }

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

inline fun DependencyHandler.includedMod(str: String, block: ExternalModuleDependency.() -> Unit = {}) {
    modImplementation(str, block)
    include(str, block)
}

dependencies {
    /**
     * Gets a version string with the [key].
     */
    fun v(key: String) = ext[key].toString()
    val excludeFabric: ExternalModuleDependency.() -> Unit = { exclude(module = "fabric-api") }

    minecraft("com.mojang:minecraft:$minecraft")
    mappings("net.fabricmc:yarn:" + v("minecraft") + '+' + v("mappings"))

    // Fabric
    modImplementation("net.fabricmc:fabric-loader:" + v("fabric-loader"))
    modImplementation("net.fabricmc.fabric-api:fabric-api:" + v("fabric-api"))
    modApi("net.fabricmc:fabric-language-kotlin:" + v("fabric-kotlin"))
    compileOnly("net.fabricmc:fabric-language-kotlin:" + v("fabric-kotlin"))

    // Other mods
    includedMod("io.github.juuxel:polyester:" + v("polyester"), excludeFabric)
    includedMod("towelette:Towelette:" + v("towelette"))
    includedMod("io.github.cottonmc:cotton:" + v("cotton"), excludeFabric)

    // Other libraries
    compileOnly("org.apiguardian:apiguardian-api:1.0.0")
}
