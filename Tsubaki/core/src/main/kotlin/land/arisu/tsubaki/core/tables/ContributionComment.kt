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

import java.util.*
import land.arisu.tsubaki.core.exposed.columns.json
import land.arisu.tsubaki.core.exposed.tables.SnowflakeEntity
import land.arisu.tsubaki.core.exposed.tables.SnowflakeEntityClass
import land.arisu.tsubaki.core.exposed.tables.SnowflakeTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime

/**
 * Represents a comment in a contribution, if a contribution
 * holds the [ContributionState.CLOSED] or [ContributionState.MERGED] state,
 * then this table is immutable in that specific [contribution][Contribution].
 */
object ContributionCommentTable: SnowflakeTable("contribution_comments") {
    /**
     * Returns a datetime of when the comment was last edited
     */
    val updatedAt: Column<DateTime?> = datetime("updated_at").nullable()

    /**
     * Returns a datetime of when the comment was created
     */
    val createdAt: Column<DateTime> = datetime("created_at").default(DateTime.now())

    /**
     * A list of reactions on this comment, refer to the `reactions_count` of a [Map] of the
     * reaction emoji and the count. Use the following mutation to increment a reaction:
     *
     * ```graphql
     * mutation {
     *    # Increment a reaction on a comment
     *    addReaction(comment_id: "<id>", reaction: "<reaction id>") {
     *      comment
     *      reaction_counts
     *      author
     *      reactions
     *    }
     *
     *    # Decrement a reaction on a comment
     *    removeReaction(comment: "<id>", reaction: "<reaction>") {
     *      comment
     *      reaction_counts
     *      author
     *      reactions
     *    }
     * }
     * ```
     */
    val reactions: Column<Map<String, Number>> = json("reactions")

    /**
     * The comment ID if this comment was a reply to another comment
     */
    val commentID: Column<UUID?> = uuid("comment_id").nullable()

    /**
     * The project reference of which this comment is from.
     */
    val project: Column<String> = text("project")

    /**
     * The comment in Markdown form with [GitHub Flavoured Markdown](https://github.github.com/gfm/) support
     * that the author has commented on.
     */
    val comment: Column<String> = text("comment")

    /**
     * The author reference of who created this comment.
     */
    val author: Column<String> = text("author")

    /**
     * Boolean value if this contribution comment was made by the same
     * person who made this contribution.
     */
    val self: Column<Boolean> = bool("is_self")
}

class ContributionComment(id: EntityID<String>): SnowflakeEntity(id) {
    companion object: SnowflakeEntityClass<ContributionComment>(ContributionCommentTable)

    var reactions by ContributionCommentTable.reactions
    var commentID by ContributionCommentTable.commentID
    var project by ContributionCommentTable.project
    var comment by ContributionCommentTable.comment
    var author by ContributionCommentTable.author
    var self by ContributionCommentTable.self
}
