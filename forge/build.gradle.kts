plugins {
    kotlin("jvm")
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("io.github.juuxel.loom-quiltflower")
    id("org.jmailen.kotlinter")
    id("com.github.johnrengelman.shadow")
}

val shadowCommon by configurations.creating

val kotlinStdlib by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}

configurations {
    implementation {
        extendsFrom(kotlinStdlib)
    }

    forgeDependencies {
        extendsFrom(kotlinStdlib)
    }
}

architectury {
    platformSetupLoomIde()
    forge()
}

loom {
    accessWidenerPath.set(project(":common").file("src/main/resources/adorn.accesswidener"))

    forge {
        convertAccessWideners.set(true)
        extraAccessWideners.add("adorn.accesswidener")
        mixinConfigs("mixins.adorn.common.json", "mixins.adorn.forge.json")
    }
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

    kotlinStdlib(kotlin("stdlib-jdk8"))

    implementation(project(":common", configuration = "dev")) {
        isTransitive = false
    }
    "developmentForge"(project(":common", configuration = "dev")) {
        isTransitive = false
    }
    shadowCommon(project(path = ":common", configuration = "transformProductionForge")) {
        isTransitive = false
    }
}

kotlinter {
    disabledRules = arrayOf("parameter-list-wrapping")
}

tasks {
    shadowJar {
        archiveClassifier.set("dev-shadow")
        configurations = listOf(shadowCommon, kotlinStdlib)
    }

    remapJar {
        dependsOn(shadowJar)
        archiveClassifier.set("forge")
        input.set(shadowJar.flatMap { it.archiveFile })
    }

    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/mods.toml") {
            expand("version" to project.version)
        }
    }
}
