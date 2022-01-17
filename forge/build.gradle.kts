plugins {
    id("adorn-datagen")
}

architectury {
    platformSetupLoomIde()
    forge()
}

loom {
    accessWidenerPath.set(project(":common").file("src/main/resources/adorn.accesswidener"))

    forge {
        convertAccessWideners.set(true)
        extraAccessWideners.add("adorn.accesswidener")
        mixinConfigs("mixins.adorn.common.json", "mixins.adorn.forge.json")
    }
}

datagen {
    forgeConditions()
    wood("byg:aspen")
    wood("byg:baobab")
    wood("byg:blue_enchanted")
    wood("byg:bulbis", fungus = true)
    wood("byg:cherry")
    wood("byg:cika")
    wood("byg:cypress")
    wood("byg:ebony")
    wood("byg:embur") {
        replace {
            "log" withId "byg:embur_pedu"
        }
    }
    wood("byg:ether")
    wood("byg:fir")
    wood("byg:green_enchanted")
    wood("byg:holly")
    wood("byg:imparius", fungus = true)
    wood("byg:jacaranda")
    wood("byg:lament")
    wood("byg:mahogany")
    wood("byg:mangrove")
    wood("byg:maple")
    wood("byg:nightshade")
    wood("byg:palm")
    wood("byg:pine")
    wood("byg:rainbow_eucalyptus")
    wood("byg:redwood")
    wood("byg:skyris")
    wood("byg:sythian", fungus = true)
    wood("byg:willow")
    wood("byg:witch_hazel")
    wood("byg:zelkova")
    stone("byg:dacite")
    stone("byg:dacite_brick", brick = true)
    stone("byg:dacite_cobblestone")
    stone("byg:mossy_stone")
    stone("byg:rocky_stone")
    stone("byg:scoria_stone", hasSidedTexture = true) {
        replace {
            "top-texture" with "byg:block/scoria_stone_top"
            "bottom-texture" with "byg:block/scoria_stone_top"
            "side-texture" with "byg:block/scoria_stone"
        }
    }
    stone("byg:scoria_cobblestone")
    stone("byg:scoria_stonebrick", brick = true) {
        replace {
            "main-texture" with "byg:block/scoria_stone_bricks"
        }
    }
    stone("byg:soapstone")
    stone("byg:polished_soapstone") {
        replace {
            "main-texture" with "byg:block/soapstone_polished"
        }
    }
    stone("byg:soapstone_brick", brick = true)
    stone("byg:soapstone_tile")
    stone("byg:red_rock")
    stone("byg:red_rock_brick", brick = true)
    stone("byg:mossy_red_rock_brick", brick = true)
    stone("byg:cracked_red_rock_brick", brick = true)
    stone("byg:chiseled_red_rock_brick", brick = true)
    // TODO: Stones declared in #walls
}

repositories {
    maven {
        name = "kotlinforforge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
    }
}

dependencies {
    forge("net.minecraftforge:forge:${rootProject.property("minecraft-version")}-${rootProject.property("forge-version")}")

    // Add Kotlin (see https://github.com/thedarkcolour/KotlinForForge/blob/70385f5/thedarkcolour/kotlinforforge/gradle/kff-3.0.0.gradle)
    implementation("thedarkcolour:kotlinforforge:${rootProject.property("kotlin-for-forge")}")
    forgeRuntimeLibrary(kotlin("stdlib-jdk8"))
    forgeRuntimeLibrary(kotlin("reflect"))

    implementation(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }
    "developmentForge"(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }
    bundle(project(path = ":common", configuration = "transformProductionForge")) {
        isTransitive = false
    }

    bundle("blue.endless:jankson:${rootProject.property("jankson")}")
    forgeRuntimeLibrary("blue.endless:jankson:${rootProject.property("jankson")}")
}

tasks {
    shadowJar {
        relocate("blue.endless.jankson", "juuxel.adorn.relocated.jankson")
    }

    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/mods.toml") {
            expand("version" to project.version)
        }
    }
}
