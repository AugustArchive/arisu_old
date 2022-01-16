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

package land.arisu.tsubaki.core.modules

import land.arisu.tsubaki.core.modules.auditlogs.AuditLogsModule
import land.arisu.tsubaki.core.modules.graphql.GraphQLModule
import land.arisu.tsubaki.core.modules.jwt.JwtModule
import land.arisu.tsubaki.core.modules.postgres.PostgresModule
import land.arisu.tsubaki.core.modules.prometheus.PrometheusModule
import land.arisu.tsubaki.core.modules.redis.RedisModule
import land.arisu.tsubaki.core.modules.storage.StorageModule
import land.arisu.tsubaki.core.modules.webhooks.WebhooksModule
import org.koin.dsl.module

val coreModules = module {
    single { PostgresModule(get()) }
    single { StorageModule(get()) }
    single { RedisModule(get()) }
    single { PrometheusModule() }
    single { AuditLogsModule() }
    single { WebhooksModule(get(), get()) }
    single { GraphQLModule() }
    single { JwtModule(get(), get(), get()) }
}
