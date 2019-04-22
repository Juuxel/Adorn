import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.30"
    idea
    id("fabric-loom") version "0.2.1-SNAPSHOT"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

base {
    archivesBaseName = "Adorn"
}

repositories {
    mavenCentral()
    mavenLocal()

    // For json-factory
    maven(url = "http://server.bbkr.space:8081/artifactory/libs-release")
    maven(url = "http://server.bbkr.space:8081/artifactory/libs-snapshot")

    // For polyester
    maven(url = "https://jitpack.io")

    // For towelette
    maven(url = "https://minecraft.curseforge.com/api/maven") {
        name = "CurseForge"
    }
}

val shortVersion = "0.1.0"
version = "$shortVersion+1.14-pre5"

minecraft {
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

configurations {
    create("shadow")
    this["compile"].extendsFrom(this["shadow"])
}

dependencies {
    minecraft("com.mojang:minecraft:1.14 Pre-Release 5")
    mappings("net.fabricmc:yarn:1.14 Pre-Release 5+build.4")

    // Fabric
    modCompile("net.fabricmc:fabric-loader:0.4.2+build.131")
    modCompile("net.fabricmc:fabric:0.2.7+build.126")
    modCompile("net.fabricmc:fabric-language-kotlin:1.3.30-SNAPSHOT")
    compileOnly("net.fabricmc:fabric-language-kotlin:1.3.30-SNAPSHOT")

    // Other mods
    modCompile("com.github.Juuxel:Polyester:739dab2cf6")
    modCompile("towelette:Towelette:1.5.0")

    // Other libraries
    implementation("io.github.cottonmc:json-factory:0.5.0-local.2-SNAPSHOT")
    implementation("io.github.cottonmc:json-factory-gui:0.5.0-local.2-SNAPSHOT")
}

tasks.withType<Jar> {
    from(configurations["shadow"].asFileTree.files.map { zipTree(it) })
}

tasks.getByName<ProcessResources>("processResources") {
    filesMatching("fabric.mod.json") {
        expand(
            mutableMapOf(
                "version" to shortVersion
            )
        )
    }
}
