package com.example.metanitdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [(User::class)], version = 1)
abstract class UserRoomDatabase : RoomDatabase() {
    abstract val userDao: UserDao

    companion object {
        private var INSTANCE: UserRoomDatabase? = null

        // Singleton базы данных Room
        fun getInstance(context: Context): UserRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserRoomDatabase::class.java,
                        "usersdb"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}