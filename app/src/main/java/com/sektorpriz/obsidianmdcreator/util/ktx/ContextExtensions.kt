package com.sektorpriz.obsidianmdcreator.util.ktx

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

fun Context.getActivity(): Activity =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.getActivity()
        else -> error("Here is no activity wtf")
    }