import net.fabricmc.loom.task.RemapJarTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.40"
    idea
    id("fabric-loom") version "0.2.5-SNAPSHOT"
    `maven-publish`
    //id("com.github.johnrengelman.shadow") version "5.1.0"
}

group = "io.github.juuxel"

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

if (file("private.gradle").exists()) {
    apply(from = "private.gradle")
}

repositories {
    mavenCentral()
    if (localBuild) {
        mavenLocal()
    }

    maven(url = "http://server.bbkr.space:8081/artifactory/libs-release") { name = "Cotton" }
    maven(url = "http://server.bbkr.space:8081/artifactory/libs-snapshot") { name = "Cotton (snapshots)" }
    maven(url = "https://minecraft.curseforge.com/api/maven") { name = "CurseForge" }
    maven(url = "https://jitpack.io")
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

fun DependencyHandler.includedMod(str: String, block: ExternalModuleDependency.() -> Unit = {}) {
    modImplementation(str, block)
    include(str, block)
}

fun DependencyHandler.includedMod(group: String, name: String, version: String, block: ExternalModuleDependency.() -> Unit = {}) {
    modImplementation(group, name, version, dependencyConfiguration = block)
    include(group, name, version, dependencyConfiguration = block)
}

dependencies {
    /**
     * Gets a version string with the [key].
     */
    fun v(key: String) = project.properties[key].toString()

    fun ExternalModuleDependency.byeFabric() = exclude(group = "net.fabricmc.fabric-api")

    minecraft("com.mojang:minecraft:$minecraft")
    mappings("net.fabricmc:yarn:" + v("minecraft") + '+' + v("mappings"))

    // Fabric
    modImplementation("net.fabricmc:fabric-loader:" + v("fabric-loader"))
    modImplementation("net.fabricmc.fabric-api:fabric-api:" + v("fabric-api"))
    modApi("net.fabricmc:fabric-language-kotlin:" + v("fabric-kotlin"))
    compileOnly("net.fabricmc:fabric-language-kotlin:" + v("fabric-kotlin"))

    // Other mods
    includedMod("io.github.cottonmc:LibGui:" + v("libgui")) { isTransitive = false }
    includedMod("io.github.cottonmc:Jankson:" + v("jankson")) { byeFabric() }
    includedMod("io.github.cottonmc", "LibCD", v("libcd")) { byeFabric(); exclude(module = "Jankson") }
    modCompileOnly("towelette:Towelette:" + v("towelette")) { byeFabric() }
    modCompileOnly("io.github.prospector:modmenu:" + v("modmenu")) { byeFabric() }
    modCompileOnly("extra-pieces:extrapieces:" + v("extra-pieces"))
    modCompileOnly("com.github.artificemc:artifice:" + v("artifice"))

    if (heavyweight) {
        modRuntime("com.terraformersmc", "traverse", v("traverse")) { byeFabric() }
        modRuntime("com.terraformersmc", "terrestria", v("terrestria")) { byeFabric() }
        modRuntime("me.shedaniel", "RoughlyEnoughItems", v("rei")) { exclude(module = "jankson"); byeFabric() }
        modRuntime("towelette:Towelette:" + v("towelette")) { byeFabric() }
        modRuntime("statement:Statement:" + v("statement")) { byeFabric() }
        modRuntime("io.github.prospector:modmenu:" + v("modmenu")) { byeFabric() }
        modRuntime("extra-pieces:extrapieces:" + v("extra-pieces"))
        modRuntime("com.github.artificemc:artifice:" + v("artifice"))
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += "-Xuse-experimental=kotlin.Experimental"
}

val remapJar = tasks.getByName<RemapJarTask>("remapJar")
// Turns out I can't even use this version of Jankson, but I'll keep the code here if I ever need it.
/*val shadowJar = tasks.getByName<ShadowJar>("shadowJar")

remapJar.input.set(shadowJar.archiveFile.get())

shadowJar.relocate("blue.endless.jankson", "juuxel.adorn.repackage.jankson")*/

publishing {
    publications.create<MavenPublication>("maven") {
        artifactId = "adorn"

        artifact(remapJar) {
            classifier = null
            builtBy(remapJar)
        }
    }
}
