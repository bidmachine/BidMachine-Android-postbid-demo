package io.bidmachine.applovinmaxdemo.adwrapper

import android.app.Activity
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.AdObject
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObject
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObjectListener
import java.lang.ref.WeakReference

abstract class FullscreenAdWrapper(adUnitId: String) :
        AdWrapper<FullscreenAdWrapperListener, FullscreenAdObject>(adUnitId) {

    private var weakActivity: WeakReference<Activity>? = null

    /**
     * At first load the Applovin MAX ad object,
     * and then based on this result load other PostBid ad objects.
     */
    fun loadAd(activity: Activity, adWrapperListener: FullscreenAdWrapperListener) {
        setupMaxAdObject(adWrapperListener).apply {
            weakActivity = WeakReference(activity)

            Utils.log(this, "load ad")

            load(activity, null, FullscreenListener())
        }
    }

    override fun loadPostBidAd(adObject: FullscreenAdObject, priceFloor: Double?) {
        val activity = weakActivity?.get()
        if (activity == null) {
            onPostBidAdLoadFail(adObject, "Activity is null")
            return
        }
        adObject.load(activity, priceFloor, FullscreenPostBidListener())
    }

    /**
     * Shows ad object with highest price.
     */
    fun showAd() {
        getAdWithHighestPrice()?.also {
            Utils.log(this, "showAd, ${it.javaClass.simpleName} (price: ${it.getPrice()})")

            it.show()
        } ?: Utils.log(this, "Nothing to show", true)
    }

    override fun destroy() {
        super.destroy()

        weakActivity?.clear()
        weakActivity = null
    }

    private fun onAdClosed(adObject: AdObject) {
        Utils.log(adObject, "onClosed")

        listener?.onAdClosed()
    }


    private inner class FullscreenListener : Listener(), FullscreenAdObjectListener {

        override fun onClosed(adObject: FullscreenAdObject) {
            onAdClosed(adObject)
        }

    }

    private inner class FullscreenPostBidListener : PostBidListener(), FullscreenAdObjectListener {

        override fun onClosed(adObject: FullscreenAdObject) {
            onAdClosed(adObject)
        }

    }

}