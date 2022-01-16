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

package land.arisu.tsubaki.core.modules.storage

import java.io.InputStream
import java.lang.IllegalArgumentException
import land.arisu.tsubaki.common.config.Config
import land.arisu.tsubaki.storage.IStorageTrailer
import land.arisu.tsubaki.storage.trailers.FilesystemStorageTrailer
import land.arisu.tsubaki.storage.trailers.GoogleCloudStorageTrailer
import land.arisu.tsubaki.storage.trailers.S3StorageTrailer

/**
 * Module for handling storage within projects.
 */
class StorageModule(config: Config) {
    // The storage trailer Tsubaki is using
    private val storage: IStorageTrailer = when {
        config.storage.filesystem != null -> FilesystemStorageTrailer(config.storage.filesystem!!.directory)
        config.storage.gcs != null -> GoogleCloudStorageTrailer()
        config.storage.s3 != null -> S3StorageTrailer(
            useWasabi = config.storage.s3!!.useWasabi,
            accessKey = config.storage.s3!!.accessKey!!,
            secretKey = config.storage.s3!!.accessKey!!,
            bucketName = config.storage.s3!!.bucket
        )

        else -> throw IllegalArgumentException("Multiple or no storage trailers were configured.")
    }

    /**
     * Function to call when handling data storage from one source to another.
     * This might be a version control software package in the future, who knows.
     *
     * @param files The list of files to create and push to [this storage trailer][IStorageTrailer]
     * @returns a [bool][Boolean] value if it succeeded or not.
     */
    fun handle(files: List<InputStream>): Boolean = storage.handle(files)
}
