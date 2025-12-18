package com.vliux.vocsaver.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Query("SELECT * FROM words ORDER BY added_ts DESC")
    fun getAllWords(): Flow<List<Word>>

    @Insert
    suspend fun insert(word: Word)
}