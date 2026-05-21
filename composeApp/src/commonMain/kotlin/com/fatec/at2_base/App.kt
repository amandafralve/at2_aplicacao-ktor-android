package com.fatec.at2_base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

@Composable
@Preview
fun App() {
    MaterialTheme {
        // remember guarda o valor entre as recomposicoes (quando o Compose redesenha a UI).
        // mutableStateOf cria um estado observavel; quando muda, a UI e redesenhada.
        var apiText by remember { mutableStateOf("-") }
        var isLoading by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        // Estas classes/funcoes estao no modulo shared e no mesmo package
        // (com.example.kmp_intro), por isso o Kotlin encontra sem import explicito.
        val api = remember { ApiClient() }
        val aluno = remember { Aluno("Ana", 20) }
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val greeting = remember { Greeting().greet() }
            Text("Compose: $greeting")
            Text("Variavel: $courseName")
            Text("Funcao: 2 + 3 = ${sum(2, 3)}")
            Text("Classe: ${aluno.resumo()}")
            Button(
                onClick = {
                    isLoading = true
                    scope.launch {
                        apiText = api.hello()
                        isLoading = false
                    }
                }
            ) {
                Text("GET /hello")
            }
            Button(
                onClick = {
                    isLoading = true
                    scope.launch {
                        apiText = api.echo("Oi do Compose")
                        isLoading = false
                    }
                }
            ) {
                Text("POST /echo")
            }
            Text("API: ${if (isLoading) "carregando..." else apiText}")
        }
    }
}