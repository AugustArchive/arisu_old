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

package land.arisu.tsubaki.bootstrap

import land.arisu.tsubaki.common.kotlin.logging
import land.arisu.tsubaki.core.Tsubaki

object Bootstrap {
    private val logger by logging(this::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        Thread.currentThread().name = "Tsubaki-MainThread"
        Banner.show()

        Tsubaki.launch()
    }
}
