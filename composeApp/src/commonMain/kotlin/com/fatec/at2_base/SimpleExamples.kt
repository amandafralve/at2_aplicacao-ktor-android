package com.fatec.at2_base

val courseName = "LDDM"

fun sum(a: Int, b: Int): Int = a + b

class Aluno(val nome: String, val idade: Int) {
    fun resumo(): String = "$nome ($idade)"
}