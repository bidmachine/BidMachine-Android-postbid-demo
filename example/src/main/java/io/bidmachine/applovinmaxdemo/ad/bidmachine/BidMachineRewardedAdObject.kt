package io.bidmachine.applovinmaxdemo.ad.bidmachine

import android.app.Activity
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.AdObject
import io.bidmachine.applovinmaxdemo.ad.AdObjectLoadListener
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObject
import io.bidmachine.applovinmaxdemo.ad.bidmachine.BidMachineAdObjectUtils.toPriceFloorParams
import io.bidmachine.rewarded.RewardedAd
import io.bidmachine.rewarded.RewardedListener
import io.bidmachine.rewarded.RewardedRequest
import io.bidmachine.utils.BMError

class BidMachineRewardedAdObject : FullscreenAdObject() {

    private var rewardedAd: RewardedAd? = null

    override fun load(activity: Activity, priceFloor: Double?, loadListener: AdObjectLoadListener<AdObject>) {
        val request = RewardedRequest.Builder()
                .setPriceFloorParams(priceFloor?.toPriceFloorParams())
                .build()
        rewardedAd = RewardedAd(activity).apply {
            setListener(Listener(loadListener))
            load(request)
        }
    }

    override fun getPrice(): Double? = rewardedAd?.auctionResult?.price

    override fun canShow(): Boolean = rewardedAd?.canShow() == true

    override fun show() {
        rewardedAd?.show()
    }

    override fun destroy() {
        rewardedAd?.also {
            it.setListener(null)
            it.destroy()
        }
        rewardedAd = null
    }


    private inner class Listener(private val loadListener: AdObjectLoadListener<AdObject>) : RewardedListener {

        override fun onAdLoaded(rewardedAd: RewardedAd) {
            loadListener.onLoaded(this@BidMachineRewardedAdObject)
        }

        override fun onAdLoadFailed(rewardedAd: RewardedAd, bmError: BMError) {
            loadListener.onFailToLoad(this@BidMachineRewardedAdObject, bmError.message)
        }

        override fun onAdShown(rewardedAd: RewardedAd) {
            Utils.log(this@BidMachineRewardedAdObject, "onAdShown")
        }

        override fun onAdShowFailed(rewardedAd: RewardedAd, bmError: BMError) {
            Utils.log(this@BidMachineRewardedAdObject, "onAdShowFailed, error - ${bmError.message}")
        }

        override fun onAdImpression(rewardedAd: RewardedAd) {
            Utils.log(this@BidMachineRewardedAdObject, "onAdImpression")
        }

        override fun onAdClicked(rewardedAd: RewardedAd) {
            Utils.log(this@BidMachineRewardedAdObject, "onAdClicked")
        }

        override fun onAdClosed(rewardedAd: RewardedAd, finished: Boolean) {
            Utils.log(this@BidMachineRewardedAdObject, "onAdClosed")
        }

        override fun onAdExpired(rewardedAd: RewardedAd) {
            Utils.log(this@BidMachineRewardedAdObject, "onAdExpired")
        }

        override fun onAdRewarded(rewardedAd: RewardedAd) {
            Utils.log(this@BidMachineRewardedAdObject, "onAdRewarded")
        }

    }

}