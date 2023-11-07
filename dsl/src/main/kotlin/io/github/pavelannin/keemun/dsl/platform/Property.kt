package io.github.pavelannin.keemun.dsl.platform

data class Property(
    val name: PlatformVariant<String>,
    val type: PlatformVariant<String>,
    val description: String?,
)

interface PropertyDsl {
    fun property(name: PlatformVariant<String>, type: PlatformVariant<String>, scope: PropertyScopeDsl.() -> Unit = {})

    fun property(name: String, type: String, scope: PropertyScopeDsl.() -> Unit = {}) {
        property(name = any(name), type = any(type), scope = scope)
    }

    fun property(name: String, type: AnyType, scope: PropertyScopeDsl.() -> Unit = {}) {
        property(name = any(name), type = type.name, scope = scope)
    }

    fun property(name: String, type: PlatformVariant<String>, scope: PropertyScopeDsl.() -> Unit = {}) {
        property(name = any(name), type = type, scope = scope)
    }
}

class PropertyBuilder : PropertyDsl {
    private val _properties = mutableListOf<Property>()
    val properties: List<Property> get() = _properties.toList()

    override fun property(name: PlatformVariant<String>, type: PlatformVariant<String>, scope: PropertyScopeDsl.() -> Unit) {
        val scopeBuilder = PropertyScopeBuilder().apply(scope)
        Property(
            name = name,
            type = type,
            description = scopeBuilder.description
        ).let(_properties::add)
    }
}

interface PropertyScopeDsl {
    var description: String?
}

class PropertyScopeBuilder : PropertyScopeDsl {
    override var description: String? = null
}

