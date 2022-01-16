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

package land.arisu.tsubaki.common

import land.arisu.tsubaki.common.extensions.*

/**
 * Represents a field to create a [Version] instance.
 * @param major The major version (rewrites, feature impls, etc)
 * @param minor The minor version (feature impls that aren't a huge impact, bug fixes, etc)
 * @param patch Patch versions (bug fixes that are very minor, vulnerabilities, etc)
 */
class Version(
    private val major: Int,
    private val minor: Int,
    private val patch: Int
) {
    companion object {
        /**
         * Returns the current version.
         */
        val CURRENT = Version(1, 0)
    }

    /**
     * Create a [Version] class using only [major] and [minor].
     * @param major The major version (rewrites, feature impls, etc)
     * @param minor The minor version (feature impls that aren't a huge impact, bug fixes, etc)
     */
    constructor(major: Int, minor: Int): this(major, minor, 0)

    /**
     * Returns a stringified version of this [Version]
     */
    override fun toString(): String = "$major.$minor.$patch [${"git rev-parse HEAD".exec().slice(0..8)}]"
}
