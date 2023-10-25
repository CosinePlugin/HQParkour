plugins {
    id("hq.shared")
    id("hq.shadow")
    id("kr.hqservice.resource-generator.bukkit")
}

bukkitResourceGenerator {
    val pluginName = extra["projectName"].toString()
    main = "${extra["projectGroup"]}.$pluginName"
    name = pluginName
    apiVersion = "1.18"
    depend = listOf("HQFramework")
    libraries = excludedRuntimeDependencies()
}

dependencies {
    compileOnly(libs.spigot.api)
    compileOnly(framework.core)
    runtimeOnly(project(":modules:core"))
    runtimeOnly(project(":modules:api"))
}