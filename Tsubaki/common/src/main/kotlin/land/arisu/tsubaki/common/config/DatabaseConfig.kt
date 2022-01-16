/**
 * ☔🥀 Tsubaki: Core backend infrastructure for Arisu, all the magic begins here ✨🚀
 * Copyright (C) 2020-2021 Noelware
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

package land.arisu.tsubaki.common.config

import kotlinx.serialization.Serializable

/**
 * Represents the Postgres database configuration
 */
@Serializable
data class DatabaseConfig(
    /**
     * The password when authenticating.
     */
    val password: String = "postgres",

    /**
     * The username when authenticating
     */
    val username: String = "postgres",

    /**
     * The database to use, if the database
     * doesn't exist, it'll create it when Tsubaki is being ran.
     */
    val database: String = "tsubaki",

    /**
     * The schema to use, if the schema is not `public`,
     * it'll create the schema and attach it to the database.
     */
    val schema: String = "public",

    /**
     * The hostname to use when authenticating
     */
    val host: String = "localhost",

    /**
     * The port to use when authenticating
     */
    val port: Int = 5432,

    /**
     * The JDBC url to use when connecting,
     * I don't recommend this unless you know what you're doing.
     */
    val url: String? = null
)
