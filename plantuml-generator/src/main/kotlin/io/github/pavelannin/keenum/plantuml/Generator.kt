package io.github.pavelannin.keenum.plantuml

import io.github.pavelannin.keemun.dsl.Feature
import io.github.pavelannin.keemun.dsl.platform.*
import io.github.pavelannin.keemun.dsl.platform.Enum
import io.github.pavelannin.plantuml.dsl.class_diagram.ClassDiagramDsl
import io.github.pavelannin.plantuml.dsl.class_diagram.ClassDiagramUml
import io.github.pavelannin.plantuml.dsl.class_diagram.DeclaringElementUml
import io.github.pavelannin.plantuml.dsl.class_diagram.classDiagram

internal fun generateClassDiagram(
    feature: Feature,
    platform: Platform,
): ClassDiagramUml = classDiagram {
    val stateUml = feature.state?.let { convertToUml(anyType = it, platform) }
    val externalMessageUml = feature.externalMessage?.let { convertToUml(anyType = it, platform) }
    val internalMessageUml = feature.internalMessage?.let { convertToUml(anyType = it, platform) }
    val effectUml = feature.effect?.let { convertToUml(anyType = it, platform) }
    val viewStateUml = feature.viewState?.let { convertToUml(anyType = it, platform) }
    val inputEventUml = feature.inputEvent?.let { convertToUml(anyType = it, platform) }
    val outputEventUml = feature.outputEvent?.let { convertToUml(anyType = it, platform) }

    val userInterfaceUml = if (externalMessageUml != null || viewStateUml != null) circle(name = "UserInterface") else null
    val otherFeatureUml = if (inputEventUml != null || outputEventUml != null) circle(name = "OtherFeature") else null

    userInterfaceUml?.let { it directedDashedLine listOfNotNull(externalMessageUml) }
    externalMessageUml?.let { it directedDashedLine listOfNotNull(stateUml, effectUml) }
    internalMessageUml?.let { it directedDashedLine listOfNotNull(stateUml, effectUml) }
    effectUml?.let { it directedDashedLine listOfNotNull(internalMessageUml, outputEventUml) }
    stateUml?.let { it directedDashedLine listOfNotNull(viewStateUml) }
    viewStateUml?.let { it directedDashedLine listOfNotNull(userInterfaceUml) }
    outputEventUml?.let { it directedDashedLine listOfNotNull(otherFeatureUml) }
    inputEventUml?.let { it directedDashedLine listOfNotNull(effectUml) }
    otherFeatureUml?.let { it directedDashedLine listOfNotNull(inputEventUml) }
}

private fun ClassDiagramDsl.convertToUml(anyType: AnyType, platform: Platform): DeclaringElementUml = when (anyType) {
    is Enum -> enum(name = anyType.name.platform(platform)) {
        anyType.cases.map { case ->
            case(name = case.name.platform(platform)) {
                case.arguments.map { argument ->
                    argument(name = argument.name.platform(platform), type = argument.type.platform(platform))
                }
            }
        }
    } to anyType.nestedTypes.map { convertToUml(anyType = it, platform = platform) }

    is Struct ->
        `class`(name = anyType.name.platform(platform)) {
            anyType.properties.map { property ->
                property(name = property.name.platform(platform), type = property.type.platform(platform))
            }
        } to anyType.nestedTypes.map { convertToUml(anyType = it, platform = platform) }
}.also { (main, nested) -> main composition nested }.let { (main, _) -> main }
