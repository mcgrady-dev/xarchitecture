/*
 * Copyright 2022 mcgrady
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mcgrady.xarchitecture.databind

import android.app.Activity
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.mcgrady.xarchitecture.delegate.ActivityDelegate
import com.mcgrady.xarchitecture.ext.addLifecycleFragment
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                activity.addLifecycleFragment { destroyed() }
            }

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
