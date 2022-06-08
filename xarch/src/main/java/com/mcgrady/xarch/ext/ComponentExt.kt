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
package com.mcgrady.xarch.ext

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.mcgrady.xarch.databind.ActivityDataBinding
import com.mcgrady.xarch.databind.FragmentDataBinding
import com.mcgrady.xarch.databind.ViewGroupDataBinding
import com.mcgrady.xarch.databind.ViewHolderDataBinding
import com.mcgrady.xarch.viewbind.ActivityViewBinding
import com.mcgrady.xarch.viewbind.FragmentViewBinding
import com.mcgrady.xarch.viewbind.ViewGroupViewBinding

inline fun <reified T : ViewBinding> Activity.viewbind() =
    ActivityViewBinding(T::class.java, this)

inline fun <reified T : ViewDataBinding> Activity.databind(@LayoutRes resId: Int) =
    com.mcgrady.xarch.databind.ActivityDataBinding<T>(this, resId)

inline fun <reified T : ViewDataBinding> Activity.databind(
    @LayoutRes resId: Int,
    noinline block: T.() -> Unit
) = com.mcgrady.xarch.databind.ActivityDataBinding<T>(this, resId, block)

inline fun <reified T : ViewBinding> Fragment.viewbind() =
    FragmentViewBinding(T::class.java, this)

inline fun <reified T : ViewDataBinding> Fragment.databind() =
    FragmentDataBinding<T>(T::class.java, this)

inline fun <reified T : ViewDataBinding> Fragment.databind(noinline block: T.() -> Unit) =
    FragmentDataBinding<T>(T::class.java, this, block = block)

inline fun <reified T : ViewBinding> ViewGroup.viewbind() = ViewGroupViewBinding(
    bindingClass = T::class.java,
    inflater = LayoutInflater.from(context),
    viewGroup = this
)

inline fun <reified T : ViewBinding> ViewGroup.viewbind(viewGroup: ViewGroup) =
    ViewGroupViewBinding(
        bindingClass = T::class.java,
        inflater = LayoutInflater.from(context),
        viewGroup = viewGroup
    )

inline fun <reified T : ViewDataBinding> RecyclerView.ViewHolder.databind() =
    ViewHolderDataBinding(T::class.java)

inline fun <reified T : ViewDataBinding> RecyclerView.ViewHolder.databind(noinline block: (T.() -> Unit)) =
    ViewHolderDataBinding(T::class.java, block)

inline fun <reified T : ViewBinding> ViewGroup.databind(@LayoutRes resId: Int) =
    ViewGroupDataBinding(
        bindingClass = T::class.java,
        resId = resId,
        inflater = LayoutInflater.from(context),
        viewGroup = this
    )

inline fun <reified T : ViewBinding> ViewGroup.databind(
    @LayoutRes resId: Int,
    noinline block: (T.() -> Unit)
) = ViewGroupDataBinding(
    bindingClass = T::class.java,
    resId = resId,
    inflater = LayoutInflater.from(context),
    viewGroup = this,
    block = block
)
