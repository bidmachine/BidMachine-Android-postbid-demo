package io.bidmachine.applovinmaxdemo

import android.app.Activity
import android.util.Log
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import io.bidmachine.applovinmaxdemo.AdWrapper.Companion.TAG
import io.bidmachine.interstitial.InterstitialAd
import io.bidmachine.interstitial.InterstitialListener
import io.bidmachine.interstitial.InterstitialRequest
import io.bidmachine.utils.BMError

class InterstitialAdWrapper : AdWrapper {

    companion object {
        private const val MAX_AD_UNIT = "YOUR_INTERSTITIAL_AD_UNIT_ID"
    }

    private var bidMachineInterstitialAd: InterstitialAd? = null
    private var maxInterstitialAd: MaxInterstitialAd? = null

    /**
     * At first load the Applovin MAX ad object,
     * and then based on this result, load the BidMachine ad.
     */
    override fun loadAd(activity: Activity, adWrapperLoadListener: AdWrapperLoadListener) {
        destroy()

        bidMachineInterstitialAd = InterstitialAd(activity).apply {
            setListener(BidMachineInterstitialListener(adWrapperLoadListener))
        }
        maxInterstitialAd = MaxInterstitialAd(MAX_AD_UNIT, activity).apply {
            setListener(MaxInterstitialListener())
            loadAd()
        }
    }

    /**
     * Load the BidMachine ad object with price floor if you want to get more expensive ads.
     */
    private fun loadBidMachine(maxRevenue: Double) {
        InterstitialRequest.Builder()
                .apply {
                    val priceFloorParams = definePriceFloorParams(maxRevenue)
                    if (priceFloorParams != null) {
                        setPriceFloorParams(priceFloorParams)
                    }
                }
                .build()
                .also {
                    bidMachineInterstitialAd?.load(it)
                }
    }

    /**
     * You should give preference to the BidMachine ad object, because it has a higher price.
     */
    override fun showAd() {
        when {
            isBidMachineCanShow() -> {
                bidMachineInterstitialAd?.show()
            }
            isMaxCanShow() -> {
                maxInterstitialAd?.showAd()
            }
            else -> {
                Log.d(TAG, "Nothing to show.")
            }
        }
    }

    private fun isBidMachineCanShow(): Boolean {
        return bidMachineInterstitialAd?.canShow() == true
    }

    private fun isMaxCanShow(): Boolean {
        return maxInterstitialAd?.isReady == true
    }

    override fun destroy() {
        bidMachineInterstitialAd?.destroy()
        bidMachineInterstitialAd = null

        maxInterstitialAd?.destroy()
        maxInterstitialAd = null
    }


    private inner class MaxInterstitialListener : MaxAdListener {

        override fun onAdLoaded(maxAd: MaxAd) {
            Log.d(TAG, "MAX interstitial - onAdLoaded, with revenue - ${maxAd.revenue}")
            Log.d(TAG, "Trying load BidMachine interstitial")

            loadBidMachine(maxAd.revenue)
        }

        override fun onAdLoadFailed(adUnitId: String, maxError: MaxError) {
            Log.d(TAG, "MAX interstitial - onAdLoadFailed, with error - ${maxError.message}")

            loadBidMachine(-1.0)
        }

        override fun onAdDisplayed(maxAd: MaxAd) {
            Log.d(TAG, "MAX interstitial - onAdDisplayed")
        }

        override fun onAdDisplayFailed(maxAd: MaxAd, maxError: MaxError) {
            Log.d(TAG, "MAX interstitial - onAdDisplayFailed, with error - ${maxError.message}")
        }

        override fun onAdClicked(maxAd: MaxAd) {
            Log.d(TAG, "MAX interstitial - onAdClicked")
        }

        override fun onAdHidden(maxAd: MaxAd) {
            Log.d(TAG, "MAX interstitial - onAdHidden")
        }

    }

    private inner class BidMachineInterstitialListener(private val adWrapperLoadListener: AdWrapperLoadListener) : InterstitialListener {

        override fun onAdLoaded(interstitialAd: InterstitialAd) {
            Log.d(TAG, "BidMachine interstitial - onAdLoaded, with eCPM - ${interstitialAd.auctionResult?.price}")

            adWrapperLoadListener.onAdLoaded()
        }

        override fun onAdLoadFailed(interstitialAd: InterstitialAd, bmError: BMError) {
            Log.d(TAG, "BidMachine interstitial - onAdLoadFailed, with error - ${bmError.message}")

            if (isMaxCanShow()) {
                adWrapperLoadListener.onAdLoaded()
            } else {
                adWrapperLoadListener.onAdFailToLoad()
            }
        }

        override fun onAdShown(interstitialAd: InterstitialAd) {
            Log.d(TAG, "BidMachine interstitial - onAdShown")
        }

        override fun onAdShowFailed(interstitialAd: InterstitialAd, bmError: BMError) {
            Log.d(TAG, "BidMachine interstitial - onAdShowFailed, with error - ${bmError.message}")
        }

        override fun onAdImpression(interstitialAd: InterstitialAd) {
            Log.d(TAG, "BidMachine interstitial - onAdImpression")
        }

        override fun onAdClicked(interstitialAd: InterstitialAd) {
            Log.d(TAG, "BidMachine interstitial - onAdClicked")
        }

        override fun onAdClosed(interstitialAd: InterstitialAd, finished: Boolean) {
            Log.d(TAG, "BidMachine interstitial - onAdClosed")
        }

        override fun onAdExpired(interstitialAd: InterstitialAd) {
            Log.d(TAG, "BidMachine interstitial - onAdExpired")
        }

    }

}