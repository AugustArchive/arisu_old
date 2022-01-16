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

/**
 * List of access tokens registered by a user
 */
object AccessTokensTable: SnowflakeTable("access_tokens") {
    /**
     * Returns the JWT token stored in the database, this has a lifetime use
     * until it is revoked and this entry is removed in the database.
     */
    val token: Column<String> = text("token")

    /**
     * Returns the user reference who owns this token, even though the token
     * has the user ID stored in it.
     */
    val user: Column<EntityID<String>> = reference("user", UserTable)
}

class AccessTokens(id: EntityID<String>): SnowflakeEntity(id) {
    companion object: SnowflakeEntityClass<AccessTokens>(AccessTokensTable)

    var token by AccessTokensTable.token
    var user by User referencedOn AccessTokensTable.user
}
