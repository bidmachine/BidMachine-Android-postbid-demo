package io.bidmachine.applovinmaxdemo

import android.content.res.Resources
import android.util.TypedValue
import android.view.ViewGroup

interface ViewAdWrapper : AdWrapper {

    fun showAd(adContainer: ViewGroup)

    fun createLayoutParams(resources: Resources, height: Int): ViewGroup.LayoutParams {
        return ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                      TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                                                height.toFloat(),
                                                                resources.displayMetrics).toInt())
    }

}