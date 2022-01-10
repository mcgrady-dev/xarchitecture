package com.mcgrady.xarchitecture.ext

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.mcgrady.xarchitecture.databind.ActivityDataBinding
import com.mcgrady.xarchitecture.databind.FragmentDataBinding
import com.mcgrady.xarchitecture.databind.ViewHolderDataBinding
import com.mcgrady.xarchitecture.viewbind.ActivityViewBinding
import com.mcgrady.xarchitecture.viewbind.FragmentViewBinding
import com.mcgrady.xarchitecture.viewbind.ViewGroupViewBinding

/**
 * Created by mcgrady on 2021/7/19.
 */

inline fun <reified T : ViewBinding> Activity.viewbind() =
    ActivityViewBinding(T::class.java, this)

inline fun <reified T : ViewDataBinding> Activity.databind(@LayoutRes resId: Int) =
    ActivityDataBinding<T>(this, resId)

inline fun <reified T : ViewDataBinding> Activity.databind(
    @LayoutRes resId: Int,
    noinline block: T.() -> Unit
) = ActivityDataBinding<T>(this, resId, block)


inline fun <reified T : ViewBinding> Fragment.viewbind() =
    FragmentViewBinding(T::class.java, this)

inline fun <reified T : ViewDataBinding> Fragment.databind() =
    FragmentDataBinding<T>(T::class.java, this)

inline fun <reified T: ViewDataBinding> Fragment.databind(noinline block: T.() -> Unit) =
    FragmentDataBinding<T>(T::class.java, this, block = block)

inline fun <reified T : ViewBinding> ViewGroup.viewbind() = ViewGroupViewBinding(
    T::class.java,
    LayoutInflater.from(context)
)

inline fun <reified T : ViewBinding> ViewGroup.viewbind(viewGroup: ViewGroup) =
    ViewGroupViewBinding(
        T::class.java,
        LayoutInflater.from(context),
        viewGroup
    )

inline fun <reified T : ViewDataBinding> RecyclerView.ViewHolder.databind() =
    ViewHolderDataBinding(T::class.java)

inline fun <reified T : ViewDataBinding> RecyclerView.ViewHolder.databind(noinline block: (T.() -> Unit)) =
    ViewHolderDataBinding(T::class.java, block)