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

package land.arisu.tsubaki.core.modules.prometheus

import io.prometheus.client.CollectorRegistry
import io.prometheus.client.Counter
import io.prometheus.client.Gauge

class PrometheusModule {
    /**
     * Represents the registry for Micrometer
     */
    val registry: CollectorRegistry = CollectorRegistry()

    // #region Metrics
    val users: Gauge = Gauge
        .Builder()
        .name("tsubaki_users_registered")
        .help("Returns how many users have been registered in the database, including organizations")
        .register(this.registry)

    val requests: Counter = Counter.Builder()
        .name("tsubaki_requests")
        .help("How many requests have Ktor has seen")
        .labelNames("endpoint", "method")
        .register(this.registry)

    val avgRequestLatency: Gauge = Gauge.Builder()
        .name("tsubaki_average_request_latency")
        .help("Returns the average latency in milliseconds per request")
        .register(this.registry)
}
