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

import com.charleskorn.kaml.Yaml
import de.mkammerer.argon2.Argon2Factory
import dev.floofy.haru.Scheduler
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import java.io.File
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import land.arisu.tsubaki.common.TsubakiInfo
import land.arisu.tsubaki.common.config.Config
import land.arisu.tsubaki.core.interceptors.LoggingInterceptor
import land.arisu.tsubaki.core.interceptors.SentryInterceptor
import land.arisu.tsubaki.core.serializers.AnySerializer
import land.arisu.tsubaki.core.serializers.JodaDateTimeSerializer
import org.joda.time.DateTime
import org.koin.dsl.module

val tsubakiModule = module {
    // Register the scheduler
    single {
        Scheduler()
    }

    single {
        SerializersModule {
            contextual(Any::class) {
                AnySerializer()
            }

            contextual(DateTime::class) {
                JodaDateTimeSerializer()
            }
        }
    }

    // Json module
    single {
        Json {
            ignoreUnknownKeys = true
            serializersModule = get()
        }
    }

    // Http client
    single {
        HttpClient(OkHttp) {
            engine {
                config {
                    followRedirects(true)
                }

                addInterceptor(LoggingInterceptor())

                if (get<Config>().sentryDSN != null) {
                    addInterceptor(SentryInterceptor())
                }
            }

            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }

            install(UserAgent) {
                agent = "Arisu/Tsubaki (+https://github.com/arisuland/Tsubaki; v${TsubakiInfo.VERSION})"
            }
        }
    }

    // Configuration
    single {
        val file = File("./config.yml")
        Yaml.default.decodeFromString(Config.serializer(), file.readText())
    }

    // Argon2
    single {
        Argon2Factory.create()
    }

    // Prometheus registry
    single {
        PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
    }
}
