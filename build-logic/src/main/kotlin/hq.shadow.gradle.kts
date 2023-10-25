plugins {
    java
    id("com.github.johnrengelman.shadow")
}

tasks.shadowJar {
    archiveBaseName.set(project.rootProject.name)
    archiveVersion.set(project.rootProject.version.toString())
    archiveClassifier.set("")
    destinationDirectory.set(file("D:\\서버\\1.19.4 - 개발\\plugins"))
}

configurations.runtimeClasspath.get().apply {
    exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
}