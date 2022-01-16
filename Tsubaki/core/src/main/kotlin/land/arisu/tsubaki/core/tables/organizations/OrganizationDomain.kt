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

package land.arisu.tsubaki.core.tables.organizations

import land.arisu.tsubaki.core.exposed.tables.*
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.Column

/**
 * Represents table of mapped organization domains and if they were
 * verified by DNS or not.
 */
object OrganizationDomainTable: SnowflakeTable("organization_domains") {
    /**
     * If the domain was verified by DNS.
     */
    val verified: Column<Boolean> = bool("verified")

    /**
     * The domain URL
     */
    val domain: Column<String> = text("domain")
}

class OrganizationDomain(id: EntityID<String>): SnowflakeEntity(id) {
    companion object: SnowflakeEntityClass<OrganizationDomain>(OrganizationDomainTable)

    var verified by OrganizationDomainTable.verified
    var domain by OrganizationDomainTable.domain
}
