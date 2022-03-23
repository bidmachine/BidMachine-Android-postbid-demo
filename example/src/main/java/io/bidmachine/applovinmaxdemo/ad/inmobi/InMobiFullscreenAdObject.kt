package io.bidmachine.applovinmaxdemo.ad.inmobi

import android.app.Activity
import com.inmobi.ads.AdMetaInfo
import com.inmobi.ads.InMobiAdRequestStatus
import com.inmobi.ads.InMobiInterstitial
import com.inmobi.ads.listeners.InterstitialAdEventListener
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.AdObject
import io.bidmachine.applovinmaxdemo.ad.AdObjectLoadListener
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObject

abstract class InMobiFullscreenAdObject : FullscreenAdObject() {

    private var inMobiInterstitial: InMobiInterstitial? = null
    private var price: Double? = null

    abstract fun getPlacementId(): Long

    override fun load(activity: Activity, priceFloor: Double?, loadListener: AdObjectLoadListener<AdObject>) {
        inMobiInterstitial = InMobiInterstitial(activity, getPlacementId(), Listener(loadListener)).apply {
            load()
        }
    }

    override fun getPrice(): Double? = price

    override fun canShow(): Boolean = inMobiInterstitial?.isReady == true

    override fun show() {
        inMobiInterstitial?.show()
    }

    override fun destroy() {
        price = null
        inMobiInterstitial = null
    }


    private inner class Listener(private val loadListener: AdObjectLoadListener<AdObject>) :
            InterstitialAdEventListener() {

        override fun onAdFetchFailed(inMobiInterstitial: InMobiInterstitial,
                                     inMobiAdRequestStatus: InMobiAdRequestStatus) {
            loadListener.onFailToLoad(this@InMobiFullscreenAdObject, inMobiAdRequestStatus.message)
        }

        override fun onAdLoadSucceeded(inMobiInterstitial: InMobiInterstitial, adMetaInfo: AdMetaInfo) {
            price = adMetaInfo.bid

            loadListener.onLoaded(this@InMobiFullscreenAdObject)
        }

        override fun onAdLoadFailed(inMobiInterstitial: InMobiInterstitial,
                                    inMobiAdRequestStatus: InMobiAdRequestStatus) {
            loadListener.onFailToLoad(this@InMobiFullscreenAdObject, inMobiAdRequestStatus.message)
        }

        override fun onAdDisplayed(inMobiInterstitial: InMobiInterstitial, adMetaInfo: AdMetaInfo) {
            Utils.log(this@InMobiFullscreenAdObject, "onAdDisplayed")
        }

        override fun onAdDisplayFailed(inMobiInterstitial: InMobiInterstitial) {
            Utils.log(this@InMobiFullscreenAdObject, "onAdDisplayFailed")
        }

        override fun onAdClicked(inMobiInterstitial: InMobiInterstitial, map: MutableMap<Any, Any>?) {
            Utils.log(this@InMobiFullscreenAdObject, "onAdClicked")
        }

        override fun onAdDismissed(inMobiInterstitial: InMobiInterstitial) {
            Utils.log(this@InMobiFullscreenAdObject, "onAdDismissed")
        }

    }

}