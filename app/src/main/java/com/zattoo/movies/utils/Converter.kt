@file:JvmName("Converter")

package com.zattoo.movies.utils

fun priceAndAvailabilityToString(price: String, isAvailable: Boolean): String {
    return when (isAvailable) {
        true -> String.format("%s %s", price, "Available")
        false -> String.format("%s %s", price, "Sold out")
    }
}