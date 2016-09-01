@file:JvmName("Queries")
@file:JvmMultifileClass

package org.jetbrains.squash.query

import org.jetbrains.squash.definition.*
import org.jetbrains.squash.expressions.*
import org.jetbrains.squash.statements.*

open class QueryBuilder : Query {
    override val compound = mutableListOf<QueryCompound>()
    override val selection = mutableListOf<Expression<*>>()
    override val filter = mutableListOf<Expression<Boolean>>()
    override val order = mutableListOf<QueryOrder>()
    override val grouping = mutableListOf<Expression<*>>()
    override val having = mutableListOf<Expression<Boolean>>()
}

fun <Q : QueryBuilder> Q.copy(): QueryStatement = query().apply {
    compound.addAll(this@copy.compound)
    selection.addAll(this@copy.selection)
    filter.addAll(this@copy.filter)
    order.addAll(this@copy.order)
    grouping.addAll(this@copy.grouping)
    having.addAll(this@copy.having)
}

/**
 * Adds a join operation to the structure
 */
fun <Q : QueryBuilder> Q.innerJoin(target: Table, on: Expression<Boolean>): Q = apply {
    compound.add(QueryCompound.InnerJoin(target, on))
}

fun <Q : QueryBuilder> Q.leftJoin(target: Table, on: Expression<Boolean>): Q = apply {
    compound.add(QueryCompound.LeftOuterJoin(target, on))
}

fun <Q : QueryBuilder> Q.rightJoin(target: Table, on: Expression<Boolean>): Q = apply {
    compound.add(QueryCompound.RightOuterJoin(target, on))
}

/**
 * Adds [expression] to the list of fields to be retrieved from the result set
 */
fun <Q : QueryBuilder> Q.select(vararg expression: Expression<*>): Q = apply {
    selection.addAll(expression)
}

/**
 * Adds [table] to the structure
 */
fun <Q : QueryBuilder> Q.from(table: Table): Q = apply {
    compound.add(QueryCompound.From(table))
}

/**
 * Adds [predicate] to the Query, filtering result set by only rows matching it
 */
fun <Q : QueryBuilder> Q.where(predicate: Expression<Boolean>): Q = apply {
    filter.add(predicate)
}

fun <Q : QueryBuilder> Q.orderBy(expression: Expression<*>, ascending: Boolean = true): Q = apply {
    if (ascending)
        order.add(QueryOrder.Ascending(expression))
    else
        order.add(QueryOrder.Descending(expression))
}

fun <Q : QueryBuilder> Q.orderByDescending(expression: Expression<*>): Q = apply {
    order.add(QueryOrder.Descending(expression))
}

/**
 * Adds grouping [expression] to the Query
 */
fun <Q : QueryBuilder> Q.groupBy(vararg expression: Expression<*>): Q = apply {
    grouping.addAll(expression)
}

/**
 * Adds [predicate] to the Query, filtering grouped result set by only rows matching it
 */
fun <Q : QueryBuilder> Q.having(predicate: Expression<Boolean>): Q = apply {
    having.add(predicate)
}