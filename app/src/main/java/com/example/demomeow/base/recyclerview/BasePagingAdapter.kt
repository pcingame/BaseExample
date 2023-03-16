package com.example.demomeow.base.recyclerview

import androidx.paging.PagingDataAdapter
import com.example.demomeow.base.BaseDiffUtilItemCallBack

abstract class BasePagingAdapter<T : Any, VH : BaseViewHolder<T>> :
    PagingDataAdapter<T, VH>(BaseDiffUtilItemCallBack()) {
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position) ?: return
        holder.bindData(item)
    }
}
