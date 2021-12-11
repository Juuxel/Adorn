architectury {
    common()
}

loom {
    accessWidenerPath.set(file("src/main/resources/adorn.accesswidener"))
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
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
