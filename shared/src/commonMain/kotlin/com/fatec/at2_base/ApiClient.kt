package com.fatec.at2_base

import com.fatec.at2_base.model.NovaPlanta
import com.fatec.at2_base.model.Planta
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

expect fun createHttpClient(): HttpClient
const val SERVER_PORT = 8080

class ApiClient {
    private val client = createHttpClient()
    private val baseUrl = "http://${serverHost()}:$SERVER_PORT"

    suspend fun getPlantas(): List<Planta> =
        client.get("$baseUrl/plantas").body()

    suspend fun postPlanta(nova: NovaPlanta): Planta =
        client.post("$baseUrl/plantas") {
            contentType(ContentType.Application.Json)
            setBody(nova)
        }.body()
}