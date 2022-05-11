package io.bidmachine.applovinmaxdemo.adwrapper

import androidx.annotation.CallSuper
import io.bidmachine.applovinmaxdemo.Utils
import io.bidmachine.applovinmaxdemo.ad.AdObject
import io.bidmachine.applovinmaxdemo.ad.AdObjectListener
import java.util.concurrent.atomic.AtomicInteger

abstract class AdWrapper<AdWrapperListenerType : AdWrapperListener, AdObjectType : AdObject>(val adUnitId: String) {

    private val loadedAdList: MutableList<AdObjectType> = mutableListOf()

    protected var listener: AdWrapperListenerType? = null

    private var maxAdObject: AdObjectType? = null
    private var postBidAdObjectList: List<AdObjectType>? = null
    private var postBidAdObjectInProgressCount = AtomicInteger()

    abstract fun createMaxAdObject(): AdObjectType

    @CallSuper
    protected fun setupMaxAdObject(adWrapperListener: AdWrapperListenerType): AdObjectType {
        destroy()
        listener = adWrapperListener

        Utils.log(this, "setup max")

        return createMaxAdObject().apply {
            maxAdObject = this
        }
    }

    @CallSuper
    open fun destroy() {
        Utils.log(this, "destroy")

        listener = null
        postBidAdObjectList?.forEach {
            it.destroy()
        }
        postBidAdObjectList = null
        loadedAdList.forEach {
            it.destroy()
        }
        loadedAdList.clear()
        maxAdObject?.destroy()
        maxAdObject = null
    }

    /**
     * Creates list of PostBid ad objects.
     */
    protected abstract fun createPostBidAdObjectList(): List<AdObjectType>

    /**
     * Starts load all PostBid ad objects which have been received from [createPostBidAdObjectList].
     */
    protected fun loadPostBidAds(priceFloor: Double?) {
        postBidAdObjectList = createPostBidAdObjectList().also { adObjectList ->
            if (adObjectList.isEmpty()) {
                onAdLoadingComplete()
                return
            }
            Utils.log(this, "loadPostBidAds, price floor - $priceFloor, count - ${adObjectList.size}")

            postBidAdObjectInProgressCount.set(adObjectList.size)

            adObjectList.forEach { adObject ->
                loadPostBidAd(adObject, priceFloor)
            }
        }
    }

    /**
     * Starts load PostBid ad object.
     */
    protected abstract fun loadPostBidAd(adObject: AdObjectType, priceFloor: Double?)

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

    private fun onPostBidAdLoadingComplete() {
        val inProgressCount = postBidAdObjectInProgressCount.decrementAndGet()
        if (inProgressCount == 0) {
            onAdLoadingComplete()
        } else {
            Utils.log(this, "$inProgressCount PostBid object(s) left to load")
        }
    }

    protected fun onPostBidAdLoadFail(adObject: AdObjectType, errorMessage: String) {
        Utils.log(adObject, "PostBid onFailToLoad, error - $errorMessage", true)

        onPostBidAdLoadingComplete()
    }

    private fun onAdLoadingComplete() {
        val loadedCount = loadedAdList.size
        if (loadedCount == 0) {
            Utils.log(this, "onAdFailToLoad", true)

            listener?.onAdFailToLoad()
        } else {
            Utils.log(this, "onAdLoaded, count of loaded object - $loadedCount")

            listener?.onAdLoaded()
        }
    }


    open inner class Listener : BaseListener() {

        @CallSuper
        override fun onLoaded(adObject: AdObjectType) {
            super.onLoaded(adObject)

            loadPostBidAds(adObject.getPrice())
        }

        @CallSuper
        override fun onFailToLoad(adObject: AdObjectType, errorMessage: String) {
            Utils.log(this, "onFailToLoad, error - $errorMessage", true)

            loadPostBidAds(null)
        }

    }

    open inner class PostBidListener : BaseListener() {

        @CallSuper
        override fun onLoaded(adObject: AdObjectType) {
            super.onLoaded(adObject)

            onPostBidAdLoadingComplete()
        }

        @CallSuper
        override fun onFailToLoad(adObject: AdObjectType, errorMessage: String) {
            onPostBidAdLoadFail(adObject, errorMessage)
        }

    }

    abstract inner class BaseListener : AdObjectListener<AdObjectType> {

        @CallSuper
        override fun onLoaded(adObject: AdObjectType) {
            onAdObjectLoaded(adObject)
        }

        @CallSuper
        override fun onShown(adObject: AdObjectType) {
            Utils.log(this@AdWrapper, "onAdShown")

            listener?.onAdShown()
        }

        @CallSuper
        override fun onClicked(adObject: AdObjectType) {
            Utils.log(this@AdWrapper, "onAdClicked")

            listener?.onAdClicked()
        }

    }

}