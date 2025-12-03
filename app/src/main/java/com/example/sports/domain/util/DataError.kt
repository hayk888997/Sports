package com.example.sports.domain.util

//TODO , improve
sealed class DataError : Throwable() {
    object Network : DataError()
    object NotFound : DataError()
    object Database : DataError()
    data class Unknown(val exception: Throwable) : DataError()
}
