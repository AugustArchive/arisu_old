/**
 * 🥑 datetime-scalars: Adds scalar support for kotlinx.datetime for GraphQL Java
 * Copyright (C) 2021 Noelware
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package land.arisu.libraries.datetime.scalars

import graphql.schema.GraphQLScalarType
import graphql.schema.Coercing
import kotlinx.datetime.Instant

object DateScalar {
    val coercing: Coercing<Instant, String> = object: Coercing<Instant, String> {
        override fun serialize(input: Any?): String? {
            var instant: Instant

            Instant.parse(input)
        }

        override fun parseValue(input: Any?): Instant {
            TODO("Not yet implemented")
        }

        override fun parseLiteral(input: Any?): Instant {
            TODO("Not yet implemented")
        }
    }

    val INSTANCE = GraphQLScalarType
        .newScalar()
        .name("Date")
        .description("An RFC-3339 compliant Date scalar")
        .coercing(coercing)
        .build()
}
