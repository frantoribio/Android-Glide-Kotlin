package es.frantoribio.perfiles.db

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class PerfilApplication: Application() {
    companion object{
        lateinit var database: PerfilDatabase
    }

    override fun onCreate() {
        super.onCreate()
        val MIGRATIO_1_2 = object : Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE PerfilEntity " +
                        "ADD COLUMN photoUrl TEXT NOT NULL DEFAULT ''")
            }
        }

        database = Room.databaseBuilder(this,
                PerfilDatabase::class.java,
                "PerfilDatabase")
            .addMigrations(MIGRATIO_1_2)
            .build()
    }
}