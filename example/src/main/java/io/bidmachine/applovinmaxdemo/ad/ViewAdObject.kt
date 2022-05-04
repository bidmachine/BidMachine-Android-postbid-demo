package io.bidmachine.applovinmaxdemo.ad

import android.content.Context
import android.view.View

abstract class ViewAdObject : AdObject() {

    /**
     * Loads ad.
     */
    abstract fun load(context: Context,
                      priceFloor: Double?,
                      listener: AdObjectListener<AdObject>)

    /**
     * Gets loaded ad view.
     */
    abstract fun getView(): View?

}