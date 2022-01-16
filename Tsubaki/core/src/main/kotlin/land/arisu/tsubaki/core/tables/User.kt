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

enum class UserFlags(val bits: Int) {
    /**
     * Represents a staff member at Arisu, i.e, handles moderation.
     */
    Staff(1.shl(0)), // 1 << 0

    /**
     * Flag for the founder of Arisu, which will be Noel (@auguwu)
     */
    Founder(1.shl(1)), // 1 << 1

    /**
     * Represents a Noelware employee
     */
    Employee(1.shl(2)), // 1 << 2

    /**
     * Represents a bug hunter, this is received when
     * you file a bug report and it is fixed in production.
     */
    BugHunter(1.shl(3)), // 1 << 3

    /**
     * System account
     */
    System(1.shl(4)), // 1 << 4

    /**
     * Represents a user who bought the Premium tier, not available
     * yet, but in the future:tm:
     */
    Premium(1.shl(5)), // 1 << 5
}

/**
 * Represents a person who is registered
 */
object UserTable: SnowflakeTable("users") {
    /**
     * Returns the twitter handle for the user, if any
     */
    val twitterHandle: Column<String?> = text("twitter_handle").nullable()

    /**
     * Returns the user's bio, if any
     */
    val description: Column<String?> = varchar("description", 256).nullable()

    /**
     * Returns a datetime of when the user has updated their profile
     */
    val updatedAt: Column<DateTime?> = datetime("updated_at").nullable()

    /**
     * Returns a datetime of when the user was registered
     */
    val createdAt: Column<DateTime> = datetime("created_at")

    /**
     * The hashed password using Argon2
     */
    val password: Column<String> = text("password")

    /**
     * Returns the uniformed URL of the user's avatar to use
     */
    val avatar: Column<String?> = text("avatar").nullable()

    /**
     * Returns the email for this user, this isn't public information
     */
    val email: Column<String> = text("email")

    /**
     * Returns the user's login, which will be used to determine their username.
     *
     * i.e, `noel` -> https://arisu.land/users/noel
     */
    val login: Column<String> = text("login")

    /**
     * The user's flags to determine their "roles"
     */
    val flags: Column<Int> = integer("flags").default(0)

    /**
     * The user's display name, or it'll stick with their username
     * when displayed.
     */
    val name: Column<String?> = text("name").nullable()
}

class User(id: EntityID<String>): SnowflakeEntity(id) {
    companion object: SnowflakeEntityClass<User>(UserTable)

    var twitterHandle by UserTable.twitterHandle
    var description by UserTable.description
    var createdAt by UserTable.createdAt
    var updatedAt by UserTable.updatedAt
    var password by UserTable.password
    var avatar by UserTable.avatar
    var login by UserTable.login
    var email by UserTable.email
    var flags by UserTable.flags
    var name by UserTable.name
}
