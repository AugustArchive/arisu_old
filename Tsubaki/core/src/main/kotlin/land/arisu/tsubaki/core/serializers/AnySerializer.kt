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

package land.arisu.tsubaki.core.serializers

import kotlinx.serialization.ContextualSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import land.arisu.tsubaki.common.utils.JsonUtils

// Based off https://github.com/Kotlin/kotlinx.serialization/issues/1019#issuecomment-841826881
class AnySerializer: KSerializer<Any?> {
    @OptIn(ExperimentalSerializationApi::class)
    override val descriptor: SerialDescriptor =
        ContextualSerializer(Any::class, null, emptyArray()).descriptor

    override fun deserialize(decoder: Decoder): Any? {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw IllegalArgumentException("Only JsonDecoder is supported.")

        return convertFromJsonElement(jsonDecoder.decodeJsonElement())
    }

    override fun serialize(encoder: Encoder, value: Any?) {
        val jsonEncoder = encoder as? JsonEncoder
            ?: throw IllegalArgumentException("Only JsonDecoder is supported.")

        jsonEncoder.encodeJsonElement(JsonUtils.convertToJsonElementOrNull(value))
    }

    private fun convertFromJsonElement(item: JsonElement): Any? = when (item) {
        JsonNull -> null

        is JsonArray -> item.map { JsonUtils.convertToJsonElementOrNull(it) }
        is JsonObject -> item.mapValues { convertFromJsonElement(it.value) }
        is JsonPrimitive -> when {
            item.isString -> item.content
            item.content == "true" || item.content == "false" -> item.content == "true"
            item.content.contains('.') -> item.content.toDouble()
            else -> item.content
        }
    }
}
