package io.github.pavelannin.keemun.dsl.platform

sealed class AnyType(open val name: PlatformVariant<String>)

interface AnyTypeDsl : StructDsl, EnumDsl

class AnyTypeBuilder(
    val structBuilder: StructBuilder = StructBuilder(),
    val enumBuilder: EnumBuilder = EnumBuilder(),
) : AnyTypeDsl,
    StructDsl by structBuilder,
    EnumDsl by enumBuilder {
    val types: List<AnyType> get() = structBuilder.structs + enumBuilder.enums
}
