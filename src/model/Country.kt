package be.countries.database

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table


object Countries : Table(){
    val id : Column<Int> = integer("id").autoIncrement().primaryKey()
    val name : Column<String> = varchar("name", 255)
    val alpha2code : Column<String> = varchar("alpha2code", 2)
    val alpha3code : Column<String> = varchar("alpha3code", 3)
    val numericcode : Column<String> = varchar("numericcode", 3)
}

data class Country(val id: Int, val name: String, val alpha2code: String, val alpha3code: String, val numericcode: String)