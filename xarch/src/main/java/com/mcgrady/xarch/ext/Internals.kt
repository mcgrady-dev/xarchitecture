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
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import java.io.Serializable

object Internals {

    const val NO_GETTER: String = "Property does not have a getter"

    fun applyRecursively(v: View, style: (View) -> Unit) {
        style(v)
        if (v is ViewGroup) {
            val maxIndex = v.childCount - 1
            for (i in 0..maxIndex) {
                v.getChildAt(i)?.let { applyRecursively(it, style) }
            }
        }
    }

    // Some constants not present in Android SDK v.15
    private object InternalConfiguration {
        const val SCREENLAYOUT_LAYOUTDIR_MASK = 0xC0
        const val SCREENLAYOUT_LAYOUTDIR_SHIFT = 6
        const val SCREENLAYOUT_LAYOUTDIR_RTL = 0x02 shl SCREENLAYOUT_LAYOUTDIR_SHIFT

        const val UI_MODE_TYPE_APPLIANCE = 0x05
        const val UI_MODE_TYPE_WATCH = 0x06
    }

    @JvmStatic
    fun <T> createIntent(ctx: Context?, clazz: Class<out T>, params: Array<out Pair<String, Any?>>): Intent {
        val intent = Intent(ctx, clazz)
        if (params.isNotEmpty()) fillIntentArguments(intent, params)
        return intent
    }

    @JvmStatic
    fun internalStartActivity(
        ctx: Context?,
        activity: Class<out Activity>,
        params: Array<out Pair<String, Any?>>
    ) {
        ctx?.startActivity(createIntent(ctx, activity, params))
    }

    @JvmStatic
    fun internalStartActivityForResult(
        act: Activity,
        activity: Class<out Activity>,
        requestCode: Int,
        params: Array<out Pair<String, Any?>>
    ) {
        act.startActivityForResult(createIntent(act, activity, params), requestCode)
    }

    @JvmStatic
    fun internalStartService(
        ctx: Context?,
        service: Class<out Service>,
        params: Array<out Pair<String, Any?>>
    ): ComponentName? = ctx?.startService(createIntent(ctx, service, params))

    @JvmStatic
    fun internalStopService(
        ctx: Context?,
        service: Class<out Service>,
        params: Array<out Pair<String, Any?>>
    ): Boolean = ctx?.stopService(createIntent(ctx, service, params)) ?: false

    @JvmStatic
    private fun fillIntentArguments(intent: Intent, params: Array<out Pair<String, Any?>>) {
        params.forEach {
            when (val value = it.second) {
                null -> intent.putExtra(it.first, null as Serializable?)
                is Int -> intent.putExtra(it.first, value)
                is Long -> intent.putExtra(it.first, value)
                is CharSequence -> intent.putExtra(it.first, value)
                is String -> intent.putExtra(it.first, value)
                is Float -> intent.putExtra(it.first, value)
                is Double -> intent.putExtra(it.first, value)
                is Char -> intent.putExtra(it.first, value)
                is Short -> intent.putExtra(it.first, value)
                is Boolean -> intent.putExtra(it.first, value)
                is Serializable -> intent.putExtra(it.first, value)
                is Bundle -> intent.putExtra(it.first, value)
                is Parcelable -> intent.putExtra(it.first, value)
                is Array<*> -> when {
                    value.isArrayOf<CharSequence>() -> intent.putExtra(it.first, value)
                    value.isArrayOf<String>() -> intent.putExtra(it.first, value)
                    value.isArrayOf<Parcelable>() -> intent.putExtra(it.first, value)
                    else -> throw RuntimeException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
                }
                is IntArray -> intent.putExtra(it.first, value)
                is LongArray -> intent.putExtra(it.first, value)
                is FloatArray -> intent.putExtra(it.first, value)
                is DoubleArray -> intent.putExtra(it.first, value)
                is CharArray -> intent.putExtra(it.first, value)
                is ShortArray -> intent.putExtra(it.first, value)
                is BooleanArray -> intent.putExtra(it.first, value)
                else -> throw RuntimeException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
            }
            return@forEach
        }
    }

    @JvmStatic
    inline fun <T> useCursor(cursor: Cursor, f: (Cursor) -> T): T = cursor.use(f)

    @JvmStatic
    fun <T : View> initiateView(ctx: Context, viewClass: Class<T>): T {
        fun getConstructor1() = viewClass.getConstructor(Context::class.java)
        fun getConstructor2() = viewClass.getConstructor(Context::class.java, AttributeSet::class.java)

        return try {
            getConstructor1().newInstance(ctx)
        } catch (e: NoSuchMethodException) {
            try {
                getConstructor2().newInstance(ctx, null)
            } catch (e: NoSuchMethodException) {
                throw RuntimeException("Can't initiate View of class ${viewClass.name}: can't find proper constructor")
            }
        }
    }
}
