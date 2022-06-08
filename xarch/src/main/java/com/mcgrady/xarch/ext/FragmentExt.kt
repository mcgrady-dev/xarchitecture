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

import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle

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
