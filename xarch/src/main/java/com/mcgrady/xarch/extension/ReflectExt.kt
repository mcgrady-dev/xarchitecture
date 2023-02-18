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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.reflect.Method

const val INFLATE_NAME = "inflate"
const val BIND_NAME = "bind"

fun <T> Class<T>.inflateMethod(): Method = getMethod(INFLATE_NAME, LayoutInflater::class.java)

fun <T> Class<T>.bindMethod(): Method = getMethod(BIND_NAME, View::class.java)

fun <T> Class<T>.inflateMethodWithViewGroup(): Method = getMethod(INFLATE_NAME, LayoutInflater::class.java, ViewGroup::class.java)
