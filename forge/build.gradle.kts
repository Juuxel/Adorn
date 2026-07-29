import juuxel.adorn.gradle.compatchecker.CheckModDataCompat
import juuxel.adorn.gradle.datagen.GenerateHoardNeoForgeModsToml

plugins {
    id("adorn-platform-module")
    id("adorn-data-generator")
}

loom {
    neoForge {
        // Convert the access widener to a NeoForge access transformer.
        convertAccessWideners(tasks.jar, "adorn.accesswidener")
    }
}

adorn {
    hoard {
        addResources(sourceSets.main.get(), project(":common").projectDir.resolve("src/generated/resources"))
        addResources(sourceSets.main.get(), project(":common").projectDir.resolve("src/hoard/other"))
        injectToNeoForgeMod()
    }

    platformModule {
        platformName = "neoforge"
        setupVersionTemplating("META-INF/neoforge.mods.toml")
    }
}

dependencies {
    // Add dependency on NeoForge. This is mainly used for generating the patched Minecraft jar with NeoForge classes.
    neoForge(libs.neoforge)

    // Bundle Jankson in the mod.
    include(libs.jankson)
    // Use Jankson as a library. Note that on Forge, regular non-mod libraries have to be declared
    // using forgeRuntimeLibrary as Forge reads the runtime classpath from a separately generated file.
    // In ForgeGradle projects, you might see a custom "library" configuration used for this.
    forgeRuntimeLibrary(libs.jankson)

    // Add regular mod dependency on REI - API for compile time and the mod itself for runtime.
    // modLocalRuntime won't be exposed if other mods depend on your mod unlike modRuntimeOnly.
    compileOnly(libs.rei.neoforge)
}

tasks {
    register<CheckModDataCompat>("checkModDataCompat") {
        configs.addAll(adorn.dataGenerator.settings.named("adorn").map { it.configs })
        mod("biomesoplenty", "biomes-o-plenty")
        // mod("biomeswevegone", "oh-the-biomes-weve-gone")
        mod("ecologics")
    }

    val generateModsToml = register<GenerateHoardNeoForgeModsToml>("generateHoardModsToml") {
        modId = adorn.hoard.modId
        version = adorn.hoard.version
        outputFile.set(file("build/hoard.neoforge.mods.toml"))
    }

    hoardJar {
        from(generateModsToml.flatMap { it.outputFile }) {
            into("META-INF")
            rename { "neoforge.mods.toml" }
        }

        from(project(":common").projectDir.resolve("src/main/resources/assets/adorn/icon.png")) {
            into(adorn.hoard.modId.map { "assets/$it" })
        }
    }

    check {
        dependsOn("checkModDataCompat")
    }
}
