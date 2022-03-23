package io.bidmachine.applovinmaxdemo.ad.bidmachine

import android.app.Activity
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.AdObject
import io.bidmachine.applovinmaxdemo.ad.AdObjectLoadListener
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObject
import io.bidmachine.applovinmaxdemo.ad.bidmachine.BidMachineAdObjectUtils.toPriceFloorParams
import io.bidmachine.interstitial.InterstitialAd
import io.bidmachine.interstitial.InterstitialListener
import io.bidmachine.interstitial.InterstitialRequest
import io.bidmachine.utils.BMError

class BidMachineInterstitialAdObject : FullscreenAdObject() {

    private var interstitialAd: InterstitialAd? = null

    override fun load(activity: Activity, priceFloor: Double?, loadListener: AdObjectLoadListener<AdObject>) {
        val request = InterstitialRequest.Builder()
                .setPriceFloorParams(priceFloor?.toPriceFloorParams())
                .build()
        interstitialAd = InterstitialAd(activity).apply {
            setListener(Listener(loadListener))
            load(request)
        }
    }

    override fun getPrice(): Double? = interstitialAd?.auctionResult?.price

    override fun canShow(): Boolean = interstitialAd?.canShow() == true

    override fun show() {
        interstitialAd?.show()
    }

    override fun destroy() {
        interstitialAd?.also {
            it.setListener(null)
            it.destroy()
        }
        interstitialAd = null
    }


    private inner class Listener(private val loadListener: AdObjectLoadListener<AdObject>) : InterstitialListener {

        override fun onAdLoaded(interstitialAd: InterstitialAd) {
            loadListener.onLoaded(this@BidMachineInterstitialAdObject)
        }

        override fun onAdLoadFailed(interstitialAd: InterstitialAd, bmError: BMError) {
            loadListener.onFailToLoad(this@BidMachineInterstitialAdObject, bmError.message)
        }

        override fun onAdShown(interstitialAd: InterstitialAd) {
            Utils.log(this@BidMachineInterstitialAdObject, "onAdShown")
        }

        override fun onAdShowFailed(interstitialAd: InterstitialAd, bmError: BMError) {
            Utils.log(this@BidMachineInterstitialAdObject, "onAdShowFailed, error - ${bmError.message}")
        }

        override fun onAdImpression(interstitialAd: InterstitialAd) {
            Utils.log(this@BidMachineInterstitialAdObject, "onAdImpression")
        }

        override fun onAdClicked(interstitialAd: InterstitialAd) {
            Utils.log(this@BidMachineInterstitialAdObject, "onAdClicked")
        }

        override fun onAdClosed(interstitialAd: InterstitialAd, finished: Boolean) {
            Utils.log(this@BidMachineInterstitialAdObject, "onAdClosed")
        }

        override fun onAdExpired(interstitialAd: InterstitialAd) {
            Utils.log(this@BidMachineInterstitialAdObject, "onAdExpired")
        }

    }

}