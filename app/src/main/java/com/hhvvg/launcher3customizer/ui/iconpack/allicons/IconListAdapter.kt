package com.hhvvg.launcher3customizer.ui.iconpack.allicons

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.DifferCallback
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.hhvvg.launcher3customizer.data.IconDrawableItem
import com.hhvvg.launcher3customizer.data.IconItem
import com.hhvvg.launcher3customizer.databinding.LayoutIconDrawableItemBinding
import java.util.Objects

class IconListAdapter : PagingDataAdapter<IconItem, IconItemHolder>(IconDrawableDifferCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconItemHolder {
        val binding = LayoutIconDrawableItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IconItemHolder(binding.root)
    }

    override fun onBindViewHolder(holder: IconItemHolder, position: Int) {
        val binding = LayoutIconDrawableItemBinding.bind(holder.itemView)
        val item = getItem(position)!!

        Glide.with(binding.root.context).load(item).into(binding.iconDrawable)
    }
}

private class IconDrawableDifferCallback : ItemCallback<IconItem>() {
    override fun areItemsTheSame(oldItem: IconItem, newItem: IconItem): Boolean {
        return oldItem.resourceName == newItem.resourceName
    }

    override fun areContentsTheSame(oldItem: IconItem, newItem: IconItem): Boolean {
        return Objects.equals(oldItem, newItem)
    }

}

class IconItemHolder(itemView: View) : ViewHolder(itemView) {

}