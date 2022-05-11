package io.bidmachine.applovinmaxdemo

import android.content.Context
import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.ViewGroup
import kotlin.math.roundToInt

object Utils {

    private const val TAG = "MAXPostBidDemo"

    private val uiHandler = Handler(Looper.getMainLooper())

    fun createLayoutParams(resources: Resources, height: Int): ViewGroup.LayoutParams {
        return ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                      dp2px(resources, height.toFloat()))
    }

    fun dp2px(context: Context, dp: Float): Int {
        return dp2px(context.resources, dp)
    }

    fun dp2px(resources: Resources, dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).roundToInt()
    }

    fun log(any: Any, message: String, isError: Boolean = false) {
        log(any.javaClass, message, isError)
    }

    fun <T> log(clazz: Class<T>, message: String, isError: Boolean = false) {
        if (isError) {
            Log.e(TAG, "[${clazz.simpleName}] $message")
        } else {
            Log.d(TAG, "[${clazz.simpleName}] $message")
        }
    }

    fun isUiThread(): Boolean {
        return Looper.myLooper() == Looper.getMainLooper()
    }

    fun onUiThread(runnable: Runnable) {
        if (isUiThread()) {
            runnable.run()
        } else {
            uiHandler.post(runnable)
        }
    }

}