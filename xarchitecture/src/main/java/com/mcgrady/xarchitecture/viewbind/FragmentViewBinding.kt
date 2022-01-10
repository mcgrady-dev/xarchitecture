package com.mcgrady.xarchitecture.viewbind

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.mcgrady.xarchitecture.base.delegate.FragmentDelegate
import com.mcgrady.xarchitecture.ext.bindMethod
import com.mcgrady.xarchitecture.ext.inflateMethod
import kotlin.reflect.KProperty

/**
 * Created by mcgrady on 2021/7/19.
 */
class FragmentViewBinding<T : ViewBinding>(
    bindingClass: Class<T>,
    fragment: Fragment
) : FragmentDelegate<T>(fragment) {

    private val layoutInflater = bindingClass.inflateMethod()
    private val bindView = bindingClass.bindMethod()

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return binding?.run {
            return this
        } ?: let {
            val bind: T = if (thisRef.view == null) {
                //这里为了兼容在 navigation 中使用 Fragment
                layoutInflater.invoke(null, thisRef.layoutInflater) as T
            } else {
                bindView.invoke(null, thisRef.view) as T
            }

            return bind.apply { binding = this }
        }
    }
}