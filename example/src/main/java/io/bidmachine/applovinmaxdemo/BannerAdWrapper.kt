package io.bidmachine.applovinmaxdemo

import android.app.Activity
import android.util.Log
import android.view.ViewGroup
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdFormat
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import io.bidmachine.applovinmaxdemo.AdWrapper.Companion.TAG
import io.bidmachine.banner.BannerListener
import io.bidmachine.banner.BannerRequest
import io.bidmachine.banner.BannerSize
import io.bidmachine.banner.BannerView
import io.bidmachine.utils.BMError

class BannerAdWrapper : ViewAdWrapper {

    companion object {
        private const val MAX_AD_UNIT = "YOUR_BANNER_AD_UNIT_ID"

        private val BID_MACHINE_SIZE = BannerSize.Size_320x50
        private val MAX_SIZE = MaxAdFormat.BANNER.size
    }

    private var bidMachineBannerView: BannerView? = null
    private var maxAdView: MaxAdView? = null
    private var isMaxLoaded = false

    /**
     * At first load the Applovin MAX ad object,
     * and then based on this result, load the BidMachine ad.
     *
     * Please note that MAX banners has auto-refresh enabled by default. You can turn it off only after ad was loaded.
     */
    override fun loadAd(activity: Activity, adWrapperLoadListener: AdWrapperLoadListener) {
        destroy()

        Log.d(TAG, "Loading MAX banner")

        bidMachineBannerView = BannerView(activity).apply {
            layoutParams = createLayoutParams(resources, BID_MACHINE_SIZE.height)
            setListener(BidMachineBannerListener(adWrapperLoadListener))
        }
        maxAdView = MaxAdView(MAX_AD_UNIT, activity).apply {
            layoutParams = createLayoutParams(resources, MAX_SIZE.height)
            setListener(MaxBannerListener())
            loadAd()
        }
    }

    /**
     * Load the BidMachine ad object with price floor if you want to get more expensive ads.
     */
    private fun loadBidMachine(maxRevenue: Double?) {
        Log.d(TAG, "Loading BidMachine banner")

        BannerRequest.Builder()
            .setSize(BID_MACHINE_SIZE)
            .setPriceFloorParams(definePriceFloorParams(maxRevenue))
            .build()
            .also {
                bidMachineBannerView?.load(it)
            }
    }

    /**
     * You should give preference to the BidMachine ad object, because it has a higher price.
     */
    override fun showAd(adContainer: ViewGroup) {
        adContainer.removeAllViews()
        when {
            isBidMachineCanShow() -> {
                Log.d(TAG, "Showing BidMachine banner")

                adContainer.addView(bidMachineBannerView)
            }
            isMaxCanShow() -> {
                Log.d(TAG, "Showing MAX banner")

                adContainer.addView(maxAdView)
            }
            else -> {
                Log.d(TAG, "Nothing to show")
            }
        }
    }

    private fun isBidMachineCanShow(): Boolean {
        return bidMachineBannerView?.canShow() == true
    }

    private fun isMaxCanShow(): Boolean {
        return maxAdView != null && isMaxLoaded
    }

    override fun destroy() {
        Log.d(TAG, "Destroying banner")

        bidMachineBannerView?.destroy()
        bidMachineBannerView = null

        maxAdView?.destroy()
        maxAdView = null

        isMaxLoaded = false
    }


    private inner class MaxBannerListener : MaxAdViewAdListener {

        override fun onAdLoaded(maxAd: MaxAd) {
            Log.d(TAG, "MAX banner - onAdLoaded, with revenue - ${maxAd.revenue}")

            maxAdView?.stopAutoRefresh()
            isMaxLoaded = true
            loadBidMachine(maxAd.revenue)
        }

        override fun onAdLoadFailed(adUnitId: String, maxError: MaxError) {
            Log.d(TAG, "MAX banner - onAdLoadFailed, with error - ${maxError.message}")

            loadBidMachine(null)
        }

        override fun onAdDisplayed(maxAd: MaxAd) {
            Log.d(TAG, "MAX banner - onAdDisplayed")
        }

        override fun onAdDisplayFailed(maxAd: MaxAd, maxError: MaxError) {
            Log.d(TAG, "MAX banner - onAdDisplayFailed, with error - ${maxError.message}")
        }

        override fun onAdClicked(maxAd: MaxAd) {
            Log.d(TAG, "MAX banner - onAdClicked")
        }

        override fun onAdHidden(maxAd: MaxAd) {
            Log.d(TAG, "MAX banner - onAdHidden")
        }

        override fun onAdExpanded(ad: MaxAd?) {
            Log.d(TAG, "MAX banner - onAdExpanded")
        }

        override fun onAdCollapsed(ad: MaxAd?) {
            Log.d(TAG, "MAX banner - onAdCollapsed")
        }

    }

    private inner class BidMachineBannerListener(private val adWrapperLoadListener: AdWrapperLoadListener) :
        BannerListener {

        override fun onAdLoaded(bannerView: BannerView) {
            Log.d(TAG, "BidMachine banner - onAdLoaded, with eCPM - ${bannerView.auctionResult?.price}")

            adWrapperLoadListener.onAdLoaded()
        }

        override fun onAdLoadFailed(bannerView: BannerView, bmError: BMError) {
            Log.d(TAG, "BidMachine banner - onAdLoadFailed, with error - ${bmError.message}")

            if (isMaxCanShow()) {
                adWrapperLoadListener.onAdLoaded()
            } else {
                adWrapperLoadListener.onAdFailToLoad()
            }
        }

        override fun onAdShown(bannerView: BannerView) {
            Log.d(TAG, "BidMachine banner - onAdShown")
        }

        override fun onAdImpression(bannerView: BannerView) {
            Log.d(TAG, "BidMachine banner - onAdShown")
        }

        override fun onAdClicked(bannerView: BannerView) {
            Log.d(TAG, "BidMachine banner - onAdShown")
        }

        override fun onAdExpired(bannerView: BannerView) {
            Log.d(TAG, "BidMachine banner - onAdShown")
        }

    }

}