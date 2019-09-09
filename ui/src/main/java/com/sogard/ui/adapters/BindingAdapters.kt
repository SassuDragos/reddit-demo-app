package com.sogard.ui.adapters

import android.graphics.drawable.Drawable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

/**
 * Binding adapter that allows adding a custom divider to a RecyclerView list * */
@BindingAdapter("itemDecorationDrawable", "dividerOrientation")
fun RecyclerView.customItemDecoration(
    dividerDrawable: Drawable,
    dividerOrientation: Int
) {
    val itemDecoration =
        DividerItemDecoration(context, dividerOrientation).apply { setDrawable(dividerDrawable) }
    addItemDecoration(itemDecoration)
}