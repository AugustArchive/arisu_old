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

package land.arisu.tsubaki.core.exposed.transations

import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import kotlinx.coroutines.future.await
import land.arisu.tsubaki.core.Tsubaki
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction

fun <T> asyncTransation(body: Transaction.() -> T): AsyncTransaction<T> = AsyncTransaction(Tsubaki.executorPool, body)

class AsyncTransaction<T>(
    private val pool: ExecutorService,
    private val body: Transaction.() -> T
) {
    fun execute(): CompletableFuture<T> {
        val future = CompletableFuture<T>()
        pool.execute {
            try {
                future.complete(transaction { body() })
            } catch (e: Exception) {
                future.completeExceptionally(e)
            }
        }

        return future
    }

    suspend fun await(): T = execute().await()
}
