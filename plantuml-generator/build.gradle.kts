import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.8.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

application {
    mainClass = "io.github.pavelannin.keenum.plantuml.MainKt"
}

tasks.withType<ShadowJar> {
    archiveFileName.set("keemun-plantuml.jar")
}

dependencies {
    implementation(project(":dsl"))
    implementation(deps.plantuml.generator)
    implementation(deps.kotlin.compiler.embeddable)
    implementation(deps.kotlin.reflect)
    implementation(deps.kotlin.script.runtime)
    implementation(deps.kotlin.script.util)
    implementation(deps.kotlin.script.jsr223)
}
