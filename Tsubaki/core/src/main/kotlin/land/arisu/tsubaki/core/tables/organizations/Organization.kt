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

package land.arisu.tsubaki.core.tables.organizations

import java.util.*
import land.arisu.tsubaki.core.exposed.columns.array
import land.arisu.tsubaki.core.exposed.tables.SnowflakeEntity
import land.arisu.tsubaki.core.exposed.tables.SnowflakeEntityClass
import land.arisu.tsubaki.core.exposed.tables.SnowflakeTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.UUIDColumnType
import org.joda.time.DateTime

/**
 * Represents a organization of many projects, this is acted like a User
 * but with different properties like [OrganizationTable.members] and such.
 */
object OrganizationTable: SnowflakeTable("organizations") {
    /**
     * Returns the twitter handle of this organization, or `null` if none
     * was specified.
     */
    val twitterHandle: Column<String?> = text("twitter_handle").nullable()

    /**
     * Returns the description of the organization to show when viewing
     * the page.
     */
    val description: Column<String?> = text("description").nullable()

    /**
     * Returns a datetime of when the organization has updated their profile
     */
    val updatedAt: Column<DateTime?> = datetime("updated_at").nullable()

    /**
     * Returns a datetime of when the organization was registered
     */
    val createdAt: Column<DateTime> = datetime("created_at").default(DateTime.now())

    /**
     * Returns a [List] of [UUID]s of the members mapped to their UUID.
     */
    val members: Column<List<UUID>> = array("members", UUIDColumnType())

    /**
     * Returns the avatar to display or `null` if none is specified.
     */
    val avatar: Column<String?> = text("avatar").nullable()

    /**
     * Returns the reference of the organization's domain
     * and if it was verified by DNS or not.
     */
    @Suppress("UNUSED")
    val domain: Column<EntityID<String>?> = reference("domain", OrganizationDomainTable).nullable()

    /**
     * The handle mapped to where to preview projects.
     *
     * i.e,
     * handle = "arisu" -> https://arisu.land/~/arisu
     */
    val handle: Column<String> = text("handle")

    /**
     * Returns the owner reference by their UUID.
     */
    val owner: Column<UUID> = uuid("owner")

    /**
     * Returns the name of the organization, if this
     * is `null`, it'll return the handle as the name
     * displayed.
     */
    val name: Column<String?> = text("name").nullable()
}

class Organization(id: EntityID<String>): SnowflakeEntity(id) {
    companion object: SnowflakeEntityClass<Organization>(OrganizationTable)

    var twitterHandle by OrganizationTable.twitterHandle
    var description by OrganizationTable.description
    var createdAt by OrganizationTable.createdAt
    var updatedAt by OrganizationTable.updatedAt
    var members by OrganizationTable.members
    var avatar by OrganizationTable.avatar
    var handle by OrganizationTable.handle
    val domain by OrganizationDomain via OrganizationTable
    var owner by OrganizationTable.owner
    var name by OrganizationTable.name
}
