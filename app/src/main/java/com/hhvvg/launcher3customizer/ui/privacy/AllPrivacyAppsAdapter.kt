package com.hhvvg.launcher3customizer.ui.privacy

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hhvvg.launcher3customizer.databinding.LayoutPrivacyAppBinding

class AllPrivacyAppsAdapter : Adapter<PrivacyAppViewHolder>() {

    private val allApps = ArrayList<PrivacyItem>()
    private var appClickListener: ((PrivacyItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrivacyAppViewHolder {
        return PrivacyAppViewHolder(LayoutPrivacyAppBinding.inflate(LayoutInflater.from(parent.context), parent, false).root)
    }

    override fun getItemCount(): Int = allApps.size

    override fun onBindViewHolder(holder: PrivacyAppViewHolder, position: Int) {
        val item = allApps[position]
        holder.bind(item)
        holder.binding.root.setOnClickListener {
            appClickListener?.invoke(item)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(apps: List<PrivacyItem>) {
        allApps.clear()
        allApps.addAll(apps)
        notifyDataSetChanged()
    }

    fun setOnAppClickListener(listener: (PrivacyItem) -> Unit) {
        appClickListener = listener
    }
}

class PrivacyAppViewHolder(view: View): ViewHolder(view) {
    val binding
        get() = LayoutPrivacyAppBinding.bind(itemView)

    fun bind(item: PrivacyItem) {
        val binding = binding
        binding.appIcon.setImageDrawable(item.icon)
        binding.appName.text = item.appName
    }
}