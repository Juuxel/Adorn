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

    // For cotton and json-factory
    maven(url = "http://server.bbkr.space:8081/artifactory/libs-release")
    maven(url = "http://server.bbkr.space:8081/artifactory/libs-snapshot")

    // For polyester
    maven(url = "https://jitpack.io")

    // For towelette
    maven(url = "https://minecraft.curseforge.com/api/maven") {
        name = "CurseForge"
    }

    // For LBA
    maven(url = "https://mod-buildcraft.com/maven") {
        name = "BuildCraft"
    }
}

val shortVersion = "0.1.0"
version = "$shortVersion+1.14"

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
    minecraft("com.mojang:minecraft:1.14")
    mappings("net.fabricmc:yarn:1.14+build.1")

    // Fabric
    modCompile("net.fabricmc:fabric-loader:0.4.2+build.132")
    modCompile("net.fabricmc:fabric:0.2.7+build.127")
    modCompile("net.fabricmc:fabric-language-kotlin:1.3.30-SNAPSHOT")
    compileOnly("net.fabricmc:fabric-language-kotlin:1.3.30-SNAPSHOT")

    // Other mods
    modCompile("com.github.Juuxel:Polyester:739dab2cf6")
    include("com.github.Juuxel:Polyester:739dab2cf6")
    modCompile("towelette:Towelette:1.5.0")
    modCompile("alexiil.mc.lib:libblockattributes:0.4.0")
    include("alexiil.mc.lib:libblockattributes:0.4.0")
    modCompile("io.github.cottonmc:cotton:0.6.1+1.14-SNAPSHOT")

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
