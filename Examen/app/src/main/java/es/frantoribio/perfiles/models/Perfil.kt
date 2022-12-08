package es.frantoribio.perfiles.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PerfilEntity")
data class Perfil(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String,
    var phone: String,
    var email: String,
    var photoUrl: String,
    var isFavorite: Boolean = false){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Perfil
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id
    }
}


