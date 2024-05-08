package com.example.newsapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SavedArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticle(article: SavedArticle)

    @Query("SELECT * FROM saved_articles")
    suspend fun getSavedArticles(): List<SavedArticle>

    @Delete
    suspend fun deleteArticle(article: SavedArticle)
}