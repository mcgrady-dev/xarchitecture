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

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment

const val LDPI: Int = DisplayMetrics.DENSITY_LOW
const val MDPI: Int = DisplayMetrics.DENSITY_MEDIUM
const val HDPI: Int = DisplayMetrics.DENSITY_HIGH

const val TVDPI: Int = DisplayMetrics.DENSITY_TV
const val XHDPI: Int = DisplayMetrics.DENSITY_XHIGH
const val XXHDPI: Int = DisplayMetrics.DENSITY_XXHIGH
const val XXXHDPI: Int = DisplayMetrics.DENSITY_XXXHIGH

const val MAXDPI: Int = 0xfffe

// 屏幕宽度(px)
inline val Context.screenWidth: Int
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val wm = systemService<WindowManager?>()
        wm?.currentWindowMetrics?.bounds?.width() ?: -1
    } else {
        resources.displayMetrics.widthPixels
    }

// 屏幕高度(px)
inline val Context.screenHeight: Int
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val wm = systemService<WindowManager?>()
        wm?.currentWindowMetrics?.bounds?.height() ?: -1
    } else {
        resources.displayMetrics.heightPixels
    }

// 屏幕的密度
inline val Context.density: Float
    get() = resources.displayMetrics.density

inline val Context.scaledDensity: Float
    get() = resources.displayMetrics.scaledDensity

// returns dip(dp) dimension value in pixels
fun Context.dp2px(value: Int): Int = (value * density).toInt()
fun Context.dp2px(value: Float): Int = (value * density).toInt()

// return sp dimension value in pixels
fun Context.sp(value: Int): Int = (value * scaledDensity).toInt()
fun Context.sp(value: Float): Int = (value * scaledDensity).toInt()

// converts px value into dip or sp
fun Context.px2dp(px: Int): Float = px.toFloat() / density
fun Context.px2sp(px: Int): Float = px.toFloat() / scaledDensity

fun Context.dimen(@DimenRes resource: Int): Int = resources.getDimensionPixelSize(resource)

// the same for the views
inline fun View.dp2px(value: Int): Int = context.dp2px(value)
inline fun View.dp2px(value: Float): Int = context.dp2px(value)
inline fun View.sp(value: Int): Int = context.sp(value)
inline fun View.sp(value: Float): Int = context.sp(value)
inline fun View.px2dp(px: Int): Float = context.px2dp(px)
inline fun View.px2sp(px: Int): Float = context.px2sp(px)
inline fun View.dimen(@DimenRes resource: Int): Int = context.dimen(resource)

// the same for Fragments
inline fun Fragment.dp2px(value: Int): Int = activity?.dp2px(value) ?: 0
inline fun Fragment.dp2px(value: Float): Int = activity?.dp2px(value) ?: 0
inline fun Fragment.sp(value: Int): Int = activity?.sp(value) ?: 0
inline fun Fragment.sp(value: Float): Int = activity?.sp(value) ?: 0
inline fun Fragment.px2dp(px: Int): Float = activity?.px2dp(px) ?: 0F
inline fun Fragment.px2sp(px: Int): Float = activity?.px2sp(px) ?: 0F
inline fun Fragment.dimen(@DimenRes resource: Int): Int = activity?.dimen(resource) ?: 0
