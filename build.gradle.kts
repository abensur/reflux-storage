plugins {
    id("java")
    id("net.minecraftforge.gradle") version "6.0.35"
}

base {
    archivesName.set(project.properties["mod_name"].toString())
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

minecraft {
    mappings("official", project.properties["minecraft_version"].toString())

    runs {
        configureEach {
            workingDirectory(project.file("runs"))
            property("forge.logging.console.level", "info")

            mods {
                create(project.properties["mod_id"].toString()) {
                    source(sourceSets.main.get())
                }
            }
        }

        create("client")
        create("server") {
            args("--nogui")
        }
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        name = "Forge"
        url = uri("https://maven.minecraftforge.net/")
    }
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
    }
}

dependencies {
    minecraft("net.minecraftforge:forge:${project.properties["minecraft_version"]}-${project.properties["forge_version"]}")

    compileOnly(fg.deobf("maven.modrinth:curios:${project.properties["curios_version"]}"))
    runtimeOnly(fg.deobf("maven.modrinth:curios:${project.properties["curios_version"]}"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(17)
}
