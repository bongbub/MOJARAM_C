package com.example.mojaram.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AutoClearedValue<T: Any>: ReadWriteProperty<Fragment, T>, DefaultLifecycleObserver {
    private var _value:T? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
        _value ?: throw  IllegalStateException("AutoClearedValue is not available")

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        thisRef.viewLifecycleOwner.lifecycle.removeObserver(this)
        _value = value
        thisRef.viewLifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        _value = null
    }
}