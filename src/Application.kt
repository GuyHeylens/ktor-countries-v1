package be.countries

import be.countries.database.Country
import be.countries.database.DatabaseFactory
import be.countries.services.CountriesService
import io.ktor.application.*
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.JacksonConverter
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        register(ContentType.Application.Json, JacksonConverter())
    }

    DatabaseFactory.init()

    routing {
            val srv = CountriesService()

            get("/api/countries"){

                //call.respond(HttpStatusCode.OK, "Test")
                call.respond(HttpStatusCode.OK, srv.getAllCountries())
            }

            get("/api/countries/{id}"){
                val id = call.parameters["id"]?.toInt()
                srv.getCountry(id!!)?.let { it -> call.respond(it) }
            }

            post("/api/countries/"){
                val country = call.receive<Country>()
                call.respond(HttpStatusCode.Created, srv.CreateCountry(country)!!)
            }

            put("/api/countries/"){
                val country = call.receive<Country>()
                call.respond(HttpStatusCode.OK , srv.UpdateCountry(country))
            }

            delete("/api/countries/{id}"){
                val id = call.parameters["id"]?.toInt()
                srv.DeleteCountry(id)
                call.respond(HttpStatusCode.NotFound)
            }
        }

}

