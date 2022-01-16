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

package land.arisu.tsubaki.core.controllers

import de.mkammerer.argon2.Argon2
import land.arisu.tsubaki.core.controllers.models.User
import land.arisu.tsubaki.core.exposed.extensions.findOne
import land.arisu.tsubaki.core.exposed.transations.asyncTransation
import land.arisu.tsubaki.core.extensions.getInjectable
import land.arisu.tsubaki.core.modules.jwt.JwtModule
import land.arisu.tsubaki.core.tables.User as TsubakiUser
import land.arisu.tsubaki.core.tables.UserTable
import org.jetbrains.exposed.sql.select
import org.joda.time.DateTime
import org.koin.core.context.GlobalContext

class UserController(private val argon2: Argon2): AbstractController<User, TsubakiUser>() {
    override suspend fun getOrNull(id: String): User? = asyncTransation {
        val def = UserTable.select { UserTable.id.eq(id) }.firstOrNull() ?: return@asyncTransation null
        User(
            def[UserTable.twitterHandle],
            def[UserTable.description],
            def[UserTable.createdAt].toString(),
            def[UserTable.updatedAt].toString(),
            def[UserTable.avatar],
            def[UserTable.login],
            def[UserTable.flags],
            def[UserTable.name],
            def[UserTable.id].value
        )
    }.await()

    override suspend fun get(id: String): User = getOrNull(id) ?: error("User with ID $id was not found.")

    override suspend fun remove(id: String) = asyncTransation {
        TsubakiUser.findOne { UserTable.id.eq(id) }?.delete() ?: return@asyncTransation false
        true
    }.await()

    suspend fun create(emailRef: String, passwordRef: String, loginRef: String): User {
        val hashed: String
        try {
            hashed = argon2.hash(
                100,
                65535,
                1, // TODO: check per cpu core?
                passwordRef.toCharArray()
            )
        } finally {
            argon2.wipeArray(passwordRef.toCharArray())
        }

        val (dbUser, user) = asyncTransation {
            val user = TsubakiUser.new {
                email = emailRef
                password = hashed
                login = loginRef
                createdAt = DateTime.now()
            }

            val entityReturn = User(
                user.twitterHandle,
                user.description,
                user.createdAt.toString(),
                user.updatedAt.toString(),
                user.avatar,
                user.login,
                user.flags,
                user.name,
                user.id.value
            )

            Pair(user, entityReturn)
        }.await()

        val jwt = GlobalContext.getInjectable<JwtModule>()
        jwt.generateUserToken(dbUser)

        return user
    }

    override suspend fun update(id: String, update: Any): Boolean {
        TODO("Not yet implemented")
    }

    override fun toEntity(user: TsubakiUser): User = User(
        user.twitterHandle,
        user.description,
        user.createdAt.toString(),
        user.updatedAt.toString(),
        user.avatar,
        user.login,
        user.flags,
        user.name,
        user.id.value
    )
}
