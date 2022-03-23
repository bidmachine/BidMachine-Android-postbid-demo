package io.bidmachine.applovinmaxdemo.adwrapper

import android.app.Activity
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObject
import io.bidmachine.applovinmaxdemo.ad.appnexus.AppNexusInterstitialAdObject
import io.bidmachine.applovinmaxdemo.ad.bidmachine.BidMachineInterstitialAdObject
import io.bidmachine.applovinmaxdemo.ad.inmobi.InMobiInterstitialAdObject
import io.bidmachine.applovinmaxdemo.ad.max.MaxInterstitialAdObject

class InterstitialAdWrapper(adUnitId: String) : FullscreenAdWrapper(adUnitId) {

    private var maxInterstitialAdObject: MaxInterstitialAdObject? = null

    /**
     * At first load the Applovin MAX ad object,
     * and then based on this result load other PostBid ad objects.
     */
    override fun loadAd(activity: Activity, adWrapperLoadListener: AdWrapperLoadListener) {
        super.loadAd(activity, adWrapperLoadListener)

        maxInterstitialAdObject = MaxInterstitialAdObject(adUnitId).apply {
            load(activity, null, LoadListener())
        }
    }

    /**
     * Creates list of interstitial PostBid ad objects.
     */
    override fun createPostBidAdObjectList(): List<FullscreenAdObject> = listOf(BidMachineInterstitialAdObject(),
                                                                                InMobiInterstitialAdObject(),
                                                                                AppNexusInterstitialAdObject())

    override fun destroy() {
        super.destroy()

        maxInterstitialAdObject?.destroy()
        maxInterstitialAdObject = null
    }

}