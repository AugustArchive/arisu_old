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

import land.arisu.tsubaki.core.exposed.tables.SnowflakeEntity
import land.arisu.tsubaki.core.exposed.tables.SnowflakeEntityClass
import land.arisu.tsubaki.core.exposed.tables.SnowflakeTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime

/**
 * Represents the table for all the webhooks within a project.
 */
object WebhookTable: SnowflakeTable("webhooks") {
    /**
     * Represents the content type the request should follow by
     */
    val contentType: Column<String> = text("content_type").default("application/json")

    /**
     * Returns the date of when this webhook was created at
     */
    val createdAt: Column<DateTime> = datetime("created_at").default(DateTime.now())

    /**
     * Returns the date of when the webhook was updated at, i.e, url/content-type changes
     */
    val updatedAt: Column<DateTime?> = datetime("updated_at").nullable()

    /**
     * How many successful hits Tsubaki has successfully made the request to
     */
    val success: Column<Int> = integer("success")

    /**
     * How many failed hits the request made
     */
    val failure: Column<Int> = integer("failure")

    /**
     * Reference to the [Project] for this [Webhook].
     */
    val project: Column<EntityID<String>> = reference("project", ProjectTable)

    /**
     * Returns the authentication header value
     */
    val auth: Column<String?> = text("auth").nullable()

    /**
     * Returns the URL to hit
     */
    val url: Column<String> = text("url")
}

class Webhook(id: EntityID<String>): SnowflakeEntity(id) {
    companion object: SnowflakeEntityClass<Webhook>(WebhookTable)

    var contentType by WebhookTable.contentType
    var createdAt by WebhookTable.createdAt
    var updatedAt by WebhookTable.updatedAt
    var success by WebhookTable.success
    var failure by WebhookTable.failure
    var project by Project via WebhookTable
    var auth by WebhookTable.auth
    var url by WebhookTable.auth
}
