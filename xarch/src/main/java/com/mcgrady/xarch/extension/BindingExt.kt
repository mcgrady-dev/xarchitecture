@file:Suppress("unused")

package com.mcgrady.xarch.extension

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.activity.ComponentActivity
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.mcgrady.xarch.R
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <VB : ViewBinding> ComponentActivity.viewBinding(inflate: (LayoutInflater) -> VB, setContentView: Boolean = true) = lazy(
    LazyThreadSafetyMode.NONE
) {
    inflate(layoutInflater).also { binding ->
        if (setContentView) setContentView(binding.root)
        if (binding is ViewDataBinding) binding.lifecycleOwner = this
    }
}

fun <VB : ViewBinding> Fragment.viewBinding(bind: (View) -> VB) = FragmentBindingDelegate(bind)

fun <VB : ViewBinding> Fragment.viewBinding(inflate: (LayoutInflater) -> VB) = FragmentInflateBindingDelegate(inflate)

class FragmentBindingDelegate<VB : ViewBinding>(private val bind: (View) -> VB) :
    ReadOnlyProperty<Fragment, VB> {
    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB =
        requireNotNull(thisRef.view) { "The constructor missing layout id or the property of ${property.name} has been destroyed." }
            .getBinding(bind).also { binding ->
                if (binding is ViewDataBinding) binding.lifecycleOwner = thisRef.viewLifecycleOwner
            }
}

class FragmentInflateBindingDelegate<VB : ViewBinding>(private val inflate: (LayoutInflater) -> VB) :
    ReadOnlyProperty<Fragment, VB> {
    private var binding: VB? = null
    private val handler by lazy { Handler(Looper.getMainLooper()) }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
        if (binding == null) {
            binding = try {
                inflate(thisRef.layoutInflater).also { binding ->
                    if (binding is ViewDataBinding) binding.lifecycleOwner = thisRef.viewLifecycleOwner
                }
            } catch (e: IllegalStateException) {
                throw IllegalStateException("The property of ${property.name} has been destroyed.")
            }
            thisRef.viewLifecycleOwner.lifecycle.observerWhenDestroyed {
                handler.post { binding = null }
            }
        }
        return binding!!
    }
}

@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> View.getBinding(bind: (View) -> VB): VB =
    getTag(R.id.tag_view_binding) as? VB ?: bind(this).also { setTag(R.id.tag_view_binding, it) }

fun <VB : ViewBinding> ViewGroup.inflate(inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB) =
    inflate(LayoutInflater.from(context), this, true)

fun <VB : ViewBinding> ViewGroup.viewBinding(
    inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    attachToParent: Boolean = false
) = lazy(LazyThreadSafetyMode.NONE) {
    inflate(LayoutInflater.from(context), if (attachToParent) this else null, attachToParent)
}

fun <T : ViewDataBinding> ViewGroup.dataBinding(
    @LayoutRes layoutRes: Int,
    attachToParent: Boolean = false
): T {
    return DataBindingUtil.inflate(
        LayoutInflater.from(context),
        layoutRes,
        this,
        attachToParent
    )
}

fun <VB : ViewBinding> RecyclerView.ViewHolder.withBinding(bind: (View) -> VB, block: VB.(RecyclerView.ViewHolder) -> Unit) = apply {
    block(getBinding(bind), this@withBinding)
}

fun <VB : ViewBinding> BindingViewHolder<VB>.withBinding(block: VB.(BindingViewHolder<VB>) -> Unit) = apply {
    block(binding, this@withBinding)
}

fun <VB : ViewBinding> RecyclerView.ViewHolder.getBinding(bind: (View) -> VB) = itemView.getBinding(bind)

class BindingViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root) {
    constructor(parent: ViewGroup, inflate: (LayoutInflater, ViewGroup, Boolean) -> VB) :
            this(inflate(LayoutInflater.from(parent.context), parent, false))
}

fun <T> OnItemClickListener<T>.onItemClick(holder: RecyclerView.ViewHolder, block: (Int) -> T) =
    onItemClick(block(holder.bindingAdapterPosition), holder.bindingAdapterPosition)

fun interface OnItemClickListener<T> {
    fun onItemClick(item: T, position: Int)
}

fun <VB : ViewBinding> Activity.popupWindow(
    inflate: (LayoutInflater) -> VB,
    width: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    height: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    focusable: Boolean = false,
    block: VB.() -> Unit
) =
    lazy(LazyThreadSafetyMode.NONE) {
        PopupWindow(inflate(layoutInflater).apply(block).root, width, height, focusable)
    }

fun <VB : ViewBinding> Fragment.popupWindow(
    inflate: (LayoutInflater) -> VB,
    width: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    height: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    focusable: Boolean = false,
    block: VB.() -> Unit
) =
    lazy(LazyThreadSafetyMode.NONE) {
        PopupWindow(inflate(layoutInflater).apply(block).root, width, height, focusable)
    }