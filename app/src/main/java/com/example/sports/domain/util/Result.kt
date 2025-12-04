package com.example.sports.domain.util

sealed class Result<out T> {
    data class Success<T>(val data: T): Result<T>()
    data class Error(val error: DataError): Result<Nothing>()
}
