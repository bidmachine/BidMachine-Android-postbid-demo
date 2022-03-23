package io.bidmachine.applovinmaxdemo.adwrapper

import android.view.ViewGroup
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.ViewAdObject

abstract class ViewAdWrapper(adUnitId: String) : AdWrapper<ViewAdObject>(adUnitId) {

    /**
     * Shows ad object with highest price.
     */
    fun showAd(adContainer: ViewGroup) {
        adContainer.removeAllViews()
        getAdWithHighestPrice()?.also {
            Utils.log(this, "showAd, ${it.javaClass.simpleName} (price: ${it.getPrice()})")

            adContainer.addView(it.getView())
        } ?: Utils.log(this, "Nothing to show")
    }

}