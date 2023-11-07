package io.github.pavelannin.keemun.dsl.platform

data class Enum(
    override val name: PlatformVariant<String>,
    val description: String?,
    val cases: List<Case>,
    val nestedTypes: List<AnyType>,
) : AnyType(name) {
    data class Case(val name: PlatformVariant<String>, val description: String?, val arguments: List<Argument>)
    data class Argument(val name: PlatformVariant<String>, val type: PlatformVariant<String>)
}

interface EnumDsl {
    fun enum(name: PlatformVariant<String>, scope: EnumScopeDsl.() -> Unit = {}): Enum

    fun enum(name: String, scope: EnumScopeDsl.() -> Unit = {}): Enum {
        return enum(name = any(name), scope = scope)
    }
}

class EnumBuilder : EnumDsl {
    private val _enums = mutableListOf<Enum>()
    val enums: List<Enum> get() = _enums.toList()

    override fun enum(name: PlatformVariant<String>, scope: EnumScopeDsl.() -> Unit): Enum {
        val scopeBuilder = EnumScopeBuilder().apply(scope)
        return Enum(
            name = name,
            description = scopeBuilder.description,
            cases = scopeBuilder.cases,
            nestedTypes = scopeBuilder.nestedAnyTypeBuilder.types
        ).apply(_enums::add)
    }
}

interface EnumScopeDsl : AnyTypeDsl {
    var description: String?

    fun case(name: PlatformVariant<String>, scope: EnumCaseScopeDsl.() -> Unit = {})

    fun case(name: String, scope: EnumCaseScopeDsl.() -> Unit = {}) {
        case(name = any(name), scope = scope)
    }
}

class EnumScopeBuilder(
    val nestedAnyTypeBuilder: AnyTypeBuilder = AnyTypeBuilder(),
) : EnumScopeDsl,
    AnyTypeDsl by nestedAnyTypeBuilder {
    private val _cases = mutableListOf<Enum.Case>()
    val cases: List<Enum.Case> get() = _cases.toList()

    override var description: String? = null

    override fun case(name: PlatformVariant<String>, scope: EnumCaseScopeDsl.() -> Unit) {
        val scopeBuilder = EnumCaseScopeBuilder().apply(scope)
        Enum.Case(
            name = name,
            description = scopeBuilder.description,
            arguments = scopeBuilder.arguments
        ).apply(_cases::add)
    }
}

interface EnumCaseScopeDsl {
    var description: String?

    fun argument(name: PlatformVariant<String>, type: PlatformVariant<String>)

    fun argument(name: String, type: String) {
        argument(name = any(name), type = any(type))
    }

    fun argument(name: String, type: AnyType) {
        argument(name = any(name), type = type.name)
    }

    fun argument(name: String, type: PlatformVariant<String>) {
        argument(name = any(name), type = type)
    }
}

class EnumCaseScopeBuilder : EnumCaseScopeDsl {
    private val _arguments = mutableListOf<Enum.Argument>()
    val arguments: List<Enum.Argument> get() = _arguments.toList()

    override var description: String? = null

    override fun argument(name: PlatformVariant<String>, type: PlatformVariant<String>) {
        Enum.Argument(
            name = name,
            type = type,
        ).apply(_arguments::add)
    }
}
