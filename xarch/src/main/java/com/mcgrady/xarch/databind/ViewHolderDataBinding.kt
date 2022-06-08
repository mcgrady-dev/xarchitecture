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
package com.mcgrady.xarch.databind

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by mcgrady on 2022/1/7.
 */

class ViewHolderDataBinding<T : ViewDataBinding>(
    @Suppress("UNUSED_PARAMETER") bindingClass: Class<T>,
    private var block: (T.() -> Unit)? = null
) : ReadOnlyProperty<RecyclerView.ViewHolder, T> {

    private var binding: T? = null

    override fun getValue(thisRef: RecyclerView.ViewHolder, property: KProperty<*>): T {
        return binding?.run {
            this
        } ?: let {
            val bind = DataBindingUtil.bind<T>(thisRef.itemView) as T
            val value = block
            bind.apply {
                binding = this
                value?.invoke(this)
                block = null
            }
        }
    }

    private fun destroyed() {
        binding = null
    }
}
