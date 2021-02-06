import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.4.30"
    id("fabric-loom") version "0.6-SNAPSHOT"
    id("io.github.juuxel.ripple") version "0.3.6"
    `maven-publish`
    id("org.jmailen.kotlinter") version "3.2.0"
}

group = "io.github.juuxel"
version = "${project.property("mod-version")}+${project.property("minecraft-version")}"

base {
    archivesBaseName = "Adorn"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    withSourcesJar()
}

ripple.processor("src/menu.ripple.json")

loom {
    accessWidener = file("src/main/resources/adorn.accesswidener")

    runs.configureEach {
        val capitalizedName = if (name.length <= 1) name else name[0].toUpperCase() + name.substring(1)
        configName = "Fabric $capitalizedName"
    }
}

repositories {
    mavenCentral()
    jcenter()

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

dependencies {
    fun DependencyHandler.includedMod(notation: String, block: ExternalModuleDependency.() -> Unit = {}) {
        include(modImplementation(notation, block))
    }

    fun DependencyHandler.includedMod(group: String, name: String, version: String, block: ExternalModuleDependency.() -> Unit = {}) {
        include(modImplementation(group, name, version, dependencyConfiguration = block))
    }

    /**
     * Gets a version string with the [key].
     */
    fun v(key: String) = project.property(key).toString()

    minecraft("com.mojang:minecraft:${v("minecraft-version")}")
    mappings(ripple.processed("net.fabricmc:yarn:" + v("minecraft-version") + '+' + v("mappings") + ":v2", "adorn.1"))

    // Fabric
    modImplementation("net.fabricmc:fabric-loader:" + v("fabric-loader"))
    modImplementation("net.fabricmc.fabric-api:fabric-api:" + v("fabric-api"))
    modApi("net.fabricmc:fabric-language-kotlin:" + v("fabric-kotlin"))
    compileOnly("net.fabricmc:fabric-language-kotlin:" + v("fabric-kotlin"))

    // Other mods
    includedMod("io.github.cottonmc:LibGui:" + v("libgui"))
    includedMod("io.github.cottonmc:Jankson-Fabric:" + v("jankson"))
    includedMod("io.github.cottonmc", "LibCD", v("libcd"))
    modCompileOnly("com.github.Virtuoel:Towelette:" + v("towelette"))
    modCompileOnly("io.github.prospector:modmenu:" + v("modmenu"))
    modRuntime("io.github.prospector:modmenu:" + v("modmenu"))
    // Not actually a dev jar, see https://github.com/Shnupbups/extra-pieces/issues/45
    modCompileOnly("com.github.Shnupbups:extra-pieces:" + v("extra-pieces") + ":dev")
    modRuntime("me.shedaniel", "RoughlyEnoughItems", v("rei"))
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }
}

kotlinter {
    disabledRules = arrayOf("parameter-list-wrapping")
}

publishing {
    publications.create<MavenPublication>("maven") {
        artifactId = "adorn"

        artifact(tasks.remapJar)
    }
}
