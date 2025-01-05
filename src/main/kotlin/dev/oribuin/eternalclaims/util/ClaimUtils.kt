package dev.oribuin.eternalclaims.util

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.manager.Manager
import kotlin.reflect.KClass


/**
 * @return The instance of a plugin manager
 */
inline fun <reified T : Manager> RosePlugin.getManager(): T = this.getManager(T::class.java)

/**
 * Parse an enum from a string
 *
 * @param enumClass The enum class
 * @param value The string value
 * @return The enum value
 */
fun <T : Enum<T>> parseEnum(enumClass: KClass<T>, value: String?): T? {
    return try {
        enumClass.java.enumConstants.first { it.name.equals(value, true) } ?: null
    } catch (ex: Exception) {
        null
    }
}