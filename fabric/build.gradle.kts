plugins {
    id("adorn-data-generator")
    id("adorn-data-generator.emi")
}

architectury {
    // Create the IDE launch configurations for this subproject.
    platformSetupLoomIde()
    // Set up Architectury for Fabric.
    fabric()
}

// The files below are for using the access widener for the common project.
// We need to copy the file from common resources to fabric resource
// for Fabric Loader to find it and not crash.

// The generated resources directory for the AW.
val generatedResources = file("src/generated/resources")
// The path to the AW file in the common subproject.
val accessWidenerFile = project(":common").file("src/main/resources/adorn.accesswidener")

loom {
    // Make the Fabric project use the common access widener.
    // Technically useless, BUT this file is also needed at dev runtime of course
    accessWidenerPath.set(accessWidenerFile)
}

sourceSets {
    main {
        resources {
            // Mark the AW generated resource directory as
            // a source directory for the resources of
            // the "main" source set.
            srcDir(generatedResources)
        }
    }
}

// Set up various Maven repositories for mod compat.
repositories {
    // Jitpack for Virtuoel's mods (Towelette compat).
    maven {
        name = "Jitpack"
        url = uri("https://jitpack.io")

        // Since Jitpack is a very slow repository,
        // it's best to filter it to only include the artifacts we want.
        content {
            includeGroup("com.github.Virtuoel")
        }
    }

    // TerraformersMC maven for Mod Menu and EMI.
    maven {
        name = "TerraformersMC"
        url = uri("https://maven.terraformersmc.com/releases")

        content {
            includeGroup("com.terraformersmc")
            includeGroup("dev.emi")
        }
    }

    // DashLoader maven.
    maven {
        url = uri("https://oskarstrom.net/maven")

        content {
            includeGroup("net.oskarstrom")
        }
    }
}

dependencies {
    // Depend on the common project. The "namedElements" configuration contains the non-remapped
    // classes and resources of the project.
    // It follows Gradle's own convention of xyzElements for "outgoing" configurations like apiElements.
    implementation(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }
    // Used at dev runtime by the Architectury Transformer to automatically read changes in the common jar
    // and apply them.
    "developmentFabric"(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }
    // Bundle the transformed version of the common project in the mod.
    // The transformed version replaces all @ExpectPlatform calls to call the Fabric versions.
    bundle(project(path = ":common", configuration = "transformProductionFabric")) {
        isTransitive = false
    }

    // Standard Fabric mod setup.
    modImplementation("net.fabricmc:fabric-loader:${rootProject.property("fabric-loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${rootProject.property("fabric-api")}")
    modApi("net.fabricmc:fabric-language-kotlin:${rootProject.property("fabric-kotlin")}")

    // Bundle Jankson in the mod and use it as a regular "implementation" library.
    bundle(implementation("blue.endless:jankson:${rootProject.property("jankson")}")!!)

    // Mod compat
    modCompileOnly("com.github.Virtuoel:Towelette:${rootProject.property("towelette")}")
    modLocalRuntime(modCompileOnly("com.terraformersmc:modmenu:${rootProject.property("modmenu")}")!!)
    modCompileOnly("net.oskarstrom:DashLoader:${rootProject.property("dashloader")}")
    runtimeOnly("org.yaml:snakeyaml:1.27") // TODO: for dashloader, remove when pom is fixed
    modLocalRuntime(modCompileOnly("dev.emi:emi:${rootProject.property("emi")}") {
        isTransitive = false
    })
}

tasks {
    // The AW file is needed in :fabric project resources when the game is run.
    // This task simply copies it.
    val copyAccessWidener by registering(Copy::class) {
        from(accessWidenerFile)
        into(generatedResources)
    }

    shadowJar {
        relocate("blue.endless.jankson", "juuxel.adorn.relocated.jankson")
    }

    processResources {
        // Hook the AW copying and data generation to processResources.
        // Note: this is done differently in the other subprojects where the data generator
        // has to be run manually! TODO: make this consistent lol
        dependsOn(copyAccessWidener, generateData)
        // Mark that this task depends on the project version,
        // and should reset when the project version changes.
        inputs.property("version", project.version)

        // Replace the $version template in fabric.mod.json with the project version.
        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }

    generateEmi {
        val resourceDirs = project(":common").sourceSets.main.get().resources.srcDirs +
            sourceSets.main.get().resources.srcDirs
        for (dir in resourceDirs) {
            recipes.from(fileTree(dir) {
                include("data/adorn/recipes/**")

                // The unpacking recipes create "uncraftable" vanilla items like
                // nether wart, so exclude them.
                exclude("data/adorn/recipes/crates/unpack/**")
            })
        }
    }
}
