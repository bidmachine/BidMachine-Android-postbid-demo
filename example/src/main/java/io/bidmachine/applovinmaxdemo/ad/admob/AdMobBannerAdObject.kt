package io.bidmachine.applovinmaxdemo.ad.admob

import android.content.Context
import android.view.View
import androidx.annotation.UiThread
import com.google.android.gms.ads.*
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.AdObject
import io.bidmachine.applovinmaxdemo.ad.AdObjectListener
import io.bidmachine.applovinmaxdemo.ad.ViewAdObject
import io.bidmachine.applovinmaxdemo.ad.admob.AdMobAdObjectUtils.findAdUnit

/**
 * Make all calls to the Mobile Ads SDK on the main thread.
 */
class AdMobBannerAdObject : ViewAdObject() {

    companion object {
        /**
         * Each ad unit is configured in the [AdMob dashboard](https://apps.admob.com).
         * For each ad unit, you need to set up an eCPM floor and switch off auto refresh.
         * [AD_UNIT_MAP] stores ad unit compliance and eCPM floor.
         */
        private val AD_UNIT_MAP =
            AdMobAdObjectUtils.createSortedSet(AdMobAdUnit("ADMOB_BANNER_AD_UNIT_ID_1", 1.0),
                                               AdMobAdUnit("ADMOB_BANNER_AD_UNIT_ID_2", 2.0),
                                               AdMobAdUnit("ADMOB_BANNER_AD_UNIT_ID_3", 3.0),
                                               AdMobAdUnit("ADMOB_BANNER_AD_UNIT_ID_4", 4.0),
                                               AdMobAdUnit("ADMOB_BANNER_AD_UNIT_ID_5", 5.0),
                                               AdMobAdUnit("ADMOB_BANNER_AD_UNIT_ID_6", 6.0),
                                               AdMobAdUnit("ADMOB_BANNER_AD_UNIT_ID_7", 7.0),
                                               AdMobAdUnit("ADMOB_BANNER_AD_UNIT_ID_8", 8.0),
                                               AdMobAdUnit("ADMOB_BANNER_AD_UNIT_ID_9", 9.0),
                                               AdMobAdUnit("ADMOB_BANNER_AD_UNIT_ID_10", 10.0),
                                               AdMobAdUnit("ADMOB_BANNER_AD_UNIT_ID_11", 11.0),
                                               AdMobAdUnit("ADMOB_BANNER_AD_UNIT_ID_12", 12.0),
                                               AdMobAdUnit("ADMOB_BANNER_AD_UNIT_ID_13", 13.0),
                                               AdMobAdUnit("ADMOB_BANNER_AD_UNIT_ID_14", 14.0),
                                               AdMobAdUnit("ADMOB_BANNER_AD_UNIT_ID_15", 15.0),
                                               AdMobAdUnit("ADMOB_BANNER_AD_UNIT_ID_16", 16.0),
                                               AdMobAdUnit("ADMOB_BANNER_AD_UNIT_ID_17", 17.0),
                                               AdMobAdUnit("ADMOB_BANNER_AD_UNIT_ID_18", 18.0),
                                               AdMobAdUnit("ADMOB_BANNER_AD_UNIT_ID_19", 19.0),
                                               AdMobAdUnit("ADMOB_BANNER_AD_UNIT_ID_20", 20.0))
    }

    private var adUnit: AdMobAdUnit? = null
    private var adView: AdView? = null
    private var isLoaded = false

    /**
     * Switch off auto refresh for ad unit in the [AdMob dashboard](https://apps.admob.com) before load it.
     * Finds the first [AdMobAdUnit] whose price is equal to or greater than the price floor and loads it.
     */
    override fun load(context: Context, priceFloor: Double?, listener: AdObjectListener<AdObject>) {
        AD_UNIT_MAP.findAdUnit(priceFloor)?.also { adUnit ->
            Utils.onUiThread {
                loadAd(context, adUnit, listener)
            }
        } ?: listener.onFailToLoad(this, "Can't find AdMobAdUnit at this price floor - $priceFloor")
    }

    @UiThread
    private fun loadAd(context: Context, adUnit: AdMobAdUnit, listener: AdObjectListener<AdObject>) {
        this.adUnit = adUnit

        adView = AdView(context).apply {
            adUnitId = adUnit.id
            adSize = AdSize.BANNER
            adListener = Listener(listener)
            loadAd(AdRequest.Builder().build())
        }
    }

    override fun getPrice(): Double? = adUnit?.price

    override fun canShow(): Boolean = isLoaded && adView != null

    override fun getView(): View? = adView

    override fun destroy() {
        adUnit = null
        isLoaded = false
        Utils.onUiThread {
            destroyAd()
        }
    }

    @UiThread
    private fun destroyAd() {
        adView?.destroy()
        adView = null
    }


    private inner class Listener(private val listener: AdObjectListener<AdObject>) : AdListener() {

        override fun onAdLoaded() {
            isLoaded = true

            listener.onLoaded(this@AdMobBannerAdObject)
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            listener.onFailToLoad(this@AdMobBannerAdObject, loadAdError.message)
        }

        override fun onAdImpression() {
            listener.onShown(this@AdMobBannerAdObject)
        }

        override fun onAdClicked() {
            listener.onClicked(this@AdMobBannerAdObject)
        }

    }

}