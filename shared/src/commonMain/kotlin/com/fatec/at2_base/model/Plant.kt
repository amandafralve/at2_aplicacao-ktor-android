package com.fatec.at2_base.model

import kotlinx.serialization.Serializable

@Serializable
data class Planta(
    val id: Int,
    val nomePop: String,
    val nomeCient: String,
    val luminosidade: String,
    val aguarCadaDias: Int,
)

@Serializable
data class NovaPlanta(
    val nomePop: String,
    val nomeCient: String,
    val luminosidade: String,
    val aguarCadaDias: Int,
)