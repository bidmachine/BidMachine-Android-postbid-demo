package io.bidmachine.applovinmaxdemo.ad.bidmachine

import android.content.Context
import android.view.View
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.AdObject
import io.bidmachine.applovinmaxdemo.ad.AdObjectListener
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

    override fun load(context: Context,
                      priceFloor: Double?,
                      listener: AdObjectListener<AdObject>) {
        val request = BannerRequest.Builder()
                .setSize(BID_MACHINE_SIZE)
                .setPriceFloorParams(priceFloor?.toPriceFloorParams())
                .build()
        bannerView = BannerView(context).apply {
            layoutParams = Utils.createLayoutParams(resources, BID_MACHINE_SIZE.height)
            setListener(Listener(listener))
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


    private inner class Listener(private val listener: AdObjectListener<AdObject>) : BannerListener {

        override fun onAdLoaded(bannerView: BannerView) {
            listener.onLoaded(this@BidMachineBannerAdObject)
        }

        override fun onAdLoadFailed(bannerView: BannerView, bmError: BMError) {
            listener.onFailToLoad(this@BidMachineBannerAdObject, bmError.message)
        }

        override fun onAdShown(bannerView: BannerView) {
            listener.onShown(this@BidMachineBannerAdObject)
        }

        override fun onAdImpression(bannerView: BannerView) {
            Utils.log(this@BidMachineBannerAdObject, "onAdImpression")
        }

        override fun onAdClicked(bannerView: BannerView) {
            listener.onClicked(this@BidMachineBannerAdObject)
        }

        override fun onAdExpired(bannerView: BannerView) {
            Utils.log(this@BidMachineBannerAdObject, "onAdExpired")
        }

    }

}