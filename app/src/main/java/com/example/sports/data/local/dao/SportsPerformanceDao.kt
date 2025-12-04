package com.example.sports.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sports.data.local.entity.SportPerformanceEntity

@Dao
interface SportsPerformanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(performance: SportPerformanceEntity)

    @Query("SELECT * FROM sports_performance")
    suspend fun getAll(): List<SportPerformanceEntity>
}
