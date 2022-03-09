plugins {
    id("adorn-data-generator")
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
    forge("net.minecraftforge:forge:${rootProject.property("minecraft-version")}-${rootProject.property("forge-version")}")

    // Add Kotlin (see https://github.com/thedarkcolour/KotlinForForge/blob/70385f5/thedarkcolour/kotlinforforge/gradle/kff-3.0.0.gradle)
    implementation("thedarkcolour:kotlinforforge:${rootProject.property("kotlin-for-forge")}")
    // Without the manually specified versions, Loom's generateDLIConfig fails??
    forgeRuntimeLibrary(kotlin("stdlib-jdk8", version = "1.6.0"))
    forgeRuntimeLibrary(kotlin("reflect", version = "1.6.0"))

    implementation(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }
    "developmentForge"(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }
    bundle(project(path = ":common", configuration = "transformProductionForge")) {
        isTransitive = false
    }

    bundle("blue.endless:jankson:${rootProject.property("jankson")}")
    forgeRuntimeLibrary("blue.endless:jankson:${rootProject.property("jankson")}")
    modCompileOnly("me.shedaniel:RoughlyEnoughItems-api-forge:${rootProject.property("rei")}")
    modLocalRuntime("me.shedaniel:RoughlyEnoughItems-forge:${rootProject.property("rei")}")
}

tasks {
    shadowJar {
        relocate("blue.endless.jankson", "juuxel.adorn.relocated.jankson")
    }

    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/mods.toml") {
            expand("version" to project.version)
        }
    }
}
