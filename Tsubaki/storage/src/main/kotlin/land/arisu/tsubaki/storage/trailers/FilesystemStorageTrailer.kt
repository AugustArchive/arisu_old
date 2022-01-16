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

package land.arisu.tsubaki.storage.trailers

import java.io.File
import java.io.InputStream
import java.lang.IllegalArgumentException
import land.arisu.tsubaki.common.kotlin.logging
import land.arisu.tsubaki.storage.IStorageTrailer

/**
 * Represents a [storage trailer][IStorageTrailer] of using the filesystem
 * to host projects. If you're using Docker, you must use this [storage trailer][FilesystemStorageTrailer]
 * as a volume (`-v host:container`) to keep it persistent. It is recommend to use this [storage trailer][FilesystemStorageTrailer]
 * when testing Tsubaki.
 *
 * ### Docker Compose
 * If you're using Docker Compose, please create the volume like this:
 * ```yml
 * version: '3.8'
 * services:
 *   tsubaki:
 *     volumes:
 *       - '/path/on/host:/path/on/container'
 *
 * volumes:
 *    tsubaki:
 * ```
 *
 * @param directory The directory to use when creating this [storage trailer][FilesystemStorageTrailer].
 */
class FilesystemStorageTrailer(private val directory: String): IStorageTrailer {
    private val logger by logging(this::class.java)
    override val name: String = "trailer:filesystem"

    /**
     * Function to call when to initialize the storage trailer,
     * this is called when [this storage builder][IStorageTrailer] is first created.
     *
     * @throws IllegalArgumentException If the [directory] was not a directory
     * @throws IllegalStateException If the directory didn't denote a parent directory
     */
    override fun init() {
        logger.info("Initializing this storage trailer... (solution: filesystem)")
        val dir = File(directory)
        if (!dir.isDirectory) {
            throw IllegalArgumentException("Directory `$directory` was not a directory.")
        }

        val files = dir.listFiles()
            ?: throw IllegalStateException("Directory `$directory` didn't denote a directory.")

        if (files.isEmpty()) {
            logger.info("Directory `$directory` didn't have any files, making directories!")
            dir.mkdirs()
        }

        logger.info("✔ Initialized filesystem storage trailer using directory `$directory`.")
    }

    override fun handle(files: List<InputStream>): Boolean {
        return true
    }
}
