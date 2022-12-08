package es.frantoribio.perfiles.db.dao

import androidx.room.*
import es.frantoribio.perfiles.models.Perfil

@Dao
interface PerfilDao {

    @Query("SELECT * FROM PerfilEntity")
    suspend fun getAllPerfil(): MutableList<Perfil>

    @Query("SELECT * FROM PerfilEntity WHERE id = :id")
    suspend fun getPerfilById(id: Int): Perfil

    @Insert
    suspend fun addPerfil(perfil: Perfil)

    @Update
    suspend fun updatePerfil(perfil: Perfil)

    @Delete
    suspend fun deletePerfil(perfil: Perfil)
}