package com.mindlab.mapboxtest.utils

import com.mindlab.mapboxtest.utils.Extensions.toPersianNumber
import java.text.NumberFormat
import java.util.*

/**
 * Created by Alireza Nezami on 1/14/2022.
 */
object Extensions {

    fun Long?.toPersianNumber(): String {
        val nf: NumberFormat = NumberFormat.getInstance(Locale("ar", "EG"))
        return nf.format(this)
    }
}