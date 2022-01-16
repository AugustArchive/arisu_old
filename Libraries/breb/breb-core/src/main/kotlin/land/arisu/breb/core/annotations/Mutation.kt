package land.arisu.breb.core.annotations

/**
 * Marks this callable function as a [Mutation].
 *
 * This mutation's name by default will use this callable function's name,
 * it cannot contain '`' as the name or it'll error out. If you wish to mark
 * this mutation's name yourself, you can use the [Mutation.name] property.
 */
@Target(AnnotationTarget.FUNCTION)
annotation class Mutation(
    /**
     * Marks this mutation's name to this. This will be overridded
     * by the callable function's name if this is a empty string.
     */
    val name: String = ""
)
