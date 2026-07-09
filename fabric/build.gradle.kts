import juuxel.adorn.gradle.compatchecker.CheckModDataCompat

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
        this.displayName = displayName
        sourceSet = "commonData"
        systemProperties.put("fabric-api.datagen", "")
        systemProperties.put("fabric-api.datagen.output-dir", targetProject.file("src/generated/resources").absolutePath)
        runDirectory = file("build/$name")

        if (common) {
            systemProperties.put("adorn.data.commonMode", "true")
            systemProperties.put("adorn.data.mainConfigs", targetProject.file("src/data/vanilla.xml").absolutePath)
            val tagConfigDirs = rootProject.subprojects.map { it.file("src/data") }
            systemProperties.put("adorn.data.tagConfigDirs", tagConfigDirs.joinToString(File.pathSeparator) { it.absolutePath })
        } else {
            systemProperties.put("adorn.data.mainConfigs", file("src/data").absolutePath)
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

tasks {
    register<CheckModDataCompat>("checkModDataCompat") {
        configs.addAll(adorn.dataGenerator.settings.named("adorn").map { it.configs })
        mod("biomesoplenty", "biomes-o-plenty")
        mod("biomeswevegone", "oh-the-biomes-weve-gone")
        mod("blockus")
        mod("cinderscapes")
        mod("techreborn")
        mod("terrestria")
        mod("traverse")
    }

    check {
        dependsOn("checkModDataCompat")
    }
}
