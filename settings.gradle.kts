pluginManagement {
    repositories {
        jcenter()
        maven(url = "https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        gradlePluginPortal()
        maven {
            name = "Forge"
            url = uri("https://files.minecraftforge.net/maven")
        }
        maven {
            name = "JuuxelBintray"
            url = uri("https://dl.bintray.com/juuxel/maven")
        }
    }
}

rootProject.name = "adorn"
