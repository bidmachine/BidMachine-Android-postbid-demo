package io.bidmachine.applovinmaxdemo.ad.max

import android.app.Activity
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.AdObject
import io.bidmachine.applovinmaxdemo.ad.AdObjectLoadListener
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObject
import io.bidmachine.applovinmaxdemo.ad.max.MaxAdObjectUtils.getCPM

class MaxInterstitialAdObject(private val adUnitId: String) : FullscreenAdObject() {

    private var maxInterstitialAd: MaxInterstitialAd? = null
    private var price: Double? = null

    override fun load(activity: Activity, priceFloor: Double?, loadListener: AdObjectLoadListener<AdObject>) {
        maxInterstitialAd = MaxInterstitialAd(adUnitId, activity).apply {
            setListener(Listener(loadListener))
            loadAd()
        }
    }

    override fun getPrice(): Double? = price

    override fun canShow(): Boolean = maxInterstitialAd?.isReady == true

    override fun show() {
        maxInterstitialAd?.showAd()
    }

    override fun destroy() {
        price = null
        maxInterstitialAd?.destroy()
        maxInterstitialAd = null
    }


    private inner class Listener(private val loadListener: AdObjectLoadListener<AdObject>) : MaxAdListener {

        override fun onAdLoaded(maxAd: MaxAd) {
            // Extension function to obtain CPM
            price = maxAd.getCPM()

            loadListener.onLoaded(this@MaxInterstitialAdObject)
        }

        override fun onAdLoadFailed(adUnitId: String, maxError: MaxError) {
            loadListener.onFailToLoad(this@MaxInterstitialAdObject, maxError.message)
        }

        override fun onAdDisplayed(maxAd: MaxAd) {
            Utils.log(this@MaxInterstitialAdObject, "onAdDisplayed")
        }

        override fun onAdDisplayFailed(maxAd: MaxAd, maxError: MaxError) {
            Utils.log(this@MaxInterstitialAdObject, "onAdDisplayFailed, error - ${maxError.message}")
        }

        override fun onAdClicked(maxAd: MaxAd) {
            Utils.log(this@MaxInterstitialAdObject, "onAdClicked")
        }

        override fun onAdHidden(maxAd: MaxAd) {
            Utils.log(this@MaxInterstitialAdObject, "onAdHidden")
        }

    }

}