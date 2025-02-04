import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.task.RemapJarTask

plugins {
    // Apply the base plugin which mostly defines useful "build lifecycle" tasks like
    // assemble, check and build. The root project doesn't contain any code,
    // so we won't apply those plugins here. Only the assemble task is used in the root project.
    // See https://docs.gradle.org/current/userguide/base_plugin.html.
    base

    // Set up a specific version of Loom. There's no code in the root project,
    // so we don't need to apply it here.
    id("dev.architectury.loom") version "1.9.+" apply false
}

// Set up basic Maven artifact metadata, including the project version
// and archive names.
group = "io.github.juuxel"
// Set the project version to be <mod version>+<Minecraft version> so the MC version is semver build metadata.
// The "mod-version" and "minecraft-version" properties are read from gradle.properties.
version = "${project.property("mod-version")}+${project.property("minecraft-version")}"
base.archivesName.set("Adorn")

tasks {
    // Register a custom "collect jars" task that copies the Fabric and Forge mod jars
    // into the root project's build/libs. This makes it easier for me to find them
    // for testing and releasing.
    val collectJars by registering(Copy::class) {
        // Find the remapJar tasks of projects that aren't :common (so :fabric and :forge) and depend on them.
        val tasks = subprojects.filter { it.path != ":common" }.map { it.tasks.named("remapJar") }
        dependsOn(tasks)

        // Copy the outputs of the tasks...
        from(tasks)
        // ...into build/libs.
        into(layout.buildDirectory.dir("libs"))
    }

    // Set up assemble to depend on the collectJars task, so it gets run on gradlew build.
    assemble {
        dependsOn(collectJars)
    }

    // This is for IDEA. If "classes" doesn't exist, it runs "assemble" - which
    // builds the final project jars and is slow - when you press the hammer icon.
    register("classes")
}

// Do the shared setup for the Minecraft subprojects.
subprojects {
    apply(plugin = "dev.architectury.loom")

    // Find the loom extension. Since it's not applied to the root project, we can't access it directly
    // by name in this file.
    val loom = project.extensions.getByName<LoomGradleExtensionAPI>("loom")
    loom.mixin {
        useLegacyMixinAp.set(false)
    }

    // Set Java version.
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    // Copy the artifact metadata from the root project.
    group = rootProject.group
    version = rootProject.version
    base.archivesName.set(rootProject.base.archivesName)

    // Set up the custom "repository" for my Menu mappings.
    // (A mapping layer that replaces Yarn's "screen handler" with Mojang's own "menu". It's a long story.)
    repositories {
        // The exclusiveContent makes sure it's only used for io.github.juuxel:menu,
        // and only this repo is used for that module.
        exclusiveContent {
            forRepository {
                ivy {
                    url = uri("https://github.com/Juuxel/Menu/archive/refs/tags")
                    patternLayout {
                        artifact("[revision].zip")
                    }
                    metadataSources {
                        artifact()
                    }
                }
            }

            filter {
                includeModule("io.github.juuxel", "menu")
            }
        }

        // For Architectury and REI.
        maven {
            name = "Architectury"
            url = uri("https://maven.architectury.dev")
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

        // For JEI.
        maven {
            name = "Modrinth"
            url = uri("https://api.modrinth.com/maven")

            content {
                includeGroup("maven.modrinth")
            }
        }
    }

    dependencies {
        // Set the Minecraft dependency. The rootProject.property calls read from gradle.properties (and a variety of other sources).
        // Note that the configuration name has to be in quotes (a string) since Loom isn't applied to the root project,
        // and so the Kotlin accessor method for it isn't generated for this file.
        "minecraft"("net.minecraft:minecraft:${rootProject.property("minecraft-version")}")

        // Set up the layered mappings with Yarn, a NeoForge compatibility patch and my Menu mappings.
        "mappings"(loom.layered {
            mappings("net.fabricmc:yarn:${rootProject.property("minecraft-version")}+${rootProject.property("yarn-mappings")}:v2")
            mappings("dev.architectury:yarn-mappings-patch-neoforge:${rootProject.property("neoforge-mappings-patch-version")}")
            val menuVersion = rootProject.property("menu-mappings").toString()
            mappings("io.github.juuxel:menu:$menuVersion") {
                enigmaMappings()
                mappingPath("Menu-${menuVersion.replace('+', '-')}/mappings")
            }
        })
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
            options.release.set(21)
        }

        withType<net.fabricmc.loom.task.GenerateSourcesTask> {
            useCache.set(false)
        }

        // Include the license in the jar files.
        // See the dependencies section above for why this is in quotes.
        "jar"(Jar::class) {
            from(rootProject.file("LICENSE"))
        }

        // Make all archives reproducible.
        withType<AbstractArchiveTask> {
            isReproducibleFileOrder = true
            isPreserveFileTimestamps = false
        }
    }
}

// Set up "platform" subprojects (non-common subprojects).
subprojects {
    if (path != ":common") {
        fun Project.sourceSets() = extensions.getByName<SourceSetContainer>("sourceSets")

        // Set a different run directory for the server run config,
        // so it won't override client logs/config (or vice versa).
        extensions.configure<LoomGradleExtensionAPI> {
            // Generate IDE run configs for each run config.
            runs.configureEach {
                isIdeConfigGenerated = true
            }

            // Set a different run directory for the server so the log and config files don't conflict.
            runs.named("server") {
                runDir = "run/server"
            }

            // "main" matches the default NeoForge mod's name
            with(mods.maybeCreate("main")) {
                sourceSet(sourceSets().getByName("main"))
                sourceSet(project(":common").sourceSets().getByName("main"))
            }
        }

        tasks {
            "jar"(Jar::class) {
                from(project(":common").sourceSets().named("main").map { it.output })
            }

            "remapJar"(RemapJarTask::class) {
                // The project name will be "fabric" or "neoforge", so this will become the classifier/suffix
                // for the jar. For example: Adorn-3.4.0-fabric.jar
                archiveClassifier.set(project.name)
            }
        }
    }
}
