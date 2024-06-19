package com.project.how.data_class.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "recent_airplane_table")
data class RecentAirplane(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val image : String?,
    val des : String,
    val time1 : String,
    val time2 : String?,
    val skyscannerUrl : String,
    @ColumnInfo(name = "created_at") val createdAt : Date = Date()
)
