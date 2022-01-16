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

package land.arisu.tsubaki.core.endpoints

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.micrometer.prometheus.PrometheusMeterRegistry
import land.arisu.tsubaki.common.config.Config
import land.arisu.tsubaki.core.routing.AbstractEndpoint

class MetricsEndpoint(
    private val metrics: PrometheusMeterRegistry,
    private val config: Config
): AbstractEndpoint("/metrics", HttpMethod.Get) {
    override suspend fun execute(call: ApplicationCall) {
        if (!config.metrics.enabled)
            return call.respondText("Cannot GET /metrics", status = HttpStatusCode.NotFound)

        call.respond(metrics.scrape())
    }
}
