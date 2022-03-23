package io.bidmachine.applovinmaxdemo

import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import android.view.ViewGroup
import kotlin.math.roundToInt

object Utils {

    private const val TAG = "MAXPostBidDemo"

    fun createLayoutParams(resources: Resources, height: Int): ViewGroup.LayoutParams {
        return ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                      TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                                                height.toFloat(),
                                                                resources.displayMetrics).roundToInt())
    }

    fun log(any: Any, message: String) {
        log(any.javaClass, message)
    }

    fun <T> log(clazz: Class<T>, message: String) {
        Log.d(TAG, "[${clazz.simpleName}] $message")
    }

}