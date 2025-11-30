package com.example.sports.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sports.data.local.dao.SportsPerformanceDao
import com.example.sports.data.local.entity.SportPerformanceEntity

@Database(entities = [SportPerformanceEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun performanceDao(): SportsPerformanceDao
}
