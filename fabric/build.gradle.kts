plugins {
    id("adorn-datagen")
}

architectury {
    platformSetupLoomIde()
    fabric()
}

val generatedResources = file("src/generated/resources")
val accessWidenerFile = project(":common").file("src/main/resources/adorn.accesswidener")

loom {
    // Technically useless, BUT this is also needed at dev runtime of course
    accessWidenerPath.set(accessWidenerFile)
}

datagen {
    libcdConditions()
    wood("biomemakeover:blighted_balsa")
    wood("biomemakeover:swamp_cypress")
    wood("biomemakeover:willow")
    wood("byg:aspen")
    wood("byg:baobab")
    wood("byg:blue_enchanted")
    wood("byg:bulbis", fungus = true)
    wood("byg:cherry")
    wood("byg:cika")
    wood("byg:cypress")
    wood("byg:ebony")
    wood("byg:embur")
    wood("byg:ether")
    wood("byg:fir")
    wood("byg:green_enchanted")
    wood("byg:holly")
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
    wood("cinderscapes:scorched", fungus = true)
    wood("cinderscapes:umbral", fungus = true)
    wood("terrestria:cypress")
    wood("terrestria:hemlock")
    wood("terrestria:japanese_maple")
    wood("terrestria:rainbow_eucalyptus")
    wood("terrestria:redwood")
    wood("terrestria:rubber")
    wood("terrestria:sakura") {
        exclude("block_models/bench_leg")
        exclude("block_models/coffee_table")
        exclude("block_models/table_leg")
        exclude("item_models/bench")
        exclude("item_models/table")
    }
    wood("terrestria:willow")
    wood("terrestria:yucca_palm") {
        exclude("block_models/bench_leg")
        exclude("item_models/bench")
    }
    stone("terrestria:basalt")
    stone("terrestria:basalt_brick", brick = true)
    stone("terrestria:basalt_cobblestone")
    stone("terrestria:mossy_basalt_brick", brick = true)
    stone("terrestria:mossy_basalt_cobblestone")
    stone("terrestria:smooth_basalt")
    wood("traverse:fir")
    wood("woods_and_mires:pine") {
        exclude("block_models/kitchen_counter")
        exclude("block_models/kitchen_sink")
        exclude("item_models/kitchen_cupboard")
        exclude("recipes/kitchen_counter")
    }
}

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

        content {
            includeGroup("com.terraformersmc")
        }
    }

    maven {
        url = uri("https://oskarstrom.net/maven")

        content {
            includeGroup("net.oskarstrom")
        }
    }
}

dependencies {
    implementation(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }
    "developmentFabric"(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }
    bundle(project(path = ":common", configuration = "transformProductionFabric")) {
        isTransitive = false
    }

    modImplementation("net.fabricmc:fabric-loader:${rootProject.property("fabric-loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${rootProject.property("fabric-api")}")
    modApi("net.fabricmc:fabric-language-kotlin:${rootProject.property("fabric-kotlin")}")

    include(modImplementation("io.github.cottonmc:Jankson-Fabric:${rootProject.property("jankson-fabric")}")!!)
    include(modImplementation("io.github.cottonmc:LibCD:${rootProject.property("libcd")}")!!)

    // Mod compat
    modCompileOnly("com.github.Virtuoel:Towelette:${rootProject.property("towelette")}")
    modLocalRuntime(modCompileOnly("com.terraformersmc:modmenu:${rootProject.property("modmenu")}")!!)
    modCompileOnly("net.oskarstrom:DashLoader:${rootProject.property("dashloader")}")
    runtimeOnly("org.yaml:snakeyaml:1.27") // TODO: for dashloader, remove when pom is fixed
}

tasks {
    // The AW file is needed in :fabric project resources when the game is run.
    val copyAccessWidener by registering(Copy::class) {
        from(accessWidenerFile)
        into(generatedResources)
    }

    processResources {
        dependsOn(copyAccessWidener)
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }
}
