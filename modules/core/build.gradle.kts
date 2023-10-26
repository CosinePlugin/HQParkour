plugins {
    id("hq.shared")
}

dependencies {
    compileOnly(libs.spigot.api)

    compileOnly(framework.core) {
        exclude("org.spigotmc", "spigot-api")
    }
    compileOnly(framework.command) {
        exclude("org.spigotmc", "spigot-api")
    }

    compileOnly(project(":modules:api"))
}