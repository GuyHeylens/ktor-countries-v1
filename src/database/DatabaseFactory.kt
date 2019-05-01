package be.countries.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory{

    fun init(){

        Database.connect(hikari())
        transaction {
            create(Countries)
            Countries.insert {
                it[name] = "Belgium"
                it[alpha2code] = "BE"
                it[alpha3code] = "BEL"
                it[numericcode] = "056"
            }
            Countries.insert {
                it[name] = "France"
                it[alpha2code] = "FR"
                it[alpha3code] = "FRA"
                it[numericcode] = "250"
            }
        }

    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.h2.Driver"
        config.jdbcUrl = "jdbc:h2:mem:test"
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(
        block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }

}