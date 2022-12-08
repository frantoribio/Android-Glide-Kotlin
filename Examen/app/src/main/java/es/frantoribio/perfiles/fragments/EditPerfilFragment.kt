package es.frantoribio.perfiles.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import es.frantoribio.perfil.R
import es.frantoribio.perfil.databinding.FragmentEditPerfilBinding
import es.frantoribio.perfiles.MainActivity
import es.frantoribio.perfiles.models.Perfil
import es.frantoribio.perfiles.db.PerfilApplication
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class EditPerfilFragment : Fragment() {
    private lateinit var binding: FragmentEditPerfilBinding
    private var mActivity: MainActivity? = null
    private var isEditMode: Boolean = false
    private var mPerfil: Perfil? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt("key_id", 0)
        if (id != null && id != 0){
            isEditMode = true
            getPerfil(id)
            Toast.makeText(activity, id.toString(), Toast.LENGTH_SHORT).show()
        }else {
            isEditMode = false
            mPerfil = Perfil(name = "", phone = "", photoUrl = "", email = "")
            Toast.makeText(activity, id.toString(), Toast.LENGTH_SHORT).show()
        }
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = getString(R.string.edit_perfil_title_add)
        setHasOptionsMenu(true)
        binding.editPhotoUrl.addTextChangedListener {
            Glide.with(this)
                .load(binding.editPhotoUrl.text.toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.imgPhoto)
        }
    }

    private fun getPerfil(id: Int) {
        lifecycleScope.launch {
            mPerfil = PerfilApplication.database.perfilDao().getPerfilById(id)
            if (this@EditPerfilFragment.mPerfil != null) setUIPerfil(mPerfil!!)
        }
    }

    private fun setUIPerfil(perfil: Perfil) {
        with(binding){
            editName.setText(perfil.name)
            editPhone.setText(perfil.phone)
            editEmail.setText(perfil.email)
            editPhotoUrl.setText(perfil.photoUrl)
            Glide.with(mActivity!!)
                .load(perfil.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imgPhoto)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_perfil, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            android.R.id.home -> {
                mActivity?.onBackPressed()
                true
            }
            R.id.action_save -> {
                if (mPerfil != null){
                    with(mPerfil!!){
                        name = binding.editName.text.toString().trim()
                        phone = binding.editPhone.text.toString().trim()
                        email = binding.editEmail.text.toString().trim()
                        photoUrl = binding.editPhotoUrl.text.toString()
                    }
                    lifecycleScope.launch {
                        if (isEditMode) PerfilApplication.database.perfilDao().updatePerfil(mPerfil!!)
                        else PerfilApplication.database.perfilDao().addPerfil(mPerfil!!)
                        if (isEditMode){
                            mActivity?.updatePerfil(mPerfil!!)
                            Snackbar.make(binding.root,
                                    "Perfil actualizado",
                                    Snackbar.LENGTH_SHORT).show()
                        }else {
                            mActivity?.addPerfil(mPerfil!!)
                            Snackbar.make(binding.root, "Perfil agregado correctamente",
                                Snackbar.LENGTH_SHORT)
                                .show()
                            mActivity?.onBackPressed()
                        }
                    }
                }
                true
            }
            else ->
            {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onDestroy() {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title = getString(R.string.app_name)
        mActivity?.hideFab(true)
        setHasOptionsMenu(false)
        super.onDestroy()
    }
}