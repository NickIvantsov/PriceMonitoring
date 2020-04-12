package com.gmail.pricemonitoring.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gmail.pricemonitoring.dao.DeviceInfoDao
import com.gmail.pricemonitoring.dao.UrlDao
import com.gmail.pricemonitoring.entity.DeviceInfoEntity
import com.gmail.pricemonitoring.entity.UrlEntity


@Database(entities = [UrlEntity::class, DeviceInfoEntity::class], version = 5, exportSchema = false)
abstract class UrlRoomDatabase : RoomDatabase() {

    abstract fun urlDao(): UrlDao
    abstract fun deviceInfoDao(): DeviceInfoDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: UrlRoomDatabase? = null

        fun getDatabase(context: Context): UrlRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UrlRoomDatabase::class.java,
                    "url_database"
                ).addMigrations(MIGRATION_3_4,MIGRATION_4_5).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

val MIGRATION_3_4: Migration = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE url_table ADD COLUMN time TEXT DEFAULT '' NOT NULL")
    }
}
val MIGRATION_4_5: Migration = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE url_table ADD COLUMN time_milliseconds INTEGER DEFAULT 0 NOT NULL")
    }
}
