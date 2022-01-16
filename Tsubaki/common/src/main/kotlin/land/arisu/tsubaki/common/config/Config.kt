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

package land.arisu.tsubaki.common.config

import kotlinx.serialization.Serializable
import land.arisu.tsubaki.common.Environment

/**
 * The configuration details for running Tsubaki
 */
@Serializable
data class Config(
    /**
     * Runs any pending migrations when running Tsubaki
     */
    val runPendingMigrations: Boolean = true,

    /**
     * Ratelimiting configuration
     */
    val ratelimiting: RatelimitingConfig = RatelimitingConfig(),

    /**
     * The environment to use, if no environment is set,
     * it'll use the development environment; only exception
     * is when using the `TSUBAKI_ENV` environment variable,
     * which sets it to Production no matter what.
     */
    val environment: Environment = if (System.getProperty("TSUBAKI_ENV") != null) Environment.Production else Environment.Development,

    /**
     * DSN details for logging requests and errors with Sentry
     */
    val sentryDSN: String? = null,

    /**
     * Database details
     */
    val database: DatabaseConfig = DatabaseConfig(),

    /**
     * Caching configuration options
     */
    val caching: CachingConfig = CachingConfig(),

    /**
     * Storage configuration options
     */
    val storage: StorageConfig = StorageConfig(),

    /**
     * Metrics with Prometheus configuration options
     */
    val metrics: MetricsConfig = MetricsConfig(),

    /**
     * Redis configuration options
     */
    val redis: RedisConfig = RedisConfig(),

    /**
     * Netty configuration options
     */
    val netty: NettyConfig? = null,

    /**
     * The hostname to use when using Tsubaki.
     */
    val host: String? = null,

    /**
     * JWT configuration options
     */
    val jwt: JwtConfig
)
