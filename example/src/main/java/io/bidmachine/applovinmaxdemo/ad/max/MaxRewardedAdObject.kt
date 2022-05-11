package io.bidmachine.applovinmaxdemo.ad.max

import android.app.Activity
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.MaxReward
import com.applovin.mediation.MaxRewardedAdListener
import com.applovin.mediation.ads.MaxRewardedAd
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObject
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObjectListener
import io.bidmachine.applovinmaxdemo.ad.max.MaxAdObjectUtils.getCPM

class MaxRewardedAdObject(private val adUnitId: String) : FullscreenAdObject() {

    private var maxRewardedAd: MaxRewardedAd? = null
    private var price: Double? = null

    override fun load(activity: Activity, priceFloor: Double?, listener: FullscreenAdObjectListener) {
        maxRewardedAd = MaxRewardedAd.getInstance(adUnitId, activity).apply {
            setListener(Listener(listener))
            loadAd()
        }
    }

    override fun getPrice(): Double? = price

    override fun canShow(): Boolean = maxRewardedAd?.isReady == true

    override fun show(activity: Activity) {
        maxRewardedAd?.showAd()
    }

    override fun destroy() {
        price = null
        maxRewardedAd?.destroy()
        maxRewardedAd = null
    }


    private inner class Listener(private val listener: FullscreenAdObjectListener) : MaxRewardedAdListener {

        override fun onAdLoaded(maxAd: MaxAd) {
            // Extension function to obtain CPM
            price = maxAd.getCPM()

            listener.onLoaded(this@MaxRewardedAdObject)
        }

        override fun onAdLoadFailed(adUnitId: String, maxError: MaxError) {
            listener.onFailToLoad(this@MaxRewardedAdObject, maxError.message)
        }

        override fun onAdDisplayed(maxAd: MaxAd) {
            listener.onShown(this@MaxRewardedAdObject)
        }

        override fun onAdDisplayFailed(maxAd: MaxAd, maxError: MaxError) {
            Utils.log(this@MaxRewardedAdObject, "onAdDisplayFailed, error - ${maxError.message}")
        }

        override fun onAdClicked(maxAd: MaxAd) {
            listener.onClicked(this@MaxRewardedAdObject)
        }

        override fun onAdHidden(maxAd: MaxAd) {
            listener.onClosed(this@MaxRewardedAdObject)
        }

        override fun onRewardedVideoStarted(maxAd: MaxAd) {
            Utils.log(this@MaxRewardedAdObject, "onRewardedVideoStarted")
        }

        override fun onRewardedVideoCompleted(maxAd: MaxAd) {
            Utils.log(this@MaxRewardedAdObject, "onRewardedVideoCompleted")
        }

        override fun onUserRewarded(maxAd: MaxAd, maxReward: MaxReward) {
            Utils.log(this@MaxRewardedAdObject, "onUserRewarded")
        }

    }

}