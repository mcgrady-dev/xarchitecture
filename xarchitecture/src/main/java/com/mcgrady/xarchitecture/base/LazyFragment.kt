package com.mcgrady.xarchitecture.base

import androidx.fragment.app.Fragment

/**
 * Created by mcgrady on 2021/5/13.
 */
abstract class LazyFragment : Fragment() {

    protected var isLoaded = false

    override fun onResume() {

        super.onResume()
        if (!isLoaded && !isHidden) {
            lazyInit()
            isLoaded = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }

    private fun lazyInit() {
        initView()
        initData()
    }

    abstract fun initView()
    abstract fun initData()
}