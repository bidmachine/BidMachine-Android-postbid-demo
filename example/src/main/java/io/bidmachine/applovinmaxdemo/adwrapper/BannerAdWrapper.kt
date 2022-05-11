package io.bidmachine.applovinmaxdemo.adwrapper

import io.bidmachine.applovinmaxdemo.ad.ViewAdObject
import io.bidmachine.applovinmaxdemo.ad.admob.AdMobBannerAdObject
import io.bidmachine.applovinmaxdemo.ad.appnexus.AppNexusBannerAdObject
import io.bidmachine.applovinmaxdemo.ad.bidmachine.BidMachineBannerAdObject
import io.bidmachine.applovinmaxdemo.ad.inmobi.InMobiBannerAdObject
import io.bidmachine.applovinmaxdemo.ad.max.MaxBannerAdObject

class BannerAdWrapper(adUnitId: String) : ViewAdWrapper(adUnitId) {

    /**
     * Creates MAX banner ad object.
     */
    override fun createMaxAdObject(): ViewAdObject = MaxBannerAdObject(adUnitId)

    /**
     * Creates list of banner PostBid ad objects.
     */
    override fun createPostBidAdObjectList(): List<ViewAdObject> = listOf(BidMachineBannerAdObject(),
                                                                          InMobiBannerAdObject(),
                                                                          AppNexusBannerAdObject(),
                                                                          AdMobBannerAdObject())

}