package io.bidmachine.applovinmaxdemo.ad.admob

import android.app.Activity
import android.content.Context
import androidx.annotation.UiThread
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObject
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObjectListener
import io.bidmachine.applovinmaxdemo.ad.admob.AdMobAdObjectUtils.findAdUnit

/**
 * Make all calls to the Mobile Ads SDK on the main thread.
 */
class AdMobRewardedAdObject : FullscreenAdObject() {

    companion object {
        /**
         * Each ad unit is configured in the [AdMob dashboard](https://apps.admob.com).
         * For each ad unit, you need to set up an eCPM floor.
         * [AD_UNIT_MAP] stores ad unit compliance and eCPM floor.
         */
        private val AD_UNIT_MAP =
            AdMobAdObjectUtils.createSortedSet(AdMobAdUnit("ADMOB_REWARDED_AD_UNIT_ID_1", 1.0),
                                               AdMobAdUnit("ADMOB_REWARDED_AD_UNIT_ID_2", 2.0),
                                               AdMobAdUnit("ADMOB_REWARDED_AD_UNIT_ID_3", 3.0),
                                               AdMobAdUnit("ADMOB_REWARDED_AD_UNIT_ID_4", 4.0),
                                               AdMobAdUnit("ADMOB_REWARDED_AD_UNIT_ID_5", 5.0),
                                               AdMobAdUnit("ADMOB_REWARDED_AD_UNIT_ID_6", 6.0),
                                               AdMobAdUnit("ADMOB_REWARDED_AD_UNIT_ID_7", 7.0),
                                               AdMobAdUnit("ADMOB_REWARDED_AD_UNIT_ID_8", 8.0),
                                               AdMobAdUnit("ADMOB_REWARDED_AD_UNIT_ID_9", 9.0),
                                               AdMobAdUnit("ADMOB_REWARDED_AD_UNIT_ID_10", 10.0),
                                               AdMobAdUnit("ADMOB_REWARDED_AD_UNIT_ID_11", 11.0),
                                               AdMobAdUnit("ADMOB_REWARDED_AD_UNIT_ID_12", 12.0),
                                               AdMobAdUnit("ADMOB_REWARDED_AD_UNIT_ID_13", 13.0),
                                               AdMobAdUnit("ADMOB_REWARDED_AD_UNIT_ID_14", 14.0),
                                               AdMobAdUnit("ADMOB_REWARDED_AD_UNIT_ID_15", 15.0),
                                               AdMobAdUnit("ADMOB_REWARDED_AD_UNIT_ID_16", 16.0),
                                               AdMobAdUnit("ADMOB_REWARDED_AD_UNIT_ID_17", 17.0),
                                               AdMobAdUnit("ADMOB_REWARDED_AD_UNIT_ID_18", 18.0),
                                               AdMobAdUnit("ADMOB_REWARDED_AD_UNIT_ID_19", 19.0),
                                               AdMobAdUnit("ADMOB_REWARDED_AD_UNIT_ID_20", 20.0))
    }

    private var adUnit: AdMobAdUnit? = null
    private var rewardedAd: RewardedAd? = null

    /**
     * Finds the first [AdMobAdUnit] whose price is equal to or greater than the price floor and loads it.
     */
    override fun load(activity: Activity, priceFloor: Double?, listener: FullscreenAdObjectListener) {
        AD_UNIT_MAP.findAdUnit(priceFloor)?.also { adUnit ->
            Utils.onUiThread {
                loadAd(activity, adUnit, listener)
            }
        } ?: listener.onFailToLoad(this, "Can't find AdMobAdUnit at this price floor - $priceFloor")
    }

    @UiThread
    private fun loadAd(context: Context, adUnit: AdMobAdUnit, listener: FullscreenAdObjectListener) {
        this.adUnit = adUnit

        RewardedAd.load(context,
                        adUnit.id,
                        AdRequest.Builder().build(),
                        LoadListener(listener))
    }

    override fun getPrice(): Double? = adUnit?.price

    override fun canShow(): Boolean = rewardedAd != null

    override fun show(activity: Activity) {
        Utils.onUiThread {
            showAd(activity)
        }
    }

    @UiThread
    private fun showAd(activity: Activity) {
        rewardedAd?.show(activity, RewardListener())
    }

    override fun destroy() {
        adUnit = null
        Utils.onUiThread {
            destroyAd()
        }
    }

    @UiThread
    private fun destroyAd() {
        rewardedAd?.fullScreenContentCallback = null
        rewardedAd = null
    }


    private inner class LoadListener(private val listener: FullscreenAdObjectListener) : RewardedAdLoadCallback() {

        override fun onAdLoaded(rewardedAd: RewardedAd) {
            this@AdMobRewardedAdObject.rewardedAd = rewardedAd.apply {
                fullScreenContentCallback = ShowListener(listener)
            }

            listener.onLoaded(this@AdMobRewardedAdObject)
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            listener.onFailToLoad(this@AdMobRewardedAdObject, loadAdError.message)
        }

    }

    private inner class ShowListener(private val listener: FullscreenAdObjectListener) : FullScreenContentCallback() {

        override fun onAdShowedFullScreenContent() {
            listener.onShown(this@AdMobRewardedAdObject)
        }

        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
            Utils.log(this@AdMobRewardedAdObject, "onAdShowFailed, error - ${adError.message}")
        }

        override fun onAdClicked() {
            listener.onClicked(this@AdMobRewardedAdObject)
        }

        override fun onAdDismissedFullScreenContent() {
            listener.onClosed(this@AdMobRewardedAdObject)
        }

    }

    private inner class RewardListener : OnUserEarnedRewardListener {

        override fun onUserEarnedReward(rewardItem: RewardItem) {
            Utils.log(this@AdMobRewardedAdObject, "onUserEarnedReward - ${rewardItem.amount} ${rewardItem.type}")
        }

    }

}