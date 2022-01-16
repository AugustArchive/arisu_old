package land.arisu.breb.core.abstractions

import kotlin.reflect.KType

/**
 * Represents the object type.
 *
 * @param name The name of the callable resolver
 * @param args The arguments to call the resolver
 * @param returnType Returns the return type, if `Unit` is used,
 * it'll just be null and the result will be null.
 *
 * @param description Returns the description of this object, it needs
 * the [Description][land.arisu.breb.core.annotations.Description] annotation
 * before being used.
 *
 * @param deprecatedReason Returns the reason of why this object is deprecated,
 * requires the built-in [Deprecated] annotation.
 */
data class ResolverType(
    val name: String,
    val args: List<ResolverArguments> = listOf(),
    val returnType: KType? = null,
    val description: String? = null,
    val deprecatedReason: String? = null
)

/**
 * Represents a list of arguments to resolve the argument type.
 * @param key The key to use when debugging
 * @param index The index of the argument when running
 * @param returnType The [KType] of the argument, used for validation
 */
data class ResolverArguments(
    val key: String,
    val index: Int,
    val returnType: KType
)
