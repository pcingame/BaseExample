package com.example.demomeow.base.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.viewbinding.ViewBinding
import com.example.demomeow.base.BaseDiffUtilItemCallBack
import com.example.demomeow.common.ViewInflater

class SimplePagingAdapter<ItemBinding : ViewBinding, T : Any>(
    private val onInflateItemBD: ViewInflater<ItemBinding>,
    private val onBind: ItemBinding.(T, Int) -> Unit = { _, _ -> },
) : PagingDataAdapter<T, BaseBindingHolder<ItemBinding, T>>(BaseDiffUtilItemCallBack()) {

    var delayClick = 200
    var onItemClick: ItemBinding.(T, Int) -> Unit = { _, _ -> }

    override fun onBindViewHolder(holder: BaseBindingHolder<ItemBinding, T>, position: Int) {
        val item = getItem(position) ?: return
        holder.bindData(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseBindingHolder<ItemBinding, T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemViewBinding = onInflateItemBD(layoutInflater, parent, false)
        return object : BaseBindingHolder<ItemBinding, T>(itemViewBinding) {
            init {
                delayClick = this@SimplePagingAdapter.delayClick
            }

            override fun bindData(itemData: T) {
                super.bindData(itemData)
                itemBD.onBind(itemData, absoluteAdapterPosition)
            }

            override fun onHandleItemClick(mainItem: T) {
                itemBD.onItemClick(mainItem, absoluteAdapterPosition)
            }
        }
    }
}
