package com.example.newsapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_articles")
data class  SavedArticle(
    @PrimaryKey val id: String,  // Unique identifier for the article (use a unique field like title+author or URL)
    val title: String,
    val description: String,
    val imageUrl: String?,
    val author: String?,
    val publishDate: String?
)
