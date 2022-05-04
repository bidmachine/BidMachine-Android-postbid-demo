package io.bidmachine.applovinmaxdemo.adwrapper

import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObject
import io.bidmachine.applovinmaxdemo.ad.bidmachine.BidMachineRewardedAdObject
import io.bidmachine.applovinmaxdemo.ad.inmobi.InMobiRewardedAdObject
import io.bidmachine.applovinmaxdemo.ad.max.MaxRewardedAdObject

class RewardedAdWrapper(adUnitId: String) : FullscreenAdWrapper(adUnitId) {

    /**
     * Creates MAX rewarded ad object.
     */
    override fun createMaxAdObject(): FullscreenAdObject = MaxRewardedAdObject(adUnitId)

    /**
     * Creates list of rewarded PostBid ad objects.
     */
    override fun createPostBidAdObjectList(): List<FullscreenAdObject> = listOf(BidMachineRewardedAdObject(),
                                                                                InMobiRewardedAdObject())

}