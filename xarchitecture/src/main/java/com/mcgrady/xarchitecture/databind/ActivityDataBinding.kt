package com.mcgrady.xarchitecture.databind

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.mcgrady.xarchitecture.base.delegate.ActivityDelegate
import kotlin.reflect.KProperty

/**
 * Created by mcgrady on 2021/7/19.
 */
class ActivityDataBinding<T : ViewDataBinding>(
    val activity: Activity,
    @LayoutRes val resId: Int,
    private var block: (T.() -> Unit)? = null
) : ActivityDelegate<T>(activity) {

    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        return binding?.run {
            this
        } ?: let {
            //当继承 Activity 且 Build.VERSION.SDK_INT < Build.VERSION_CODES.Q 时触发
            addLifecycleFragment(activity)

            //获取 ViewDataBinding
            val bind: T = DataBindingUtil.setContentView(thisRef, resId)
            return bind.apply {
                if (activity is ComponentActivity) {
                    bind.lifecycleOwner = activity
                }
                binding = this
                block?.invoke(this)
                block = null
            }
        }
    }
}