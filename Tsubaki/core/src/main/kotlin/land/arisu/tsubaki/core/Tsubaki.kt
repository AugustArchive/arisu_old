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

package land.arisu.tsubaki.core

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.metrics.micrometer.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.sentry.Sentry
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import land.arisu.tsubaki.common.Environment
import land.arisu.tsubaki.common.Version
import land.arisu.tsubaki.common.config.Config
import land.arisu.tsubaki.common.kotlin.logging
import land.arisu.tsubaki.core.controllers.controllersModule
import land.arisu.tsubaki.core.endpoints.endpointsModule
import land.arisu.tsubaki.core.jobs.jobsModule
import land.arisu.tsubaki.core.modules.TsubakiThreadFactory
import land.arisu.tsubaki.core.modules.coreModules
import land.arisu.tsubaki.core.modules.postgres.PostgresModule
import land.arisu.tsubaki.core.modules.redis.RedisModule
import land.arisu.tsubaki.core.plugins.pluginsModule
import land.arisu.tsubaki.core.plugins.ratelimiting.Ratelimiting
import land.arisu.tsubaki.core.routing.AbstractEndpoint
import org.koin.core.Koin
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

/**
 * Represents the main entry point of running Tsubaki.
 */
object Tsubaki {
    val executorPool: ExecutorService = Executors.newCachedThreadPool(TsubakiThreadFactory())
    private val log by logging(this::class.java)

    /**
     * Startup of running Tsubaki.
     */
    fun launch() {
        log.info("Launching Tsubaki...")

        // Launch Koin so pieces can be injected
        startKoin {
            modules(tsubakiModule, endpointsModule, jobsModule, coreModules, controllersModule, pluginsModule)
        }

        val koin = GlobalContext.get()
        runBlocking {
            startup(koin)
        }
    }

    /**
     * Shuts down the current Tsubaki process
     */
    private fun shutdown() {
        val koin = GlobalContext.get()

        koin.get<RedisModule>().disconnect()
        koin.get<PostgresModule>().close()
    }

    private suspend fun startup(koin: Koin) {
        // Load Postgres and Redis
        koin.get<RedisModule>().connect()
        koin.get<PostgresModule>()

        // Setup Sentry
        val config = koin.get<Config>()
        if (config.sentryDSN != null) {
            log.info("found a sentry dsn to use, configuring sentry! :D")

            Sentry.init { opts ->
                opts.dsn = config.sentryDSN
                opts.tags += mapOf(
                    "project.versions.kotlin" to KotlinVersion.CURRENT.toString(),
                    "project.versions.java" to "${System.getProperty("java.version", "unknown")} (${System.getProperty("java.vendor", "unknown")})"
                )

                opts.environment = when (config.environment) {
                    Environment.Development -> "dev"
                    Environment.Production -> "prod"
                    else -> "unknown"
                }
            }
        }

        // Load Ktor server
        val environment = applicationEngineEnvironment {
            this.developmentMode = config.environment == Environment.Development
            this.log = LoggerFactory.getLogger("land.arisu.tsubaki.server.Application")

            connector {
                host = config.host ?: "0.0.0.0"
                port = 28093
            }

            module {
                install(Ratelimiting)

                install(CallLogging) {
                    level = Level.DEBUG

                    // ignore POST /graphql (indev, it'll spam console since gql playground does stuff with this + not needed anyway SOOOOO)
                    filter { call -> !(call.request.uri == "/graphql" && call.request.httpMethod == HttpMethod.Post) }
                }

                install(ContentNegotiation) {
                    json(koin.get())
                }

                install(DefaultHeaders) {
                    header("X-Powered-By", "Arisu/Tsubaki (+https://github.com/arisuland/Tsubaki; ${Version.CURRENT})")
                    header("Server", "cute furries doing cute things :3")
                }

                install(CORS) {
                    header(HttpHeaders.XForwardedProto)
                    anyHost()
                }

                if (config.metrics.enabled) {
                    val _registry = koin.get<PrometheusMeterRegistry>()
                    install(MicrometerMetrics) {
                        registry = _registry
                        meterBinders = listOf(
                            JvmMemoryMetrics(),
                            JvmGcMetrics(),
                            ProcessorMetrics(),
                            JvmThreadMetrics()
                        )
                    }
                }

                val endpoints = koin.getAll<AbstractEndpoint>()
                install(Routing) {
                    for (endpoint in endpoints) {
                        routing {
                            route(endpoint.path, endpoint.method) {
                                handle {
                                    try {
                                        endpoint.execute(call)
                                    } catch (e: Exception) {
                                        this@Tsubaki.log.error("Unable to run \"${endpoint.method.value} ${endpoint.path}\":", e)
                                        return@handle call.respondText(Json.encodeToString(JsonObject.serializer(), JsonObject(mapOf(
                                            "message" to JsonPrimitive("Unable to handle the request, the error has been logged.")
                                        ))), status = HttpStatusCode.InternalServerError, contentType = ContentType.parse("application/json"))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        val server = embeddedServer(Netty, environment)
        server.addShutdownHook {
            shutdown()
            server.stop(1, 5, TimeUnit.SECONDS)
        }

        server.start(wait = true)
    }
}
