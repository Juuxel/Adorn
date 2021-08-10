plugins {
    kotlin("jvm")
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("io.github.juuxel.loom-quiltflower")
    id("org.jmailen.kotlinter")
    id("com.github.johnrengelman.shadow")
}

val shadowCommon by configurations.creating

architectury {
    platformSetupLoomIde()
    forge()
}

repositories {
    maven {
        name = "kotlinforforge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
    }
}

dependencies {
    minecraft("net.minecraft:minecraft:${rootProject.property("minecraft-version")}")
    mappings("net.fabricmc:yarn:${rootProject.property("minecraft-version")}+${rootProject.property("mappings")}:v2")
    forge("net.minecraftforge:forge:${rootProject.property("minecraft-version")}-${rootProject.property("forge-version")}")

    implementation("thedarkcolour:kotlinforforge:${rootProject.property("kotlin-for-forge")}")

    implementation(project(":common")) {
        isTransitive = false
    }
    "developmentForge"(project(":common")) {
        isTransitive = false
    }
    shadowCommon(project(path = ":common", configuration = "transformProductionFabric")) {
        isTransitive = false
    }
}

tasks {
    shadowJar {
        archiveClassifier.set("dev-shadow")
        configurations = listOf(shadowCommon)
    }

    remapJar {
        dependsOn(shadowJar)
        archiveClassifier.set("forge")
        input.set(shadowJar.flatMap { it.archiveFile })
    }
}
