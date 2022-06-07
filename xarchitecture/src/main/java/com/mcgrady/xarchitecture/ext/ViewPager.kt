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
@file:Suppress("NOTHING_TO_INLINE")

package com.mcgrady.xarchitecture.ext

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

/**
 * Created by mcgrady on 2022/5/31.
 */

// 设置ViewPager2的过度滚动模式为绝不允许用户过度滚动此视图
// @kotlin.internal.InlineOnly
inline fun ViewPager2.overScrollNever() {
    val childView: View = this.getChildAt(0)
    if (childView is RecyclerView) {
        childView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }
}

// 设置ViewPager2的过度滚动模式为始终允许用户过度滚动此视图，前提是它是可以滚动的视图
// @kotlin.internal.InlineOnly
inline fun ViewPager2.overScrollAlways() {
    val childView: View = this.getChildAt(0)
    if (childView is RecyclerView) {
        childView.overScrollMode = RecyclerView.OVER_SCROLL_ALWAYS
    }
}

// 设置ViewPager2的过度滚动模式为仅当内容大到足以有意义地滚动时，才允许用户过度滚动此视图，前提是它是可以滚动的视图
// @kotlin.internal.InlineOnly
inline fun ViewPager2.overScrollIfContentScrolls() {
    val childView: View = this.getChildAt(0)
    if (childView is RecyclerView) {
        childView.overScrollMode = RecyclerView.OVER_SCROLL_IF_CONTENT_SCROLLS
    }
}
