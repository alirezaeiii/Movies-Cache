@file:JvmName("Converter")

package com.sample.movies.utils

import android.content.Context
import com.sample.movies.R

fun priceAndAvailabilityToString(price: String, isAvailable: Boolean, context: Context): String {
    return when (isAvailable) {
        true -> String.format("%s %s", price, context.getString(R.string.available))
        false -> String.format("%s %s", price, context.getString(R.string.not_available))
    }
}