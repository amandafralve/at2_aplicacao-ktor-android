package com.fatec.at2_base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fatec.at2_base.model.NovaPlanta
import com.fatec.at2_base.model.Planta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlantasViewModel : ViewModel() {

    private val api = ApiClient()
    private val _plantas = MutableStateFlow<List<Planta>>(emptyList())
    val plantas = _plantas.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _mensagem = MutableStateFlow<String?>(null)
    val mensagem = _mensagem.asStateFlow()

    init { carregarPlantas() }

    fun carregarPlantas() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _plantas.value = api.getPlantas()
            } catch (e: Exception) {
                _mensagem.value = "Sem conexão com o servidor"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun adicionarPlanta(
        nomePop: String,
        nomeCient: String,
        luminosidade: String,
        aguarCadaDias: Int,
        onSucesso: () -> Unit
    ) {
        if (nomePop.isBlank()) {
            _mensagem.value = "O nome popular não pode ser vazio!"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                api.postPlanta(NovaPlanta(nomePop, nomeCient, luminosidade, aguarCadaDias))
                _mensagem.value = "'$nomePop' adicionada!"
                carregarPlantas()
                onSucesso()
            } catch (e: Exception) {
                _mensagem.value = "Erro ao salvar"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun limparMensagem() { _mensagem.value = null }
}