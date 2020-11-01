import net.fabricmc.loom.task.RemapJarTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.4.0"
    idea
    id("fabric-loom") version "0.5-SNAPSHOT"
    `maven-publish`
    id("org.jmailen.kotlinter") version "3.2.0"
}

group = "io.github.juuxel"

base {
    archivesBaseName = "Adorn"
}

val minecraft: String by ext
version = "${project.properties["mod-version"]}+$minecraft"

repositories {
    mavenCentral()

    maven {
        name = "Cotton"
        url = uri("https://server.bbkr.space/artifactory/libs-release")
    }

    maven {
        name = "Jitpack"
        url = uri("https://jitpack.io")

        content {
            includeGroup("com.github.Virtuoel")
            includeGroup("com.github.Shnupbups")
        }
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
        expand("version" to project.version)
    }
}

loom {
    accessWidener = file("src/main/resources/adorn.accesswidener")
}

fun DependencyHandler.includedMod(notation: String, block: ExternalModuleDependency.() -> Unit = {}) {
    include(modImplementation(notation, block))
}

fun DependencyHandler.includedMod(group: String, name: String, version: String, block: ExternalModuleDependency.() -> Unit = {}) {
    include(modImplementation(group, name, version, dependencyConfiguration = block))
}

dependencies {
    /**
     * Gets a version string with the [key].
     */
    fun v(key: String) = project.properties[key].toString()

    fun ExternalModuleDependency.excludes() {
        exclude(group = "net.fabricmc.fabric-api")
        exclude(group = "curse.maven")
    }

    minecraft("com.mojang:minecraft:$minecraft")
    mappings("net.fabricmc:yarn:" + v("minecraft") + '+' + v("mappings") + ":v2")

    // Fabric
    modImplementation("net.fabricmc:fabric-loader:" + v("fabric-loader"))
    modImplementation("net.fabricmc.fabric-api:fabric-api:" + v("fabric-api"))
    modApi("net.fabricmc:fabric-language-kotlin:" + v("fabric-kotlin"))
    compileOnly("net.fabricmc:fabric-language-kotlin:" + v("fabric-kotlin"))

    // Other mods
    includedMod("io.github.cottonmc:LibGui:" + v("libgui")) { isTransitive = false }
    includedMod("io.github.cottonmc:Jankson-Fabric:" + v("jankson")) { excludes() }
    includedMod("io.github.cottonmc", "LibCD", v("libcd")) { excludes(); exclude(module = "Jankson"); exclude(module = "nbt-crafting") }
    modCompileOnly("com.github.Virtuoel:Towelette:" + v("towelette")) { excludes() }
    modCompileOnly("io.github.prospector:modmenu:" + v("modmenu")) { excludes() }
    modRuntime("io.github.prospector:modmenu:" + v("modmenu")) { excludes() }
    // Not actually a dev jar, see https://github.com/Shnupbups/extra-pieces/issues/45
    modCompileOnly("com.github.Shnupbups:extra-pieces:" + v("extra-pieces") + ":dev") { excludes() }
    modRuntime("me.shedaniel", "RoughlyEnoughItems", v("rei")) { exclude(module = "jankson"); excludes() }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += "-Xuse-experimental=kotlin.Experimental"
}

val remapJar = tasks.getByName<RemapJarTask>("remapJar")

kotlinter {
    disabledRules = arrayOf("parameter-list-wrapping")
}

publishing {
    publications.create<MavenPublication>("maven") {
        artifactId = "adorn"

        artifact(remapJar) {
            classifier = null
            builtBy(remapJar)
        }
    }
}
