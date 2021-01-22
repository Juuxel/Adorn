import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.4.20"
    id("io.github.juuxel.fabric-loom") version "0.6.7"
    id("io.github.juuxel.ripple") version "0.3.6"
    `maven-publish`
    id("org.jmailen.kotlinter") version "3.2.0"
}

group = "io.github.juuxel"
version = "${project.property("mod-version")}+${project.property("minecraft-version")}-forge"

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
    mixinConfig = "mixins.adorn.json"
    useFabricMixin = true
}

repositories {
    maven {
        name = "kotlinforforge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
    }
}

dependencies {
    /**
     * Gets a version string with the [key].
     */
    fun v(key: String) = project.property(key).toString()

    minecraft("com.mojang:minecraft:${v("minecraft-version")}")
    mappings(ripple.processed("net.fabricmc:yarn:" + v("minecraft-version") + '+' + v("mappings") + ":v2", "adorn.1"))

    // Forge
    forge("net.minecraftforge", "forge", v("forge"))
    implementation("thedarkcolour", "kotlinforforge", v("kotlin-for-forge"))
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
