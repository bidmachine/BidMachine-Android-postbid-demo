package io.bidmachine.applovinmaxdemo.ad.bidmachine

import android.app.Activity
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObject
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObjectListener
import io.bidmachine.applovinmaxdemo.ad.bidmachine.BidMachineAdObjectUtils.toPriceFloorParams
import io.bidmachine.rewarded.RewardedAd
import io.bidmachine.rewarded.RewardedListener
import io.bidmachine.rewarded.RewardedRequest
import io.bidmachine.utils.BMError

class BidMachineRewardedAdObject : FullscreenAdObject() {

    private var rewardedAd: RewardedAd? = null

    override fun load(activity: Activity, priceFloor: Double?, listener: FullscreenAdObjectListener) {
        val request = RewardedRequest.Builder()
                .setPriceFloorParams(priceFloor?.toPriceFloorParams())
                .build()
        rewardedAd = RewardedAd(activity).apply {
            setListener(Listener(listener))
            load(request)
        }
    }

    override fun getPrice(): Double? = rewardedAd?.auctionResult?.price

    override fun canShow(): Boolean = rewardedAd?.canShow() == true

    override fun show(activity: Activity) {
        rewardedAd?.show()
    }

    override fun destroy() {
        rewardedAd?.also {
            it.setListener(null)
            it.destroy()
        }
        rewardedAd = null
    }


    private inner class Listener(private val listener: FullscreenAdObjectListener) : RewardedListener {

        override fun onAdLoaded(rewardedAd: RewardedAd) {
            listener.onLoaded(this@BidMachineRewardedAdObject)
        }

        override fun onAdLoadFailed(rewardedAd: RewardedAd, bmError: BMError) {
            listener.onFailToLoad(this@BidMachineRewardedAdObject, bmError.message)
        }

        override fun onAdShown(rewardedAd: RewardedAd) {
            listener.onShown(this@BidMachineRewardedAdObject)
        }

        override fun onAdShowFailed(rewardedAd: RewardedAd, bmError: BMError) {
            Utils.log(this@BidMachineRewardedAdObject, "onAdShowFailed, error - ${bmError.message}")
        }

        override fun onAdImpression(rewardedAd: RewardedAd) {
            Utils.log(this@BidMachineRewardedAdObject, "onAdImpression")
        }

        override fun onAdClicked(rewardedAd: RewardedAd) {
            listener.onClicked(this@BidMachineRewardedAdObject)
        }

        override fun onAdClosed(rewardedAd: RewardedAd, finished: Boolean) {
            listener.onClosed(this@BidMachineRewardedAdObject)
        }

        override fun onAdExpired(rewardedAd: RewardedAd) {
            Utils.log(this@BidMachineRewardedAdObject, "onAdExpired")
        }

        override fun onAdRewarded(rewardedAd: RewardedAd) {
            Utils.log(this@BidMachineRewardedAdObject, "onAdRewarded")
        }

    }

}