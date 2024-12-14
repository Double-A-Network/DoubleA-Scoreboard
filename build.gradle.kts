plugins {
    id("com.gradleup.shadow") version "8.3.5" // https://github.com/GradleUp/shadow
    `java-library`
    `maven-publish`
}

group = "com.andrew121410.mc"
version = "1.0"
description = "DoubleA-Scoreboard"

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

tasks {
    build {
        dependsOn("shadowJar")
        dependsOn("processResources")
    }

    jar {
        enabled = false
    }

    compileJava {
        options.encoding = "UTF-8"
    }

    shadowJar {
        archiveBaseName.set("DoubleA-Scoreboard")
        archiveClassifier.set("")
        archiveVersion.set("")

//        relocate("org.bstats", "com.andrew121410.mc.world16essentials.bstats")
    }
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }

    maven {
        url = uri("https://repo.essentialsx.net/releases/")
    }

    maven {
        url = uri("https://repo.essentialsx.net/snapshots/")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }

    maven {
        url = uri("https://repo.opencollab.dev/main/")
    }

    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("com.github.World1-6.World1-6Utils:World1-6Utils-Plugin:1145eed45b")
    compileOnly("org.geysermc.floodgate:api:2.2.3-SNAPSHOT")
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            artifact(tasks.named("shadowJar"))
        }
    }
}