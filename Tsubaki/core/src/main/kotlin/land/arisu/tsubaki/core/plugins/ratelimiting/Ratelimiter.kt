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

package land.arisu.tsubaki.core.plugins.ratelimiting

import java.time.Duration
import java.time.Instant
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import land.arisu.tsubaki.common.config.Config
import land.arisu.tsubaki.common.kotlin.logging
import land.arisu.tsubaki.core.Tsubaki
import land.arisu.tsubaki.core.models.InMemoryStore

class Ratelimiter(private val config: Config): CoroutineScope {
    private var lastPurgedAt = Instant.now()
    private val logger by logging(this::class.java)
    private val mutex = Mutex()
    private val store: InMemoryStore<String, Ratelimit> = InMemoryStore(config.caching.memory)
    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext = job + Tsubaki.executorPool.asCoroutineDispatcher()

    fun get(ip: String): Ratelimit {
        val item = store[ip]
        if (item?.expired == true)
            store.store.remove(ip)

        val packet = when {
            item == null -> Ratelimit(config.ratelimiting.maxRequests.toLong(), Instant.now().plus(Duration.ofHours(1L)))
            item.expired -> Ratelimit(config.ratelimiting.maxRequests.toLong(), Instant.now().plus(Duration.ofHours(1L))).also {
                logger.debug("IP $ip has been reset")
            }

            else -> item.consume()
        }

        cleanup()
        store[ip] = packet
        return packet
    }

    private fun launchInScope(block: suspend CoroutineScope.() -> Unit) = launch {
        block()
    }

    private fun cleanup() {
        if (shouldPurge()) {
            logger.info("Was told to purge records, now creating coroutine...")
            launchInScope {
                mutex.lock()

                try {
                    val keys = store.store.filter { it.value.expired }.keys
                    logger.debug("Found ${keys.size} keys to remove from in-memory pool")

                    keys.forEach {
                        store.store.remove(it)
                    }

                    lastPurgedAt = Instant.now()

                    logger.debug("Removed ${keys.size} from memory pool; now at ${store.store.size} ratelimits")
                } finally {
                    mutex.unlock()
                }
            }
        }
    }

    private fun shouldPurge(): Boolean = Duration.between(lastPurgedAt, Instant.now()) > Duration.ofHours(1L)
}
