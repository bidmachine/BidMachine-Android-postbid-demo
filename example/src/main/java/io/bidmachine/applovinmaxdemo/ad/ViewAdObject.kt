package io.bidmachine.applovinmaxdemo.ad

import android.view.View

abstract class ViewAdObject : AdObject() {

    /**
     * Gets loaded ad view.
     */
    abstract fun getView(): View?

}