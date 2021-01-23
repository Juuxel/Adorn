pluginManagement {
    repositories {
        jcenter()
        maven(url = "https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        gradlePluginPortal()
        maven {
            name = "JuuxelBintray"
            url = uri("https://dl.bintray.com/juuxel/maven")
        }
        maven {
            // Even though I don't use forgeloom features, I do use one other feature from my fork.
            name = "Forge"
            url = uri("https://files.minecraftforge.net/maven")
        }
    }
}

rootProject.name = "adorn"
