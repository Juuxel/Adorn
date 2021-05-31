pluginManagement {
    repositories {
        jcenter()
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        maven {
            name = "Arch"
            url = uri("https://maven.architectury.dev/")
        }
        maven {
            name = "Forge"
            url = uri("https://maven.minecraftforge.net/")
        }
        gradlePluginPortal()
    }
}

rootProject.name = "adorn"

include("common", "fabric")
