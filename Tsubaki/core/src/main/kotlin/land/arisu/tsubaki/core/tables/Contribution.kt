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

import land.arisu.tsubaki.core.exposed.tables.*
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime

/**
 * Represents the state of a [Contribution].
 */
enum class ContributionState {
    /**
     * The original state of a [Contribution], that it is opened to be checked. :3
     */
    OPENED,

    /**
     * Whoops! Maybe that contribution wasn't right and the owner has told you why.
     */
    NEEDS_REVIEW,

    /**
     * Sorry, the contribution is closed and won't be merged. :/
     */
    CLOSED,

    /**
     * Yay! It has been merged and it'll be in the project's
     * application soon enough. :<
     */
    MERGED
}

/**
 * Represents the contribution details from a project.
 */
object ContributionTable: SnowflakeTable("contributions") {
    /**
     * Returns a datetime of when the contribution was last updated
     */
    val updatedAt: Column<DateTime?> = datetime("updated_at").nullable()

    /**
     * Returns a datetime of when the contribution was posted at
     */
    val createdAt: Column<DateTime> = datetime("created_at").default(DateTime.now())

    /**
     * How many additions the contribution has
     */
    val additions: Column<Int> = integer("additions")

    /**
     * How many deletions the contribution has
     */
    val deletions: Column<Int> = integer("deletions")

    /**
     * The project ID that the [issuer] has contributed to
     */
    val project: Column<String> = text("project")

    /**
     * The issuer's ID who contributed
     */
    val issuer: Column<String> = text("issuer")

    /**
     * The owner of the repository
     */
    val owner: Column<String> = text("owner")

    /**
     * The current state of this [Contribution].
     */
    val state: Column<ContributionState> = enumeration("state", ContributionState::class)

    /**
     * The file path of the contribution
     */
    val file: Column<String> = text("file")

    /**
     * The difference between the contribution and the file structured:
     *
     * ```
     * <file> - <issuer id>
     * ...
     *
     * <file> - <old file contents>
     * ...
     * ```
     */
    val diff: Column<String> = text("diff")
}

class Contribution(id: EntityID<String>): SnowflakeEntity(id) {
    companion object: SnowflakeEntityClass<Contribution>(ContributionTable)

    var createdAt by ContributionTable.createdAt
    var updatedAt by ContributionTable.updatedAt
    var additions by ContributionTable.additions
    var deletions by ContributionTable.deletions
    var project by ContributionTable.project
    var issuer by ContributionTable.issuer
    var owner by ContributionTable.owner
    var state by ContributionTable.state
    var file by ContributionTable.file
    var diff by ContributionTable.diff
}
