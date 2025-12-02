package com.example.sports.data.util

import android.database.sqlite.SQLiteException
import com.example.sports.domain.util.DataError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.sports.domain.util.Result
import kotlinx.coroutines.flow.catch
import retrofit2.HttpException
import java.io.IOException

fun mapException(t: Throwable): DataError = when (t) {
    is IOException -> DataError.Network
    is HttpException -> if (t.code() == 404) DataError.NotFound else DataError.Unknown(t)
    is SQLiteException -> DataError.Database
    is Exception -> DataError.Unknown(t)
    else -> DataError.Unknown(Throwable("Non-exception throwable: $t"))
}

suspend fun <T> safeCall(block: suspend () -> T): Result<T> =
    try {
        Result.Success(block())
    } catch (e: Throwable) {
        Result.Error(mapException(e))
    }

fun <T> safeFlow(block: () -> Flow<T>): Flow<Result<T>> =
    block()
        .map<T, Result<T>> { Result.Success(it) }
        .catch { ex ->
            emit(Result.Error(mapException(ex)))
        }
