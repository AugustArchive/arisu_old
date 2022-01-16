package land.arisu.breb.core.annotations

/**
 * Marks a description of a function that is marked in an [AbstractResolver][land.arisu.breb.core.abstractions.AbstractResolver]
 */
annotation class Description(
    /**
     * Returns the description itself :3
     */
    val message: String = ""
)
