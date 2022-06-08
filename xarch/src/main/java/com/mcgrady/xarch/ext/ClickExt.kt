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

package com.mcgrady.xarch.ext

import android.view.View
import androidx.annotation.CheckResult
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.CancellationException

/**
 * Created by mcgrady on 2022/5/31.
 */
@ExperimentalCoroutinesApi
inline fun <E> SendChannel<E>.safeOffer(value: E) = !isClosedForSend && try {
    trySend(value).isSuccess
} catch (e: CancellationException) {
    false
}

@ExperimentalCoroutinesApi
@CheckResult
inline fun View.clickFlow(): Flow<View> {
    return callbackFlow {
        setOnClickListener {
            safeOffer(it)
        }
        awaitClose { setOnClickListener(null) }
    }
}

@ExperimentalCoroutinesApi
inline fun View.click(lifecycle: LifecycleCoroutineScope, noinline onClick: (view: View) -> Unit) {
    clickFlow().onEach {
        onClick(this)
    }.launchIn(lifecycle)
}

// 延迟第一次点击事件
@ExperimentalCoroutinesApi
inline fun View.clickDelayed(
    lifecycle: LifecycleCoroutineScope,
    delayMillis: Long = 500,
    noinline onClick: (view: View) -> Unit
) {
    clickFlow().onEach {
        delay(delayMillis)
        onClick(this)
    }.launchIn(lifecycle)
}

/**
 * 防止多次点击
 *
 * Example：
 *
 * view.clickTrigger(lifecycleScope) {
 *     showShortToast("公众号：ByteCode")
 * }
 */
var lastMillis: Long = 0

@ExperimentalCoroutinesApi
inline fun View.clickTrigger(
    lifecycle: LifecycleCoroutineScope,
    intervalMillis: Long = 500,
    noinline onClick: (view: View) -> Unit
) {
    clickFlow().onEach {
        val curMillis = System.currentTimeMillis()
        if (curMillis - lastMillis < intervalMillis) {
            return@onEach
        }
        lastMillis = curMillis
        onClick(this)
    }.launchIn(lifecycle)
}
