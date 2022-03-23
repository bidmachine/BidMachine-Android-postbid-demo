package io.bidmachine.applovinmaxdemo.ad.inmobi

import android.app.Activity
import android.view.View
import com.inmobi.ads.AdMetaInfo
import com.inmobi.ads.InMobiAdRequestStatus
import com.inmobi.ads.InMobiBanner
import com.inmobi.ads.listeners.BannerAdEventListener
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.AdObject
import io.bidmachine.applovinmaxdemo.ad.AdObjectLoadListener
import io.bidmachine.applovinmaxdemo.ad.ViewAdObject

class InMobiBannerAdObject : ViewAdObject() {

    companion object {
        private const val PLACEMENT_ID = 1473189489298L
    }

    private var inMobiBanner: InMobiBanner? = null
    private var price: Double? = null
    private var isLoaded = false

    override fun load(activity: Activity, priceFloor: Double?, loadListener: AdObjectLoadListener<AdObject>) {
        inMobiBanner = InMobiBanner(activity, PLACEMENT_ID).apply {
            setBannerSize(320, 50)
            setEnableAutoRefresh(false)
            setListener(Listener(loadListener))
            load()
        }
    }

    override fun getPrice(): Double? = price

    override fun canShow(): Boolean = isLoaded && inMobiBanner != null

    override fun getView(): View? = inMobiBanner

    override fun destroy() {
        isLoaded = false
        price = null
        inMobiBanner?.destroy()
        inMobiBanner = null
    }


    private inner class Listener(private val loadListener: AdObjectLoadListener<AdObject>) : BannerAdEventListener() {

        override fun onAdFetchFailed(inMobiBanner: InMobiBanner, inMobiAdRequestStatus: InMobiAdRequestStatus) {
            loadListener.onFailToLoad(this@InMobiBannerAdObject, inMobiAdRequestStatus.message)
        }

        override fun onAdLoadSucceeded(inMobiBanner: InMobiBanner, adMetaInfo: AdMetaInfo) {
            price = adMetaInfo.bid
            isLoaded = true

            loadListener.onLoaded(this@InMobiBannerAdObject)
        }

        override fun onAdLoadFailed(inMobiBanner: InMobiBanner, inMobiAdRequestStatus: InMobiAdRequestStatus) {
            loadListener.onFailToLoad(this@InMobiBannerAdObject, inMobiAdRequestStatus.message)
        }

        override fun onAdDisplayed(inMobiBanner: InMobiBanner) {
            Utils.log(this@InMobiBannerAdObject, "onAdDisplayed")
        }

        override fun onAdClicked(inMobiBanner: InMobiBanner, map: MutableMap<Any, Any>?) {
            Utils.log(this@InMobiBannerAdObject, "onAdClicked")
        }

    }

}