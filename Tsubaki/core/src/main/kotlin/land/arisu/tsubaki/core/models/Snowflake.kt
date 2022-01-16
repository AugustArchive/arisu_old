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

package land.arisu.tsubaki.core.models

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/**
 * Represents a unified identifier for identifying objects within Tsubaki.
 *
 * ## Formula
 * ```
 * 64                                         22     17    12         0
 * 000000111011000111100001101001000101000000  00001 0000  000000000000
 *   number of ms since epoch (2021-12-01)     thread pid   increment
 * ```
 */
object Snowflake {
    // Returns the increment atomically (max 4095)
    private val INCREMENT: AtomicLong = AtomicLong(0L)

    // Returns the thread ID atomically
    private val threadId: AtomicInteger = AtomicInteger(Thread.activeCount().rem(31))

    // Returns the process ID atomically
    private val processId: AtomicLong = AtomicLong(ProcessHandle.current().pid().rem(31))

    /**
     * Returns the epoch when constructing snowflakes,
     * which is December 1st, 2021 @ 00:00 UTC
     */
    val EPOCH: Long = 1638230400000

    fun generate(): String {
        val time = (System.currentTimeMillis() - EPOCH).shl(36)
        val worker = threadId.getAndSet(Thread.activeCount().rem(31)).shl(17)
        val process = processId.getAndSet(ProcessHandle.current().pid().rem(31)).shl(12)
        if (INCREMENT.get() >= 4095L)
            INCREMENT.set(0L)

        val increment = INCREMENT.getAndIncrement()
        return (time + worker.toLong() + process + increment).toString()
    }
}
