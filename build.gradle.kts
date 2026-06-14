plugins {
    id("java")
    id("net.neoforged.gradle.userdev") version "7.1.37"
}

base {
    archivesName.set(project.properties["mod_name"].toString())
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

minecraft {
    accessTransformers.file("src/main/resources/META-INF/accesstransformer.cfg")
}

runs {
    create("client") {
        modSource(project.sourceSets.getByName("main"))
        systemProperty("forge.logging.console.level", "info")
    }
    create("server") {
        modSource(project.sourceSets.getByName("main"))
        programArgument("--nogui")
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        name = "NeoForged"
        url = uri("https://maven.neoforged.net/releases")
    }
    maven {
        name = "Illusive Soulworks"
        url = uri("https://maven.theillusivec4.top/")
    }
}

dependencies {
    implementation("net.neoforged:neoforge:${project.properties["neoforge_version"]}")
    compileOnly("top.theillusivec4.curios:curios-neoforge:9.5.1+1.21.1:api")
    runtimeOnly("top.theillusivec4.curios:curios-neoforge:9.5.1+1.21.1")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(21)
}
