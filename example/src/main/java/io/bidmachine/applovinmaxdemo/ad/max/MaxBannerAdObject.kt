package io.bidmachine.applovinmaxdemo.ad.max

import android.content.Context
import android.view.View
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdFormat
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.AdObject
import io.bidmachine.applovinmaxdemo.ad.AdObjectListener
import io.bidmachine.applovinmaxdemo.ad.ViewAdObject
import io.bidmachine.applovinmaxdemo.ad.max.MaxAdObjectUtils.getCPM

class MaxBannerAdObject(private val adUnitId: String) : ViewAdObject() {

    companion object {
        private val MAX_SIZE = MaxAdFormat.BANNER.size
    }

    private var maxAdView: MaxAdView? = null
    private var price: Double? = null
    private var isLoaded = false

    override fun load(context: Context, priceFloor: Double?, listener: AdObjectListener<AdObject>) {
        maxAdView = MaxAdView(adUnitId, context).apply {
            layoutParams = Utils.createLayoutParams(resources, MAX_SIZE.height)
            setListener(Listener(listener))
            loadAd()
        }
    }

    override fun getPrice(): Double? = price

    override fun canShow(): Boolean = isLoaded && maxAdView != null

    override fun getView(): View? = maxAdView

    override fun destroy() {
        isLoaded = false
        price = null
        maxAdView?.destroy()
        maxAdView = null
    }


    private inner class Listener(private val listener: AdObjectListener<AdObject>) : MaxAdViewAdListener {

        override fun onAdLoaded(maxAd: MaxAd) {
            // Switch off auto refresh for MaxAdView
            maxAdView?.stopAutoRefresh()
            // Extension function to obtain CPM
            price = maxAd.getCPM()
            isLoaded = true

            listener.onLoaded(this@MaxBannerAdObject)
        }

        override fun onAdLoadFailed(adUnitId: String, maxError: MaxError) {
            listener.onFailToLoad(this@MaxBannerAdObject, maxError.message)
        }

        override fun onAdDisplayed(maxAd: MaxAd) {
            listener.onShown(this@MaxBannerAdObject)
        }

        override fun onAdDisplayFailed(maxAd: MaxAd, maxError: MaxError) {
            Utils.log(this@MaxBannerAdObject, "onAdDisplayFailed, error - ${maxError.message}")
        }

        override fun onAdClicked(maxAd: MaxAd) {
            listener.onClicked(this@MaxBannerAdObject)
        }

        override fun onAdHidden(maxAd: MaxAd) {
            Utils.log(this@MaxBannerAdObject, "onAdHidden")
        }

        override fun onAdExpanded(ad: MaxAd?) {
            Utils.log(this@MaxBannerAdObject, "onAdExpanded")
        }

        override fun onAdCollapsed(ad: MaxAd?) {
            Utils.log(this@MaxBannerAdObject, "onAdCollapsed")
        }

    }

}