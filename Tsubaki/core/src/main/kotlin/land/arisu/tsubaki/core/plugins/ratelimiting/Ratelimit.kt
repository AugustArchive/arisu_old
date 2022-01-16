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

import java.time.Instant

/**
 * Represents a snapshot of a ratelimit state, which is immutable.
 */
data class Ratelimit(
    /**
     * The number of remaining requests before you get ratelimited.
     */
    val remaining: Long,

    /**
     * [Instant] at which the ratelimit will reset
     */
    val resetAt: Instant
)

/**
 * Returns a [Boolean] if the ratelimit has expired.
 */
val Ratelimit.expired: Boolean
    get() = this.resetAt < Instant.now()

/**
 * Consume a new [Ratelimit] copy and returns the modified state.
 */
fun Ratelimit.consume(): Ratelimit = copy(
    remaining = (remaining - 1).coerceAtLeast(0)
)

/**
 * Check if we should ratelimit the user or not
 */
val Ratelimit.exceeded: Boolean
    get() = !expired && remaining == 0L
