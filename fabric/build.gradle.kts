plugins {
    id("adorn-platform-module")
    id("adorn-data-generator")
}

adorn {
    platformModule {
        setupVersionTemplating("fabric.mod.json")
    }
}

sourceSets {
    create("commonData") {
        compileClasspath += main.get().compileClasspath
        runtimeClasspath += main.get().runtimeClasspath
        compileClasspath += main.get().output
        runtimeClasspath += main.get().output
    }
}

fun registerDataGenerator(name: String, displayName: String, common: Boolean) {
    val targetProject = if (common) {
        project(":common")
    } else {
        project
    }

    loom.runs.register(name) {
        inherit(loom.runs.getByName("client"))
        configName = displayName
        source("commonData")
        property("fabric-api.datagen")
        property("fabric-api.datagen.output-dir", targetProject.file("src/generated/resources").absolutePath)
        runDir("build/$name")

        if (common) {
            property("adorn.data.commonMode", "true")
            property("adorn.data.mainConfigs", targetProject.file("src/data/vanilla.xml").absolutePath)
            val tagConfigDirs = rootProject.subprojects.map { it.file("src/data") }
            property("adorn.data.tagConfigDirs", tagConfigDirs.joinToString(File.pathSeparator) { it.absolutePath })
            property("adorn.data.fabricConfigDirs", project(":fabric").file("src/data").absolutePath)
            property("adorn.data.neoforgeConfigDirs", project(":forge").file("src/data").absolutePath)
        } else {
            property("adorn.data.mainConfigs", file("src/data").absolutePath)
        }
    }
}

registerDataGenerator("commonData", "Common Data Generator", common = true)
registerDataGenerator("data", "Fabric Data Generator", common = false)

sourceSets {
    main {
        resources {
            srcDir("src/generated/resources")
        }
    }
}

loom {
    mods {
        register("data") {
            sourceSet("commonData")
        }
    }
}

dependencies {
    // Standard Fabric mod setup.
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)

    // Bundle Jankson in the mod and use it as a regular "implementation" library.
    implementation(libs.jankson)
    include(libs.jankson)

    // Data generation
    "commonDataImplementation"("io.github.juuxel:adorn-data-generator")

    // Mod compat
    modCompileOnly(libs.towelette)
    modCompileOnly(libs.modmenu)
    modLocalRuntime(libs.modmenu)
    modCompileOnly(libs.emi.fabric) {
        isTransitive = false
    }
}
