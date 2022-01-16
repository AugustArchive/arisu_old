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

import java.io.InputStream
import land.arisu.tsubaki.storage.IStorageTrailer

/**
 * Represents a [storage trailer][IStorageTrailer] for using Google Cloud Storage (GCS)
 * to host your project files.
 */
class GoogleCloudStorageTrailer: IStorageTrailer {
    override val name: String = "trailer:gcs"

    override fun init() {
        // TODO: this
    }

    override fun handle(files: List<InputStream>): Boolean {
        return true
    }
}
