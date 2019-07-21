import net.fabricmc.loom.task.RemapJarTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.40"
    idea
    id("fabric-loom") version "0.2.4-SNAPSHOT"
    `maven-publish`
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

        // For auto-config
        maven(url = "http://maven.sargunv.s3-website-us-west-2.amazonaws.com/")
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
    includedMod("io.github.cottonmc:LibGui:" + v("libgui"))
    includedMod("cloth-config:ClothConfig:" + v("cloth-config"))
    includedMod("me.sargunvohra.mcmods:auto-config:" + v("auto-config"))
    modImplementation("towelette:Towelette:" + v("towelette"))
    modRuntime("io.github.prospector.modmenu:ModMenu:" + v("modmenu"))

    // Other libraries
    compileOnly("org.apiguardian:apiguardian-api:1.0.0")
}

val remapJar: RemapJarTask by tasks.getting {}

publishing {
    publications.create<MavenPublication>("maven") {
        // Copied from the Cotton buildscript

        artifact("${project.buildDir.absolutePath}/libs/${base.archivesBaseName}-${project.version}.jar") { //release jar - file location not provided anywhere in loom
            classifier = null
            builtBy(remapJar)
        }

        artifact ("${project.buildDir.absolutePath}/libs/${base.archivesBaseName}-${project.version}-dev.jar") { //release jar - file location not provided anywhere in loom
            classifier = "dev"
            builtBy(remapJar)
        }

        /*artifact(sourcesJar) {
            builtBy remapSourcesJar
        }*/
    }
}
