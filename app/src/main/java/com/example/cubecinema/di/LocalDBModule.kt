package com.example.cubecinema.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cubecinema.model.Movie
import com.example.cubecinema.repository.local.MovieDao

@Database(entities = [Movie::class], version = 1)
abstract class LocalDBModule : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDBModule? = null
        private const val MOVIE_DB = "movie_database"

        fun getInstance(context: Context): LocalDBModule {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDBModule::class.java,
                    MOVIE_DB
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}