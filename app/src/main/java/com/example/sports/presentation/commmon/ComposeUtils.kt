package com.example.sports.presentation.commmon

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.example.sports.R

@Composable
fun isLandscape(): Boolean {
    val config = LocalConfiguration.current
    return config.orientation == Configuration.ORIENTATION_LANDSCAPE
}

@Composable
fun errorTextFor(error: AppError): String {
    return when (error) {

        // ---- Validation ----
        AppError.EmptyName ->
            stringResource(R.string.error_empty_name)

        AppError.EmptyVenue ->
            stringResource(R.string.error_empty_venue)

        AppError.InvalidDuration ->
            stringResource(R.string.error_invalid_duration)

        AppError.NegativeDuration ->
            stringResource(R.string.error_negative_duration)


        // ---- Local DB ----
        AppError.LocalDBFailed ->
            stringResource(R.string.error_local_failed)


        // ---- Remote Firestore ----
        AppError.RemoteFailed ->
            stringResource(R.string.error_remote_save_failed)

        AppError.RemotePermissionDenied ->
            stringResource(R.string.error_remote_permission_denied)

        AppError.RemoteNotFound ->
            stringResource(R.string.error_remote_not_found)


        // ---- Network ----
        AppError.NetworkUnavailable ->
            stringResource(R.string.error_network_unavailable)

        AppError.Timeout ->
            stringResource(R.string.error_timeout)


        // ---- Unknown ----
        AppError.Unknown ->
            stringResource(R.string.error_unknown)
    }
}
