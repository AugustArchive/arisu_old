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

package land.arisu.tsubaki.common.utils

import java.lang.NullPointerException
import kotlinx.serialization.json.*

object JsonUtils {
    fun convertToJsonElement(item: Any?): JsonElement = when (item) {
        null -> throw NullPointerException("Received `null` when converting non-nulls in JsonUtils#convertToJsonElement(Any). Consider using JsonUtils#convertToJsonElementOrNull(Any?) instead :3")
        is String -> JsonPrimitive(item)
        is Number -> JsonPrimitive(item)
        is Boolean -> JsonPrimitive(item)
        is List<*> -> JsonArray(item.map { convertToJsonElement(it) })
        is Map<*, *> -> JsonObject(item.map { (k, v) -> k.toString() to convertToJsonElement(v) }.toMap())
        else -> error("JsonUtils#convertToJsonElement(Any) doesn't support for $item")
    }

    fun convertToJsonElementOrNull(item: Any?): JsonElement = when (item) {
        null -> JsonNull
        is String -> JsonPrimitive(item)
        is Number -> JsonPrimitive(item)
        is Boolean -> JsonPrimitive(item)
        is List<*> -> JsonArray(item.map { convertToJsonElement(it) })
        is Map<*, *> -> JsonObject(item.map { (k, v) -> k.toString() to convertToJsonElement(v) }.toMap())
        else -> error("JsonUtils#convertToJsonElement(Any) doesn't support for $item")
    }
}
