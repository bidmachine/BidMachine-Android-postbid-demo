package io.bidmachine.applovinmaxdemo.adwrapper

import android.app.Activity
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObject
import io.bidmachine.applovinmaxdemo.ad.bidmachine.BidMachineRewardedAdObject
import io.bidmachine.applovinmaxdemo.ad.inmobi.InMobiRewardedAdObject
import io.bidmachine.applovinmaxdemo.ad.max.MaxRewardedAdObject

class RewardedAdWrapper(adUnitId: String) : FullscreenAdWrapper(adUnitId) {

    private var maxRewardedAdObject: MaxRewardedAdObject? = null

    /**
     * At first load the Applovin MAX ad object,
     * and then based on this result load other PostBid ad objects.
     */
    override fun loadAd(activity: Activity, adWrapperLoadListener: AdWrapperLoadListener) {
        super.loadAd(activity, adWrapperLoadListener)

        maxRewardedAdObject = MaxRewardedAdObject(adUnitId).apply {
            load(activity, null, LoadListener())
        }
    }

    /**
     * Creates list of rewarded PostBid ad objects.
     */
    override fun createPostBidAdObjectList(): List<FullscreenAdObject> = listOf(BidMachineRewardedAdObject(),
                                                                                InMobiRewardedAdObject())

    override fun destroy() {
        super.destroy()

        maxRewardedAdObject?.destroy()
        maxRewardedAdObject = null
    }

}