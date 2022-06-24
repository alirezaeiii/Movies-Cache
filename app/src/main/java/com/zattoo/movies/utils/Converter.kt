@file:JvmName("Converter")

package com.zattoo.movies.utils

import android.content.Context
import com.zattoo.movies.R

fun priceAndAvailabilityToString(price: String, isAvailable: Boolean, context: Context): String {
    return when (isAvailable) {
        true -> String.format("%s %s", price, context.getString(R.string.available))
        false -> String.format("%s %s", price, context.getString(R.string.not_available))
    }
}