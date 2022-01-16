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

@Serializable
data class RedisConfig(
    /**
     * A list of sentinels to connect to (see https://redis.io/topics/sentinel for more information)
     */
    val sentinels: List<RedisSentinel>? = listOf(),

    /**
     * The password to authenticate with Redis
     */
    val password: String? = null,

    /**
     * The name of the master sentinel to connect to
     */
    val master: String? = null,

    /**
     * The port of the Redis instance
     */
    val port: Int = 6379,

    /**
     * The hostname of the Redis instance
     */
    val host: String = "localhost",

    /**
     * The database to connect to
     */
    val db: Int = 4
)

@Serializable
data class RedisSentinel(
    val host: String,
    val port: Int
)
