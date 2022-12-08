package es.frantoribio.perfiles.interfaces

import es.frantoribio.perfiles.models.Perfil

interface MainAux {
    fun hideFab(isVisible: Boolean = false)
    fun addPerfil(perfil: Perfil)
    fun updatePerfil(perfil: Perfil)
}