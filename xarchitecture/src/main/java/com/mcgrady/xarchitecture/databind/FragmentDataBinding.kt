package com.mcgrady.xarchitecture.databind

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.mcgrady.xarchitecture.base.delegate.FragmentDelegate
import com.mcgrady.xarchitecture.ext.inflateMethod
import java.lang.IllegalStateException
import kotlin.reflect.KProperty

/**
 * Created by mcgrady on 2021/7/19.
 */
class FragmentDataBinding<T: ViewDataBinding>(
    bindingClass: Class<T>,
    val fragment: Fragment,
    private var block: (T.() -> Unit)? = null
) : FragmentDelegate<T>(fragment) {

    private val layoutInflater = bindingClass.inflateMethod()

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return binding?.run {
            return this
        } ?: let {
            val bind: T? = if (thisRef.view == null) {
                //这里为了兼容在 navigation 中使用 Fragment
                layoutInflater.invoke(null, thisRef.layoutInflater) as T
            } else {
                DataBindingUtil.bind(thisRef.requireView())
            }

            return bind?.apply {
                binding = this
                lifecycleOwner = fragment.viewLifecycleOwner
                block?.invoke(this)
                block = null
            } ?: throw IllegalStateException("Fragment $this binding cannot be null")
        }
    }
}