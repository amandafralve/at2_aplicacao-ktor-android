package com.fatec.at2_base

import com.fatec.at2_base.model.NovaPlanta
import com.fatec.at2_base.model.Planta
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

val plantas = mutableListOf(
    Planta(1, "Samambaia",     "Nephrolepis exaltata", "Meia Sombra",  2),
    Planta(2, "Suculenta Rosa","Echeveria elegans",    "Sol Pleno",  14),
    Planta(3, "Monstera",      "Monstera deliciosa",   "Meia Sombra",  7),
    Planta(4, "Girassol", "Helianthus annuus", "Sol Pleno", 3),
)
var proximoId = 5

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {

        install(CORS) {
            anyHost()
            allowHeader(HttpHeaders.ContentType)
        }

        install(ContentNegotiation) {
            json(Json { prettyPrint = true })
        }

        routing {
            get("/plantas") {
                call.respond(plantas)
            }
            post("/plantas") {
                val nova = call.receive<NovaPlanta>()
                val planta = Planta(
                    id           = proximoId++,
                    nomePop      = nova.nomePop,
                    nomeCient    = nova.nomeCient,
                    luminosidade = nova.luminosidade,
                    aguarCadaDias = nova.aguarCadaDias,
                )
                plantas.add(planta)
                call.respond(HttpStatusCode.Created, planta)
            }
        }

    }.start(wait = true)
}