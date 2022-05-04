package io.bidmachine.applovinmaxdemo.ad.appnexus

import android.app.Activity
import com.appnexus.opensdk.*
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObject
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObjectListener

class AppNexusInterstitialAdObject : FullscreenAdObject() {

    companion object {
        private const val PLACEMENT_ID = "1326299"
    }

    private var interstitialAdView: InterstitialAdView? = null

    override fun load(activity: Activity, priceFloor: Double?, listener: FullscreenAdObjectListener) {
        interstitialAdView = InterstitialAdView(activity).apply {
            placementID = PLACEMENT_ID
            adListener = Listener(listener)
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


    private inner class Listener(private val listener: FullscreenAdObjectListener) : AdListener {

        override fun onAdLoaded(adView: AdView?) {
            listener.onLoaded(this@AppNexusInterstitialAdObject)
        }

        override fun onAdRequestFailed(adView: AdView?, resultCode: ResultCode?) {
            listener.onFailToLoad(this@AppNexusInterstitialAdObject, "${resultCode?.message}")
        }

        override fun onAdExpanded(adView: AdView?) {
            listener.onShown(this@AppNexusInterstitialAdObject)
        }

        override fun onAdCollapsed(adView: AdView?) {
            listener.onClosed(this@AppNexusInterstitialAdObject)
        }

        override fun onAdClicked(adView: AdView?) {
            listener.onClicked(this@AppNexusInterstitialAdObject)
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