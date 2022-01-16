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

package land.arisu.tsubaki.core.exposed.tables

import land.arisu.tsubaki.core.models.Snowflake
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.sql.Column

open class SnowflakeTable(name: String = ""): IdTable<String>(name) {
    override val id: Column<EntityID<String>> = text("id")
        .primaryKey()
        .clientDefault { Snowflake.generate() }
        .entityId()
}

abstract class SnowflakeEntity(id: EntityID<String>): Entity<String>(id)
abstract class SnowflakeEntityClass<out E: SnowflakeEntity>(
    table: IdTable<String>,
    entityType: Class<E>? = null
): EntityClass<String, E>(table, entityType)
