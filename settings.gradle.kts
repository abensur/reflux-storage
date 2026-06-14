pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            name = "NeoForged"
            url = uri("https://maven.neoforged.net/releases")
        }
        maven {
            name = "Forge"
            url = uri("https://maven.minecraftforge.net/")
        }
    }
}

rootProject.name = "reflux-storage"
