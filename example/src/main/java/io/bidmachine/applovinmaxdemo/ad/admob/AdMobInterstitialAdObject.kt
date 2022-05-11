package io.bidmachine.applovinmaxdemo.ad.admob

import android.app.Activity
import android.content.Context
import androidx.annotation.UiThread
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObject
import io.bidmachine.applovinmaxdemo.ad.FullscreenAdObjectListener
import io.bidmachine.applovinmaxdemo.ad.admob.AdMobAdObjectUtils.findAdUnit

/**
 * Make all calls to the Mobile Ads SDK on the main thread.
 */
class AdMobInterstitialAdObject : FullscreenAdObject() {

    companion object {
        /**
         * Each ad unit is configured in the [AdMob dashboard](https://apps.admob.com).
         * For each ad unit, you need to set up an eCPM floor.
         * [AD_UNIT_MAP] stores ad unit compliance and eCPM floor.
         */
        private val AD_UNIT_MAP =
            AdMobAdObjectUtils.createSortedSet(AdMobAdUnit("ADMOB_INTERSTITIAL_AD_UNIT_ID_1", 1.0),
                                               AdMobAdUnit("ADMOB_INTERSTITIAL_AD_UNIT_ID_2", 2.0),
                                               AdMobAdUnit("ADMOB_INTERSTITIAL_AD_UNIT_ID_3", 3.0),
                                               AdMobAdUnit("ADMOB_INTERSTITIAL_AD_UNIT_ID_4", 4.0),
                                               AdMobAdUnit("ADMOB_INTERSTITIAL_AD_UNIT_ID_5", 5.0),
                                               AdMobAdUnit("ADMOB_INTERSTITIAL_AD_UNIT_ID_6", 6.0),
                                               AdMobAdUnit("ADMOB_INTERSTITIAL_AD_UNIT_ID_7", 7.0),
                                               AdMobAdUnit("ADMOB_INTERSTITIAL_AD_UNIT_ID_8", 8.0),
                                               AdMobAdUnit("ADMOB_INTERSTITIAL_AD_UNIT_ID_9", 9.0),
                                               AdMobAdUnit("ADMOB_INTERSTITIAL_AD_UNIT_ID_10", 10.0),
                                               AdMobAdUnit("ADMOB_INTERSTITIAL_AD_UNIT_ID_11", 11.0),
                                               AdMobAdUnit("ADMOB_INTERSTITIAL_AD_UNIT_ID_12", 12.0),
                                               AdMobAdUnit("ADMOB_INTERSTITIAL_AD_UNIT_ID_13", 13.0),
                                               AdMobAdUnit("ADMOB_INTERSTITIAL_AD_UNIT_ID_14", 14.0),
                                               AdMobAdUnit("ADMOB_INTERSTITIAL_AD_UNIT_ID_15", 15.0),
                                               AdMobAdUnit("ADMOB_INTERSTITIAL_AD_UNIT_ID_16", 16.0),
                                               AdMobAdUnit("ADMOB_INTERSTITIAL_AD_UNIT_ID_17", 17.0),
                                               AdMobAdUnit("ADMOB_INTERSTITIAL_AD_UNIT_ID_18", 18.0),
                                               AdMobAdUnit("ADMOB_INTERSTITIAL_AD_UNIT_ID_19", 19.0),
                                               AdMobAdUnit("ADMOB_INTERSTITIAL_AD_UNIT_ID_20", 20.0))
    }

    private var adUnit: AdMobAdUnit? = null
    private var interstitialAd: InterstitialAd? = null

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

        InterstitialAd.load(context,
                            adUnit.id,
                            AdRequest.Builder().build(),
                            LoadListener(listener))
    }

    override fun getPrice(): Double? = adUnit?.price

    override fun canShow(): Boolean = interstitialAd != null

    override fun show(activity: Activity) {
        Utils.onUiThread {
            showAd(activity)
        }
    }

    @UiThread
    private fun showAd(activity: Activity) {
        interstitialAd?.show(activity)
    }

    override fun destroy() {
        adUnit = null
        Utils.onUiThread {
            destroyAd()
        }
    }

    @UiThread
    private fun destroyAd() {
        interstitialAd?.fullScreenContentCallback = null
        interstitialAd = null
    }


    private inner class LoadListener(private val listener: FullscreenAdObjectListener) : InterstitialAdLoadCallback() {

        override fun onAdLoaded(interstitialAd: InterstitialAd) {
            this@AdMobInterstitialAdObject.interstitialAd = interstitialAd.apply {
                fullScreenContentCallback = ShowListener(listener)
            }

            listener.onLoaded(this@AdMobInterstitialAdObject)
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            listener.onFailToLoad(this@AdMobInterstitialAdObject, loadAdError.message)
        }

    }

    private inner class ShowListener(private val listener: FullscreenAdObjectListener) : FullScreenContentCallback() {

        override fun onAdShowedFullScreenContent() {
            listener.onShown(this@AdMobInterstitialAdObject)
        }

        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
            Utils.log(this@AdMobInterstitialAdObject, "onAdShowFailed, error - ${adError.message}")
        }

        override fun onAdClicked() {
            listener.onClicked(this@AdMobInterstitialAdObject)
        }

        override fun onAdDismissedFullScreenContent() {
            listener.onClosed(this@AdMobInterstitialAdObject)
        }

    }

}