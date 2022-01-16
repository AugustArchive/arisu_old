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

package land.arisu.tsubaki.core.modules.webhooks

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.response.*
import io.ktor.client.statement.*
import io.ktor.client.statement.HttpResponse
import io.ktor.util.date.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import land.arisu.tsubaki.common.kotlin.logging
import org.apache.commons.codec.digest.DigestUtils

/**
 * Represents a empty payload to send
 */
@Serializable
open class Payload

/**
 * Represents the payload to ping to the server
 * to validate that it's working successfully
 */
@Serializable
data class PingPayload(
    val message: String
): Payload()

/**
 * Represents the webhook event, which is determined using
 * the `X-Webhook-Event` header.
 */
enum class WebhookEvent(val event: String) {
    /**
     * Determines this event as a PING.
     */
    PING("ping")
}

/**
 * Module for handling webhooks, read about it [here](https://docs.arisu.land/api/webhooks).
 */
class WebhooksModule(private val http: HttpClient, private val json: Json) {
    private val logger by logging(this::class.java)
    private val responses = listOf(
        "tfw u get an automated message",
        "is this really built by furries?",
        "Hello there! You'll have a magical life ahead of you to make translation easy. :D",
        "uwu da owo",
        "these responses aren't made to be serious",
        "#kotlin",
        "do people really thing you can get away at looking at all the responses? probably...",
        "maybe being a closed sourced project was a good idea... right? probably not...",
        "microsoft if you're reading this, hire me please i need a job :pleading_face:",
        "please don't take this message seriously or the rest of the messages!",
        "if you're reading this, make sure to star the repository: https://github.com/arisuland/Tsubaki",
        "winterfox ".repeat(50)
    )

    /**
     * Creates a request and returns a object if it is successful
     * or not.
     */
    suspend fun <T: Payload> createRequest(
        url: String,
        project: String,
        event: WebhookEvent,
        payload: T,
        payloadAsString: String, // TODO: find out to get .serializer() from `payload`
        contentType: String = "application/json"
    ): Boolean {
        logger.info("Making a request to $url (project=$project;event=${event.event}) with content type $contentType...")

        val sha1 = DigestUtils.sha1Hex(payloadAsString)
        val sha256 = DigestUtils.sha256Hex(payloadAsString)

        val response: HttpResponse = http
            .post<HttpStatement>(url) {
                body = payload

                header("X-Payload-SHA256", sha256)
                header("X-Payload-SHA1", sha1)
                header("X-Webhook-Event", event.event)
                header("Content-Type", contentType)
            }.execute()

        val blob = response.readText()
        val log = buildString {
            append("POST $url ${response.version} | $project -> webhook event ${event.event}")
            appendLine()
            append("${response.status.value} ${response.status.description}:")
            appendLine()
            append(blob)
        }

        logger.info(log)
        return response.status.value <= 300 || response.status.value >= 400
    }

    suspend fun ping(
        url: String,
        project: String,
        contentType: String = "application/json"
    ): Boolean {
        val payload = PingPayload(message = responses.random())
        val asString = json.encodeToString(PingPayload.serializer(), payload)

        return createRequest(
            url = url,
            project = project,
            event = WebhookEvent.PING,
            payload = PingPayload(message = responses.random()),
            payloadAsString = asString,
            contentType = contentType
        )
    }
}
