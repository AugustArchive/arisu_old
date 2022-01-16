package land.arisu.breb.core.builders

import land.arisu.breb.core.annotations.BrebDsl

/**
 * Represents a builder class for creating a new [GraphQL][land.arisu.breb.core.GraphQL] engine.
 */
@BrebDsl
class GraphQLBuilder {
    /**
     * Uses this [SchemaBuilder] to construct a new [Schema][land.arisu.breb.core.Schema].
     */
    var schema: SchemaBuilder = SchemaBuilder()
}
