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
package com.mcgrady.xarchitecture.ext

import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle

/**
 * Created by mcgrady on 2021/6/10.
 */

/**
 * 加载根Fragment
 * @param containerViewId 布局id
 * @param rootFragment  根fragment
 */
fun Fragment.loadRootFragment(@IdRes containerViewId: Int, rootFragment: Fragment) {
    loadFragmentsTransaction(containerViewId, 0, childFragmentManager, rootFragment)
}

/**
 * 加载同级的Fragment
 * @param containerViewId 布局id
 * @param showPosition  默认显示的角标
 * @param fragments    加载的fragment
 */
fun Fragment.loadFragments(
    @IdRes containerViewId: Int,
    showPosition: Int = 0,
    vararg fragments: Fragment
) {
    loadFragmentsTransaction(containerViewId, showPosition, childFragmentManager, *fragments)
}

/**
 * 显示目标fragment，并隐藏其他fragment
 * 当调用 Fragment.showHideFragment ，确保已经先调用 Fragment.loadFragments
 * @param showFragment 需要显示的fragment
 */
fun Fragment.showHideFragment(showFragment: Fragment) {
    showHideFragmentTransaction(childFragmentManager, showFragment)
}

/**
 * 加载根Fragment
 * @param containerViewId 布局id
 * @param rootFragment  根fragment
 */
fun FragmentActivity.loadRootFragment(@IdRes containerViewId: Int, rootFragment: Fragment) {
    loadFragmentsTransaction(containerViewId, 0, supportFragmentManager, rootFragment)
}

/**
 * 加载同级的Fragment
 * @param containerViewId 布局id
 * @param showPosition  默认显示的角标
 * @param fragments    加载的fragment
 */
fun FragmentActivity.loadFragments(
    @IdRes containerViewId: Int,
    showPosition: Int = 0,
    vararg fragments: Fragment
) {
    loadFragmentsTransaction(containerViewId, showPosition, supportFragmentManager, *fragments)
}

/**
 * 显示目标fragment，并隐藏其他fragment，调用前确保已经先调用 FragmentActivity.loadFragments
 * @param showFragment 需要显示的fragment
 */
fun FragmentActivity.showHideFragment(showFragment: Fragment) {
    showHideFragmentTransaction(supportFragmentManager, showFragment)
}

/**
 * 使用add+show+hide模式加载fragment
 *
 * 默认显示位置[showPosition]的Fragment，最大Lifecycle为Lifecycle.State.RESUMED
 * 其他隐藏的Fragment，最大Lifecycle为Lifecycle.State.STARTED
 *
 *@param containerViewId 容器id
 *@param showPosition  fragments
 *@param fragmentManager FragmentManager
 *@param fragments  控制显示的Fragments
 */
private fun loadFragmentsTransaction(
    @IdRes containerViewId: Int,
    showPosition: Int,
    fragmentManager: FragmentManager,
    vararg fragments: Fragment
) {
    if (fragments.isNotEmpty()) {
        fragmentManager.beginTransaction().apply {
            for (index in fragments.indices) {
                val fragment = fragments[index]
                add(containerViewId, fragment, fragment.javaClass.name)
                if (showPosition == index) {
                    setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
                } else {
                    hide(fragment)
                    setMaxLifecycle(fragment, Lifecycle.State.STARTED)
                }
            }
        }.commit()
    } else {
        throw IllegalStateException(
            "fragments must not empty"
        )
    }
}

/**
 * 显示需要显示的Fragment[showFragment]，并设置其最大Lifecycle为Lifecycle.State.RESUMED。
 * 同时隐藏其他Fragment,并设置最大Lifecycle为Lifecycle.State.STARTED
 * @param fragmentManager
 * @param showFragment
 */
private fun showHideFragmentTransaction(fragmentManager: FragmentManager, showFragment: Fragment) {
    fragmentManager.beginTransaction().apply {
        show(showFragment)
        setMaxLifecycle(showFragment, Lifecycle.State.RESUMED)

        // 获取其中所有的fragment,其他的fragment进行隐藏
        val fragments = fragmentManager.fragments
        for (fragment in fragments) {
            if (fragment != showFragment) {
                hide(fragment)
                setMaxLifecycle(fragment, Lifecycle.State.STARTED)
            }
        }
    }.commit()
}

fun DialogFragment.isShowing(): Boolean {
    return dialog?.isShowing ?: false
}

// inline fun <reified T : Any> Fragment.intent(
//    key: String,
//    crossinline defaultValue: () -> Unit
// ) = lazy(LazyThreadSafetyMode.NONE) {
//    val value = arguments?.get(key)
//    if (value is T) value else defaultValue()
// }
//
// /**
// * Example:
// *
// * ```
// * private val userPassword by intent<String>(KEY_USER_PASSWORD)
// * ```
// */
// inline fun <reified T : Any> Fragment.intent(
//    key: String
// ) = lazy(LazyThreadSafetyMode.NONE) {
//    arguments?.get(key)
// }
//
// /**
// * Example:
// *
// * ```
// * fun newInstance(): Fragment {
// *     return LoginFragment().makeBundle(
// *         ProfileActivity.KEY_USER_NAME to "ByteCode",
// *         ProfileActivity.KEY_USER_PASSWORD to "1024",
// *         ProfileActivity.KEY_PEOPLE_PARCELIZE to PeopleModel("hi-dhl")
// *     )
// * }
// * ```
// */
// //@kotlin.internal.InlineOnly
// inline fun Fragment.makeBundle(
//    vararg params: Pair<String, Any>
// ): Fragment {
//    makeBundle {
//        params
//    }
//    return this
// }
//
// /**
// * Example:
// *
// * ```
// * fun newInstance(): Fragment {
// *     return LoginFragment().makeBundle {
// *         arrayOf(
// *             KEY_USER_NAME to "ByteCode",
// *             KEY_USER_PASSWORD to "1024",
// *             KEY_PEOPLE_PARCELIZE to PeopleModel("hi-dhl")
// *         )
// *     }
// * }
// * ```
// */
// inline fun Fragment.makeBundle(
//    params: () -> Array<out Pair<String, Any>>
// ): Fragment {
//    return apply {
//        arguments = Bundle().apply {
//            val arry = params()
//            for ((_, value) in arry.withIndex()) {
//                makeParams(value)
//            }
//        }
//    }
// }
//
//
// //@kotlin.internal.InlineOnly
// inline fun Bundle.makeParams(it: Pair<String, Any>) {
//    when (val value = it.second) {
//        is Int -> putInt(it.first, value)
//        is Long -> putLong(it.first, value)
//        is CharSequence -> putCharSequence(it.first, value)
//        is String -> putString(it.first, value)
//        is Float -> putFloat(it.first, value)
//        is Double -> putDouble(it.first, value)
//        is Char -> putChar(it.first, value)
//        is Short -> putShort(it.first, value)
//        is Boolean -> putBoolean(it.first, value)
//        is java.io.Serializable -> putSerializable(it.first, value)
//        is Bundle -> putBundle(it.first, value)
//        is Parcelable -> putParcelable(it.first, value)
//        is IntArray -> putIntArray(it.first, value)
//        is LongArray -> putLongArray(it.first, value)
//        is FloatArray -> putFloatArray(it.first, value)
//        is DoubleArray -> putDoubleArray(it.first, value)
//        is CharArray -> putCharArray(it.first, value)
//        is ShortArray -> putShortArray(it.first, value)
//        is BooleanArray -> putBooleanArray(it.first, value)
//        else -> throw IllegalArgumentException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
//    }
// }
