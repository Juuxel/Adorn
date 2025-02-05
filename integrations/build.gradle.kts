plugins {
    id("adorn-compat")
}

base {
    archivesName.set("AdornIntegrations")
}

compat {
    registerTargetMod("biomemakeover", "Biome Makeover", true, false)
    registerTargetMod("blockus", "Blockus", true, false)
    registerTargetMod("byg", "BYG", true, false)
    registerTargetMod("cinderscapes", "Cinderscapes", true, false)
    registerTargetMod("promenade", "Promenade", true, false)
    registerTargetMod("terrestria", "Terrestria", true, false)
    registerTargetMod("traverse", "Traverse", true, false)
    registerTargetMod("techreborn", "Tech Reborn", true, false)
    registerTargetMod("woods_and_mires", "Woods and Mires", true, false)
}

dependencies {
    implementation(project(":common", configuration = "namedElements"))

    compileOnly(libs.autoservice.annotations)
    annotationProcessor(libs.autoservice.processor)
}
