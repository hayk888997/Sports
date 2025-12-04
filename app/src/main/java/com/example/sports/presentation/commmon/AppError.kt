package com.example.sports.presentation.commmon

import com.example.sports.domain.util.DataError
sealed class AppError {

    // ---- Validation errors (UI-origin) ----
    object EmptyName : AppError()
    object EmptyVenue : AppError()
    object InvalidDuration : AppError()
    object NegativeDuration : AppError()

    // ---- Local DB errors ----
    object LocalDBFailed : AppError()

    // ---- Remote Firestore errors ----
    object RemoteFailed : AppError()
    object RemotePermissionDenied : AppError()
    object RemoteNotFound : AppError()

    // ---- Connectivity ----
    object NetworkUnavailable : AppError()
    object Timeout : AppError()

    // ---- Unknown ----
    object Unknown : AppError()
}

fun DataError.toAppError(): AppError {
    return when (this) {

        // ---- Networking ----
        DataError.Network -> AppError.NetworkUnavailable
        DataError.Timeout -> AppError.Timeout

        // ---- Firestore (remote) ----
        DataError.RemotePermissionDenied -> AppError.RemotePermissionDenied
        DataError.RemoteNotFound -> AppError.RemoteNotFound
        DataError.RemoteUnavailable -> AppError.NetworkUnavailable
        DataError.RemoteFailed -> AppError.RemoteFailed

        // ---- Local DB ----
        DataError.LocalFailed -> AppError.LocalDBFailed

        // ---- Unknown ----
        is DataError.Unknown -> AppError.Unknown
    }
}
