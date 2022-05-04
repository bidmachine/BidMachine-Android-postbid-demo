package io.bidmachine.applovinmaxdemo.ad.inmobi

import android.content.Context
import android.view.View
import com.inmobi.ads.AdMetaInfo
import com.inmobi.ads.InMobiAdRequestStatus
import com.inmobi.ads.InMobiBanner
import com.inmobi.ads.listeners.BannerAdEventListener
import io.bidmachine.applovinmaxdemo.ad.AdObject
import io.bidmachine.applovinmaxdemo.ad.AdObjectListener
import io.bidmachine.applovinmaxdemo.ad.ViewAdObject

class InMobiBannerAdObject : ViewAdObject() {

    companion object {
        private const val PLACEMENT_ID = 1473189489298L
    }

    private var inMobiBanner: InMobiBanner? = null
    private var price: Double? = null
    private var isLoaded = false

    override fun load(context: Context, priceFloor: Double?, listener: AdObjectListener<AdObject>) {
        inMobiBanner = InMobiBanner(context, PLACEMENT_ID).apply {
            setBannerSize(320, 50)
            setEnableAutoRefresh(false)
            setListener(Listener(listener))
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


    private inner class Listener(private val listener: AdObjectListener<AdObject>) : BannerAdEventListener() {

        override fun onAdFetchFailed(inMobiBanner: InMobiBanner, inMobiAdRequestStatus: InMobiAdRequestStatus) {
            listener.onFailToLoad(this@InMobiBannerAdObject, inMobiAdRequestStatus.message)
        }

        override fun onAdLoadSucceeded(inMobiBanner: InMobiBanner, adMetaInfo: AdMetaInfo) {
            price = adMetaInfo.bid
            isLoaded = true

            listener.onLoaded(this@InMobiBannerAdObject)
        }

        override fun onAdLoadFailed(inMobiBanner: InMobiBanner, inMobiAdRequestStatus: InMobiAdRequestStatus) {
            listener.onFailToLoad(this@InMobiBannerAdObject, inMobiAdRequestStatus.message)
        }

        override fun onAdDisplayed(inMobiBanner: InMobiBanner) {
            listener.onShown(this@InMobiBannerAdObject)
        }

        override fun onAdClicked(inMobiBanner: InMobiBanner, map: MutableMap<Any, Any>?) {
            listener.onClicked(this@InMobiBannerAdObject)
        }

    }

}