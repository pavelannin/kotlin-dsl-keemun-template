package io.github.pavelannin.keemun.dsl

import io.github.pavelannin.keemun.dsl.platform.*
import io.github.pavelannin.keemun.dsl.platform.Enum

fun feature(name: String, init: FeatureDsl.() -> Unit): Feature = FeatureBuilder()
    .apply(init)
    .let { builder ->
        Feature(
            name = name,
            description = builder.description,
            state = builder.state,
            externalMessage = builder.externalMessage,
            internalMessage = builder.internalMessage,
            effect = builder.effect,
            viewState = builder.viewState,
            inputEvent = builder.inputEvent,
            outputEvent = builder.outputEvent
        )
    }

data class Feature(
    val name: String,
    val description: String?,
    val state: Struct?,
    val externalMessage: Enum?,
    val internalMessage: Enum?,
    val effect: Enum?,
    val viewState: AnyType?,
    val inputEvent: Enum?,
    val outputEvent: Enum?,
)

interface FeatureDsl {
    var description: String?

    fun state(init: StructScopeDsl.() -> Unit)
    fun externalMessage(init: EnumScopeDsl.() -> Unit)
    fun internalMessage(init: EnumScopeDsl.() -> Unit)
    fun effect(init: EnumScopeDsl.() -> Unit)
    fun viewStateStruct(init: StructScopeDsl.() -> Unit)
    fun viewStateEnum(init: EnumScopeDsl.() -> Unit)
    fun inputEvent(init: EnumScopeDsl.() -> Unit)
    fun outputEvent(init: EnumScopeDsl.() -> Unit)
}

class FeatureBuilder(
    private val structBuilder: StructBuilder = StructBuilder(),
    private val enumBuilder: EnumBuilder = EnumBuilder(),
) : FeatureDsl {
    var state: Struct? = null
    var externalMessage: Enum? = null
    var internalMessage: Enum? = null
    var effect: Enum? = null
    var viewState: AnyType? = null
    var inputEvent: Enum? = null
    var outputEvent: Enum? = null

    override var description: String? = null

    override fun state(init: StructScopeDsl.() -> Unit) {
        state = structBuilder.struct(name = "State", scope = init)
    }

    override fun externalMessage(init: EnumScopeDsl.() -> Unit) {
        externalMessage = enumBuilder.enum(name = "ExternalMessage", scope = init)
    }

    override fun internalMessage(init: EnumScopeDsl.() -> Unit) {
        internalMessage = enumBuilder.enum(name = "InternalMessage", scope = init)
    }

    override fun effect(init: EnumScopeDsl.() -> Unit) {
        effect = enumBuilder.enum(name = "Effect", scope = init)
    }

    override fun viewStateStruct(init: StructScopeDsl.() -> Unit) {
        viewState = structBuilder.struct(name = "ViewState", scope = init)
    }

    override fun viewStateEnum(init: EnumScopeDsl.() -> Unit) {
        viewState = enumBuilder.enum(name = "ViewState", scope = init)
    }

    override fun inputEvent(init: EnumScopeDsl.() -> Unit) {
        inputEvent = enumBuilder.enum(name = "InputEvent", scope = init)
    }

    override fun outputEvent(init: EnumScopeDsl.() -> Unit) {
        outputEvent = enumBuilder.enum(name = "OutputEvent", scope = init)
    }
}
