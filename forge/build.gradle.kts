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
    implementation("thedarkcolour:kotlinforforge:${rootProject.property("kotlin-for-forge")}")

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
