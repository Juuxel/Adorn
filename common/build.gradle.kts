plugins {
    id("adorn-datagen")
}

architectury {
    common()
}

loom {
    accessWidenerPath.set(file("src/main/resources/adorn.accesswidener"))
}

datagen {
    wood("minecraft:oak")
    wood("minecraft:spruce")
    wood("minecraft:birch") {
        exclude("block_models/coffee_table")
    }
    wood("minecraft:jungle") {
        exclude("block_models/coffee_table")
    }
    wood("minecraft:acacia") {
        exclude("block_models/coffee_table")
    }
    wood("minecraft:crimson", fungus = true)
    wood("minecraft:warped", fungus = true)
    stone("minecraft:basalt", hasSidedTexture = true) {
        exclude("recipes/platform")
        exclude("recipe_advancements/platform")
        exclude("recipes/step")
        exclude("recipe_advancements/step")

        properties {
            "side-texture" with "minecraft:block/basalt_side"
            "top-texture" with "minecraft:block/basalt_top"
            "bottom-texture" with "minecraft:block/basalt_top"
        }
    }
    colorMaterials.set(true)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("blue.endless:jankson:${rootProject.property("jankson")}")

    // Just for @Environment and mixin deps :)
    modImplementation("net.fabricmc:fabric-loader:${rootProject.property("fabric-loader")}")
}

// PLEASE REMOVE AFTEREVALUATE FROM LOOM
afterEvaluate {
    tasks {
        remapJar {
            remapAccessWidener.set(false)
        }
    }
}
