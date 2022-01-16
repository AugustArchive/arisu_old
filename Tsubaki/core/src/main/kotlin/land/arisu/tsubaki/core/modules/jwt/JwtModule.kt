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

package land.arisu.tsubaki.core.modules.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.crypto.SecretKey
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import land.arisu.tsubaki.common.config.Config
import land.arisu.tsubaki.common.extensions.asPrimitive
import land.arisu.tsubaki.common.kotlin.logging
import land.arisu.tsubaki.core.controllers.models.User
import land.arisu.tsubaki.core.exposed.transations.asyncTransation
import land.arisu.tsubaki.core.models.InMemoryStore
import land.arisu.tsubaki.core.modules.redis.RedisModule
import land.arisu.tsubaki.core.tables.AccessTokens

/**
 * Represents a module for handling JWT tokens for lifetime access tokens
 * and short-lived tokens for the frontend UI, which is stored in Redis
 * and in-memory + invalidated after a month of use.
 */
class JwtModule(config: Config, private val json: Json, private val redis: RedisModule) {
    private val hashMethod: SecretKey = Keys.hmacShaKeyFor(config.jwt.token.toByteArray())
    private val logger by logging(this::class.java)
    private val store: InMemoryStore<String, String> = InMemoryStore(config.caching.memory)

    /**
     * Generates a short-lived token, this is a Mutation but
     * only the frontend UI is allowed to generate short lived
     * tokens.
     *
     * @param user The user who is generating this token
     * @return The short-lived tokens as a [Pair] or (access, refresh). The
     * refresh token is to refresh the access token and re-generate both and
     * invalidates both and stores them in-memory + Redis.
     */
    fun generateShortLivedToken(user: User): String {
        logger.info("Generating access token for ${user.name ?: "@${user.login}"} (${user.id})")

        // Returns the access token, which is short-lived for 1 day
        val accessToken = Jwts
            .builder()
            .setSubject("Arisu/Tsubaki")
            .signWith(hashMethod)
            .setPayload(json.encodeToString(JsonObject.serializer(), JsonObject(mapOf(
                "user_id" to user.id.asPrimitive()
            ))))
            .setExpiration(Date.from(Instant.now().plusSeconds(3600L)))
            .compact()

        val packet = JsonObject(mapOf(
            "user_id" to user.id.asPrimitive()
        ))

        val redisPacket = JsonObject(
            mapOf(
                "user_id" to user.id.asPrimitive()
            )
        )

        store["tokens:${user.id}"] = Json.encodeToString(JsonObject.serializer(), packet)
        redis.commands.hset(
            "tsubaki:access_tokens",
            user.id,
            json.encodeToString(JsonObject.serializer(), redisPacket)
        )

        return accessToken
    }

    /**
     * Generates a user token that can bypass authentication, this
     * is stored in the database under the `access_tokens` table and is
     * not available to be queries unauthenticated, this is shown in the
     * `login` mutation with a valid login/email + password.
     *
     * @param userRef The user reference from the database to create a one-to-one relationship
     * @return The JWT token to be used with the API. :D
     */
    suspend fun generateUserToken(userRef: land.arisu.tsubaki.core.tables.User): String {
        logger.info("Generating user token for ${userRef.name ?: "@${userRef.login}"} (${userRef.id.value})")

        // user tokens never expire, so we'll never get a JwtExpiredException
        val userToken = Jwts
            .builder()
            .setSubject("Arisu/Tsubaki")
            .signWith(hashMethod)
            .setPayload(json.encodeToString(JsonObject.serializer(), JsonObject(mapOf(
                "user_id" to userRef.id.value.asPrimitive()
            ))))
            .compact()

        // create a transaction to create a JWT token
        asyncTransation {
            AccessTokens.new {
                token = userToken
                user = userRef
            }
        }.await() // await it :D

        return userToken
    }

    fun invalidateToken(user: User) {
        if (store.store.contains("tokens:${user.id}"))
            store.store.remove("tokens:${user.id}")

        redis.commands.hdel("tsubaki:access_tokens", user.id)
    }

    fun isValidToken(token: String): Boolean {
        if (!isActive(token))
            return false

        return store.store.filterValues { it == token }.isNotEmpty()
    }

    /**
     * Checks if the [token] is active or not, which will
     * return a [Boolean] if it is active or not.
     */
    fun isActive(token: String): Boolean = try {
        Jwts
            .parserBuilder()
            .setSigningKey(hashMethod)
            .build()
            .parseClaimsJws(token)

        true
    } catch (e: Exception) {
        false
    }
}
