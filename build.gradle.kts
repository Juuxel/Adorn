import net.fabricmc.loom.task.RemapJarTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.40"
    idea
    id("fabric-loom") version "0.2.5-SNAPSHOT"
    `maven-publish`
}

base {
    archivesBaseName = "Adorn"
}

apply(from = "load-heavyweight-mode.gradle")

val minecraft: String by ext
val modVersion = ext["mod-version"] ?: error("Version was null")
val localBuild = ext["local-build"].toString().toBoolean()
version = "$modVersion+$minecraft" + if (localBuild) "-local" else ""
val heavyweight = ext["heavyweight"].toString().toBoolean()

if (localBuild) {
    println("Note: local build mode enabled in gradle.properties; all dependencies might not work!")
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

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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

minecraft {
}

inline fun DependencyHandler.includedMod(str: String, block: ExternalModuleDependency.() -> Unit = {}) {
    modImplementation(str, block)
    include(str, block)
}

inline fun DependencyHandler.includedMod(group: String, name: String, version: String, block: ExternalModuleDependency.() -> Unit = {}) {
    modImplementation(group, name, version, dependencyConfiguration = block)
    include(group, name, version, dependencyConfiguration = block)
}

dependencies {
    /**
     * Gets a version string with the [key].
     */
    fun v(key: String) = ext[key].toString()

    minecraft("com.mojang:minecraft:$minecraft")
    mappings("net.fabricmc:yarn:" + v("minecraft") + '+' + v("mappings"))

    // Fabric
    modImplementation("net.fabricmc:fabric-loader:" + v("fabric-loader"))
    modImplementation("net.fabricmc.fabric-api:fabric-api:" + v("fabric-api"))
    modApi("net.fabricmc:fabric-language-kotlin:" + v("fabric-kotlin"))
    compileOnly("net.fabricmc:fabric-language-kotlin:" + v("fabric-kotlin"))

    // Other mods
    includedMod("io.github.cottonmc:LibGui:" + v("libgui")) { exclude(module = "modmenu"); exclude(group = "net.fabricmc.fabric-api") }
    includedMod("io.github.cottonmc:Jankson:" + v("jankson")) { exclude(group = "net.fabricmc.fabric-api") }
    includedMod("io.github.cottonmc", "LibCD", v("libcd")) { exclude(group = "net.fabricmc.fabric-api") }
    modImplementation("towelette:Towelette:" + v("towelette")) { exclude(group = "net.fabricmc.fabric-api") }
    modImplementation("io.github.prospector.modmenu:ModMenu:" + v("modmenu")) { exclude(group = "net.fabricmc.fabric-api") }

    if (heavyweight) {
        modRuntime("com.terraformersmc", "traverse", v("traverse")) { exclude(group = "net.fabricmc.fabric-api") }
        modRuntime("com.terraformersmc", "terrestria", v("terrestria")) { exclude(group = "net.fabricmc.fabric-api") }
        modRuntime("me.shedaniel", "RoughlyEnoughItems", v("rei")) { exclude(module = "jankson"); exclude(group = "net.fabricmc.fabric-api") }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += "-Xuse-experimental=kotlin.Experimental"
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
