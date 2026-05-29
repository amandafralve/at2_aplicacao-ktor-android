package com.fatec.at2_base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fatec.at2_base.model.Planta

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF2E7D32),
                    primaryContainer = Color(0xFFA5D6A7),
                    surface = Color(0xFFF1F8E9)
                )
            ) { JardimApp() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JardimApp(vm: PlantasViewModel = viewModel()) {
    val plantas by vm.plantas.collectAsStateWithLifecycle()
    val isLoading by vm.isLoading.collectAsStateWithLifecycle()
    val mensagem by vm.mensagem.collectAsStateWithLifecycle()
    var mostrarForm by remember { mutableStateOf(false) }
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(mensagem) {
        if (mensagem != null) {
            snackbar.showSnackbar(mensagem!!)
            vm.limparMensagem()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🌿 Meu Jardim") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    IconButton(onClick = { vm.carregarPlantas() }) {
                        Icon(Icons.Default.Refresh, "Atualizar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarForm = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, "Nova planta", tint = Color.White)
            }
        },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(8.dp))
                        Text("Regando o jardim...")
                    }
                }
                plantas.isEmpty() -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text("Jardim vazio \nToque em + para adicionar!")
                }
                else -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) { items(plantas) { PlantaCard(it) } }
            }
        }
        if (mostrarForm) {
            FormularioDialog(
                onDismiss = { mostrarForm = false },
                onConfirm = { nomePop, nomeCient, luminosidade, aguarCadaDias ->
                    vm.adicionarPlanta(nomePop, nomeCient, luminosidade, aguarCadaDias) { mostrarForm = false }
                },
                isLoading = isLoading
            )
        }
    }
}

@Composable
fun PlantaCard(planta: Planta) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(planta.nomePop, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium)
            Text(planta.nomeCient, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, label = { Text("💧 ${planta.aguarCadaDias} dias") })
                AssistChip(onClick = {}, label = { Text("☀️ ${planta.luminosidade}") })
            }
        }
    }
}

@Composable
fun FormularioDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, Int) -> Unit,
    isLoading: Boolean
) {
    var nomePop by remember { mutableStateOf("") }
    var nomeCient by remember { mutableStateOf("") }
    var luminosidade by remember { mutableStateOf("") }
    var aguarCadaDias by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nova Planta") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = nomePop, onValueChange = { nomePop = it },
                    label = { Text("Nome popular *") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = nomeCient, onValueChange = { nomeCient = it },
                    label = { Text("Nome científico") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = luminosidade, onValueChange = { luminosidade = it },
                    label = { Text("Ambiente que a planta precisa") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth())
                OutlinedTextField(
                    value = aguarCadaDias,
                    onValueChange = { aguarCadaDias = it.filter { c -> c.isDigit() } },
                    label = { Text("Regar a cada quantos dias?") },
                    singleLine = true, modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(nomePop, nomeCient, luminosidade, aguarCadaDias.toIntOrNull() ?: 7) },
                enabled = !isLoading
            ) {
                if (isLoading) CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp)
                else Text("Salvar")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}