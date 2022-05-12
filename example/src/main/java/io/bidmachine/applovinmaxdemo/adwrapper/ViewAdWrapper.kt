package io.bidmachine.applovinmaxdemo.adwrapper

import android.content.Context
import android.view.ViewGroup
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.ViewAdObject
import java.lang.ref.WeakReference

abstract class ViewAdWrapper(adUnitId: String) : AdWrapper<AdWrapperListener, ViewAdObject>(adUnitId) {

    private var weakContext: WeakReference<Context>? = null

    /**
     * At first load the Applovin MAX ad object,
     * and then based on this result load other PostBid ad objects.
     *
     * Please note that MAX banners has auto-refresh enabled by default.
     * You must turn it off after ad was loaded. [com.applovin.mediation.ads.MaxAdView.stopAutoRefresh]
     */
    fun loadAd(context: Context, adWrapperListener: AdWrapperListener) {
        setupMaxAdObject(adWrapperListener).apply {
            weakContext = WeakReference(context)

            Utils.log(this, "load ad")

            load(context, null, Listener())
        }
    }

    override fun loadPostBidAd(adObject: ViewAdObject, priceFloor: Double?) {
        val context = weakContext?.get()
        if (context == null) {
            onPostBidAdObjectLoadFail(adObject, "Context is null")
            return
        }
        adObject.load(context, priceFloor, PostBidListener())
    }

    /**
     * Shows ad object with highest price.
     */
    fun showAd(adContainer: ViewGroup) {
        adContainer.removeAllViews()
        getAdWithHighestPrice()?.also {
            Utils.log(this, "showAd, ${it.javaClass.simpleName} (price: ${it.getPrice()})")

            adContainer.addView(it.getView())
        } ?: Utils.log(this, "Nothing to show", true)
    }

    override fun destroy() {
        super.destroy()

        weakContext?.clear()
        weakContext = null
    }

}