package com.hhvvg.launcher3customizer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hhvvg.launcher3customizer.data.AppItem
import com.hhvvg.launcher3customizer.databinding.AppListItemBinding

class AllAppsAdapter(private val allApps: List<AppItem>) : RecyclerView.Adapter<AppsHolder>() {
    private var listener: ((AppItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppsHolder {
        val binding = AppListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppsHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return allApps.size
    }

    override fun onBindViewHolder(holder: AppsHolder, position: Int) {
        val binding = AppListItemBinding.bind(holder.itemView)
        val item = allApps[position]

        binding.appIcon.setImageDrawable(item.icon)
        binding.appLabel.text = item.label
        binding.componentName.text = item.component.flattenToString()

        binding.root.setOnClickListener {
            listener?.invoke(item)
        }
    }

    fun setOnItemClickListener(listener: (AppItem) -> Unit) {
        this.listener = listener
    }
}

class AppsHolder(itemView: View) : ViewHolder(itemView) {
}