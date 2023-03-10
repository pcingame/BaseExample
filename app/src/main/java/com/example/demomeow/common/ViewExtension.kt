package com.example.demomeow.common

import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

typealias ViewInflater<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

private var lastTimeClicked = 0L
private var lastTimeClickedId = 0

/**
 * This is method show in textView
 * @param visible is type View
 */
fun View.show(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun View.inVisibility(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}

fun View.click(defaultInterval: Int = 1000, onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener(defaultInterval) {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

class SafeClickListener(
    private var defaultInterval: Int = 1000,
    private val onSafeClick: (View) -> Unit
) : View.OnClickListener {
    override fun onClick(v: View) {
        if (lastTimeClickedId == v.id) {
            if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval){
                return
            } else if (SystemClock.elapsedRealtime() - lastTimeClicked < 200) {
                return
            }
        }

        lastTimeClicked = SystemClock.elapsedRealtime()
        lastTimeClickedId = v.id
        onSafeClick(v)
    }

}

fun View.setBackgroundTintColor(@ColorRes colorRes: Int) {
    backgroundTintList =
        ContextCompat.getColorStateList(context, colorRes)
}

fun View.setAnimationResource(@AnimRes resId: Int, duration: Long = 250) {
    kotlin.runCatching {
        val animation = AnimationUtils.loadAnimation(context, resId).apply {
            this.duration = duration
        }
        startAnimation(animation)
    }
}
