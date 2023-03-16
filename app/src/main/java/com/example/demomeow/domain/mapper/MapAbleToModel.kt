package com.example.demomeow.domain.mapper

interface MapAbleToModel<Model> {
    fun toModel(): Model
}
