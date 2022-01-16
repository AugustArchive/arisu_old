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

package land.arisu.tsubaki.core.modules.postgres

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import land.arisu.tsubaki.common.config.Config
import land.arisu.tsubaki.common.kotlin.logging
import land.arisu.tsubaki.core.tables.*
import land.arisu.tsubaki.core.tables.organizations.OrganizationDomainTable
import land.arisu.tsubaki.core.tables.organizations.OrganizationMemberTable
import land.arisu.tsubaki.core.tables.organizations.OrganizationTable
import okhttp3.internal.closeQuietly
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Represents the module for PostgreSQL.
 */
class PostgresModule(config: Config) {
    // The data source for pooling connections with Exposed
    private val dataSource: HikariDataSource

    // The database object itself
    private val database: Database
    private val logger by logging(this::class.java)

    init {
        logger.info("Configuring PostgreSQL...")

        val hikariConfig = HikariConfig().apply {
            jdbcUrl = config.database.url ?: "jdbc:postgresql://${config.database.host}:${config.database.port}/${config.database.database}"
            username = config.database.username
            password = config.database.password
            schema = config.database.schema
            driverClassName = "org.postgresql.Driver"
        }

        dataSource = HikariDataSource(hikariConfig)
        database = Database.connect(dataSource)

        transaction {
            // Add the SLF4J logger
            addLogger(Slf4jSqlDebugLogger)

            // Create/updates tables and columns
            SchemaUtils.createMissingTablesAndColumns(
                AuditLogsTable,
                ContributionTable,
                ContributionCommentTable,
                ProjectTable,
                UserTable,
                WebhookTable,
                OrganizationTable,
                OrganizationDomainTable,
                OrganizationMemberTable,
                AccessTokensTable
            )
        }

        logger.info("Configured and connected to PostgreSQL ${database.version} successfully")
    }

    fun close() {
        logger.warn("Closing connection with PostgreSQL...")
        dataSource.closeQuietly()
    }
}
