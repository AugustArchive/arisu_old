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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Configuration details for using Amazon S3 or Wasabi (100% S3 compatible)
 * for storing projects.
 */
@Serializable
data class S3StorageConfig(
    /**
     * If we should use Wasabi instead of Amazon S3
     */
    @SerialName("use_wasabi")
    val useWasabi: Boolean = false,

    /**
     * The access key to authenticate to
     */
    @SerialName("access_key")
    val accessKey: String? = null,

    /**
     * The secret key to authenticate to
     */
    val secretKey: String? = null,

    /**
     * The bucket to use to store projects,
     * if this bucket wasn't created before,
     * Tsubaki will create it.
     */
    val bucket: String = "tsubaki"
)
