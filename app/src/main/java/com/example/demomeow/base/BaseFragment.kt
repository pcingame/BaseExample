package com.example.demomeow.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.viewbinding.ViewBinding
import com.example.demomeow.R
import com.example.demomeow.common.DebugLog
import com.example.demomeow.common.error.ApiException
import com.example.demomeow.common.extension.ViewInflater

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel>(
    val bindingInflater: ViewInflater<VB>
) : Fragment() {
    private var _binding: VB? = null
    protected val viewBD get() = _binding!!

    protected abstract val viewModel: VM

    protected val debugLog by lazy { DebugLog() }

    /**
     * animation for switch fragment
     */
    private val navOptions = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    /**
     * Override onCreateView function of Fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater(layoutInflater, container, false)
        return viewBD.root
    }

    /**
     * Override onViewCreated function of Fragment
     */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        initData()
        observeBase()
        observeData()
        debugLog.i(this::class.java.simpleName)
    }

    /**
     * Override onDestroyView function of Fragment
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected open fun setupView() {
        //implement
    }

    protected open fun initData() {
        //implement
    }

    private fun observeBase() {
        viewModel.mLoading.observe(viewLifecycleOwner) { shouldLoading ->
            (activity as? BaseActivity<*>)?.showLoading(shouldLoading)
        }
        viewModel.mError.observe(viewLifecycleOwner) { throwable ->
            val activity = activity as? BaseActivity<*> ?: return@observe
            val message = if (throwable is ApiException) {
                throwable.errorMessage ?: throwable.message
            } else {
                throwable.message
            }

            if (throwable is ApiException && message != null) {
                activity.showErrorDialog(message)
            }
        }
    }

    protected open fun observeData() {
        //implement
    }

    /**
     * Navigate with animation param
     * @param resId is resId screen
     */
    fun navigate(resId: Int, args: Bundle? = null, options: NavOptions = navOptions) {
        findNavController().navigate(resId, args, options)
    }

    /**
     * Navigate with animation param
     * @param resId is resId screen
     */
    fun navigate(directions: NavDirections, options: NavOptions = navOptions) {
        findNavController().navigate(directions, options)
    }

    /**
     * Navigate with animation param
     * @param resId is resId screen
     * @param args is data
     * @param resIdPopUpTo is resId screen
     */
    fun navigatePopupTo(resId: Int, args: Bundle? = null, resIdPopupTo: Int) {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(resIdPopupTo, true)
            .setEnterAnim(R.anim.fade_in)
            .setEnterAnim(R.anim.fade_out)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right)
            .build()
        findNavController().navigate(resId, args, navOptions)
    }

    /**
     * Navigate listener
     * @param resId is id screen
     * @param args is Data
     */
    fun navigateListener(resId: Int, args: Bundle? = null): View.OnClickListener {
        return Navigation.createNavigateOnClickListener(resId, args)
    }
}
