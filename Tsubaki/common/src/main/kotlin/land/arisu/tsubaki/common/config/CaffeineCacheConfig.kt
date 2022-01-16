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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the configuration for caching in-memory blobs within Tsubaki
 */
@Serializable
data class CaffeineCacheConfig(
    /**
     * Represents the duration time to use, it can only
     * go from hours -> weeks. The default is days.
     */
    val durationStyle: DurationStyle = DurationStyle.DAYS,

    /**
     * How many items can be cached
     */
    val maxItems: Int = 100_000,

    /**
     * The max age of when the items get disposed
     * and new items can be stored. The default is
     * 1 day.
     */
    val maxAge: Int = 1
)

@Serializable
enum class DurationStyle {
    @SerialName("hours")
    HOURS,

    @SerialName("days")
    DAYS,

    @SerialName("weeks")
    WEEKS
}
