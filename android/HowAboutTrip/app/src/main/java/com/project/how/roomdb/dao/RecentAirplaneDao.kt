package com.project.how.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.project.how.data_class.roomdb.RecentAirplane
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentAirplaneDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data : RecentAirplane) : Long

    @Update
    suspend fun update(data : RecentAirplane)

    @Query("SELECT * FROM recent_airplane_table")
    suspend fun getAllAirplanes() : List<RecentAirplane>

    @Query("SELECT * FROM recent_airplane_table ORDER BY created_at DESC LIMIT 10")
    suspend fun getRecentAirplanes() : List<RecentAirplane>

    @Query("DELETE FROM recent_airplane_table")
    suspend fun deleteAll()
}