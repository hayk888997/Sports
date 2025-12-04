package com.example.sports.domain.util

sealed class DataError {
    // Networking / connectivity
    object Network : DataError()
    object Timeout : DataError()

    // Firestore / remote
    object RemotePermissionDenied : DataError()
    object RemoteNotFound : DataError()
    object RemoteUnavailable : DataError()
    object RemoteFailed : DataError()

    // Local persistence (Room / SQLite)
    object LocalFailed : DataError()

    // Generic
    data class Unknown(val exception: Throwable) : DataError()
}
