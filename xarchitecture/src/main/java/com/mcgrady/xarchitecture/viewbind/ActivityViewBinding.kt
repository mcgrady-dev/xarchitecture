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
package com.mcgrady.xarchitecture.viewbind

import android.app.Activity
import androidx.viewbinding.ViewBinding
import com.mcgrady.xarchitecture.delegate.ActivityDelegate
import com.mcgrady.xarchitecture.ext.addLifecycleFragment
import com.mcgrady.xarchitecture.ext.inflateMethod
import kotlin.reflect.KProperty

/**
 * Created by mcgrady on 2021/7/14.
 */
class ActivityViewBinding<T : ViewBinding>(
    bindingClass: Class<T>,
    val activity: Activity
) : ActivityDelegate<T>(activity) {

    private var layoutInflater = bindingClass.inflateMethod()

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        return binding?.run {
            this
        } ?: let {
            activity.addLifecycleFragment { destroyed() }

            val bind = layoutInflater.invoke(null, thisRef.layoutInflater) as T
            thisRef.setContentView(bind.root)
            return bind.apply { binding = this }
        }
    }
}
