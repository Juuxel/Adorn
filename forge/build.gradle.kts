plugins {
    id("adorn-data-generator")
    id("adorn-minify-json")
    id("adorn-service-inline")
}

loom {
    // Make the Forge project use the common access widener.
    accessWidenerPath.set(project(":common").file("src/main/resources/adorn.accesswidener"))
}

repositories {
    // Set up NeoForge's Maven repository.
    maven("https://maven.neoforged.net/releases/")
}

dependencies {
    // Add dependency on NeoForge. This is mainly used for generating the patched Minecraft jar with NeoForge classes.
    neoForge(libs.neoforge)

    // Depend on the common project. The "namedElements" configuration contains the non-remapped
    // classes and resources of the project.
    // It follows Gradle's own convention of xyzElements for "outgoing" configurations like apiElements.
    implementation(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }

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
        archiveClassifier.set("neoforge")
    }

    processResources {
        // Mark that this task depends on the project version,
        // and should reset when the project version changes.
        inputs.property("version", project.version)

        // Replace the $version template in neoforge.mods.toml with the project version.
        filesMatching("META-INF/neoforge.mods.toml") {
            expand("version" to project.version)
        }
    }
}
