package io.bidmachine.applovinmaxdemo.adwrapper

import android.app.Activity
import io.bidmachine.applovinmaxdemo.ad.ViewAdObject
import io.bidmachine.applovinmaxdemo.ad.appnexus.AppNexusBannerAdObject
import io.bidmachine.applovinmaxdemo.ad.bidmachine.BidMachineBannerAdObject
import io.bidmachine.applovinmaxdemo.ad.inmobi.InMobiBannerAdObject
import io.bidmachine.applovinmaxdemo.ad.max.MaxBannerAdObject

class BannerAdWrapper(adUnitId: String) : ViewAdWrapper(adUnitId) {

    private var maxBannerAdObject: MaxBannerAdObject? = null

    /**
     * At first load the Applovin MAX ad object,
     * and then based on this result load other PostBid ad objects.
     *
     * Please note that MAX banners has auto-refresh enabled by default.
     * You must turn it off after ad was loaded. [com.applovin.mediation.ads.MaxAdView.stopAutoRefresh]
     */
    override fun loadAd(activity: Activity, adWrapperLoadListener: AdWrapperLoadListener) {
        super.loadAd(activity, adWrapperLoadListener)

        maxBannerAdObject = MaxBannerAdObject(adUnitId).apply {
            load(activity, null, LoadListener())
        }
    }

    /**
     * Creates list of banner PostBid ad objects.
     */
    override fun createPostBidAdObjectList(): List<ViewAdObject> = listOf(BidMachineBannerAdObject(),
                                                                          InMobiBannerAdObject(),
                                                                          AppNexusBannerAdObject())

    override fun destroy() {
        super.destroy()

        maxBannerAdObject?.destroy()
        maxBannerAdObject = null
    }

}