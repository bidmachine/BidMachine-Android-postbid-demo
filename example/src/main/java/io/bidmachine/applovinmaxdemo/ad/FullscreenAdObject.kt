package io.bidmachine.applovinmaxdemo.ad

import android.app.Activity

abstract class FullscreenAdObject : AdObject() {

    /**
     * Loads ad.
     */
    abstract fun load(activity: Activity,
                      priceFloor: Double?,
                      listener: FullscreenAdObjectListener)

    /**
     * Shows loaded ad.
     */
    abstract fun show(activity: Activity)

}