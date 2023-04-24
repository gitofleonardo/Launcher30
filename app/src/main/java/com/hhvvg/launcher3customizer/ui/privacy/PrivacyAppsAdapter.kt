package com.hhvvg.launcher3customizer.ui.privacy

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hhvvg.launcher3customizer.databinding.LayoutPrivacyAppSelectionItemBinding

class PrivacyAppsAdapter : Adapter<PrivacyViewHolder>() {
    private val appItems = ArrayList<PrivacyItem>()

    private var onSelectionChangedListener: ((PrivacyItem, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrivacyViewHolder {
        return PrivacyViewHolder(LayoutPrivacyAppSelectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).root)
    }

    override fun getItemCount(): Int = appItems.size

    override fun onBindViewHolder(holder: PrivacyViewHolder, position: Int) {
        val item = appItems[position]
        holder.onBind(item).apply {
            root.setOnClickListener {
                privacyCheckbox.isChecked = !privacyCheckbox.isChecked
            }
            privacyCheckbox.setOnCheckedChangeListener { _, isChecked ->
                item.selected = isChecked
                onSelectionChangedListener?.invoke(item, isChecked)
            }
        }
    }

    fun setOnSelectionChangedListener(listener: (PrivacyItem, Boolean) -> Unit) {
        onSelectionChangedListener = listener
    }

    override fun onViewRecycled(holder: PrivacyViewHolder) {
        super.onViewRecycled(holder)
        holder.binding.privacyCheckbox.setOnCheckedChangeListener(null)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(items: List<PrivacyItem>) {
        appItems.clear()
        appItems.addAll(items)
        notifyDataSetChanged()
    }
}

class PrivacyViewHolder(view: View) : ViewHolder(view) {
    val binding
        get() = LayoutPrivacyAppSelectionItemBinding.bind(itemView)

    fun onBind(item: PrivacyItem): LayoutPrivacyAppSelectionItemBinding {
        val binding = LayoutPrivacyAppSelectionItemBinding.bind(itemView)
        binding.appIcon.setImageDrawable(item.icon)
        binding.appComponent.text = item.component.flattenToString()
        binding.appName.text = item.appName
        binding.privacyCheckbox.isChecked = item.selected
        return binding
    }
}