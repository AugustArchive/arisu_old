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

/**
 * Configuration for the server itself, which uses Netty under the hood.
 *
 * For more information, checkout the Ktor API docs:
 * https://api.ktor.io/ktor-server/ktor-server-netty/ktor-server-netty/io.ktor.server.netty/-netty-application-engine/-configuration/index.html
 */
@Serializable
data class NettyConfig(
    /**
     * Timeout in seconds for sending responses to client
     */
    val responseWriteTimeoutInSeconds: Int = 10,

    /**
     * Timeout in seconds for reading requests from client, "0" is infinite.
     */
    val requestReadTimeoutSeconds: Int = 0,

    /**
     * Size of the queue to store requests that cannot be immediately processed.
     */
    val requestQueueLimit: Int = 16,

    /**
     * Do not create separate call event groups and reuse worker group for processing calls
     */
    val shareWorkGroup: Boolean = false,

    /**
     * Enables TCP keep alive for connections.
     */
    val tcpKeepAlive: Boolean = false,

    /**
     * Number of concurrently running requests from the same HTTP pipeline
     */
    val runningLimit: Int = 10
)
