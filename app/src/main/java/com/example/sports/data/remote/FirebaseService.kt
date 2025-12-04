package com.example.sports.data.remote

import com.example.sports.data.remote.dto.SportPerformanceDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseService(
    private val firestore: FirebaseFirestore
) {
    fun observePerformances(): Flow<List<SportPerformanceDto>> = callbackFlow {
        val listener = firestore.collection("sport_performances")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val performances = snapshot?.documents?.mapNotNull {
                    it.toObject(SportPerformanceDto::class.java)?.copy(id = it.id)
                }.orEmpty()

                trySend(performances)
            }

        awaitClose { listener.remove() }
    }

    suspend fun getAllPerformances(): List<SportPerformanceDto> {
        return firestore.collection("sport_performances")
            .get()
            .await()
            .documents
            .mapNotNull { it.toObject(SportPerformanceDto::class.java)?.copy(id = it.id) }
    }

    suspend fun insertPerformance(performance: SportPerformanceDto) {
        firestore.collection("sport_performances")
            .add(performance)
            .await()
    }
}
