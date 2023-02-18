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

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.mcgrady.xarch.R

fun Lifecycle.observerWhenCreated(create: () -> Unit) {
    addObserver(LifecycleObserver(lifecycle = this, create = create))
}

fun Lifecycle.observerWhenDestroyed(destroyed: () -> Unit) {
    addObserver(LifecycleObserver(lifecycle = this, destroyed = destroyed))
}

@RequiresApi(api = Build.VERSION_CODES.Q)
fun Activity.observerWhenDestroyed(destroyed: () -> Unit) {
    registerActivityLifecycleCallbacks(LifecycleCallbacks(destroyed))
}

class LifecycleObserver(
    var lifecycle: Lifecycle?,
    var destroyed: (() -> Unit)? = null,
    var create: (() -> Unit)? = null
) : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        create?.invoke()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        destroyed?.invoke()
        lifecycle?.apply {
            removeObserver(this@LifecycleObserver)
            lifecycle = null
        }
        create = null
        destroyed = null
    }
}

class LifecycleCallbacks(var destroyed: (() -> Unit)? = null) : ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        destroyed?.invoke()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activity.unregisterActivityLifecycleCallbacks(this)
        }
        destroyed = null
    }
}

@Suppress("DEPRECATION")
@SuppressLint("ValidFragment")
class LifecycleFragment constructor(private var destroyed: (() -> Unit)? = null) : android.app.Fragment() {

    @Deprecated("Deprecated in Java")
    override fun onDestroy() {
        super.onDestroy()
        destroyed?.invoke()
        destroyed = null
    }
}

/**
 * 对LiveData订阅的简化封装
 *
 * @receiver LifecycleOwner
 * @param liveData LiveData<T> 需要进行订阅的LiveData
 * @param action action: (t: T) -> Unit 处理订阅内容的方法
 * @return Unit
 */
inline fun <T> LifecycleOwner.observeLiveData(
    liveData: LiveData<T>,
    crossinline action: (t: T) -> Unit
) {
    liveData.observe(this) { it?.let { t -> action(t) } }
}

/**
 * 当继承 Activity 且 Build.VERSION.SDK_INT < Build.VERSION_CODES.Q 以下的时候，
 * 会添加一个 空白的 Fragment, 当生命周期处于 onDestroy 时销毁数据
 */
private const val LIFECYCLE_FRAGMENT_TAG = "com.mcgrady.xarch.binding.binding_lifecycle_fragment"
internal inline fun Activity.registerLifecycleBelowQ(crossinline destroyed: () -> Unit) {
    val activity = this
    if (activity is FragmentActivity || activity is AppCompatActivity) return

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) return

    val fragmentManager = activity.fragmentManager
    if (fragmentManager.findFragmentByTag(LIFECYCLE_FRAGMENT_TAG) == null) {
        val transaction = fragmentManager.beginTransaction()
        transaction.add(LifecycleFragment { destroyed() }, LIFECYCLE_FRAGMENT_TAG).commit()
        fragmentManager.executePendingTransactions()
    }
}