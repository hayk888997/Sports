package com.example.sports.data.util

import android.database.sqlite.SQLiteException
import com.example.sports.domain.util.DataError
import com.google.firebase.FirebaseNetworkException
import io.mockk.mockk
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ErrorMappingTest {

    @Test
    fun `IOException maps to Network`() {
        val result = mapException(IOException("io"))
        assertEquals(DataError.Network, result)
    }

    @Test
    fun `FirebaseNetworkException maps to Network`() {
        val fake = mockk<FirebaseNetworkException>(relaxed = true)
        val result = mapException(fake)
        assertEquals(DataError.Network, result)
    }

    @Test
    fun `UnknownHostException maps to Network`() {
        val result = mapException(UnknownHostException("no internet"))
        assertEquals(DataError.Network, result)
    }

    @Test
    fun `ConnectException maps to Network`() {
        val result = mapException(ConnectException("connect fail"))
        assertEquals(DataError.Network, result)
    }

    @Test
    fun `SQLiteException maps to LocalFailed`() {
        val result = mapException(SQLiteException("db error"))
        assertEquals(DataError.LocalFailed, result)
    }

    @Test
    fun `unknown throwable maps to Unknown`() {
        val e = Throwable("unknown")
        val result = mapException(e)

        assertTrue(result is DataError.Unknown)
        assertEquals(e, result.exception)
    }
}
