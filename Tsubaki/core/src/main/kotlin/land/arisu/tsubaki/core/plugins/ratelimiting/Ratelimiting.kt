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

package land.arisu.tsubaki.core.plugins.ratelimiting

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.util.*
import java.time.Instant
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import land.arisu.tsubaki.common.config.Config
import land.arisu.tsubaki.common.extensions.asPrimitive
import land.arisu.tsubaki.core.extensions.getInjectable
import org.koin.core.context.GlobalContext

class Ratelimiting {
    companion object Feature: ApplicationFeature<ApplicationCallPipeline, Unit, Unit> {
        override val key: AttributeKey<Unit> = AttributeKey("Ratelimiting")

        override fun install(pipeline: ApplicationCallPipeline, configure: Unit.() -> Unit) {
            val ratelimiter = GlobalContext.getInjectable<Ratelimiter>()
            val json = GlobalContext.getInjectable<Json>()
            val config = GlobalContext.getInjectable<Config>()

            pipeline.sendPipeline.intercept(ApplicationSendPipeline.After) {
                // apparently localhost ip is structured like this??? i have zero idea lol
                val ip = this.call.request.origin.remoteHost
                if (ip == "0:0:0:0:0:0:0:1") {
                    proceed()
                    return@intercept
                }

                val ratelimit = ratelimiter.get(ip)
                context.response.header("X-RateLimit-Limit", config.ratelimiting.maxRequests)
                context.response.header("X-RateLimit-Remaining", ratelimit.remaining)
                context.response.header("X-RateLimit-Reset", ratelimit.resetAt.epochSecond)

                if (ratelimit.exceeded) {
                    val retryAfter = (ratelimit.resetAt.epochSecond - Instant.now().epochSecond).coerceAtLeast(0)
                    context.response.header(HttpHeaders.RetryAfter, retryAfter)
                    context.respondText(
                        json.encodeToString(JsonObject.serializer(), JsonObject(mapOf(
                            "message" to "IP $ip has been ratelimited, try again later. >:3".asPrimitive()
                        ))),

                        status = HttpStatusCode.TooManyRequests,
                        contentType = ContentType.parse("application/json")
                    )

                    finish()
                } else {
                    proceed()
                }
            }
        }
    }
}
