package com.lucane.studio.flux.data.model

data class Symptom(
    val id: Long = 0L,
    val name: String,
    val category: SymptomCategory,
    val isCustom: Boolean = false,
)