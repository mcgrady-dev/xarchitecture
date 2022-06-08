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
package com.mcgrady.xarch.viewbind

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.viewbinding.ViewBinding
import com.mcgrady.xarch.ext.addLifecycleFragment
import com.mcgrady.xarch.ext.inflateMethod
import com.mcgrady.xarch.ext.inflateMethodWithViewGroup
import com.mcgrady.xarch.ext.observerWhenDestroyed
import java.lang.reflect.Method
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewGroupViewBinding<T : ViewBinding>(
    bindingClass: Class<T>,
    private val inflater: LayoutInflater,
    private val viewGroup: ViewGroup? = null
) : ReadOnlyProperty<ViewGroup, T> {

    private var binding: T? = null
    private var layoutInflater: Method = if (viewGroup != null) {
        bindingClass.inflateMethodWithViewGroup()
    } else {
        bindingClass.inflateMethod()
    }

    init {
        viewGroup?.apply {
            when (context) {
                is ComponentActivity -> {
                    (context as ComponentActivity).lifecycle.observerWhenDestroyed { destroyed() }
                }
                is Activity -> {
                    val activity = context as Activity
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        activity.observerWhenDestroyed { destroyed() }
                    } else {
                        activity.addLifecycleFragment { destroyed() }
                    }
                }
            }
        }
    }

    override fun getValue(thisRef: ViewGroup, property: KProperty<*>): T {
        return binding?.run {
            this
        } ?: let {
            @Suppress("UNCHECKED_CAST")
            val bind: T = viewGroup?.let {
                layoutInflater.invoke(null, inflater, viewGroup) as T
            } ?: let {
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

    private fun destroyed() {
        binding = null
    }
}
