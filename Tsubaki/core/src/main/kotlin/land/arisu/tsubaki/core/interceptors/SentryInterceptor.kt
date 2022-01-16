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

package land.arisu.tsubaki.core.interceptors

import io.sentry.*
import java.io.IOException
import okhttp3.Interceptor
import okhttp3.Response

class SentryInterceptor: Interceptor {
    private val hub: IHub = HubAdapter.getInstance()

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = "${request.method} ${request.url}"
        val span = hub.span?.startChild("tsubaki.http.client", "Request: $url")
        val response: Response
        var code = 200

        try {
            span?.toSentryTrace()?.let {
                request = request
                    .newBuilder()
                    .addHeader(it.name, it.value)
                    .build()
            }

            response = chain.proceed(request)
            code = response.code
            span?.status = SpanStatus.fromHttpStatusCode(response.code)
            return response
        } catch (e: IOException) {
            span?.apply {
                this.throwable = e
                this.status = SpanStatus.INTERNAL_ERROR
            }

            throw e
        } finally {
            span?.finish()
            val breb = Breadcrumb.http(request.url.toString(), request.method, code)
            breb.level = if (code <= 300 || code >= 400)
                SentryLevel.ERROR
            else
                SentryLevel.INFO

            hub.addBreadcrumb(breb)
        }
    }
}
