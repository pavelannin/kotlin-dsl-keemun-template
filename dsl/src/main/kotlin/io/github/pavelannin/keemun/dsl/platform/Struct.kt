package io.github.pavelannin.keemun.dsl.platform

data class Struct(
    override val name: PlatformVariant<String>,
    val description: String?,
    val properties: List<Property>,
    val nestedTypes: List<AnyType>,
) : AnyType(name)

interface StructDsl {
    fun struct(name: PlatformVariant<String>, scope: StructScopeDsl.() -> Unit = {}): Struct

    fun struct(name: String, scope: StructScopeDsl.() -> Unit = {}): Struct {
        return struct(name = any(name), scope = scope)
    }
}

class StructBuilder : StructDsl {
    private val _structs = mutableListOf<Struct>()
    val structs: List<Struct> get() = _structs.toList()

    override fun struct(name: PlatformVariant<String>, scope: StructScopeDsl.() -> Unit): Struct {
        val scopeBuilder = StructScopeBuilder().apply(scope)
        return Struct(
            name = name,
            description = scopeBuilder.description,
            properties = scopeBuilder.propertyBuilder.properties,
            nestedTypes = scopeBuilder.nestedAnyType.types
        ).apply(_structs::add)
    }
}

interface StructScopeDsl : PropertyDsl, AnyTypeDsl {
    var description: String?
}

class StructScopeBuilder(
    val propertyBuilder: PropertyBuilder = PropertyBuilder(),
    val nestedAnyType: AnyTypeBuilder = AnyTypeBuilder(),
) : StructScopeDsl,
    PropertyDsl by propertyBuilder,
    AnyTypeDsl by nestedAnyType {
    override var description: String? = null
}
