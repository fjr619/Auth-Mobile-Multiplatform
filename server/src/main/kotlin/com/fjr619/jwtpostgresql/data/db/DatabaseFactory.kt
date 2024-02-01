package com.fjr619.jwtpostgresql.data.db

import com.fjr619.jwtpostgresql.base.AppConfig
import com.fjr619.jwtpostgresql.data.db.schemas.StoryTable
import com.fjr619.jwtpostgresql.data.db.schemas.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Singleton

/**
 * DataBase Factory to connect to our database
 * @property appConfig AppConfig Configuration of our service
 */

@Singleton
class DatabaseFactory(
    private val appConfig: AppConfig
) {

    init {
        Database.connect(hikari())
        transaction {
            SchemaUtils.createMissingTablesAndColumns(tables = arrayOf(UserTable, StoryTable))
        }
    }

    private fun hikari(): HikariDataSource {
        val hikariConfig = HikariConfig()
        hikariConfig.driverClassName =
            appConfig.applicationConfiguration.propertyOrNull("database.driver")?.getString()
        hikariConfig.jdbcUrl =
            appConfig.applicationConfiguration.propertyOrNull("database.jdbcUrl")?.getString()
        hikariConfig.maximumPoolSize = 3
        hikariConfig.isAutoCommit = false
        hikariConfig.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        hikariConfig.validate()
        return HikariDataSource(hikariConfig)
    }

//        suspend fun <T> dbQuery(block: () -> T): T = withContext(Dispatchers.IO) {
//            transaction {
//                block()
//            }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}