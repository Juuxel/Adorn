plugins {
    id("adorn-data-generator")
}

loom {
    // Make the Forge project use the common access widener.
    accessWidenerPath.set(project(":common").file("src/main/resources/adorn.accesswidener"))

    forge {
        // Enable Loom's AW -> AT conversion for Forge.
        // This will make remapJar convert the common AW to a Forge access transformer.
        convertAccessWideners.set(true)
        // Add an "extra" converted access widener which comes from outside the project.
        // The path is relative to the mod jar's root.
        extraAccessWideners.add("adorn.accesswidener")

        // Add mixin configs. Forge needs these explicitly declared
        // via Gradle; in Fabric, they're in fabric.mod.json.
        mixinConfigs("mixins.adorn.common.json", "mixins.adorn.forge.json")
    }
}

repositories {
    // Set up Kotlin for Forge's Maven repository.
    maven {
        name = "kotlinforforge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
    }
}

dependencies {
    // Add dependency on Forge. This is mainly used for generating the patched Minecraft jar with Forge classes.
    forge("net.minecraftforge:forge:${rootProject.property("minecraft-version")}-${rootProject.property("forge-version")}")

    // Add Kotlin for Forge.
    // Based on their own instructions: https://github.com/thedarkcolour/KotlinForForge/blob/70385f5/thedarkcolour/kotlinforforge/gradle/kff-3.0.0.gradle
    implementation("thedarkcolour:kotlinforforge:${rootProject.property("kotlin-for-forge")}")
    // Without the manually specified versions, Loom's generateDLIConfig fails??
    forgeRuntimeLibrary(kotlin("stdlib-jdk8", version = "1.6.0"))
    forgeRuntimeLibrary(kotlin("reflect", version = "1.6.0"))

    // Depend on the common and client classes of the common project.
    implementation(project(":common", configuration = "commonOutputs"))
    implementation(project(":common", configuration = "clientOutputs"))
    // Bundle the common project in the mod. The "namedElements" configuration contains the non-remapped
    // classes and resources of the project.
    // It follows Gradle's own convention of xyzElements for "outgoing" configurations like apiElements.
    bundle(project(path = ":common", configuration = "namedElements")) {
        isTransitive = false
    }

    // Bundle Jankson in the mod.
    bundle("blue.endless:jankson:${rootProject.property("jankson")}")
    // Use Jankson as a library. Note that on Forge, regular non-mod libraries have to be declared
    // using forgeRuntimeLibrary as Forge reads the runtime classpath from a separately generated file.
    // In ForgeGradle projects, you might see a custom "library" configuration used for this.
    forgeRuntimeLibrary("blue.endless:jankson:${rootProject.property("jankson")}")

    // Add regular mod dependency on REI - API for compile time and the mod itself for runtime.
    // modLocalRuntime won't be exposed if other mods depend on your mod unlike modRuntimeOnly.
    modCompileOnly("me.shedaniel:RoughlyEnoughItems-api-forge:${rootProject.property("rei")}")
    modLocalRuntime("me.shedaniel:RoughlyEnoughItems-forge:${rootProject.property("rei")}")
    modLocalRuntime("dev.architectury:architectury-forge:${rootProject.property("architectury-api")}")
}

tasks {
    compileJava {
        source(project(":common").sourceSets.getByName("mixin").allJava)
    }

    shadowJar {
        relocate("blue.endless.jankson", "juuxel.adorn.relocated.jankson")
    }

    processResources {
        // Mark that this task depends on the project version,
        // and should reset when the project version changes.
        inputs.property("version", project.version)

        from(project(":common").sourceSets.getByName("mixin").resources)

        // Replace the $version template in mods.toml with the project version.
        filesMatching("META-INF/mods.toml") {
            expand("version" to project.version)
        }
    }
}
