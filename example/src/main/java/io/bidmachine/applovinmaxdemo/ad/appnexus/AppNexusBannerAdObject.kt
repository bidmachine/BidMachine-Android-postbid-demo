package io.bidmachine.applovinmaxdemo.ad.appnexus

import android.app.Activity
import android.view.View
import com.appnexus.opensdk.*
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.AdObject
import io.bidmachine.applovinmaxdemo.ad.AdObjectLoadListener
import io.bidmachine.applovinmaxdemo.ad.ViewAdObject

class AppNexusBannerAdObject : ViewAdObject() {

    companion object {
        private const val PLACEMENT_ID = "1326299"
    }

    private var bannerAdView: BannerAdView? = null
    private var isLoaded = false

    override fun load(activity: Activity, priceFloor: Double?, loadListener: AdObjectLoadListener<AdObject>) {
        bannerAdView = BannerAdView(activity).apply {
            placementID = PLACEMENT_ID
            autoRefreshInterval = 0
            setAdSize(320, 50)
            adListener = Listener(loadListener)
            loadAd()
        }
    }

    override fun getPrice(): Double? = bannerAdView?.adResponseInfo?.cpm

    override fun canShow(): Boolean = isLoaded && bannerAdView != null

    override fun getView(): View? = bannerAdView

    override fun destroy() {
        isLoaded = false
        bannerAdView?.destroy()
        bannerAdView = null
    }


    private inner class Listener(private val loadListener: AdObjectLoadListener<AdObject>) : AdListener {

        override fun onAdLoaded(adView: AdView?) {
            isLoaded = true

            loadListener.onLoaded(this@AppNexusBannerAdObject)
        }

        override fun onAdRequestFailed(adView: AdView?, resultCode: ResultCode?) {
            loadListener.onFailToLoad(this@AppNexusBannerAdObject, "${resultCode?.message}")
        }

        override fun onAdExpanded(adView: AdView?) {
            Utils.log(this@AppNexusBannerAdObject, "onAdExpanded")
        }

        override fun onAdCollapsed(adView: AdView?) {
            Utils.log(this@AppNexusBannerAdObject, "onAdCollapsed")
        }

        override fun onAdClicked(adView: AdView?) {
            Utils.log(this@AppNexusBannerAdObject, "onAdClicked")
        }

        override fun onAdClicked(adView: AdView?, clickUrl: String?) {
            Utils.log(this@AppNexusBannerAdObject, "onAdClicked")
        }

        override fun onAdLoaded(nativeAdResponse: NativeAdResponse?) {

        }

        override fun onLazyAdLoaded(adView: AdView?) {

        }

    }

}