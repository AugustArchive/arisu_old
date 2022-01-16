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

package land.arisu.tsubaki.core.exposed.columns

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import land.arisu.tsubaki.common.utils.JsonUtils
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.koin.core.context.GlobalContext

class JsonColumn: ColumnType() {
    override fun sqlType(): String = "JSONB"

    override fun valueToDB(value: Any?): Any? {
        if (value is List<*>)
            error("Lists aren't supported in Json columns, use ArrayColumn instead.")

        if (value is Map<*, *>) {
            val koin = GlobalContext.get()
            val json = koin.get<Json>()
            val `object` = JsonObject(value.map { (k, v) ->
                k.toString() to JsonUtils.convertToJsonElement(v)
            }.toMap())

            return "'${json.encodeToString(JsonObject.serializer(), `object`)}'"
        } else {
            return super.valueToDB(value)
        }
    }

    override fun valueFromDB(value: Any): Any {
        if (value is Map<*, *>)
            return value

        error("Unable to correlate $value with kotlin.Map<?, ?>")
    }

    override fun notNullValueToDB(value: Any): Any {
        if (value is List<*>)
            error("Lists aren't supported in Json columns, use ArrayColumn instead.")

        if (value is Map<*, *>) {
            if (value.isEmpty())
                return "'{}'"

            val koin = GlobalContext.get()
            val json = koin.get<Json>()
            val `object` = JsonObject(value.map { (k, v) ->
                k.toString() to JsonUtils.convertToJsonElement(v)
            }.toMap())

            return "'${json.encodeToString(JsonObject.serializer(), `object`)}'"
        } else {
            return super.nonNullValueToString(value)
        }
    }
}

fun <T> Table.json(name: String): Column<Map<String, T>> = registerColumn(name, JsonColumn())
