package com.uucyan.memopush

import android.support.annotation.IdRes
import android.view.View
import android.widget.TextView

/**
 * Created by Uucyan on 2017/08/15.
 */
fun <T : View> View.bindView(@IdRes id: Int): Lazy<T> = lazy {
    findViewById<TextView>(id) as T
}