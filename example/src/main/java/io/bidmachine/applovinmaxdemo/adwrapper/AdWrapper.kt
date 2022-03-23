package io.bidmachine.applovinmaxdemo.adwrapper

import android.app.Activity
import androidx.annotation.CallSuper
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.AdObject
import io.bidmachine.applovinmaxdemo.ad.AdObjectLoadListener
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicInteger

abstract class AdWrapper<AdObjectType : AdObject>(val adUnitId: String) {

    private val loadedAdList: MutableList<AdObjectType> = mutableListOf()

    private var weakActivity: WeakReference<Activity>? = null
    private var loadListener: AdWrapperLoadListener? = null
    private var postBidAdObjectList: List<AdObjectType>? = null
    private var postBidAdObjectInProgressCount = AtomicInteger()

    @CallSuper
    open fun loadAd(activity: Activity, adWrapperLoadListener: AdWrapperLoadListener) {
        destroy()
        weakActivity = WeakReference(activity)
        loadListener = adWrapperLoadListener

        Utils.log(this, "loadAd")
    }

    @CallSuper
    open fun destroy() {
        Utils.log(this, "destroy")

        weakActivity?.clear()
        weakActivity = null
        loadListener = null
        postBidAdObjectList?.forEach {
            it.destroy()
        }
        postBidAdObjectList = null
        loadedAdList.forEach {
            it.destroy()
        }
        loadedAdList.clear()
    }

    /**
     * Creates list of PostBid ad objects.
     */
    protected abstract fun createPostBidAdObjectList(): List<AdObjectType>

    /**
     * Starts load all PostBid ad objects which have been received from [createPostBidAdObjectList].
     */
    protected fun loadPostBidAds(priceFloor: Double?) {
        val activity = weakActivity?.get()
        if (activity == null) {
            onAdLoadingComplete()
            return
        }
        postBidAdObjectList = createPostBidAdObjectList().also {
            if (it.isEmpty()) {
                onAdLoadingComplete()
                return
            }
            Utils.log(this, "loadPostBidAds, price floor - $priceFloor, count - ${it.size}")

            postBidAdObjectInProgressCount.set(it.size)
            it.forEach { adObject ->
                adObject.load(activity, priceFloor, PostBidLoadListener())
            }
        }
    }

    /**
     * Gets AdObject with highest price to show.
     */
    protected fun getAdWithHighestPrice(): AdObjectType? {
        loadedAdList.sortByDescending {
            it.getPrice()
        }
        return loadedAdList.firstOrNull {
            it.canShow()
        }
    }

    private fun onAdObjectLoaded(adObject: AdObjectType) {
        Utils.log(adObject, "onAdObjectLoaded, price - ${adObject.getPrice()}")

        loadedAdList.add(adObject)
    }

    private fun onPostBidLoadingComplete() {
        val inProgressCount = postBidAdObjectInProgressCount.decrementAndGet()
        if (inProgressCount == 0) {
            onAdLoadingComplete()
        } else {
            Utils.log(this, "$inProgressCount PostBid object(s) left to load")
        }
    }

    private fun onAdLoadingComplete() {
        val loadedCount = loadedAdList.size
        if (loadedCount == 0) {
            Utils.log(this, "onAdFailToLoad")

            loadListener?.onAdFailToLoad()
        } else {
            Utils.log(this, "onAdLoaded, count of loaded object - $loadedCount")

            loadListener?.onAdLoaded()
        }
    }


    inner class LoadListener : AdObjectLoadListener<AdObjectType> {

        override fun onLoaded(adObject: AdObjectType) {
            onAdObjectLoaded(adObject)
            loadPostBidAds(adObject.getPrice())
        }

        override fun onFailToLoad(adObject: AdObjectType, errorMessage: String) {
            Utils.log(this, "onFailToLoad, error - $errorMessage")

            loadPostBidAds(null)
        }

    }

    private inner class PostBidLoadListener : AdObjectLoadListener<AdObjectType> {

        override fun onLoaded(adObject: AdObjectType) {
            onAdObjectLoaded(adObject)
            onPostBidLoadingComplete()
        }

        override fun onFailToLoad(adObject: AdObjectType, errorMessage: String) {
            Utils.log(adObject, "PostBid onFailToLoad, error - $errorMessage")

            onPostBidLoadingComplete()
        }

    }

}