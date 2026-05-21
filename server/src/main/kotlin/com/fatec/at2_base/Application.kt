package com.fatec.at2_base

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.receiveText
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    routing {
        get("/hello") {
            call.respondText("Olá! ${Greeting().greet()}")
        }

        post("/echo") {
            call.respondText(call.receiveText())
        }
    }
}