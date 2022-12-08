package es.frantoribio.perfiles.interfaces

import es.frantoribio.perfiles.models.Perfil

interface PerfilOnClickListener {
    fun onClick(perfilId: Int)
    fun onFavoritePerfil(perfil: Perfil)
    fun onDeletePerfil(perfil: Perfil)
}