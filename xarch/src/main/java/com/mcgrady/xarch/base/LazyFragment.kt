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
package com.mcgrady.xarch.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * Created by mcgrady on 2021/5/13.
 */
abstract class LazyFragment(@LayoutRes protected open val contentLayoutId: Int = -1) : Fragment(contentLayoutId) {

    private var isLoaded = false

    override fun onResume() {
        super.onResume()
        // 增加Fragment可见判断，解决Fragment嵌套Fragment下，不可见Fragment执行onResume问题
        if (!isLoaded && !isHidden) {
            lazyInit()
            isLoaded = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }

    protected abstract fun lazyInit()
}
