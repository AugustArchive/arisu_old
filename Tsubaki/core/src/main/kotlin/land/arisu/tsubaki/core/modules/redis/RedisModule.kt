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

package land.arisu.tsubaki.core.modules.redis

import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.reactive.RedisReactiveCommands
import land.arisu.tsubaki.common.config.Config
import land.arisu.tsubaki.common.kotlin.logging

/**
 * Represents a connection with Redis, which is linked to this [RedisModule].
 */
class RedisModule(private val config: Config) {
    private lateinit var connection: StatefulRedisConnection<String, String>
    lateinit var commands: RedisReactiveCommands<String, String>
    private val logger by logging(this::class.java)
    private val url: RedisURI = RedisURI().let {
        if (config.redis.sentinels?.isNotEmpty() == true) {
            it.sentinels += config.redis.sentinels!!.map { s -> RedisURI.create(s.host, s.port) }

            if (config.redis.password != null) {
                it.password = config.redis.password!!.toCharArray()
            }

            if (config.redis.master != null) {
                it.sentinelMasterId = config.redis.master
            }

            return@let it
        } else {
            it.host = config.redis.host
            it.port = config.redis.port
            it.database = config.redis.db
            it.clientName = "Arisu/Tsubaki"

            if (config.redis.password != null) {
                it.password = config.redis.password!!.toCharArray()
            }

            it
        }
    }

    private val client: RedisClient = RedisClient.create(url)

    fun connect() {
        logger.info("Now connecting to Redis...")
        connection = client.connect()
        commands = connection.reactive()
    }

    fun disconnect() {
        logger.info("Disposing Redis connection...")
        connection.close()
    }
}
