package com.example.demomeow.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.example.demomeow.common.DebugLog
import com.example.demomeow.common.extension.ViewInflater
import com.example.demomeow.databinding.ActivityBaseBinding

abstract class BaseActivity<VB : ViewBinding>(val inflateBinding: ViewInflater<VB>) :
    AppCompatActivity() {

    protected lateinit var viewBD: VB
    protected lateinit var baseBiding: ActivityBaseBinding

    private var isErrorDialogShowing = false
    private val debugLog by lazy {
        DebugLog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBaseBinding()
    }

    private fun initBaseBinding() {
        baseBiding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(baseBiding.root)
        viewBD = inflateBinding(LayoutInflater.from(this), baseBiding.activityContent, true)
    }

    open fun setupView() {}
    open fun initData() {}
    open fun observeData() {}

    /**
     * This function is used to show loading or not
     * @param isLoading is [Boolean] value
     * */
    fun showLoading(isLoading: Boolean) {
        if (isLoading && baseBiding.activityError.isVisible) {
            baseBiding.activityError.isVisible = false
        }
        baseBiding.activityLoading.isVisible = isLoading
    }

    /**
     * This is the method to show error dialog
     * */
    fun showErrorDialog(message: String, name: String? = null) {
        if (isErrorDialogShowing) {
            debugLog.d("skip show if showing $name")
            return
        }
        isErrorDialogShowing = true
    }
}
