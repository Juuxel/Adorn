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
    wood("minecraft:dark_oak")
    wood("minecraft:crimson", fungus = true)
    wood("minecraft:warped", fungus = true)
    stone("minecraft:andesite")
    stone("minecraft:basalt", hasSidedTexture = true) {
        exclude("recipes/platform")
        exclude("recipe_advancements/platform")
        exclude("recipes/step")
        exclude("recipe_advancements/step")

        replace {
            "side-texture" with "minecraft:block/basalt_side"
            "top-texture" with "minecraft:block/basalt_top"
            "bottom-texture" with "minecraft:block/basalt_top"
        }
    }
    stone("minecraft:blackstone")
    stone("minecraft:brick", brick = true)
    stone("minecraft:cobblestone")
    stone("minecraft:cut_red_sandstone", hasSidedTexture = true) {
        replace {
            "side-texture" with "minecraft:block/cut_red_sandstone"
            "top-texture" with "minecraft:block/red_sandstone_top"
            "bottom-texture" with "minecraft:block/red_sandstone_bottom"
        }
    }
    stone("minecraft:cut_sandstone", hasSidedTexture = true) {
        replace {
            "side-texture" with "minecraft:block/cut_sandstone"
            "top-texture" with "minecraft:block/sandstone_top"
            "bottom-texture" with "minecraft:block/sandstone_bottom"
        }
    }
    stone("minecraft:dark_prismarine")
    stone("minecraft:diorite")
    stone("minecraft:end_stone_brick", brick = true)
    stone("minecraft:granite")
    stone("minecraft:mossy_cobblestone")
    stone("minecraft:mossy_stone_brick", brick = true)
    stone("minecraft:nether_brick", brick = true)
    stone("minecraft:polished_andesite")
    stone("minecraft:polished_blackstone")
    stone("minecraft:polished_blackstone_brick", brick = true)
    stone("minecraft:polished_diorite")
    stone("minecraft:polished_granite")
    stone("minecraft:prismarine")
    stone("minecraft:prismarine_brick", brick = true)
    stone("minecraft:purpur") {
        replace {
            "planks" withId "minecraft:purpur_block"
        }
    }
    stone("minecraft:quartz", hasSidedTexture = true) {
        replace {
            "planks" withId "minecraft:quartz_block"
            "side-texture" with "minecraft:block/quartz_block_side"
            "top-texture" with "minecraft:block/quartz_block_top"
            "bottom-texture" with "minecraft:block/quartz_block_bottom"
        }
    }
    stone("minecraft:red_nether_brick", brick = true)
    stone("minecraft:red_sandstone", hasSidedTexture = true) {
        replace {
            "side-texture" with "minecraft:block/red_sandstone"
            "top-texture" with "minecraft:block/red_sandstone_top"
            "bottom-texture" with "minecraft:block/red_sandstone_bottom"
        }
    }
    stone("minecraft:sandstone", hasSidedTexture = true) {
        replace {
            "side-texture" with "minecraft:block/sandstone"
            "top-texture" with "minecraft:block/sandstone_top"
            "bottom-texture" with "minecraft:block/sandstone_bottom"
        }
    }
    stone("minecraft:smooth_red_sandstone") {
        replace {
            "main-texture" with "minecraft:block/red_sandstone_top"
        }
    }
    stone("minecraft:smooth_sandstone") {
        replace {
            "main-texture" with "minecraft:block/sandstone_top"
        }
    }
    stone("minecraft:smooth_stone")
    stone("minecraft:stone_brick", brick = true)
    stone("minecraft:stone")
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
