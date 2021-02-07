import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.30"
    id("fabric-loom") version "0.6-SNAPSHOT"
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

loom {
    accessWidener = file("src/main/resources/adorn.accesswidener")

    runs.configureEach {
        val capitalizedName = if (name.length <= 1) name else name[0].toUpperCase() + name.substring(1)
        configName = "Fabric $capitalizedName"
    }
}

repositories {
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

    maven {
        name = "TerraformersMC"
        url = uri("https://maven.terraformersmc.com/releases")
    }

    jcenter {
        content {
            includeGroup("com.lettuce.fudge")
        }
    }
}

dependencies {
    minecraft("net.minecraft:minecraft:${project.property("minecraft-version")}")
    mappings("net.fabricmc:yarn:${project.property("minecraft-version")}+${project.property("mappings")}:v2")

    modImplementation("net.fabricmc:fabric-loader:${project.property("fabric-loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric-api")}")
    modApi("net.fabricmc:fabric-language-kotlin:${project.property("fabric-kotlin")}")

    include(modImplementation("io.github.cottonmc:LibGui:${project.property("libgui")}")!!)
    include(modImplementation("io.github.cottonmc:Jankson-Fabric:${project.property("jankson")}")!!)
    include(modImplementation("io.github.cottonmc:LibCD:${project.property("libcd")}")!!)

    // Mod compat
    modCompileOnly("com.github.Virtuoel:Towelette:${project.property("towelette")}")
    modRuntime(modCompileOnly("com.terraformersmc:modmenu:${project.property("modmenu")}")!!)
    // Not actually a dev jar, see https://github.com/Shnupbups/extra-pieces/issues/45
    modCompileOnly("com.github.Shnupbups:extra-pieces:${project.property("extra-pieces")}:dev") {
        exclude(module = "RoughlyEnoughItems")
    }
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
