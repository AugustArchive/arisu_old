package land.arisu.breb.core.abstractions

import land.arisu.breb.core.annotations.Description
import land.arisu.breb.core.annotations.Mutation
import land.arisu.breb.core.annotations.Query
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

/**
 * Returns an abstraction of a resolver. A resolver is a object
 * that resolves data from a query or mutation (subscriptions aren't
 * supported yet:tm:). This is way more maintainable than using a DSL
 * for your queries / mutations and doesn't require any data loading, you do that
 * yourself! :D
 */
class AbstractResolver {
    /**
     * Returns all of the [ResolverType]s of the mutations available in this
     * resolver.
     */
    val mutations: List<ResolverType>
        get() = this::class.members.filter {
            it.hasAnnotation<Mutation>()
        }.map {
            ResolverType(
                name = it.name,
                returnType = it.returnType,
                args = if (it.parameters.isEmpty())
                    listOf()
                else
                    it
                        .parameters
                        .filter { p -> !p.isVararg && !p.isVararg && p.name != null }
                        .map { p -> ResolverArguments(p.name!!, p.index, p.type) },

                deprecatedReason = if (it.hasAnnotation<Deprecated>())
                    it.findAnnotation<Deprecated>()!!.message
                else
                    "",

                description = if (it.hasAnnotation<Description>())
                    it.findAnnotation<Deprecated>()!!.message
                else
                    ""
            )
        }

    /**
     * Returns all of the [ResolverType]s of the queries available in this
     * resolver.
     */
    val queries: List<ResolverType>
        get() = this::class.members.filter {
            it.hasAnnotation<Query>()
        }.map {
            ResolverType(
                name = it.name,
                returnType = it.returnType,
                args = if (it.parameters.isEmpty())
                    listOf()
                else
                    it
                        .parameters
                        .filter { p -> !p.isVararg && !p.isVararg && p.name != null }
                        .map { p -> ResolverArguments(p.name!!, p.index, p.type) },

                deprecatedReason = if (it.hasAnnotation<Deprecated>())
                    it.findAnnotation<Deprecated>()!!.message
                else
                    "",

                description = if (it.hasAnnotation<Description>())
                    it.findAnnotation<Deprecated>()!!.message
                else
                    ""
            )
        }
}
