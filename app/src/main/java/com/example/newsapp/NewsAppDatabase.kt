package com.example.newsapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SavedArticle::class], version = 1)
abstract class NewsAppDatabase : RoomDatabase() {
    abstract fun savedArticleDao(): SavedArticleDao

    companion object {
        @Volatile
        private var INSTANCE: NewsAppDatabase? = null

        fun getDatabase(context: Context): NewsAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsAppDatabase::class.java,
                    "news_app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}