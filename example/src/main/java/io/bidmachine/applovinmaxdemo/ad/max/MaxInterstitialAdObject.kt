package io.bidmachine.applovinmaxdemo.ad.max

import android.app.Activity
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObject
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObjectListener
import io.bidmachine.applovinmaxdemo.ad.max.MaxAdObjectUtils.getCPM

class MaxInterstitialAdObject(private val adUnitId: String) : FullscreenAdObject() {

    private var maxInterstitialAd: MaxInterstitialAd? = null
    private var price: Double? = null

    override fun load(activity: Activity, priceFloor: Double?, listener: FullscreenAdObjectListener) {
        maxInterstitialAd = MaxInterstitialAd(adUnitId, activity).apply {
            setListener(Listener(listener))
            loadAd()
        }
    }

    override fun getPrice(): Double? = price

    override fun canShow(): Boolean = maxInterstitialAd?.isReady == true

    override fun show(activity: Activity) {
        maxInterstitialAd?.showAd()
    }

    override fun destroy() {
        price = null
        maxInterstitialAd?.destroy()
        maxInterstitialAd = null
    }


    private inner class Listener(private val listener: FullscreenAdObjectListener) : MaxAdListener {

        override fun onAdLoaded(maxAd: MaxAd) {
            // Extension function to obtain CPM
            price = maxAd.getCPM()

            listener.onLoaded(this@MaxInterstitialAdObject)
        }

        override fun onAdLoadFailed(adUnitId: String, maxError: MaxError) {
            listener.onFailToLoad(this@MaxInterstitialAdObject, maxError.message)
        }

        override fun onAdDisplayed(maxAd: MaxAd) {
            listener.onShown(this@MaxInterstitialAdObject)
        }

        override fun onAdDisplayFailed(maxAd: MaxAd, maxError: MaxError) {
            Utils.log(this@MaxInterstitialAdObject, "onAdDisplayFailed, error - ${maxError.message}")
        }

        override fun onAdClicked(maxAd: MaxAd) {
            listener.onClicked(this@MaxInterstitialAdObject)
        }

        override fun onAdHidden(maxAd: MaxAd) {
            listener.onClosed(this@MaxInterstitialAdObject)
        }

    }

}