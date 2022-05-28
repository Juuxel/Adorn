plugins {
    id("adorn-data-generator")
}

// The files below are for using the access widener for the common project.
// We need to copy the file from common resources to fabric resource
// for Fabric Loader to find it and not crash.

// The generated resources directory for the AW.
val generatedResources = file("src/generated/resources")
// The path to the AW file in the common subproject.
val accessWidenerFile = project(":common").file("src/main/resources/adorn.accesswidener")

loom {
    splitEnvironmentSourceSets()

    // Make the Fabric project use the common access widener.
    // Technically useless, BUT this file is also needed at dev runtime of course
    accessWidenerPath.set(accessWidenerFile)

    mods.getByName("main") {
        sourceSet(sourceSets.getByName("client"))
    }
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

    // TerraformersMC maven for Mod Menu.
    maven {
        name = "TerraformersMC"
        url = uri("https://maven.terraformersmc.com/releases")

        content {
            includeGroup("com.terraformersmc")
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
    // Depend on the common outputs the common project.
    implementation(project(":common", configuration = "commonOutputs"))
    // Bundle the common project in the mod. The "namedElements" configuration contains the non-remapped
    // classes and resources of the project.
    // It follows Gradle's own convention of xyzElements for "outgoing" configurations like apiElements.
    bundle(project(path = ":common", configuration = "namedElements")) {
        isTransitive = false
    }

    // Add client dependency on the common project's client classes and resources.
    "clientImplementation"(project(":common", configuration = "clientOutputs"))
    // Bundle client classes in the final jar. Since we need the shadow plugin,
    // Loom doesn't automatically handle it for us.
    bundle(sourceSets.getByName("client").output)

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
    modLocalRuntime("me.shedaniel:RoughlyEnoughItems-fabric:${rootProject.property("rei")}")
}

tasks {
    // The AW file is needed in :fabric project resources when the game is run.
    // This task simply copies it.
    val copyAccessWidener by registering(Copy::class) {
        from(accessWidenerFile)
        into(generatedResources)
    }

    compileJava {
        source(project(":common").sourceSets.getByName("mixin").allJava)
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

        from(project(":common").sourceSets.getByName("mixin").resources)

        // Replace the $version template in fabric.mod.json with the project version.
        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }
}
