package io.bidmachine.applovinmaxdemo.ad.bidmachine

import android.app.Activity
import android.view.View
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.AdObject
import io.bidmachine.applovinmaxdemo.ad.AdObjectLoadListener
import io.bidmachine.applovinmaxdemo.ad.ViewAdObject
import io.bidmachine.applovinmaxdemo.ad.bidmachine.BidMachineAdObjectUtils.toPriceFloorParams
import io.bidmachine.banner.BannerListener
import io.bidmachine.banner.BannerRequest
import io.bidmachine.banner.BannerSize
import io.bidmachine.banner.BannerView
import io.bidmachine.utils.BMError

class BidMachineBannerAdObject : ViewAdObject() {

    companion object {
        private val BID_MACHINE_SIZE = BannerSize.Size_320x50
    }

    private var bannerView: BannerView? = null

    override fun load(activity: Activity,
                      priceFloor: Double?,
                      loadListener: AdObjectLoadListener<AdObject>) {
        val request = BannerRequest.Builder()
                .setSize(BID_MACHINE_SIZE)
                .setPriceFloorParams(priceFloor?.toPriceFloorParams())
                .build()
        bannerView = BannerView(activity).apply {
            layoutParams = Utils.createLayoutParams(resources, BID_MACHINE_SIZE.height)
            setListener(Listener(loadListener))
            load(request)
        }
    }

    override fun getPrice(): Double? = bannerView?.auctionResult?.price

    override fun canShow(): Boolean = bannerView?.canShow() == true

    override fun getView(): View? = bannerView

    override fun destroy() {
        bannerView?.also {
            it.setListener(null)
            it.destroy()
        }
        bannerView = null
    }


    private inner class Listener(private val loadListener: AdObjectLoadListener<AdObject>) : BannerListener {

        override fun onAdLoaded(bannerView: BannerView) {
            loadListener.onLoaded(this@BidMachineBannerAdObject)
        }

        override fun onAdLoadFailed(bannerView: BannerView, bmError: BMError) {
            loadListener.onFailToLoad(this@BidMachineBannerAdObject, bmError.message)
        }

        override fun onAdShown(bannerView: BannerView) {
            Utils.log(this@BidMachineBannerAdObject, "onAdShown")
        }

        override fun onAdImpression(bannerView: BannerView) {
            Utils.log(this@BidMachineBannerAdObject, "onAdImpression")
        }

        override fun onAdClicked(bannerView: BannerView) {
            Utils.log(this@BidMachineBannerAdObject, "onAdClicked")
        }

        override fun onAdExpired(bannerView: BannerView) {
            Utils.log(this@BidMachineBannerAdObject, "onAdExpired")
        }

    }

}