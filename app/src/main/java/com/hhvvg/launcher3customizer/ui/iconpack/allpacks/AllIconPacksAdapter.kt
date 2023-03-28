package com.hhvvg.launcher3customizer.ui.iconpack.allpacks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hhvvg.launcher3customizer.data.IconPackItem
import com.hhvvg.launcher3customizer.databinding.LayoutIconPackItemBinding

class AllIconPacksAdapter(private val allApps: List<IconPackItem>) : RecyclerView.Adapter<IconPackHolder>() {
    private var listener: ((IconPackItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconPackHolder {
        val binding = LayoutIconPackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IconPackHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return allApps.size
    }

    override fun onBindViewHolder(holder: IconPackHolder, position: Int) {
        val binding = LayoutIconPackItemBinding.bind(holder.itemView)
        val item = allApps[position]

        binding.appIcon.setImageDrawable(item.icon)
        binding.appLabel.text = item.label

        binding.root.setOnClickListener {
            listener?.invoke(item)
        }
    }

    fun setOnItemClickListener(listener: (IconPackItem) -> Unit) {
        this.listener = listener
    }
}

class IconPackHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
}