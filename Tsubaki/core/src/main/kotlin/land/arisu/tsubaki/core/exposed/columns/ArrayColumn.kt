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

// Modified version of https://github.com/LorittaBot/Loritta/blob/development/platforms/discord/legacy/src/main/java/com/mrpowergamerbr/loritta/utils/exposed/array.kt
// Released under AGPL 3

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.jdbc.JdbcConnectionImpl
import org.jetbrains.exposed.sql.transactions.TransactionManager

class ArrayColumn(private val columnType: ColumnType): ColumnType() {
    override fun sqlType(): String = "${columnType.sqlType()}[]"

    override fun valueToDB(value: Any?): Any? {
        if (value is List<*>) {
            val column = columnType.sqlType().split("(")[0]

            @Suppress("CAST_NEVER_SUCCEEDS")
            return (TransactionManager.current().connection as JdbcConnectionImpl).connection.createArrayOf(column, value.toTypedArray())
        } else {
            return super.valueToDB(value)
        }
    }

    override fun valueFromDB(value: Any): Any {
        if (value is java.sql.Array)
            return value.array

        if (value is Array<*>)
            return value.toList()

        error("Unable to correlate $value with java.sql.Array or kotlin.List<?>")
    }

    override fun notNullValueToDB(value: Any): Any {
        if (value is List<*>) {
            if (value.isEmpty())
                return "'{}'"

            val column = columnType.sqlType().split("(")[0]

            @Suppress("CAST_NEVER_SUCCEEDS")
            return (TransactionManager.current().connection as JdbcConnectionImpl).connection.createArrayOf(column, value.toTypedArray())
        } else {
            return super.notNullValueToDB(value)
        }
    }
}

class AnyOp(val expr1: Expression<*>, val expr2: Expression<*>): Op<Boolean>() {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        if (expr2 is OrOp) {
            queryBuilder.append("(").append(expr2).append(")")
        } else {
            queryBuilder.append(expr2)
        }

        queryBuilder.append(" = ANY (")
        if (expr1 is OrOp) {
            queryBuilder.append("(").append(expr1).append(")")
        } else {
            queryBuilder.append(expr1)
        }

        queryBuilder.append(")")
    }
}

class ContainsOp(expr1: Expression<*>, expr2: Expression<*>) : ComparisonOp(expr1, expr2, "@>")

infix fun <T, S> ExpressionWithColumnType<T>.any(t: S) : Op<Boolean> {
    if (t == null) {
        return IsNullOp(this)
    }
    return AnyOp(this, QueryParameter(t, columnType))
}

infix fun <T, S> ExpressionWithColumnType<T>.contains(arry: Array<in S>) : Op<Boolean> = ContainsOp(this, QueryParameter(arry, columnType))
fun <T> Table.array(name: String, columnType: ColumnType): Column<List<T>> = registerColumn(name, ArrayColumn(columnType))
