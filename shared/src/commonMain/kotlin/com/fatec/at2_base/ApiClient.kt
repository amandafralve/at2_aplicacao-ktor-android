package com.fatec.at2_base

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText

expect fun createHttpClient(): HttpClient
const val SERVER_PORT = 8080

class ApiClient {
    private val client = createHttpClient()
    private val baseUrl = "http://${serverHost()}:$SERVER_PORT"

    suspend fun hello(): String = client.get("$baseUrl/hello").bodyAsText()

    suspend fun echo(text: String): String = client.post("$baseUrl/echo") {
        setBody(text)
    }.bodyAsText()
}