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

package land.arisu.tsubaki.storage

import java.io.InputStream

/**
 * Represents a trailer for handling storage for projects and such.
 *
 * ### Contributors Note:
 * > If you plan to add your storage trailers, place them under the `trailers/` package that should
 * > extend this [IStorageTrailer] interface.
 */
interface IStorageTrailer {
    /**
     * Returns the name of this [storage trailer][IStorageTrailer].
     */
    val name: String

    /**
     * Function to call when to initialize the storage trailer,
     * this is called when [this storage builder][IStorageTrailer] is first created.
     */
    fun init() {
        // no-op operation (if the storage bucket doesn't need to be initialized)
    }

    /**
     * Function to call when handling data storage from one source to another.
     * This might be a version control software package in the future, who knows.
     *
     * @param files The list of files to create and push to [this storage trailer][IStorageTrailer]
     * @returns a [bool][Boolean] value if it succeeded or not.
     */
    fun handle(
        files: List<InputStream>
    ): Boolean
}
