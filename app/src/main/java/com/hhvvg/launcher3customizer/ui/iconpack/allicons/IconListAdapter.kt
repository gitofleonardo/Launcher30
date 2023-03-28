package com.hhvvg.launcher3customizer.ui.iconpack.allicons

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.DifferCallback
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hhvvg.launcher3customizer.data.IconDrawableItem
import com.hhvvg.launcher3customizer.databinding.LayoutIconDrawableItemBinding

class IconListAdapter : PagingDataAdapter<IconDrawableItem, IconItemHolder>(IconDrawableDifferCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconItemHolder {
        val binding = LayoutIconDrawableItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IconItemHolder(binding.root)
    }

    override fun onBindViewHolder(holder: IconItemHolder, position: Int) {
        val binding = LayoutIconDrawableItemBinding.bind(holder.itemView)
        val item = getItem(position)!!

        binding.iconDrawable.setImageDrawable(item.icon)
    }
}

private class IconDrawableDifferCallback : ItemCallback<IconDrawableItem>() {
    override fun areItemsTheSame(oldItem: IconDrawableItem, newItem: IconDrawableItem): Boolean {
        return oldItem.resourceName == newItem.resourceName
    }

    override fun areContentsTheSame(oldItem: IconDrawableItem, newItem: IconDrawableItem): Boolean {
        return oldItem == newItem
    }

}

class IconItemHolder(itemView: View) : ViewHolder(itemView) {

}