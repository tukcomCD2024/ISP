package com.project.how.roomdb.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.project.how.data_class.roomdb.RecentAirplane
import com.project.how.roomdb.converters.DateTypeConverter
import com.project.how.roomdb.dao.RecentAirplaneDao


@Database(entities = [RecentAirplane::class], version = 2, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun recentAirplaneDao(): RecentAirplaneDao
}