package com.example.sports.presentation.util

import com.example.sports.domain.util.DataError
import com.example.sports.domain.util.Result

//TODO, improve and maybe move to common
inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <T> Result<T>.onError(action: (DataError) -> Unit): Result<T> {
    if (this is Result.Error) action(error)
    return this
}
