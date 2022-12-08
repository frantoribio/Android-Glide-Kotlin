package es.frantoribio.perfiles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import es.frantoribio.perfil.R
import es.frantoribio.perfil.databinding.ActivityMainBinding
import es.frantoribio.perfiles.adapters.PerfilAdapter
import es.frantoribio.perfiles.db.PerfilApplication
import es.frantoribio.perfiles.fragments.EditPerfilFragment
import es.frantoribio.perfiles.interfaces.MainAux
import es.frantoribio.perfiles.interfaces.PerfilOnClickListener
import es.frantoribio.perfiles.models.Perfil
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), PerfilOnClickListener, MainAux {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: PerfilAdapter
    private lateinit var mGridLayout: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.fab.setOnClickListener { launchEditfragment() }
        setupRecyclerView()
    }

    private fun launchEditfragment(args: Bundle? = null) {
        val fragment = EditPerfilFragment()
        if (args != null) fragment.arguments = args
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.containerMain, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        hideFab()
    }

    private fun setupRecyclerView() {
        mAdapter = PerfilAdapter(mutableListOf(), this)
        mGridLayout = GridLayoutManager(this, 1)
        getPerfiles()

        binding.recycler.apply {
            setHasFixedSize(true)
            layoutManager = mGridLayout
            adapter = mAdapter
        }
    }

    private fun getPerfiles(){
        lifecycleScope.launch {
            val perfils = PerfilApplication.database.perfilDao().getAllPerfil()
            mAdapter.setPerfils(perfils)
        }
    }

    override fun onClick(perfilId: Int) {
        val args = Bundle()
        args.putInt("key_id", perfilId)
        launchEditfragment(args)
    }

    override fun onFavoritePerfil(perfil: Perfil) {
        perfil.isFavorite = !perfil.isFavorite
        lifecycleScope.launch {
            PerfilApplication.database.perfilDao().updatePerfil(perfil)
            mAdapter.update(perfil)
        }
    }

    override fun onDeletePerfil(perfil: Perfil) {
        lifecycleScope.launch {
            PerfilApplication.database.perfilDao().deletePerfil(perfil)
            mAdapter.delete(perfil)
        }
    }

    override fun hideFab(isVisible: Boolean) {
        if(isVisible) binding.fab.show() else binding.fab.hide()
    }

    override fun addPerfil(perfil: Perfil) {
        mAdapter.add(perfil)
    }

    override fun updatePerfil(perfil: Perfil) {
        mAdapter.update(perfil)
    }
}