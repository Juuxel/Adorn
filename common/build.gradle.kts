plugins {
    id("adorn-data-generator")
}

architectury {
    common(false)
}

loom {
    accessWidenerPath.set(file("src/main/resources/adorn.accesswidener"))
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("blue.endless:jankson:${rootProject.property("jankson")}")

    // Just for @Environment and mixin deps :)
    modImplementation("net.fabricmc:fabric-loader:${rootProject.property("fabric-loader")}")

    modCompileOnly("me.shedaniel:RoughlyEnoughItems-api:${rootProject.property("rei")}")
}

tasks {
    generateTags {
        rootProject.subprojects {
            configs.from(fileTree("src/data"))
        }
    }
}
