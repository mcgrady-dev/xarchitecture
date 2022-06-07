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

import android.app.usage.UsageStatsManager
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

/**
 * Created by mcgrady on 2022/5/31.
 */

// @kotlin.internal.InlineOnly
inline fun View.visible() {
    visibility = View.VISIBLE
}

// @kotlin.internal.InlineOnly
inline fun View.gone() {
    visibility = View.GONE
}

// @kotlin.internal.InlineOnly
inline fun View.invisible() {
    visibility = View.INVISIBLE
}

inline fun Context.appStandbyBucket(context: Context): Int =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val usm = systemService<UsageStatsManager?>()
        usm?.appStandbyBucket ?: -1
    } else {
        -1
    }

// @kotlin.internal.InlineOnly
inline fun View.snackbar(
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT,
    actionName: String? = null,
    noinline block: (() -> Unit?)? = null
) {
    Snackbar.make(this, message, duration).run {
        if (actionName.isNotNullOrEmpty() && block != null) {
            setAction(actionName) {
                block()
            }
        }
        show()
    }
}

// @kotlin.internal.InlineOnly
inline fun View.snackbar(
    @StringRes stringResId: Int,
    duration: Int = Snackbar.LENGTH_SHORT,
    actionName: String? = null,
    noinline block: (() -> Unit?)? = null
) {
    Snackbar.make(this, stringResId, duration).run {
        if (actionName.isNotNullOrEmpty() && block != null) {
            setAction(actionName) {
                block()
            }
        }
        show()
    }
}

// @kotlin.internal.InlineOnly
inline fun View.roundCornerBackground(
    @ColorInt color: Int,
    cornerRadius: Float = 15F
) {
    background = GradientDrawable().apply {
        setColor(color)
        setCornerRadius(cornerRadius)
    }
}
