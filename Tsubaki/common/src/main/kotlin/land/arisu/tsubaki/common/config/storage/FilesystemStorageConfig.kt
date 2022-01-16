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

package land.arisu.tsubaki.common.config.storage

import kotlinx.serialization.Serializable

/**
 * Configuration details for the filesystem
 */
@Serializable
data class FilesystemStorageConfig(
    /**
     * The directory for hosting the projects, if you are using
     * Docker, this is a volume you must create using the `-v` flag.
     *
     * ## Example with `docker-compose`
     * ```yml
     * services:
     *   tsubaki:
     *     volumes:
     *       tsubaki_projects:/opt/Tsubaki/data/tsubaki
     *
     * volumes:
     *    tsubaki_projects:
     * ```
     */
    val directory: String = "./data/tsubaki"
)
