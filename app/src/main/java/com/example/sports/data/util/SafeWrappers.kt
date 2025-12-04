package com.example.sports.data.util

import android.database.sqlite.SQLiteException
import com.example.sports.domain.util.DataError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.sports.domain.util.Result
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.catch
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

fun mapException(t: Throwable): DataError = when (t) {

    // ---- NETWORK PROBLEMS ----
    is FirebaseNetworkException,
    is UnknownHostException,
    is ConnectException,
    is IOException -> DataError.Network

    // ---- ROOM / SQLITE ----
    is SQLiteException -> DataError.LocalFailed

    // ---- FIRESTORE ----
    is FirebaseFirestoreException -> when (t.code) {
        FirebaseFirestoreException.Code.PERMISSION_DENIED ->
            DataError.RemotePermissionDenied

        FirebaseFirestoreException.Code.NOT_FOUND ->
            DataError.RemoteNotFound

        FirebaseFirestoreException.Code.UNAVAILABLE ->
            DataError.RemoteUnavailable

        FirebaseFirestoreException.Code.DEADLINE_EXCEEDED ->
            DataError.Timeout

        FirebaseFirestoreException.Code.ALREADY_EXISTS,
        FirebaseFirestoreException.Code.ABORTED,
        FirebaseFirestoreException.Code.INTERNAL ->
            DataError.RemoteFailed

        else -> DataError.RemoteFailed
    }

    // ---- FIRESTORE QUOTA LIMIT ----
    is FirebaseTooManyRequestsException ->
        DataError.RemoteFailed

    // ---- UNKNOWN ----
    else -> DataError.Unknown(t)
}

suspend inline fun <T> safeCall(crossinline block: suspend () -> T): Result<T> {
    return try {
        Result.Success(block())
    } catch (e: Throwable) {
        Result.Error(mapException(e))
    }
}

fun <T> safeFlow(block: () -> Flow<T>): Flow<Result<T>> =
    block()
        .map<T, Result<T>> { Result.Success(it) }
        .catch { ex ->
            emit(Result.Error(mapException(ex)))
        }
