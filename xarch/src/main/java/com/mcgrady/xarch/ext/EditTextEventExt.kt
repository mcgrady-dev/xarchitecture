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
@file: Suppress("NOTHING_TO_INLINE")

package com.mcgrady.xarch.ext

import android.text.Editable
import android.widget.EditText
import androidx.annotation.CheckResult
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doBeforeTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
@CheckResult
inline fun EditText.doAfterTextChangeFlow(): Flow<Editable?> = callbackFlow {
    val textChangedListener = doAfterTextChanged {
        safeOffer(it)
    }
    awaitClose {
        removeTextChangedListener(textChangedListener)
    }
}

@ExperimentalCoroutinesApi
@CheckResult
inline fun EditText.doBeforeTextChangeFlow(): Flow<CharSequence?> = callbackFlow {
    val textChangeListener = doBeforeTextChanged { text, _, _, _ ->
        safeOffer(text)
    }
    awaitClose { removeTextChangedListener(textChangeListener) }
}

@ExperimentalCoroutinesApi
@CheckResult
inline fun EditText.doTextChangedFlow(): Flow<CharSequence?> = callbackFlow {
    val textChangeListener = doOnTextChanged { text, _, _, _ ->
        safeOffer(text)
    }
    awaitClose { removeTextChangedListener(textChangeListener) }
}

@ExperimentalCoroutinesApi
inline fun EditText.textChange(
    lifecycle: LifecycleCoroutineScope,
    crossinline onChange: (s: String) -> Unit
) {
    doAfterTextChangeFlow()
        .onEach {
            onChange(it.toString())
        }.launchIn(lifecycle)
}

@ExperimentalCoroutinesApi
@FlowPreview
inline fun EditText.textChange(
    lifecycle: LifecycleCoroutineScope,
    timeoutMillis: Long = 500,
    crossinline onChange: (s: String) -> Unit
) {
    doAfterTextChangeFlow()
        .debounce(timeoutMillis)
        .onEach {
            onChange(it.toString())
        }.launchIn(lifecycle)
}

@ExperimentalCoroutinesApi
inline fun EditText.textChangeBefore(
    lifecycle: LifecycleCoroutineScope,
    crossinline onChange: (s: String) -> Unit
) {
    doBeforeTextChangeFlow()
        .onEach {
            onChange(it.toString())
        }.launchIn(lifecycle)
}

@ExperimentalCoroutinesApi
@FlowPreview
inline fun EditText.textChangeBefore(
    lifecycle: LifecycleCoroutineScope,
    timeoutMillis: Long = 500,
    crossinline onChange: (s: String) -> Unit
) {
    doBeforeTextChangeFlow()
        .debounce(timeoutMillis)
        .onEach {
            onChange(it.toString())
        }.launchIn(lifecycle)
}

@ExperimentalCoroutinesApi
inline fun EditText.textChangeAfter(
    lifecycle: LifecycleCoroutineScope,
    crossinline onChange: (s: String) -> Unit
) {
    doAfterTextChangeFlow()
        .onEach {
            onChange(it.toString())
        }.launchIn(lifecycle)
}

@ExperimentalCoroutinesApi
@FlowPreview
inline fun EditText.textChangeAfter(
    lifecycle: LifecycleCoroutineScope,
    timeoutMillis: Long = 500,
    crossinline onChange: (s: String) -> Unit
) {
    doAfterTextChangeFlow()
        .debounce(timeoutMillis)
        .onEach {
            onChange(it.toString())
        }.launchIn(lifecycle)
}
