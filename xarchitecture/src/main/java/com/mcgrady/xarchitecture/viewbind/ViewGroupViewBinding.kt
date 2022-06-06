package com.mcgrady.xarchitecture.viewbind

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.mcgrady.xarchitecture.ext.inflateMethod
import com.mcgrady.xarchitecture.ext.inflateMethodWithViewGroup
import java.lang.reflect.Method
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by mcgrady on 2021/12/28.
 */
class ViewGroupViewBinding<T : ViewBinding>(
    classes: Class<T>,
    private val inflater: LayoutInflater,
    private val viewGroup: ViewGroup? = null
) : ReadOnlyProperty<ViewGroup, T> {

    private var binding: T? = null
    private var layoutInflater: Method = if (viewGroup != null) {
        classes.inflateMethodWithViewGroup()
    } else {
        classes.inflateMethod()
    }

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: ViewGroup, property: KProperty<*>): T {
        return binding?.run {
            this
        } ?: let {
            val bind: T = if (viewGroup != null) {
                layoutInflater.invoke(null, inflater, viewGroup) as T
            } else {
                layoutInflater.invoke(null, inflater) as T
            }

            bind.apply {
                if (viewGroup == null) {
                    thisRef.addView(bind.root)
                }
                binding = this
            }
        }
    }

}