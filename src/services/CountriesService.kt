package be.countries.services

import be.countries.database.Countries
import be.countries.database.Countries.alpha2code
import be.countries.database.Countries.alpha3code
import be.countries.database.Countries.id
import be.countries.database.Countries.name
import be.countries.database.Countries.numericcode
import be.countries.database.Country
import be.countries.database.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class CountriesService{

    suspend fun getAllCountries(): List<Country> = dbQuery {
        Countries.selectAll().map { toCountry(it) }
    }

    suspend fun getCountry(id: Int):Country? = dbQuery {
        Countries.select{
            (Countries.id eq id)}.mapNotNull { toCountry(it) }.singleOrNull()
    }

    suspend fun CreateCountry(country: Country) : Country? {
        var key = transaction  { Countries.insert {
            it[name] = country.name
            it[alpha2code] = country.alpha2code
            it[alpha3code] = country.alpha3code
            it[numericcode] = country.numericcode
        }  }.get(id)

        return getCountry(key!!)
     }

    suspend fun UpdateCountry(country: Country) : Country {
        transaction{
            Countries.update({Countries.id eq country.id}){
                it[name] = country.name
                it[alpha2code] = country.alpha2code
                it[alpha3code] = country.alpha3code
            }
        }
        return getCountry(country.id)!!

    }

    suspend fun DeleteCountry(id: Int?) {
        if(id != null){
            transaction{
                Countries.deleteWhere { Countries.id eq id }
            }
        }
    }

    private fun toCountry(row: ResultRow) : Country = Country(
        id = row[id] ,
        name = row[name],
        alpha2code = row[alpha2code],
        alpha3code = row[alpha3code],
        numericcode = row[numericcode]
    )

}