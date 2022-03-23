package io.bidmachine.applovinmaxdemo.ad.appnexus

import android.app.Activity
import com.appnexus.opensdk.*
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.AdObject
import io.bidmachine.applovinmaxdemo.ad.AdObjectLoadListener
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObject

class AppNexusInterstitialAdObject : FullscreenAdObject() {

    companion object {
        private const val PLACEMENT_ID = "1326299"
    }

    private var interstitialAdView: InterstitialAdView? = null

    override fun load(activity: Activity, priceFloor: Double?, loadListener: AdObjectLoadListener<AdObject>) {
        interstitialAdView = InterstitialAdView(activity).apply {
            placementID = PLACEMENT_ID
            adListener = Listener(loadListener)
            loadAd()
        }
    }

    override fun getPrice(): Double? = interstitialAdView?.adResponseInfo?.cpm

    override fun canShow(): Boolean = interstitialAdView?.isReady == true

    override fun show() {
        interstitialAdView?.show()
    }

    override fun destroy() {
        interstitialAdView?.destroy()
        interstitialAdView = null
    }


    private inner class Listener(private val loadListener: AdObjectLoadListener<AdObject>) : AdListener {

        override fun onAdLoaded(adView: AdView?) {
            loadListener.onLoaded(this@AppNexusInterstitialAdObject)
        }

        override fun onAdRequestFailed(adView: AdView?, resultCode: ResultCode?) {
            loadListener.onFailToLoad(this@AppNexusInterstitialAdObject, "${resultCode?.message}")
        }

        override fun onAdExpanded(adView: AdView?) {
            Utils.log(this@AppNexusInterstitialAdObject, "onAdExpanded")
        }

        override fun onAdCollapsed(adView: AdView?) {
            Utils.log(this@AppNexusInterstitialAdObject, "onAdCollapsed")
        }

        override fun onAdClicked(adView: AdView?) {
            Utils.log(this@AppNexusInterstitialAdObject, "onAdClicked")
        }

        override fun onAdClicked(adView: AdView?, clickUrl: String?) {
            Utils.log(this@AppNexusInterstitialAdObject, "onAdClicked")
        }

        override fun onAdLoaded(nativeAdResponse: NativeAdResponse?) {

        }

        override fun onLazyAdLoaded(adView: AdView?) {

        }

    }

}