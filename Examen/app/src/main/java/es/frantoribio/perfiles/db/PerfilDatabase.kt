package es.frantoribio.perfiles.db

import androidx.room.Database
import androidx.room.RoomDatabase
import es.frantoribio.perfiles.models.Perfil
import es.frantoribio.perfiles.db.dao.PerfilDao

@Database(entities = [Perfil::class], version = 2)

abstract class PerfilDatabase : RoomDatabase() {
    abstract fun perfilDao(): PerfilDao
}