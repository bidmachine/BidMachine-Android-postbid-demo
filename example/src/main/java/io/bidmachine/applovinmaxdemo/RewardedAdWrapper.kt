package io.bidmachine.applovinmaxdemo

import android.app.Activity
import android.util.Log
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.MaxReward
import com.applovin.mediation.MaxRewardedAdListener
import com.applovin.mediation.ads.MaxRewardedAd
import io.bidmachine.applovinmaxdemo.AdWrapper.Companion.TAG
import io.bidmachine.rewarded.RewardedAd
import io.bidmachine.rewarded.RewardedListener
import io.bidmachine.rewarded.RewardedRequest
import io.bidmachine.utils.BMError

class RewardedAdWrapper : AdWrapper {

    companion object {
        private const val MAX_AD_UNIT = "YOUR_REWARDED_AD_UNIT_ID"
    }

    private var bidMachineRewardedAd: RewardedAd? = null
    private var maxRewardedAd: MaxRewardedAd? = null

    /**
     * At first load the Applovin MAX ad object,
     * and then based on this result, load the BidMachine ad.
     */
    override fun loadAd(activity: Activity, adWrapperLoadListener: AdWrapperLoadListener) {
        destroy()

        bidMachineRewardedAd = RewardedAd(activity).apply {
            setListener(BidMachineRewardedListener(adWrapperLoadListener))
        }
        maxRewardedAd = MaxRewardedAd.getInstance(MAX_AD_UNIT, activity).apply {
            setListener(MaxRewardedListener())
            loadAd()
        }
    }

    /**
     * Load the BidMachine ad object with price floor if you want to get more expensive ads.
     */
    private fun loadBidMachine(maxRevenue: Double) {
        RewardedRequest.Builder()
                .apply {
                    val priceFloorParams = definePriceFloorParams(maxRevenue)
                    if (priceFloorParams != null) {
                        setPriceFloorParams(priceFloorParams)
                    }
                }
                .build()
                .also {
                    bidMachineRewardedAd?.load(it)
                }
    }

    /**
     * You should give preference to the BidMachine ad object, because it has a higher price.
     */
    override fun showAd() {
        when {
            isBidMachineCanShow() -> {
                bidMachineRewardedAd?.show()
            }
            isMaxCanShow() -> {
                maxRewardedAd?.showAd()
            }
            else -> {
                Log.d(TAG, "Nothing to show.")
            }
        }
    }

    private fun isBidMachineCanShow(): Boolean {
        return bidMachineRewardedAd?.canShow() == true
    }

    private fun isMaxCanShow(): Boolean {
        return maxRewardedAd?.isReady == true
    }

    override fun destroy() {
        maxRewardedAd?.destroy()
        maxRewardedAd = null

        bidMachineRewardedAd?.destroy()
        bidMachineRewardedAd = null
    }


    private inner class MaxRewardedListener : MaxRewardedAdListener {

        override fun onAdLoaded(maxAd: MaxAd) {
            Log.d(TAG, "MAX rewarded - onAdLoaded, with revenue - ${maxAd.revenue}")
            Log.d(TAG, "Trying load BidMachine rewarded")

            loadBidMachine(maxAd.revenue)
        }

        override fun onAdLoadFailed(adUnitId: String, maxError: MaxError) {
            Log.d(TAG, "MAX rewarded - onAdLoadFailed, with error - ${maxError.message}")

            loadBidMachine(-1.0)
        }

        override fun onAdDisplayed(maxAd: MaxAd) {
            Log.d(TAG, "MAX rewarded - onAdDisplayed")
        }

        override fun onAdDisplayFailed(maxAd: MaxAd, maxError: MaxError) {
            Log.d(TAG, "MAX rewarded - onAdDisplayFailed, with error - ${maxError.message}")
        }

        override fun onAdClicked(maxAd: MaxAd) {
            Log.d(TAG, "MAX rewarded - onAdClicked")
        }

        override fun onAdHidden(maxAd: MaxAd) {
            Log.d(TAG, "MAX rewarded - onAdHidden")
        }

        override fun onRewardedVideoStarted(maxAd: MaxAd) {
            Log.d(TAG, "MAX rewarded - onRewardedVideoStarted")
        }

        override fun onRewardedVideoCompleted(maxAd: MaxAd) {
            Log.d(TAG, "MAX rewarded - onRewardedVideoCompleted")
        }

        override fun onUserRewarded(maxAd: MaxAd, maxReward: MaxReward) {
            Log.d(TAG, "MAX rewarded - onUserRewarded")
        }

    }

    private inner class BidMachineRewardedListener(private val adWrapperLoadListener: AdWrapperLoadListener) : RewardedListener {

        override fun onAdLoaded(rewardedAd: RewardedAd) {
            Log.d(TAG, "BidMachine rewarded - onAdLoaded, with eCPM - ${rewardedAd.auctionResult?.price}")

            adWrapperLoadListener.onAdLoaded()
        }

        override fun onAdLoadFailed(rewardedAd: RewardedAd, bmError: BMError) {
            Log.d(TAG, "BidMachine rewarded - onAdLoadFailed, with error - ${bmError.message}")

            if (isMaxCanShow()) {
                adWrapperLoadListener.onAdLoaded()
            } else {
                adWrapperLoadListener.onAdFailToLoad()
            }
        }

        override fun onAdShown(rewardedAd: RewardedAd) {
            Log.d(TAG, "BidMachine rewarded - onAdShown")
        }

        override fun onAdShowFailed(rewardedAd: RewardedAd, bmError: BMError) {
            Log.d(TAG, "BidMachine rewarded - onAdShowFailed, with error - ${bmError.message}")
        }

        override fun onAdImpression(rewardedAd: RewardedAd) {
            Log.d(TAG, "BidMachine rewarded - onAdImpression")
        }

        override fun onAdClicked(rewardedAd: RewardedAd) {
            Log.d(TAG, "BidMachine rewarded - onAdClicked")
        }

        override fun onAdClosed(rewardedAd: RewardedAd, finished: Boolean) {
            Log.d(TAG, "BidMachine rewarded - onAdClosed")
        }

        override fun onAdExpired(rewardedAd: RewardedAd) {
            Log.d(TAG, "BidMachine rewarded - onAdExpired")
        }

        override fun onAdRewarded(rewardedAd: RewardedAd) {
            Log.d(TAG, "BidMachine rewarded - onAdRewarded")
        }

    }

}