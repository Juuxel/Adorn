import juuxel.adorn.gradle.compatchecker.CheckModDataCompat

plugins {
    id("adorn-platform-module")
    id("adorn-data-generator")
}

adorn {
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
    modCompileOnly(libs.rei.neoforge)
}

tasks {
    remapJar {
        // Convert the access widener to a NeoForge access transformer.
        atAccessWideners.add("adorn.accesswidener")
    }

    register<CheckModDataCompat>("checkModDataCompat") {
        configs.addAll(adorn.dataGenerator.settings.named("adorn").map { it.configs })
        mod("biomesoplenty", "biomes-o-plenty")
    }

    check {
        dependsOn("checkModDataCompat")
    }
}
