package io.github.pavelannin.keemun.dsl.platform

data class PlatformVariant<T>(val android: T, val iOS: T)

fun <T> any(value: T) = PlatformVariant(android = value, iOS = value)

fun <T> variant(android: T, iOS: T) = PlatformVariant(android = android, iOS = iOS)

enum class Platform { Android, IOS }

fun <T> PlatformVariant<T>.platform(platform: Platform): T = when (platform) {
    Platform.Android -> this.android
    Platform.IOS -> this.iOS
}
