package com.test.test_karim2.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chareem.core.Constant
import com.test.test_karim2.data.model.*

@Database(entities = [Users::class, Film::class, FilmStok::class],
    views = [FilmAndFilmstokRelation::class], version = 6, exportSchema = true)

abstract class AppDatabase : RoomDatabase() {

    abstract fun userDAO(): UsersDAO
    abstract fun filmDAO(): FilmDAO
    abstract fun filmStokDAO(): FilmStokDAO

    companion object {
        var database: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (database == null) {
                synchronized(AppDatabase::class.java) {
                    if (database == null) {
                        database = buildDatabase(context)
                    }
                }
            }
            return database
        }

        fun buildDatabase(applicationContext: Context): AppDatabase? {
            return Room.databaseBuilder(applicationContext.applicationContext, AppDatabase::class.java, Constant.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                //.allowMainThreadQueries()
                .build()
        }
    }
}