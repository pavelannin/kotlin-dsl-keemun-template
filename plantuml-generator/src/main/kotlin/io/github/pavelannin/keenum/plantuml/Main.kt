package io.github.pavelannin.keenum.plantuml

import io.github.pavelannin.keemun.dsl.Feature
import io.github.pavelannin.keemun.dsl.platform.Platform
import io.github.pavelannin.plantuml.generator.plantUml
import java.io.File
import javax.script.Compilable
import javax.script.ScriptEngineManager

fun main(args: Array<String>) {
    val dsl = args.dslFile().readText()
    val platforms = args.platform()

    val kotlinEngine = ScriptEngineManager().getEngineByExtension("kts")
    val feature = when (kotlinEngine) {
        is Compilable -> {
            println("Compiling script content")
            val compiledCode = kotlinEngine.compile(dsl)
            println("Running compiled script content")
            compiledCode.eval()
        }
        else -> throw IllegalStateException("Kotlin is not an instance of Compilable. Cannot compile script.")
    } as? Feature ?: error("The root dsl object must be feature(name) { }")

    platforms.forEach { platform ->
        val plantuml = generateClassDiagram(feature, platform = Platform.Android).plantUml()
        File("${feature.name}-${platform.suffix()}.puml").writeText(plantuml)
    }
}

private fun Array<String>.dslFile() = this.indexOf("--dsl")
    .takeIf { it != -1 }
    ?.let { this.getOrNull(index = it.inc()) }
    ?.let(::File)
    ?: error("--dsl is required argument")

private fun Array<String>.platform(default: Set<Platform> = Platform.values().toSet()) = this.indexOf("--platform")
    .takeIf { it != -1 }
    ?.let { this.getOrNull(index = it.inc()) }
    .let { argument ->
        when (argument) {
            "android" -> setOf(Platform.Android)
            "ios" -> setOf(Platform.IOS)
            "any" -> Platform.values().toSet()
            null -> default
            else -> error("Incorrect platform argument value")
        }
    }

private fun Platform.suffix() = when (this) {
    Platform.Android -> "android"
    Platform.IOS -> "ios"
}
