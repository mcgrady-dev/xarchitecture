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
package com.mcgrady.xarch.extension

/**
 * 数据
 */
sealed class BooleanExt<out T>

object Otherwise : BooleanExt<Nothing>()
class WithData<T>(val data: T) : BooleanExt<T>()

/**
 * 判断条件为true 时执行block
 */
inline fun <T : Any> Boolean.yes(block: () -> T) =
    when {
        this -> {
            WithData(block())
        }
        else -> {
            Otherwise
        }
    }

/**
 * 判断条件为false 时执行block
 *
 */
inline fun <T> Boolean.no(block: () -> T) = when {
    this -> Otherwise
    else -> {
        WithData(block())
    }
}

/**
 * 与判断条件互斥时执行block
 */
inline fun <T> BooleanExt<T>.ifNot(block: () -> T): T =
    when (this) {
        is Otherwise -> block()
        is WithData -> this.data
    }
