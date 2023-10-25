plugins {
    id("hq.shared")
}

dependencies {
    compileOnly(libs.spigot.api)

    compileOnly(framework.core)
    compileOnly(framework.command)

    compileOnly(project(":modules:api"))
}