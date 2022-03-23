package io.bidmachine.applovinmaxdemo.adwrapper

import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObject

abstract class FullscreenAdWrapper(adUnitId: String) : AdWrapper<FullscreenAdObject>(adUnitId) {

    /**
     * Shows ad object with highest price.
     */
    fun showAd() {
        getAdWithHighestPrice()?.also {
            Utils.log(this, "showAd, ${it.javaClass.simpleName} (price: ${it.getPrice()})")

            it.show()
        } ?: Utils.log(this, "Nothing to show")
    }

}