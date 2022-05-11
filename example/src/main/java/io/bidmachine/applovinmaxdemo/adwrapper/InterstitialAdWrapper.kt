package io.bidmachine.applovinmaxdemo.adwrapper

import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObject
import io.bidmachine.applovinmaxdemo.ad.admob.AdMobInterstitialAdObject
import io.bidmachine.applovinmaxdemo.ad.appnexus.AppNexusInterstitialAdObject
import io.bidmachine.applovinmaxdemo.ad.bidmachine.BidMachineInterstitialAdObject
import io.bidmachine.applovinmaxdemo.ad.inmobi.InMobiInterstitialAdObject
import io.bidmachine.applovinmaxdemo.ad.max.MaxInterstitialAdObject

class InterstitialAdWrapper(adUnitId: String) : FullscreenAdWrapper(adUnitId) {

    /**
     * Creates MAX interstitial ad object.
     */
    override fun createMaxAdObject(): FullscreenAdObject = MaxInterstitialAdObject(adUnitId)

    /**
     * Creates list of interstitial PostBid ad objects.
     */
    override fun createPostBidAdObjectList(): List<FullscreenAdObject> = listOf(BidMachineInterstitialAdObject(),
                                                                                InMobiInterstitialAdObject(),
                                                                                AppNexusInterstitialAdObject(),
                                                                                AdMobInterstitialAdObject())

}