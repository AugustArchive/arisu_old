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

package land.arisu.tsubaki.core.tables

import land.arisu.tsubaki.common.bits.Bitfield
import land.arisu.tsubaki.core.exposed.columns.*
import land.arisu.tsubaki.core.exposed.tables.*
import land.arisu.tsubaki.core.exposed.tables.SnowflakeEntity
import land.arisu.tsubaki.core.exposed.tables.SnowflakeEntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.TextColumnType
import org.joda.time.DateTime

/**
 * A list of [Project] flags.
 * @param bitfield The bitfield object
 */
enum class ProjectFlags(
    @Suppress("UNUSED")
    val bitfield: Bitfield
) {
    /**
     * Represents if the project is public to the world
     */
    Public(Bitfield(1.shl(0))), // 1 << 0

    /**
     * Represents if the project is archived, or read-only.
     */
    Readonly(Bitfield(1.shl(1))), // 1 << 1

    /**
     * Represents if the project is private, only the members
     * of the project or organization can view it.
     */
    Private(Bitfield(1.shl(2))) // 1 << 2
}

/**
 * Represents a project owned by a [User] or [Organization][land.arisu.tsubaki.core.tables.organizations.Organization]
 */
object ProjectTable: SnowflakeTable("projects") {
    /**
     * List of snowflakes of the contributors who contributed to the project.
     */
    val contributors: Column<List<String>> = array("contributors", TextColumnType())

    /**
     * The description of the project, if any
     */
    val description: Column<String?> = text("description").nullable()

    /**
     * Returns a datetime of when the project has been updated
     */
    val updatedAt: Column<DateTime?> = datetime("updated_at").nullable()

    /**
     * Returns a datetime of when the project was created
     */
    val createdAt: Column<DateTime> = datetime("created_at").default(DateTime.now())

    /**
     * The directory, or path to the contents of this project.
     */
    val directory: Column<String> = text("directory")

    /**
     * The owner's ID linked to this project, which can refer
     * to a [User] or [Organization][land.arisu.tsubaki.core.tables.organizations.Organization].
     */
    val owner: Column<String> = text("owner_id")

    /**
     * Bitfield of this project's flags, by default, the flags
     * only has `1 << 0` (1), which means the project is public.
     */
    val flags: Column<Int> = integer("flags").default(ProjectFlags.Public.bitfield.bits)

    /**
     * The name of the project
     */
    val name: Column<String> = text("name")
}

class Project(id: EntityID<String>): SnowflakeEntity(id) {
    companion object: SnowflakeEntityClass<Project>(ProjectTable)

    var contributors by ProjectTable.contributors
    var description by ProjectTable.description
    var directory by ProjectTable.directory
    var owner by ProjectTable.owner
    var flags by ProjectTable.flags
    var name by ProjectTable.name
}
