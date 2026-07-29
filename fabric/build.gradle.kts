import juuxel.adorn.gradle.CorePlugin
import juuxel.adorn.gradle.compatchecker.CheckModDataCompat
import net.fabricmc.loom.task.FabricModJsonV1Task

plugins {
    id("adorn-split-sources")
    id("adorn-platform-module")
    id("adorn-data-generator")
}

sourceSets {
    create("commonData") {
        for (parentName in listOf("main", "client")) {
            val parent = getByName(parentName)
            compileClasspath += parent.compileClasspath
            runtimeClasspath += parent.runtimeClasspath
            compileClasspath += parent.output
            runtimeClasspath += parent.output
        }
    }
}

adorn {
    hoard {
        addResources(sourceSets.main.get(), "src/generated/resources")
        addResources(sourceSets.main.get(), "src/hoard/other")
        addResources(sourceSets.main.get(), project(":common").projectDir.resolve("src/generated/resources"))
        addResources(sourceSets.main.get(), project(":common").projectDir.resolve("src/hoard/other"))
        injectToFabricMod()
    }

    minecraft {
        generatePackageInfos(sourceSets.getByName("client"))
        generatePackageInfos(sourceSets.getByName("commonData"))
    }

    platformModule {
        setupVersionTemplating("fabric.mod.json")
    }
}

fun registerDataGenerator(name: String, displayName: String, common: Boolean) {
    val projectDir = if (common) {
        project(":common")
    } else {
        project
    }.projectDir

    loom.runs.register(name) {
        inherit(loom.runs.getByName("client"))
        this.displayName = displayName
        sourceSet = "commonData"
        systemProperties.put("fabric-api.datagen", "")
        systemProperties.put("fabric-api.datagen.output-dir", projectDir.resolve("src/generated/resources").absolutePath)
        runDirectory = file("build/$name")

        if (common) {
            systemProperties.put("adorn.data.commonMode", "true")
            systemProperties.put("adorn.data.mainConfigs", projectDir.resolve("src/data/vanilla.xml").absolutePath)
            systemProperties.put("adorn.data.tagConfigDirs", rootProject.subprojects.joinToString(File.pathSeparator) {
                it.projectDir.resolve("src/data").absolutePath
            })
            systemProperties.put("adorn.data.fabricConfigDirs", file("src/data").absolutePath)
            systemProperties.put("adorn.data.neoforgeConfigDirs", project(":forge").projectDir.resolve("src/data").absolutePath)
        } else {
            systemProperties.put("adorn.data.mainConfigs", file("src/data").absolutePath)
        }
    }
}

registerDataGenerator("commonData", "Common Data Generator", common = true)
registerDataGenerator("data", "Fabric Data Generator", common = false)

loom {
    mods {
        register("data") {
            sourceSet("commonData")
        }
    }
}

dependencies {
    // Standard Fabric mod setup.
    implementation(libs.fabric.loader)
    implementation(libs.fabric.api)

    // Bundle Jankson in the mod and use it as a regular "implementation" library.
    implementation(libs.jankson)
    include(libs.jankson)

    // Data generation
    "commonDataImplementation"("io.github.juuxel:adorn-data-generator")

    // Mod compat
    // compileOnly(libs.towelette)
    "clientCompileOnly"(libs.modmenu)
    localRuntime(libs.modmenu)
}

tasks {
    register<CheckModDataCompat>("checkModDataCompat") {
        configs.addAll(adorn.dataGenerator.settings.named("adorn").map { it.configs })
        mod("biomesoplenty", "biomes-o-plenty")
        // mod("biomeswevegone", "oh-the-biomes-weve-gone")
        mod("blockus")
        // mod("cinderscapes")
        mod("ecologics")
        // mod("promenade")
        // mod("terrestria")
        mod("traverse")
    }

    val hoardFmj = register<FabricModJsonV1Task>("generateHoardFabricModJson") {
        group = CorePlugin.TASK_GROUP
        json {
            modId = adorn.hoard.modId
            name = "Adorn Resources"
            version = adorn.hoard.version
            description = "Adorn resources."
            author("Juuz")
            licenses.add("MIT")
            icon {
                path = modId.map { "assets/$it/icon.png" }
            }
            contactInformation.putAll(
                mapOf(
                    "homepage" to "https://modrinth.com/mod/adorn",
                    "sources" to "https://github.com/Juuxel/Adorn",
                    "issues" to "https://github.com/Juuxel/Adorn/issues",
                )
            )
            customData.put("modmenu", mapOf("parent" to "adorn"))
        }
        outputFile.set(file("build/hoard.fabric.mod.json"))
    }

    hoardJar {
        from(hoardFmj.flatMap { it.outputFile }) {
            rename { "fabric.mod.json" }
        }

        from(project(":common").projectDir.resolve("src/main/resources/assets/adorn/icon.png")) {
            into(adorn.hoard.modId.map { "assets/$it" })
        }
    }

    check {
        dependsOn("checkModDataCompat")
    }
}
