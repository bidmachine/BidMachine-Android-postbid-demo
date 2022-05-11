package io.bidmachine.applovinmaxdemo.ad.inmobi

import android.app.Activity
import com.inmobi.ads.AdMetaInfo
import com.inmobi.ads.InMobiAdRequestStatus
import com.inmobi.ads.InMobiInterstitial
import com.inmobi.ads.listeners.InterstitialAdEventListener
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObject
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObjectListener

abstract class InMobiFullscreenAdObject : FullscreenAdObject() {

    private var inMobiInterstitial: InMobiInterstitial? = null
    private var price: Double? = null

    abstract fun getPlacementId(): Long

    override fun load(activity: Activity, priceFloor: Double?, listener: FullscreenAdObjectListener) {
        inMobiInterstitial = InMobiInterstitial(activity, getPlacementId(), Listener(listener)).apply {
            load()
        }
    }

    override fun getPrice(): Double? = price

    override fun canShow(): Boolean = inMobiInterstitial?.isReady == true

    override fun show(activity: Activity) {
        inMobiInterstitial?.show()
    }

    override fun destroy() {
        price = null
        inMobiInterstitial = null
    }


    private inner class Listener(private val listener: FullscreenAdObjectListener) :
            InterstitialAdEventListener() {

        override fun onAdFetchFailed(inMobiInterstitial: InMobiInterstitial,
                                     inMobiAdRequestStatus: InMobiAdRequestStatus) {
            listener.onFailToLoad(this@InMobiFullscreenAdObject, inMobiAdRequestStatus.message)
        }

        override fun onAdLoadSucceeded(inMobiInterstitial: InMobiInterstitial, adMetaInfo: AdMetaInfo) {
            price = adMetaInfo.bid

            listener.onLoaded(this@InMobiFullscreenAdObject)
        }

        override fun onAdLoadFailed(inMobiInterstitial: InMobiInterstitial,
                                    inMobiAdRequestStatus: InMobiAdRequestStatus) {
            listener.onFailToLoad(this@InMobiFullscreenAdObject, inMobiAdRequestStatus.message)
        }

        override fun onAdDisplayed(inMobiInterstitial: InMobiInterstitial, adMetaInfo: AdMetaInfo) {
            listener.onShown(this@InMobiFullscreenAdObject)
        }

        override fun onAdDisplayFailed(inMobiInterstitial: InMobiInterstitial) {
            Utils.log(this@InMobiFullscreenAdObject, "onAdDisplayFailed")
        }

        override fun onAdClicked(inMobiInterstitial: InMobiInterstitial, map: MutableMap<Any, Any>?) {
            listener.onClicked(this@InMobiFullscreenAdObject)
        }

        override fun onAdDismissed(inMobiInterstitial: InMobiInterstitial) {
            listener.onClosed(this@InMobiFullscreenAdObject)
        }

    }

}