package org.jetbrains.squash.dialects.mysql

import org.jetbrains.squash.drivers.*
import org.jetbrains.squash.schema.*

class MySqlTransaction(connection: JDBCConnection) : JDBCTransaction(connection) {
    override fun databaseSchema(): DatabaseSchema = MySqlDatabaseSchema(this)
}