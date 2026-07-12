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

    // Just for @Environment and mixin deps :)
    modImplementation(libs.fabric.loader)

    // Add a mod dependency on some APIs for compat code.
    modCompileOnly(libs.rei.common)
    modCompileOnly(libs.emi.common)
    modCompileOnly(libs.jei.fabric)
    compileOnly(libs.rei.annotations)
}
