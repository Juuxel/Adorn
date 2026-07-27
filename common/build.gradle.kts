plugins {
    id("adorn-minecraft-setup")
    id("adorn-translations")
}

sourceSets {
    main {
        resources.srcDir("src/generated/resources")
        resources.exclude(".cache")
    }
}

loom {
    accessWidenerPath.set(file("src/main/resources/adorn.accesswidener"))
}

dependencies {
    implementation(libs.jankson)

    // Just for mixin deps :)
    implementation(libs.fabric.loader)

    // Add a mod dependency on some APIs for compat code.
    compileOnly(libs.rei.common)
    compileOnly(libs.jei.fabric)
    compileOnly(libs.rei.annotations)
}
