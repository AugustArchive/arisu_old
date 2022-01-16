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

import java.io.IOException
import land.arisu.tsubaki.common.kotlin.logging
import okhttp3.Interceptor
import okhttp3.Response

class LoggingInterceptor: Interceptor {
    private val logger by logging(this::class.java)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startTime = System.nanoTime()

        logger.info("Performing request: \"${request.method.uppercase()} ${request.url}\"")
        val res = chain.proceed(request)
        val endTime = System.nanoTime()

        logger.info("\"${request.method.uppercase()} ${request.url}\" [${res.code} ${res.message}] ~${(endTime - startTime) / 1e6}ms")
        return res
    }
}
