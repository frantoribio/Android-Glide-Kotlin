package es.frantoribio.perfiles.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import es.frantoribio.perfil.R
import es.frantoribio.perfil.databinding.ItemPerfilBinding
import es.frantoribio.perfiles.models.Perfil
import es.frantoribio.perfiles.interfaces.PerfilOnClickListener


class PerfilAdapter(private var perfils: MutableList<Perfil>,
                    private var listener: PerfilOnClickListener):
    RecyclerView.Adapter<PerfilAdapter.ViewHolder>() {
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_perfil, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val perfil = perfils.get(position)
        holder.bind(perfil)
        holder.setListener(perfil)
    }

    override fun getItemCount(): Int {
       return perfils.size
    }

    fun add(perfil: Perfil) {
        if (!perfils.contains(perfil)){
            perfils.add(perfil)
            notifyItemInserted(perfils.size-1)
        }
    }

    fun setPerfils(perfilsList: MutableList<Perfil>) {
        this.perfils = perfilsList
        notifyDataSetChanged()
    }

    fun update(perfil: Perfil) {
        val index = perfils.indexOf(perfil)
        if(index != -1) {
            perfils.set(index, perfil)
            notifyItemChanged(index)
        }
    }

    fun delete(perfil: Perfil) {
        val index = perfils.indexOf(perfil)
        if(index != -1) {
            perfils.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemPerfilBinding.bind(view)

        fun bind(perfil: Perfil){
            binding.textName.text = perfil.name
            binding.cbFavorite.isChecked = perfil.isFavorite
            Glide.with(mContext)
                .load(perfil.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.imgPhoto)
        }

        fun setListener(perfil: Perfil){
            binding.root.setOnClickListener { listener.onClick(perfil.id) }
            binding.root.setOnLongClickListener {
                listener.onDeletePerfil(perfil)
                true
            }
            binding.cbFavorite.setOnClickListener { listener.onFavoritePerfil(perfil) }
        }
    }
}