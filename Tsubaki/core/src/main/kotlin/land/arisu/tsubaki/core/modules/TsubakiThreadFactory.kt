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

package land.arisu.tsubaki.core.modules

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class TsubakiThreadFactory: ThreadFactory {
    private val counter = AtomicInteger(1)
    private val group: ThreadGroup

    init {
        val security = System.getSecurityManager()
        group = if (security != null)
            security.threadGroup
        else
            Thread.currentThread().threadGroup
    }

    override fun newThread(r: Runnable): Thread {
        val thread = Thread(group, r, "Tsubaki-ExecutorThread-${counter.getAndIncrement()}", 0)
        if (thread.isDaemon)
            thread.isDaemon = false

        if (thread.priority != Thread.NORM_PRIORITY)
            thread.priority = Thread.NORM_PRIORITY

        return thread
    }
}
