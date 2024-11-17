plugins {
    kotlin("jvm") version "1.7.21"
}

group = "kr.cosine.parkour"
version = "1.2.0"

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://maven.hqservice.kr/repository/maven-public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    compileOnly("org.spigotmc", "spigot-api", "1.17.1-R0.1-SNAPSHOT")
    compileOnly("me.clip", "placeholderapi", "2.11.6")

    compileOnly("kr.hqservice", "hqframework-bukkit-core", "1.0.2-SNAPSHOT") {
        exclude("org.spigotmc", "spigot-api")
        exclude("io.papermc.paper", "paper-api")
    }
    compileOnly("kr.hqservice", "hqframework-bukkit-command", "1.0.2-SNAPSHOT") {
        exclude("org.spigotmc", "spigot-api")
        exclude("io.papermc.paper", "paper-api")
    }
    compileOnly("kr.hqservice", "hqgiftbox-api", "1.0.0")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("reflect"))
}

tasks {
    test {
        useJUnitPlatform()
    }
    jar {
        archiveFileName.set("${rootProject.name}-${rootProject.version}.jar")
        destinationDirectory.set(file("D:\\서버\\1.20.1 - 개발\\plugins"))
    }
}