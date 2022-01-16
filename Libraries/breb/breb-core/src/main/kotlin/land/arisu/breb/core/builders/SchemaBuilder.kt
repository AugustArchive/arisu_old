package land.arisu.breb.core.builders

/**
 * Represents a builder class for creating a new [Schema][land.arisu.breb.core.Schema].
 */
class SchemaBuilder {
    /**
     * Represents all the object types to register
     */
    var objectTypes: List<Any> = listOf()

    /**
     * Represents all the input types to register
     */
    var inputTypes: List<Any> = listOf()

    /**
     * Represents all the unions to register
     */
    var unions: List<Any> = listOf()
}
