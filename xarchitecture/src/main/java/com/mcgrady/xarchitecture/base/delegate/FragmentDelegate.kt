package com.mcgrady.xarchitecture.base.delegate

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.mcgrady.xarchitecture.ext.observerWhenCreated
import com.mcgrady.xarchitecture.ext.observerWhenDestroyed
import kotlin.properties.ReadOnlyProperty

/**
 * Created by mcgrady on 2021/7/19.
 */
abstract class FragmentDelegate<T : ViewBinding>(
    fragment: Fragment
) : ReadOnlyProperty<Fragment, T> {
    protected var binding: T? = null

    init {

        /**
         * 最原始的处理的方案 监听 Fragment 的生命周期，会存在 Fragment 和 Fragment 中的 View 生命周期不一致问题
         * 详情查看 [issue][https://github.com/hi-dhl/Binding/issues/15]
         */
        fragment.lifecycle.observerWhenCreated {
            fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewOwner ->
                viewOwner.lifecycle.observerWhenDestroyed { destroyed() }
            }
        }
    }

    private fun destroyed() {
        Log.d(TAG, "set binding null")
        binding = null
    }

    companion object {
        const val TAG = "FragmentDelegate"
    }
}