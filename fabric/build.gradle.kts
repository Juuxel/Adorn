plugins {
    kotlin("jvm")
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("org.jmailen.kotlinter")
    id ("com.github.johnrengelman.shadow")
}

val shadowCommon by configurations.creating

architectury {
    platformSetupLoomIde()
    fabric()
}

val generatedResources = file("src/generated/resources")
val accessWidenerFile = project(":common").file("src/main/resources/adorn.accesswidener")

sourceSets {
    main {
        resources {
            srcDir(generatedResources)
        }
    }
}

repositories {
    maven {
        name = "Cotton"
        url = uri("https://server.bbkr.space/artifactory/libs-release")
    }

    maven {
        name = "Jitpack"
        url = uri("https://jitpack.io")

        content {
            includeGroup("com.github.Virtuoel")
        }
    }

    maven {
        name = "TerraformersMC"
        url = uri("https://maven.terraformersmc.com/releases")
    }
}

dependencies {
    minecraft("net.minecraft:minecraft:${rootProject.property("minecraft-version")}")
    mappings("net.fabricmc:yarn:${rootProject.property("minecraft-version")}+${rootProject.property("mappings")}:v2")

    implementation(project(":common")) {
        isTransitive = false
    }
    "developmentFabric"(project(":common")) {
        isTransitive = false
    }
    shadowCommon(project(path = ":common", configuration = "transformProductionFabric")) {
        isTransitive = false
    }

    modImplementation("net.fabricmc:fabric-loader:${rootProject.property("fabric-loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${rootProject.property("fabric-api")}")
    modApi("net.fabricmc:fabric-language-kotlin:${rootProject.property("fabric-kotlin")}")

    include(modImplementation("io.github.cottonmc:LibGui:${rootProject.property("libgui")}")!!)
    include(modImplementation("io.github.cottonmc:Jankson-Fabric:${rootProject.property("jankson")}")!!)
    include(modImplementation("io.github.cottonmc:LibCD:${rootProject.property("libcd")}")!!)

    // Mod compat
    modCompileOnly("com.github.Virtuoel:Towelette:${rootProject.property("towelette")}")
    modRuntime(modCompileOnly("com.terraformersmc:modmenu:${rootProject.property("modmenu")}")!!)
}

tasks {
    // The AW file is needed in :fabric project resources when the game is run.
    val copyAccessWidener by registering(Copy::class) {
        from(accessWidenerFile)
        into(generatedResources)
    }

    shadowJar {
        archiveClassifier.set("dev-shadow")
        configurations = listOf(shadowCommon)
    }

    remapJar {
        dependsOn(shadowJar)
        archiveClassifier.set("fabric")
        input.set(shadowJar.flatMap { it.archiveFile })
    }

    processResources {
        dependsOn(copyAccessWidener)
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }
}

kotlinter {
    disabledRules = arrayOf("parameter-list-wrapping")
}
